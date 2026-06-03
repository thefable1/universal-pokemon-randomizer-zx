<script lang="ts">
  import Card from "$lib/ui/Card.svelte";
  import Check from "$lib/ui/Check.svelte";
  import type { SettingsRequest } from "$lib/bridge";

  let { settings }: { settings: SettingsRequest } = $props();
</script>

<Card title="Misc. Tweaks">
  <p class="note">Availability depends on the loaded game; unsupported tweaks are ignored by the engine.</p>
  <div class="grid">
    <Check label="Lower-case Pokémon names" bind:checked={settings.miscLowerCaseNames} />
    <Check label="National Dex at start" bind:checked={settings.miscNationalDexAtStart} />
    <Check label="Update type effectiveness" bind:checked={settings.miscUpdateTypeEffectiveness} />
    <Check label="Fastest text" bind:checked={settings.miscFastestText} />
    <Check label="Running shoes indoors" bind:checked={settings.miscRunningShoesIndoors} />
    <Check label="Run without running shoes" bind:checked={settings.miscRunWithoutRunningShoes} />
    <Check label="Force challenge mode" bind:checked={settings.miscForceChallengeMode} />
    <Check label="Randomize PC potion" bind:checked={settings.miscRandomizePcPotion} />
    <Check label="Allow Pikachu evolution" bind:checked={settings.miscAllowPikachuEvolution} />
    <Check label="Randomize catching tutorial" bind:checked={settings.miscRandomizeCatchingTutorial} />
    <Check label="Faster HP & EXP bars" bind:checked={settings.miscFasterHpExpBars} />
    <Check label="Fast Distortion World" bind:checked={settings.miscFastDistortionWorld} />
    <Check label="Update Rotom forme typing" bind:checked={settings.miscUpdateRotomFormeTyping} />
    <Check label="Disable low-HP music" bind:checked={settings.miscDisableLowHpMusic} />
    <Check label="SOS battles for all" bind:checked={settings.miscSosBattlesForAll} />
    <Check label="Balance static levels" bind:checked={settings.miscBalanceStaticLevels} />
    <Check label="Retain alternate formes" bind:checked={settings.miscRetainAltFormes} />
    <Check label="BW EXP patch" bind:checked={settings.miscBwExpPatch} />
    <Check label="Nerf X Accuracy" bind:checked={settings.miscNerfXAccuracy} />
    <Check label="Fix critical-hit rate" bind:checked={settings.miscFixCritRate} />
    <Check label="Ban Lucky Egg" bind:checked={settings.miscBanLuckyEgg} />
    <Check label="No free Lucky Egg" bind:checked={settings.miscNoFreeLuckyEgg} />
    <Check label="Ban big Maniac items" bind:checked={settings.miscBanBigManiacItems} />
  </div>
</Card>

<style>
  .note { font-size: 0.78rem; color: #7c8499; margin: 0 0 10px; }
  .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 0 20px; }
</style>
