// Enum option lists (value = engine enum constant name, label = display text).
export type Opt = { value: string; label: string };

export const baseStats: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "SHUFFLE", label: "Shuffle" },
  { value: "RANDOM", label: "Random" },
];

export const types: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM_FOLLOW_EVOLUTIONS", label: "Random (follow evolutions)" },
  { value: "COMPLETELY_RANDOM", label: "Random (completely)" },
];

export const abilities: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOMIZE", label: "Randomize" },
];

export const evolutions: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM", label: "Random" },
  { value: "RANDOM_EVERY_LEVEL", label: "Random every level" },
];

export const starters: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "CUSTOM", label: "Custom" },
  { value: "COMPLETELY_RANDOM", label: "Completely random" },
  { value: "RANDOM_WITH_TWO_EVOLUTIONS", label: "Random (2 evolutions)" },
];

export const statics: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM_MATCHING", label: "Random (similar)" },
  { value: "COMPLETELY_RANDOM", label: "Completely random" },
  { value: "SIMILAR_STRENGTH", label: "Random (similar strength)" },
];

export const trades: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOMIZE_GIVEN", label: "Randomize given" },
  { value: "RANDOMIZE_GIVEN_AND_REQUESTED", label: "Randomize given & requested" },
];

export const movesets: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM_PREFER_SAME_TYPE", label: "Random (prefer same type)" },
  { value: "COMPLETELY_RANDOM", label: "Completely random" },
  { value: "METRONOME_ONLY", label: "Metronome only" },
];

export const trainers: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM", label: "Random" },
  { value: "DISTRIBUTED", label: "Random (distributed)" },
  { value: "MAINPLAYTHROUGH", label: "Random (main playthrough)" },
  { value: "TYPE_THEMED", label: "Type themed" },
  { value: "TYPE_THEMED_ELITE4_GYMS", label: "Type themed (gyms/E4 only)" },
];

export const wild: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM", label: "Random" },
  { value: "AREA_MAPPING", label: "Area 1-to-1" },
  { value: "GLOBAL_MAPPING", label: "Global 1-to-1" },
];

export const tms: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM", label: "Random" },
];

export const compat: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM_PREFER_TYPE", label: "Random (prefer type)" },
  { value: "COMPLETELY_RANDOM", label: "Completely random" },
  { value: "FULL", label: "Full compatibility" },
];

export const fieldItems: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "SHUFFLE", label: "Shuffle" },
  { value: "RANDOM", label: "Random" },
  { value: "RANDOM_EVEN", label: "Random (even distribution)" },
];

export const shopItems: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "SHUFFLE", label: "Shuffle" },
  { value: "RANDOM", label: "Random" },
];

export const pickup: Opt[] = [
  { value: "UNCHANGED", label: "Unchanged" },
  { value: "RANDOM", label: "Random" },
];

export const TABS = [
  { id: "traits", label: "Pokémon Traits" },
  { id: "starters", label: "Starters, Statics & Trades" },
  { id: "moves", label: "Moves & Movesets" },
  { id: "foes", label: "Foe Pokémon" },
  { id: "wild", label: "Wild Pokémon" },
  { id: "tms", label: "TM/HMs & Tutors" },
  { id: "items", label: "Items" },
  { id: "misc", label: "Misc. Tweaks" },
] as const;
