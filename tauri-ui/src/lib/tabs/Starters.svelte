<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let startersOn = $derived(settings.startersMod !== "UNCHANGED");
</script>

<Card title="Starter Pokémon">
  <Radios options={opt.starters} bind:value={settings.startersMod} />
  <Check label="Randomize starter held items" bind:checked={settings.randomizeStartersHeldItems} disabled={!startersOn} />
</Card>

<Card title="Static Pokémon">
  <Radios options={opt.statics} bind:value={settings.staticPokemonMod} />
</Card>

<Card title="In-Game Trades">
  <Radios options={opt.trades} bind:value={settings.inGameTradesMod} />
</Card>
