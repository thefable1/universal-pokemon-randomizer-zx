<script lang="ts">
  import { onMount } from "svelte";
  import { open, save } from "@tauri-apps/plugin-dialog";
  import {
    encode,
    randomize,
    health,
    defaultSettings,
    type SettingsRequest,
    type BaseStatsMod,
    type TypesMod,
  } from "$lib/bridge";

  let settings = $state<SettingsRequest>(defaultSettings());

  // Reactive derived state — this is the whole point vs. Swing's manual wiring.
  let statsRandomized = $derived(settings.baseStatsMod === "RANDOM");
  let typesRandomized = $derived(settings.typesMod !== "UNCHANGED");

  let engineVersion = $state<string | null>(null);
  let connError = $state<string | null>(null);

  let romPath = $state<string | null>(null);
  let outputPath = $state<string | null>(null);
  let settingsString = $state("");
  let log = $state("");
  let error = $state<string | null>(null);
  let busy = $state(false);

  const baseStatsOptions: { value: BaseStatsMod; label: string }[] = [
    { value: "UNCHANGED", label: "Unchanged" },
    { value: "SHUFFLE", label: "Shuffle" },
    { value: "RANDOM", label: "Random" },
  ];
  const typesOptions: { value: TypesMod; label: string }[] = [
    { value: "UNCHANGED", label: "Unchanged" },
    { value: "RANDOM_FOLLOW_EVOLUTIONS", label: "Random (follow evolutions)" },
    { value: "COMPLETELY_RANDOM", label: "Random (completely)" },
  ];

  onMount(async () => {
    try {
      engineVersion = (await health()).engineVersion;
    } catch (e) {
      connError = "Cannot reach the engine bridge. Start it with: ./gradlew :web-bridge:run";
    }
  });

  async function pickRom() {
    const sel = await open({
      multiple: false,
      filters: [{ name: "ROM", extensions: ["gb", "gbc", "gba", "nds", "3ds", "cxi"] }],
    });
    if (typeof sel === "string") romPath = sel;
  }

  async function pickOutput() {
    const sel = await save({
      filters: [{ name: "ROM", extensions: ["gba", "nds", "gbc", "gb", "cxi"] }],
    });
    if (sel) outputPath = sel;
  }

  async function generate() {
    error = null;
    try {
      settingsString = (await encode(settings)).settingsString;
    } catch (e) {
      error = String(e);
    }
  }

  async function runRandomize() {
    if (!romPath || !outputPath) {
      error = "Pick a ROM and an output path first.";
      return;
    }
    busy = true;
    error = null;
    log = "";
    try {
      log = (await randomize(romPath, outputPath, settings)).log;
    } catch (e) {
      error = String(e);
    } finally {
      busy = false;
    }
  }
</script>

