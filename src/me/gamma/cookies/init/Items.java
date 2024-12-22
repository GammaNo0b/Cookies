
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.ITEMS;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.network.energy.Tesseract;
import me.gamma.cookies.object.block.network.fluid.EnderTank;
import me.gamma.cookies.object.block.network.item.EnderChest;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomBlockItem;
import me.gamma.cookies.object.item.CustomCraftingTableBlueprint;
import me.gamma.cookies.object.item.CustomItem;
import me.gamma.cookies.object.item.GeneratorItem;
import me.gamma.cookies.object.item.MachineItem;
import me.gamma.cookies.object.item.PlayerRegister;
import me.gamma.cookies.object.item.armor.AngelWings;
import me.gamma.cookies.object.item.armor.ArmorType;
import me.gamma.cookies.object.item.armor.BerryPants;
import me.gamma.cookies.object.item.armor.CactusShirt;
import me.gamma.cookies.object.item.armor.ColoredArmorPiece;
import me.gamma.cookies.object.item.armor.FarmerBoots;
import me.gamma.cookies.object.item.armor.GlowHat;
import me.gamma.cookies.object.item.armor.HasteArmorPiece;
import me.gamma.cookies.object.item.armor.InvisibilityHat;
import me.gamma.cookies.object.item.armor.LuckyLeggings;
import me.gamma.cookies.object.item.armor.RabbitBoots;
import me.gamma.cookies.object.item.armor.RainbowArmorPiece;
import me.gamma.cookies.object.item.armor.ScubaHelmet;
import me.gamma.cookies.object.item.armor.TurtleShell;
import me.gamma.cookies.object.item.resources.Backpack;
import me.gamma.cookies.object.item.resources.BatteryItem;
import me.gamma.cookies.object.item.resources.CardItem;
import me.gamma.cookies.object.item.resources.CardPile;
import me.gamma.cookies.object.item.resources.EnderLinkedBlockItem;
import me.gamma.cookies.object.item.resources.InsulatedCopperWire;
import me.gamma.cookies.object.item.resources.Lootbox;
import me.gamma.cookies.object.item.resources.MagicMetal;
import me.gamma.cookies.object.item.resources.ResistorItem;
import me.gamma.cookies.object.item.resources.TankItem;
import me.gamma.cookies.object.item.tools.Airgun;
import me.gamma.cookies.object.item.tools.AngelBlockItem;
import me.gamma.cookies.object.item.tools.CookieCookBook;
import me.gamma.cookies.object.item.tools.DragonEye;
import me.gamma.cookies.object.item.tools.EnergyMeasureGadget;
import me.gamma.cookies.object.item.tools.FarmerScythe;
import me.gamma.cookies.object.item.tools.ItemFilterItem;
import me.gamma.cookies.object.item.tools.KnockbackStick;
import me.gamma.cookies.object.item.tools.LightningBow;
import me.gamma.cookies.object.item.tools.LumberAxe;
import me.gamma.cookies.object.item.tools.MeasuringTape;
import me.gamma.cookies.object.item.tools.MiniaturizingWand;
import me.gamma.cookies.object.item.tools.MultiBlockBook;
import me.gamma.cookies.object.item.tools.NoobSword;
import me.gamma.cookies.object.item.tools.PlayerTracker;
import me.gamma.cookies.object.item.tools.PortableCraftingTable;
import me.gamma.cookies.object.item.tools.PortableCustomCraftingOpener;
import me.gamma.cookies.object.item.tools.PortableEndPortal;
import me.gamma.cookies.object.item.tools.PortableEnderChest;
import me.gamma.cookies.object.item.tools.PortableNetherPortal;
import me.gamma.cookies.object.item.tools.Pouch;
import me.gamma.cookies.object.item.tools.RedstoneFrequencyGadget;
import me.gamma.cookies.object.item.tools.RocketLauncher;
import me.gamma.cookies.object.item.tools.SlimeSling;
import me.gamma.cookies.object.item.tools.SpawnerWand;
import me.gamma.cookies.object.item.tools.VanillaRecipeBook;
import me.gamma.cookies.object.item.tools.VeinMinerPickaxe;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.recipe.RecipeType;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.CachingSupplier;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.core.MinecraftItemHelper;



public class Items {

	// Miscellaneous
	public static CustomCraftingTableBlueprint CUSTOM_CRAFTING_TABLE_BLUEPRINT;
	public static VanillaRecipeBook VANILLA_RECIPE_BOOK;
	public static CookieCookBook COOKIE_COOK_BOOK;
	public static MultiBlockBook MULTI_BLOCK_BOOK;
	public static PortableCraftingTable PORTABLE_CRAFTING_TABLE;
	public static PortableCustomCraftingOpener PORTABLE_CUSTOM_CRAFTING_TABLE;
	public static PortableCustomCraftingOpener PORTABLE_ENGINEERING_STATION;
	public static PortableCustomCraftingOpener PORTABLE_MAGIC_ALTAR;
	public static PortableCustomCraftingOpener PORTABLE_KITCHEN;
	public static PortableNetherPortal PORTABLE_NETHER_PORTAL;
	public static PortableEndPortal PORTABLE_END_PORTAL;
	public static PortableEnderChest PORTABLE_ENDER_CHEST;
	public static AngelBlockItem ANGEL_BLOCK;
	public static MeasuringTape MEASURING_TAPE;
	public static Pouch POUCH;
	public static Lootbox LOOTBOX;

	// Tools
	public static VeinMinerPickaxe VEIN_MINER_PICKAXE;
	public static LumberAxe LUMBER_AXE;
	public static FarmerScythe FARMER_SCYTHE;
	public static SlimeSling SLIME_SLING;
	public static PlayerTracker PLAYER_TRACKER;
	public static Airgun AIRGUN;
	public static RocketLauncher ROCKET_LAUNCHER;

	// Weapons
	public static KnockbackStick KNOCKBACK_STICK;
	public static NoobSword NOOB_SWORD;
	public static LightningBow LIGHTNING_BOW;

	// Armor
	public static GlowHat GLOW_HAT;
	public static ScubaHelmet SCUBA_HELMET;
	public static InvisibilityHat INVISIBILITY_HAT;
	public static CactusShirt CACTUS_SHIRT;
	public static TurtleShell TURTLE_SHELL;
	public static BerryPants BERRY_PANTS;
	public static LuckyLeggings LUCKY_LEGGINGS_1;
	public static LuckyLeggings LUCKY_LEGGINGS_2;
	public static LuckyLeggings LUCKY_LEGGINGS_3;
	public static FarmerBoots FARMER_BOOTS;
	public static RabbitBoots RABBIT_BOOTS;
	public static AngelWings ANGEL_WINGS;
	public static RainbowArmorPiece RAINBOW_HELMET;
	public static RainbowArmorPiece RAINBOW_CHESTPLATE;
	public static RainbowArmorPiece RAINBOW_LEGGINGS;
	public static RainbowArmorPiece RAINBOW_BOOTS;
	public static ColoredArmorPiece COLORED_HELMET;
	public static ColoredArmorPiece COLORED_CHESTPLATE;
	public static ColoredArmorPiece COLORED_LEGGINGS;
	public static ColoredArmorPiece COLORED_BOOTS;
	public static HasteArmorPiece HASTE_HELMET;
	public static HasteArmorPiece HASTE_CHESTPLATE;
	public static HasteArmorPiece HASTE_LEGGINGS;
	public static HasteArmorPiece HASTE_BOOTS;

	// Resources
	public static CustomItem STONE_PEBBLE;
	public static CustomItem ANDESITE_PEBBLE;
	public static CustomItem DIORITE_PEBBLE;
	public static CustomItem GRANITE_PEBBLE;
	public static CustomItem CALCITE_PEBBLE;
	public static CustomItem TUFF_PEBBLE;
	public static CustomItem DEEPSLATE_PEBBLE;
	public static CustomBlockItem COMPRESSED_COBBLESTONE;
	public static CustomBlockItem DOUBLE_COMPRESSED_COBBLESTONE;
	public static CustomBlockItem TRIPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlockItem QUADRUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlockItem QUINTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlockItem SEXTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlockItem SEPTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlockItem OCTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomItem DUST;
	public static CustomItem RED_DUST;
	public static CustomItem BLACK_SAND;
	public static CustomItem CRUSHED_NETHERRACK;
	public static CustomItem CRUSHED_END_STONE;
	public static CustomItem CRUSHED_OBSIDIAN;
	public static CustomItem CRUSHED_CRYING_OBSIDIAN;
	public static CustomItem SILICON;
	public static CustomItem STEEL_COMPOUND;
	public static CustomItem PULVERIZED_COAL;
	public static CustomItem SULFUR;
	public static CustomItem QUARTZ_DUST;
	public static CustomItem IRON_DUST;
	public static CustomItem GOLD_DUST;
	public static CustomItem COPPER_DUST;
	public static CustomItem ALUMINUM_DUST;
	public static CustomItem TIN_DUST;
	public static CustomItem NICKEL_DUST;
	public static CustomItem LEAD_DUST;
	public static CustomItem SILVER_DUST;
	public static CustomItem LITHIUM_DUST;
	public static CustomItem MAGNESIUM_DUST;
	public static CustomItem STEEL_DUST;
	public static CustomItem SOUL_DUST;
	public static CustomItem ALUMINUM_INGOT;
	public static CustomItem TIN_INGOT;
	public static CustomItem NICKEL_INGOT;
	public static CustomItem LEAD_INGOT;
	public static CustomItem SILVER_INGOT;
	public static CustomItem MAGNESIUM_INGOT;
	public static CustomItem STEEL_INGOT;
	public static CustomItem BRONZE_INGOT;
	public static CustomItem ELECTRUM_INGOT;
	public static CustomItem INVAR_INGOT;
	public static CustomItem ALUMINUM_STEEL_INGOT;
	public static CustomItem HARDENED_METAL;
	public static CustomItem REDSTONE_ALLOY;
	public static CustomItem HARDENED_ALLOY;
	public static CustomItem NETHER_STEEL;
	public static CustomItem BLAZING_ALLOY;
	public static CustomItem ENDER_STEEL;
	public static CustomItem ENDERIUM;
	public static CustomItem ENERGETIC_ALLOY;
	public static CustomItem REDSTONIUM;
	public static CustomItem CARBON;
	public static CustomItem COMPRESSED_CARBON;
	public static CustomItem CARBON_CHUNK;
	public static CustomItem CARBONADO;
	public static CustomItem STICKY_BALL;
	public static CustomItem RUBBER;
	public static CustomItem RUBBER_SHEETS;
	public static CustomItem PLASTIC_SHEET;
	public static CustomItem GILDED_PAPER;
	public static CustomItem RAINBOW_DUST;

