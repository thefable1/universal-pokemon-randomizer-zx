<script lang="ts">
  let {
    label,
    checked = $bindable(),
    disabled = false,
  }: { label: string; checked: boolean; disabled?: boolean } = $props();
</script>

<label class="check" class:disabled>
  <input type="checkbox" bind:checked {disabled} /> {label}
</label>

<style>
  .check { display: block; font-size: 0.9rem; margin: 5px 0; }
  .check.disabled { opacity: 0.4; }
</style>
