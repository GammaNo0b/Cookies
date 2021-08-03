
package me.gamma.cookies.setup;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.BlockRegister;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.objects.block.HyperFurnace;
import me.gamma.cookies.objects.block.machine.Machine;
import me.gamma.cookies.objects.block.machine.MachineTier;
import me.gamma.cookies.objects.block.skull.AngelBlock;
import me.gamma.cookies.objects.block.skull.Baseball;
import me.gamma.cookies.objects.block.skull.Basketball;
import me.gamma.cookies.objects.block.skull.CompressedCobblestone;
import me.gamma.cookies.objects.block.skull.Cube;
import me.gamma.cookies.objects.block.skull.CustomSkullBlock;
import me.gamma.cookies.objects.block.skull.Golfball;
import me.gamma.cookies.objects.block.skull.HermitPlushie;
import me.gamma.cookies.objects.block.skull.IllusionCube;
import me.gamma.cookies.objects.block.skull.MonsterAttractor;
import me.gamma.cookies.objects.block.skull.Pebble;
import me.gamma.cookies.objects.block.skull.RainbowCube;
import me.gamma.cookies.objects.block.skull.RedstoneAndGate;
import me.gamma.cookies.objects.block.skull.RedstoneOrGate;
import me.gamma.cookies.objects.block.skull.RedstoneXorGate;
import me.gamma.cookies.objects.block.skull.Soccerball;
import me.gamma.cookies.objects.block.skull.WirelessRedstoneReceiver;
import me.gamma.cookies.objects.block.skull.WirelessRedstoneTransmitter;
import me.gamma.cookies.objects.block.skull.machine.Accumulator;
import me.gamma.cookies.objects.block.skull.machine.AndroidCore;
import me.gamma.cookies.objects.block.skull.machine.BasaltGenerator;
import me.gamma.cookies.objects.block.skull.machine.BlockBreaker;
import me.gamma.cookies.objects.block.skull.machine.CarbonPress;
import me.gamma.cookies.objects.block.skull.machine.ChunkLoader;
import me.gamma.cookies.objects.block.skull.machine.CobblestoneGenerator;
import me.gamma.cookies.objects.block.skull.machine.CompactLavaGenerator;
import me.gamma.cookies.objects.block.skull.machine.Compressor;
import me.gamma.cookies.objects.block.skull.machine.Computer;
import me.gamma.cookies.objects.block.skull.machine.CopperCoil;
import me.gamma.cookies.objects.block.skull.machine.Crusher;
import me.gamma.cookies.objects.block.skull.machine.Disenchanter;
import me.gamma.cookies.objects.block.skull.machine.DyeFabricator;
import me.gamma.cookies.objects.block.skull.machine.ElectricalCircuit;
import me.gamma.cookies.objects.block.skull.machine.Electromagnet;
import me.gamma.cookies.objects.block.skull.machine.Enchanter;
import me.gamma.cookies.objects.block.skull.machine.EnchantmentCombiner;
import me.gamma.cookies.objects.block.skull.machine.ExperienceAbsorber;
import me.gamma.cookies.objects.block.skull.machine.Farmer;
import me.gamma.cookies.objects.block.skull.machine.Freezer;
import me.gamma.cookies.objects.block.skull.machine.Gammabot;
import me.gamma.cookies.objects.block.skull.machine.ItemAbsorber;
import me.gamma.cookies.objects.block.skull.machine.LED;
import me.gamma.cookies.objects.block.skull.machine.LavaGenerator;
import me.gamma.cookies.objects.block.skull.machine.MachineCasing;
import me.gamma.cookies.objects.block.skull.machine.MineralExtractor;
import me.gamma.cookies.objects.block.skull.machine.MobGrinder;
import me.gamma.cookies.objects.block.skull.machine.Motor;
import me.gamma.cookies.objects.block.skull.machine.ObsidianGenerator;
import me.gamma.cookies.objects.block.skull.machine.Quarry;
import me.gamma.cookies.objects.block.skull.machine.Smeltery;
import me.gamma.cookies.objects.block.skull.machine.StoneGenerator;
import me.gamma.cookies.objects.block.skull.machine.Toaster;
import me.gamma.cookies.objects.block.skull.storage.ClownfishChest;
import me.gamma.cookies.objects.block.skull.storage.EnderAccessor;
import me.gamma.cookies.objects.block.skull.storage.StorageConnector;
import me.gamma.cookies.objects.block.skull.storage.StorageExporter;
import me.gamma.cookies.objects.block.skull.storage.StorageImporter;
import me.gamma.cookies.objects.block.skull.storage.StorageMonitor;
import me.gamma.cookies.objects.block.skull.storage.StorageSkullBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.util.ItemBuilder;



