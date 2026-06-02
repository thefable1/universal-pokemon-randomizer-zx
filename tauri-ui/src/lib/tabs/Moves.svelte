<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let movesetsOn = $derived(settings.movesetsMod !== "UNCHANGED" && settings.movesetsMod !== "METRONOME_ONLY");
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
  <Check label="Force a guaranteed starting move" bind:checked={settings.startWithGuaranteedMoves} disabled={!movesetsOn} />
  <Check label="Reorder damaging moves" bind:checked={settings.reorderDamagingMoves} disabled={!movesetsOn} />
</Card>
