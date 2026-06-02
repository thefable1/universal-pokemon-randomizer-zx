// Typed client for the Java engine's HTTP bridge (web-bridge module).
// Start the bridge with:  ./gradlew :web-bridge:run   (listens on 127.0.0.1:7890)

const BASE = "http://127.0.0.1:7890";

/** Mirrors com.dabomstew.pkrandom.bridge.SettingsRequest (grouped by UI tab). */
export interface SettingsRequest {
  // Pokémon Traits
  baseStatsMod: string;
  baseStatsFollowEvolutions: boolean;
  standardizeExpCurves: boolean;
  updateBaseStats: boolean;
  typesMod: string;
  dualTypeOnly: boolean;
  abilitiesMod: string;
  allowWonderGuard: boolean;
  banTrappingAbilities: boolean;
  banNegativeAbilities: boolean;
  banBadAbilities: boolean;
  evolutionsMod: string;
  changeImpossibleEvolutions: boolean;
  makeEvolutionsEasier: boolean;
  removeTimeBasedEvolutions: boolean;
  // Starters, Statics & Trades
  startersMod: string;
  randomizeStartersHeldItems: boolean;
  staticPokemonMod: string;
  inGameTradesMod: string;
  // Moves & Movesets
  randomizeMovePowers: boolean;
  randomizeMoveAccuracies: boolean;
  randomizeMovePPs: boolean;
  randomizeMoveTypes: boolean;
  randomizeMoveCategory: boolean;
  movesetsMod: string;
  startWithGuaranteedMoves: boolean;
  reorderDamagingMoves: boolean;
  // Foe Pokémon
  trainersMod: string;
  rivalCarriesStarterThroughout: boolean;
  trainersUsePokemonOfSimilarStrength: boolean;
  trainersBlockLegendaries: boolean;
  trainersForceFullyEvolved: boolean;
  trainersForceFullyEvolvedLevel: number;
  // Wild Pokémon
  wildPokemonMod: string;
  blockWildLegendaries: boolean;
  useTimeBasedEncounters: boolean;
  randomizeWildPokemonHeldItems: boolean;
  wildForceFullyEvolved: boolean;
  wildForceFullyEvolvedLevel: number;
  // TM/HMs & Tutors
  tmsMod: string;
  tmsHmsCompatibilityMod: string;
  fullHMCompat: boolean;
  tmLevelUpMoveSanity: boolean;
  keepFieldMoveTMs: boolean;
  moveTutorMovesMod: string;
  moveTutorsCompatibilityMod: string;
  // Items
  fieldItemsMod: string;
  banBadRandomFieldItems: boolean;
  shopItemsMod: string;
  banBadRandomShopItems: boolean;
  balanceShopPrices: boolean;
  guaranteeEvolutionItems: boolean;
  pickupItemsMod: string;
  // Misc Tweaks
  miscLowerCaseNames: boolean;
  miscNationalDexAtStart: boolean;
  miscUpdateTypeEffectiveness: boolean;
  miscFastestText: boolean;
  miscRunningShoesIndoors: boolean;
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
  if (!res.ok) throw new Error((body as any)?.error ?? `HTTP ${res.status}`);
  return body as T;
}

const postJson = (path: string, data: unknown) =>
  ({ method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(data) }) as RequestInit;

export const health = () => request<HealthResponse>("/api/health");
export const encode = (settings: SettingsRequest) =>
  request<EncodeResponse>("/api/settings/encode", postJson("/api/settings/encode", settings));
export const randomize = (romPath: string, outputPath: string, settings: SettingsRequest) =>
  request<RandomizeResponse>("/api/randomize", postJson("/api/randomize", { romPath, outputPath, settings }));

export function defaultSettings(): SettingsRequest {
  return {
    baseStatsMod: "UNCHANGED", baseStatsFollowEvolutions: false, standardizeExpCurves: false, updateBaseStats: false,
    typesMod: "UNCHANGED", dualTypeOnly: false,
    abilitiesMod: "UNCHANGED", allowWonderGuard: true, banTrappingAbilities: false, banNegativeAbilities: false, banBadAbilities: false,
    evolutionsMod: "UNCHANGED", changeImpossibleEvolutions: false, makeEvolutionsEasier: false, removeTimeBasedEvolutions: false,
    startersMod: "UNCHANGED", randomizeStartersHeldItems: false, staticPokemonMod: "UNCHANGED", inGameTradesMod: "UNCHANGED",
    randomizeMovePowers: false, randomizeMoveAccuracies: false, randomizeMovePPs: false, randomizeMoveTypes: false, randomizeMoveCategory: false,
    movesetsMod: "UNCHANGED", startWithGuaranteedMoves: false, reorderDamagingMoves: false,
    trainersMod: "UNCHANGED", rivalCarriesStarterThroughout: false, trainersUsePokemonOfSimilarStrength: false,
    trainersBlockLegendaries: true, trainersForceFullyEvolved: false, trainersForceFullyEvolvedLevel: 30,
    wildPokemonMod: "UNCHANGED", blockWildLegendaries: true, useTimeBasedEncounters: false, randomizeWildPokemonHeldItems: false,
    wildForceFullyEvolved: false, wildForceFullyEvolvedLevel: 30,
    tmsMod: "UNCHANGED", tmsHmsCompatibilityMod: "UNCHANGED", fullHMCompat: false, tmLevelUpMoveSanity: false, keepFieldMoveTMs: false,
    moveTutorMovesMod: "UNCHANGED", moveTutorsCompatibilityMod: "UNCHANGED",
    fieldItemsMod: "UNCHANGED", banBadRandomFieldItems: false, shopItemsMod: "UNCHANGED", banBadRandomShopItems: false,
    balanceShopPrices: false, guaranteeEvolutionItems: false, pickupItemsMod: "UNCHANGED",
    miscLowerCaseNames: false, miscNationalDexAtStart: false, miscUpdateTypeEffectiveness: false, miscFastestText: false, miscRunningShoesIndoors: false,
  };
}
