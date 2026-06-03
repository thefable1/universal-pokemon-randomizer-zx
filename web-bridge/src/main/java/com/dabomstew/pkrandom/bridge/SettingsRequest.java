package com.dabomstew.pkrandom.bridge;

/**
 * The JSON contract between the frontend and the engine.
 *
 * Gson (de)serializes this plain DTO; {@link SettingsMapper} turns it into a
 * real engine {@link com.dabomstew.pkrandom.Settings}. Fields are grouped by the
 * UI tab they belong to. Enum-valued options are sent as the engine enum's
 * constant name (e.g. "RANDOM"); booleans/ints map 1:1.
 */
public class SettingsRequest {

    // --- Pokémon Traits ---
    public String baseStatsMod = "UNCHANGED";          // UNCHANGED | SHUFFLE | RANDOM
    public boolean baseStatsFollowEvolutions = false;
    public boolean baseStatsFollowMegaEvolutions = false;
    public boolean assignEvoStatsRandomly = false;
    public boolean standardizeExpCurves = false;
    public boolean updateBaseStats = false;

    public String typesMod = "UNCHANGED";              // UNCHANGED | RANDOM_FOLLOW_EVOLUTIONS | COMPLETELY_RANDOM
    public boolean typesFollowMegaEvolutions = false;
    public boolean dualTypeOnly = false;

    public String abilitiesMod = "UNCHANGED";          // UNCHANGED | RANDOMIZE
    public boolean allowWonderGuard = true;
    public boolean abilitiesFollowEvolutions = false;
    public boolean abilitiesFollowMegaEvolutions = false;
    public boolean weighDuplicateAbilitiesTogether = false;
    public boolean ensureTwoAbilities = false;
    public boolean banTrappingAbilities = false;
    public boolean banNegativeAbilities = false;
    public boolean banBadAbilities = false;

    public String evolutionsMod = "UNCHANGED";         // UNCHANGED | RANDOM | RANDOM_EVERY_LEVEL
    public boolean evosSimilarStrength = false;
    public boolean evosSameTyping = false;
    public boolean evosMaxThreeStages = false;
    public boolean evosForceChange = false;
    public boolean changeImpossibleEvolutions = false;
    public boolean makeEvolutionsEasier = false;
    public boolean removeTimeBasedEvolutions = false;

    // --- Starters, Statics & Trades ---
    public String startersMod = "UNCHANGED";           // UNCHANGED | CUSTOM | COMPLETELY_RANDOM | RANDOM_WITH_TWO_EVOLUTIONS
    public boolean allowStarterAltFormes = false;
    public boolean randomizeStartersHeldItems = false;
    public boolean banBadRandomStarterHeldItems = false;

    public String staticPokemonMod = "UNCHANGED";      // UNCHANGED | RANDOM_MATCHING | COMPLETELY_RANDOM | SIMILAR_STRENGTH
    public boolean limitMainGameLegendaries = false;
    public boolean limit600 = false;
    public boolean allowStaticAltFormes = false;
    public boolean swapStaticMegaEvos = false;

    public String inGameTradesMod = "UNCHANGED";       // UNCHANGED | RANDOMIZE_GIVEN | RANDOMIZE_GIVEN_AND_REQUESTED
    public boolean randomizeInGameTradesNicknames = false;
    public boolean randomizeInGameTradesOTs = false;
    public boolean randomizeInGameTradesIVs = false;
    public boolean randomizeInGameTradesItems = false;

    // --- Moves & Movesets ---
    public boolean randomizeMovePowers = false;
    public boolean randomizeMoveAccuracies = false;
    public boolean randomizeMovePPs = false;
    public boolean randomizeMoveTypes = false;
    public boolean randomizeMoveCategory = false;

    public String movesetsMod = "UNCHANGED";           // UNCHANGED | RANDOM_PREFER_SAME_TYPE | COMPLETELY_RANDOM | METRONOME_ONLY
    public boolean startWithGuaranteedMoves = false;
    public int guaranteedMoveCount = 2;                // 2..4
    public boolean reorderDamagingMoves = false;
    public boolean movesetsForceGoodDamaging = false;
    public int movesetsGoodDamagingPercent = 0;        // 0..100
    public boolean blockBrokenMovesetMoves = false;
    public boolean evolutionMovesForAll = false;

