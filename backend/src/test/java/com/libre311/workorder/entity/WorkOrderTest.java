package com.libre311.workorder.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkOrderTest {

    private Validator validator;
    private WorkOrder workOrder;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        workOrder = new WorkOrder();
        workOrder.setTitle("Test Work Order");
        workOrder.setDescription("Test Description");
        workOrder.setLibre311ServiceRequestId("SR-123");
        workOrder.setStatus(WorkOrder.WorkOrderStatus.PENDING);
        workOrder.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
    }

    @Test
    void testValidWorkOrder() {
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testTitleNotBlank() {
        workOrder.setTitle("");
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void testTitleNotNull() {
        workOrder.setTitle(null);
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void testDescriptionCanBeEmpty() {
        workOrder.setDescription("");
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testLibre311ServiceRequestIdNotBlank() {
        workOrder.setLibre311ServiceRequestId("");
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("libre311ServiceRequestId")));
    }

    @Test
    void testStatusCanBeNull() {
        workOrder.setStatus(null);
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPriorityCanBeNull() {
        workOrder.setPriority(null);
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPrePersistSetsCreatedAt() {
        WorkOrder newWorkOrder = new WorkOrder();
        assertNull(newWorkOrder.getCreatedAt());
        assertNull(newWorkOrder.getUpdatedAt());
        
        newWorkOrder.onCreate();
        
        assertNotNull(newWorkOrder.getCreatedAt());
        assertNotNull(newWorkOrder.getUpdatedAt());
        // They should be very close in time
        assertTrue(newWorkOrder.getCreatedAt().isBefore(newWorkOrder.getUpdatedAt()) || 
                   newWorkOrder.getCreatedAt().equals(newWorkOrder.getUpdatedAt()));
    }

    @Test
    void testPreUpdateSetsUpdatedAt() throws InterruptedException {
        workOrder.onCreate();
        java.time.LocalDateTime originalCreatedAt = workOrder.getCreatedAt();
        java.time.LocalDateTime originalUpdatedAt = workOrder.getUpdatedAt();
        
        Thread.sleep(1);
        workOrder.onUpdate();
        
        assertEquals(originalCreatedAt, workOrder.getCreatedAt());
        assertTrue(workOrder.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void testAllStatusValues() {
        assertDoesNotThrow(() -> {
            workOrder.setStatus(WorkOrder.WorkOrderStatus.PENDING);
            workOrder.setStatus(WorkOrder.WorkOrderStatus.ASSIGNED);
            workOrder.setStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS);
            workOrder.setStatus(WorkOrder.WorkOrderStatus.COMPLETED);
            workOrder.setStatus(WorkOrder.WorkOrderStatus.CANCELLED);
        });
    }

    @Test
    void testAllPriorityValues() {
        assertDoesNotThrow(() -> {
            workOrder.setPriority(WorkOrder.WorkOrderPriority.LOW);
            workOrder.setPriority(WorkOrder.WorkOrderPriority.MEDIUM);
            workOrder.setPriority(WorkOrder.WorkOrderPriority.HIGH);
            workOrder.setPriority(WorkOrder.WorkOrderPriority.URGENT);
        });
    }

    @Test
    void testAssignedToCanBeNull() {
        workOrder.setAssignedTo(null);
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAssignedToCanBeSet() {
        workOrder.setAssignedTo("john.doe");
        Set<ConstraintViolation<WorkOrder>> violations = validator.validate(workOrder);
        assertTrue(violations.isEmpty());
        assertEquals("john.doe", workOrder.getAssignedTo());
    }
}