	// Bags
	public static CustomItem BAG_OF_COAL;
	public static CustomItem BAG_OF_IRON;
	public static CustomItem BAG_OF_GOLD;
	public static CustomItem BAG_OF_REDSTONE;
	public static CustomItem BAG_OF_LAPIS;
	public static CustomItem BAG_OF_EMERALDS;
	public static CustomItem BAG_OF_DIAMONDS;
	public static CustomItem BAG_OF_AMETHYSTS;

	public static CustomItem BAG_OF_SEEDS;
	public static CustomItem BAG_OF_WHEAT;
	public static CustomItem BAG_OF_POTATOES;
	public static CustomItem BAG_OF_POISONOUS_POTATOES;
	public static CustomItem BAG_OF_CARROTS;
	public static CustomItem BAG_OF_BEETROOTS;
	public static CustomItem BAG_OF_PUMPKINS;
	public static CustomItem BAG_OF_MELONS;
	public static CustomItem BAG_OF_CACTI;
	public static CustomItem BAG_OF_SUGAR_CANE;
	public static CustomItem BAG_OF_BAMBOO;
	public static CustomItem BAG_OF_BROWN_MUSHROOMS;
	public static CustomItem BAG_OF_RED_MUSHROOMS;
	public static CustomItem BAG_OF_FLOWERS;

	public static CustomItem BAG_OF_SNOW_BALLS;
	public static CustomItem BAG_OF_BONE_MEAL;
	public static CustomItem BAG_OF_CLAY_BALLS;
	public static CustomItem BAG_OF_GUNPOWDER;
	public static CustomItem BAG_OF_GLOWSTONE_DUST;
	public static CustomItem BAG_OF_NETHER_WARTS;
	public static CustomItem BAG_OF_SUGAR;
	public static CustomItem BAG_OF_SALT;
	public static CustomItem BAG_OF_ENDER_PEARLS;

	public static CustomItem BASKET_OF_APPLES;
	public static CustomItem BASKET_OF_SWEET_BERRIES;
	public static CustomItem BASKET_OF_GLOW_BERRIES;
	public static CustomItem BAG_OF_HONEY;
	public static CustomItem BAG_OF_FISH;

	// Redstone
	public static RedstoneFrequencyGadget REDSTONE_FREQUENCY_GADGET;
	public static CustomBlockItem WIRELESS_REDSTONE_TRANSMITTER;
	public static CustomBlockItem WIRELESS_REDSTONE_RECEIVER;
	public static CustomBlockItem REDSTONE_OR_GATE;
	public static CustomBlockItem REDSTONE_AND_GATE;
	public static CustomBlockItem REDSTONE_XOR_GATE;

	// Electric Components
	public static CustomItem COPPER_WIRE;
	public static CustomItem COPPER_COIL;
	public static InsulatedCopperWire INSULATED_COPPER_WIRE;
	public static ResistorItem RESISTOR_1;
	public static ResistorItem RESISTOR_2;
	public static ResistorItem RESISTOR_3;
	public static ResistorItem RESISTOR_4;
	public static ResistorItem RESISTOR_5;
	public static CustomItem CAPACITOR_1;
	public static CustomItem CAPACITOR_2;
	public static CustomItem CAPACITOR_3;
	public static CustomItem CAPACITOR_4;
	public static CustomItem CAPACITOR_5;
	public static CustomItem ELECTROMAGNET;
	public static CustomItem MOTOR;
	public static CustomItem LASER;
	public static CustomBlockItem ACCUMULATOR;
	public static CustomBlockItem ITEM_ATTRACTOR;
	public static CustomBlockItem MONSTER_ATTRACTOR;
	public static CustomItem ELECTRICAL_CIRCUIT_1;
	public static CustomItem ELECTRICAL_CIRCUIT_2;
	public static CustomItem ELECTRICAL_CIRCUIT_3;
	public static CustomItem ELECTRICAL_CIRCUIT_4;
	public static CustomItem ELECTRICAL_CIRCUIT_5;
	public static EnergyMeasureGadget ENERGY_MEASURE_GADGET;
	public static CustomItem PHOTOVOLTAIC_CELL_1;
	public static CustomItem PHOTOVOLTAIC_CELL_2;
	public static CustomItem PHOTOVOLTAIC_CELL_3;
	public static CustomItem PHOTOVOLTAIC_CELL_4;
	public static CustomItem PHOTOVOLTAIC_CELL_5;
	public static CustomItem PHOTOVOLTAIC_CELL_6;
	public static CustomItem PHOTOVOLTAIC_CELL_7;
	public static CustomItem PHOTOVOLTAIC_CELL_8;

	// Technical Components
	public static CustomItem CIRCUIT_BOARD;
	public static CustomBlockItem LIGHT_CONVEYOR_BELT;
	public static CustomBlockItem MEDIUM_CONVEYOR_BELT;
	public static CustomBlockItem HEAVY_CONVEYOR_BELT;
	public static CustomItem UPGRADE_BASE;
	public static CustomItem UPGRADE_SHARPNESS;
	public static CustomItem UPGRADE_FORTUNE;
	public static CustomItem UPGRADE_FIREASPECT;
	public static CustomItem UPGRADE_BEHEADING;
	public static CustomItem UPGRADE_RANGE;
	public static CustomItem UPGRADE_SPEED;
	public static CustomItem UPGRADE_EFFICIENCY;
	public static CustomItem UPGRADE_ENERGY_STORAGE;

	// Machines
	public static CustomBlockItem BASIC_MACHINE_CASING;
	public static CustomBlockItem ADVANCED_MACHINE_CASING;
	public static CustomBlockItem IMPROVED_MACHINE_CASING;
	public static CustomBlockItem PERFECTED_MACHINE_CASING;
	public static CustomBlockItem COBBLESTONE_GENERATOR;
	public static CustomBlockItem STONE_GENERATOR;
	public static CustomBlockItem DRIPSTONE_GENERATOR;
	public static CustomBlockItem BASALT_GENERATOR;
	public static CustomBlockItem LAVA_GENERATOR;
	public static CustomBlockItem BASIC_LAVA_GENERATOR;
	public static CustomBlockItem ADVANCED_LAVA_GENERATOR;
	public static CustomBlockItem IMPROVED_LAVA_GENERATOR;
	public static CustomBlockItem PERFECTED_LAVA_GENERATOR;
	public static MachineItem OBSIDIAN_GENERATOR;
	public static MachineItem ITEM_ABSORBER;
	public static MachineItem EXPERIENCE_ABSORBER;
	public static MachineItem BASIC_FARMER;
	public static MachineItem ADVANCED_FARMER;
	public static MachineItem IMPROVED_FARMER;
	public static MachineItem PERFECTED_FARMER;
	public static MachineItem MOB_GRINDER;
	public static MachineItem BLOCK_BREAKER;
	public static MachineItem QUARRY;
	public static MachineItem BASIC_VOID_ORE_MINER;
	public static MachineItem ADVANCED_VOID_ORE_MINER;
	public static MachineItem IMPROVED_VOID_ORE_MINER;
	public static MachineItem PERFECTED_VOID_ORE_MINER;
	public static MachineItem DYE_PRESS;
	public static MachineItem DYE_MIXER;
	public static MachineItem ENCHANTER;
	public static MachineItem DISENCHANTER;
	public static MachineItem ENCHANTMENT_COMBINER;
	public static CustomBlockItem COMPUTER;
	public static CustomBlockItem CHUNK_LOADER;
	public static MachineItem HYPER_FURNACE_1;
	public static MachineItem HYPER_FURNACE_2;
	public static MachineItem HYPER_FURNACE_3;
	public static MachineItem HYPER_FURNACE_4;
	public static MachineItem HYPER_FURNACE_5;
	public static MachineItem HYPER_FURNACE_6;
	public static MachineItem HYPER_FURNACE_7;
	public static MachineItem HYPER_FURNACE_8;
	public static MachineItem HYPER_FURNACE_9;
	public static MachineItem HYPER_FURNACE_10;
	public static MachineItem HYPER_FURNACE_11;
	public static MachineItem HYPER_FURNACE_12;
	public static MachineItem BASIC_CRUSHER;
	public static MachineItem ADVANCED_CRUSHER;
	public static MachineItem IMPROVED_CRUSHER;
	public static MachineItem PERFECTED_CRUSHER;
	public static MachineItem BASIC_MINERAL_EXTRACTOR;
	public static MachineItem ADVANCED_MINERAL_EXTRACTOR;
	public static MachineItem IMPROVED_MINERAL_EXTRACTOR;
	public static MachineItem PERFECTED_MINERAL_EXTRACTOR;
	public static MachineItem BASIC_AERIAL_EXTRACTOR;
	public static MachineItem ADVANCED_AERIAL_EXTRACTOR;
	public static MachineItem IMPROVED_AERIAL_EXTRACTOR;
	public static MachineItem PERFECTED_AERIAL_EXTRACTOR;
	public static MachineItem BASIC_SAWMILL;
	public static MachineItem ADVANCED_SAWMILL;
	public static MachineItem IMPROVED_SAWMILL;
	public static MachineItem PERFECTED_SAWMILL;
	public static MachineItem BASIC_SMELTERY;
	public static MachineItem ADVANCED_SMELTERY;
	public static MachineItem IMPROVED_SMELTERY;
	public static MachineItem PERFECTED_SMELTERY;
	public static MachineItem BASIC_COMPRESSOR;
	public static MachineItem ADVANCED_COMPRESSOR;
	public static MachineItem IMPROVED_COMPRESSOR;
	public static MachineItem PERFECTED_COMPRESSOR;
	public static MachineItem BASIC_BIO_PRESS;
	public static MachineItem ADVANCED_BIO_PRESS;
	public static MachineItem IMPROVED_BIO_PRESS;
	public static MachineItem PERFECTED_BIO_PRESS;
	public static MachineItem LATEX_EXTRACTOR;
	public static MachineItem BASIC_CRAFTER;
	public static MachineItem ADVANCED_CRAFTER;
	public static MachineItem IMPROVED_CRAFTER;
	public static MachineItem PERFECTED_CRAFTER;
	public static MachineItem BASIC_CRAFTING_FACTORY;
	public static MachineItem ADVANCED_CRAFTING_FACTORY;
	public static MachineItem IMPROVED_CRAFTING_FACTORY;
	public static MachineItem PERFECTED_CRAFTING_FACTORY;
	public static MachineItem BASIC_CARBON_PRESS;
	public static MachineItem ADVANCED_CARBON_PRESS;
	public static MachineItem IMPROVED_CARBON_PRESS;
	public static MachineItem PERFECTED_CARBON_PRESS;
	public static MachineItem BASIC_FREEZER;
	public static MachineItem ADVANCED_FREEZER;
	public static MachineItem IMPROVED_FREEZER;
	public static MachineItem PERFECTED_FREEZER;
	public static MachineItem HONEY_EXTRACTOR;
	public static MachineItem STAR_MAKER;
	public static MachineItem ROCKET_ASSEMBLER;

