package com.libre311.workorder.dto;

import com.libre311.workorder.entity.WorkOrder;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable
public class CreateWorkOrderRequest {
    
    @NotBlank
    private String libre311ServiceRequestId;
    
    @NotBlank
    private String title;
    
    private String description;
    
    private WorkOrder.WorkOrderPriority priority = WorkOrder.WorkOrderPriority.MEDIUM;
    
    private String assignedTo;
    
    public String getLibre311ServiceRequestId() {
        return libre311ServiceRequestId;
    }
    
    public void setLibre311ServiceRequestId(String libre311ServiceRequestId) {
        this.libre311ServiceRequestId = libre311ServiceRequestId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public WorkOrder.WorkOrderPriority getPriority() {
        return priority;
    }
    
    public void setPriority(WorkOrder.WorkOrderPriority priority) {
        this.priority = priority;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}