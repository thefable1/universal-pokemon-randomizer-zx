<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import Slider from "$lib/ui/Slider.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let on = $derived(settings.wildPokemonMod !== "UNCHANGED");
</script>

<Card title="Wild Pokémon">
  <Radios options={opt.wild} bind:value={settings.wildPokemonMod} />
  <Check label="Block legendaries" bind:checked={settings.blockWildLegendaries} disabled={!on} />
  <Check label="Use time-based encounters" bind:checked={settings.useTimeBasedEncounters} />
  <Check label="Allow alternate formes" bind:checked={settings.allowWildAltFormes} disabled={!on} />
  <Check label="Balance shaking-grass Pokémon" bind:checked={settings.balanceShakingGrass} disabled={!on} />
</Card>

<Card title="Held Items & Catch Rate">
  <Check label="Randomize held items" bind:checked={settings.randomizeWildPokemonHeldItems} disabled={!on} />
  <Check label="Ban bad held items" bind:checked={settings.banBadRandomWildPokemonHeldItems} disabled={!settings.randomizeWildPokemonHeldItems} />
  <Check label="Set minimum catch rate" bind:checked={settings.useMinimumCatchRate} />
  <Slider label="Minimum catch rate level" min={1} max={5} bind:value={settings.minimumCatchRateLevel} disabled={!settings.useMinimumCatchRate} />
</Card>

<Card title="Levels">
  <Check label="Modify wild levels" bind:checked={settings.wildLevelsModified} />
  <Slider label="Level modifier %" min={-50} max={50} bind:value={settings.wildLevelModifier} disabled={!settings.wildLevelsModified} />
  <Check label="Force fully evolved at level:" bind:checked={settings.wildForceFullyEvolved} />
  <Slider label="Force-fully-evolved level" min={30} max={65} bind:value={settings.wildForceFullyEvolvedLevel} disabled={!settings.wildForceFullyEvolved} />
</Card>
