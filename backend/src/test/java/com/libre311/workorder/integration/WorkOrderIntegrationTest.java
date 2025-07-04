package com.libre311.workorder.integration;

import com.libre311.workorder.BaseTestContainer;
import com.libre311.workorder.dto.CreateWorkOrderRequest;
import com.libre311.workorder.dto.UpdateWorkOrderRequest;
import com.libre311.workorder.entity.WorkOrder;
import com.libre311.workorder.repository.WorkOrderRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkOrderIntegrationTest extends BaseTestContainer {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    WorkOrderRepository workOrderRepository;

    @BeforeEach
    void setUp() {
        // Clear existing data - delete all individually since deleteAll() doesn't exist
        workOrderRepository.findAll().forEach(wo -> workOrderRepository.deleteById(wo.getId()));
    }

    @Test
    void testCompleteWorkOrderLifecycle() {
        // 1. Create a work order
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Integration Test Work Order");
        createRequest.setDescription("This is an integration test");
        createRequest.setLibre311ServiceRequestId("SR-INT-001");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.HIGH);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatus());
        WorkOrder createdWorkOrder = createResponse.getBody().get();
        assertNotNull(createdWorkOrder.getId());
        assertEquals("Integration Test Work Order", createdWorkOrder.getTitle());
        assertEquals(WorkOrder.WorkOrderStatus.PENDING, createdWorkOrder.getStatus());
        assertEquals(WorkOrder.WorkOrderPriority.HIGH, createdWorkOrder.getPriority());

        // 2. Get the work order by ID
        HttpResponse<WorkOrder> getResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/" + createdWorkOrder.getId()),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatus());
        WorkOrder retrievedWorkOrder = getResponse.getBody().get();
        assertEquals(createdWorkOrder.getId(), retrievedWorkOrder.getId());
        assertEquals(createdWorkOrder.getTitle(), retrievedWorkOrder.getTitle());

        // 3. Update the work order
        UpdateWorkOrderRequest updateRequest = new UpdateWorkOrderRequest();
        updateRequest.setTitle("Updated Integration Test Work Order");
        updateRequest.setDescription("Updated description");
        updateRequest.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        updateRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpResponse<WorkOrder> updateResponse = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId(), updateRequest),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatus());
        WorkOrder updatedWorkOrder = updateResponse.getBody().get();
        assertEquals("Updated Integration Test Work Order", updatedWorkOrder.getTitle());
        assertEquals("Updated description", updatedWorkOrder.getDescription());
        assertEquals(WorkOrder.WorkOrderStatus.IN_PROGRESS, updatedWorkOrder.getStatus());
        assertEquals(WorkOrder.WorkOrderPriority.MEDIUM, updatedWorkOrder.getPriority());

        // 4. Assign the work order
        HttpResponse<WorkOrder> assignResponse = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId() + "/assign?assignedTo=integration.tester", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, assignResponse.getStatus());
        WorkOrder assignedWorkOrder = assignResponse.getBody().get();
        assertEquals("integration.tester", assignedWorkOrder.getAssignedTo());
        assertEquals(WorkOrder.WorkOrderStatus.ASSIGNED, assignedWorkOrder.getStatus());

        // 5. Update status to completed
        HttpResponse<WorkOrder> statusResponse = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId() + "/status?status=COMPLETED", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, statusResponse.getStatus());
        WorkOrder completedWorkOrder = statusResponse.getBody().get();
        assertEquals(WorkOrder.WorkOrderStatus.COMPLETED, completedWorkOrder.getStatus());

        // 6. Get by Libre311 service request ID
        HttpResponse<WorkOrder> libre311Response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/libre311/SR-INT-001"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.OK, libre311Response.getStatus());
        WorkOrder libre311WorkOrder = libre311Response.getBody().get();
        assertEquals(createdWorkOrder.getId(), libre311WorkOrder.getId());
        assertEquals("SR-INT-001", libre311WorkOrder.getLibre311ServiceRequestId());

        // 7. Delete the work order
        HttpResponse<Void> deleteResponse = client.toBlocking().exchange(
                HttpRequest.DELETE("/api/work-orders/" + createdWorkOrder.getId()),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatus());

        // 8. Verify deletion
        HttpResponse<WorkOrder> verifyDeleteResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/" + createdWorkOrder.getId()),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, verifyDeleteResponse.getStatus());
    }

    @Test
    void testMultipleWorkOrdersAndFiltering() {
        // Create multiple work orders
        CreateWorkOrderRequest request1 = new CreateWorkOrderRequest();
        request1.setTitle("Work Order 1");
        request1.setDescription("Description 1");
        request1.setLibre311ServiceRequestId("SR-001");
        request1.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        request1.setAssignedTo("john.doe");

        CreateWorkOrderRequest request2 = new CreateWorkOrderRequest();
        request2.setTitle("Work Order 2");
        request2.setDescription("Description 2");
        request2.setLibre311ServiceRequestId("SR-002");
        request2.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        request2.setAssignedTo("jane.smith");

        CreateWorkOrderRequest request3 = new CreateWorkOrderRequest();
        request3.setTitle("Work Order 3");
        request3.setDescription("Description 3");
        request3.setLibre311ServiceRequestId("SR-003");
        request3.setPriority(WorkOrder.WorkOrderPriority.LOW);
        request3.setAssignedTo("john.doe");

        // Create work orders
        HttpResponse<WorkOrder> response1 = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", request1), WorkOrder.class);
        HttpResponse<WorkOrder> response2 = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", request2), WorkOrder.class);
        HttpResponse<WorkOrder> response3 = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", request3), WorkOrder.class);

        WorkOrder workOrder1 = response1.getBody().get();
        WorkOrder workOrder2 = response2.getBody().get();
        WorkOrder workOrder3 = response3.getBody().get();

        // Test get all work orders
        HttpResponse<List> getAllResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders"), List.class);
        assertEquals(HttpStatus.OK, getAllResponse.getStatus());
        List<?> allWorkOrders = getAllResponse.getBody().get();
        assertEquals(3, allWorkOrders.size());

        // Test get by status (all should be PENDING initially)
        HttpResponse<List> statusResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/status/PENDING"), List.class);
        assertEquals(HttpStatus.OK, statusResponse.getStatus());
        List<?> pendingWorkOrders = statusResponse.getBody().get();
        assertEquals(3, pendingWorkOrders.size());

        // Test get by assigned user
        HttpResponse<List> johnResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/assigned/john.doe"), List.class);
        assertEquals(HttpStatus.OK, johnResponse.getStatus());
        List<?> johnWorkOrders = johnResponse.getBody().get();
        assertEquals(2, johnWorkOrders.size());

        HttpResponse<List> janeResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/assigned/jane.smith"), List.class);
        assertEquals(HttpStatus.OK, janeResponse.getStatus());
        List<?> janeWorkOrders = janeResponse.getBody().get();
        assertEquals(1, janeWorkOrders.size());

        // Update one work order status and test filtering
        client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/" + workOrder1.getId() + "/status?status=IN_PROGRESS", null),
                WorkOrder.class);

        // Test get by status again
        HttpResponse<List> pendingResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/status/PENDING"), List.class);
        assertEquals(HttpStatus.OK, pendingResponse.getStatus());
        List<?> stillPendingWorkOrders = pendingResponse.getBody().get();
        assertEquals(2, stillPendingWorkOrders.size());

        HttpResponse<List> inProgressResponse = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/status/IN_PROGRESS"), List.class);
        assertEquals(HttpStatus.OK, inProgressResponse.getStatus());
        List<?> inProgressWorkOrders = inProgressResponse.getBody().get();
        assertEquals(1, inProgressWorkOrders.size());
    }

    @Test
    void testDatabasePersistence() {
        // Create work order via API
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Database Persistence Test");
        createRequest.setDescription("Testing database persistence");
        createRequest.setLibre311ServiceRequestId("SR-DB-001");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.URGENT);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        WorkOrder createdWorkOrder = createResponse.getBody().get();

        // Verify it exists in database through repository
        var dbWorkOrder = workOrderRepository.findById(createdWorkOrder.getId());
        assertTrue(dbWorkOrder.isPresent());
        assertEquals("Database Persistence Test", dbWorkOrder.get().getTitle());
        assertEquals("Testing database persistence", dbWorkOrder.get().getDescription());
        assertEquals("SR-DB-001", dbWorkOrder.get().getLibre311ServiceRequestId());
        assertEquals(WorkOrder.WorkOrderPriority.URGENT, dbWorkOrder.get().getPriority());
        assertEquals(WorkOrder.WorkOrderStatus.PENDING, dbWorkOrder.get().getStatus());
        assertNotNull(dbWorkOrder.get().getCreatedAt());
        assertNotNull(dbWorkOrder.get().getUpdatedAt());

        // Update via API
        UpdateWorkOrderRequest updateRequest = new UpdateWorkOrderRequest();
        updateRequest.setTitle("Updated Database Persistence Test");
        updateRequest.setStatus(WorkOrder.WorkOrderStatus.COMPLETED);

        client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId(), updateRequest),
                WorkOrder.class
        );

        // Verify update persisted in database
        var updatedDbWorkOrder = workOrderRepository.findById(createdWorkOrder.getId());
        assertTrue(updatedDbWorkOrder.isPresent());
        assertEquals("Updated Database Persistence Test", updatedDbWorkOrder.get().getTitle());
        assertEquals(WorkOrder.WorkOrderStatus.COMPLETED, updatedDbWorkOrder.get().getStatus());
        assertTrue(updatedDbWorkOrder.get().getUpdatedAt().isAfter(updatedDbWorkOrder.get().getCreatedAt()));
    }

    @Test
    void testRepositoryCustomQueries() {
        // Create test data
        CreateWorkOrderRequest highPriorityRequest = new CreateWorkOrderRequest();
        highPriorityRequest.setTitle("High Priority Task");
        highPriorityRequest.setDescription("High priority description");
        highPriorityRequest.setLibre311ServiceRequestId("SR-HIGH-001");
        highPriorityRequest.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        highPriorityRequest.setAssignedTo("developer1");

        CreateWorkOrderRequest mediumPriorityRequest = new CreateWorkOrderRequest();
        mediumPriorityRequest.setTitle("Medium Priority Task");
        mediumPriorityRequest.setDescription("Medium priority description");
        mediumPriorityRequest.setLibre311ServiceRequestId("SR-MED-001");
        mediumPriorityRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        mediumPriorityRequest.setAssignedTo("developer2");

        // Create work orders
        HttpResponse<WorkOrder> highResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", highPriorityRequest), WorkOrder.class);
        HttpResponse<WorkOrder> mediumResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", mediumPriorityRequest), WorkOrder.class);

        WorkOrder highWorkOrder = highResponse.getBody().get();
        WorkOrder mediumWorkOrder = mediumResponse.getBody().get();

        // Test repository queries directly
        var highPriorityOrders = workOrderRepository.findByPriority(WorkOrder.WorkOrderPriority.HIGH);
        assertEquals(1, highPriorityOrders.size());
        assertEquals(highWorkOrder.getId(), highPriorityOrders.get(0).getId());

        var mediumPriorityOrders = workOrderRepository.findByPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        assertEquals(1, mediumPriorityOrders.size());
        assertEquals(mediumWorkOrder.getId(), mediumPriorityOrders.get(0).getId());

        var dev1Orders = workOrderRepository.findByAssignedTo("developer1");
        assertEquals(1, dev1Orders.size());
        assertEquals(highWorkOrder.getId(), dev1Orders.get(0).getId());

        var dev2Orders = workOrderRepository.findByAssignedTo("developer2");
        assertEquals(1, dev2Orders.size());
        assertEquals(mediumWorkOrder.getId(), dev2Orders.get(0).getId());

        // Test combined queries
        var pendingDev1Orders = workOrderRepository.findByStatusAndAssignedTo(
                WorkOrder.WorkOrderStatus.PENDING, "developer1");
        assertEquals(1, pendingDev1Orders.size());
        assertEquals(highWorkOrder.getId(), pendingDev1Orders.get(0).getId());

        var completedDev1Orders = workOrderRepository.findByStatusAndAssignedTo(
                WorkOrder.WorkOrderStatus.COMPLETED, "developer1");
        assertEquals(0, completedDev1Orders.size());
    }
}