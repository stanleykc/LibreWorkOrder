package com.libre311.workorder.dto;

import com.libre311.workorder.entity.WorkOrder;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class UpdateWorkOrderRequest {
    
    private String title;
    private String description;
    private WorkOrder.WorkOrderStatus status;
    private String assignedTo;
    private WorkOrder.WorkOrderPriority priority;
    
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
    
    public WorkOrder.WorkOrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(WorkOrder.WorkOrderStatus status) {
        this.status = status;
    }
    
    public String getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public WorkOrder.WorkOrderPriority getPriority() {
        return priority;
    }
    
    public void setPriority(WorkOrder.WorkOrderPriority priority) {
        this.priority = priority;
    }
}