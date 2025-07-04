package com.libre311.workorder.error;

import com.libre311.workorder.BaseTestContainer;
import com.libre311.workorder.dto.CreateWorkOrderRequest;
import com.libre311.workorder.dto.UpdateWorkOrderRequest;
import com.libre311.workorder.entity.WorkOrder;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ErrorHandlingTest extends BaseTestContainer {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testCreateWorkOrderWithMissingRequiredFields() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        // Missing all required fields

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", request),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateWorkOrderWithEmptyTitle() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", request),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateWorkOrderWithEmptyDescription() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", request),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateWorkOrderWithEmptyLibre311ServiceRequestId() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", request),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testCreateWorkOrderWithNullPriority() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(null);

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", request),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testGetWorkOrderWithNonExistentId() {
        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/999999"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testGetWorkOrderWithInvalidId() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.GET("/api/work-orders/invalid-id"),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testGetWorkOrderByNonExistentLibre311ServiceRequestId() {
        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.GET("/api/work-orders/libre311/NON-EXISTENT-SR"),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testGetWorkOrdersByInvalidStatus() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.GET("/api/work-orders/status/INVALID_STATUS"),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testUpdateNonExistentWorkOrder() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/999999", request),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testUpdateWorkOrderWithEmptyTitle() {
        // First create a work order
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Original Title");
        createRequest.setDescription("Original Description");
        createRequest.setLibre311ServiceRequestId("SR-ERR-001");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        WorkOrder createdWorkOrder = createResponse.getBody().get();

        // Try to update with empty title
        UpdateWorkOrderRequest updateRequest = new UpdateWorkOrderRequest();
        updateRequest.setTitle("");
        updateRequest.setDescription("Updated Description");

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId(), updateRequest),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testUpdateWorkOrderWithEmptyDescription() {
        // First create a work order
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Original Title");
        createRequest.setDescription("Original Description");
        createRequest.setLibre311ServiceRequestId("SR-ERR-002");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        WorkOrder createdWorkOrder = createResponse.getBody().get();

        // Try to update with empty description
        UpdateWorkOrderRequest updateRequest = new UpdateWorkOrderRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("");

        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId(), updateRequest),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testAssignNonExistentWorkOrder() {
        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/999999/assign?assignedTo=john.doe", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testAssignWorkOrderWithMissingAssignedToParameter() {
        // First create a work order
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Test Work Order");
        createRequest.setDescription("Test Description");
        createRequest.setLibre311ServiceRequestId("SR-ERR-003");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        WorkOrder createdWorkOrder = createResponse.getBody().get();

        // Try to assign without assignedTo parameter
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId() + "/assign", null),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testUpdateStatusOfNonExistentWorkOrder() {
        HttpResponse<WorkOrder> response = client.toBlocking().exchange(
                HttpRequest.PUT("/api/work-orders/999999/status?status=COMPLETED", null),
                WorkOrder.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testUpdateStatusWithInvalidStatus() {
        // First create a work order
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Test Work Order");
        createRequest.setDescription("Test Description");
        createRequest.setLibre311ServiceRequestId("SR-ERR-004");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        WorkOrder createdWorkOrder = createResponse.getBody().get();

        // Try to update with invalid status
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId() + "/status?status=INVALID_STATUS", null),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testUpdateStatusWithMissingStatusParameter() {
        // First create a work order
        CreateWorkOrderRequest createRequest = new CreateWorkOrderRequest();
        createRequest.setTitle("Test Work Order");
        createRequest.setDescription("Test Description");
        createRequest.setLibre311ServiceRequestId("SR-ERR-005");
        createRequest.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        HttpResponse<WorkOrder> createResponse = client.toBlocking().exchange(
                HttpRequest.POST("/api/work-orders", createRequest),
                WorkOrder.class
        );

        WorkOrder createdWorkOrder = createResponse.getBody().get();

        // Try to update status without status parameter
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.PUT("/api/work-orders/" + createdWorkOrder.getId() + "/status", null),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testDeleteNonExistentWorkOrder() {
        HttpResponse<Void> response = client.toBlocking().exchange(
                HttpRequest.DELETE("/api/work-orders/999999"),
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @Test
    void testInvalidHttpMethod() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.PATCH("/api/work-orders/1", null),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, exception.getStatus());
    }

    @Test
    void testInvalidContentType() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", "invalid-json-content")
                                .contentType("text/plain"),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exception.getStatus());
    }

    @Test
    void testMalformedJsonRequest() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.POST("/api/work-orders", "{invalid json}")
                                .contentType("application/json"),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testInvalidUrlPath() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> client.toBlocking().exchange(
                        HttpRequest.GET("/api/work-orders/invalid/path/structure"),
                        WorkOrder.class
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}