export interface WorkOrder {
  id: number;
  libre311ServiceRequestId: string;
  title: string;
  description: string;
  status: WorkOrderStatus;
  assignedTo: string | null;
  priority: WorkOrderPriority;
  createdAt: string;
  updatedAt: string;
}

export enum WorkOrderStatus {
  PENDING = 'PENDING',
  ASSIGNED = 'ASSIGNED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export enum WorkOrderPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  URGENT = 'URGENT'
}

export interface CreateWorkOrderRequest {
  libre311ServiceRequestId: string;
  title: string;
  description: string;
  priority: WorkOrderPriority;
}

export interface UpdateWorkOrderRequest {
  title?: string;
  description?: string;
  status?: WorkOrderStatus;
  assignedTo?: string;
  priority?: WorkOrderPriority;
}