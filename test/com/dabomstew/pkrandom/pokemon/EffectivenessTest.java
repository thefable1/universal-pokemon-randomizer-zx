package com.dabomstew.pkrandom.pokemon;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pure-logic tests for the type chart ({@link Type} / {@link Effectiveness}).
 *
 * These need no ROM -- the tables are hard-coded -- and pin well-known
 * real-game matchups, including the generation differences the randomizer
 * relies on when it reasons about types.
 */
public class EffectivenessTest {

    // ---- Type availability per generation ----------------------------------

    @Test
    void typeCountsPerGeneration() {
        assertEquals(15, Type.getAllTypes(1).size(), "Gen 1: NORMAL..ICE");
        assertEquals(17, Type.getAllTypes(5).size(), "Gen 2-5: adds DARK and STEEL");
        assertEquals(18, Type.getAllTypes(6).size(), "Gen 6+: adds FAIRY");
    }

    @Test
    void fairyOnlyExistsFromGen6() {
        assertFalse(Type.getAllTypes(5).contains(Type.FAIRY), "FAIRY did not exist in Gen 5");
        assertTrue(Type.getAllTypes(6).contains(Type.FAIRY), "FAIRY exists from Gen 6");
        assertTrue(Type.getAllTypes(5).contains(Type.STEEL), "STEEL exists from Gen 2");
    }

    // ---- Generation-specific matchups --------------------------------------

    @Test
    void gen1GhostIsNotSuperEffectiveAgainstPsychic() {
        // The infamous Gen 1 bug: Ghost was coded NEUTRAL/0x against Psychic.
        assertFalse(Effectiveness.superEffective(Type.GHOST, 1, false).contains(Type.PSYCHIC),
                "Gen 1: Ghost is NOT super effective vs Psychic");
        // Fixed from Gen 2 onward.
        assertTrue(Effectiveness.superEffective(Type.GHOST, 2, false).contains(Type.PSYCHIC),
                "Gen 2+: Ghost IS super effective vs Psychic");
    }

    @Test
    void waterIsSuperEffectiveAgainstFireRockGround() {
        List<Type> se = Effectiveness.superEffective(Type.WATER, 3, false);
        assertTrue(se.contains(Type.FIRE) && se.contains(Type.ROCK) && se.contains(Type.GROUND),
                "Water hits Fire, Rock and Ground super effectively");
    }

    @Test
    void fairyIsSuperEffectiveAgainstDragonDarkFighting() {
        List<Type> se = Effectiveness.superEffective(Type.FAIRY, 6, false);
        assertTrue(se.contains(Type.DRAGON) && se.contains(Type.DARK) && se.contains(Type.FIGHTING),
                "Fairy hits Dragon, Dark and Fighting super effectively");
    }

    // ---- Dual-type combination + immunity ----------------------------------

    @Test
    void dualWeaknessStacksToQuadruple() {
        // Both Rock and Ground take double damage from Water -> 4x on a Rock/Ground.
        Map<Type, Effectiveness> against = Effectiveness.against(Type.ROCK, Type.GROUND, 6);
        assertEquals(Effectiveness.QUADRUPLE, against.get(Type.WATER),
                "Rock/Ground is 4x weak to Water");
    }

    @Test
    void groundIsImmuneToElectric() {
        Map<Type, Effectiveness> against = Effectiveness.against(Type.GROUND, null, 6);
        assertEquals(Effectiveness.ZERO, against.get(Type.ELECTRIC), "Ground is immune to Electric");
    }
}
