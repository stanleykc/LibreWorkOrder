<script lang="ts">
  import { onMount } from 'svelte';
  import { apiClient } from '../api';
  import type { WorkOrder } from '../types';
  import { WorkOrderStatus, WorkOrderPriority } from '../types';

  export let onSelectWorkOrder: (workOrder: WorkOrder) => void;

  let workOrders: WorkOrder[] = [];
  let loading = true;
  let error: string | null = null;

  onMount(async () => {
    try {
      workOrders = await apiClient.getAllWorkOrders();
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to load work orders';
    } finally {
      loading = false;
    }
  });

  function getStatusColor(status: WorkOrderStatus): string {
    switch (status) {
      case WorkOrderStatus.PENDING:
        return 'bg-yellow-100 text-yellow-800';
      case WorkOrderStatus.ASSIGNED:
        return 'bg-blue-100 text-blue-800';
      case WorkOrderStatus.IN_PROGRESS:
        return 'bg-purple-100 text-purple-800';
      case WorkOrderStatus.COMPLETED:
        return 'bg-green-100 text-green-800';
      case WorkOrderStatus.CANCELLED:
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  function getPriorityColor(priority: WorkOrderPriority): string {
    switch (priority) {
      case WorkOrderPriority.LOW:
        return 'bg-gray-100 text-gray-800';
      case WorkOrderPriority.MEDIUM:
        return 'bg-yellow-100 text-yellow-800';
      case WorkOrderPriority.HIGH:
        return 'bg-orange-100 text-orange-800';
      case WorkOrderPriority.URGENT:
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  function formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }
</script>

<div class="work-order-list">
  <h2 class="text-2xl font-bold mb-4">Work Orders</h2>
  
  {#if loading}
    <div class="text-center py-8">
      <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
      <p class="mt-2 text-gray-600">Loading work orders...</p>
    </div>
  {:else if error}
    <div class="bg-red-50 border border-red-200 rounded-md p-4">
      <p class="text-red-800">Error: {error}</p>
    </div>
  {:else if workOrders.length === 0}
    <div class="text-center py-8 text-gray-500">
      <p>No work orders found.</p>
    </div>
  {:else}
    <div class="overflow-x-auto">
      <table class="min-w-full bg-white border border-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Priority</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Assigned To</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          {#each workOrders as workOrder}
            <tr class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {workOrder.id}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{workOrder.title}</div>
                <div class="text-sm text-gray-500">{workOrder.libre311ServiceRequestId}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full {getStatusColor(workOrder.status)}">
                  {workOrder.status}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full {getPriorityColor(workOrder.priority)}">
                  {workOrder.priority}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {workOrder.assignedTo || 'Unassigned'}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {formatDate(workOrder.createdAt)}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <button
                  on:click={() => onSelectWorkOrder(workOrder)}
                  class="text-blue-600 hover:text-blue-900"
                >
                  View
                </button>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>
    </div>
  {/if}
</div>

<style>
  .work-order-list {
    padding: 1rem;
  }
</style>