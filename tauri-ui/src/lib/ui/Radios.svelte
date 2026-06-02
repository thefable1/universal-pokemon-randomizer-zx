<script lang="ts">
  import type { Opt } from "$lib/options";
  let { options, value = $bindable() }: { options: Opt[]; value: string } = $props();
</script>

<div class="radios">
  {#each options as opt}
    <label><input type="radio" bind:group={value} value={opt.value} /> {opt.label}</label>
  {/each}
</div>

<style>
  .radios { display: flex; flex-direction: column; gap: 5px; }
  label { font-size: 0.9rem; }
</style>
