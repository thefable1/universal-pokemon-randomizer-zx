// Typed client for the Java engine's HTTP bridge (web-bridge module).
// Start the bridge with:  ./gradlew :web-bridge:run   (listens on 127.0.0.1:7890)

const BASE = "http://127.0.0.1:7890";

export type BaseStatsMod = "UNCHANGED" | "SHUFFLE" | "RANDOM";
export type TypesMod = "UNCHANGED" | "RANDOM_FOLLOW_EVOLUTIONS" | "COMPLETELY_RANDOM";

/** Mirrors com.dabomstew.pkrandom.bridge.SettingsRequest. */
export interface SettingsRequest {
  baseStatsMod: BaseStatsMod;
  baseStatsFollowEvolutions: boolean;
  standardizeExpCurves: boolean;
  updateBaseStats: boolean;
  typesMod: TypesMod;
  dualTypeOnly: boolean;
  wildForceFullyEvolved: boolean;
  wildForceFullyEvolvedLevel: number;
}

export interface HealthResponse {
  status: string;
  engineVersion: string;
  settingsFormatVersion: number;
}

export interface EncodeResponse {
  settingsString: string;
  appliedSummary: Record<string, unknown>;
}

export interface RandomizeResponse {
  success: boolean;
  game: string;
  generation: number;
  outputPath: string;
  log: string;
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE}${path}`, init);
  const text = await res.text();
  const body = text ? JSON.parse(text) : {};
  if (!res.ok) {
    throw new Error(body?.error ?? `HTTP ${res.status}`);
  }
  return body as T;
}

export function health(): Promise<HealthResponse> {
  return request<HealthResponse>("/api/health");
}

export function encode(settings: SettingsRequest): Promise<EncodeResponse> {
  return request<EncodeResponse>("/api/settings/encode", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(settings),
  });
}

export function randomize(
  romPath: string,
  outputPath: string,
  settings: SettingsRequest,
): Promise<RandomizeResponse> {
  return request<RandomizeResponse>("/api/randomize", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ romPath, outputPath, settings }),
  });
}

export function defaultSettings(): SettingsRequest {
  return {
    baseStatsMod: "UNCHANGED",
    baseStatsFollowEvolutions: false,
    standardizeExpCurves: false,
    updateBaseStats: false,
    typesMod: "UNCHANGED",
    dualTypeOnly: false,
    wildForceFullyEvolved: false,
    wildForceFullyEvolvedLevel: 30,
  };
}