    // --- Foe Pokémon (trainers) ---
    public String trainersMod = "UNCHANGED";           // UNCHANGED | RANDOM | DISTRIBUTED | MAINPLAYTHROUGH | TYPE_THEMED | TYPE_THEMED_ELITE4_GYMS
    public boolean rivalCarriesStarterThroughout = false;
    public boolean trainersUsePokemonOfSimilarStrength = false;
    public boolean trainersMatchTypingDistribution = false;
    public boolean trainersBlockLegendaries = true;
    public boolean betterTrainerMovesets = false;
    public boolean doubleBattleMode = false;
    public boolean shinyChance = false;
    public boolean randomizeTrainerNames = false;
    public boolean randomizeTrainerClassNames = false;
    public boolean trainersForceFullyEvolved = false;
    public int trainersForceFullyEvolvedLevel = 30;
    public boolean trainersLevelModified = false;
    public int trainersLevelModifier = 0;              // -50..50
    public int additionalBossTrainerPokemon = 0;       // 0..5
    public int additionalImportantTrainerPokemon = 0;  // 0..5
    public int additionalRegularTrainerPokemon = 0;    // 0..5
    public boolean randomizeHeldItemsForBossTrainerPokemon = false;
    public boolean randomizeHeldItemsForImportantTrainerPokemon = false;
    public boolean randomizeHeldItemsForRegularTrainerPokemon = false;
    public boolean consumableItemsOnlyForTrainerPokemon = false;
    public boolean sensibleItemsOnlyForTrainerPokemon = false;
    public boolean highestLevelOnlyGetsItemsForTrainerPokemon = false;

    // --- Wild Pokémon ---
    public String wildPokemonMod = "UNCHANGED";        // UNCHANGED | RANDOM | AREA_MAPPING | GLOBAL_MAPPING
    public boolean blockWildLegendaries = true;
    public boolean useTimeBasedEncounters = false;
    public boolean useMinimumCatchRate = false;
    public int minimumCatchRateLevel = 1;              // 1..5
    public boolean randomizeWildPokemonHeldItems = false;
    public boolean banBadRandomWildPokemonHeldItems = false;
    public boolean balanceShakingGrass = false;
    public boolean allowWildAltFormes = false;
    public boolean wildLevelsModified = false;
    public int wildLevelModifier = 0;                  // -50..50
    public boolean wildForceFullyEvolved = false;
    public int wildForceFullyEvolvedLevel = 30;

    // --- TM/HMs & Tutors ---
    public String tmsMod = "UNCHANGED";                // UNCHANGED | RANDOM
    public String tmsHmsCompatibilityMod = "UNCHANGED"; // UNCHANGED | RANDOM_PREFER_TYPE | COMPLETELY_RANDOM | FULL
    public boolean fullHMCompat = false;
    public boolean tmLevelUpMoveSanity = false;
    public boolean keepFieldMoveTMs = false;
    public boolean tmsForceGoodDamaging = false;
    public int tmsGoodDamagingPercent = 0;             // 0..100
    public boolean blockBrokenTMMoves = false;
    public boolean tmsFollowEvolutions = false;
    public String moveTutorMovesMod = "UNCHANGED";     // UNCHANGED | RANDOM
    public String moveTutorsCompatibilityMod = "UNCHANGED"; // UNCHANGED | RANDOM_PREFER_TYPE | COMPLETELY_RANDOM | FULL
    public boolean tutorLevelUpMoveSanity = false;
    public boolean keepFieldMoveTutors = false;
    public boolean tutorsForceGoodDamaging = false;
    public int tutorsGoodDamagingPercent = 0;          // 0..100
    public boolean blockBrokenTutorMoves = false;
    public boolean tutorFollowEvolutions = false;

    // --- Items ---
    public String fieldItemsMod = "UNCHANGED";         // UNCHANGED | SHUFFLE | RANDOM | RANDOM_EVEN
    public boolean banBadRandomFieldItems = false;
    public String shopItemsMod = "UNCHANGED";          // UNCHANGED | SHUFFLE | RANDOM
    public boolean banBadRandomShopItems = false;
    public boolean banRegularShopItems = false;
    public boolean banOPShopItems = false;
    public boolean balanceShopPrices = false;
    public boolean guaranteeEvolutionItems = false;
    public boolean guaranteeXItems = false;
    public String pickupItemsMod = "UNCHANGED";        // UNCHANGED | RANDOM
    public boolean banBadRandomPickupItems = false;

    // --- Misc Tweaks (mapped to the currentMiscTweaks bitfield) ---
    public boolean miscBwExpPatch = false;
    public boolean miscNerfXAccuracy = false;
    public boolean miscFixCritRate = false;
    public boolean miscFastestText = false;
    public boolean miscRunningShoesIndoors = false;
    public boolean miscRandomizePcPotion = false;
    public boolean miscAllowPikachuEvolution = false;
    public boolean miscNationalDexAtStart = false;
    public boolean miscUpdateTypeEffectiveness = false;
    public boolean miscForceChallengeMode = false;
    public boolean miscLowerCaseNames = false;
    public boolean miscRandomizeCatchingTutorial = false;
    public boolean miscBanLuckyEgg = false;
    public boolean miscNoFreeLuckyEgg = false;
    public boolean miscBanBigManiacItems = false;
    public boolean miscSosBattlesForAll = false;
    public boolean miscBalanceStaticLevels = false;
    public boolean miscRetainAltFormes = false;
    public boolean miscRunWithoutRunningShoes = false;
    public boolean miscFasterHpExpBars = false;
    public boolean miscFastDistortionWorld = false;
    public boolean miscUpdateRotomFormeTyping = false;
    public boolean miscDisableLowHpMusic = false;
}