	// Generators
	public static GeneratorItem FURNACE_GENERATOR;
	public static GeneratorItem THERMO_GENERATOR;
	public static GeneratorItem BASIC_SOLAR_PANEL;
	public static GeneratorItem ADVANCED_SOLAR_PANEL;
	public static GeneratorItem IMPROVED_SOLAR_PANEL;
	public static GeneratorItem PERFECTED_SOLAR_PANEL;
	public static GeneratorItem BASIC_BIO_GENERATOR;
	public static GeneratorItem ADVANCED_BIO_GENERATOR;
	public static GeneratorItem IMPROVED_BIO_GENERATOR;
	public static GeneratorItem PERFECTED_BIO_GENERATOR;
	public static GeneratorItem CREATIVE_GENERATOR;

	// Storage
	public static Backpack BROWN_BACKPACK;
	public static Backpack COPPER_BACKPACK;
	public static Backpack IRON_BACKPACK;
	public static Backpack GOLDEN_BACKPACK;
	public static Backpack DIAMOND_BACKPACK;
	public static Backpack NETHERITE_BACKPACK;
	public static CustomBlockItem OAK_STORAGE_CRATE;
	public static CustomBlockItem SPRUCE_STORAGE_CRATE;
	public static CustomBlockItem BIRCH_STORAGE_CRATE;
	public static CustomBlockItem JUNGLE_STORAGE_CRATE;
	public static CustomBlockItem ACACIA_STORAGE_CRATE;
	public static CustomBlockItem DARK_OAK_STORAGE_CRATE;
	public static CustomBlockItem MANGROVE_STORAGE_CRATE;
	public static CustomBlockItem CHERRY_STORAGE_CRATE;
	public static CustomBlockItem PALE_OAK_STORAGE_CRATE;
	public static CustomBlockItem BAMBOO_STORAGE_CRATE;
	public static CustomBlockItem CRIMSON_STORAGE_CRATE;
	public static CustomBlockItem WARPED_STORAGE_CRATE;
	public static CustomBlockItem IRON_STORAGE_CRATE;
	public static CustomBlockItem GOLDEN_STORAGE_CRATE;
	public static CustomBlockItem DIAMOND_STORAGE_CRATE;
	public static CustomBlockItem EMERALD_STORAGE_CRATE;
	public static CustomBlockItem CLOWNFISH_STORAGE_CRATE;
	public static EnderLinkedBlockItem<Inventory, EnderChest> ENDER_CHEST;
	public static CustomBlockItem TRASHCAN;
	public static CustomItem STORAGE_CASING;
	public static CustomBlockItem STORAGE_CONNECTOR;
	public static CustomBlockItem STORAGE_IMPORTER;
	public static CustomBlockItem STORAGE_EXPORTER;
	public static CustomBlockItem STORAGE_READER;
	public static CustomBlockItem ENDER_ACCESSOR;
	public static CustomBlockItem STORAGE_MONITOR;
	public static ItemFilterItem NONSTACKABLE_FILTER;
	public static ItemFilterItem DAMAGEABLE_FILTER;
	public static ItemFilterItem NBT_FILTER;
	public static ItemFilterItem ENCHANTED_FILTER;
	public static ItemFilterItem FOOD_FILTER;
	public static ItemFilterItem COOKIES_FILTER;

	// Fluids
	public static TankItem BASIC_TANK;
	public static TankItem ADVANCED_TANK;
	public static TankItem IMPROVED_TANK;
	public static TankItem PERFECTED_TANK;
	public static MachineItem FLUID_PUMP;
	public static EnderLinkedBlockItem<Fluid, EnderTank> ENDER_TANK;
	public static CustomBlockItem WASTE_BARREL;

	// Energy
	public static BatteryItem BATTERY_RED;
	public static BatteryItem BATTERY_YELLOW;
	public static BatteryItem BATTERY_GREEN;
	public static BatteryItem BATTERY_CYAN;
	public static BatteryItem BATTERY_PURPLE;
	public static BatteryItem BATTERY_BLACK;
	public static CustomBlockItem LED_PURPLE;
	public static CustomBlockItem LED_BLUE;
	public static CustomBlockItem LED_GREEN;
	public static CustomBlockItem LED_ORANGE;
	public static CustomBlockItem LED_RED;
	public static EnderLinkedBlockItem<Holder<Integer>, Tesseract> TESSERACT;

	// Magic
	public static MagicMetal MAGIC_METAL;
	public static CustomItem BASIC_WAND;
	public static CustomItem MAGIC_WAND;
	public static CustomItem POWERED_WAND;
	public static CustomItem EMPOWERED_WAND;
	public static CustomItem FLOWERING_FLOWER;
	public static CustomItem FOREST_BUNDLE;
	public static CustomItem FOOD_BUNDLE;
	public static CustomItem ORGANIC_MATTER;
	public static CustomItem PEARLSTONE;
	public static CustomItem OVERWORLD_ARTEFACT;
	public static CustomItem MAGIC_ARTEFACT;
	public static CustomItem NETHER_CORE;
	public static CustomItem NETHER_CRYSTAL;
	public static CustomItem ENDER_CORE;
	public static CustomItem ENDER_CRYSTAL;
	public static CustomItem FLESH_CLUMP;
	public static CustomItem FIRE_CRYSTAL;
	public static CustomItem MONSTER_CORE;
	public static CustomItem SPAWNER_CRYSTAL;
	public static SpawnerWand SPAWNER_WAND;
	public static DragonEye DRAGON_EYE;
	public static CustomItem STICKY_GOO;
	public static CustomItem COMPACT_PEBBLE;
	public static CustomItem COMPACT;
	public static MiniaturizingWand MINIATURIZING_WAND;

	// Fun
	public static List<CardItem> PLAYING_CARDS;
	public static CardPile CARD_PILE;

	// Plushies
	public static CustomBlockItem FALSE_SYMMETRY_PLUSHIE;
	public static CustomBlockItem XISUMA_PLUSHIE;
	public static CustomBlockItem ZEDAPH_PLUSHIE;
	public static CustomBlockItem XB_CRAFTED_PLUSHIE;
	public static CustomBlockItem WELSKNIGHT_PLUSHIE;
	public static CustomBlockItem TIN_FOIL_CHEF_PLUSHIE;
	public static CustomBlockItem MUMBO_JUMBO_PLUSHIE;
	public static CustomBlockItem JOE_HILLS_SAYS_PLUSHIE;
	public static CustomBlockItem HYPNOTIZD_PLUSHIE;
	public static CustomBlockItem GRIAN_PLUSHIE;
	public static CustomBlockItem GUINEA_PIG_GRIAN_PLUSHIE;
	public static CustomBlockItem POULTRY_MAN_PLUSHIE;
	public static CustomBlockItem VINTAGE_BEEF_PLUSHIE;
	public static CustomBlockItem GOOD_TIMES_WITH_SCAR_PLUSHIE;
	public static CustomBlockItem JELLIE_PLUSHIE;
	public static CustomBlockItem KERALIS_PLUSHIE;
	public static CustomBlockItem FRENCHRALIS_PLUSHIE;
	public static CustomBlockItem I_JEVIN_PLUSHIE;
	public static CustomBlockItem ETHOSLAB_PLUSHIE;
	public static CustomBlockItem ISKALL85_PLUSHIE;
	public static CustomBlockItem TANGO_TEK_PLUSHIE;
	public static CustomBlockItem IMPULS_SV_PLUSHIE;
	public static CustomBlockItem STRESSMONSTER101_PLUSHIE;
	public static CustomBlockItem BDOUBLEO100_PLUSHIE;
	public static CustomBlockItem BDOUBLEO100_SMILE_PLUSHIE;
	public static CustomBlockItem DOCM77_PLUSHIE;
	public static CustomBlockItem CUBFAN135_PLUSHIE;
	public static CustomBlockItem DOCTOR_CUBFAN135_PLUSHIE;
	public static CustomBlockItem PHARAO_CUBFAN135_PLUSHIE;
	public static CustomBlockItem ZOMBIE_CLEO_PLUSHIE;
	public static CustomBlockItem REN_THE_DOG_PLUSHIE;
	public static CustomBlockItem REN_BOB_PLUSHIE;
	public static CustomBlockItem PEARLESCENT_MOON_PLUSHIE;
	public static CustomBlockItem GEMINI_TAY_PLUSHIE;
	public static CustomBlockItem JOEYGRACEFFA_PLUSHIE;
	public static CustomBlockItem SHUBBLE_YT_PLUSHIE;
	public static CustomBlockItem SOLIDARITY_GAMING_PLUSHIE;
	public static CustomBlockItem SMALISHBEANS_PLUSHIE;
	public static CustomBlockItem SMAJOR1995_PLUSHIE;
	public static CustomBlockItem PIXLRIFFS_PLUSHIE;
	public static CustomBlockItem MYTHICAL_SAUSAGE_PLUSHIE;
	public static CustomBlockItem LDS_SHADOWLADY_PLUSHIE;
	public static CustomBlockItem KATHERINEELIZ_PLUSHIE;
	public static CustomBlockItem FWHIP_PLUSHIE;

