<script lang="ts">
  import { getCurrentWindow } from "@tauri-apps/api/window";

  // getCurrentWindow() is only available inside the Tauri runtime; guard so the
  // page still renders in a plain browser (pnpm dev without Tauri).
  let appWindow: ReturnType<typeof getCurrentWindow> | null = null;
  try {
    appWindow = getCurrentWindow();
  } catch {
    appWindow = null;
  }

  let { title = "Universal Pokémon Randomizer ZX" }: { title?: string } = $props();
</script>

<!-- data-tauri-drag-region makes the bar draggable; buttons (without it) stay clickable -->
<div class="titlebar" role="toolbar" tabindex="-1" data-tauri-drag-region ondblclick={() => appWindow?.toggleMaximize()}>
  <span class="title" data-tauri-drag-region>{title}</span>
  <div class="controls">
    <button class="ctl" title="Minimize" onclick={() => appWindow?.minimize()} aria-label="Minimize">
      <svg viewBox="0 0 10 10"><line x1="1" y1="5" x2="9" y2="5" /></svg>
    </button>
    <button class="ctl" title="Maximize" onclick={() => appWindow?.toggleMaximize()} aria-label="Maximize">
      <svg viewBox="0 0 10 10"><rect x="1.5" y="1.5" width="7" height="7" fill="none" /></svg>
    </button>
    <button class="ctl close" title="Close" onclick={() => appWindow?.close()} aria-label="Close">
      <svg viewBox="0 0 10 10"><line x1="1.5" y1="1.5" x2="8.5" y2="8.5" /><line x1="8.5" y1="1.5" x2="1.5" y2="8.5" /></svg>
    </button>
  </div>
</div>

<style>
  .titlebar {
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: #14161f;
    border-bottom: 1px solid #272b38;
    user-select: none;
    flex-shrink: 0;
  }
  .title {
    font-size: 0.82rem;
    color: #aab2c5;
    padding-left: 14px;
    pointer-events: none; /* let the drag-region under it receive the drag */
  }
  .controls { display: flex; height: 100%; }
  .ctl {
    width: 44px;
    height: 100%;
    border: none;
    background: none;
    color: #aab2c5;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .ctl svg { width: 11px; height: 11px; stroke: currentColor; stroke-width: 1.1; }
  .ctl:hover { background: #232a45; color: #fff; }
  .ctl.close:hover { background: #c2384a; color: #fff; }
</style>
