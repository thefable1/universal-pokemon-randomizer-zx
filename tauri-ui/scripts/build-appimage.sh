#!/usr/bin/env bash
# Generates the AppImage from the AppDir that `pnpm tauri build` produced.
#
# Tauri's own `linuxdeploy` step fails on some systems ("failed to run
# linuxdeploy"), but it still creates the .AppDir. This runs the cached
# linuxdeploy-plugin-appimage directly on that AppDir to finish the job, and
# relativizes the absolute symlinks Tauri leaves behind so the AppImage is
# portable across machines.
#
# Usage:  pnpm tauri build   (may error at linuxdeploy — that's fine)
#         pnpm appimage       (this script)
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APPIMAGE_DIR="$SCRIPT_DIR/../src-tauri/target/release/bundle/appimage"

APPDIR="$(find "$APPIMAGE_DIR" -maxdepth 1 -name '*.AppDir' 2>/dev/null | head -1)"
[ -n "$APPDIR" ] || { echo "No .AppDir found in $APPIMAGE_DIR. Run 'pnpm tauri build' first."; exit 1; }
NAME="$(basename "$APPDIR" .AppDir)"

PLUGIN="$HOME/.cache/tauri/linuxdeploy-plugin-appimage.AppImage"
[ -f "$PLUGIN" ] || { echo "Cached plugin missing ($PLUGIN). Run 'pnpm tauri build' once to download it."; exit 1; }

# tauri/linuxdeploy leave absolute symlinks pointing at the BUILD path (e.g.
# /home/runner/.../tauri-ui.AppDir/...). On any other machine those are broken,
# and the AppImage AppRun segfaults in getline() when it can't open the .desktop.
# Relativize ANY absolute symlink that points inside the AppDir, matched by the
# AppDir's own name so it's robust to non-canonical paths (e.g. scripts/../).
echo ">> Relativizing absolute symlinks in $NAME.AppDir…"
find "$APPDIR" -type l | while read -r link; do
  tgt="$(readlink "$link")"
  case "$tgt" in
    /*"/$NAME.AppDir/"*)
      rel="${tgt##*"/$NAME.AppDir/"}"   # everything after .../<AppDir>/
      ln -sf "$rel" "$link"
      echo "   $(basename "$link") -> $rel" ;;
  esac
done

echo ">> Running linuxdeploy-plugin-appimage on $NAME.AppDir…"
cd "$APPIMAGE_DIR"
# Drop any AppImage tauri/linuxdeploy already emitted (it has the broken absolute
# symlinks); we ship only the relativized one the plugin produces below.
rm -f ./*.AppImage
export ARCH="${ARCH:-x86_64}"
export APPIMAGE_EXTRACT_AND_RUN=1   # let the plugin run even without FUSE
"$PLUGIN" --appdir="./$NAME.AppDir"

echo ">> Done:"
find "$APPIMAGE_DIR" -maxdepth 1 -name '*.AppImage' -printf '   %f  (%s bytes)\n'
