<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Check from "$lib/ui/Check.svelte";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
</script>

<Card title="Misc. Tweaks">
  <p class="note">Availability depends on the loaded game; unsupported tweaks are ignored by the engine.</p>
  <Check label="Lower-case Pokémon names" bind:checked={settings.miscLowerCaseNames} />
  <Check label="National Dex at start" bind:checked={settings.miscNationalDexAtStart} />
  <Check label="Update type effectiveness" bind:checked={settings.miscUpdateTypeEffectiveness} />
  <Check label="Fastest text" bind:checked={settings.miscFastestText} />
  <Check label="Running shoes indoors" bind:checked={settings.miscRunningShoesIndoors} />
</Card>

<style>
  .note { font-size: 0.78rem; color: #7c8499; margin: 0 0 10px; }
</style>
