package com.libre311.workorder.repository;

import com.libre311.workorder.BaseTestContainer;
import com.libre311.workorder.entity.WorkOrder;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkOrderRepositoryTest extends BaseTestContainer {

    @Inject
    private WorkOrderRepository workOrderRepository;

    private WorkOrder workOrder1;
    private WorkOrder workOrder2;
    private WorkOrder workOrder3;

    @BeforeEach
    void setUp() {
        // Clear existing data - delete all individually since deleteAll() doesn't exist
        workOrderRepository.findAll().forEach(wo -> workOrderRepository.deleteById(wo.getId()));

        workOrder1 = createWorkOrder("Work Order 1", "Description 1", "SR-001", 
                                   WorkOrder.WorkOrderStatus.PENDING, WorkOrder.WorkOrderPriority.HIGH, "john.doe");
        workOrder2 = createWorkOrder("Work Order 2", "Description 2", "SR-002", 
                                   WorkOrder.WorkOrderStatus.ASSIGNED, WorkOrder.WorkOrderPriority.MEDIUM, "jane.smith");
        workOrder3 = createWorkOrder("Work Order 3", "Description 3", "SR-003", 
                                   WorkOrder.WorkOrderStatus.IN_PROGRESS, WorkOrder.WorkOrderPriority.LOW, "john.doe");

        workOrder1 = workOrderRepository.save(workOrder1);
        workOrder2 = workOrderRepository.save(workOrder2);
        workOrder3 = workOrderRepository.save(workOrder3);
    }

    @Test
    void testFindByStatus() {
        List<WorkOrder> pendingOrders = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.PENDING);
        assertEquals(1, pendingOrders.size());
        assertEquals(workOrder1.getId(), pendingOrders.get(0).getId());

        List<WorkOrder> assignedOrders = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.ASSIGNED);
        assertEquals(1, assignedOrders.size());
        assertEquals(workOrder2.getId(), assignedOrders.get(0).getId());

        List<WorkOrder> completedOrders = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.COMPLETED);
        assertTrue(completedOrders.isEmpty());
    }

    @Test
    void testFindByAssignedTo() {
        List<WorkOrder> johnOrders = workOrderRepository.findByAssignedTo("john.doe");
        assertEquals(2, johnOrders.size());
        assertTrue(johnOrders.stream().anyMatch(wo -> wo.getId().equals(workOrder1.getId())));
        assertTrue(johnOrders.stream().anyMatch(wo -> wo.getId().equals(workOrder3.getId())));

        List<WorkOrder> janeOrders = workOrderRepository.findByAssignedTo("jane.smith");
        assertEquals(1, janeOrders.size());
        assertEquals(workOrder2.getId(), janeOrders.get(0).getId());

        List<WorkOrder> nonExistentOrders = workOrderRepository.findByAssignedTo("nonexistent");
        assertTrue(nonExistentOrders.isEmpty());
    }

    @Test
    void testFindByPriority() {
        List<WorkOrder> highPriorityOrders = workOrderRepository.findByPriority(WorkOrder.WorkOrderPriority.HIGH);
        assertEquals(1, highPriorityOrders.size());
        assertEquals(workOrder1.getId(), highPriorityOrders.get(0).getId());

        List<WorkOrder> mediumPriorityOrders = workOrderRepository.findByPriority(WorkOrder.WorkOrderPriority.MEDIUM);
        assertEquals(1, mediumPriorityOrders.size());
        assertEquals(workOrder2.getId(), mediumPriorityOrders.get(0).getId());

        List<WorkOrder> urgentPriorityOrders = workOrderRepository.findByPriority(WorkOrder.WorkOrderPriority.URGENT);
        assertTrue(urgentPriorityOrders.isEmpty());
    }

    @Test
    void testFindByLibre311ServiceRequestId() {
        Optional<WorkOrder> foundOrder = workOrderRepository.findByLibre311ServiceRequestId("SR-001");
        assertTrue(foundOrder.isPresent());
        assertEquals(workOrder1.getId(), foundOrder.get().getId());

        Optional<WorkOrder> notFoundOrder = workOrderRepository.findByLibre311ServiceRequestId("SR-999");
        assertFalse(notFoundOrder.isPresent());
    }

    @Test
    void testFindByStatusAndAssignedTo() {
        List<WorkOrder> pendingJohnOrders = workOrderRepository.findByStatusAndAssignedTo(
                WorkOrder.WorkOrderStatus.PENDING, "john.doe");
        assertEquals(1, pendingJohnOrders.size());
        assertEquals(workOrder1.getId(), pendingJohnOrders.get(0).getId());

        List<WorkOrder> assignedJohnOrders = workOrderRepository.findByStatusAndAssignedTo(
                WorkOrder.WorkOrderStatus.ASSIGNED, "john.doe");
        assertTrue(assignedJohnOrders.isEmpty());

        List<WorkOrder> inProgressJohnOrders = workOrderRepository.findByStatusAndAssignedTo(
                WorkOrder.WorkOrderStatus.IN_PROGRESS, "john.doe");
        assertEquals(1, inProgressJohnOrders.size());
        assertEquals(workOrder3.getId(), inProgressJohnOrders.get(0).getId());
    }

    @Test
    void testFindAllOrderedByCreatedAt() {
        List<WorkOrder> allOrders = workOrderRepository.findAllByOrderByCreatedAtDesc();
        assertEquals(3, allOrders.size());
        
        // Should be ordered by creation date (newest first based on query)
        assertTrue(allOrders.get(0).getCreatedAt().isAfter(allOrders.get(1).getCreatedAt()) ||
                   allOrders.get(0).getCreatedAt().equals(allOrders.get(1).getCreatedAt()));
        assertTrue(allOrders.get(1).getCreatedAt().isAfter(allOrders.get(2).getCreatedAt()) ||
                   allOrders.get(1).getCreatedAt().equals(allOrders.get(2).getCreatedAt()));
    }

    @Test
    void testBasicCrudOperations() {
        // Test save
        WorkOrder newOrder = createWorkOrder("New Order", "New Description", "SR-004", 
                                           WorkOrder.WorkOrderStatus.PENDING, WorkOrder.WorkOrderPriority.HIGH, null);
        WorkOrder savedOrder = workOrderRepository.save(newOrder);
        assertNotNull(savedOrder.getId());
        assertNotNull(savedOrder.getCreatedAt());
        assertNotNull(savedOrder.getUpdatedAt());

        // Test findById
        Optional<WorkOrder> foundOrder = workOrderRepository.findById(savedOrder.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals("New Order", foundOrder.get().getTitle());

        // Test update
        savedOrder.setTitle("Updated Title");
        WorkOrder updatedOrder = workOrderRepository.update(savedOrder);
        assertEquals("Updated Title", updatedOrder.getTitle());

        // Test delete
        workOrderRepository.deleteById(updatedOrder.getId());
        Optional<WorkOrder> deletedOrder = workOrderRepository.findById(savedOrder.getId());
        assertFalse(deletedOrder.isPresent());
    }

    @Test
    void testFindAll() {
        List<WorkOrder> allOrders = workOrderRepository.findAll();
        assertEquals(3, allOrders.size());
    }

    @Test
    void testCount() {
        List<WorkOrder> allOrders = workOrderRepository.findAll();
        assertEquals(3, allOrders.size());
    }

    private WorkOrder createWorkOrder(String title, String description, String libre311Id, 
                                     WorkOrder.WorkOrderStatus status, WorkOrder.WorkOrderPriority priority, String assignedTo) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setTitle(title);
        workOrder.setDescription(description);
        workOrder.setLibre311ServiceRequestId(libre311Id);
        workOrder.setStatus(status);
        workOrder.setPriority(priority);
        workOrder.setAssignedTo(assignedTo);
        return workOrder;
    }
}