<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let tmsOn = $derived(settings.tmsMod === "RANDOM");
</script>

<Card title="TM Moves">
  <Radios options={opt.tms} bind:value={settings.tmsMod} />
  <Check label="Keep field-move TMs" bind:checked={settings.keepFieldMoveTMs} disabled={!tmsOn} />
</Card>

<Card title="TM/HM Compatibility">
  <Radios options={opt.compat} bind:value={settings.tmsHmsCompatibilityMod} />
  <Check label="Full HM compatibility" bind:checked={settings.fullHMCompat} />
  <Check label="Level-up move sanity" bind:checked={settings.tmLevelUpMoveSanity} />
</Card>

<Card title="Move Tutors">
  <Radios options={opt.tms} bind:value={settings.moveTutorMovesMod} />
</Card>

<Card title="Move Tutor Compatibility">
  <Radios options={opt.compat} bind:value={settings.moveTutorsCompatibilityMod} />
</Card>
