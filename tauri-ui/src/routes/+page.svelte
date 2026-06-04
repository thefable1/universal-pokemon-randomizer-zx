<script lang="ts">
  import { onMount } from "svelte";
  import { open, save } from "@tauri-apps/plugin-dialog";
  import { encode, randomize, health, defaultSettings, type SettingsRequest } from "$lib/bridge";
  import { TABS } from "$lib/options";
  import Titlebar from "$lib/ui/Titlebar.svelte";
  import Traits from "$lib/tabs/Traits.svelte";
  import Starters from "$lib/tabs/Starters.svelte";
  import Moves from "$lib/tabs/Moves.svelte";
  import Foes from "$lib/tabs/Foes.svelte";
  import Wild from "$lib/tabs/Wild.svelte";
  import TMs from "$lib/tabs/TMs.svelte";
  import Items from "$lib/tabs/Items.svelte";
  import Misc from "$lib/tabs/Misc.svelte";

  const COMPONENTS: Record<string, any> = {
    traits: Traits, starters: Starters, moves: Moves, foes: Foes,
    wild: Wild, tms: TMs, items: Items, misc: Misc,
  };

  let settings = $state<SettingsRequest>(defaultSettings());
  let active = $state<string>("traits");
  let ActiveTab = $derived(COMPONENTS[active]);

  let engineVersion = $state<string | null>(null);
  let connError = $state<string | null>(null);
  let romPath = $state<string | null>(null);
  let outputPath = $state<string | null>(null);
  let settingsString = $state("");
  let log = $state("");
  let error = $state<string | null>(null);
  let busy = $state(false);

  onMount(async () => {
    // The bundled engine (a JVM) takes a couple seconds to boot while the webview
    // loads instantly, so poll instead of checking once. ~30s budget.
    connError = "Starting engine…";
    for (let attempt = 0; attempt < 40; attempt++) {
      try {
        engineVersion = (await health()).engineVersion;
        connError = null;
        return;
      } catch {
        await new Promise((r) => setTimeout(r, 750));
      }
    }
    connError = "Engine didn't start. In dev, run: ./gradlew :web-bridge:run";
  });

  async function pickRom() {
    const sel = await open({ multiple: false, filters: [{ name: "ROM", extensions: ["gb", "gbc", "gba", "nds", "3ds", "cxi"] }] });
    if (typeof sel === "string") romPath = sel;
  }
  async function pickOutput() {
    const sel = await save({ filters: [{ name: "ROM", extensions: ["gba", "nds", "gbc", "gb", "cxi"] }] });
    if (sel) outputPath = sel;
  }
  async function preview() {
    error = null;
    try { settingsString = (await encode(settings)).settingsString; } catch (e) { error = String(e); }
  }
  async function run() {
    if (!romPath || !outputPath) { error = "Pick a ROM and an output path first."; return; }
    busy = true; error = null; log = "";
    try { log = (await randomize(romPath, outputPath, settings)).log; } catch (e) { error = String(e); }
    finally { busy = false; }
  }
</script>

<div class="window">
<Titlebar />
<div class="app">
  <aside class="sidebar">
    <div class="brand">UPR-ZX</div>
    <nav>
      {#each TABS as tab}
        <button class:active={active === tab.id} onclick={() => (active = tab.id)}>{tab.label}</button>
      {/each}
    </nav>
    <div class="conn">
      {#if engineVersion}<span class="badge ok">engine {engineVersion}</span>
      {:else if connError}<span class="badge err">{connError}</span>
      {:else}<span class="badge">connecting…</span>{/if}
    </div>
  </aside>

  <main>
    <div class="content">
      <ActiveTab {settings} />
    </div>

    <footer class="runbar">
      <div class="files">
        <button onclick={pickRom}>Open ROM…</button>
        <code>{romPath ?? "no ROM"}</code>
        <button onclick={pickOutput}>Output…</button>
        <code>{outputPath ?? "no output"}</code>
      </div>
      <div class="run-actions">
        <button onclick={preview}>Preview string</button>
        <button class="primary" onclick={run} disabled={busy}>{busy ? "Randomizing…" : "Randomize"}</button>
      </div>
    </footer>

    {#if error}<p class="error">{error}</p>{/if}
    {#if settingsString}<code class="result">{settingsString}</code>{/if}
    {#if log}<pre class="result">{log}</pre>{/if}
  </main>
</div>
</div>

<style>
  :global(body) {
    margin: 0;
    font-family: system-ui, sans-serif;
    background: #0f1117;
    color: #e7e9ee;
    /* Behave like a native app: don't let the UI chrome be text-selected. */
    user-select: none;
    -webkit-user-select: none;
    cursor: default;
  }
  /* Keep the generated outputs selectable so they can be copied. */
  .result { user-select: text; -webkit-user-select: text; }
  .window { display: flex; flex-direction: column; height: 100vh; }
  .app { flex: 1; display: grid; grid-template-columns: 232px 1fr; overflow: hidden; min-height: 0; }
  .sidebar { background: #14161f; border-right: 1px solid #272b38; display: flex; flex-direction: column; padding: 14px 10px; }
  .brand { font-weight: 700; font-size: 1.1rem; padding: 6px 10px 14px; color: #4f7cff; }
  nav { display: flex; flex-direction: column; gap: 2px; flex: 1; }
  nav button {
    text-align: left; background: none; border: none; color: #aab2c5;
    padding: 9px 10px; border-radius: 8px; cursor: pointer; font-size: 0.86rem;
  }
  nav button:hover { background: #1d2130; color: #e7e9ee; }
  nav button.active { background: #232a45; color: #fff; }
  .conn { padding-top: 10px; }
  main { display: flex; flex-direction: column; overflow: hidden; }
  .content { flex: 1; overflow-y: auto; padding: 20px 24px; }
  .runbar {
    border-top: 1px solid #272b38; padding: 12px 24px; display: flex;
    align-items: center; justify-content: space-between; gap: 16px; flex-wrap: wrap; background: #14161f;
  }
  .files { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
  .run-actions { display: flex; gap: 8px; }
  button { background: #272b38; color: #e7e9ee; border: none; border-radius: 8px; padding: 7px 12px; cursor: pointer; font-size: 0.85rem; }
  button:hover { background: #323748; }
  button.primary { background: #4f7cff; }
  button.primary:disabled { opacity: 0.5; cursor: default; }
  .badge { font-size: 0.75rem; padding: 4px 9px; border-radius: 999px; background: #272b38; }
  .badge.ok { background: #16361f; color: #6fe39a; }
  .badge.err { background: #3a1a1a; color: #ff8f8f; font-size: 0.68rem; }
  code { font-size: 0.78rem; color: #9aa3b8; word-break: break-all; }
  .result { display: block; margin: 0 24px 16px; background: #0f1117; padding: 10px; border-radius: 8px; max-height: 220px; overflow: auto; }
  .error { color: #ff8f8f; margin: 8px 24px; }
</style>
