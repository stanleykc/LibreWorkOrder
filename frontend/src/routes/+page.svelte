<script lang="ts">
  import WorkOrderList from '$lib/components/WorkOrderList.svelte';
  import WorkOrderDetail from '$lib/components/WorkOrderDetail.svelte';
  import type { WorkOrder } from '$lib/types';

  let selectedWorkOrder: WorkOrder | null = null;

  function selectWorkOrder(workOrder: WorkOrder) {
    selectedWorkOrder = workOrder;
  }

  function closeDetail() {
    selectedWorkOrder = null;
  }

  function updateWorkOrder(updatedWorkOrder: WorkOrder) {
    selectedWorkOrder = updatedWorkOrder;
  }
</script>

<svelte:head>
  <title>LibreWorkOrder - Work Order Management</title>
</svelte:head>

<main class="min-h-screen bg-gray-100">
  <div class="container mx-auto px-4 py-8">
    <header class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900">LibreWorkOrder</h1>
      <p class="text-gray-600 mt-2">Work Order Management System</p>
    </header>

    <WorkOrderList onSelectWorkOrder={selectWorkOrder} />

    {#if selectedWorkOrder}
      <WorkOrderDetail
        workOrder={selectedWorkOrder}
        onClose={closeDetail}
        onUpdate={updateWorkOrder}
      />
    {/if}
  </div>
</main>

<style>
  :global(body) {
    margin: 0;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  }
</style>