	public static void init() {
		// Miscellaneous
		CUSTOM_CRAFTING_TABLE_BLUEPRINT = ITEMS.register(new CustomCraftingTableBlueprint());
		VANILLA_RECIPE_BOOK = ITEMS.register(new VanillaRecipeBook());
		COOKIE_COOK_BOOK = ITEMS.register(new CookieCookBook());
		MULTI_BLOCK_BOOK = ITEMS.register(new MultiBlockBook());
		PORTABLE_CRAFTING_TABLE = ITEMS.register(new PortableCraftingTable());
		PORTABLE_CUSTOM_CRAFTING_TABLE = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.FLETCHING_TABLE, RecipeType.CUSTOM));
		PORTABLE_ENGINEERING_STATION = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.SMITHING_TABLE, RecipeType.ENGINEER));
		PORTABLE_MAGIC_ALTAR = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.ENCHANTMENT_TABLE, RecipeType.ALTAR));
		PORTABLE_KITCHEN = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.SMOKER_OFF, RecipeType.KITCHEN));
		PORTABLE_NETHER_PORTAL = ITEMS.register(new PortableNetherPortal());
		PORTABLE_END_PORTAL = ITEMS.register(new PortableEndPortal());
		PORTABLE_ENDER_CHEST = ITEMS.register(new PortableEnderChest());
		ANGEL_BLOCK = ITEMS.register(new AngelBlockItem());
		MEASURING_TAPE = ITEMS.register(new MeasuringTape());
		POUCH = ITEMS.register(new Pouch());
		LOOTBOX = ITEMS.register(new Lootbox());

		// Tools
		VEIN_MINER_PICKAXE = ITEMS.register(new VeinMinerPickaxe());
		LUMBER_AXE = ITEMS.register(new LumberAxe());
		FARMER_SCYTHE = ITEMS.register(new FarmerScythe());
		SLIME_SLING = ITEMS.register(new SlimeSling());
		PLAYER_TRACKER = ITEMS.register(new PlayerTracker());
		AIRGUN = ITEMS.register(new Airgun());
		ROCKET_LAUNCHER = ITEMS.register(new RocketLauncher());

		// Weapons
		KNOCKBACK_STICK = ITEMS.register(new KnockbackStick());
		NOOB_SWORD = ITEMS.register(new NoobSword());
		LIGHTNING_BOW = ITEMS.register(new LightningBow());

		// Armor
		GLOW_HAT = ITEMS.register(new GlowHat());
		SCUBA_HELMET = ITEMS.register(new ScubaHelmet());
		INVISIBILITY_HAT = ITEMS.register(new InvisibilityHat());
		CACTUS_SHIRT = ITEMS.register(new CactusShirt());
		TURTLE_SHELL = ITEMS.register(new TurtleShell());
		BERRY_PANTS = ITEMS.register(new BerryPants());
		LUCKY_LEGGINGS_1 = ITEMS.register(new LuckyLeggings(1));
		LUCKY_LEGGINGS_2 = ITEMS.register(new LuckyLeggings(2));
		LUCKY_LEGGINGS_3 = ITEMS.register(new LuckyLeggings(3));
		FARMER_BOOTS = ITEMS.register(new FarmerBoots());
		RABBIT_BOOTS = ITEMS.register(new RabbitBoots());
		ANGEL_WINGS = ITEMS.register(new AngelWings());
		RAINBOW_HELMET = ITEMS.register(new RainbowArmorPiece(ArmorType.HELMET));
		RAINBOW_CHESTPLATE = ITEMS.register(new RainbowArmorPiece(ArmorType.CHESTPLATE));
		RAINBOW_LEGGINGS = ITEMS.register(new RainbowArmorPiece(ArmorType.LEGGINGS));
		RAINBOW_BOOTS = ITEMS.register(new RainbowArmorPiece(ArmorType.BOOTS));
		COLORED_HELMET = ITEMS.register(new ColoredArmorPiece(ArmorType.HELMET));
		COLORED_CHESTPLATE = ITEMS.register(new ColoredArmorPiece(ArmorType.CHESTPLATE));
		COLORED_LEGGINGS = ITEMS.register(new ColoredArmorPiece(ArmorType.LEGGINGS));
		COLORED_BOOTS = ITEMS.register(new ColoredArmorPiece(ArmorType.BOOTS));
		HASTE_HELMET = ITEMS.register(new HasteArmorPiece(ArmorType.HELMET));
		HASTE_CHESTPLATE = ITEMS.register(new HasteArmorPiece(ArmorType.CHESTPLATE));
		HASTE_LEGGINGS = ITEMS.register(new HasteArmorPiece(ArmorType.LEGGINGS));
		HASTE_BOOTS = ITEMS.register(new HasteArmorPiece(ArmorType.BOOTS));

		// Resources
		STONE_PEBBLE = ITEMS.register(new CustomItem("§fStone Pebble", HeadTextures.STONE_PEBBLE));
		ANDESITE_PEBBLE = ITEMS.register(new CustomItem("§fAndesite Pebble", HeadTextures.ANDESITE_PEBBLE));
		DIORITE_PEBBLE = ITEMS.register(new CustomItem("§fDiorite Pebble", HeadTextures.DIORITE_PEBBLE));
		GRANITE_PEBBLE = ITEMS.register(new CustomItem("§fGranite Pebble", HeadTextures.GRANITE_PEBBLE));
		CALCITE_PEBBLE = ITEMS.register(new CustomItem("§fCalcite Pebble", HeadTextures.CALCITE_PEBBLE));
		TUFF_PEBBLE = ITEMS.register(new CustomItem("§fTuff Pebble", HeadTextures.TUFF_PEBBLE));
		DEEPSLATE_PEBBLE = ITEMS.register(new CustomItem("§fDeepslate Pebble", HeadTextures.DEEPSLATE_PEBBLE));
		COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.COMPRESSED_COBBLESTONE, "§fCompressed Cobblestone").setDescription("§7Contains 9 blocks of cobblestone."));
		DOUBLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.DOUBLE_COMPRESSED_COBBLESTONE, "§fDouble Compressed Cobblestone").setDescription("§7Contains 81 blocks of cobblestone."));
		TRIPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.TRIPLE_COMPRESSED_COBBLESTONE, "§fTriple Compressed Cobblestone").setDescription("§7Contains 729 blocks of cobblestone."));
		QUADRUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.QUADRUPLE_COMPRESSED_COBBLESTONE, "§fQuadruple Compressed Cobblestone").setDescription("§7Contains 6561 blocks of cobblestone."));
		QUINTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.QUINTUPLE_COMPRESSED_COBBLESTONE, "§fQuintuple Compressed Cobblestone").setDescription("§7Contains 59049 blocks of cobblestone."));
		SEXTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.SEXTUPLE_COMPRESSED_COBBLESTONE, "§fSextuple Compressed Cobblestone").setDescription("§7Contains 531441 blocks of cobblestone."));
		SEPTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.SEPTUPLE_COMPRESSED_COBBLESTONE, "§fSeptuple Compressed Cobblestone").setDescription("§7Contains 4782969 blocks of cobblestone."));
		OCTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.OCTUPLE_COMPRESSED_COBBLESTONE, "§fOctuple Compressed Cobblestone").setDescription("§7Contains 43046721 blocks of cobblestone."));
		DUST = ITEMS.register(new CustomItem("§fDust", HeadTextures.DUST));
		RED_DUST = ITEMS.register(new CustomItem("§fRed Dust", HeadTextures.RED_DUST));
		BLACK_SAND = ITEMS.register(new CustomItem("§fBlack Sand", HeadTextures.BLACK_SAND));
		CRUSHED_NETHERRACK = ITEMS.register(new CustomItem("§fCrushed Netherrack", HeadTextures.CRUSHED_NETHERRACK));
		CRUSHED_END_STONE = ITEMS.register(new CustomItem("§fCrushed End Stone", HeadTextures.CRUSHED_END_STONE));
		CRUSHED_OBSIDIAN = ITEMS.register(new CustomItem("§fCrushed Obsidian", HeadTextures.CRUSHED_OBSIDIAN));
		CRUSHED_CRYING_OBSIDIAN = ITEMS.register(new CustomItem("§fCrushed Crying Obsidian", HeadTextures.CRUSHED_CRYING_OBSIDIAN));
		SILICON = ITEMS.register(new CustomItem("§fSilicon", HeadTextures.SILICON));
		STEEL_COMPOUND = ITEMS.register(new CustomItem("§fSteel Compound", HeadTextures.STEEL_COMPOUND));
		PULVERIZED_COAL = ITEMS.register(new CustomItem("§fPulverized Coal", Material.GUNPOWDER));
		SULFUR = ITEMS.register(new CustomItem("§fSulfur", Material.GLOWSTONE_DUST));
		QUARTZ_DUST = ITEMS.register(new CustomItem("§fQuartz Dust", Material.SUGAR));
		IRON_DUST = ITEMS.register(new CustomItem("§fIron Dust", Material.GUNPOWDER));
		GOLD_DUST = ITEMS.register(new CustomItem("§fGold Dust", Material.GLOWSTONE_DUST));
		COPPER_DUST = ITEMS.register(new CustomItem("§fCopper Dust", Material.GLOWSTONE_DUST));
		ALUMINUM_DUST = ITEMS.register(new CustomItem("§fAluminum Dust", Material.SUGAR));
		TIN_DUST = ITEMS.register(new CustomItem("§fTin Dust", Material.SUGAR));
		NICKEL_DUST = ITEMS.register(new CustomItem("§fNickel Dust", Material.GLOWSTONE_DUST));
		LEAD_DUST = ITEMS.register(new CustomItem("§fLead Dust", Material.GUNPOWDER));
		SILVER_DUST = ITEMS.register(new CustomItem("§fSilver Dust", Material.SUGAR));
		LITHIUM_DUST = ITEMS.register(new CustomItem("§fLithium Dust", Material.SUGAR));
		MAGNESIUM_DUST = ITEMS.register(new CustomItem("§fMagnesium Dust", Material.SUGAR));
		STEEL_DUST = ITEMS.register(new CustomItem("§fSteel Dust", Material.GUNPOWDER));
		SOUL_DUST = ITEMS.register(new CustomItem("§fSoul Dust", Material.BROWN_DYE));
		ALUMINUM_INGOT = ITEMS.register(new CustomItem("§fAluminum Ingot", Material.IRON_INGOT));
		TIN_INGOT = ITEMS.register(new CustomItem("§fTin Ingot", Material.IRON_INGOT));
		NICKEL_INGOT = ITEMS.register(new CustomItem("§fNickel Ingot", Material.GOLD_INGOT));
		LEAD_INGOT = ITEMS.register(new CustomItem("§fLead Ingot", Material.IRON_INGOT));
		SILVER_INGOT = ITEMS.register(new CustomItem("§fSilver Ingot", Material.IRON_INGOT));
		MAGNESIUM_INGOT = ITEMS.register(new CustomItem("§fMagnesium Ingot", Material.IRON_INGOT));
		STEEL_INGOT = ITEMS.register(new CustomItem("§fSteel Ingot", Material.IRON_INGOT));
		BRONZE_INGOT = ITEMS.register(new CustomItem("§fBronze Ingot", Material.COPPER_INGOT));
		REDSTONE_ALLOY = ITEMS.register(new CustomItem("§eRedstone Alloy", Material.BRICK));
		ELECTRUM_INGOT = ITEMS.register(new CustomItem("§fElectrum Ingot", Material.GOLD_INGOT));
		INVAR_INGOT = ITEMS.register(new CustomItem("§fInvar Ingot", Material.IRON_INGOT));
		ALUMINUM_STEEL_INGOT = ITEMS.register(new CustomItem("§fAluminum Steel Ingot", Material.IRON_INGOT));
		HARDENED_METAL = ITEMS.register(new CustomItem("§eHardened Metal", Material.IRON_INGOT));
		HARDENED_ALLOY = ITEMS.register(new CustomItem("§eHardened Alloy", Material.NETHERITE_INGOT));
		NETHER_STEEL = ITEMS.register(new CustomItem("§eNether Steel", Material.NETHER_BRICK));
		BLAZING_ALLOY = ITEMS.register(new CustomItem("§eBlazing Alloy", Material.GOLD_INGOT));
		ENDER_STEEL = ITEMS.register(new CustomItem("§fEnder Steel", Material.IRON_INGOT));
		ENDERIUM = ITEMS.register(new CustomItem("§bEnderium", Material.NETHERITE_INGOT));
		ENERGETIC_ALLOY = ITEMS.register(new CustomItem("§eEnergetic Alloy", Material.GOLD_INGOT));
		REDSTONIUM = ITEMS.register(new CustomItem("§eRedstonium", Material.COPPER_INGOT));
		CARBON = ITEMS.register(new CustomItem("§fCarbon", HeadTextures.CARBON));
		COMPRESSED_CARBON = ITEMS.register(new CustomItem("§fCompressed Carbon", HeadTextures.COMPRESSED_CARBON));
		CARBON_CHUNK = ITEMS.register(new CustomItem("§fCarbon Chunk", HeadTextures.CARBON_CHUNK));
		CARBONADO = ITEMS.register(new CustomItem("§fCarbonado", HeadTextures.CARBONADO));
		STICKY_BALL = ITEMS.register(new CustomItem("§fSticky Ball", HeadTextures.STICKY_BALL));
		RUBBER = ITEMS.register(new CustomItem("§fRubber", HeadTextures.RUBBER));
		RUBBER_SHEETS = ITEMS.register(new CustomItem("§fRubber Sheets", Material.DRIED_KELP));
		PLASTIC_SHEET = ITEMS.register(new CustomItem("§fPlastic Sheet", Material.PAPER));
		GILDED_PAPER = ITEMS.register(new CustomItem("§fGilded Paper", Material.MAP));
		RAINBOW_DUST = ITEMS.register(new CustomItem(ColorUtils.color("Rainbow Dust", ColorUtils.RAINBOW_COLOR_SEQUENCE, 1), Material.GLOWSTONE_DUST));

		// Bags
		BAG_OF_COAL = ITEMS.register(new CustomItem("§0Bag of Coal", HeadTextures.BAG_OF_COAL));
		BAG_OF_IRON = ITEMS.register(new CustomItem("§7Bag of Iron", HeadTextures.BAG_OF_IRON));
		BAG_OF_GOLD = ITEMS.register(new CustomItem("§6Bag of Gold", HeadTextures.BAG_OF_GOLD));
		BAG_OF_REDSTONE = ITEMS.register(new CustomItem("§cBag of Redstone", HeadTextures.BAG_OF_REDSTONE));
		BAG_OF_LAPIS = ITEMS.register(new CustomItem("§9Bag of Lapis", HeadTextures.BAG_OF_LAPIS));
		BAG_OF_EMERALDS = ITEMS.register(new CustomItem("§aBag of Emeralds", HeadTextures.BAG_OF_EMERALDS));
		BAG_OF_DIAMONDS = ITEMS.register(new CustomItem("§bBag of Diamonds", HeadTextures.BAG_OF_DIAMONDS));
		BAG_OF_AMETHYSTS = ITEMS.register(new CustomItem("§5Bag of Amethysts", HeadTextures.BAG_OF_AMETHYSTS));

		BAG_OF_SEEDS = ITEMS.register(new CustomItem("§aBag of Seeds", HeadTextures.BAG_OF_SEEDS));
		BAG_OF_WHEAT = ITEMS.register(new CustomItem("§eBag of Wheat", HeadTextures.BAG_OF_WHEAT));
		BAG_OF_POTATOES = ITEMS.register(new CustomItem("§eBag of Potatoes", HeadTextures.BAG_OF_POTATOES));
		BAG_OF_POISONOUS_POTATOES = ITEMS.register(new CustomItem("§aBag of Poisonous_Potatoes", HeadTextures.BAG_OF_POISONOUS_POTATOES));
		BAG_OF_CARROTS = ITEMS.register(new CustomItem("§6Bag of Carrots", HeadTextures.BAG_OF_CARROTS));
		BAG_OF_BEETROOTS = ITEMS.register(new CustomItem("§4Bag of Beetroots", HeadTextures.BAG_OF_BEETROOTS));
		BAG_OF_PUMPKINS = ITEMS.register(new CustomItem("§6Bag of Pumpkins", HeadTextures.BAG_OF_PUMPKINS));
		BAG_OF_MELONS = ITEMS.register(new CustomItem("§2Bag of Melons", HeadTextures.BAG_OF_MELONS));
		BAG_OF_CACTI = ITEMS.register(new CustomItem("§2Bag of Cacti", HeadTextures.BAG_OF_CACTI));
		BAG_OF_SUGAR_CANE = ITEMS.register(new CustomItem("§aBag of Sugar_Cane", HeadTextures.BAG_OF_SUGAR_CANE));
		BAG_OF_BAMBOO = ITEMS.register(new CustomItem("§aBag of Bamboo", HeadTextures.BAG_OF_BAMBOO));
		BAG_OF_BROWN_MUSHROOMS = ITEMS.register(new CustomItem("§6Bag of Brown_Mushrooms", HeadTextures.BAG_OF_BROWN_MUSHROOMS));
		BAG_OF_RED_MUSHROOMS = ITEMS.register(new CustomItem("§cBag of Red_Mushrooms", HeadTextures.BAG_OF_RED_MUSHROOMS));
		BAG_OF_FLOWERS = ITEMS.register(new CustomItem("§fBag of Flowers", HeadTextures.BAG_OF_FLOWERS));

		BAG_OF_SNOW_BALLS = ITEMS.register(new CustomItem("§fBag of Snow_Balls", HeadTextures.BAG_OF_SNOW_BALLS));
		BAG_OF_BONE_MEAL = ITEMS.register(new CustomItem("§fBag of Bone_Meal", HeadTextures.BAG_OF_BONE_MEAL));
		BAG_OF_CLAY_BALLS = ITEMS.register(new CustomItem("§7Bag of Clay_Balls", HeadTextures.BAG_OF_CLAY_BALLS));
		BAG_OF_GUNPOWDER = ITEMS.register(new CustomItem("§8Bag of Gunpowder", HeadTextures.BAG_OF_GUNPOWDER));
		BAG_OF_GLOWSTONE_DUST = ITEMS.register(new CustomItem("§6Bag of Glowstone_Dust", HeadTextures.BAG_OF_GLOWSTONE_DUST));
		BAG_OF_NETHER_WARTS = ITEMS.register(new CustomItem("§4Bag of Nether_Warts", HeadTextures.BAG_OF_NETHER_WARTS));
		BAG_OF_SUGAR = ITEMS.register(new CustomItem("§fBag of Sugar", HeadTextures.BAG_OF_SUGAR));
		BAG_OF_SALT = ITEMS.register(new CustomItem("§fBag of Salt", HeadTextures.BAG_OF_SALT));
		BAG_OF_ENDER_PEARLS = ITEMS.register(new CustomItem("§3Bag of Ender_Pearls", HeadTextures.BAG_OF_ENDER_PEARLS));

		BASKET_OF_APPLES = ITEMS.register(new CustomItem("§cBasket of Apples", HeadTextures.BASKET_OF_APPLES));
		BASKET_OF_SWEET_BERRIES = ITEMS.register(new CustomItem("§cBasket of Sweet_Berries", HeadTextures.BASKET_OF_SWEET_BERRIES));
		BASKET_OF_GLOW_BERRIES = ITEMS.register(new CustomItem("§6Basket of Glow_Berries", HeadTextures.BASKET_OF_GLOW_BERRIES));
		BAG_OF_HONEY = ITEMS.register(new CustomItem("§6Bag of Honey", HeadTextures.BAG_OF_HONEY));
		BAG_OF_FISH = ITEMS.register(new CustomItem("§3Bag of Fish", HeadTextures.BAG_OF_FISH));

		// Redstone
		REDSTONE_FREQUENCY_GADGET = ITEMS.register(new RedstoneFrequencyGadget());
		WIRELESS_REDSTONE_TRANSMITTER = ITEMS.register(new CustomBlockItem(Blocks.WIRELESS_REDSTONE_TRANSMITTER, "§cWireless Redstone Transmitter"));
		WIRELESS_REDSTONE_RECEIVER = ITEMS.register(new CustomBlockItem(Blocks.WIRELESS_REDSTONE_RECEIVER, "§cWireless Redstone Receiver"));
		REDSTONE_OR_GATE = ITEMS.register(new CustomBlockItem(Blocks.REDSTONE_OR_GATE, "§cRedstone OR Gate"));
		REDSTONE_AND_GATE = ITEMS.register(new CustomBlockItem(Blocks.REDSTONE_AND_GATE, "§cRedstone AND Gate"));
		REDSTONE_XOR_GATE = ITEMS.register(new CustomBlockItem(Blocks.REDSTONE_XOR_GATE, "§cRedstone XOR Gat"));

		// Electric Components
		COPPER_WIRE = ITEMS.register(new CustomItem("§fCopper Wire", Material.STRING));
		ENERGY_MEASURE_GADGET = ITEMS.register(new EnergyMeasureGadget());
		COPPER_COIL = ITEMS.register(new CustomItem("§fCopper Coil", HeadTextures.COPPER_COIL));
		INSULATED_COPPER_WIRE = ITEMS.register(new InsulatedCopperWire());
		RESISTOR_1 = ITEMS.register(new ResistorItem("§f1Ω Resistor", Material.ORANGE_CANDLE));
		RESISTOR_2 = ITEMS.register(new ResistorItem("§f10Ω Resistor", Material.RED_CANDLE));
		RESISTOR_3 = ITEMS.register(new ResistorItem("§f100Ω Resistor", Material.GREEN_CANDLE));
		RESISTOR_4 = ITEMS.register(new ResistorItem("§f1kΩ Resistor", Material.GRAY_CANDLE));
		RESISTOR_5 = ITEMS.register(new ResistorItem("§f10kΩ Resistor", Material.BLACK_CANDLE));
		CAPACITOR_1 = ITEMS.register(new CustomItem("§f1μF Capacitor", Material.GRAY_DYE));
		CAPACITOR_2 = ITEMS.register(new CustomItem("§f10μF Capacitor", Material.GRAY_DYE));
		CAPACITOR_3 = ITEMS.register(new CustomItem("§f100μF Capacitor", Material.GRAY_DYE));
		CAPACITOR_4 = ITEMS.register(new CustomItem("§f1mF Capacitor", Material.GRAY_DYE));
		CAPACITOR_5 = ITEMS.register(new CustomItem("§f10mF Capacitor", Material.GRAY_DYE));
		ELECTROMAGNET = ITEMS.register(new CustomItem("§fElectromagnet", HeadTextures.MAGNET));
		MOTOR = ITEMS.register(new CustomItem("§fMotor", HeadTextures.ELECTROMOTOR));
		LASER = ITEMS.register(new CustomItem("§fLaser", HeadTextures.LASER));
		ACCUMULATOR = ITEMS.register(new CustomBlockItem(Blocks.ACCUMULATOR, "§fAccumulator"));
		ITEM_ATTRACTOR = ITEMS.register(new CustomBlockItem(Blocks.ITEM_ATTRACTOR, "§fItem Attractor"));
		MONSTER_ATTRACTOR = ITEMS.register(new CustomBlockItem(Blocks.MONSTER_ATTRACTOR, "§fMonster Attractor"));
		ELECTRICAL_CIRCUIT_1 = ITEMS.register(new CustomItem("§fBasic Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_1));
		ELECTRICAL_CIRCUIT_2 = ITEMS.register(new CustomItem("§fSimple Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_2));
		ELECTRICAL_CIRCUIT_3 = ITEMS.register(new CustomItem("§eMessy Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_3));
		ELECTRICAL_CIRCUIT_4 = ITEMS.register(new CustomItem("§eComplex Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_4));
		ELECTRICAL_CIRCUIT_5 = ITEMS.register(new CustomItem("§bPowerful Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_5));
		PHOTOVOLTAIC_CELL_1 = ITEMS.register(new CustomItem("§fPhotovoltaic Cell I", Material.GLASS_PANE));
		PHOTOVOLTAIC_CELL_2 = ITEMS.register(new CustomItem("§fPhotovoltaic Cell II", Material.WHITE_STAINED_GLASS_PANE));
		PHOTOVOLTAIC_CELL_3 = ITEMS.register(new CustomItem("§ePhotovoltaic Cell III", Material.LIGHT_BLUE_STAINED_GLASS_PANE));
		PHOTOVOLTAIC_CELL_4 = ITEMS.register(new CustomItem("§ePhotovoltaic Cell IV", Material.CYAN_STAINED_GLASS_PANE));
		PHOTOVOLTAIC_CELL_5 = ITEMS.register(new CustomItem("§bPhotovoltaic Cell V", Material.BLUE_STAINED_GLASS_PANE));
		PHOTOVOLTAIC_CELL_6 = ITEMS.register(new CustomItem("§bPhotovoltaic Cell VI", Material.BLUE_STAINED_GLASS_PANE));
		PHOTOVOLTAIC_CELL_7 = ITEMS.register(new CustomItem("§dPhotovoltaic Cell VII", Material.LIME_STAINED_GLASS_PANE));
		PHOTOVOLTAIC_CELL_8 = ITEMS.register(new CustomItem("§dPhotovoltaic Cell VIII", Material.GREEN_STAINED_GLASS_PANE));

		// Technical Components
		CIRCUIT_BOARD = ITEMS.register(new CustomItem("§fCircuit Board", Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
		LIGHT_CONVEYOR_BELT = ITEMS.register(new CustomBlockItem(Blocks.LIGHT_CONVEYOR_BELT, "§fLight Conveyor Belt"));
		MEDIUM_CONVEYOR_BELT = ITEMS.register(new CustomBlockItem(Blocks.MEDIUM_CONVEYOR_BELT, "§fMedium Conveyor Belt"));
		HEAVY_CONVEYOR_BELT = ITEMS.register(new CustomBlockItem(Blocks.HEAVY_CONVEYOR_BELT, "§fHeavy Conveyor Belt"));
		UPGRADE_BASE = ITEMS.register(new CustomItem("§cMachine Upgrade Base", Material.MAP));
		UPGRADE_SHARPNESS = ITEMS.register(new CustomItem("§bSharpness", Material.MAP));
		UPGRADE_FORTUNE = ITEMS.register(new CustomItem("§9Fortune", Material.MAP));
		UPGRADE_FIREASPECT = ITEMS.register(new CustomItem("§6Fireaspect", Material.MAP));
		UPGRADE_BEHEADING = ITEMS.register(new CustomItem("§8Beheading", Material.MAP));
		UPGRADE_RANGE = ITEMS.register(new CustomItem("§eRange", Material.MAP));
		UPGRADE_SPEED = ITEMS.register(new CustomItem("§fSpeed", Material.MAP));
		UPGRADE_EFFICIENCY = ITEMS.register(new CustomItem("§aEfficiency", Material.MAP));
		UPGRADE_ENERGY_STORAGE = ITEMS.register(new CustomItem("§cEnergy Storage", Material.MAP));

		// Machines
		BASIC_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.BASIC_MACHINE_CASING, "§fBasic Machine Casing"));
		ADVANCED_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.ADVANCED_MACHINE_CASING, "§eAdvanced Machine Casing"));
		IMPROVED_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.IMPROVED_MACHINE_CASING, "§bImproved Machine Casing"));
		PERFECTED_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.PERFECTED_MACHINE_CASING, "§dPerfected Machine Casing"));
		COBBLESTONE_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.COBBLESTONE_GENERATOR, "§fCobblestone Generator"));
		STONE_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.STONE_GENERATOR, "§fStone Generator"));
		DRIPSTONE_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.DRIPSTONE_GENERATOR, "§fDripstone Generator"));
		BASALT_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.BASALT_GENERATOR, "§fBasalt Generator"));
		LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.LAVA_GENERATOR, "§fLava Generator"));
		BASIC_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.BASIC_LAVA_GENERATOR, "§fBasic Lava Generator"));
		ADVANCED_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.ADVANCED_LAVA_GENERATOR, "§eAdvanced Lava Generator"));
		IMPROVED_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.IMPROVED_LAVA_GENERATOR, "§bImproved Lava Generator"));
		PERFECTED_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.PERFECTED_LAVA_GENERATOR, "§dPerfected Lava Generator"));
		OBSIDIAN_GENERATOR = ITEMS.register(new MachineItem(Blocks.OBSIDIAN_GENERATOR).setDescription("§7Cools lava down to obsidian."));
		ITEM_ABSORBER = ITEMS.register(new MachineItem(Blocks.ITEM_ABSORBER).setDescription("§7Absorbes nearby items and stores them internal."));
		EXPERIENCE_ABSORBER = ITEMS.register(new MachineItem(Blocks.EXPERIENCE_ABSORBER).setDescription("§7Absorbes Experience and stores it for later."));
		BASIC_FARMER = ITEMS.register(new MachineItem(Blocks.BASIC_FARMER));
		ADVANCED_FARMER = ITEMS.register(new MachineItem(Blocks.ADVANCED_FARMER));
		IMPROVED_FARMER = ITEMS.register(new MachineItem(Blocks.IMPROVED_FARMER));
		PERFECTED_FARMER = ITEMS.register(new MachineItem(Blocks.PERFECTED_FARMER));
		MOB_GRINDER = ITEMS.register(new MachineItem(Blocks.MOB_GRINDER));
		BLOCK_BREAKER = ITEMS.register(new MachineItem(Blocks.BLOCK_BREAKER).setDescription("§7Breaks the block in front."));
		QUARRY = ITEMS.register(new MachineItem(Blocks.QUARRY));
		BASIC_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.BASIC_VOID_ORE_MINER));
		ADVANCED_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.ADVANCED_VOID_ORE_MINER));
		IMPROVED_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.IMPROVED_VOID_ORE_MINER));
		PERFECTED_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.PERFECTED_VOID_ORE_MINER));
		DYE_PRESS = ITEMS.register(new MachineItem(Blocks.DYE_PRESS));
		DYE_MIXER = ITEMS.register(new MachineItem(Blocks.DYE_MIXER));
		ENCHANTER = ITEMS.register(new MachineItem(Blocks.ENCHANTER).setDescription("§7Removed enchantments from books and stores them into an item.", "§7Can enchant gold ingots into magic metal."));
		DISENCHANTER = ITEMS.register(new MachineItem(Blocks.DISENCHANTER).setDescription("§7Removes enchantments from an item and stores them onto a book."));
		ENCHANTMENT_COMBINER = ITEMS.register(new MachineItem(Blocks.ENCHANTMENT_COMBINER).setDescription("§7Combines the enchantments of two books and stores all in one.", "§7Two enchantments of the same level will be combined with the level increased by one."));
		COMPUTER = ITEMS.register(new CustomBlockItem(Blocks.COMPUTER, "§dComputer"));
		CHUNK_LOADER = ITEMS.register(new CustomBlockItem(Blocks.CHUNK_LOADER, "§dChunk Loader"));
		HYPER_FURNACE_1 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_1));
		HYPER_FURNACE_2 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_2));
		HYPER_FURNACE_3 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_3));
		HYPER_FURNACE_4 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_4));
		HYPER_FURNACE_5 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_5));
		HYPER_FURNACE_6 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_6));
		HYPER_FURNACE_7 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_7));
		HYPER_FURNACE_8 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_8));
		HYPER_FURNACE_9 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_9));
		HYPER_FURNACE_10 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_10));
		HYPER_FURNACE_11 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_11));
		HYPER_FURNACE_12 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_12));
		BASIC_CRUSHER = ITEMS.register(new MachineItem(Blocks.BASIC_CRUSHER));
		ADVANCED_CRUSHER = ITEMS.register(new MachineItem(Blocks.ADVANCED_CRUSHER));
		IMPROVED_CRUSHER = ITEMS.register(new MachineItem(Blocks.IMPROVED_CRUSHER));
		PERFECTED_CRUSHER = ITEMS.register(new MachineItem(Blocks.PERFECTED_CRUSHER));
		BASIC_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.BASIC_MINERAL_EXTRACTOR));
		ADVANCED_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.ADVANCED_MINERAL_EXTRACTOR));
		IMPROVED_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.IMPROVED_MINERAL_EXTRACTOR));
		PERFECTED_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.PERFECTED_MINERAL_EXTRACTOR));
		BASIC_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.BASIC_AERIAL_EXTRACTOR));
		ADVANCED_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.ADVANCED_AERIAL_EXTRACTOR));
		IMPROVED_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.IMPROVED_AERIAL_EXTRACTOR));
		PERFECTED_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.PERFECTED_AERIAL_EXTRACTOR));
		BASIC_SAWMILL = ITEMS.register(new MachineItem(Blocks.BASIC_SAWMILL));
		ADVANCED_SAWMILL = ITEMS.register(new MachineItem(Blocks.ADVANCED_SAWMILL));
		IMPROVED_SAWMILL = ITEMS.register(new MachineItem(Blocks.IMPROVED_SAWMILL));
		PERFECTED_SAWMILL = ITEMS.register(new MachineItem(Blocks.PERFECTED_SAWMILL));
		BASIC_SMELTERY = ITEMS.register(new MachineItem(Blocks.BASIC_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
		ADVANCED_SMELTERY = ITEMS.register(new MachineItem(Blocks.ADVANCED_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
		IMPROVED_SMELTERY = ITEMS.register(new MachineItem(Blocks.IMPROVED_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
		PERFECTED_SMELTERY = ITEMS.register(new MachineItem(Blocks.PERFECTED_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
		BASIC_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.BASIC_COMPRESSOR));
		ADVANCED_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.ADVANCED_COMPRESSOR));
		IMPROVED_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.IMPROVED_COMPRESSOR));
		PERFECTED_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.PERFECTED_COMPRESSOR));
		BASIC_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.BASIC_BIO_PRESS));
		ADVANCED_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.ADVANCED_BIO_PRESS));
		IMPROVED_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.IMPROVED_BIO_PRESS));
		PERFECTED_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.PERFECTED_BIO_PRESS));
		LATEX_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.LATEX_EXTRACTOR));
		BASIC_CRAFTER = ITEMS.register(new MachineItem(Blocks.BASIC_CRAFTER));
		ADVANCED_CRAFTER = ITEMS.register(new MachineItem(Blocks.ADVANCED_CRAFTER));
		IMPROVED_CRAFTER = ITEMS.register(new MachineItem(Blocks.IMPROVED_CRAFTER));
		PERFECTED_CRAFTER = ITEMS.register(new MachineItem(Blocks.PERFECTED_CRAFTER));
		BASIC_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.BASIC_CRAFTING_FACTORY));
		ADVANCED_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.ADVANCED_CRAFTING_FACTORY));
		IMPROVED_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.IMPROVED_CRAFTING_FACTORY));
		PERFECTED_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.PERFECTED_CRAFTING_FACTORY));
		BASIC_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.BASIC_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
		ADVANCED_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.ADVANCED_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
		IMPROVED_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.IMPROVED_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
		PERFECTED_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.PERFECTED_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
		BASIC_FREEZER = ITEMS.register(new MachineItem(Blocks.BASIC_FREEZER));
		ADVANCED_FREEZER = ITEMS.register(new MachineItem(Blocks.ADVANCED_FREEZER));
		IMPROVED_FREEZER = ITEMS.register(new MachineItem(Blocks.IMPROVED_FREEZER));
		PERFECTED_FREEZER = ITEMS.register(new MachineItem(Blocks.PERFECTED_FREEZER));
		HONEY_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.HONEY_EXTRACTOR));
		STAR_MAKER = ITEMS.register(new MachineItem(Blocks.STAR_MAKER));
		ROCKET_ASSEMBLER = ITEMS.register(new MachineItem(Blocks.ROCKET_ASSEMBLER));

		// Generators
		FURNACE_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.FURNACE_GENERATOR).setDescription("§7Generates Energy when placed above a Furnace, Blastfurnace or Smoker."));
		THERMO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.THERMO_GENERATOR).setDescription("§7Generates Energy from Lava."));
		BASIC_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.BASIC_SOLAR_PANEL));
		ADVANCED_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.ADVANCED_SOLAR_PANEL));
		IMPROVED_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.IMPROVED_SOLAR_PANEL));
		PERFECTED_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.PERFECTED_SOLAR_PANEL));
		BASIC_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.BASIC_BIO_GENERATOR));
		ADVANCED_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.ADVANCED_BIO_GENERATOR));
		IMPROVED_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.IMPROVED_BIO_GENERATOR));
		PERFECTED_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.PERFECTED_BIO_GENERATOR));
		CREATIVE_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.CREATIVE_GENERATOR));

		// Storage
		BROWN_BACKPACK = ITEMS.register(new Backpack(Blocks.BROWN_BACKPACK));
		COPPER_BACKPACK = ITEMS.register(new Backpack(Blocks.COPPER_BACKPACK));
		IRON_BACKPACK = ITEMS.register(new Backpack(Blocks.IRON_BACKPACK));
		GOLDEN_BACKPACK = ITEMS.register(new Backpack(Blocks.GOLDEN_BACKPACK));
		DIAMOND_BACKPACK = ITEMS.register(new Backpack(Blocks.DIAMOND_BACKPACK));
		NETHERITE_BACKPACK = ITEMS.register(new Backpack(Blocks.NETHERITE_BACKPACK));
		OAK_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.OAK_STORAGE_CRATE, "§fOak Storage Crate"));
		SPRUCE_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.SPRUCE_STORAGE_CRATE, "§fSpruce Storage Crate"));
		BIRCH_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.BIRCH_STORAGE_CRATE, "§fBirch Storage Crate"));
		JUNGLE_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.JUNGLE_STORAGE_CRATE, "§fJungle Storage Crate"));
		ACACIA_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.ACACIA_STORAGE_CRATE, "§fAcacia Storage Crate"));
		DARK_OAK_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.DARK_OAK_STORAGE_CRATE, "§fDark Oak Storage Crate"));
		MANGROVE_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.MANGROVE_STORAGE_CRATE, "§fMangrove Storage Crate"));
		CHERRY_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.CHERRY_STORAGE_CRATE, "§fCherry Storage Crate"));
		PALE_OAK_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.PALE_OAK_STORAGE_CRATE, "§fPale Oak Storage Crate"));
		BAMBOO_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.BAMBOO_STORAGE_CRATE, "§fBamboo Storage Crate"));
		CRIMSON_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.CRIMSON_STORAGE_CRATE, "§fCrimson Storage Crate"));
		WARPED_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.WARPED_STORAGE_CRATE, "§fWarped Storage Crate"));
		IRON_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.IRON_STORAGE_CRATE, "§eIron Storage Crate"));
		GOLDEN_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.GOLDEN_STORAGE_CRATE, "§bGolden Storage Crate"));
		DIAMOND_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.DIAMOND_STORAGE_CRATE, "§dDiamond Storage Crate"));
		EMERALD_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.EMERALD_STORAGE_CRATE, "§aEmerald Storage Crate"));
		CLOWNFISH_STORAGE_CRATE = ITEMS.register(new CustomBlockItem(Blocks.CLOWNFISH_STORAGE_SKULL_BLOCK, "§9Clownfish Storage Crate"));
		ENDER_CHEST = ITEMS.register(new EnderLinkedBlockItem<>(Blocks.ENDER_CHEST, "§eEnder Chest"));
		TRASHCAN = ITEMS.register(new CustomBlockItem(Blocks.TRASHCAN, "§fTrashcan"));
		STORAGE_CASING = ITEMS.register(new CustomItem("§fStorage Casing", HeadTextures.STORAGE_CASING));
		STORAGE_CONNECTOR = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_CONNECTOR, "§fStorage Connector"));
		STORAGE_IMPORTER = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_IMPORTER, "§eStorage Importer").setDescription("§7Import items from adjacent inventory holders and storage blocks."));
		STORAGE_EXPORTER = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_EXPORTER, "§eStorage Exporter").setDescription("§7Exports items to adjacent inventory holders and storage blocks."));
		STORAGE_READER = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_READER, "§eStorage Reader").setDescription("§7Emits an redstone signal if the filter matches any item in the facing storage."));
		ENDER_ACCESSOR = ITEMS.register(new CustomBlockItem(Blocks.ENDER_ACCESSOR, "§bEnder Accessor").setDescription("§7Acts as a port to the enderchest of the owner of this block."));
		STORAGE_MONITOR = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_MONITOR, "§bStorage Monitor").setDescription("§7The main component of item networks.", "§7Lists all items in the network and lets the user interact with them."));
		NONSTACKABLE_FILTER = ITEMS.register(ItemFilterItem.of("§fNon-Stackable Filter", "§7Filters items that cannot be stacked.", m -> m.getMaxStackSize() == 1));
		DAMAGEABLE_FILTER = ITEMS.register(ItemFilterItem.of("§fDamageable Filter", "§7Filters items that can be damaged.", m -> m.getMaxDurability() > 0));
		NBT_FILTER = ITEMS.register(new ItemFilterItem("§fNBT Filter", "§7Filters items that store currently nbt data.", stack -> MinecraftItemHelper.getNumberComponents(stack) > 0));
		ENCHANTED_FILTER = ITEMS.register(new ItemFilterItem("§fEnchanted Filter", "§7Filters items that are enchanted.", stack -> !stack.getEnchantments().isEmpty()));
		FOOD_FILTER = ITEMS.register(ItemFilterItem.of("§fFood Filter", "§7Filters items that can be eaten.", Material::isEdible));
		COOKIES_FILTER = ITEMS.register(new ItemFilterItem("§fCookies Filter", "§7Filters items that were added by the §6Cookies §7plugin.", ItemUtils::isCustomItem));

		// Fluids
		BASIC_TANK = ITEMS.register(new TankItem(Blocks.BASIC_TANK));
		ADVANCED_TANK = ITEMS.register(new TankItem(Blocks.ADVANCED_TANK));
		IMPROVED_TANK = ITEMS.register(new TankItem(Blocks.IMPROVED_TANK));
		PERFECTED_TANK = ITEMS.register(new TankItem(Blocks.PERFECTED_TANK));
		FLUID_PUMP = ITEMS.register(new MachineItem(Blocks.FLUID_PUMP));
		ENDER_TANK = ITEMS.register(new EnderLinkedBlockItem<>(Blocks.ENDER_TANK, "§eEnder Tank"));
		WASTE_BARREL = ITEMS.register(new CustomBlockItem(Blocks.WASTE_BARREL, "§fWaste Barrel"));

		// Energy
		BATTERY_RED = ITEMS.register(new BatteryItem(Blocks.BATTERY_RED, "§fRed Battery"));
		BATTERY_YELLOW = ITEMS.register(new BatteryItem(Blocks.BATTERY_YELLOW, "§fYellow Battery"));
		BATTERY_GREEN = ITEMS.register(new BatteryItem(Blocks.BATTERY_GREEN, "§fGreen Battery"));
		BATTERY_CYAN = ITEMS.register(new BatteryItem(Blocks.BATTERY_CYAN, "§fCyan Battery"));
		BATTERY_PURPLE = ITEMS.register(new BatteryItem(Blocks.BATTERY_PURPLE, "§fPurple Battery"));
		BATTERY_BLACK = ITEMS.register(new BatteryItem(Blocks.BATTERY_BLACK, "§fBlack Battery"));
		LED_PURPLE = ITEMS.register(new CustomBlockItem(Blocks.LED_PURPLE, "§5LED"));
		LED_BLUE = ITEMS.register(new CustomBlockItem(Blocks.LED_BLUE, "§9LED"));
		LED_GREEN = ITEMS.register(new CustomBlockItem(Blocks.LED_GREEN, "§aLED"));
		LED_ORANGE = ITEMS.register(new CustomBlockItem(Blocks.LED_ORANGE, "§6LED"));
		LED_RED = ITEMS.register(new CustomBlockItem(Blocks.LED_RED, "§cLED"));
		TESSERACT = ITEMS.register(new EnderLinkedBlockItem<>(Blocks.TESSERACT, "§dTesseract"));

		// Magic
		MAGIC_METAL = ITEMS.register(new MagicMetal());
		BASIC_WAND = ITEMS.register(new CustomItem("§fBasic Wand", Material.BLAZE_ROD));
		MAGIC_WAND = ITEMS.register(new CustomItem("§fMagic Wand", Material.BLAZE_ROD));
		POWERED_WAND = ITEMS.register(new CustomItem("§ePowered Wand", Material.BLAZE_ROD));
		EMPOWERED_WAND = ITEMS.register(new CustomItem("§bEmpowered Wand", Material.BLAZE_ROD));
		FLOWERING_FLOWER = ITEMS.register(new CustomItem("§eFlowering Flower", Material.OXEYE_DAISY));
		FOREST_BUNDLE = ITEMS.register(new CustomItem("§eForest Bundle", Material.DARK_OAK_SAPLING));
		FOOD_BUNDLE = ITEMS.register(new CustomItem("§eFood Bundle", Material.RABBIT_STEW));
		ORGANIC_MATTER = ITEMS.register(new CustomItem("§eOrganic Matter", HeadTextures.SATURATED_MOSS));
		PEARLSTONE = ITEMS.register(new CustomItem("§ePearlstone", HeadTextures.RAINBOW_GEM));
		OVERWORLD_ARTEFACT = ITEMS.register(new CustomItem("§bOverworld Artefact", HeadTextures.GLOBE));
		MAGIC_ARTEFACT = ITEMS.register(new CustomItem("§bMagic Artefact", Material.ENCHANTED_GOLDEN_APPLE));
		NETHER_CORE = ITEMS.register(new CustomItem("§eNether Core", HeadTextures.NETHER_CORE));
		NETHER_CRYSTAL = ITEMS.register(new CustomItem("§bNether Crystal", Material.NETHER_STAR));
		ENDER_CORE = ITEMS.register(new CustomItem("§eEnder Core", HeadTextures.ENDER_CORE));
		ENDER_CRYSTAL = ITEMS.register(new CustomItem("§bEnder Crystal", HeadTextures.ENDER_CRYSTAL));
		FLESH_CLUMP = ITEMS.register(new CustomItem("§eFlesh Clump", HeadTextures.FLESH_CLUMP));
		FIRE_CRYSTAL = ITEMS.register(new CustomItem("§eFire Crystal", HeadTextures.FIRE_CRYSTAL));
		MONSTER_CORE = ITEMS.register(new CustomItem("§bMonster Core", HeadTextures.MONSTER_CORE));
		SPAWNER_CRYSTAL = ITEMS.register(new CustomItem("§dSpawner Crystal", HeadTextures.EMPTY_SPAWNER));
		SPAWNER_WAND = ITEMS.register(new SpawnerWand());
		DRAGON_EYE = ITEMS.register(new DragonEye());
		STICKY_GOO = ITEMS.register(new CustomItem("§fSticky Goo", HeadTextures.STICKY_GOO));
		COMPACT_PEBBLE = ITEMS.register(new CustomItem("§eCompact Pebble", Material.FLINT));
		COMPACT = ITEMS.register(new CustomItem("§bFat", HeadTextures.COMPACT));
		MINIATURIZING_WAND = ITEMS.register(new MiniaturizingWand());

		// Fun
		PLAYING_CARDS = ITEMS.register(Stream.of(CardItem.Color.values()).flatMap(color -> Stream.of(CardItem.Rank.values()).map(rank -> new CardItem(color, rank))));
		CARD_PILE = ITEMS.register(new CardPile());

		// Plushies
		FALSE_SYMMETRY_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.FALSE_SYMMETRY_PLUSHIE, "§eFalseSymmetry"));
		XISUMA_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.XISUMA_PLUSHIE, "§aXisuma"));
		ZEDAPH_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ZEDAPH_PLUSHIE, "§6Zedaph"));
		XB_CRAFTED_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.XB_CRAFTED_PLUSHIE, "§cXBCrafted"));
		WELSKNIGHT_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.WELSKNIGHT_PLUSHIE, "§9Welsknight"));
		TIN_FOIL_CHEF_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.TIN_FOIL_CHEF_PLUSHIE, "§7TinFoilChef"));
		MUMBO_JUMBO_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.MUMBO_JUMBO_PLUSHIE, "§8MumboJumbo"));
		JOE_HILLS_SAYS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.JOE_HILLS_SAYS_PLUSHIE, "§bJoeHillsSays"));
		HYPNOTIZD_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.HYPNOTIZD_PLUSHIE, "§8Hypnotized"));
		GRIAN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GRIAN_PLUSHIE, "§cGrian"));
		GUINEA_PIG_GRIAN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GUINEA_PIG_GRIAN_PLUSHIE, "§6Guinea-Pig Grian"));
		POULTRY_MAN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.POULTRY_MAN_PLUSHIE, "§6Poultry Man"));
		VINTAGE_BEEF_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.VINTAGE_BEEF_PLUSHIE, "§9VintageBeef"));
		GOOD_TIMES_WITH_SCAR_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GOOD_TIMES_WITH_SCAR_PLUSHIE, "§6GoodTimesWithScar"));
		JELLIE_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.JELLIE_PLUSHIE, "§7Jellie"));
		KERALIS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.KERALIS_PLUSHIE, "§eKeralis"));
		FRENCHRALIS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.FRENCHRALIS_PLUSHIE, "§6Frenchralis"));
		I_JEVIN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.I_JEVIN_PLUSHIE, "§bIJevin"));
		ETHOSLAB_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ETHOSLAB_PLUSHIE, "§8Ethoslab"));
		ISKALL85_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ISKALL85_PLUSHIE, "§aIskall85"));
		TANGO_TEK_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.TANGO_TEK_PLUSHIE, "§cTangoTek"));
		IMPULS_SV_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.IMPULS_SV_PLUSHIE, "§eImpulseSV"));
		STRESSMONSTER101_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.STRESSMONSTER101_PLUSHIE, "§dStressMonster101"));
		BDOUBLEO100_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.BDOUBLEO100_PLUSHIE, "§9BDoubleO100"));
		BDOUBLEO100_SMILE_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.BDOUBLEO100_SMILE_PLUSHIE, "§bBDoubleO100"));
		DOCM77_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.DOCM77_PLUSHIE, "§aDocm77"));
		CUBFAN135_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.CUBFAN135_PLUSHIE, "§fCubfan135"));
		DOCTOR_CUBFAN135_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.DOCTOR_CUBFAN135_PLUSHIE, "§fDoctor Cubfan135"));
		PHARAO_CUBFAN135_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.PHARAO_CUBFAN135_PLUSHIE, "§6Pharao Cubfan135"));
		ZOMBIE_CLEO_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ZOMBIE_CLEO_PLUSHIE, "§6ZombieCleo"));
		REN_THE_DOG_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.REN_THE_DOG_PLUSHIE, "§cRenTheDog"));
		REN_BOB_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.REN_BOB_PLUSHIE, "§cRenBob"));
		PEARLESCENT_MOON_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.PEARLESCENT_MOON_PLUSHIE, "§bPearlescentMoon"));
		GEMINI_TAY_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GEMINI_TAY_PLUSHIE, "§aGeminiTay"));
		JOEYGRACEFFA_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.JOEYGRACEFFA_PLUSHIE, "§3JoeyGraceffa"));
		SHUBBLE_YT_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SHUBBLE_YT_PLUSHIE, "§cShubbleYT"));
		SOLIDARITY_GAMING_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SOLIDARITY_GAMING_PLUSHIE, "§6SolidarityGaming"));
		SMALISHBEANS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SMALISHBEANS_PLUSHIE, "§cSmalishbeans"));
		SMAJOR1995_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SMAJOR1995_PLUSHIE, "§bSmajor1995"));
		PIXLRIFFS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.PIXLRIFFS_PLUSHIE, "§9Pixlriffs"));
		MYTHICAL_SAUSAGE_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.MYTHICAL_SAUSAGE_PLUSHIE, "§cMythicalSausage"));
		LDS_SHADOWLADY_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.LDS_SHADOWLADY_PLUSHIE, "§dLDSShadowlady"));
		KATHERINEELIZ_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.KATHERINEELIZ_PLUSHIE, "§bKatherineeliz"));
		FWHIP_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.FWHIP_PLUSHIE, "§cfWhip"));

		ITEMS.forEach(Configurable::configure);
	}


	public static AbstractCustomItem getCustomItemFromStack(ItemStack stack) {
		if(ItemUtils.isEmpty(stack))
			return null;

		if(!ItemUtils.isCustomItem(stack))
			return null;

		return ITEMS.filterFirst(item -> item.getIdentifier().equals(Properties.IDENTIFIER.fetch(stack.getItemMeta())));
	}


	public static AbstractCustomItem getCustomItemFromCustomBlock(AbstractCustomBlock block) {
		return ITEMS.filterFirst(item -> item.getIdentifier().equals(block.getIdentifier()));
	}


	public static boolean isInstanceOf(ItemStack stack, Class<?> clazz) {
		AbstractCustomItem item = getCustomItemFromStack(stack);
		return item != null && clazz.isAssignableFrom(item.getClass());
	}

	private static CachingSupplier<List<PlayerRegister>> PLAYER_REGISTER_SUPPLIER = new CachingSupplier<>(() -> ITEMS.filterByClass(PlayerRegister.class));

	public static List<PlayerRegister> getPlayerRegister() {
		return PLAYER_REGISTER_SUPPLIER.get();
	}

}
