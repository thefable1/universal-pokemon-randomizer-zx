use std::process::{Child, Command};
use std::sync::Mutex;
use tauri::{Manager, RunEvent};

/// Holds the spawned Java bridge process so we can kill it when the app exits.
struct BridgeProcess(Mutex<Option<Child>>);

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    // WebKitGTK's DMABUF renderer crashes on some Linux GPU/compositor combos
    // (NVIDIA + Wayland in particular: "Error 71 dispatching to Wayland display").
    // Disabling it is the standard fix. Set it here -- before the webview is
    // created -- so the shipped binary works with no user-set env var. We only
    // set it when the user hasn't, so anyone on a working setup can override.
    // NOTE: on edition 2024 set_var becomes unsafe and needs an `unsafe { }` block.
    #[cfg(target_os = "linux")]
    if std::env::var_os("WEBKIT_DISABLE_DMABUF_RENDERER").is_none() {
        std::env::set_var("WEBKIT_DISABLE_DMABUF_RENDERER", "1");
    }

    let app = tauri::Builder::default()
        .plugin(tauri_plugin_opener::init())
        // Native open/save dialogs for picking the ROM in/out files.
        .plugin(tauri_plugin_dialog::init())
        .manage(BridgeProcess(Mutex::new(None)))
        .setup(|app| {
            if let Some(child) = spawn_bridge(app.handle()) {
                *app.state::<BridgeProcess>().0.lock().unwrap() = Some(child);
            }
            Ok(())
        })
        .build(tauri::generate_context!())
        .expect("error while running tauri application");

    app.run(|app_handle, event| {
        // Don't leave a stray Java process behind when the window closes.
        if let RunEvent::Exit = event {
            if let Some(state) = app_handle.try_state::<BridgeProcess>() {
                if let Some(mut child) = state.0.lock().unwrap().take() {
                    let _ = child.kill();
                }
            }
        }
    });
}

/// Launches the bundled Java bridge (jlink'd JRE + fat jar that contains the
/// whole randomizer engine). Returns None when the sidecar isn't present -- e.g.
/// in `tauri dev`, where you instead run `./gradlew :web-bridge:run` yourself.
fn spawn_bridge(app: &tauri::AppHandle) -> Option<Child> {
    let java_name = if cfg!(windows) { "java.exe" } else { "java" };

    // Look in the bundled resource dir (release) first, then the source tree
    // that `prep:bridge` populates (used by `tauri dev`). The bundle maps
    // resources/jre -> <resource_dir>/jre and resources/web-bridge.jar -> root.
    let mut bases: Vec<std::path::PathBuf> = Vec::new();
    if let Ok(rd) = app.path().resource_dir() {
        bases.push(rd);
    }
    bases.push(std::path::Path::new(env!("CARGO_MANIFEST_DIR")).join("resources"));

    for base in bases {
        let java = base.join("jre/bin").join(java_name);
        let jar = base.join("web-bridge.jar");
        if java.exists() && jar.exists() {
            return match Command::new(&java).arg("-jar").arg(&jar).spawn() {
                Ok(child) => Some(child),
                Err(e) => {
                    eprintln!("Failed to start bundled bridge: {e}");
                    None
                }
            };
        }
    }

    eprintln!("Bridge sidecar not found. Run `pnpm prep:bridge`, or `./gradlew :web-bridge:run`.");
    None
}
