package com.dabomstew.pkrandom.pokemon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pure-logic tests for {@link EvolutionType}'s per-generation index mapping.
 *
 * {@code toIndex(gen)} / {@code fromIndex(gen, idx)} translate between the
 * enum and the raw byte each game stores. Note this is NOT a bijection in
 * every generation -- e.g. in Gen 2 several distinct methods (TRADE,
 * TRADE_ITEM) collapse onto the same index because the game had fewer
 * evolution methods. The invariant that must always hold is index
 * stability: decoding an index and re-encoding it yields the same index.
 */
public class EvolutionTypeTest {

    @Test
    void indexMappingIsStableAcrossAllGenerations() {
        int checked = 0;
        for (EvolutionType type : EvolutionType.values()) {
            for (int gen = 1; gen <= 7; gen++) {
                int index = type.toIndex(gen);
                if (index == -1) {
                    continue; // method does not exist in this generation
                }
                checked++;
                EvolutionType decoded = EvolutionType.fromIndex(gen, index);
                assertEquals(index, decoded.toIndex(gen),
                        "Index " + index + " in gen " + gen + " must round-trip stably (from " + type + ")");
            }
        }
        // Sanity check that the loop actually exercised a meaningful number of mappings.
        assertTrue(checked > 100, "expected to check many type/generation mappings, got " + checked);
    }

    @Test
    void usesLevelClassifiesLevelBasedEvolutions() {
        assertTrue(EvolutionType.LEVEL.usesLevel(), "LEVEL is level-based");
        assertTrue(EvolutionType.LEVEL_ATTACK_HIGHER.usesLevel(), "LEVEL_ATTACK_HIGHER is level-based");
        assertFalse(EvolutionType.STONE.usesLevel(), "STONE is not level-based");
        assertFalse(EvolutionType.TRADE.usesLevel(), "TRADE is not level-based");
        assertFalse(EvolutionType.HAPPINESS.usesLevel(), "HAPPINESS is not level-based");
    }
}
