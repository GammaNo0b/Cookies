
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.BLOCKS;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.Backpack;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.CardPile;
import me.gamma.cookies.object.block.ChunkLoader;
import me.gamma.cookies.object.block.Computer;
import me.gamma.cookies.object.block.ConveyorBelt;
import me.gamma.cookies.object.block.CustomBlock;
import me.gamma.cookies.object.block.LED;
import me.gamma.cookies.object.block.MachineCasing;
import me.gamma.cookies.object.block.MagnetBlock;
import me.gamma.cookies.object.block.Upgradeable;
import me.gamma.cookies.object.block.generator.CreativeGenerator;
import me.gamma.cookies.object.block.generator.FluidGenerator;
import me.gamma.cookies.object.block.generator.FurnaceGenerator;
import me.gamma.cookies.object.block.generator.SolarPanel;
import me.gamma.cookies.object.block.generator.ThermoGenerator;
import me.gamma.cookies.object.block.machine.AbstractCraftingMachine;
import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.block.machine.AerialExtractor;
import me.gamma.cookies.object.block.machine.BioPress;
import me.gamma.cookies.object.block.machine.BlockBreaker;
import me.gamma.cookies.object.block.machine.BlockGenerator;
import me.gamma.cookies.object.block.machine.CarbonPress;
import me.gamma.cookies.object.block.machine.Compressor;
import me.gamma.cookies.object.block.machine.Crafter;
import me.gamma.cookies.object.block.machine.CraftingFactory;
import me.gamma.cookies.object.block.machine.Crusher;
import me.gamma.cookies.object.block.machine.Disenchanter;
import me.gamma.cookies.object.block.machine.Dryer;
import me.gamma.cookies.object.block.machine.DyePress;
import me.gamma.cookies.object.block.machine.Dyer;
import me.gamma.cookies.object.block.machine.Enchanter;
import me.gamma.cookies.object.block.machine.EnchantmentCombiner;
import me.gamma.cookies.object.block.machine.ExperienceAbsorber;
import me.gamma.cookies.object.block.machine.Farmer;
import me.gamma.cookies.object.block.machine.FluidPump;
import me.gamma.cookies.object.block.machine.Freezer;
import me.gamma.cookies.object.block.machine.HoneyExtractor;
import me.gamma.cookies.object.block.machine.HyperFurnace;
import me.gamma.cookies.object.block.machine.ItemAbsorber;
import me.gamma.cookies.object.block.machine.LatexExtractor;
import me.gamma.cookies.object.block.machine.LavaGenerator;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.block.machine.MineralExtractor;
import me.gamma.cookies.object.block.machine.MobGrinder;
import me.gamma.cookies.object.block.machine.ObsidianGenerator;
import me.gamma.cookies.object.block.machine.Quarry;
import me.gamma.cookies.object.block.machine.RocketAssembler;
import me.gamma.cookies.object.block.machine.Sawmill;
import me.gamma.cookies.object.block.machine.Smeltery;
import me.gamma.cookies.object.block.machine.StarMaker;
import me.gamma.cookies.object.block.machine.VoidOreMiner;
import me.gamma.cookies.object.block.network.energy.Battery;
import me.gamma.cookies.object.block.network.energy.Tesseract;
import me.gamma.cookies.object.block.network.fluid.EnderTank;
import me.gamma.cookies.object.block.network.fluid.Tank;
import me.gamma.cookies.object.block.network.fluid.WasteBarrel;
import me.gamma.cookies.object.block.network.item.EnderAccessor;
import me.gamma.cookies.object.block.network.item.EnderChest;
import me.gamma.cookies.object.block.network.item.StorageConnector;
import me.gamma.cookies.object.block.network.item.StorageCrateBlock;
import me.gamma.cookies.object.block.network.item.StorageExporter;
import me.gamma.cookies.object.block.network.item.StorageImporter;
import me.gamma.cookies.object.block.network.item.StorageMonitor;
import me.gamma.cookies.object.block.network.item.StorageReader;
import me.gamma.cookies.object.block.network.item.Trashcan;
import me.gamma.cookies.object.block.redstone.DoubleInputRedstoneGate;
import me.gamma.cookies.object.block.redstone.WirelessRedstoneReceiver;
import me.gamma.cookies.object.block.redstone.WirelessRedstoneTransmitter;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.CachingSupplier;



public class Blocks {

	// Miscellaneous
	public static CustomBlock ANGEL_BLOCK;

	// Resources
	public static CustomBlock COMPRESSED_COBBLESTONE;
	public static CustomBlock DOUBLE_COMPRESSED_COBBLESTONE;
	public static CustomBlock TRIPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlock QUADRUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlock QUINTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlock SEXTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlock SEPTUPLE_COMPRESSED_COBBLESTONE;
	public static CustomBlock OCTUPLE_COMPRESSED_COBBLESTONE;

	// Redstone
	public static WirelessRedstoneTransmitter WIRELESS_REDSTONE_TRANSMITTER;
	public static WirelessRedstoneReceiver WIRELESS_REDSTONE_RECEIVER;
	public static DoubleInputRedstoneGate REDSTONE_OR_GATE;
	public static DoubleInputRedstoneGate REDSTONE_AND_GATE;
	public static DoubleInputRedstoneGate REDSTONE_XOR_GATE;

	// Electric Components
	public static Battery ACCUMULATOR;
	public static MagnetBlock ITEM_ATTRACTOR;
	public static MagnetBlock MONSTER_ATTRACTOR;

	// Technical Components
	public static ConveyorBelt LIGHT_CONVEYOR_BELT;
	public static ConveyorBelt MEDIUM_CONVEYOR_BELT;
	public static ConveyorBelt HEAVY_CONVEYOR_BELT;

