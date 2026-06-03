<script lang="ts">
  let {
    label,
    value = $bindable(),
    min,
    max,
    disabled = false,
  }: { label: string; value: number; min: number; max: number; disabled?: boolean } = $props();
</script>

<div class="slider" class:disabled>
  <span class="lbl">{label}</span>
  <input type="range" {min} {max} bind:value {disabled} />
  <span class="val">{value}</span>
</div>

<style>
  .slider { display: flex; align-items: center; gap: 10px; margin: 5px 0; font-size: 0.9rem; }
  .slider.disabled { opacity: 0.4; }
  .lbl { min-width: 200px; }
  .val { min-width: 28px; text-align: right; color: #9aa3b8; }
  input { flex: 1; }
</style>
