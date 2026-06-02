package com.dabomstew.pkrandom.bridge;

import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.pokemon.ExpCurve;

/**
 * Maps the JSON {@link SettingsRequest} DTO onto a real engine {@link Settings}.
 *
 * This is the heart of the integration: the frontend never touches the engine's
 * custom binary settings format, it just sends field values as JSON and this
 * class applies them through the normal Settings setters.
 */
final class SettingsMapper {

    private SettingsMapper() {}

    static Settings toSettings(SettingsRequest req) {
        Settings s = new Settings();

        // These are normally populated when a ROM is loaded. Set safe defaults so
        // that Settings.toString() works even without a ROM (e.g. for /encode).
        s.setRomName("Bridge");
        s.setSelectedEXPCurve(ExpCurve.MEDIUM_FAST);

        // Enum settings: the setters take booleans in enum-ordinal order.
        s.setBaseStatisticsMod(ordinalFlags(
                Settings.BaseStatisticsMod.values().length,
                Settings.BaseStatisticsMod.valueOf(req.baseStatsMod).ordinal()));
        s.setBaseStatsFollowEvolutions(req.baseStatsFollowEvolutions);
        s.setStandardizeEXPCurves(req.standardizeExpCurves);
        s.setUpdateBaseStats(req.updateBaseStats);

        s.setTypesMod(ordinalFlags(
                Settings.TypesMod.values().length,
                Settings.TypesMod.valueOf(req.typesMod).ordinal()));
        s.setDualTypeOnly(req.dualTypeOnly);

        s.setWildForceFullyEvolved(req.wildForceFullyEvolved);
        s.setWildForceFullyEvolvedLevel(req.wildForceFullyEvolvedLevel);

        return s;
    }

    private static boolean[] ordinalFlags(int size, int selected) {
        boolean[] flags = new boolean[size];
        if (selected >= 0 && selected < size) {
            flags[selected] = true;
        }
        return flags;
    }
}
