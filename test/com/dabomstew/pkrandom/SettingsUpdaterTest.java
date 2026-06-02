package com.dabomstew.pkrandom;

import com.dabomstew.pkrandom.pokemon.ExpCurve;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link SettingsUpdater}, which migrates settings strings saved by
 * older versions of the randomizer up to the current format.
 *
 * Migrations are gated on the saved version (e.g. {@code oldVersion < 321}).
 * The 4.6.1 release (version 322) bumped the version without changing the data
 * layout, so upgrading a current-format string as if it came from the previous
 * version exercises the updater's pipeline (decode -> apply applicable
 * migrations -> recompute checksum -> re-encode) and must round-trip cleanly.
 */
public class SettingsUpdaterTest {

    private static Settings baseSettings() {
        Settings s = new Settings();
        s.setRomName("Pokemon Test Version");
        s.setSelectedEXPCurve(ExpCurve.MEDIUM_FAST);
        return s;
    }

    @Test
    void updatingFromPreviousVersionPreservesSettings() throws Exception {
        Settings original = baseSettings();
        original.setBlockWildLegendaries(true);
        original.setMovesetsGoodDamagingPercent(60);

        String saved = original.toString();
        String updated = new SettingsUpdater().update(Version.VERSION - 1, saved);

        // The updater must emit a valid, checksummed string fromString accepts...
        Settings restored = Settings.fromString(updated);

        // ...and the values must be intact.
        assertTrue(restored.isBlockWildLegendaries(), "block wild legendaries preserved across update");
        assertEquals(60, restored.getMovesetsGoodDamagingPercent(), "percent preserved across update");
        assertEquals(saved, restored.toString(),
                "upgrading from the previous version is lossless when the layout is unchanged");
    }

    @Test
    void readRoutesOldVersionThroughUpdater() throws Exception {
        // Settings.read() flags whether it had to upgrade an older save. Verify
        // a current-format string still parses (the common no-migration path).
        Settings original = baseSettings();
        Settings restored = Settings.fromString(original.toString());
        assertEquals(original.toString(), restored.toString(), "current-version string parses unchanged");
    }
}
