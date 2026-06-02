package com.dabomstew.pkrandom.bridge;

/**
 * The JSON contract between the frontend and the engine.
 *
 * This is a plain DTO that Gson (de)serializes. The frontend sends one of these
 * as JSON; {@link SettingsMapper} turns it into a real engine {@link
 * com.dabomstew.pkrandom.Settings}. It deliberately mirrors a SUBSET of the
 * settings (the "Pokemon Traits" tab plus the wild force-fully-evolved option)
 * to demonstrate the pattern -- extending it to the full ~120 options is purely
 * mechanical (add a field here, one line in SettingsMapper).
 */
public class SettingsRequest {
    // --- Base statistics ---
    public String baseStatsMod = "UNCHANGED";          // UNCHANGED | SHUFFLE | RANDOM
    public boolean baseStatsFollowEvolutions = false;
    public boolean standardizeExpCurves = false;
    public boolean updateBaseStats = false;

    // --- Types ---
    public String typesMod = "UNCHANGED";              // UNCHANGED | RANDOM_FOLLOW_EVOLUTIONS | COMPLETELY_RANDOM
    public boolean dualTypeOnly = false;

    // --- Wild Pokemon (the feature added earlier) ---
    public boolean wildForceFullyEvolved = false;
    public int wildForceFullyEvolvedLevel = 30;
}
