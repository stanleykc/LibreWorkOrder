<script lang="ts">
  import { apiClient } from '../api';
  import type { WorkOrder, UpdateWorkOrderRequest } from '../types';
  import { WorkOrderStatus, WorkOrderPriority } from '../types';

  export let workOrder: WorkOrder;
  export let onClose: () => void;
  export let onUpdate: (updatedWorkOrder: WorkOrder) => void;

  let isEditing = false;
  let loading = false;
  let error: string | null = null;

  // Form data
  let formData = {
    title: workOrder.title,
    description: workOrder.description,
    status: workOrder.status,
    assignedTo: workOrder.assignedTo || '',
    priority: workOrder.priority
  };

  async function saveChanges() {
    loading = true;
    error = null;

    try {
      const updateRequest: UpdateWorkOrderRequest = {
        title: formData.title,
        description: formData.description,
        status: formData.status,
        assignedTo: formData.assignedTo || null,
        priority: formData.priority
      };

      const updatedWorkOrder = await apiClient.updateWorkOrder(workOrder.id, updateRequest);
      onUpdate(updatedWorkOrder);
      workOrder = updatedWorkOrder;
      isEditing = false;
    } catch (err) {
      error = err instanceof Error ? err.message : 'Failed to update work order';
    } finally {
      loading = false;
    }
  }

  function cancelEdit() {
    formData = {
      title: workOrder.title,
      description: workOrder.description,
      status: workOrder.status,
      assignedTo: workOrder.assignedTo || '',
      priority: workOrder.priority
    };
    isEditing = false;
    error = null;
  }

  function formatDate(dateString: string): string {
    return new Date(dateString).toLocaleString();
  }

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
</script>

<div class="work-order-detail">
  <div class="bg-white shadow-lg rounded-lg p-6">
    <div class="flex justify-between items-start mb-6">
      <h2 class="text-2xl font-bold text-gray-900">Work Order #{workOrder.id}</h2>
      <button
        on:click={onClose}
        class="text-gray-400 hover:text-gray-600 text-xl"
      >
        ×
      </button>
    </div>

    {#if error}
      <div class="bg-red-50 border border-red-200 rounded-md p-4 mb-4">
        <p class="text-red-800">Error: {error}</p>
      </div>
    {/if}

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Title</label>
        {#if isEditing}
          <input
            type="text"
            bind:value={formData.title}
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        {:else}
          <p class="text-gray-900">{workOrder.title}</p>
        {/if}
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Libre311 Service Request ID</label>
        <p class="text-gray-900">{workOrder.libre311ServiceRequestId}</p>
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Status</label>
        {#if isEditing}
          <select
            bind:value={formData.status}
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {#each Object.values(WorkOrderStatus) as status}
              <option value={status}>{status}</option>
            {/each}
          </select>
        {:else}
          <span class="inline-flex px-3 py-1 text-sm font-semibold rounded-full {getStatusColor(workOrder.status)}">
            {workOrder.status}
          </span>
        {/if}
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Priority</label>
        {#if isEditing}
          <select
            bind:value={formData.priority}
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {#each Object.values(WorkOrderPriority) as priority}
              <option value={priority}>{priority}</option>
            {/each}
          </select>
        {:else}
          <span class="inline-flex px-3 py-1 text-sm font-semibold rounded-full {getPriorityColor(workOrder.priority)}">
            {workOrder.priority}
          </span>
        {/if}
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Assigned To</label>
        {#if isEditing}
          <input
            type="text"
            bind:value={formData.assignedTo}
            placeholder="Enter email or username"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        {:else}
          <p class="text-gray-900">{workOrder.assignedTo || 'Unassigned'}</p>
        {/if}
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Created</label>
        <p class="text-gray-900">{formatDate(workOrder.createdAt)}</p>
      </div>

      <div class="md:col-span-2">
        <label class="block text-sm font-medium text-gray-700 mb-2">Description</label>
        {#if isEditing}
          <textarea
            bind:value={formData.description}
            rows="4"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          ></textarea>
        {:else}
          <p class="text-gray-900 whitespace-pre-wrap">{workOrder.description || 'No description provided'}</p>
        {/if}
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Last Updated</label>
        <p class="text-gray-900">{formatDate(workOrder.updatedAt)}</p>
      </div>
    </div>

    <div class="mt-6 flex justify-end space-x-3">
      {#if isEditing}
        <button
          on:click={cancelEdit}
          disabled={loading}
          class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
        >
          Cancel
        </button>
        <button
          on:click={saveChanges}
          disabled={loading}
          class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? 'Saving...' : 'Save Changes'}
        </button>
      {:else}
        <button
          on:click={() => isEditing = true}
          class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
        >
          Edit
        </button>
      {/if}
    </div>
  </div>
</div>

<style>
  .work-order-detail {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    padding: 1rem;
  }

  .work-order-detail > div {
    max-width: 4xl;
    width: 100%;
    max-height: 90vh;
    overflow-y: auto;
  }
</style>