	// Machines
	public static MachineCasing BASIC_MACHINE_CASING;
	public static MachineCasing ADVANCED_MACHINE_CASING;
	public static MachineCasing IMPROVED_MACHINE_CASING;
	public static MachineCasing PERFECTED_MACHINE_CASING;
	public static BlockGenerator COBBLESTONE_GENERATOR;
	public static BlockGenerator STONE_GENERATOR;
	public static BlockGenerator DRIPSTONE_GENERATOR;
	public static BlockGenerator BASALT_GENERATOR;
	public static LavaGenerator LAVA_GENERATOR;
	public static LavaGenerator BASIC_LAVA_GENERATOR;
	public static LavaGenerator ADVANCED_LAVA_GENERATOR;
	public static LavaGenerator IMPROVED_LAVA_GENERATOR;
	public static LavaGenerator PERFECTED_LAVA_GENERATOR;
	public static ObsidianGenerator OBSIDIAN_GENERATOR;
	public static ItemAbsorber ITEM_ABSORBER;
	public static ExperienceAbsorber EXPERIENCE_ABSORBER;
	public static Farmer BASIC_FARMER;
	public static Farmer ADVANCED_FARMER;
	public static Farmer IMPROVED_FARMER;
	public static Farmer PERFECTED_FARMER;
	public static MobGrinder MOB_GRINDER;
	public static BlockBreaker BLOCK_BREAKER;
	public static Quarry QUARRY;
	public static VoidOreMiner BASIC_VOID_ORE_MINER;
	public static VoidOreMiner ADVANCED_VOID_ORE_MINER;
	public static VoidOreMiner IMPROVED_VOID_ORE_MINER;
	public static VoidOreMiner PERFECTED_VOID_ORE_MINER;
	public static DyePress LIST;
	public static DyePress DYE_PRESS;
	public static Dyer DYE_MIXER;
	public static Enchanter ENCHANTER;
	public static Disenchanter DISENCHANTER;
	public static EnchantmentCombiner ENCHANTMENT_COMBINER;
	public static Computer COMPUTER;
	public static ChunkLoader CHUNK_LOADER;
	public static HyperFurnace HYPER_FURNACE_1;
	public static HyperFurnace HYPER_FURNACE_2;
	public static HyperFurnace HYPER_FURNACE_3;
	public static HyperFurnace HYPER_FURNACE_4;
	public static HyperFurnace HYPER_FURNACE_5;
	public static HyperFurnace HYPER_FURNACE_6;
	public static HyperFurnace HYPER_FURNACE_7;
	public static HyperFurnace HYPER_FURNACE_8;
	public static HyperFurnace HYPER_FURNACE_9;
	public static HyperFurnace HYPER_FURNACE_10;
	public static HyperFurnace HYPER_FURNACE_11;
	public static HyperFurnace HYPER_FURNACE_12;
	public static Crusher BASIC_CRUSHER;
	public static Crusher ADVANCED_CRUSHER;
	public static Crusher IMPROVED_CRUSHER;
	public static Crusher PERFECTED_CRUSHER;
	public static MineralExtractor BASIC_MINERAL_EXTRACTOR;
	public static MineralExtractor ADVANCED_MINERAL_EXTRACTOR;
	public static MineralExtractor IMPROVED_MINERAL_EXTRACTOR;
	public static MineralExtractor PERFECTED_MINERAL_EXTRACTOR;
	public static AerialExtractor BASIC_AERIAL_EXTRACTOR;
	public static AerialExtractor ADVANCED_AERIAL_EXTRACTOR;
	public static AerialExtractor IMPROVED_AERIAL_EXTRACTOR;
	public static AerialExtractor PERFECTED_AERIAL_EXTRACTOR;
	public static Sawmill BASIC_SAWMILL;
	public static Sawmill ADVANCED_SAWMILL;
	public static Sawmill IMPROVED_SAWMILL;
	public static Sawmill PERFECTED_SAWMILL;
	public static Smeltery BASIC_SMELTERY;
	public static Smeltery ADVANCED_SMELTERY;
	public static Smeltery IMPROVED_SMELTERY;
	public static Smeltery PERFECTED_SMELTERY;
	public static Compressor BASIC_COMPRESSOR;
	public static Compressor ADVANCED_COMPRESSOR;
	public static Compressor IMPROVED_COMPRESSOR;
	public static Compressor PERFECTED_COMPRESSOR;
	public static BioPress BASIC_BIO_PRESS;
	public static BioPress ADVANCED_BIO_PRESS;
	public static BioPress IMPROVED_BIO_PRESS;
	public static BioPress PERFECTED_BIO_PRESS;
	public static LatexExtractor LATEX_EXTRACTOR;
	public static Crafter BASIC_CRAFTER;
	public static Crafter ADVANCED_CRAFTER;
	public static Crafter IMPROVED_CRAFTER;
	public static Crafter PERFECTED_CRAFTER;
	public static CraftingFactory BASIC_CRAFTING_FACTORY;
	public static CraftingFactory ADVANCED_CRAFTING_FACTORY;
	public static CraftingFactory IMPROVED_CRAFTING_FACTORY;
	public static CraftingFactory PERFECTED_CRAFTING_FACTORY;
	public static CarbonPress BASIC_CARBON_PRESS;
	public static CarbonPress ADVANCED_CARBON_PRESS;
	public static CarbonPress IMPROVED_CARBON_PRESS;
	public static CarbonPress PERFECTED_CARBON_PRESS;
	public static Dryer BASIC_DRYER;
	public static Dryer ADVANCED_DRYER;
	public static Dryer IMPROVED_DRYER;
	public static Dryer PERFECTED_DRYER;
	public static Freezer BASIC_FREEZER;
	public static Freezer ADVANCED_FREEZER;
	public static Freezer IMPROVED_FREEZER;
	public static Freezer PERFECTED_FREEZER;
	public static HoneyExtractor HONEY_EXTRACTOR;
	public static StarMaker STAR_MAKER;
	public static RocketAssembler ROCKET_ASSEMBLER;

	// Generators
	public static FurnaceGenerator FURNACE_GENERATOR;
	public static ThermoGenerator THERMO_GENERATOR;
	public static SolarPanel BASIC_SOLAR_PANEL;
	public static SolarPanel ADVANCED_SOLAR_PANEL;
	public static SolarPanel IMPROVED_SOLAR_PANEL;
	public static SolarPanel PERFECTED_SOLAR_PANEL;
	public static FluidGenerator BASIC_BIO_GENERATOR;
	public static FluidGenerator ADVANCED_BIO_GENERATOR;
	public static FluidGenerator IMPROVED_BIO_GENERATOR;
	public static FluidGenerator PERFECTED_BIO_GENERATOR;
	public static CreativeGenerator CREATIVE_GENERATOR;

