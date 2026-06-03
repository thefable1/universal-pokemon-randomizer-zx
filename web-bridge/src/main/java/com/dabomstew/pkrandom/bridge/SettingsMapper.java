package com.dabomstew.pkrandom.bridge;

import com.dabomstew.pkrandom.MiscTweak;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.pokemon.ExpCurve;

/**
 * Maps the JSON {@link SettingsRequest} DTO onto a real engine {@link Settings}.
 *
 * The frontend never touches the engine's binary settings format -- it sends
 * field values as JSON and this class applies them through the normal setters.
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
        s.setBaseStatsFollowMegaEvolutions(r.baseStatsFollowMegaEvolutions);
        s.setAssignEvoStatsRandomly(r.assignEvoStatsRandomly);
        s.setStandardizeEXPCurves(r.standardizeExpCurves);
        s.setUpdateBaseStats(r.updateBaseStats);

        s.setTypesMod(flags(Settings.TypesMod.class, r.typesMod));
        s.setTypesFollowMegaEvolutions(r.typesFollowMegaEvolutions);
        s.setDualTypeOnly(r.dualTypeOnly);

        s.setAbilitiesMod(flags(Settings.AbilitiesMod.class, r.abilitiesMod));
        s.setAllowWonderGuard(r.allowWonderGuard);
        s.setAbilitiesFollowEvolutions(r.abilitiesFollowEvolutions);
        s.setAbilitiesFollowMegaEvolutions(r.abilitiesFollowMegaEvolutions);
        s.setWeighDuplicateAbilitiesTogether(r.weighDuplicateAbilitiesTogether);
        s.setEnsureTwoAbilities(r.ensureTwoAbilities);
        s.setBanTrappingAbilities(r.banTrappingAbilities);
        s.setBanNegativeAbilities(r.banNegativeAbilities);
        s.setBanBadAbilities(r.banBadAbilities);

        s.setEvolutionsMod(flags(Settings.EvolutionsMod.class, r.evolutionsMod));
        s.setEvosSimilarStrength(r.evosSimilarStrength);
        s.setEvosSameTyping(r.evosSameTyping);
        s.setEvosMaxThreeStages(r.evosMaxThreeStages);
        s.setEvosForceChange(r.evosForceChange);
        s.setChangeImpossibleEvolutions(r.changeImpossibleEvolutions);
        s.setMakeEvolutionsEasier(r.makeEvolutionsEasier);
        s.setRemoveTimeBasedEvolutions(r.removeTimeBasedEvolutions);

        // --- Starters, Statics & Trades ---
        s.setStartersMod(flags(Settings.StartersMod.class, r.startersMod));
        s.setAllowStarterAltFormes(r.allowStarterAltFormes);
        s.setRandomizeStartersHeldItems(r.randomizeStartersHeldItems);
        s.setBanBadRandomStarterHeldItems(r.banBadRandomStarterHeldItems);

        s.setStaticPokemonMod(flags(Settings.StaticPokemonMod.class, r.staticPokemonMod));
        s.setLimitMainGameLegendaries(r.limitMainGameLegendaries);
        s.setLimit600(r.limit600);
        s.setAllowStaticAltFormes(r.allowStaticAltFormes);
        s.setSwapStaticMegaEvos(r.swapStaticMegaEvos);

        s.setInGameTradesMod(flags(Settings.InGameTradesMod.class, r.inGameTradesMod));
        s.setRandomizeInGameTradesNicknames(r.randomizeInGameTradesNicknames);
        s.setRandomizeInGameTradesOTs(r.randomizeInGameTradesOTs);
        s.setRandomizeInGameTradesIVs(r.randomizeInGameTradesIVs);
        s.setRandomizeInGameTradesItems(r.randomizeInGameTradesItems);

        // --- Moves & Movesets ---
        s.setRandomizeMovePowers(r.randomizeMovePowers);
        s.setRandomizeMoveAccuracies(r.randomizeMoveAccuracies);
        s.setRandomizeMovePPs(r.randomizeMovePPs);
        s.setRandomizeMoveTypes(r.randomizeMoveTypes);
        s.setRandomizeMoveCategory(r.randomizeMoveCategory);

        s.setMovesetsMod(flags(Settings.MovesetsMod.class, r.movesetsMod));
        s.setStartWithGuaranteedMoves(r.startWithGuaranteedMoves);
        s.setGuaranteedMoveCount(r.guaranteedMoveCount);
        s.setReorderDamagingMoves(r.reorderDamagingMoves);
        s.setMovesetsForceGoodDamaging(r.movesetsForceGoodDamaging);
        s.setMovesetsGoodDamagingPercent(r.movesetsGoodDamagingPercent);
        s.setBlockBrokenMovesetMoves(r.blockBrokenMovesetMoves);
        s.setEvolutionMovesForAll(r.evolutionMovesForAll);

        // --- Foe Pokémon (trainers) ---
        s.setTrainersMod(flags(Settings.TrainersMod.class, r.trainersMod));
        s.setRivalCarriesStarterThroughout(r.rivalCarriesStarterThroughout);
        s.setTrainersUsePokemonOfSimilarStrength(r.trainersUsePokemonOfSimilarStrength);
        s.setTrainersMatchTypingDistribution(r.trainersMatchTypingDistribution);
        s.setTrainersBlockLegendaries(r.trainersBlockLegendaries);
        s.setBetterTrainerMovesets(r.betterTrainerMovesets);
        s.setDoubleBattleMode(r.doubleBattleMode);
        s.setShinyChance(r.shinyChance);
        s.setRandomizeTrainerNames(r.randomizeTrainerNames);
        s.setRandomizeTrainerClassNames(r.randomizeTrainerClassNames);
        s.setTrainersForceFullyEvolved(r.trainersForceFullyEvolved);
        s.setTrainersForceFullyEvolvedLevel(r.trainersForceFullyEvolvedLevel);
        s.setTrainersLevelModified(r.trainersLevelModified);
        s.setTrainersLevelModifier(r.trainersLevelModifier);
        s.setAdditionalBossTrainerPokemon(r.additionalBossTrainerPokemon);
        s.setAdditionalImportantTrainerPokemon(r.additionalImportantTrainerPokemon);
        s.setAdditionalRegularTrainerPokemon(r.additionalRegularTrainerPokemon);
        s.setRandomizeHeldItemsForBossTrainerPokemon(r.randomizeHeldItemsForBossTrainerPokemon);
        s.setRandomizeHeldItemsForImportantTrainerPokemon(r.randomizeHeldItemsForImportantTrainerPokemon);
        s.setRandomizeHeldItemsForRegularTrainerPokemon(r.randomizeHeldItemsForRegularTrainerPokemon);
        s.setConsumableItemsOnlyForTrainers(r.consumableItemsOnlyForTrainerPokemon);
        s.setSensibleItemsOnlyForTrainers(r.sensibleItemsOnlyForTrainerPokemon);
        s.setHighestLevelGetsItemsForTrainers(r.highestLevelOnlyGetsItemsForTrainerPokemon);

        // --- Wild Pokémon ---
        s.setWildPokemonMod(flags(Settings.WildPokemonMod.class, r.wildPokemonMod));
        s.setBlockWildLegendaries(r.blockWildLegendaries);
        s.setUseTimeBasedEncounters(r.useTimeBasedEncounters);
        s.setUseMinimumCatchRate(r.useMinimumCatchRate);
        s.setMinimumCatchRateLevel(r.minimumCatchRateLevel);
        s.setRandomizeWildPokemonHeldItems(r.randomizeWildPokemonHeldItems);
        s.setBanBadRandomWildPokemonHeldItems(r.banBadRandomWildPokemonHeldItems);
        s.setBalanceShakingGrass(r.balanceShakingGrass);
        s.setAllowWildAltFormes(r.allowWildAltFormes);
        s.setWildLevelsModified(r.wildLevelsModified);
        s.setWildLevelModifier(r.wildLevelModifier);
        s.setWildForceFullyEvolved(r.wildForceFullyEvolved);
        s.setWildForceFullyEvolvedLevel(r.wildForceFullyEvolvedLevel);

        // --- TM/HMs & Tutors ---
        s.setTmsMod(flags(Settings.TMsMod.class, r.tmsMod));
        s.setTmsHmsCompatibilityMod(flags(Settings.TMsHMsCompatibilityMod.class, r.tmsHmsCompatibilityMod));
        s.setFullHMCompat(r.fullHMCompat);
        s.setTmLevelUpMoveSanity(r.tmLevelUpMoveSanity);
        s.setKeepFieldMoveTMs(r.keepFieldMoveTMs);
        s.setTmsForceGoodDamaging(r.tmsForceGoodDamaging);
        s.setTmsGoodDamagingPercent(r.tmsGoodDamagingPercent);
        s.setBlockBrokenTMMoves(r.blockBrokenTMMoves);
        s.setTmsFollowEvolutions(r.tmsFollowEvolutions);
        s.setMoveTutorMovesMod(flags(Settings.MoveTutorMovesMod.class, r.moveTutorMovesMod));
        s.setMoveTutorsCompatibilityMod(flags(Settings.MoveTutorsCompatibilityMod.class, r.moveTutorsCompatibilityMod));
        s.setTutorLevelUpMoveSanity(r.tutorLevelUpMoveSanity);
        s.setKeepFieldMoveTutors(r.keepFieldMoveTutors);
        s.setTutorsForceGoodDamaging(r.tutorsForceGoodDamaging);
        s.setTutorsGoodDamagingPercent(r.tutorsGoodDamagingPercent);
        s.setBlockBrokenTutorMoves(r.blockBrokenTutorMoves);
        s.setTutorFollowEvolutions(r.tutorFollowEvolutions);

        // --- Items ---
        s.setFieldItemsMod(flags(Settings.FieldItemsMod.class, r.fieldItemsMod));
        s.setBanBadRandomFieldItems(r.banBadRandomFieldItems);
        s.setShopItemsMod(flags(Settings.ShopItemsMod.class, r.shopItemsMod));
        s.setBanBadRandomShopItems(r.banBadRandomShopItems);
        s.setBanRegularShopItems(r.banRegularShopItems);
        s.setBanOPShopItems(r.banOPShopItems);
        s.setBalanceShopPrices(r.balanceShopPrices);
        s.setGuaranteeEvolutionItems(r.guaranteeEvolutionItems);
        s.setGuaranteeXItems(r.guaranteeXItems);
        s.setPickupItemsMod(flags(Settings.PickupItemsMod.class, r.pickupItemsMod));
        s.setBanBadRandomPickupItems(r.banBadRandomPickupItems);

        // --- Misc Tweaks (OR the selected tweak flags into one bitfield) ---
        int t = 0;
        if (r.miscBwExpPatch) t |= MiscTweak.BW_EXP_PATCH.getValue();
        if (r.miscNerfXAccuracy) t |= MiscTweak.NERF_X_ACCURACY.getValue();
        if (r.miscFixCritRate) t |= MiscTweak.FIX_CRIT_RATE.getValue();
        if (r.miscFastestText) t |= MiscTweak.FASTEST_TEXT.getValue();
        if (r.miscRunningShoesIndoors) t |= MiscTweak.RUNNING_SHOES_INDOORS.getValue();
        if (r.miscRandomizePcPotion) t |= MiscTweak.RANDOMIZE_PC_POTION.getValue();
        if (r.miscAllowPikachuEvolution) t |= MiscTweak.ALLOW_PIKACHU_EVOLUTION.getValue();
        if (r.miscNationalDexAtStart) t |= MiscTweak.NATIONAL_DEX_AT_START.getValue();
        if (r.miscUpdateTypeEffectiveness) t |= MiscTweak.UPDATE_TYPE_EFFECTIVENESS.getValue();
        if (r.miscForceChallengeMode) t |= MiscTweak.FORCE_CHALLENGE_MODE.getValue();
        if (r.miscLowerCaseNames) t |= MiscTweak.LOWER_CASE_POKEMON_NAMES.getValue();
        if (r.miscRandomizeCatchingTutorial) t |= MiscTweak.RANDOMIZE_CATCHING_TUTORIAL.getValue();
        if (r.miscBanLuckyEgg) t |= MiscTweak.BAN_LUCKY_EGG.getValue();
        if (r.miscNoFreeLuckyEgg) t |= MiscTweak.NO_FREE_LUCKY_EGG.getValue();
        if (r.miscBanBigManiacItems) t |= MiscTweak.BAN_BIG_MANIAC_ITEMS.getValue();
        if (r.miscSosBattlesForAll) t |= MiscTweak.SOS_BATTLES_FOR_ALL.getValue();
        if (r.miscBalanceStaticLevels) t |= MiscTweak.BALANCE_STATIC_LEVELS.getValue();
        if (r.miscRetainAltFormes) t |= MiscTweak.RETAIN_ALT_FORMES.getValue();
        if (r.miscRunWithoutRunningShoes) t |= MiscTweak.RUN_WITHOUT_RUNNING_SHOES.getValue();
        if (r.miscFasterHpExpBars) t |= MiscTweak.FASTER_HP_AND_EXP_BARS.getValue();
        if (r.miscFastDistortionWorld) t |= MiscTweak.FAST_DISTORTION_WORLD.getValue();
        if (r.miscUpdateRotomFormeTyping) t |= MiscTweak.UPDATE_ROTOM_FORME_TYPING.getValue();
        if (r.miscDisableLowHpMusic) t |= MiscTweak.DISABLE_LOW_HP_MUSIC.getValue();
        s.setCurrentMiscTweaks(t);

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
