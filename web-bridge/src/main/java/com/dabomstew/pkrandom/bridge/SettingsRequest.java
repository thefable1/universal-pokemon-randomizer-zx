package com.dabomstew.pkrandom.bridge;

/**
 * The JSON contract between the frontend and the engine.
 *
 * Gson (de)serializes this plain DTO; {@link SettingsMapper} turns it into a
 * real engine {@link com.dabomstew.pkrandom.Settings}. Fields are grouped by the
 * UI tab they belong to. Enum-valued options are sent as the engine enum's
 * constant name (e.g. "RANDOM"); booleans/ints map 1:1.
 *
 * Adding another option is mechanical: add a field here, one line in
 * SettingsMapper, and a control in the matching Svelte tab.
 */
public class SettingsRequest {

    // --- Pokémon Traits ---
    public String baseStatsMod = "UNCHANGED";          // UNCHANGED | SHUFFLE | RANDOM
    public boolean baseStatsFollowEvolutions = false;
    public boolean standardizeExpCurves = false;
    public boolean updateBaseStats = false;

    public String typesMod = "UNCHANGED";              // UNCHANGED | RANDOM_FOLLOW_EVOLUTIONS | COMPLETELY_RANDOM
    public boolean dualTypeOnly = false;

    public String abilitiesMod = "UNCHANGED";          // UNCHANGED | RANDOMIZE
    public boolean allowWonderGuard = true;
    public boolean banTrappingAbilities = false;
    public boolean banNegativeAbilities = false;
    public boolean banBadAbilities = false;

    public String evolutionsMod = "UNCHANGED";         // UNCHANGED | RANDOM | RANDOM_EVERY_LEVEL
    public boolean changeImpossibleEvolutions = false;
    public boolean makeEvolutionsEasier = false;
    public boolean removeTimeBasedEvolutions = false;

    // --- Starters, Statics & Trades ---
    public String startersMod = "UNCHANGED";           // UNCHANGED | CUSTOM | COMPLETELY_RANDOM | RANDOM_WITH_TWO_EVOLUTIONS
    public boolean randomizeStartersHeldItems = false;
    public String staticPokemonMod = "UNCHANGED";      // UNCHANGED | RANDOM_MATCHING | COMPLETELY_RANDOM | SIMILAR_STRENGTH
    public String inGameTradesMod = "UNCHANGED";       // UNCHANGED | RANDOMIZE_GIVEN | RANDOMIZE_GIVEN_AND_REQUESTED

    // --- Moves & Movesets ---
    public boolean randomizeMovePowers = false;
    public boolean randomizeMoveAccuracies = false;
    public boolean randomizeMovePPs = false;
    public boolean randomizeMoveTypes = false;
    public boolean randomizeMoveCategory = false;
    public String movesetsMod = "UNCHANGED";           // UNCHANGED | RANDOM_PREFER_SAME_TYPE | COMPLETELY_RANDOM | METRONOME_ONLY
    public boolean startWithGuaranteedMoves = false;
    public boolean reorderDamagingMoves = false;

    // --- Foe Pokémon (trainers) ---
    public String trainersMod = "UNCHANGED";           // UNCHANGED | RANDOM | DISTRIBUTED | MAINPLAYTHROUGH | TYPE_THEMED | TYPE_THEMED_ELITE4_GYMS
    public boolean rivalCarriesStarterThroughout = false;
    public boolean trainersUsePokemonOfSimilarStrength = false;
    public boolean trainersBlockLegendaries = true;
    public boolean trainersForceFullyEvolved = false;
    public int trainersForceFullyEvolvedLevel = 30;

    // --- Wild Pokémon ---
    public String wildPokemonMod = "UNCHANGED";        // UNCHANGED | RANDOM | AREA_MAPPING | GLOBAL_MAPPING
    public boolean blockWildLegendaries = true;
    public boolean useTimeBasedEncounters = false;
    public boolean randomizeWildPokemonHeldItems = false;
    public boolean wildForceFullyEvolved = false;
    public int wildForceFullyEvolvedLevel = 30;

    // --- TM/HMs & Tutors ---
    public String tmsMod = "UNCHANGED";                // UNCHANGED | RANDOM
    public String tmsHmsCompatibilityMod = "UNCHANGED"; // UNCHANGED | RANDOM_PREFER_TYPE | COMPLETELY_RANDOM | FULL
    public boolean fullHMCompat = false;
    public boolean tmLevelUpMoveSanity = false;
    public boolean keepFieldMoveTMs = false;
    public String moveTutorMovesMod = "UNCHANGED";     // UNCHANGED | RANDOM
    public String moveTutorsCompatibilityMod = "UNCHANGED"; // UNCHANGED | RANDOM_PREFER_TYPE | COMPLETELY_RANDOM | FULL

    // --- Items ---
    public String fieldItemsMod = "UNCHANGED";         // UNCHANGED | SHUFFLE | RANDOM | RANDOM_EVEN
    public boolean banBadRandomFieldItems = false;
    public String shopItemsMod = "UNCHANGED";          // UNCHANGED | SHUFFLE | RANDOM
    public boolean banBadRandomShopItems = false;
    public boolean balanceShopPrices = false;
    public boolean guaranteeEvolutionItems = false;
    public String pickupItemsMod = "UNCHANGED";        // UNCHANGED | RANDOM

    // --- Misc Tweaks (mapped to the currentMiscTweaks bitfield) ---
    public boolean miscLowerCaseNames = false;
    public boolean miscNationalDexAtStart = false;
    public boolean miscUpdateTypeEffectiveness = false;
    public boolean miscFastestText = false;
    public boolean miscRunningShoesIndoors = false;
}
