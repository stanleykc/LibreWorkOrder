package com.libre311.workorder.service;

import com.libre311.workorder.dto.CreateWorkOrderRequest;
import com.libre311.workorder.dto.UpdateWorkOrderRequest;
import com.libre311.workorder.entity.WorkOrder;
import com.libre311.workorder.repository.WorkOrderRepository;
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
class WorkOrderServiceTest {

    @Inject
    private WorkOrderService workOrderService;

    @Mock
    private WorkOrderRepository workOrderRepository;

    private WorkOrder workOrder;

    @MockBean(WorkOrderRepository.class)
    WorkOrderRepository workOrderRepository() {
        return mock(WorkOrderRepository.class);
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
        when(workOrderRepository.save(any(WorkOrder.class))).thenReturn(workOrder);

        WorkOrder result = workOrderService.createWorkOrder(any(CreateWorkOrderRequest.class));

        assertNotNull(result);
        assertEquals(workOrder.getTitle(), result.getTitle());
        verify(workOrderRepository, times(1)).save(workOrder);
    }

    @Test
    void testFindAllWorkOrders() {
        List<WorkOrder> workOrders = Arrays.asList(workOrder);
        when(workOrderRepository.findAll()).thenReturn(workOrders);

        List<WorkOrder> result = workOrderService.getAllWorkOrders();

        assertEquals(1, result.size());
        assertEquals(workOrder.getId(), result.get(0).getId());
        verify(workOrderRepository, times(1)).findAll();
    }

    @Test
    void testFindWorkOrderById() {
        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(workOrder));

