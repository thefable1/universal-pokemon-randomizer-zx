<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let startersOn = $derived(settings.startersMod !== "UNCHANGED");
  let staticsOn = $derived(settings.staticPokemonMod !== "UNCHANGED");
  let tradesOn = $derived(settings.inGameTradesMod !== "UNCHANGED");
</script>

<Card title="Starter Pokémon">
  <Radios options={opt.starters} bind:value={settings.startersMod} />
  <Check label="Allow alternate formes" bind:checked={settings.allowStarterAltFormes} disabled={!startersOn} />
  <Check label="Randomize held items" bind:checked={settings.randomizeStartersHeldItems} disabled={!startersOn} />
  <Check label="Ban bad held items" bind:checked={settings.banBadRandomStarterHeldItems} disabled={!settings.randomizeStartersHeldItems} />
</Card>

<Card title="Static Pokémon">
  <Radios options={opt.statics} bind:value={settings.staticPokemonMod} />
  <Check label="Limit main-game legendaries" bind:checked={settings.limitMainGameLegendaries} disabled={!staticsOn} />
  <Check label="Limit 600 BST" bind:checked={settings.limit600} disabled={!staticsOn} />
  <Check label="Allow alternate formes" bind:checked={settings.allowStaticAltFormes} disabled={!staticsOn} />
  <Check label="Swap mega evolvers" bind:checked={settings.swapStaticMegaEvos} disabled={!staticsOn} />
</Card>

<Card title="In-Game Trades">
  <Radios options={opt.trades} bind:value={settings.inGameTradesMod} />
  <Check label="Randomize nicknames" bind:checked={settings.randomizeInGameTradesNicknames} disabled={!tradesOn} />
  <Check label="Randomize OTs" bind:checked={settings.randomizeInGameTradesOTs} disabled={!tradesOn} />
  <Check label="Randomize IVs" bind:checked={settings.randomizeInGameTradesIVs} disabled={!tradesOn} />
  <Check label="Randomize held items" bind:checked={settings.randomizeInGameTradesItems} disabled={!tradesOn} />
</Card>
