package com.libre311.workorder.dto;

import com.libre311.workorder.entity.WorkOrder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DTOValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreateWorkOrderRequestValid() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testCreateWorkOrderRequestTitleNotBlank() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void testCreateWorkOrderRequestTitleNotNull() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle(null);
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void testCreateWorkOrderRequestDescriptionCanBeNull() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription(null);
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testCreateWorkOrderRequestLibre311ServiceRequestIdNotBlank() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("libre311ServiceRequestId")));
    }

    @Test
    void testCreateWorkOrderRequestPriorityCanBeNull() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(null);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testCreateWorkOrderRequestAssignedToCanBeNull() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        request.setAssignedTo(null);

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testCreateWorkOrderRequestAssignedToCanBeSet() {
        CreateWorkOrderRequest request = new CreateWorkOrderRequest();
        request.setTitle("Valid Title");
        request.setDescription("Valid Description");
        request.setLibre311ServiceRequestId("SR-123");
        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        request.setAssignedTo("john.doe");

        Set<ConstraintViolation<CreateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestValid() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        request.setAssignedTo("jane.smith");

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestAllFieldsNull() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        // All fields are null - should be valid for partial updates

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestTitleCanBeNull() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle(null);
        request.setDescription("Updated Description");
        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestTitleCanBeEmpty() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("");
        request.setDescription("Updated Description");

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestDescriptionCanBeEmpty() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("");

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestStatusCanBeNull() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(null);
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestPriorityCanBeNull() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        request.setPriority(null);

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestAssignedToCanBeNull() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        request.setAssignedTo(null);

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestAssignedToCanBeSet() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        request.setAssignedTo("jane.smith");

        Set<ConstraintViolation<UpdateWorkOrderRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestWithAllPriorityValues() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        // Test all priority values
        request.setPriority(WorkOrder.WorkOrderPriority.LOW);
        assertTrue(validator.validate(request).isEmpty());

        request.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        assertTrue(validator.validate(request).isEmpty());

        request.setPriority(WorkOrder.WorkOrderPriority.HIGH);
        assertTrue(validator.validate(request).isEmpty());

        request.setPriority(WorkOrder.WorkOrderPriority.URGENT);
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void testUpdateWorkOrderRequestWithAllStatusValues() {
        UpdateWorkOrderRequest request = new UpdateWorkOrderRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        // Test all status values
        request.setStatus(WorkOrder.WorkOrderStatus.PENDING);
        assertTrue(validator.validate(request).isEmpty());

        request.setStatus(WorkOrder.WorkOrderStatus.ASSIGNED);
        assertTrue(validator.validate(request).isEmpty());

        request.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
        assertTrue(validator.validate(request).isEmpty());

        request.setStatus(WorkOrder.WorkOrderStatus.COMPLETED);
        assertTrue(validator.validate(request).isEmpty());

        request.setStatus(WorkOrder.WorkOrderStatus.CANCELLED);
        assertTrue(validator.validate(request).isEmpty());
    }
}