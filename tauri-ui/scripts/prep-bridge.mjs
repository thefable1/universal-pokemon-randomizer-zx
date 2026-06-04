// Cross-platform (Linux/macOS/Windows) version of prep-bridge.sh.
//
// Assembles the Java bridge into src-tauri/resources/ so Tauri bundles it:
//   - web-bridge.jar : bridge + the whole randomizer engine + its data + gson
//   - jre/           : a minimal jlink'd Java runtime for THIS platform
//
// Run via `pnpm prep:bridge`; tauri's beforeBuildCommand runs it automatically.
// Node (not bash) so it works identically on the Windows/macOS CI runners.

import { execFileSync } from "node:child_process";
import { chmodSync, cpSync, existsSync, mkdirSync, readdirSync, rmSync, statSync } from "node:fs";
import { dirname, join } from "node:path";
import { fileURLToPath } from "node:url";

const isWin = process.platform === "win32";
const scriptDir = dirname(fileURLToPath(import.meta.url));
const tauriUi = join(scriptDir, "..");
const root = join(tauriUi, ".."); // repo root
const res = join(tauriUi, "src-tauri", "resources");
const jreDir = join(res, "jre");
const jarSrc = join(root, "web-bridge", "build", "libs", "web-bridge-all.jar");
const jarDst = join(res, "web-bridge.jar");

// Java tools resolve from JAVA_HOME/bin when set (CI sets it), else from PATH.
const javaBin = process.env.JAVA_HOME ? join(process.env.JAVA_HOME, "bin") : "";
const tool = (name) => (javaBin ? join(javaBin, name) : name);

function run(file, args, opts = {}) {
  console.log(`[prep-bridge] ${file} ${args.join(" ")}`);
  // shell:true lets Windows resolve .bat (gradlew) and .exe (PATHEXT).
  execFileSync(file, args, { stdio: "inherit", shell: true, ...opts });
}

// 1. Fat jar (incremental, cheap).
const gradlew = isWin ? join(root, "gradlew.bat") : join(root, "gradlew");
run(gradlew, [":web-bridge:fatJar", "-q", "--console=plain"], { cwd: root });
mkdirSync(res, { recursive: true });
cpSync(jarSrc, jarDst);

// 2. Minimal JRE (built once).
const javaExe = join(jreDir, "bin", isWin ? "java.exe" : "java");
if (!existsSync(javaExe)) {
  console.log("[prep-bridge] building minimal JRE with jlink…");
  const mods = execFileSync(
    tool("jdeps"),
    ["--print-module-deps", "--ignore-missing-deps", "--multi-release", "17", jarDst],
    { encoding: "utf8", shell: true },
  ).trim();
  const allMods = [
    ...new Set([...mods.split(",").map((m) => m.trim()).filter(Boolean), "jdk.httpserver", "jdk.unsupported"]),
  ].join(",");
  rmSync(jreDir, { recursive: true, force: true });
  run(tool("jlink"), [
    "--add-modules", allMods,
    "--strip-debug", "--no-header-files", "--no-man-pages", "--compress", "zip-6",
    "--output", jreDir,
  ]);
} else {
  console.log("[prep-bridge] JRE already present — skipping jlink");
}

// 3. jlink marks the JRE read-only. Tauri's resource copier (fs::copy without
// unlink) then can't overwrite on rebuilds -> EACCES (Linux) / access denied
// (Windows). Make every file writable. (chmod write-bit also clears the
// Windows read-only attribute via Node.)
function makeWritable(p) {
  // Add the owner-write bit, preserving execute (so bin/java stays runnable).
  // On Windows, setting the write bit clears the read-only attribute.
  try { chmodSync(p, statSync(p).mode | 0o200); } catch { /* best effort */ }
  if (statSync(p).isDirectory()) {
    for (const entry of readdirSync(p)) makeWritable(join(p, entry));
  }
}
makeWritable(jreDir);

console.log("[prep-bridge] done.");
