<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let statsRandom = $derived(settings.baseStatsMod === "RANDOM");
  let typesOn = $derived(settings.typesMod !== "UNCHANGED");
  let abilitiesOn = $derived(settings.abilitiesMod === "RANDOMIZE");
  let evosOn = $derived(settings.evolutionsMod !== "UNCHANGED");
</script>

<Card title="Base Statistics">
  <Radios options={opt.baseStats} bind:value={settings.baseStatsMod} />
  <Check label="Follow evolutions" bind:checked={settings.baseStatsFollowEvolutions} disabled={!statsRandom} />
  <Check label="Follow mega evolutions" bind:checked={settings.baseStatsFollowMegaEvolutions} disabled={!statsRandom} />
  <Check label="Assign evolution stats randomly" bind:checked={settings.assignEvoStatsRandomly} disabled={!statsRandom} />
  <Check label="Standardize EXP curves" bind:checked={settings.standardizeExpCurves} />
  <Check label="Update to a later generation" bind:checked={settings.updateBaseStats} />
</Card>

<Card title="Types">
  <Radios options={opt.types} bind:value={settings.typesMod} />
  <Check label="Follow mega evolutions" bind:checked={settings.typesFollowMegaEvolutions} disabled={!typesOn} />
  <Check label="Dual-type only" bind:checked={settings.dualTypeOnly} disabled={!typesOn} />
</Card>

<Card title="Abilities">
  <Radios options={opt.abilities} bind:value={settings.abilitiesMod} />
  <Check label="Allow Wonder Guard" bind:checked={settings.allowWonderGuard} disabled={!abilitiesOn} />
  <Check label="Follow evolutions" bind:checked={settings.abilitiesFollowEvolutions} disabled={!abilitiesOn} />
  <Check label="Follow mega evolutions" bind:checked={settings.abilitiesFollowMegaEvolutions} disabled={!abilitiesOn} />
  <Check label="Weigh duplicate abilities together" bind:checked={settings.weighDuplicateAbilitiesTogether} disabled={!abilitiesOn} />
  <Check label="Ensure two abilities" bind:checked={settings.ensureTwoAbilities} disabled={!abilitiesOn} />
  <Check label="Ban trapping abilities" bind:checked={settings.banTrappingAbilities} disabled={!abilitiesOn} />
  <Check label="Ban negative abilities" bind:checked={settings.banNegativeAbilities} disabled={!abilitiesOn} />
  <Check label="Ban bad abilities" bind:checked={settings.banBadAbilities} disabled={!abilitiesOn} />
</Card>

<Card title="Evolutions">
  <Radios options={opt.evolutions} bind:value={settings.evolutionsMod} />
  <Check label="Similar strength" bind:checked={settings.evosSimilarStrength} disabled={!evosOn} />
  <Check label="Same typing" bind:checked={settings.evosSameTyping} disabled={!evosOn} />
  <Check label="Limit to three stages" bind:checked={settings.evosMaxThreeStages} disabled={!evosOn} />
  <Check label="Force change" bind:checked={settings.evosForceChange} disabled={!evosOn} />
  <Check label="Change impossible evolutions" bind:checked={settings.changeImpossibleEvolutions} />
  <Check label="Make evolutions easier" bind:checked={settings.makeEvolutionsEasier} />
  <Check label="Remove time-based evolutions" bind:checked={settings.removeTimeBasedEvolutions} />
</Card>
