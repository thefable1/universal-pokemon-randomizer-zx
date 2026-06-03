<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import Slider from "$lib/ui/Slider.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let on = $derived(settings.trainersMod !== "UNCHANGED");
</script>

<Card title="Trainer Pokémon">
  <Radios options={opt.trainers} bind:value={settings.trainersMod} />
  <Check label="Rival carries starter through game" bind:checked={settings.rivalCarriesStarterThroughout} disabled={!on} />
  <Check label="Use Pokémon of similar strength" bind:checked={settings.trainersUsePokemonOfSimilarStrength} disabled={!on} />
  <Check label="Match typing distribution" bind:checked={settings.trainersMatchTypingDistribution} disabled={!on} />
  <Check label="Block legendaries" bind:checked={settings.trainersBlockLegendaries} disabled={!on} />
  <Check label="Better movesets" bind:checked={settings.betterTrainerMovesets} />
</Card>

<Card title="Battle Style & Naming">
  <Check label="Double battle mode" bind:checked={settings.doubleBattleMode} />
  <Check label="Add a shiny chance" bind:checked={settings.shinyChance} />
  <Check label="Randomize trainer names" bind:checked={settings.randomizeTrainerNames} />
  <Check label="Randomize trainer class names" bind:checked={settings.randomizeTrainerClassNames} />
</Card>

<Card title="Levels & Team Size">
  <Check label="Force fully evolved at level:" bind:checked={settings.trainersForceFullyEvolved} />
  <Slider label="Force-fully-evolved level" min={30} max={65} bind:value={settings.trainersForceFullyEvolvedLevel} disabled={!settings.trainersForceFullyEvolved} />
  <Check label="Modify trainer levels" bind:checked={settings.trainersLevelModified} />
  <Slider label="Level modifier %" min={-50} max={50} bind:value={settings.trainersLevelModifier} disabled={!settings.trainersLevelModified} />
  <Slider label="Extra boss Pokémon" min={0} max={5} bind:value={settings.additionalBossTrainerPokemon} />
  <Slider label="Extra important Pokémon" min={0} max={5} bind:value={settings.additionalImportantTrainerPokemon} />
  <Slider label="Extra regular Pokémon" min={0} max={5} bind:value={settings.additionalRegularTrainerPokemon} />
</Card>

<Card title="Held Items">
  <Check label="Randomize for boss trainers" bind:checked={settings.randomizeHeldItemsForBossTrainerPokemon} />
  <Check label="Randomize for important trainers" bind:checked={settings.randomizeHeldItemsForImportantTrainerPokemon} />
  <Check label="Randomize for regular trainers" bind:checked={settings.randomizeHeldItemsForRegularTrainerPokemon} />
  <Check label="Consumable items only" bind:checked={settings.consumableItemsOnlyForTrainerPokemon} />
  <Check label="Sensible items only" bind:checked={settings.sensibleItemsOnlyForTrainerPokemon} />
  <Check label="Only highest-level gets items" bind:checked={settings.highestLevelOnlyGetsItemsForTrainerPokemon} />
</Card>
