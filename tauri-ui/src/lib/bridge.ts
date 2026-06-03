// Typed client for the Java engine's HTTP bridge (web-bridge module).
// Start the bridge with:  ./gradlew :web-bridge:run   (listens on 127.0.0.1:7890)

const BASE = "http://127.0.0.1:7890";

/** Mirrors com.dabomstew.pkrandom.bridge.SettingsRequest (grouped by UI tab). */
export interface SettingsRequest {
  // Pokémon Traits
  baseStatsMod: string;
  baseStatsFollowEvolutions: boolean;
  baseStatsFollowMegaEvolutions: boolean;
  assignEvoStatsRandomly: boolean;
  standardizeExpCurves: boolean;
  updateBaseStats: boolean;
  typesMod: string;
  typesFollowMegaEvolutions: boolean;
  dualTypeOnly: boolean;
  abilitiesMod: string;
  allowWonderGuard: boolean;
  abilitiesFollowEvolutions: boolean;
  abilitiesFollowMegaEvolutions: boolean;
  weighDuplicateAbilitiesTogether: boolean;
  ensureTwoAbilities: boolean;
  banTrappingAbilities: boolean;
  banNegativeAbilities: boolean;
  banBadAbilities: boolean;
  evolutionsMod: string;
  evosSimilarStrength: boolean;
  evosSameTyping: boolean;
  evosMaxThreeStages: boolean;
  evosForceChange: boolean;
  changeImpossibleEvolutions: boolean;
  makeEvolutionsEasier: boolean;
  removeTimeBasedEvolutions: boolean;
  // Starters, Statics & Trades
  startersMod: string;
  allowStarterAltFormes: boolean;
  randomizeStartersHeldItems: boolean;
  banBadRandomStarterHeldItems: boolean;
  staticPokemonMod: string;
  limitMainGameLegendaries: boolean;
  limit600: boolean;
  allowStaticAltFormes: boolean;
  swapStaticMegaEvos: boolean;
  inGameTradesMod: string;
  randomizeInGameTradesNicknames: boolean;
  randomizeInGameTradesOTs: boolean;
  randomizeInGameTradesIVs: boolean;
  randomizeInGameTradesItems: boolean;
  // Moves & Movesets
  randomizeMovePowers: boolean;
  randomizeMoveAccuracies: boolean;
  randomizeMovePPs: boolean;
  randomizeMoveTypes: boolean;
  randomizeMoveCategory: boolean;
  movesetsMod: string;
  startWithGuaranteedMoves: boolean;
  guaranteedMoveCount: number;
  reorderDamagingMoves: boolean;
  movesetsForceGoodDamaging: boolean;
  movesetsGoodDamagingPercent: number;
  blockBrokenMovesetMoves: boolean;
  evolutionMovesForAll: boolean;
  // Foe Pokémon
  trainersMod: string;
  rivalCarriesStarterThroughout: boolean;
  trainersUsePokemonOfSimilarStrength: boolean;
  trainersMatchTypingDistribution: boolean;
  trainersBlockLegendaries: boolean;
  betterTrainerMovesets: boolean;
  doubleBattleMode: boolean;
  shinyChance: boolean;
  randomizeTrainerNames: boolean;
  randomizeTrainerClassNames: boolean;
  trainersForceFullyEvolved: boolean;
  trainersForceFullyEvolvedLevel: number;
  trainersLevelModified: boolean;
  trainersLevelModifier: number;
  additionalBossTrainerPokemon: number;
  additionalImportantTrainerPokemon: number;
  additionalRegularTrainerPokemon: number;
  randomizeHeldItemsForBossTrainerPokemon: boolean;
  randomizeHeldItemsForImportantTrainerPokemon: boolean;
  randomizeHeldItemsForRegularTrainerPokemon: boolean;
  consumableItemsOnlyForTrainerPokemon: boolean;
  sensibleItemsOnlyForTrainerPokemon: boolean;
  highestLevelOnlyGetsItemsForTrainerPokemon: boolean;
  // Wild Pokémon
  wildPokemonMod: string;
  blockWildLegendaries: boolean;
  useTimeBasedEncounters: boolean;
  useMinimumCatchRate: boolean;
  minimumCatchRateLevel: number;
  randomizeWildPokemonHeldItems: boolean;
  banBadRandomWildPokemonHeldItems: boolean;
  balanceShakingGrass: boolean;
  allowWildAltFormes: boolean;
  wildLevelsModified: boolean;
  wildLevelModifier: number;
  wildForceFullyEvolved: boolean;
  wildForceFullyEvolvedLevel: number;
  // TM/HMs & Tutors
  tmsMod: string;
  tmsHmsCompatibilityMod: string;
  fullHMCompat: boolean;
  tmLevelUpMoveSanity: boolean;
  keepFieldMoveTMs: boolean;
  tmsForceGoodDamaging: boolean;
  tmsGoodDamagingPercent: number;
  blockBrokenTMMoves: boolean;
  tmsFollowEvolutions: boolean;
  moveTutorMovesMod: string;
  moveTutorsCompatibilityMod: string;
  tutorLevelUpMoveSanity: boolean;
  keepFieldMoveTutors: boolean;
  tutorsForceGoodDamaging: boolean;
  tutorsGoodDamagingPercent: number;
  blockBrokenTutorMoves: boolean;
  tutorFollowEvolutions: boolean;
  // Items
  fieldItemsMod: string;
  banBadRandomFieldItems: boolean;
  shopItemsMod: string;
  banBadRandomShopItems: boolean;
  banRegularShopItems: boolean;
  banOPShopItems: boolean;
  balanceShopPrices: boolean;
  guaranteeEvolutionItems: boolean;
  guaranteeXItems: boolean;
  pickupItemsMod: string;
  banBadRandomPickupItems: boolean;
  // Misc Tweaks
  miscBwExpPatch: boolean;
  miscNerfXAccuracy: boolean;
  miscFixCritRate: boolean;
  miscFastestText: boolean;
  miscRunningShoesIndoors: boolean;
  miscRandomizePcPotion: boolean;
  miscAllowPikachuEvolution: boolean;
  miscNationalDexAtStart: boolean;
  miscUpdateTypeEffectiveness: boolean;
  miscForceChallengeMode: boolean;
  miscLowerCaseNames: boolean;
  miscRandomizeCatchingTutorial: boolean;
  miscBanLuckyEgg: boolean;
  miscNoFreeLuckyEgg: boolean;
  miscBanBigManiacItems: boolean;
  miscSosBattlesForAll: boolean;
  miscBalanceStaticLevels: boolean;
  miscRetainAltFormes: boolean;
  miscRunWithoutRunningShoes: boolean;
  miscFasterHpExpBars: boolean;
  miscFastDistortionWorld: boolean;
  miscUpdateRotomFormeTyping: boolean;
  miscDisableLowHpMusic: boolean;
}