<main>
  <header>
    <h1>Universal Pokémon Randomizer ZX</h1>
    {#if engineVersion}
      <span class="badge ok">engine {engineVersion} connected</span>
    {:else if connError}
      <span class="badge err">{connError}</span>
    {:else}
      <span class="badge">connecting…</span>
    {/if}
  </header>

  <section class="card">
    <h2>Base Statistics</h2>
    <div class="radios">
      {#each baseStatsOptions as opt}
        <label><input type="radio" bind:group={settings.baseStatsMod} value={opt.value} /> {opt.label}</label>
      {/each}
    </div>
    <!-- Enabled is DERIVED from the mode — no manual enable/disable plumbing. -->
    <label class="check" class:disabled={!statsRandomized}>
      <input type="checkbox" bind:checked={settings.baseStatsFollowEvolutions} disabled={!statsRandomized} />
      Follow evolutions
    </label>
    <label class="check">
      <input type="checkbox" bind:checked={settings.standardizeExpCurves} /> Standardize EXP curves
    </label>
    <label class="check">
      <input type="checkbox" bind:checked={settings.updateBaseStats} /> Update base stats to a later generation
    </label>
  </section>

  <section class="card">
    <h2>Types</h2>
    <div class="radios">
      {#each typesOptions as opt}
        <label><input type="radio" bind:group={settings.typesMod} value={opt.value} /> {opt.label}</label>
      {/each}
    </div>
    <label class="check" class:disabled={!typesRandomized}>
      <input type="checkbox" bind:checked={settings.dualTypeOnly} disabled={!typesRandomized} /> Dual-type only
    </label>
  </section>

  <section class="card">
    <h2>Wild Pokémon</h2>
    <label class="check">
      <input type="checkbox" bind:checked={settings.wildForceFullyEvolved} /> Force fully evolved at level:
    </label>
    <div class="slider" class:disabled={!settings.wildForceFullyEvolved}>
      <input
        type="range"
        min="30"
        max="65"
        bind:value={settings.wildForceFullyEvolvedLevel}
        disabled={!settings.wildForceFullyEvolved}
      />
      <span>{settings.wildForceFullyEvolvedLevel}</span>
    </div>
  </section>

  <section class="card">
    <h2>Run</h2>
    <div class="files">
      <button onclick={pickRom}>Open ROM…</button>
      <code>{romPath ?? "no ROM selected"}</code>
    </div>
    <div class="files">
      <button onclick={pickOutput}>Output to…</button>
      <code>{outputPath ?? "no output selected"}</code>
    </div>
    <div class="actions">
      <button onclick={generate}>Preview settings string</button>
      <button class="primary" onclick={runRandomize} disabled={busy}>
        {busy ? "Randomizing…" : "Randomize"}
      </button>
    </div>
  </section>

  {#if error}<p class="error">{error}</p>{/if}

  {#if settingsString}
    <section class="card">
      <h2>Settings string (engine-encoded)</h2>
      <code class="block">{settingsString}</code>
    </section>
  {/if}

  {#if log}
    <section class="card">
      <h2>Randomization log</h2>
      <pre>{log}</pre>
    </section>
  {/if}
</main>

<style>
  :global(body) {
    margin: 0;
    font-family: system-ui, sans-serif;
    background: #0f1117;
    color: #e7e9ee;
  }
  main {
    max-width: 760px;
    margin: 0 auto;
    padding: 24px;
    display: flex;
    flex-direction: column;
    gap: 16px;
  }
  header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
  }
  h1 { font-size: 1.4rem; margin: 0; }
  h2 { font-size: 1rem; margin: 0 0 10px; color: #aab2c5; }
  .card {
    background: #1a1d27;
    border: 1px solid #272b38;
    border-radius: 12px;
    padding: 16px 18px;
  }
  .radios { display: flex; flex-direction: column; gap: 6px; margin-bottom: 8px; }
  .check { display: block; margin: 4px 0; }
  .check.disabled, .slider.disabled { opacity: 0.4; }
  .slider { display: flex; align-items: center; gap: 10px; margin-top: 6px; }
  .files { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
  .actions { display: flex; gap: 10px; margin-top: 8px; }
  button {
    background: #272b38;
    color: #e7e9ee;
    border: none;
    border-radius: 8px;
    padding: 8px 14px;
    cursor: pointer;
    font-size: 0.9rem;
  }
  button:hover { background: #323748; }
  button.primary { background: #4f7cff; }
  button.primary:disabled { opacity: 0.5; cursor: default; }
  .badge { font-size: 0.8rem; padding: 4px 10px; border-radius: 999px; background: #272b38; }
  .badge.ok { background: #16361f; color: #6fe39a; }
  .badge.err { background: #3a1a1a; color: #ff8f8f; }
  code { font-size: 0.8rem; color: #9aa3b8; word-break: break-all; }
  code.block { display: block; background: #0f1117; padding: 10px; border-radius: 8px; }
  pre { background: #0f1117; padding: 10px; border-radius: 8px; overflow: auto; max-height: 280px; font-size: 0.8rem; }
  .error { color: #ff8f8f; }
</style>