	// Storage
	public static Backpack BROWN_BACKPACK;
	public static Backpack COPPER_BACKPACK;
	public static Backpack IRON_BACKPACK;
	public static Backpack GOLDEN_BACKPACK;
	public static Backpack DIAMOND_BACKPACK;
	public static Backpack NETHERITE_BACKPACK;
	public static StorageCrateBlock OAK_STORAGE_CRATE;
	public static StorageCrateBlock SPRUCE_STORAGE_CRATE;
	public static StorageCrateBlock BIRCH_STORAGE_CRATE;
	public static StorageCrateBlock JUNGLE_STORAGE_CRATE;
	public static StorageCrateBlock ACACIA_STORAGE_CRATE;
	public static StorageCrateBlock DARK_OAK_STORAGE_CRATE;
	public static StorageCrateBlock MANGROVE_STORAGE_CRATE;
	public static StorageCrateBlock CHERRY_STORAGE_CRATE;
	public static StorageCrateBlock PALE_OAK_STORAGE_CRATE;
	public static StorageCrateBlock BAMBOO_STORAGE_CRATE;
	public static StorageCrateBlock CRIMSON_STORAGE_CRATE;
	public static StorageCrateBlock WARPED_STORAGE_CRATE;
	public static StorageCrateBlock IRON_STORAGE_CRATE;
	public static StorageCrateBlock GOLDEN_STORAGE_CRATE;
	public static StorageCrateBlock DIAMOND_STORAGE_CRATE;
	public static StorageCrateBlock EMERALD_STORAGE_CRATE;
	public static StorageCrateBlock CLOWNFISH_STORAGE_SKULL_BLOCK;
	// public static ClownfishChest CLOWNFISH_CHEST ;
	public static EnderChest ENDER_CHEST;
	public static Trashcan TRASHCAN;
	public static StorageConnector STORAGE_CONNECTOR;
	public static StorageImporter STORAGE_IMPORTER;
	public static StorageExporter STORAGE_EXPORTER;
	public static StorageReader STORAGE_READER;
	public static EnderAccessor ENDER_ACCESSOR;
	public static StorageMonitor STORAGE_MONITOR;

	// Fluids
	public static Tank BASIC_TANK;
	public static Tank ADVANCED_TANK;
	public static Tank IMPROVED_TANK;
	public static Tank PERFECTED_TANK;
	public static FluidPump FLUID_PUMP;
	public static EnderTank ENDER_TANK;
	public static WasteBarrel WASTE_BARREL;

	// Energy
	public static Battery BATTERY_RED;
	public static Battery BATTERY_YELLOW;
	public static Battery BATTERY_GREEN;
	public static Battery BATTERY_CYAN;
	public static Battery BATTERY_PURPLE;
	public static Battery BATTERY_BLACK;
	public static LED LED_PURPLE;
	public static LED LED_BLUE;
	public static LED LED_GREEN;
	public static LED LED_ORANGE;
	public static LED LED_RED;
	public static Tesseract TESSERACT;

	// Fun
	public static CardPile CARD_PILE;

	// Plushies
	public static CustomBlock FALSE_SYMMETRY_PLUSHIE;
	public static CustomBlock XISUMA_PLUSHIE;
	public static CustomBlock ZEDAPH_PLUSHIE;
	public static CustomBlock XB_CRAFTED_PLUSHIE;
	public static CustomBlock WELSKNIGHT_PLUSHIE;
	public static CustomBlock TIN_FOIL_CHEF_PLUSHIE;
	public static CustomBlock MUMBO_JUMBO_PLUSHIE;
	public static CustomBlock JOE_HILLS_SAYS_PLUSHIE;
	public static CustomBlock HYPNOTIZD_PLUSHIE;
	public static CustomBlock GRIAN_PLUSHIE;
	public static CustomBlock GUINEA_PIG_GRIAN_PLUSHIE;
	public static CustomBlock POULTRY_MAN_PLUSHIE;
	public static CustomBlock VINTAGE_BEEF_PLUSHIE;
	public static CustomBlock GOOD_TIMES_WITH_SCAR_PLUSHIE;
	public static CustomBlock JELLIE_PLUSHIE;
	public static CustomBlock KERALIS_PLUSHIE;
	public static CustomBlock FRENCHRALIS_PLUSHIE;
	public static CustomBlock I_JEVIN_PLUSHIE;
	public static CustomBlock ETHOSLAB_PLUSHIE;
	public static CustomBlock ISKALL85_PLUSHIE;
	public static CustomBlock TANGO_TEK_PLUSHIE;
	public static CustomBlock IMPULS_SV_PLUSHIE;
	public static CustomBlock STRESSMONSTER101_PLUSHIE;
	public static CustomBlock BDOUBLEO100_PLUSHIE;
	public static CustomBlock BDOUBLEO100_SMILE_PLUSHIE;
	public static CustomBlock DOCM77_PLUSHIE;
	public static CustomBlock CUBFAN135_PLUSHIE;
	public static CustomBlock DOCTOR_CUBFAN135_PLUSHIE;
	public static CustomBlock PHARAO_CUBFAN135_PLUSHIE;
	public static CustomBlock ZOMBIE_CLEO_PLUSHIE;
	public static CustomBlock REN_THE_DOG_PLUSHIE;
	public static CustomBlock REN_BOB_PLUSHIE;
	public static CustomBlock PEARLESCENT_MOON_PLUSHIE;
	public static CustomBlock GEMINI_TAY_PLUSHIE;
	public static CustomBlock JOEYGRACEFFA_PLUSHIE;
	public static CustomBlock SHUBBLE_YT_PLUSHIE;
	public static CustomBlock SOLIDARITY_GAMING_PLUSHIE;
	public static CustomBlock SMALISHBEANS_PLUSHIE;
	public static CustomBlock SMAJOR1995_PLUSHIE;
	public static CustomBlock PIXLRIFFS_PLUSHIE;
	public static CustomBlock MYTHICAL_SAUSAGE_PLUSHIE;
	public static CustomBlock LDS_SHADOWLADY_PLUSHIE;
	public static CustomBlock KATHERINEELIZ_PLUSHIE;
	public static CustomBlock FWHIP_PLUSHIE;