public class CustomBlockSetup {

	public static final List<AbstractTileStateBlock> customBlocks = new ArrayList<>();
	public static final List<GuiProvider> guiBlocks = new ArrayList<>();
	public static final List<BlockRegister> blockRegisters = new ArrayList<>();
	public static final List<BlockTicker> blockTickers = new ArrayList<>();

	// Miscellaneous
	public static final AngelBlock ANGEL_BLOCK = registerCustomBlock(new AngelBlock());

	// Fun
	public static final RainbowCube RAINBOW_CUBE = registerCustomBlock(new RainbowCube());
	public static final IllusionCube ILLUSION_CUBE = registerCustomBlock(new IllusionCube());
	public static final Soccerball SOCCERBALL = registerCustomBlock(new Soccerball());
	public static final Basketball BASKETBALL = registerCustomBlock(new Basketball());
	public static final Golfball GOLFBALL = registerCustomBlock(new Golfball());
	public static final Baseball BASEBALL = registerCustomBlock(new Baseball());

	// Resources
	public static final Pebble STONE_PEBBLE = registerCustomBlock(new Pebble("stone_pebble", "§7Stone Pebble", HeadTextures.STONE_PEBBLE));
	public static final Pebble GRANITE_PEBBLE = registerCustomBlock(new Pebble("granite_pebble", "§cGranite Pebble", HeadTextures.GRANITE_PEBBLE));
	public static final Pebble DIORITE_PEBBLE = registerCustomBlock(new Pebble("diorite_pebble", "§fDiorite Pebble", HeadTextures.DIORITE_PEBBLE));
	public static final Pebble ANDESITE_PEBBLE = registerCustomBlock(new Pebble("andesite_pebble", "§7Andesite Pebble", HeadTextures.ANDESITE_PEBBLE));
	public static final CompressedCobblestone COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("compressed_cobblestone", "§8Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.COMPRESSED_COBBLESTONE, 1, new ItemStack(Material.COBBLESTONE)));
	public static final CompressedCobblestone DOUBLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("double_compressed_cobblestone", "§8Double Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.DOUBLE_COMPRESSED_COBBLESTONE, 2, COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CompressedCobblestone TRIPLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("triple_compressed_cobblestone", "§8Triple Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.TRIPLE_COMPRESSED_COBBLESTONE, 3, DOUBLE_COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CompressedCobblestone QUADRUPLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("quadruple_compressed_cobblestone", "§8Quadruple Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.QUADRUPLE_COMPRESSED_COBBLESTONE, 4, TRIPLE_COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CompressedCobblestone QUINTUPLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("quintuple_compressed_cobblestone", "§8Quintuple Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.QUINTUPLE_COMPRESSED_COBBLESTONE, 5, QUADRUPLE_COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CompressedCobblestone SEXTUPLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("sextuple_compressed_cobblestone", "§8Sextuple Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.SEXTUPLE_COMPRESSED_COBBLESTONE, 6, QUINTUPLE_COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CompressedCobblestone SEPTUPLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("septuple_compressed_cobblestone", "§8Septuple Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.SEPTUPLE_COMPRESSED_COBBLESTONE, 7, SEXTUPLE_COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CompressedCobblestone OCTUPLE_COMPRESSED_COBBLESTONE = registerCustomBlock(new CompressedCobblestone("octuple_compressed_cobblestone", "§8Octuple Compressed Cobblestone", () -> RecipeCategory.RESOURCES, HeadTextures.OCTUPLE_COMPRESSED_COBBLESTONE, 8, SEPTUPLE_COMPRESSED_COBBLESTONE.createDefaultItemStack()));
	public static final CustomSkullBlock CARBON = registerCustomBlock(new CustomSkullBlock("carbon", "§8Carbon", () -> RecipeCategory.RESOURCES, HeadTextures.CARBON));
	public static final CustomSkullBlock COMPRESSED_CARBON = registerCustomBlock(new CustomSkullBlock("compressed_carbon", "§8Compressed Carbon", () -> RecipeCategory.RESOURCES, HeadTextures.COMPRESSED_CARBON));
	public static final CustomSkullBlock CARBON_CHUNK = registerCustomBlock(new CustomSkullBlock("carbon_chunk", "§8Carbon Chunk", () -> RecipeCategory.RESOURCES, HeadTextures.CARBON_CHUNK));
	public static final CustomSkullBlock CARBONADO = registerCustomBlock(new CustomSkullBlock("carbonado", "§8Carbonado", () -> RecipeCategory.RESOURCES, HeadTextures.CARBONADO));
	public static final CustomSkullBlock CRUSHED_NETHERRACK = registerCustomBlock(new CustomSkullBlock("crushed_netherrack", "§4Crushed Netherrack", () -> RecipeCategory.RESOURCES, HeadTextures.CRUSHED_NETHERRACK));
	public static final CustomSkullBlock CRUSHED_END_STONE = registerCustomBlock(new CustomSkullBlock("crushed_end_stone", "§eCrushed End Stone", () -> RecipeCategory.RESOURCES, HeadTextures.CRUSHED_END_STONE));
	public static final CustomSkullBlock CRUSHED_OBSIDIAN = registerCustomBlock(new CustomSkullBlock("crushed_obsidian", "§8Crushed Obsidian", () -> RecipeCategory.RESOURCES, HeadTextures.CRUSHED_OBSIDIAN));
	public static final CustomSkullBlock CRUSHED_CRYING_OBSIDIAN = registerCustomBlock(new CustomSkullBlock("crushed_crying_obsidian", "§5Crushed Crying Obsidian", () -> RecipeCategory.RESOURCES, HeadTextures.CRUSHED_CRYING_OBSIDIAN));
	public static final Cube CUBE = registerCustomBlock(new Cube());

	// Storage
	public static final ClownfishChest CLOWNFISH_CHEST = registerCustomBlock(new ClownfishChest());
	public static final StorageSkullBlock STORAGE_SKULL_BLOCK_TIER_1 = registerCustomBlock(new StorageSkullBlock(HeadTextures.BROWN_STORAGE_CRATE, 1, new ItemStack(Material.DARK_OAK_PLANKS), new ItemStack(Material.DARK_OAK_LOG), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.CHEST)));
	public static final StorageSkullBlock STORAGE_SKULL_BLOCK_TIER_2 = registerCustomBlock(new StorageSkullBlock(HeadTextures.SILVER_STORAGE_CRATE, 2, new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_NUGGET), new ItemStack(Material.GOLD_INGOT), STORAGE_SKULL_BLOCK_TIER_1.createDefaultItemStack()));
	public static final StorageSkullBlock STORAGE_SKULL_BLOCK_TIER_3 = registerCustomBlock(new StorageSkullBlock(HeadTextures.GOLDEN_STORAGE_CRATE, 3, new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.GOLD_NUGGET), new ItemStack(Material.DIAMOND), STORAGE_SKULL_BLOCK_TIER_2.createDefaultItemStack()));
	public static final StorageSkullBlock STORAGE_SKULL_BLOCK_TIER_4 = registerCustomBlock(new StorageSkullBlock(HeadTextures.LIGHT_BLUE_STORAGE_CRATE, 4, new ItemStack(Material.DIAMOND), new ItemStack(Material.EMERALD), new ItemStack(Material.NETHER_STAR), STORAGE_SKULL_BLOCK_TIER_3.createDefaultItemStack()));
	public static final StorageSkullBlock CLOWNFISH_STORAGE_SKULL_BLOCK = registerCustomBlock(new StorageSkullBlock(HeadTextures.BLUE_STORAGE_CRATE, 5, new ItemStack(Material.DIAMOND), new ItemStack(Material.LAPIS_BLOCK), new ItemBuilder(Material.TROPICAL_FISH).setName("Clownfish").build(), STORAGE_SKULL_BLOCK_TIER_4.createDefaultItemStack()));
	public static final StorageConnector STORAGE_CONNECTOR = registerCustomBlock(new StorageConnector());
	public static final StorageMonitor STORAGE_MONITOR = registerCustomBlock(new StorageMonitor());
	public static final StorageImporter STORAGE_IMPORTER = registerCustomBlock(new StorageImporter());
	public static final StorageExporter STORAGE_EXPORTER = registerCustomBlock(new StorageExporter());
	public static final EnderAccessor ENDER_ACCESSOR = registerCustomBlock(new EnderAccessor());

	// Electric Components
	public static final CopperCoil COPPER_COIL = registerCustomBlock(new CopperCoil());
	public static final Electromagnet ELECTROMAGNET = registerCustomBlock(new Electromagnet());
	public static final Motor MOTOR = registerCustomBlock(new Motor());
	public static final Accumulator ACCUMULATOR = registerCustomBlock(new Accumulator());
	public static final LED LED_BLUE = registerCustomBlock(new LED(HeadTextures.LED_BLUE, "led_blue", "§9LED", Material.BLUE_STAINED_GLASS_PANE));
	public static final LED LED_GREEN = registerCustomBlock(new LED(HeadTextures.LED_GREEN, "led_green", "§aLED", Material.LIME_STAINED_GLASS_PANE));
	public static final LED LED_ORANGE = registerCustomBlock(new LED(HeadTextures.LED_ORANGE, "led_orange", "§6LED", Material.ORANGE_STAINED_GLASS_PANE));
	public static final LED LED_RED = registerCustomBlock(new LED(HeadTextures.LED_RED, "led_red", "§cLED", Material.RED_STAINED_GLASS_PANE));
	public static final ElectricalCircuit ELECTRICAL_CIRCUIT = registerCustomBlock(new ElectricalCircuit());
	public static final AndroidCore ANDROID_CORE = registerCustomBlock(new AndroidCore());

	// Machines
	public static final MachineCasing MACHINE_CASING = registerCustomBlock(new MachineCasing(MachineTier.BASIC));
	public static final MachineCasing ADVANCED_MACHINE_CASING = registerCustomBlock(new MachineCasing(MachineTier.ADVANCED));
	public static final MachineCasing IMPROVED_MACHINE_CASING = registerCustomBlock(new MachineCasing(MachineTier.IMPROVED));
	public static final MachineCasing PERFECTED_MACHINE_CASING = registerCustomBlock(new MachineCasing(MachineTier.PERFECTED));
	public static final CobblestoneGenerator COBBLESTONE_GENERATOR = registerCustomBlock(new CobblestoneGenerator());
	public static final StoneGenerator STONE_GENERATOR = registerCustomBlock(new StoneGenerator());
	public static final BasaltGenerator BASALT_GENERATOR = registerCustomBlock(new BasaltGenerator());
	public static final LavaGenerator LAVA_GENERATOR = registerCustomBlock(new LavaGenerator());
	public static final ObsidianGenerator OBSIDIAN_GENERATOR = registerCustomBlock(new ObsidianGenerator());
	public static final ItemAbsorber ITEM_ABSORBER = registerCustomBlock(new ItemAbsorber());
	public static final ExperienceAbsorber EXPERIENCE_ABSORBER = registerCustomBlock(new ExperienceAbsorber());
	public static final Farmer FARMER = registerCustomBlock(new Farmer());
	public static final MobGrinder MOB_GRINDER = registerCustomBlock(new MobGrinder());
	public static final BlockBreaker BLOCK_BREAKER = registerCustomBlock(new BlockBreaker());
	public static final Quarry QUARRY = registerCustomBlock(new Quarry());
	public static final DyeFabricator DYE_FABRICATOR = registerCustomBlock(new DyeFabricator());
	public static final Enchanter ENCHANTER = registerCustomBlock(new Enchanter());
	public static final Disenchanter DISENCHANTER = registerCustomBlock(new Disenchanter());
	public static final EnchantmentCombiner ENCHANTMENT_COMBINER = registerCustomBlock(new EnchantmentCombiner());
	public static final Toaster TOASTER = registerCustomBlock(new Toaster());
	public static final MonsterAttractor MONSTER_ATTRACTOR = registerCustomBlock(new MonsterAttractor());
	public static final ChunkLoader CHUNK_LOADER = registerCustomBlock(new ChunkLoader());
	public static final Computer COMPUTER = registerCustomBlock(new Computer());
	public static final Gammabot GAMMABOT = registerCustomBlock(new Gammabot());
	public static final HyperFurnace HYPER_FURNACE_1 = registerCustomBlock(new HyperFurnace(1, 2, 0.5, 1, 1, new ItemStack(Material.FURNACE)));
	public static final HyperFurnace HYPER_FURNACE_2 = registerCustomBlock(new HyperFurnace(2, 3, 1.0, 1, 1, HYPER_FURNACE_1.get()));
	public static final HyperFurnace HYPER_FURNACE_3 = registerCustomBlock(new HyperFurnace(3, 4, 1.5, 2, 1, HYPER_FURNACE_2.get()));
	public static final HyperFurnace HYPER_FURNACE_4 = registerCustomBlock(new HyperFurnace(4, 5, 2.0, 2, 1, HYPER_FURNACE_3.get()));
	public static final HyperFurnace HYPER_FURNACE_5 = registerCustomBlock(new HyperFurnace(5, 6, 2.5, 3, 2, HYPER_FURNACE_4.get()));
	public static final HyperFurnace HYPER_FURNACE_6 = registerCustomBlock(new HyperFurnace(6, 7, 3.0, 3, 2, HYPER_FURNACE_5.get()));
	public static final HyperFurnace HYPER_FURNACE_7 = registerCustomBlock(new HyperFurnace(7, 8, 3.5, 4, 2, HYPER_FURNACE_6.get()));
	public static final HyperFurnace HYPER_FURNACE_8 = registerCustomBlock(new HyperFurnace(8, 9, 4.0, 4, 2, HYPER_FURNACE_7.get()));
	public static final HyperFurnace HYPER_FURNACE_9 = registerCustomBlock(new HyperFurnace(9, 10, 4.5, 5, 3, HYPER_FURNACE_8.get()));
	public static final HyperFurnace HYPER_FURNACE_10 = registerCustomBlock(new HyperFurnace(10, 11, 5.0, 5, 3, HYPER_FURNACE_9.get()));
	public static final HyperFurnace HYPER_FURNACE_11 = registerCustomBlock(new HyperFurnace(11, 12, 5.5, 6, 3, HYPER_FURNACE_10.get()));
	public static final HyperFurnace HYPER_FURNACE_12 = registerCustomBlock(new HyperFurnace(12, 13, 6.0, 6, 3, HYPER_FURNACE_11.get()));
	public static final MineralExtractor MINERAL_EXTRACTOR = registerCustomBlock(new MineralExtractor(MachineTier.BASIC));
	public static final MineralExtractor ADVANCED_MINERAL_EXTRACTOR = registerCustomBlock(new MineralExtractor(MachineTier.ADVANCED));
	public static final MineralExtractor IMPROVED_MINERAL_EXTRACTOR = registerCustomBlock(new MineralExtractor(MachineTier.IMPROVED));
	public static final MineralExtractor PERFECTED_MINERAL_EXTRACTOR = registerCustomBlock(new MineralExtractor(MachineTier.PERFECTED));
	public static final Crusher CRUSHER = registerCustomBlock(new Crusher(MachineTier.BASIC));
	public static final Crusher ADVANCED_CRUSHER = registerCustomBlock(new Crusher(MachineTier.ADVANCED));
	public static final Crusher IMPROVED_CRUSHER = registerCustomBlock(new Crusher(MachineTier.IMPROVED));
	public static final Crusher PERFECTED_CRUSHER = registerCustomBlock(new Crusher(MachineTier.PERFECTED));
	public static final Smeltery SMELTERY = registerCustomBlock(new Smeltery(MachineTier.BASIC));
	public static final Smeltery ADVANCED_SMELTERY = registerCustomBlock(new Smeltery(MachineTier.ADVANCED));
	public static final Smeltery IMPROVED_SMELTERY = registerCustomBlock(new Smeltery(MachineTier.IMPROVED));
	public static final Smeltery PERFECTED_SMELTERY = registerCustomBlock(new Smeltery(MachineTier.PERFECTED));
	public static final Compressor COMPRESSOR = registerCustomBlock(new Compressor(MachineTier.BASIC));
	public static final Compressor ADVANCED_COMPRESSOR = registerCustomBlock(new Compressor(MachineTier.ADVANCED));
	public static final Compressor IMPROVED_COMPRESSOR = registerCustomBlock(new Compressor(MachineTier.IMPROVED));
	public static final Compressor PERFECTED_COMPRESSOR = registerCustomBlock(new Compressor(MachineTier.PERFECTED));
	public static final Freezer FREEZER = registerCustomBlock(new Freezer(MachineTier.BASIC));
	public static final Freezer ADVANCED_FREEZER = registerCustomBlock(new Freezer(MachineTier.ADVANCED));
	public static final Freezer IMPROVED_FREEZER = registerCustomBlock(new Freezer(MachineTier.IMPROVED));
	public static final Freezer PERFECTED_FREEZER = registerCustomBlock(new Freezer(MachineTier.PERFECTED));
	public static final CompactLavaGenerator COMPACT_LAVA_GENERATOR = registerCustomBlock(new CompactLavaGenerator(MachineTier.BASIC));
	public static final CompactLavaGenerator ADVANCED_COMPACT_LAVA_GENERATOR = registerCustomBlock(new CompactLavaGenerator(MachineTier.ADVANCED));
	public static final CompactLavaGenerator IMPROVED_COMPACT_LAVA_GENERATOR = registerCustomBlock(new CompactLavaGenerator(MachineTier.IMPROVED));
	public static final CompactLavaGenerator PERFECTED_COMPACT_LAVA_GENERATOR = registerCustomBlock(new CompactLavaGenerator(MachineTier.PERFECTED));
	public static final CarbonPress CARBON_PRESS = registerCustomBlock(new CarbonPress(MachineTier.BASIC));
	public static final CarbonPress ADVANCED_CARBON_PRESS = registerCustomBlock(new CarbonPress(MachineTier.ADVANCED));
	public static final CarbonPress IMPROVED_CARBON_PRESS = registerCustomBlock(new CarbonPress(MachineTier.IMPROVED));
	public static final CarbonPress PERFECTED_CARBON_PRESS = registerCustomBlock(new CarbonPress(MachineTier.PERFECTED));

	// Redstone
	public static final WirelessRedstoneTransmitter WIRELESS_REDSTONE_TRANSMITTER = registerCustomBlock(new WirelessRedstoneTransmitter());
	public static final WirelessRedstoneReceiver WIRELESS_REDSTONE_RECEIVER = registerCustomBlock(new WirelessRedstoneReceiver());
	public static final RedstoneOrGate REDSTONE_OR_GATE = registerCustomBlock(new RedstoneOrGate());
	public static final RedstoneAndGate REDSTONE_AND_GATE = registerCustomBlock(new RedstoneAndGate());
	public static final RedstoneXorGate REDSTONE_XOR_GATE = registerCustomBlock(new RedstoneXorGate());

	// Hermit Plushies
	public static final HermitPlushie FALSE_SYMMETRY = registerCustomBlock(new HermitPlushie("false_symmetry_plushie", "§eFalseSymmetry", HeadTextures.FALSE_SYMMETRY, Material.WHITE_WOOL, Material.DIAMOND_SWORD, Material.CYAN_TERRACOTTA, Material.YELLOW_DYE));
	public static final HermitPlushie XISUMA = registerCustomBlock(new HermitPlushie("xisuma_plushie", "§aXisuma", HeadTextures.XISUMA, Material.GREEN_WOOL, Material.PHANTOM_MEMBRANE, Material.HONEYCOMB, Material.SCUTE));
	public static final HermitPlushie ZEDAPH = registerCustomBlock(new HermitPlushie("zedaph_plushie", "§6Zedaph", HeadTextures.ZEDAPH, Material.BROWN_WOOL, Material.REDSTONE, Material.LAPIS_LAZULI, Material.HONEY_BLOCK));
	public static final HermitPlushie XB_CRAFTED = registerCustomBlock(new HermitPlushie("xb_crafted_plushie", "§cXBCrafted", HeadTextures.XB_CRAFTED, Material.RED_WOOL, Material.PINK_CARPET, Material.BRICKS, Material.SAND));
	public static final HermitPlushie WELSKNIGHT = registerCustomBlock(new HermitPlushie("welsknight_plushie", "§9Welsknight", HeadTextures.WELSKNIGHT, Material.LIGHT_BLUE_WOOL, Material.CHAINMAIL_CHESTPLATE, Material.BRICK, Material.STONE_BRICKS));
	public static final HermitPlushie TIN_FOIL_CHEF = registerCustomBlock(new HermitPlushie("tin_foil_chef_plushie", "§7TinFoilChef", HeadTextures.TIN_FOIL_CHEF, new ItemStack(Material.CYAN_WOOL), new ItemStack(Material.OAK_PLANKS), CustomItemSetup.RAINBOW_DUST.createDefaultItemStack(), new ItemStack(Material.IRON_HOE)));
	public static final HermitPlushie MUMBO_JUMBO = registerCustomBlock(new HermitPlushie("mumbo_jumbo_plushie", "§8MumboJumbo", HeadTextures.MUMBO_JUMBO, Material.BLACK_WOOL, Material.WHITE_DYE, Material.BAMBOO, Material.REDSTONE));
	public static final HermitPlushie JOE_HILLS_SAYS = registerCustomBlock(new HermitPlushie("joe_hills_says_plushie", "§bJoeHillsSays", HeadTextures.JOE_HILLS_SAYS, Material.CYAN_WOOL, Material.WHITE_DYE, Material.YELLOW_DYE, Material.BLUE_DYE));
	public static final HermitPlushie HYPNOTIZD = registerCustomBlock(new HermitPlushie("hypnotizd_plushie", "§8Hypnotized", HeadTextures.HYPNOTIZD, Material.GRAY_WOOL, Material.BLACK_DYE, Material.YELLOW_DYE, Material.YELLOW_STAINED_GLASS));
	public static final HermitPlushie GRIAN = registerCustomBlock(new HermitPlushie("grian_plushie", "§cGrian", HeadTextures.GRIAN, new ItemStack(Material.TNT), new ItemStack(Material.RED_DYE), new ItemStack(Material.DARK_PRISMARINE), CustomItemSetup.RAINBOW_DUST.createDefaultItemStack()));
	public static final HermitPlushie GUINEA_PIG_GRIAN = registerCustomBlock(new HermitPlushie("guinea_pig_grian_plushie", "§6Guinea-Pig Grian", HeadTextures.GRIAN_GUINEA_PIG, Material.BROWN_WOOL, Material.RABBIT_FOOT, Material.GOLDEN_CARROT, Material.ORANGE_DYE));
	public static final HermitPlushie VINTAGE_BEEF = registerCustomBlock(new HermitPlushie("vintage_beef_plushie", "§9VintageBeef", HeadTextures.VINTAGE_BEEF, Material.BLUE_WOOL, Material.WHITE_DYE, Material.BLACK_DYE, Material.SCUTE));
	public static final HermitPlushie GOOD_TIMES_WITH_SCAR = registerCustomBlock(new HermitPlushie("good_times_with_scar_plushie", "§6GoodTimesWithScar", HeadTextures.GOOD_TIMES_WITH_SCAR, Material.BROWN_WOOL, Material.GRASS_BLOCK, Material.SAND, Material.STONE));
	public static final HermitPlushie KERALIS = registerCustomBlock(new HermitPlushie("keralis_plushie", "§eKeralis", HeadTextures.KERALIS, Material.ORANGE_WOOL, Material.YELLOW_DYE, Material.SCAFFOLDING, Material.LIGHT_BLUE_CARPET));
	public static final HermitPlushie FRENCHRALIS = registerCustomBlock(new HermitPlushie("frenchralis_plushie", "§6Frenchralis", HeadTextures.FRENCHRALIS, Material.WHITE_WOOL, Material.RED_DYE, Material.BLACK_DYE, Material.YELLOW_DYE));
	public static final HermitPlushie I_JEVIN = registerCustomBlock(new HermitPlushie("ijevin_plushie", "§bIJevin", HeadTextures.I_JEVIN, Material.LIGHT_BLUE_WOOL, Material.SLIME_BALL, Material.LIGHT_BLUE_DYE, Material.REDSTONE));
	public static final HermitPlushie ETHOSLAB = registerCustomBlock(new HermitPlushie("ethoslab_plushie", "§8Ethoslab", HeadTextures.ETHOSLAB, Material.LIGHT_GRAY_WOOL, Material.REDSTONE, Material.GRAY_DYE, Material.GREEN_TERRACOTTA));
	public static final HermitPlushie ISKALL85 = registerCustomBlock(new HermitPlushie("iskall85_plushie", "§aIskall85", HeadTextures.ISKALL85, Material.LIME_WOOL, Material.SLIME_BALL, Material.REDSTONE, Material.BONE));
	public static final HermitPlushie TANGO_TEK = registerCustomBlock(new HermitPlushie("tango_tek_plushie", "§cTangoTek", HeadTextures.TANGO_TEK, Material.RED_WOOL, Material.BLACK_DYE, Material.IRON_INGOT, Material.REDSTONE_BLOCK));
	public static final HermitPlushie IMPULS_SV = registerCustomBlock(new HermitPlushie("impulse_sv_plushie", "§eImpulseSV", HeadTextures.IMPULSE_SV, Material.GRAY_WOOL, Material.BLACK_DYE, Material.YELLOW_DYE, Material.REDSTONE));
	public static final HermitPlushie STRESSMONSTER101 = registerCustomBlock(new HermitPlushie("stressmonster101_plushie", "§dStressMonster101", HeadTextures.STRESSMONSTER101, Material.MAGENTA_WOOL, Material.PINK_DYE, Material.PURPLE_DYE, Material.CYAN_DYE));
	public static final HermitPlushie BDOUBLEO100 = registerCustomBlock(new HermitPlushie("bdoubleo100_plushie", "§9BDoubleO100", HeadTextures.BDOUBLEO100, Material.WHITE_WOOL, Material.DIORITE, Material.SADDLE, Material.REDSTONE_ORE));
	public static final HermitPlushie BDOUBLEO100_SMILE = registerCustomBlock(new HermitPlushie("bdoubleo100_smile_plushie", "§bBDoubleO100", HeadTextures.BDOUBLEO100_SMILE, Material.WHITE_WOOL, Material.LIGHT_BLUE_DYE, Material.SUGAR, Material.BLACK_DYE));
	public static final HermitPlushie DOCM77 = registerCustomBlock(new HermitPlushie("docm77_plushie", "§aDocm77", HeadTextures.DOCM77, Material.LIME_WOOL, Material.RED_DYE, Material.GRAY_DYE, Material.REDSTONE_BLOCK));
	public static final HermitPlushie CUBFAN135 = registerCustomBlock(new HermitPlushie("cubfan135_plushie", "§7Cubfan135", HeadTextures.CUBFAN135, Material.WHITE_WOOL, Material.BLUE_DYE, Material.GOLD_BLOCK, Material.GLASS));
	public static final HermitPlushie PHARAO_CUBFAN135 = registerCustomBlock(new HermitPlushie("pharao_cubfan135_plushie", "§6Pharao Cubfan135", HeadTextures.PHARAO_CUBFAN135, Material.RED_WOOL, Material.GOLD_INGOT, Material.LAPIS_LAZULI, Material.WHITE_DYE));
	public static final HermitPlushie ZOMBIE_CLEO = registerCustomBlock(new HermitPlushie("zombie_cleo_plushie", "§6ZombieCleo", HeadTextures.ZOMBIE_CLEO, Material.GREEN_WOOL, Material.ORANGE_DYE, Material.ROTTEN_FLESH, Material.ARMOR_STAND));
	public static final HermitPlushie REN_THE_DOG = registerCustomBlock(new HermitPlushie("ren_the_dog_plushie", "§cRenTheDog", HeadTextures.REN_THE_DOG, Material.RED_WOOL, Material.BLUE_DYE, Material.BLACK_STAINED_GLASS_PANE, Material.BROWN_DYE));
	public static final HermitPlushie REN_BOB = registerCustomBlock(new HermitPlushie("ren_bob_plushie", "§cRenBob", HeadTextures.REN_BOB, Material.GREEN_WOOL, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.GOLD_INGOT, Material.BROWN_DYE));

	public static <T extends AbstractTileStateBlock> T registerCustomBlock(T block) {
		customBlocks.add(block);
		if(block instanceof GuiProvider) {
			guiBlocks.add((GuiProvider) block);
		}
		if(block instanceof BlockRegister) {
			blockRegisters.add((BlockRegister) block);
			if(block instanceof BlockTicker) {
				blockTickers.add((BlockTicker) block);
			}
		}
		return block;
	}


	public static Stream<Listener> getCustomListeners() {
		return customBlocks.stream().filter(AbstractTileStateBlock::hasCustomListener).map(AbstractTileStateBlock::getCustomListener);
	}


	public static void initializeMachineRecipes() {
		blockTickers.stream().filter(ticker -> ticker instanceof Machine).map(ticker -> (Machine) ticker).forEach(Machine::createRecipeMap);
	}


	public static AbstractTileStateBlock getCustomBlockFromTileState(TileState block) {
		for(AbstractTileStateBlock custom : customBlocks) {
			if(custom.isInstanceOf(block)) {
				return custom;
			}
		}
		return null;
	}


	public static AbstractTileStateBlock getCustomBlockFromStack(ItemStack stack) {
		if(!AbstractCustomItem.isCustomItem(stack))
			return null;
		for(AbstractTileStateBlock custom : customBlocks) {
			if(custom.isInstanceOf(stack.getItemMeta())) {
				return custom;
			}
		}
		return null;
	}


	public static Machine getMachineFromStack(ItemStack stack) {
		if(!Machine.isMachine(stack.getItemMeta()))
			return null;
		for(BlockTicker ticker : blockTickers)
			if(ticker instanceof Machine && ((Machine) ticker).isInstanceOf(stack.getItemMeta()))
				return (Machine) ticker;
		return null;
	}

}