export interface HealthResponse { status: string; engineVersion: string; settingsFormatVersion: number; }
export interface EncodeResponse { settingsString: string; appliedSummary: Record<string, unknown>; }
export interface RandomizeResponse { success: boolean; game: string; generation: number; outputPath: string; log: string; }

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const res = await fetch(`${BASE}${path}`, init);
  const text = await res.text();
  const body = text ? JSON.parse(text) : {};
  if (!res.ok) throw new Error((body as any)?.error ?? `HTTP ${res.status}`);
  return body as T;
}
const post = (data: unknown): RequestInit =>
  ({ method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(data) });

export const health = () => request<HealthResponse>("/api/health");
export const encode = (s: SettingsRequest) => request<EncodeResponse>("/api/settings/encode", post(s));
export const randomize = (romPath: string, outputPath: string, settings: SettingsRequest) =>
  request<RandomizeResponse>("/api/randomize", post({ romPath, outputPath, settings }));

/** All-defaults settings. Booleans default false (except the few the engine defaults true). */
export function defaultSettings(): SettingsRequest {
  const s: any = {
    baseStatsMod: "UNCHANGED", typesMod: "UNCHANGED", abilitiesMod: "UNCHANGED", evolutionsMod: "UNCHANGED",
    startersMod: "UNCHANGED", staticPokemonMod: "UNCHANGED", inGameTradesMod: "UNCHANGED", movesetsMod: "UNCHANGED",
    trainersMod: "UNCHANGED", wildPokemonMod: "UNCHANGED", tmsMod: "UNCHANGED", tmsHmsCompatibilityMod: "UNCHANGED",
    moveTutorMovesMod: "UNCHANGED", moveTutorsCompatibilityMod: "UNCHANGED",
    fieldItemsMod: "UNCHANGED", shopItemsMod: "UNCHANGED", pickupItemsMod: "UNCHANGED",
    // numeric defaults
    guaranteedMoveCount: 2, movesetsGoodDamagingPercent: 0,
    trainersForceFullyEvolvedLevel: 30, trainersLevelModifier: 0,
    additionalBossTrainerPokemon: 0, additionalImportantTrainerPokemon: 0, additionalRegularTrainerPokemon: 0,
    minimumCatchRateLevel: 1, wildLevelModifier: 0, wildForceFullyEvolvedLevel: 30,
    tmsGoodDamagingPercent: 0, tutorsGoodDamagingPercent: 0,
    // engine defaults these to true
    allowWonderGuard: true, trainersBlockLegendaries: true, blockWildLegendaries: true,
  };
  // every other boolean defaults false
  for (const k of BOOLEAN_FALSE_DEFAULTS) if (!(k in s)) s[k] = false;
  return s as SettingsRequest;
}