	public static void init() {
		// Miscellaneous
		ANGEL_BLOCK = BLOCKS.register(new CustomBlock("§6Angel Block", HeadTextures.ANGEL_BLOCK));

		// Resources
		COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("compressed_cobblestone", HeadTextures.COMPRESSED_COBBLESTONE));
		DOUBLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("double_compressed_cobblestone", HeadTextures.DOUBLE_COMPRESSED_COBBLESTONE));
		TRIPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("triple_compressed_cobblestone", HeadTextures.TRIPLE_COMPRESSED_COBBLESTONE));
		QUADRUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("quadruple_compressed_cobblestone", HeadTextures.QUADRUPLE_COMPRESSED_COBBLESTONE));
		QUINTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("quintuple_compressed_cobblestone", HeadTextures.QUINTUPLE_COMPRESSED_COBBLESTONE));
		SEXTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("sectuple_compressed_cobblestone", HeadTextures.SEXTUPLE_COMPRESSED_COBBLESTONE));
		SEPTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("septuple_compressed_cobblestone", HeadTextures.SEPTUPLE_COMPRESSED_COBBLESTONE));
		OCTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("octuple_compressed_cobblestone", HeadTextures.OCTUPLE_COMPRESSED_COBBLESTONE));

		// Redstone
		WIRELESS_REDSTONE_TRANSMITTER = BLOCKS.register(new WirelessRedstoneTransmitter());
		WIRELESS_REDSTONE_RECEIVER = BLOCKS.register(new WirelessRedstoneReceiver());
		REDSTONE_OR_GATE = BLOCKS.register(new DoubleInputRedstoneGate("or_gate", (a, b) -> a || b));
		REDSTONE_AND_GATE = BLOCKS.register(new DoubleInputRedstoneGate("and_gate", (a, b) -> a && b));
		REDSTONE_XOR_GATE = BLOCKS.register(new DoubleInputRedstoneGate("xor_gate", (a, b) -> a ^ b));

		// Electric Components
		ACCUMULATOR = BLOCKS.register(new Battery("accumulator", HeadTextures.ACCUMULATOR, 8, 100000, 1000));
		ITEM_ATTRACTOR = BLOCKS.register(new MagnetBlock("item_attractor", e -> e.getType() == EntityType.ITEM, 10.0D, 0.2D));
		MONSTER_ATTRACTOR = BLOCKS.register(new MagnetBlock("monster_attractor", e -> e instanceof Enemy, 10.0D, 0.2D));

		// Technical Components
		LIGHT_CONVEYOR_BELT = BLOCKS.register(new ConveyorBelt(ConveyorBelt.Type.LIGHT));
		MEDIUM_CONVEYOR_BELT = BLOCKS.register(new ConveyorBelt(ConveyorBelt.Type.MEDIUM));
		HEAVY_CONVEYOR_BELT = BLOCKS.register(new ConveyorBelt(ConveyorBelt.Type.HEAVY));

		// Machines
		BASIC_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.BASIC, HeadTextures.BASIC_MACHINE_CASING));
		ADVANCED_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.ADVANCED, HeadTextures.ADVANCED_MACHINE_CASING));
		IMPROVED_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.IMPROVED, HeadTextures.IMPROVED_MACHINE_CASING));
		PERFECTED_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.PERFECTED, HeadTextures.PERFECTED_MACHINE_CASING));
		COBBLESTONE_GENERATOR = BLOCKS.register(new BlockGenerator("cobblestone_generator", IItemSupplier.of(Material.COBBLESTONE), 10));
		STONE_GENERATOR = BLOCKS.register(new BlockGenerator("stone_generator", IItemSupplier.of(Material.STONE), 10));
		DRIPSTONE_GENERATOR = BLOCKS.register(new BlockGenerator("dripstone_generator", IItemSupplier.of(Material.POINTED_DRIPSTONE), 10));
		BASALT_GENERATOR = BLOCKS.register(new BlockGenerator("basalt_generator", IItemSupplier.of(Material.BASALT), 10));
		LAVA_GENERATOR = BLOCKS.register(new LavaGenerator(null));
		BASIC_LAVA_GENERATOR = BLOCKS.register(new LavaGenerator(MachineTier.BASIC));
		ADVANCED_LAVA_GENERATOR = BLOCKS.register(new LavaGenerator(MachineTier.ADVANCED));
		IMPROVED_LAVA_GENERATOR = BLOCKS.register(new LavaGenerator(MachineTier.IMPROVED));
		PERFECTED_LAVA_GENERATOR = BLOCKS.register(new LavaGenerator(MachineTier.PERFECTED));
		OBSIDIAN_GENERATOR = BLOCKS.register(new ObsidianGenerator());
		ITEM_ABSORBER = BLOCKS.register(new ItemAbsorber());
		EXPERIENCE_ABSORBER = BLOCKS.register(new ExperienceAbsorber());
		BASIC_FARMER = BLOCKS.register(new Farmer(MachineTier.BASIC));
		ADVANCED_FARMER = BLOCKS.register(new Farmer(MachineTier.ADVANCED));
		IMPROVED_FARMER = BLOCKS.register(new Farmer(MachineTier.IMPROVED));
		PERFECTED_FARMER = BLOCKS.register(new Farmer(MachineTier.PERFECTED));
		MOB_GRINDER = BLOCKS.register(new MobGrinder());
		BLOCK_BREAKER = BLOCKS.register(new BlockBreaker());
		QUARRY = BLOCKS.register(new Quarry());
		BASIC_VOID_ORE_MINER = BLOCKS.register(new VoidOreMiner(MachineTier.BASIC));
		ADVANCED_VOID_ORE_MINER = BLOCKS.register(new VoidOreMiner(MachineTier.ADVANCED));
		IMPROVED_VOID_ORE_MINER = BLOCKS.register(new VoidOreMiner(MachineTier.IMPROVED));
		PERFECTED_VOID_ORE_MINER = BLOCKS.register(new VoidOreMiner(MachineTier.PERFECTED));
		DYE_PRESS = BLOCKS.register(new DyePress());
		DYE_MIXER = BLOCKS.register(new Dyer());
		ENCHANTER = BLOCKS.register(new Enchanter());
		DISENCHANTER = BLOCKS.register(new Disenchanter());
		ENCHANTMENT_COMBINER = BLOCKS.register(new EnchantmentCombiner());
		COMPUTER = BLOCKS.register(new Computer());
		CHUNK_LOADER = BLOCKS.register(new ChunkLoader());
		HYPER_FURNACE_1 = BLOCKS.register(new HyperFurnace(MachineTier.BASIC, 1));
		HYPER_FURNACE_2 = BLOCKS.register(new HyperFurnace(MachineTier.BASIC, 2));
		HYPER_FURNACE_3 = BLOCKS.register(new HyperFurnace(MachineTier.BASIC, 3));
		HYPER_FURNACE_4 = BLOCKS.register(new HyperFurnace(MachineTier.ADVANCED, 4));
		HYPER_FURNACE_5 = BLOCKS.register(new HyperFurnace(MachineTier.ADVANCED, 5));
		HYPER_FURNACE_6 = BLOCKS.register(new HyperFurnace(MachineTier.ADVANCED, 6));
		HYPER_FURNACE_7 = BLOCKS.register(new HyperFurnace(MachineTier.IMPROVED, 7));
		HYPER_FURNACE_8 = BLOCKS.register(new HyperFurnace(MachineTier.IMPROVED, 8));
		HYPER_FURNACE_9 = BLOCKS.register(new HyperFurnace(MachineTier.IMPROVED, 9));
		HYPER_FURNACE_10 = BLOCKS.register(new HyperFurnace(MachineTier.PERFECTED, 10));
		HYPER_FURNACE_11 = BLOCKS.register(new HyperFurnace(MachineTier.PERFECTED, 11));
		HYPER_FURNACE_12 = BLOCKS.register(new HyperFurnace(MachineTier.PERFECTED, 12));
		BASIC_CRUSHER = BLOCKS.register(new Crusher(MachineTier.BASIC));
		ADVANCED_CRUSHER = BLOCKS.register(new Crusher(MachineTier.ADVANCED));
		IMPROVED_CRUSHER = BLOCKS.register(new Crusher(MachineTier.IMPROVED));
		PERFECTED_CRUSHER = BLOCKS.register(new Crusher(MachineTier.PERFECTED));
		BASIC_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractor(MachineTier.BASIC));
		ADVANCED_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractor(MachineTier.ADVANCED));
		IMPROVED_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractor(MachineTier.IMPROVED));
		PERFECTED_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractor(MachineTier.PERFECTED));
		BASIC_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractor(MachineTier.BASIC));
		ADVANCED_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractor(MachineTier.ADVANCED));
		IMPROVED_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractor(MachineTier.IMPROVED));
		PERFECTED_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractor(MachineTier.PERFECTED));
		BASIC_SAWMILL = BLOCKS.register(new Sawmill(MachineTier.BASIC));
		ADVANCED_SAWMILL = BLOCKS.register(new Sawmill(MachineTier.ADVANCED));
		IMPROVED_SAWMILL = BLOCKS.register(new Sawmill(MachineTier.IMPROVED));
		PERFECTED_SAWMILL = BLOCKS.register(new Sawmill(MachineTier.PERFECTED));
		BASIC_SMELTERY = BLOCKS.register(new Smeltery(MachineTier.BASIC));
		ADVANCED_SMELTERY = BLOCKS.register(new Smeltery(MachineTier.ADVANCED));
		IMPROVED_SMELTERY = BLOCKS.register(new Smeltery(MachineTier.IMPROVED));
		PERFECTED_SMELTERY = BLOCKS.register(new Smeltery(MachineTier.PERFECTED));
		BASIC_COMPRESSOR = BLOCKS.register(new Compressor(MachineTier.BASIC));
		ADVANCED_COMPRESSOR = BLOCKS.register(new Compressor(MachineTier.ADVANCED));
		IMPROVED_COMPRESSOR = BLOCKS.register(new Compressor(MachineTier.IMPROVED));
		PERFECTED_COMPRESSOR = BLOCKS.register(new Compressor(MachineTier.PERFECTED));
		BASIC_BIO_PRESS = BLOCKS.register(new BioPress(MachineTier.BASIC));
		ADVANCED_BIO_PRESS = BLOCKS.register(new BioPress(MachineTier.ADVANCED));
		IMPROVED_BIO_PRESS = BLOCKS.register(new BioPress(MachineTier.IMPROVED));
		PERFECTED_BIO_PRESS = BLOCKS.register(new BioPress(MachineTier.PERFECTED));
		LATEX_EXTRACTOR = BLOCKS.register(new LatexExtractor());
		BASIC_CRAFTER = BLOCKS.register(new Crafter(MachineTier.BASIC));
		ADVANCED_CRAFTER = BLOCKS.register(new Crafter(MachineTier.ADVANCED));
		IMPROVED_CRAFTER = BLOCKS.register(new Crafter(MachineTier.IMPROVED));
		PERFECTED_CRAFTER = BLOCKS.register(new Crafter(MachineTier.PERFECTED));
		BASIC_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactory(MachineTier.BASIC));
		ADVANCED_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactory(MachineTier.ADVANCED));
		IMPROVED_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactory(MachineTier.IMPROVED));
		PERFECTED_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactory(MachineTier.PERFECTED));
		BASIC_CARBON_PRESS = BLOCKS.register(new CarbonPress(MachineTier.BASIC));
		ADVANCED_CARBON_PRESS = BLOCKS.register(new CarbonPress(MachineTier.ADVANCED));
		IMPROVED_CARBON_PRESS = BLOCKS.register(new CarbonPress(MachineTier.IMPROVED));
		PERFECTED_CARBON_PRESS = BLOCKS.register(new CarbonPress(MachineTier.PERFECTED));
		BASIC_DRYER = BLOCKS.register(new Dryer(MachineTier.BASIC));
		ADVANCED_DRYER = BLOCKS.register(new Dryer(MachineTier.ADVANCED));
		IMPROVED_DRYER = BLOCKS.register(new Dryer(MachineTier.IMPROVED));
		PERFECTED_DRYER = BLOCKS.register(new Dryer(MachineTier.PERFECTED));
		BASIC_FREEZER = BLOCKS.register(new Freezer(MachineTier.BASIC));
		ADVANCED_FREEZER = BLOCKS.register(new Freezer(MachineTier.ADVANCED));
		IMPROVED_FREEZER = BLOCKS.register(new Freezer(MachineTier.IMPROVED));
		PERFECTED_FREEZER = BLOCKS.register(new Freezer(MachineTier.PERFECTED));
		HONEY_EXTRACTOR = BLOCKS.register(new HoneyExtractor());
		STAR_MAKER = BLOCKS.register(new StarMaker());
		ROCKET_ASSEMBLER = BLOCKS.register(new RocketAssembler());

		// Generators
		FURNACE_GENERATOR = BLOCKS.register(new FurnaceGenerator());
		THERMO_GENERATOR = BLOCKS.register(new ThermoGenerator());
		BASIC_SOLAR_PANEL = BLOCKS.register(new SolarPanel(HeadTextures.SOLAR_PANEL_1, MachineTier.BASIC));
		ADVANCED_SOLAR_PANEL = BLOCKS.register(new SolarPanel(HeadTextures.SOLAR_PANEL_2, MachineTier.ADVANCED));
		IMPROVED_SOLAR_PANEL = BLOCKS.register(new SolarPanel(HeadTextures.SOLAR_PANEL_3, MachineTier.IMPROVED));
		PERFECTED_SOLAR_PANEL = BLOCKS.register(new SolarPanel(HeadTextures.SOLAR_PANEL_4, MachineTier.PERFECTED));
		BASIC_BIO_GENERATOR = BLOCKS.register(new FluidGenerator("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.BASIC, FluidType.BIO_MASS));
		ADVANCED_BIO_GENERATOR = BLOCKS.register(new FluidGenerator("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.ADVANCED, FluidType.BIO_MASS));
		IMPROVED_BIO_GENERATOR = BLOCKS.register(new FluidGenerator("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.IMPROVED, FluidType.BIO_MASS));
		PERFECTED_BIO_GENERATOR = BLOCKS.register(new FluidGenerator("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.PERFECTED, FluidType.BIO_MASS));
		CREATIVE_GENERATOR = BLOCKS.register(new CreativeGenerator());

		// Storage
		BROWN_BACKPACK = BLOCKS.register(new Backpack("brown_backpack", "§fBrown Backpack", HeadTextures.BROWN_BACKPACK, 9));
		COPPER_BACKPACK = BLOCKS.register(new Backpack("copper_backpack", "§cCopper Backpack", HeadTextures.COPPER_BACKPACK, 18));
		IRON_BACKPACK = BLOCKS.register(new Backpack("iron_backpack", "§7Iron Backpack", HeadTextures.IRON_BACKPACK, 27));
		GOLDEN_BACKPACK = BLOCKS.register(new Backpack("golden_backpack", "§eGolden Backpack", HeadTextures.GOLDEN_BACKPACK, 36));
		DIAMOND_BACKPACK = BLOCKS.register(new Backpack("diamond_backpack", "§bDiamond Backpack", HeadTextures.DIAMOND_BACKPACK, 45));
		NETHERITE_BACKPACK = BLOCKS.register(new Backpack("netherite_backpack", "§dNetherite Backpack", HeadTextures.NETHERITE_BACKPACK, 54));
		OAK_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("oak_storage_crate", HeadTextures.OAK_STORAGE_CRATE, 1));
		SPRUCE_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("spruce_storage_crate", HeadTextures.SPRUCE_STORAGE_CRATE, 1));
		BIRCH_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("birch_storage_crate", HeadTextures.BIRCH_STORAGE_CRATE, 1));
		JUNGLE_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("jungle_storage_crate", HeadTextures.JUNGLE_STORAGE_CRATE, 1));
		ACACIA_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("acacia_storage_crate", HeadTextures.ACACIA_STORAGE_CRATE, 1));
		DARK_OAK_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("dark_oak_storage_crate", HeadTextures.DARK_OAK_STORAGE_CRATE, 1));
		MANGROVE_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("mangrove_storage_crate", HeadTextures.MANGROVE_STORAGE_CRATE, 1));
		CHERRY_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("cherry_storage_crate", HeadTextures.CHERRY_STORAGE_CRATE, 1));
		PALE_OAK_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("pale_oak_storage_crate", HeadTextures.PALE_OAK_STORAGE_CRATE, 1));
		BAMBOO_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("bamboo_storage_crate", HeadTextures.BAMBOO_STORAGE_CRATE, 1));
		CRIMSON_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("crimson_storage_crate", HeadTextures.CRIMSON_STORAGE_CRATE, 1));
		WARPED_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("warped_storage_crate", HeadTextures.WARPED_STORAGE_CRATE, 1));
		IRON_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("iron_storage_crate", HeadTextures.IRON_STORAGE_CRATE, 7));
		GOLDEN_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("golden_storage_crate", HeadTextures.GOLDEN_STORAGE_CRATE, 14));
		DIAMOND_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("diamond_storage_crate", HeadTextures.DIAMOND_STORAGE_CRATE, 28));
		EMERALD_STORAGE_CRATE = BLOCKS.register(new StorageCrateBlock("emerald_storage_crate", HeadTextures.EMERALD_STORAGE_CRATE, 56));
		CLOWNFISH_STORAGE_SKULL_BLOCK = BLOCKS.register(new StorageCrateBlock("clownfish_storage_crate", HeadTextures.BLUE_STORAGE_CRATE, 112));
		// CLOWNFISH_CHEST = BLOCKS.register(new ClownfishChest());
		ENDER_CHEST = BLOCKS.register(new EnderChest());
		TRASHCAN = BLOCKS.register(new Trashcan());
		STORAGE_CONNECTOR = BLOCKS.register(new StorageConnector());
		STORAGE_IMPORTER = BLOCKS.register(new StorageImporter());
		STORAGE_EXPORTER = BLOCKS.register(new StorageExporter());
		STORAGE_READER = BLOCKS.register(new StorageReader());
		ENDER_ACCESSOR = BLOCKS.register(new EnderAccessor());
		STORAGE_MONITOR = BLOCKS.register(new StorageMonitor());

		// Fluids
		BASIC_TANK = BLOCKS.register(new Tank(MachineTier.BASIC));
		ADVANCED_TANK = BLOCKS.register(new Tank(MachineTier.ADVANCED));
		IMPROVED_TANK = BLOCKS.register(new Tank(MachineTier.IMPROVED));
		PERFECTED_TANK = BLOCKS.register(new Tank(MachineTier.PERFECTED));
		FLUID_PUMP = BLOCKS.register(new FluidPump());
		ENDER_TANK = BLOCKS.register(new EnderTank());
		WASTE_BARREL = BLOCKS.register(new WasteBarrel());

		// Energy
		BATTERY_RED = BLOCKS.register(new Battery("battery_red", HeadTextures.BATTERY_RED, 3, 1000, 10));
		BATTERY_YELLOW = BLOCKS.register(new Battery("battery_yellow", HeadTextures.BATTERY_YELLOW, 5, 4000, 40));
		BATTERY_GREEN = BLOCKS.register(new Battery("battery_green", HeadTextures.BATTERY_GREEN, 8, 16000, 160));
		BATTERY_CYAN = BLOCKS.register(new Battery("battery_cyan", HeadTextures.BATTERY_CYAN, 13, 64000, 640));
		BATTERY_PURPLE = BLOCKS.register(new Battery("battery_purple", HeadTextures.BATTERY_PURPLE, 21, 256000, 2560));
		BATTERY_BLACK = BLOCKS.register(new Battery("battery_black", HeadTextures.BATTERY_BLACK, 34, 1024000, 10240));
		LED_PURPLE = BLOCKS.register(new LED("led_purple", HeadTextures.LED_PURPLE));
		LED_BLUE = BLOCKS.register(new LED("led_blue", HeadTextures.LED_BLUE));
		LED_GREEN = BLOCKS.register(new LED("led_green", HeadTextures.LED_GREEN));
		LED_ORANGE = BLOCKS.register(new LED("led_orange", HeadTextures.LED_ORANGE));
		LED_RED = BLOCKS.register(new LED("led_red", HeadTextures.LED_RED));
		TESSERACT = BLOCKS.register(new Tesseract());

		// Fun
		CARD_PILE = BLOCKS.register(new CardPile());

		// Plushies
		FALSE_SYMMETRY_PLUSHIE = BLOCKS.register(new CustomBlock("false_symmetry_plushie", HeadTextures.FALSE_SYMMETRY));
		XISUMA_PLUSHIE = BLOCKS.register(new CustomBlock("xisuma_plushie", HeadTextures.XISUMA));
		ZEDAPH_PLUSHIE = BLOCKS.register(new CustomBlock("zedaph_plushie", HeadTextures.ZEDAPH));
		XB_CRAFTED_PLUSHIE = BLOCKS.register(new CustomBlock("xb_crafted_plushie", HeadTextures.XB_CRAFTED));
		WELSKNIGHT_PLUSHIE = BLOCKS.register(new CustomBlock("welsknight_plushie", HeadTextures.WELSKNIGHT));
		TIN_FOIL_CHEF_PLUSHIE = BLOCKS.register(new CustomBlock("tin_foil_chef_plushie", HeadTextures.TIN_FOIL_CHEF));
		MUMBO_JUMBO_PLUSHIE = BLOCKS.register(new CustomBlock("mumbo_jumbo_plushie", HeadTextures.MUMBO_JUMBO));
		JOE_HILLS_SAYS_PLUSHIE = BLOCKS.register(new CustomBlock("joe_hills_says_plushie", HeadTextures.JOE_HILLS_SAYS));
		HYPNOTIZD_PLUSHIE = BLOCKS.register(new CustomBlock("hypnotizd_plushie", HeadTextures.HYPNOTIZD));
		GRIAN_PLUSHIE = BLOCKS.register(new CustomBlock("grian_plushie", HeadTextures.GRIAN));
		GUINEA_PIG_GRIAN_PLUSHIE = BLOCKS.register(new CustomBlock("guinea_pig_grian_plushie", HeadTextures.GRIAN_GUINEA_PIG));
		POULTRY_MAN_PLUSHIE = BLOCKS.register(new CustomBlock("poultry_man_plushie", HeadTextures.POULTRY_MAN));
		VINTAGE_BEEF_PLUSHIE = BLOCKS.register(new CustomBlock("vintage_beef_plushie", HeadTextures.VINTAGE_BEEF));
		GOOD_TIMES_WITH_SCAR_PLUSHIE = BLOCKS.register(new CustomBlock("good_times_with_scar_plushie", HeadTextures.GOOD_TIMES_WITH_SCAR));
		JELLIE_PLUSHIE = BLOCKS.register(new CustomBlock("jellie_plushie", HeadTextures.JELLIE));
		KERALIS_PLUSHIE = BLOCKS.register(new CustomBlock("keralis_plushie", HeadTextures.KERALIS));
		FRENCHRALIS_PLUSHIE = BLOCKS.register(new CustomBlock("frenchralis_plushie", HeadTextures.FRENCHRALIS));
		I_JEVIN_PLUSHIE = BLOCKS.register(new CustomBlock("ijevin_plushie", HeadTextures.I_JEVIN));
		ETHOSLAB_PLUSHIE = BLOCKS.register(new CustomBlock("ethoslab_plushie", HeadTextures.ETHOSLAB));
		ISKALL85_PLUSHIE = BLOCKS.register(new CustomBlock("iskall85_plushie", HeadTextures.ISKALL85));
		TANGO_TEK_PLUSHIE = BLOCKS.register(new CustomBlock("tango_tek_plushie", HeadTextures.TANGO_TEK));
		IMPULS_SV_PLUSHIE = BLOCKS.register(new CustomBlock("impulse_sv_plushie", HeadTextures.IMPULSE_SV));
		STRESSMONSTER101_PLUSHIE = BLOCKS.register(new CustomBlock("stressmonster101_plushie", HeadTextures.STRESSMONSTER101));
		BDOUBLEO100_PLUSHIE = BLOCKS.register(new CustomBlock("bdoubleo100_plushie", HeadTextures.BDOUBLEO100));
		BDOUBLEO100_SMILE_PLUSHIE = BLOCKS.register(new CustomBlock("bdoubleo100_smile_plushie", HeadTextures.BDOUBLEO100_SMILE));
		DOCM77_PLUSHIE = BLOCKS.register(new CustomBlock("docm77_plushie", HeadTextures.DOCM77));
		CUBFAN135_PLUSHIE = BLOCKS.register(new CustomBlock("cubfan135_plushie", HeadTextures.CUBFAN135));
		DOCTOR_CUBFAN135_PLUSHIE = BLOCKS.register(new CustomBlock("doctor_cubfan135_plushie", HeadTextures.DOCTOR_CUBFAN135));
		PHARAO_CUBFAN135_PLUSHIE = BLOCKS.register(new CustomBlock("pharao_cubfan135_plushie", HeadTextures.PHARAO_CUBFAN135));
		ZOMBIE_CLEO_PLUSHIE = BLOCKS.register(new CustomBlock("zombie_cleo_plushie", HeadTextures.ZOMBIE_CLEO));
		REN_THE_DOG_PLUSHIE = BLOCKS.register(new CustomBlock("ren_the_dog_plushie", HeadTextures.REN_THE_DOG));
		REN_BOB_PLUSHIE = BLOCKS.register(new CustomBlock("ren_bob_plushie", HeadTextures.REN_BOB));
		PEARLESCENT_MOON_PLUSHIE = BLOCKS.register(new CustomBlock("pearlescent_moon_plushie", HeadTextures.PEARLESCENT_MOON));
		GEMINI_TAY_PLUSHIE = BLOCKS.register(new CustomBlock("gemini_tay_plushie", HeadTextures.GEMINI_TAY));
		JOEYGRACEFFA_PLUSHIE = BLOCKS.register(new CustomBlock("joeygraceffa_plushie", HeadTextures.JOEYGRACEFFA));
		SHUBBLE_YT_PLUSHIE = BLOCKS.register(new CustomBlock("shubble_yt_plushie", HeadTextures.SHUBBLE_YT));
		SOLIDARITY_GAMING_PLUSHIE = BLOCKS.register(new CustomBlock("solidarity_gaming_plushie", HeadTextures.SOLIDARITY_GAMING));
		SMALISHBEANS_PLUSHIE = BLOCKS.register(new CustomBlock("smalishbeans_plushie", HeadTextures.SMALISHBEANS));
		SMAJOR1995_PLUSHIE = BLOCKS.register(new CustomBlock("smajor1995_plushis", HeadTextures.SMAJOR1995));
		PIXLRIFFS_PLUSHIE = BLOCKS.register(new CustomBlock("pixlriffs_plushie", HeadTextures.PIXLRIFFS));
		MYTHICAL_SAUSAGE_PLUSHIE = BLOCKS.register(new CustomBlock("mythical_sausage_plushie", HeadTextures.MYTHICAL_SAUSAGE));
		LDS_SHADOWLADY_PLUSHIE = BLOCKS.register(new CustomBlock("lds_shadowlady", HeadTextures.LDS_SHADOWLADY));
		KATHERINEELIZ_PLUSHIE = BLOCKS.register(new CustomBlock("katherineeliz_plushie", HeadTextures.KATHERINEELIZ));
		FWHIP_PLUSHIE = BLOCKS.register(new CustomBlock("fwhip_plushie", HeadTextures.FWHIP));
	}


	@SuppressWarnings("unchecked")
	public static <B> B getCastedBlockFromBlock(TileState block) {
		try {
			return (B) getCustomBlockFromBlock(block);
		} catch(ClassCastException e) {
			return null;
		}
	}


	public static AbstractCustomBlock getCustomBlockFromBlock(TileState block) {
		if(!BlockUtils.isCustomBlock(block))
			return null;

		return getCustomBlockFromHolder(block);
	}


	public static AbstractCustomBlock getCustomBlockFromHolder(PersistentDataHolder holder) {
		String identifier = Properties.IDENTIFIER.fetch(holder);
		return BLOCKS.filterFirst(custom -> custom.getIdentifier().equals(identifier));
	}

	private static final CachingSupplier<List<BlockInventoryProvider>> GUI_PROVIDER_SUPPLIER = new CachingSupplier<>(() -> BLOCKS.filterByClass(BlockInventoryProvider.class));

	public static List<BlockInventoryProvider> getGuiProviders() {
		return GUI_PROVIDER_SUPPLIER.get();
	}


	public static Stream<Listener> getCustomListeners() {
		return BLOCKS.stream().filter(AbstractCustomBlock::hasListener).map(AbstractCustomBlock::getListener);
	}


	public static AbstractMachine getMachineFromHolder(PersistentDataHolder holder) {
		return (AbstractMachine) BLOCKS.filterFirst(block -> block.isInstanceOf(holder));
	}


	public static AbstractCraftingMachine getCraftingMachineFromHolder(PersistentDataHolder holder) {
		if(!AbstractCraftingMachine.isCraftingMachine(holder))
			return null;

		return (AbstractCraftingMachine) BLOCKS.filterFirst(block -> block.isInstanceOf(holder));
	}


	public static Upgradeable getUpgradeableFromBlock(TileState block) {
		return getCustomBlockFromBlock(block) instanceof Upgradeable upgradeable ? upgradeable : null;
	}

}
