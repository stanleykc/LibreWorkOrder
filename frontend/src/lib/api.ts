import type { WorkOrder, CreateWorkOrderRequest, UpdateWorkOrderRequest } from './types';

const API_BASE_URL = 'http://localhost:8080/api/work-orders';

class ApiClient {
  private async request<T>(url: string, options: RequestInit = {}): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });

    if (!response.ok) {
      throw new Error(`API request failed: ${response.status} ${response.statusText}`);
    }

    return response.json();
  }

  async getAllWorkOrders(): Promise<WorkOrder[]> {
    return this.request<WorkOrder[]>('');
  }

  async getWorkOrderById(id: number): Promise<WorkOrder> {
    return this.request<WorkOrder>(`/${id}`);
  }

  async createWorkOrder(request: CreateWorkOrderRequest): Promise<WorkOrder> {
    return this.request<WorkOrder>('', {
      method: 'POST',
      body: JSON.stringify(request),
    });
  }

  async updateWorkOrder(id: number, request: UpdateWorkOrderRequest): Promise<WorkOrder> {
    return this.request<WorkOrder>(`/${id}`, {
      method: 'PUT',
      body: JSON.stringify(request),
    });
  }

  async deleteWorkOrder(id: number): Promise<void> {
    await this.request<void>(`/${id}`, {
      method: 'DELETE',
    });
  }

  async assignWorkOrder(id: number, assignedTo: string): Promise<WorkOrder> {
    return this.request<WorkOrder>(`/${id}/assign`, {
      method: 'PUT',
      body: JSON.stringify(assignedTo),
    });
  }

  async updateWorkOrderStatus(id: number, status: string): Promise<WorkOrder> {
    return this.request<WorkOrder>(`/${id}/status`, {
      method: 'PUT',
      body: JSON.stringify(status),
    });
  }
}

export const apiClient = new ApiClient();