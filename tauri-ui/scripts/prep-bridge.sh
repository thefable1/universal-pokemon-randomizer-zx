#!/usr/bin/env bash
# Assembles the Java bridge (fat jar) + a minimal jlink'd JRE into
# src-tauri/resources/ so Tauri bundles them into the shipped app. Idempotent:
# the fat jar is rebuilt every time (Gradle is incremental, so it's cheap), the
# JRE is built once.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TAURI_UI="$(cd "$SCRIPT_DIR/.." && pwd)"
ROOT="$(cd "$TAURI_UI/.." && pwd)"
RES="$TAURI_UI/src-tauri/resources"
mkdir -p "$RES"

echo "[prep-bridge] building bridge fat jar..."
(cd "$ROOT" && ./gradlew :web-bridge:fatJar -q --console=plain)
cp "$ROOT/web-bridge/build/libs/web-bridge-all.jar" "$RES/web-bridge.jar"

if [ ! -x "$RES/jre/bin/java" ]; then
  echo "[prep-bridge] building minimal JRE with jlink (one-time)..."
  MODS="$(jdeps --print-module-deps --ignore-missing-deps --multi-release 17 "$RES/web-bridge.jar" 2>/dev/null)"
  MODS="${MODS},jdk.httpserver,jdk.unsupported"
  rm -rf "$RES/jre"
  jlink --add-modules "$MODS" \
        --strip-debug --no-header-files --no-man-pages --compress zip-6 \
        --output "$RES/jre"
else
  echo "[prep-bridge] JRE already present — skipping jlink"
fi

# jlink marks JRE files read-only. Tauri's resource copier (tauri-build's
# copy_resources -> fs::copy) doesn't unlink the destination first, so on a
# rebuild it can't overwrite the read-only copies and fails with EACCES.
# Make the tree writable so re-copies succeed.
chmod -R u+w "$RES/jre"

echo "[prep-bridge] done: $RES (jar $(du -h "$RES/web-bridge.jar" | cut -f1), jre $(du -sh "$RES/jre" | cut -f1))"
