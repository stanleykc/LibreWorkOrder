package com.libre311.workorder.controller;

import com.libre311.workorder.dto.CreateWorkOrderRequest;
import com.libre311.workorder.dto.UpdateWorkOrderRequest;
import com.libre311.workorder.entity.WorkOrder;
import com.libre311.workorder.service.WorkOrderService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller("/api/work-orders")
@Validated
public class WorkOrderController {
    
    private final WorkOrderService workOrderService;
    
    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }
    
    @Post
    public HttpResponse<WorkOrder> createWorkOrder(@Valid @Body CreateWorkOrderRequest request) {
        WorkOrder workOrder = workOrderService.createWorkOrder(request);
        return HttpResponse.created(workOrder);
    }
    
    @Get
    public List<WorkOrder> getAllWorkOrders() {
        return workOrderService.getAllWorkOrders();
    }
    
    @Get("/{id}")
    public HttpResponse<WorkOrder> getWorkOrderById(@PathVariable Long id) {
        Optional<WorkOrder> workOrder = workOrderService.getWorkOrderById(id);
        return workOrder.map(HttpResponse::ok)
                       .orElse(HttpResponse.notFound());
    }
    
    @Get("/libre311/{libre311ServiceRequestId}")
    public HttpResponse<WorkOrder> getWorkOrderByLibre311ServiceRequestId(
            @PathVariable String libre311ServiceRequestId) {
        Optional<WorkOrder> workOrder = workOrderService.getWorkOrderByLibre311ServiceRequestId(libre311ServiceRequestId);
        return workOrder.map(HttpResponse::ok)
                       .orElse(HttpResponse.notFound());
    }
    
    @Get("/status/{status}")
    public List<WorkOrder> getWorkOrdersByStatus(@PathVariable WorkOrder.WorkOrderStatus status) {
        return workOrderService.getWorkOrdersByStatus(status);
    }
    
    @Get("/assigned/{assignedTo}")
    public List<WorkOrder> getWorkOrdersByAssignedTo(@PathVariable String assignedTo) {
        return workOrderService.getWorkOrdersByAssignedTo(assignedTo);
    }
    
    @Put("/{id}")
    public HttpResponse<WorkOrder> updateWorkOrder(@PathVariable Long id, 
                                                  @Valid @Body UpdateWorkOrderRequest request) {
        Optional<WorkOrder> workOrder = workOrderService.updateWorkOrder(id, request);
        return workOrder.map(HttpResponse::ok)
                       .orElse(HttpResponse.notFound());
    }
    
    @Put("/{id}/assign")
    public HttpResponse<WorkOrder> assignWorkOrder(@PathVariable Long id, 
                                                  @Body String assignedTo) {
        Optional<WorkOrder> workOrder = workOrderService.assignWorkOrder(id, assignedTo);
        return workOrder.map(HttpResponse::ok)
                       .orElse(HttpResponse.notFound());
    }
    
    @Put("/{id}/status")
    public HttpResponse<WorkOrder> updateWorkOrderStatus(@PathVariable Long id, 
                                                        @Body WorkOrder.WorkOrderStatus status) {
        Optional<WorkOrder> workOrder = workOrderService.updateWorkOrderStatus(id, status);
        return workOrder.map(HttpResponse::ok)
                       .orElse(HttpResponse.notFound());
    }
    
    @Delete("/{id}")
    public HttpResponse<Void> deleteWorkOrder(@PathVariable Long id) {
        boolean deleted = workOrderService.deleteWorkOrder(id);
        return deleted ? HttpResponse.noContent() : HttpResponse.notFound();
    }
}