        Optional<WorkOrder> result = workOrderService.getWorkOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(workOrder.getId(), result.get().getId());
        verify(workOrderRepository, times(1)).findById(1L);
    }

    @Test
    void testFindWorkOrderByIdNotFound() {
        when(workOrderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<WorkOrder> result = workOrderService.getWorkOrderById(999L);

        assertFalse(result.isPresent());
        verify(workOrderRepository, times(1)).findById(999L);
    }

    @Test
    void testFindWorkOrderByLibre311ServiceRequestId() {
        when(workOrderRepository.findByLibre311ServiceRequestId("SR-123")).thenReturn(Optional.of(workOrder));

        Optional<WorkOrder> result = workOrderService.getWorkOrderByLibre311ServiceRequestId("SR-123");

        assertTrue(result.isPresent());
        assertEquals(workOrder.getLibre311ServiceRequestId(), result.get().getLibre311ServiceRequestId());
        verify(workOrderRepository, times(1)).findByLibre311ServiceRequestId("SR-123");
    }

    @Test
    void testFindWorkOrdersByStatus() {
        List<WorkOrder> workOrders = Arrays.asList(workOrder);
        when(workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.PENDING)).thenReturn(workOrders);

        List<WorkOrder> result = workOrderService.getWorkOrdersByStatus(WorkOrder.WorkOrderStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(WorkOrder.WorkOrderStatus.PENDING, result.get(0).getStatus());
        verify(workOrderRepository, times(1)).findByStatus(WorkOrder.WorkOrderStatus.PENDING);
    }

    @Test
    void testFindWorkOrdersByAssignedTo() {
        List<WorkOrder> workOrders = Arrays.asList(workOrder);
        when(workOrderRepository.findByAssignedTo("john.doe")).thenReturn(workOrders);

        List<WorkOrder> result = workOrderService.getWorkOrdersByAssignedTo("john.doe");

        assertEquals(1, result.size());
        assertEquals("john.doe", result.get(0).getAssignedTo());
        verify(workOrderRepository, times(1)).findByAssignedTo("john.doe");
    }

    @Test
    void testUpdateWorkOrder() {
        UpdateWorkOrderRequest updateRequest = new UpdateWorkOrderRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        updateRequest.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        updateRequest.setAssignedTo("jane.smith");

        WorkOrder updatedWorkOrder = new WorkOrder();
        updatedWorkOrder.setId(1L);
        updatedWorkOrder.setTitle("Updated Title");
        updatedWorkOrder.setDescription("Updated Description");
        updatedWorkOrder.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        updatedWorkOrder.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        updatedWorkOrder.setAssignedTo("jane.smith");

        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.update(any(WorkOrder.class))).thenReturn(updatedWorkOrder);

        Optional<WorkOrder> result = workOrderService.updateWorkOrder(1L, updateRequest);

        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
        assertEquals("Updated Description", result.get().getDescription());
        assertEquals(WorkOrder.WorkOrderStatus.IN_PROGRESS, result.get().getStatus());
        assertEquals(WorkOrder.WorkOrderPriority.HIGH, result.get().getPriority());
        assertEquals("jane.smith", result.get().getAssignedTo());
        verify(workOrderRepository, times(1)).findById(1L);
        verify(workOrderRepository, times(1)).update(any(WorkOrder.class));
    }

    @Test
    void testUpdateWorkOrderNotFound() {
        UpdateWorkOrderRequest updateRequest = new UpdateWorkOrderRequest();
        when(workOrderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<WorkOrder> result = workOrderService.updateWorkOrder(999L, updateRequest);

        assertFalse(result.isPresent());
        verify(workOrderRepository, times(1)).findById(999L);
        verify(workOrderRepository, never()).update(any(WorkOrder.class));
    }

    @Test
    void testUpdateWorkOrderPartialUpdate() {
        UpdateWorkOrderRequest partialUpdate = new UpdateWorkOrderRequest();
        partialUpdate.setTitle("New Title");
        // Other fields are null

        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.update(any(WorkOrder.class))).thenAnswer(invocation -> {
            WorkOrder updated = invocation.getArgument(0);
            return updated;
        });

        Optional<WorkOrder> result = workOrderService.updateWorkOrder(1L, partialUpdate);

        assertTrue(result.isPresent());
        assertEquals("New Title", result.get().getTitle());
        assertEquals("Test Description", result.get().getDescription()); // Should keep original
        assertEquals(WorkOrder.WorkOrderStatus.PENDING, result.get().getStatus()); // Should keep original
        verify(workOrderRepository, times(1)).update(any(WorkOrder.class));
    }

    @Test
    void testAssignWorkOrder() {
        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.update(any(WorkOrder.class))).thenAnswer(invocation -> {
            WorkOrder updated = invocation.getArgument(0);
            return updated;
        });

        Optional<WorkOrder> result = workOrderService.assignWorkOrder(1L, "jane.smith");

        assertTrue(result.isPresent());
        assertEquals("jane.smith", result.get().getAssignedTo());
        assertEquals(WorkOrder.WorkOrderStatus.ASSIGNED, result.get().getStatus());
        verify(workOrderRepository, times(1)).findById(1L);
        verify(workOrderRepository, times(1)).update(any(WorkOrder.class));
    }

    @Test
    void testAssignWorkOrderNotFound() {
        when(workOrderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<WorkOrder> result = workOrderService.assignWorkOrder(999L, "jane.smith");

        assertFalse(result.isPresent());
        verify(workOrderRepository, times(1)).findById(999L);
        verify(workOrderRepository, never()).update(any(WorkOrder.class));
    }

    @Test
    void testUpdateWorkOrderStatus() {
        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(workOrder));
        when(workOrderRepository.update(any(WorkOrder.class))).thenAnswer(invocation -> {
            WorkOrder updated = invocation.getArgument(0);
            return updated;
        });

        Optional<WorkOrder> result = workOrderService.updateWorkOrderStatus(1L, WorkOrder.WorkOrderStatus.COMPLETED);

        assertTrue(result.isPresent());
        assertEquals(WorkOrder.WorkOrderStatus.COMPLETED, result.get().getStatus());
        verify(workOrderRepository, times(1)).findById(1L);
        verify(workOrderRepository, times(1)).update(any(WorkOrder.class));
    }

    @Test
    void testUpdateWorkOrderStatusNotFound() {
        when(workOrderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<WorkOrder> result = workOrderService.updateWorkOrderStatus(999L, WorkOrder.WorkOrderStatus.COMPLETED);

        assertFalse(result.isPresent());
        verify(workOrderRepository, times(1)).findById(999L);
        verify(workOrderRepository, never()).update(any(WorkOrder.class));
    }

    @Test
    void testDeleteWorkOrder() {
        when(workOrderRepository.findById(1L)).thenReturn(Optional.of(workOrder));
        doNothing().when(workOrderRepository).deleteById(1L);

        boolean result = workOrderService.deleteWorkOrder(1L);

        assertTrue(result);
        verify(workOrderRepository, times(1)).findById(1L);
        verify(workOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteWorkOrderNotFound() {
        when(workOrderRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = workOrderService.deleteWorkOrder(999L);

        assertFalse(result);
        verify(workOrderRepository, times(1)).findById(999L);
        verify(workOrderRepository, never()).deleteById(anyLong());
    }
}