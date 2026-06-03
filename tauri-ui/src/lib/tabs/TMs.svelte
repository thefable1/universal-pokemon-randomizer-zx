<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import Slider from "$lib/ui/Slider.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let tmsOn = $derived(settings.tmsMod === "RANDOM");
  let tutorsOn = $derived(settings.moveTutorMovesMod === "RANDOM");
</script>

<Card title="TM Moves">
  <Radios options={opt.tms} bind:value={settings.tmsMod} />
  <Check label="Keep field-move TMs" bind:checked={settings.keepFieldMoveTMs} disabled={!tmsOn} />
  <Check label="Force good damaging moves" bind:checked={settings.tmsForceGoodDamaging} disabled={!tmsOn} />
  <Slider label="Good damaging %" min={0} max={100} bind:value={settings.tmsGoodDamagingPercent} disabled={!tmsOn || !settings.tmsForceGoodDamaging} />
  <Check label="Block broken moves" bind:checked={settings.blockBrokenTMMoves} disabled={!tmsOn} />
  <Check label="Follow evolutions (compat.)" bind:checked={settings.tmsFollowEvolutions} />
</Card>

<Card title="TM/HM Compatibility">
  <Radios options={opt.compat} bind:value={settings.tmsHmsCompatibilityMod} />
  <Check label="Full HM compatibility" bind:checked={settings.fullHMCompat} />
  <Check label="Level-up move sanity" bind:checked={settings.tmLevelUpMoveSanity} />
</Card>

<Card title="Move Tutor Moves">
  <Radios options={opt.tms} bind:value={settings.moveTutorMovesMod} />
  <Check label="Keep field-move tutors" bind:checked={settings.keepFieldMoveTutors} disabled={!tutorsOn} />
  <Check label="Force good damaging moves" bind:checked={settings.tutorsForceGoodDamaging} disabled={!tutorsOn} />
  <Slider label="Good damaging %" min={0} max={100} bind:value={settings.tutorsGoodDamagingPercent} disabled={!tutorsOn || !settings.tutorsForceGoodDamaging} />
  <Check label="Block broken moves" bind:checked={settings.blockBrokenTutorMoves} disabled={!tutorsOn} />
  <Check label="Follow evolutions (compat.)" bind:checked={settings.tutorFollowEvolutions} />
</Card>

<Card title="Move Tutor Compatibility">
  <Radios options={opt.compat} bind:value={settings.moveTutorsCompatibilityMod} />
  <Check label="Level-up move sanity" bind:checked={settings.tutorLevelUpMoveSanity} />
</Card>
