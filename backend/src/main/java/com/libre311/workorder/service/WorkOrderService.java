package com.libre311.workorder.service;

import com.libre311.workorder.dto.CreateWorkOrderRequest;
import com.libre311.workorder.dto.UpdateWorkOrderRequest;
import com.libre311.workorder.entity.WorkOrder;
import com.libre311.workorder.repository.WorkOrderRepository;
import jakarta.inject.Singleton;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class WorkOrderService {
    
    private final WorkOrderRepository workOrderRepository;
    
    public WorkOrderService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }
    
    @Transactional
    public WorkOrder createWorkOrder(CreateWorkOrderRequest request) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setLibre311ServiceRequestId(request.getLibre311ServiceRequestId());
        workOrder.setTitle(request.getTitle());
        workOrder.setDescription(request.getDescription());
        workOrder.setPriority(request.getPriority());
        workOrder.setStatus(WorkOrder.WorkOrderStatus.PENDING);
        workOrder.setAssignedTo(request.getAssignedTo());
        
        return workOrderRepository.save(workOrder);
    }
    
    public List<WorkOrder> getAllWorkOrders() {
        return workOrderRepository.findAll();
    }
    
    public Optional<WorkOrder> getWorkOrderById(Long id) {
        return workOrderRepository.findById(id);
    }
    
    public Optional<WorkOrder> getWorkOrderByLibre311ServiceRequestId(String libre311ServiceRequestId) {
        return workOrderRepository.findByLibre311ServiceRequestId(libre311ServiceRequestId);
    }
    
    public List<WorkOrder> getWorkOrdersByStatus(WorkOrder.WorkOrderStatus status) {
        return workOrderRepository.findByStatus(status);
    }
    
    public List<WorkOrder> getWorkOrdersByAssignedTo(String assignedTo) {
        return workOrderRepository.findByAssignedTo(assignedTo);
    }
    
    @Transactional
    public Optional<WorkOrder> updateWorkOrder(Long id, UpdateWorkOrderRequest request) {
        Optional<WorkOrder> workOrderOpt = workOrderRepository.findById(id);
        if (workOrderOpt.isPresent()) {
            WorkOrder workOrder = workOrderOpt.get();
            
            if (request.getTitle() != null) {
                workOrder.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                workOrder.setDescription(request.getDescription());
            }
            if (request.getStatus() != null) {
                workOrder.setStatus(request.getStatus());
            }
            if (request.getAssignedTo() != null) {
                workOrder.setAssignedTo(request.getAssignedTo());
            }
            if (request.getPriority() != null) {
                workOrder.setPriority(request.getPriority());
            }
            
            return Optional.of(workOrderRepository.update(workOrder));
        }
        return Optional.empty();
    }
    
    @Transactional
    public boolean deleteWorkOrder(Long id) {
        Optional<WorkOrder> workOrder = workOrderRepository.findById(id);
        if (workOrder.isPresent()) {
            workOrderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    @Transactional
    public Optional<WorkOrder> assignWorkOrder(Long id, String assignedTo) {
        Optional<WorkOrder> workOrderOpt = workOrderRepository.findById(id);
        if (workOrderOpt.isPresent()) {
            WorkOrder workOrder = workOrderOpt.get();
            workOrder.setAssignedTo(assignedTo);
            workOrder.setStatus(WorkOrder.WorkOrderStatus.ASSIGNED);
            return Optional.of(workOrderRepository.update(workOrder));
        }
        return Optional.empty();
    }
    
    @Transactional
    public Optional<WorkOrder> updateWorkOrderStatus(Long id, WorkOrder.WorkOrderStatus status) {
        Optional<WorkOrder> workOrderOpt = workOrderRepository.findById(id);
        if (workOrderOpt.isPresent()) {
            WorkOrder workOrder = workOrderOpt.get();
            workOrder.setStatus(status);
            return Optional.of(workOrderRepository.update(workOrder));
        }
        return Optional.empty();
    }
}