package com.dabomstew.pkrandom;

import com.dabomstew.pkrandom.pokemon.ExpCurve;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.zip.CRC32;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link SettingsUpdater}, which migrates settings strings saved by
 * older versions of the randomizer up to the current format.
 *
 * Migrations are gated on the saved version. Version 323 appended one byte to
 * the settings block for the wild "force fully evolved" option, with a matching
 * {@code oldVersion < 323} migration that inserts the default. These tests pin
 * that upgrade path.
 */
public class SettingsUpdaterTest {

    /** Index of the wild "force fully evolved" byte added in version 323. */
    private static final int WILD_FFE_BYTE_INDEX = 51;

    private static Settings baseSettings() {
        Settings s = new Settings();
        s.setRomName("Pokemon Test Version");
        s.setSelectedEXPCurve(ExpCurve.MEDIUM_FAST);
        return s;
    }

    /**
     * Rewrites a current-format settings string into the pre-323 layout by
     * removing the wild-FFE byte and fixing the settings checksum, so we can
     * test the real 322 -> 323 upgrade without a hand-built binary fixture.
     */
    private static String downgradeToVersion322(String current) {
        byte[] data = Base64.getDecoder().decode(current);
        byte[] older = new byte[data.length - 1];
        System.arraycopy(data, 0, older, 0, WILD_FFE_BYTE_INDEX);
        System.arraycopy(data, WILD_FFE_BYTE_INDEX + 1, older, WILD_FFE_BYTE_INDEX,
                data.length - WILD_FFE_BYTE_INDEX - 1);
        CRC32 checksum = new CRC32();
        checksum.update(older, 0, older.length - 8);
        byte[] crc = ByteBuffer.allocate(4).putInt((int) checksum.getValue()).array();
        System.arraycopy(crc, 0, older, older.length - 8, 4);
        return Base64.getEncoder().encodeToString(older);
    }

    @Test
    void updatingAtCurrentVersionIsNoOp() throws Exception {
        Settings original = baseSettings();
        original.setBlockWildLegendaries(true);

        String saved = original.toString();
        String updated = new SettingsUpdater().update(Version.VERSION, saved);

        assertEquals(saved, Settings.fromString(updated).toString(),
                "updating a current-version string must change nothing");
    }

    @Test
    void upgradingFromVersion322InsertsWildForceFullyEvolvedDefaults() throws Exception {
        Settings original = baseSettings();
        original.setBlockWildLegendaries(true);
        // Set non-default values for the new option; a genuine v322 save predates
        // this field, so after the downgrade+upgrade they must come back as defaults.
        original.setWildForceFullyEvolved(true);
        original.setWildForceFullyEvolvedLevel(45);

        String version322 = downgradeToVersion322(original.toString());
        String upgraded = new SettingsUpdater().update(322, version322);
        Settings restored = Settings.fromString(upgraded);

        // The new field comes back as its default (disabled, level 30)...
        assertFalse(restored.isWildForceFullyEvolved(), "new option defaults to off after upgrade");
        assertEquals(30, restored.getWildForceFullyEvolvedLevel(), "new option defaults to level 30 after upgrade");
        // ...while pre-existing fields are preserved.
        assertTrue(restored.isBlockWildLegendaries(), "existing setting preserved across upgrade");
    }
}
