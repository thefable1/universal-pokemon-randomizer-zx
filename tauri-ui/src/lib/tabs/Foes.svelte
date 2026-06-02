<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let trainersOn = $derived(settings.trainersMod !== "UNCHANGED");
</script>

<Card title="Trainer Pokémon">
  <Radios options={opt.trainers} bind:value={settings.trainersMod} />
  <Check label="Rival carries starter through game" bind:checked={settings.rivalCarriesStarterThroughout} disabled={!trainersOn} />
  <Check label="Use Pokémon of similar strength" bind:checked={settings.trainersUsePokemonOfSimilarStrength} disabled={!trainersOn} />
  <Check label="Block legendaries" bind:checked={settings.trainersBlockLegendaries} disabled={!trainersOn} />
</Card>

<Card title="Force Fully Evolved">
  <Check label="Force fully evolved at level:" bind:checked={settings.trainersForceFullyEvolved} />
  <div class="slider" class:disabled={!settings.trainersForceFullyEvolved}>
    <input type="range" min="30" max="65" bind:value={settings.trainersForceFullyEvolvedLevel} disabled={!settings.trainersForceFullyEvolved} />
    <span>{settings.trainersForceFullyEvolvedLevel}</span>
  </div>
</Card>

<style>
  .slider { display: flex; align-items: center; gap: 10px; }
  .slider.disabled { opacity: 0.4; }
</style>
