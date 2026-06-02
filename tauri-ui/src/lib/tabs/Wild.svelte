<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let wildOn = $derived(settings.wildPokemonMod !== "UNCHANGED");
</script>

<Card title="Wild Pokémon">
  <Radios options={opt.wild} bind:value={settings.wildPokemonMod} />
  <Check label="Block legendaries" bind:checked={settings.blockWildLegendaries} disabled={!wildOn} />
  <Check label="Use time-based encounters" bind:checked={settings.useTimeBasedEncounters} />
  <Check label="Randomize held items" bind:checked={settings.randomizeWildPokemonHeldItems} disabled={!wildOn} />
</Card>

<Card title="Force Fully Evolved">
  <Check label="Force fully evolved at level:" bind:checked={settings.wildForceFullyEvolved} />
  <div class="slider" class:disabled={!settings.wildForceFullyEvolved}>
    <input type="range" min="30" max="65" bind:value={settings.wildForceFullyEvolvedLevel} disabled={!settings.wildForceFullyEvolved} />
    <span>{settings.wildForceFullyEvolvedLevel}</span>
  </div>
</Card>

<style>
  .slider { display: flex; align-items: center; gap: 10px; }
  .slider.disabled { opacity: 0.4; }
</style>
