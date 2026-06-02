# UPR-ZX — Tauri + Svelte UI

A modern desktop UI for the Universal Pokémon Randomizer ZX, built with
Tauri 2 + SvelteKit (Svelte 5). It does **not** reimplement the randomizer —
it talks to the existing Java engine over a small local HTTP bridge.

```
Svelte UI (this app)  ──HTTP/JSON──►  web-bridge (Java)  ──►  engine ──► ROM in/out
   in a Tauri webview                 127.0.0.1:7890
```

The frontend only ever sends/receives JSON; the engine's binary settings format
stays entirely server-side. See `../web-bridge` for the bridge.

## Prerequisites

- JDK (the repo targets 26; 17+ works) + the Gradle wrapper (`../gradlew`)
- Rust toolchain (`cargo`, `rustc`) and, on Linux, `webkit2gtk-4.1`
- `pnpm`

## Running it (dev)

Two terminals from the repo root:

```bash
# 1. start the engine bridge (http://127.0.0.1:7890)
./gradlew :web-bridge:run

# 2. start the desktop app  (first run compiles Rust — a few minutes)
cd tauri-ui
pnpm install
pnpm app           # = WEBKIT_DISABLE_DMABUF_RENDERER=1 tauri dev
```

### NVIDIA / Wayland note

On NVIDIA + Wayland, plain `pnpm tauri dev` crashes the webview with
`Error 71 (Protocol error) dispatching to Wayland display`. Use:

- `pnpm app` — disables WebKitGTK's DMABUF renderer (fixes most setups), or
- `pnpm app:x11` — also forces the webview through XWayland (most reliable).

These env vars only affect the webview; the Java bridge is unaffected.

## Adding a setting

The UI mirrors a subset of the engine's ~120 options. Exposing another one is
three mechanical steps:

1. add the field to `SettingsRequest` (`../web-bridge/.../SettingsRequest.java`)
2. map it in `SettingsMapper` (one line)
3. add a control in `src/routes/+page.svelte`, bound to `settings.<field>`

Enable/disable is *derived* state (`$derived`), not hand-wired — e.g.
"Follow evolutions" is enabled `={settings.baseStatsMod === "RANDOM"}`.

## Status / TODO

- Prototype: covers the "Pokémon Traits" + wild force-fully-evolved options.
- Dev runs the bridge as a separate process. For a shippable app, bundle the
  Java bridge as a **Tauri sidecar** (jlink/jpackage a JRE) so it auto-starts.
