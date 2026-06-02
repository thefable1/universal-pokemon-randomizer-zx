package com.dabomstew.pkrandom;

import com.dabomstew.pkrandom.pokemon.ExpCurve;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests for {@link Settings} serialization.
 *
 * Settings are persisted as a Base64 + CRC32 string via {@link Settings#toString()}
 * and rebuilt by {@link Settings#fromString(String)}. That hand-rolled format is
 * easy to break silently when fields are added/reordered, so these tests pin the
 * round-trip invariant: serialize -> deserialize -> serialize must be stable, and
 * representative values must survive the trip.
 *
 * No ROM is required; the only external dependency is the bundled custom-names
 * config file (on the classpath as a resource), which toString() checksums.
 */
public class SettingsRoundTripTest {

    /**
     * Builds a Settings with the fields that toString() dereferences populated
     * (romName + selected EXP curve), mirroring what the GUI/ROM-load would set.
     */
    private static Settings baseSettings() {
        Settings s = new Settings();
        s.setRomName("Pokemon Test Version");
        s.setSelectedEXPCurve(ExpCurve.MEDIUM_FAST);
        return s;
    }

    @Test
    void defaultSettingsRoundTripIsStable() throws Exception {
        Settings original = baseSettings();

        String firstPass = original.toString();
        Settings restored = Settings.fromString(firstPass);
        String secondPass = restored.toString();

        assertEquals(firstPass, secondPass,
                "Serializing default settings, restoring, and re-serializing must produce an identical string");
    }

    @Test
    void modifiedSettingsRoundTripPreservesValues() throws Exception {
        Settings original = baseSettings();
        original.setCustomStarters(new int[] {1, 4, 7});
        original.setGuaranteedMoveCount(4);          // stored in 2 bits as (n - 2): valid range 2..5
        original.setMovesetsGoodDamagingPercent(50);
        original.setBlockWildLegendaries(true);
        original.setUpdateMoves(true);

        String firstPass = original.toString();
        Settings restored = Settings.fromString(firstPass);

        // Values survive the round-trip.
        assertArrayEquals(new int[] {1, 4, 7}, restored.getCustomStarters(), "custom starters");
        assertEquals(4, restored.getGuaranteedMoveCount(), "guaranteed move count");
        assertEquals(50, restored.getMovesetsGoodDamagingPercent(), "good damaging move percent");
        assertTrue(restored.isBlockWildLegendaries(), "block wild legendaries");
        assertTrue(restored.isUpdateMoves(), "update moves");

        // And the format itself is stable.
        assertEquals(firstPass, restored.toString(),
                "Re-serializing restored settings must match the original string");
    }

    @Test
    void binaryWriteReadRoundTrip(@TempDir Path tmpDir) throws Exception {
        // Exercises the version-prefixed binary format used by .rnqs preset files:
        // write() prepends VERSION + length, read() validates them and rebuilds.
        Settings original = baseSettings();
        original.setBlockWildLegendaries(true);
        original.setMovesetsGoodDamagingPercent(75);

        File file = tmpDir.resolve("preset.rnqs").toFile();
        try (FileOutputStream out = new FileOutputStream(file)) {
            original.write(out);
        }

        Settings restored;
        try (FileInputStream in = new FileInputStream(file)) {
            restored = Settings.read(in);
        }

        assertTrue(restored.isBlockWildLegendaries(), "block wild legendaries survives binary round-trip");
        assertEquals(75, restored.getMovesetsGoodDamagingPercent(), "percent survives binary round-trip");
        assertEquals(original.toString(), restored.toString(), "binary round-trip is lossless");
    }
}
