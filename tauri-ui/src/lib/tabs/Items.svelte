<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Radios from "$lib/ui/Radios.svelte";
  import Check from "$lib/ui/Check.svelte";
  import * as opt from "$lib/options";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
  let fieldOn = $derived(settings.fieldItemsMod === "RANDOM" || settings.fieldItemsMod === "RANDOM_EVEN");
  let shopOn = $derived(settings.shopItemsMod !== "UNCHANGED");
  let pickupOn = $derived(settings.pickupItemsMod !== "UNCHANGED");
</script>

<Card title="Field Items">
  <Radios options={opt.fieldItems} bind:value={settings.fieldItemsMod} />
  <Check label="Ban bad random items" bind:checked={settings.banBadRandomFieldItems} disabled={!fieldOn} />
</Card>

<Card title="Shop Items">
  <Radios options={opt.shopItems} bind:value={settings.shopItemsMod} />
  <Check label="Ban bad random items" bind:checked={settings.banBadRandomShopItems} disabled={!shopOn} />
  <Check label="Ban regular shop items" bind:checked={settings.banRegularShopItems} disabled={!shopOn} />
  <Check label="Ban overpowered shop items" bind:checked={settings.banOPShopItems} disabled={!shopOn} />
  <Check label="Balance shop prices" bind:checked={settings.balanceShopPrices} />
  <Check label="Guarantee evolution items" bind:checked={settings.guaranteeEvolutionItems} disabled={!shopOn} />
  <Check label="Guarantee X items" bind:checked={settings.guaranteeXItems} disabled={!shopOn} />
</Card>

<Card title="Pickup Items">
  <Radios options={opt.pickup} bind:value={settings.pickupItemsMod} />
  <Check label="Ban bad random items" bind:checked={settings.banBadRandomPickupItems} disabled={!pickupOn} />
</Card>
