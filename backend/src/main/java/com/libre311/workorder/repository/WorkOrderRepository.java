package com.libre311.workorder.repository;

import com.libre311.workorder.entity.WorkOrder;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.repository.GenericRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends GenericRepository<WorkOrder, Long> {
    
    WorkOrder save(WorkOrder workOrder);
    
    WorkOrder update(WorkOrder workOrder);
    
    Optional<WorkOrder> findById(Long id);
    
    List<WorkOrder> findAll();
    
    void deleteById(Long id);
    
    Optional<WorkOrder> findByLibre311ServiceRequestId(String libre311ServiceRequestId);
    
    List<WorkOrder> findByStatus(WorkOrder.WorkOrderStatus status);
    
    List<WorkOrder> findByAssignedTo(String assignedTo);
    
    List<WorkOrder> findByPriority(WorkOrder.WorkOrderPriority priority);
    
    @Query("SELECT w FROM WorkOrder w ORDER BY w.createdAt DESC")
    List<WorkOrder> findAllByOrderByCreatedAtDesc();
    
    List<WorkOrder> findByStatusAndAssignedTo(WorkOrder.WorkOrderStatus status, String assignedTo);
}