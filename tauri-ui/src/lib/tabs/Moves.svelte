<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import Slider from "$lib/ui/Slider.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let real = $derived(settings.movesetsMod !== "UNCHANGED" && settings.movesetsMod !== "METRONOME_ONLY");
</script>

<Card title="Move Data">
  <Check label="Randomize move powers" bind:checked={settings.randomizeMovePowers} />
  <Check label="Randomize move accuracies" bind:checked={settings.randomizeMoveAccuracies} />
  <Check label="Randomize move PPs" bind:checked={settings.randomizeMovePPs} />
  <Check label="Randomize move types" bind:checked={settings.randomizeMoveTypes} />
  <Check label="Randomize move category" bind:checked={settings.randomizeMoveCategory} />
</Card>

<Card title="Pokémon Movesets">
  <Radios options={opt.movesets} bind:value={settings.movesetsMod} />
  <Check label="Guarantee starting moves" bind:checked={settings.startWithGuaranteedMoves} disabled={!real} />
  <Slider label="Guaranteed move count" min={2} max={4} bind:value={settings.guaranteedMoveCount} disabled={!real || !settings.startWithGuaranteedMoves} />
  <Check label="Reorder damaging moves" bind:checked={settings.reorderDamagingMoves} disabled={!real} />
  <Check label="Force good damaging moves" bind:checked={settings.movesetsForceGoodDamaging} disabled={!real} />
  <Slider label="Good damaging %" min={0} max={100} bind:value={settings.movesetsGoodDamagingPercent} disabled={!real || !settings.movesetsForceGoodDamaging} />
  <Check label="Block broken moves" bind:checked={settings.blockBrokenMovesetMoves} disabled={!real} />
  <Check label="Evolution moves for all" bind:checked={settings.evolutionMovesForAll} disabled={!real} />
</Card>
