package com.dabomstew.pkrandom.bridge;

import com.dabomstew.pkrandom.MiscTweak;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.pokemon.ExpCurve;

/**
 * Maps the JSON {@link SettingsRequest} DTO onto a real engine {@link Settings}.
 *
 * The frontend never touches the engine's binary settings format -- it sends
 * field values as JSON and this class applies them through the normal setters.
 * Enum settings take booleans in enum-ordinal order, so {@link #ordinalFlags}
 * builds the flag array from the enum constant the frontend names.
 */
final class SettingsMapper {

    private SettingsMapper() {}

    static Settings toSettings(SettingsRequest r) {
        Settings s = new Settings();
        // Normally set on ROM load; defaults so Settings.toString() works ROM-less.
        s.setRomName("Bridge");
        s.setSelectedEXPCurve(ExpCurve.MEDIUM_FAST);

        // --- Pokémon Traits ---
        s.setBaseStatisticsMod(flags(Settings.BaseStatisticsMod.class, r.baseStatsMod));
        s.setBaseStatsFollowEvolutions(r.baseStatsFollowEvolutions);
        s.setStandardizeEXPCurves(r.standardizeExpCurves);
        s.setUpdateBaseStats(r.updateBaseStats);

        s.setTypesMod(flags(Settings.TypesMod.class, r.typesMod));
        s.setDualTypeOnly(r.dualTypeOnly);

        s.setAbilitiesMod(flags(Settings.AbilitiesMod.class, r.abilitiesMod));
        s.setAllowWonderGuard(r.allowWonderGuard);
        s.setBanTrappingAbilities(r.banTrappingAbilities);
        s.setBanNegativeAbilities(r.banNegativeAbilities);
        s.setBanBadAbilities(r.banBadAbilities);

        s.setEvolutionsMod(flags(Settings.EvolutionsMod.class, r.evolutionsMod));
        s.setChangeImpossibleEvolutions(r.changeImpossibleEvolutions);
        s.setMakeEvolutionsEasier(r.makeEvolutionsEasier);
        s.setRemoveTimeBasedEvolutions(r.removeTimeBasedEvolutions);

        // --- Starters, Statics & Trades ---
        s.setStartersMod(flags(Settings.StartersMod.class, r.startersMod));
        s.setRandomizeStartersHeldItems(r.randomizeStartersHeldItems);
        s.setStaticPokemonMod(flags(Settings.StaticPokemonMod.class, r.staticPokemonMod));
        s.setInGameTradesMod(flags(Settings.InGameTradesMod.class, r.inGameTradesMod));

        // --- Moves & Movesets ---
        s.setRandomizeMovePowers(r.randomizeMovePowers);
        s.setRandomizeMoveAccuracies(r.randomizeMoveAccuracies);
        s.setRandomizeMovePPs(r.randomizeMovePPs);
        s.setRandomizeMoveTypes(r.randomizeMoveTypes);
        s.setRandomizeMoveCategory(r.randomizeMoveCategory);
        s.setMovesetsMod(flags(Settings.MovesetsMod.class, r.movesetsMod));
        s.setStartWithGuaranteedMoves(r.startWithGuaranteedMoves);
        s.setReorderDamagingMoves(r.reorderDamagingMoves);

        // --- Foe Pokémon (trainers) ---
        s.setTrainersMod(flags(Settings.TrainersMod.class, r.trainersMod));
        s.setRivalCarriesStarterThroughout(r.rivalCarriesStarterThroughout);
        s.setTrainersUsePokemonOfSimilarStrength(r.trainersUsePokemonOfSimilarStrength);
        s.setTrainersBlockLegendaries(r.trainersBlockLegendaries);
        s.setTrainersForceFullyEvolved(r.trainersForceFullyEvolved);
        s.setTrainersForceFullyEvolvedLevel(r.trainersForceFullyEvolvedLevel);

        // --- Wild Pokémon ---
        s.setWildPokemonMod(flags(Settings.WildPokemonMod.class, r.wildPokemonMod));
        s.setBlockWildLegendaries(r.blockWildLegendaries);
        s.setUseTimeBasedEncounters(r.useTimeBasedEncounters);
        s.setRandomizeWildPokemonHeldItems(r.randomizeWildPokemonHeldItems);
        s.setWildForceFullyEvolved(r.wildForceFullyEvolved);
        s.setWildForceFullyEvolvedLevel(r.wildForceFullyEvolvedLevel);

        // --- TM/HMs & Tutors ---
        s.setTmsMod(flags(Settings.TMsMod.class, r.tmsMod));
        s.setTmsHmsCompatibilityMod(flags(Settings.TMsHMsCompatibilityMod.class, r.tmsHmsCompatibilityMod));
        s.setFullHMCompat(r.fullHMCompat);
        s.setTmLevelUpMoveSanity(r.tmLevelUpMoveSanity);
        s.setKeepFieldMoveTMs(r.keepFieldMoveTMs);
        s.setMoveTutorMovesMod(flags(Settings.MoveTutorMovesMod.class, r.moveTutorMovesMod));
        s.setMoveTutorsCompatibilityMod(flags(Settings.MoveTutorsCompatibilityMod.class, r.moveTutorsCompatibilityMod));

        // --- Items ---
        s.setFieldItemsMod(flags(Settings.FieldItemsMod.class, r.fieldItemsMod));
        s.setBanBadRandomFieldItems(r.banBadRandomFieldItems);
        s.setShopItemsMod(flags(Settings.ShopItemsMod.class, r.shopItemsMod));
        s.setBanBadRandomShopItems(r.banBadRandomShopItems);
        s.setBalanceShopPrices(r.balanceShopPrices);
        s.setGuaranteeEvolutionItems(r.guaranteeEvolutionItems);
        s.setPickupItemsMod(flags(Settings.PickupItemsMod.class, r.pickupItemsMod));

        // --- Misc Tweaks (OR the selected tweak flags into one bitfield) ---
        int tweaks = 0;
        if (r.miscLowerCaseNames) tweaks |= MiscTweak.LOWER_CASE_POKEMON_NAMES.getValue();
        if (r.miscNationalDexAtStart) tweaks |= MiscTweak.NATIONAL_DEX_AT_START.getValue();
        if (r.miscUpdateTypeEffectiveness) tweaks |= MiscTweak.UPDATE_TYPE_EFFECTIVENESS.getValue();
        if (r.miscFastestText) tweaks |= MiscTweak.FASTEST_TEXT.getValue();
        if (r.miscRunningShoesIndoors) tweaks |= MiscTweak.RUNNING_SHOES_INDOORS.getValue();
        s.setCurrentMiscTweaks(tweaks);

        return s;
    }

    /** Builds the ordinal-order boolean flags an enum setter expects from a constant name. */
    private static <E extends Enum<E>> boolean[] flags(Class<E> type, String constant) {
        E[] values = type.getEnumConstants();
        E selected = Enum.valueOf(type, constant);
        boolean[] result = new boolean[values.length];
        result[selected.ordinal()] = true;
        return result;
    }
}