const BOOLEAN_FALSE_DEFAULTS = [
  "baseStatsFollowEvolutions","baseStatsFollowMegaEvolutions","assignEvoStatsRandomly","standardizeExpCurves","updateBaseStats",
  "typesFollowMegaEvolutions","dualTypeOnly","abilitiesFollowEvolutions","abilitiesFollowMegaEvolutions",
  "weighDuplicateAbilitiesTogether","ensureTwoAbilities","banTrappingAbilities","banNegativeAbilities","banBadAbilities",
  "evosSimilarStrength","evosSameTyping","evosMaxThreeStages","evosForceChange","changeImpossibleEvolutions",
  "makeEvolutionsEasier","removeTimeBasedEvolutions","allowStarterAltFormes","randomizeStartersHeldItems",
  "banBadRandomStarterHeldItems","limitMainGameLegendaries","limit600","allowStaticAltFormes","swapStaticMegaEvos",
  "randomizeInGameTradesNicknames","randomizeInGameTradesOTs","randomizeInGameTradesIVs","randomizeInGameTradesItems",
  "randomizeMovePowers","randomizeMoveAccuracies","randomizeMovePPs","randomizeMoveTypes","randomizeMoveCategory",
  "startWithGuaranteedMoves","reorderDamagingMoves","movesetsForceGoodDamaging","blockBrokenMovesetMoves","evolutionMovesForAll",
  "rivalCarriesStarterThroughout","trainersUsePokemonOfSimilarStrength","trainersMatchTypingDistribution",
  "betterTrainerMovesets","doubleBattleMode","shinyChance","randomizeTrainerNames","randomizeTrainerClassNames",
  "trainersForceFullyEvolved","trainersLevelModified","randomizeHeldItemsForBossTrainerPokemon",
  "randomizeHeldItemsForImportantTrainerPokemon","randomizeHeldItemsForRegularTrainerPokemon",
  "consumableItemsOnlyForTrainerPokemon","sensibleItemsOnlyForTrainerPokemon","highestLevelOnlyGetsItemsForTrainerPokemon",
  "useTimeBasedEncounters","useMinimumCatchRate","randomizeWildPokemonHeldItems","banBadRandomWildPokemonHeldItems",
  "balanceShakingGrass","allowWildAltFormes","wildLevelsModified","wildForceFullyEvolved",
  "fullHMCompat","tmLevelUpMoveSanity","keepFieldMoveTMs","tmsForceGoodDamaging","blockBrokenTMMoves","tmsFollowEvolutions",
  "tutorLevelUpMoveSanity","keepFieldMoveTutors","tutorsForceGoodDamaging","blockBrokenTutorMoves","tutorFollowEvolutions",
  "banBadRandomFieldItems","banBadRandomShopItems","banRegularShopItems","banOPShopItems","balanceShopPrices",
  "guaranteeEvolutionItems","guaranteeXItems","banBadRandomPickupItems",
  "miscBwExpPatch","miscNerfXAccuracy","miscFixCritRate","miscFastestText","miscRunningShoesIndoors","miscRandomizePcPotion",
  "miscAllowPikachuEvolution","miscNationalDexAtStart","miscUpdateTypeEffectiveness","miscForceChallengeMode",
  "miscLowerCaseNames","miscRandomizeCatchingTutorial","miscBanLuckyEgg","miscNoFreeLuckyEgg","miscBanBigManiacItems",
  "miscSosBattlesForAll","miscBalanceStaticLevels","miscRetainAltFormes","miscRunWithoutRunningShoes","miscFasterHpExpBars",
  "miscFastDistortionWorld","miscUpdateRotomFormeTyping","miscDisableLowHpMusic",
];
