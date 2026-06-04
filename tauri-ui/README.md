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

Single launch — the app spawns the Java bridge itself:

```bash
cd tauri-ui
pnpm install
pnpm app           # first run compiles Rust + builds the JRE — a few minutes
```

`pnpm app` runs `prep:bridge` (assembles the bridge fat jar + a jlink'd JRE into
`src-tauri/resources/`), then launches the app; the Rust side starts the bundled
bridge on `127.0.0.1:7890` and kills it on exit. No separate `gradlew` terminal.

> If the bundled bridge can't start, the app falls back to an externally-run one
> (`./gradlew :web-bridge:run`) — handy when iterating on the Java side.

### NVIDIA / Wayland note

On some Linux GPU/compositor combos (notably NVIDIA + Wayland), WebKitGTK's
DMABUF renderer crashes the webview with `Error 71 (Protocol error) dispatching
to Wayland display`. The app fixes this itself: `src-tauri/src/lib.rs` sets
`WEBKIT_DISABLE_DMABUF_RENDERER=1` before the webview starts (Linux only, and
only if you haven't set it), so **the shipped binary works with no env var**.

- `pnpm app` (= `tauri dev`) — normal dev; the in-binary fix applies.
- `pnpm app:x11` — escape hatch that forces the webview through XWayland
  (`GDK_BACKEND=x11`) if native Wayland still misbehaves.

## Adding a setting

The UI mirrors a subset of the engine's ~120 options. Exposing another one is
three mechanical steps:

1. add the field to `SettingsRequest` (`../web-bridge/.../SettingsRequest.java`)
2. map it in `SettingsMapper` (one line)
3. add a control in the matching tab under `src/lib/tabs/`, bound to `settings.<field>`

Enable/disable is *derived* state (`$derived`), not hand-wired — e.g.
"Follow evolutions" is enabled `={settings.baseStatsMod === "RANDOM"}`.

## Building for distribution

```bash
cd tauri-ui
pnpm tauri build
```

`beforeBuildCommand` runs `prep:bridge` first, so the bundle includes the bridge
jar **and a minimal JRE** (jlink'd, ~53 MB). The shipped app (AppImage/.deb/.dmg
/.msi) is fully self-contained — **the end user does NOT need Java installed.**
Requires a JDK on the *build* machine (for `jlink`/`jdeps`).

## Status / TODO

- Covers ~140 of the engine's ~120-ish options across all 8 tabs.
- Single self-contained launch (bundled JRE sidecar). Done. ✅
- Remaining: ROM-dependent options (custom starters, "update to gen N",
  Pokémon-limit filters) need a `/api/rom/info` endpoint feeding the UI.
