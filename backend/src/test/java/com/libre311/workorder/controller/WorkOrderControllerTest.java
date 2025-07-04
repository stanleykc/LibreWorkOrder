package com.libre311.workorder.controller;

import com.libre311.workorder.dto.CreateWorkOrderRequest;
import com.libre311.workorder.dto.UpdateWorkOrderRequest;
import com.libre311.workorder.entity.WorkOrder;
import com.libre311.workorder.service.WorkOrderService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@MicronautTest
class WorkOrderControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Mock
    private WorkOrderService workOrderService;

    private WorkOrder workOrder;

    @MockBean(WorkOrderService.class)
    WorkOrderService workOrderService() {
        return mock(WorkOrderService.class);
    }

    @BeforeEach
    void setUp() {
        workOrder = new WorkOrder();
        workOrder.setId(1L);
        workOrder.setTitle("Test Work Order");
        workOrder.setDescription("Test Description");
        workOrder.setLibre311ServiceRequestId("SR-123");
        workOrder.setStatus(WorkOrder.WorkOrderStatus.PENDING);
        workOrder.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        workOrder.setAssignedTo("john.doe");
    }

    @Test
    void testCreateWorkOrder() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("New Work Order");
        request.setDescription("New Description");
        request.setLibre311ServiceRequestId("SR-456");
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);

        when(workOrderService.createWorkOrder(any(CreateWorkOrderRequest.class))).thenReturn(workOrder);

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", request),
                WorkOrder.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(workOrder.getId(), response.getBody().get().getId());
        verify(workOrderService, times(1)).createWorkOrder(any(CreateWorkOrderRequest.class));
    }

    @Test
    void testGetAllWorkOrders() {
        List<WorkOrder> workOrders = Arrays.asList(workOrder);
        when(workOrderService.getAllWorkOrders()).thenReturn(workOrders);

        HttpResponse<List> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders"),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(1, response.getBody().get().size());
        verify(workOrderService, times(1)).getAllWorkOrders();
    }

    @Test
    void testGetWorkOrderById() {
        when(workOrderService.getWorkOrderById(1L)).thenReturn(Optional.of(workOrder));

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/1"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(workOrder.getId(), response.getBody().get().getId());
        verify(workOrderService, times(1)).getWorkOrderById(1L);
    }

    @Test
    void testGetWorkOrderByIdNotFound() {
        when(workOrderService.getWorkOrderById(999L)).thenReturn(Optional.empty());

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/999"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        verify(workOrderService, times(1)).getWorkOrderById(999L);
    }

    @Test
    void testGetWorkOrderByLibre311ServiceRequestId() {
        when(workOrderService.getWorkOrderByLibre311ServiceRequestId("SR-123")).thenReturn(Optional.of(workOrder));

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/libre311/SR-123"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(workOrder.getLibre311ServiceRequestId(), response.getBody().get().getLibre311ServiceRequestId());
        verify(workOrderService, times(1)).getWorkOrderByLibre311ServiceRequestId("SR-123");
    }

    @Test
    void testGetWorkOrderByLibre311ServiceRequestIdNotFound() {
        when(workOrderService.getWorkOrderByLibre311ServiceRequestId("SR-999")).thenReturn(Optional.empty());

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/libre311/SR-999"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        verify(workOrderService, times(1)).getWorkOrderByLibre311ServiceRequestId("SR-999");
    }

    @Test
    void testGetWorkOrdersByStatus() {
        List<WorkOrder> workOrders = Arrays.asList(workOrder);
        when(workOrderService.getWorkOrdersByStatus(WorkOrder.WorkOrderStatus.PENDING)).thenReturn(workOrders);

        HttpResponse<List> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/status/PENDING"),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(1, response.getBody().get().size());
        verify(workOrderService, times(1)).getWorkOrdersByStatus(WorkOrder.WorkOrderStatus.PENDING);
    }

    @Test
    void testGetWorkOrdersByAssignedTo() {
        List<WorkOrder> workOrders = Arrays.asList(workOrder);
        when(workOrderService.getWorkOrdersByAssignedTo("john.doe")).thenReturn(workOrders);

        HttpResponse<List> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/assigned/john.doe"),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(1, response.getBody().get().size());
        verify(workOrderService, times(1)).getWorkOrdersByAssignedTo("john.doe");
    }

    @Test
    void testUpdateWorkOrder() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        request.setAssignedTo("jane.smith");

        WorkOrder updatedWorkOrder = new WorkOrder();
        updatedWorkOrder.setId(1L);
        updatedWorkOrder.setTitle("Updated Title");
        updatedWorkOrder.setDescription("Updated Description");
        updatedWorkOrder.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        updatedWorkOrder.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        updatedWorkOrder.setAssignedTo("jane.smith");

        when(workOrderService.updateWorkOrder(anyLong(), any(UpdateWorkOrderRequest.class))).thenReturn(Optional.of(updatedWorkOrder));

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/1", request),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals("Updated Title", response.getBody().get().getTitle());
        verify(workOrderService, times(1)).updateWorkOrder(anyLong(), any(UpdateWorkOrderRequest.class));
    }

    @Test
    void testUpdateWorkOrderNotFound() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");

        when(workOrderService.updateWorkOrder(anyLong(), any(UpdateWorkOrderRequest.class))).thenReturn(Optional.empty());

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/999", request),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        verify(workOrderService, times(1)).updateWorkOrder(anyLong(), any(UpdateWorkOrderRequest.class));
    }

    @Test
    void testAssignWorkOrder() {
        WorkOrder assignedWorkOrder = new WorkOrder();
        assignedWorkOrder.setId(1L);
        assignedWorkOrder.setAssignedTo("jane.smith");
        assignedWorkOrder.setStatus(WorkOrder.WorkOrderStatus.ASSIGNED);

        when(workOrderService.assignWorkOrder(1L, "jane.smith")).thenReturn(Optional.of(assignedWorkOrder));

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/1/assign?assignedTo=jane.smith", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals("jane.smith", response.getBody().get().getAssignedTo());
        verify(workOrderService, times(1)).assignWorkOrder(1L, "jane.smith");
    }

    @Test
    void testAssignWorkOrderNotFound() {
        when(workOrderService.assignWorkOrder(999L, "jane.smith")).thenReturn(Optional.empty());

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/999/assign?assignedTo=jane.smith", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        verify(workOrderService, times(1)).assignWorkOrder(999L, "jane.smith");
    }

    @Test
    void testUpdateWorkOrderStatus() {
        WorkOrder updatedWorkOrder = new WorkOrder();
        updatedWorkOrder.setId(1L);
        updatedWorkOrder.setStatus(WorkOrder.WorkOrderStatus.COMPLETED);

        when(workOrderService.updateWorkOrderStatus(1L, WorkOrder.WorkOrderStatus.COMPLETED)).thenReturn(Optional.of(updatedWorkOrder));

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/1/status?status=COMPLETED", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, response.getStatus());
        assertNotNull(response.getBody().get());
        assertEquals(WorkOrder.WorkOrderStatus.COMPLETED, response.getBody().get().getStatus());
        verify(workOrderService, times(1)).updateWorkOrderStatus(1L, WorkOrder.WorkOrderStatus.COMPLETED);
    }

    @Test
    void testUpdateWorkOrderStatusNotFound() {
        when(workOrderService.updateWorkOrderStatus(999L, WorkOrder.WorkOrderStatus.COMPLETED)).thenReturn(Optional.empty());

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/999/status?status=COMPLETED", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        verify(workOrderService, times(1)).updateWorkOrderStatus(999L, WorkOrder.WorkOrderStatus.COMPLETED);
    }

    @Test
    void testDeleteWorkOrder() {
        when(workOrderService.deleteWorkOrder(1L)).thenReturn(true);

        HttpResponse<Void> response = client.toBlocking().exchange(
                HttpRequest.DELETE("/api/work-orders/1"),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        verify(workOrderService, times(1)).deleteWorkOrder(1L);
    }

    @Test
    void testDeleteWorkOrderNotFound() {
        when(workOrderService.deleteWorkOrder(999L)).thenReturn(false);

        HttpResponse<Void> response = client.toBlocking().exchange(
                HttpRequest.DELETE("/api/work-orders/999"),
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
        verify(workOrderService, times(1)).deleteWorkOrder(999L);
    }
}