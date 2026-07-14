
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.BLOCKS;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BackpackBlock;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.CardPileBlock;
import me.gamma.cookies.object.block.ChunkLoader;
import me.gamma.cookies.object.block.ClownfishChestBlock;
import me.gamma.cookies.object.block.Computer;
import me.gamma.cookies.object.block.ConveyorBeltBlock;
import me.gamma.cookies.object.block.CustomBlock;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.object.block.LEDBlock;
import me.gamma.cookies.object.block.MachineCasing;
import me.gamma.cookies.object.block.generator.CreativeGeneratorBlock;
import me.gamma.cookies.object.block.generator.FluidGeneratorBlock;
import me.gamma.cookies.object.block.generator.FurnaceGeneratorBlock;
import me.gamma.cookies.object.block.generator.LightningGeneratorBlock;
import me.gamma.cookies.object.block.generator.SolarPanelBlock;
import me.gamma.cookies.object.block.machine.AerialExtractorBlock;
import me.gamma.cookies.object.block.machine.BedrockBreakerBlock;
import me.gamma.cookies.object.block.machine.BioPressBlock;
import me.gamma.cookies.object.block.machine.BlockBreakerBlock;
import me.gamma.cookies.object.block.machine.BlockGeneratorBlock;
import me.gamma.cookies.object.block.machine.CarbonPressBlock;
import me.gamma.cookies.object.block.machine.CompressorBlock;
import me.gamma.cookies.object.block.machine.CrafterBlock;
import me.gamma.cookies.object.block.machine.CraftingFactoryBlock;
import me.gamma.cookies.object.block.machine.CrusherBlock;
import me.gamma.cookies.object.block.machine.DisenchanterBlock;
import me.gamma.cookies.object.block.machine.DryerBlock;
import me.gamma.cookies.object.block.machine.DyePressBlock;
import me.gamma.cookies.object.block.machine.DyerBlock;
import me.gamma.cookies.object.block.machine.EnchanterBlock;
import me.gamma.cookies.object.block.machine.EnchantmentCombinerBlock;
import me.gamma.cookies.object.block.machine.ExperienceAbsorberBlock;
import me.gamma.cookies.object.block.machine.FarmerBlock;
import me.gamma.cookies.object.block.machine.FluidPumpBlock;
import me.gamma.cookies.object.block.machine.FreezerBlock;
import me.gamma.cookies.object.block.machine.HoneyExtractorBlock;
import me.gamma.cookies.object.block.machine.HyperFurnaceBlock;
import me.gamma.cookies.object.block.machine.ItemAbsorberBlock;
import me.gamma.cookies.object.block.machine.LatexExtractorBlock;
import me.gamma.cookies.object.block.machine.LavaGeneratorBlock;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.block.machine.MineralExtractorBlock;
import me.gamma.cookies.object.block.machine.MobGrinderBlock;
import me.gamma.cookies.object.block.machine.ObsidianGeneratorBlock;
import me.gamma.cookies.object.block.machine.OilPumpBlock;
import me.gamma.cookies.object.block.machine.OilRefineryBlock;
import me.gamma.cookies.object.block.machine.QuarryBlock;
import me.gamma.cookies.object.block.machine.RocketAssemblerBlock;
import me.gamma.cookies.object.block.machine.SawmillBlock;
import me.gamma.cookies.object.block.machine.SmelteryBlock;
import me.gamma.cookies.object.block.machine.StarMakerBlock;
import me.gamma.cookies.object.block.machine.TradingMachineBlock;
import me.gamma.cookies.object.block.machine.VoidOreMinerBlock;
import me.gamma.cookies.object.block.network.StorageConnectorBlock;
import me.gamma.cookies.object.block.network.StorageMainComponentBlock;
import me.gamma.cookies.object.block.network.energy.BatteryBlock;
import me.gamma.cookies.object.block.network.energy.TesseractBlock;
import me.gamma.cookies.object.block.network.fluid.EnderTankBlock;
import me.gamma.cookies.object.block.network.fluid.TankBlock;
import me.gamma.cookies.object.block.network.fluid.WasteBarrelBlock;
import me.gamma.cookies.object.block.network.item.EnderAccessorBlock;
import me.gamma.cookies.object.block.network.item.EnderChestBlock;
import me.gamma.cookies.object.block.network.item.ItemStorageCrateBlock;
import me.gamma.cookies.object.block.network.item.ItemStorageExporterBlock;
import me.gamma.cookies.object.block.network.item.ItemStorageImporterBlock;
import me.gamma.cookies.object.block.network.item.ItemStorageMonitorBlock;
import me.gamma.cookies.object.block.network.item.ItemStorageReaderBlock;
import me.gamma.cookies.object.block.network.item.TrashcanBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemDirectionalPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemExtractionPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemFilterPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemInsertionPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemSpeedPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemVacuumPipeBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemVoidPipeBlock;
import me.gamma.cookies.object.block.organic.FruitTreeLeavesBlock;
import me.gamma.cookies.object.block.organic.FruitTreeSaplingBlock;
import me.gamma.cookies.object.block.organic.PlantBushBlock;
import me.gamma.cookies.object.block.organic.PlantStemBlock;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.collection.CachingSupplier;



public class Blocks {

	// Miscellaneous
	public static final CustomBlock ANGEL_BLOCK = BLOCKS.register(new CustomBlock("§6Angel Block", HeadTextures.ANGEL_BLOCK));

	// Resources
	public static final CustomBlock COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("compressed_cobblestone", HeadTextures.COMPRESSED_COBBLESTONE));
	public static final CustomBlock DOUBLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("double_compressed_cobblestone", HeadTextures.DOUBLE_COMPRESSED_COBBLESTONE));
	public static final CustomBlock TRIPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("triple_compressed_cobblestone", HeadTextures.TRIPLE_COMPRESSED_COBBLESTONE));
	public static final CustomBlock QUADRUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("quadruple_compressed_cobblestone", HeadTextures.QUADRUPLE_COMPRESSED_COBBLESTONE));
	public static final CustomBlock QUINTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("quintuple_compressed_cobblestone", HeadTextures.QUINTUPLE_COMPRESSED_COBBLESTONE));
	public static final CustomBlock SEXTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("sectuple_compressed_cobblestone", HeadTextures.SEXTUPLE_COMPRESSED_COBBLESTONE));
	public static final CustomBlock SEPTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("septuple_compressed_cobblestone", HeadTextures.SEPTUPLE_COMPRESSED_COBBLESTONE));
	public static final CustomBlock OCTUPLE_COMPRESSED_COBBLESTONE = BLOCKS.register(new CustomBlock("octuple_compressed_cobblestone", HeadTextures.OCTUPLE_COMPRESSED_COBBLESTONE));

	// Redstone
	/*
	 * WIRELESS_REDSTONE_TRANSMITTER = BLOCKS.register(new WirelessRedstoneTransmitter()); WIRELESS_REDSTONE_RECEIVER = BLOCKS.register(new
	 * WirelessRedstoneReceiver()); REDSTONE_OR_GATE = BLOCKS.register(new DoubleInputRedstoneGate("or_gate", (a, b) -> a || b)); REDSTONE_AND_GATE =
	 * BLOCKS.register(new DoubleInputRedstoneGate("and_gate", (a, b) -> a && b)); REDSTONE_XOR_GATE = BLOCKS.register(new
	 * DoubleInputRedstoneGate("xor_gate", (a, b) -> a ^ b));
	 */

	// Electric Components

	// Technical Components
	public static final ConveyorBeltBlock LIGHT_CONVEYOR_BELT = BLOCKS.register(new ConveyorBeltBlock(ConveyorBeltBlock.Type.LIGHT));
	public static final ConveyorBeltBlock MEDIUM_CONVEYOR_BELT = BLOCKS.register(new ConveyorBeltBlock(ConveyorBeltBlock.Type.MEDIUM));
	public static final ConveyorBeltBlock HEAVY_CONVEYOR_BELT = BLOCKS.register(new ConveyorBeltBlock(ConveyorBeltBlock.Type.HEAVY));

	// Machines
	public static final MachineCasing BASIC_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.BASIC, HeadTextures.BASIC_MACHINE_CASING));
	public static final MachineCasing ADVANCED_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.ADVANCED, HeadTextures.ADVANCED_MACHINE_CASING));
	public static final MachineCasing IMPROVED_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.IMPROVED, HeadTextures.IMPROVED_MACHINE_CASING));
	public static final MachineCasing PERFECTED_MACHINE_CASING = BLOCKS.register(new MachineCasing(MachineTier.PERFECTED, HeadTextures.PERFECTED_MACHINE_CASING));
	public static final BlockGeneratorBlock COBBLESTONE_GENERATOR = BLOCKS.register(new BlockGeneratorBlock("cobblestone_generator", IItemSupplier.of(Material.COBBLESTONE), 80));
	public static final BlockGeneratorBlock STONE_GENERATOR = BLOCKS.register(new BlockGeneratorBlock("stone_generator", IItemSupplier.of(Material.STONE), 120));
	public static final BlockGeneratorBlock DRIPSTONE_GENERATOR = BLOCKS.register(new BlockGeneratorBlock("dripstone_generator", IItemSupplier.of(Material.POINTED_DRIPSTONE), 600));
	public static final BlockGeneratorBlock BASALT_GENERATOR = BLOCKS.register(new BlockGeneratorBlock("basalt_generator", IItemSupplier.of(Material.BASALT), 160));
	public static final LavaGeneratorBlock LAVA_GENERATOR = BLOCKS.register(new LavaGeneratorBlock(null));
	public static final LavaGeneratorBlock BASIC_LAVA_GENERATOR = BLOCKS.register(new LavaGeneratorBlock(MachineTier.BASIC));
	public static final LavaGeneratorBlock ADVANCED_LAVA_GENERATOR = BLOCKS.register(new LavaGeneratorBlock(MachineTier.ADVANCED));
	public static final LavaGeneratorBlock IMPROVED_LAVA_GENERATOR = BLOCKS.register(new LavaGeneratorBlock(MachineTier.IMPROVED));
	public static final LavaGeneratorBlock PERFECTED_LAVA_GENERATOR = BLOCKS.register(new LavaGeneratorBlock(MachineTier.PERFECTED));
	public static final ObsidianGeneratorBlock OBSIDIAN_GENERATOR = BLOCKS.register(new ObsidianGeneratorBlock());
	public static final ItemAbsorberBlock ITEM_ABSORBER = BLOCKS.register(new ItemAbsorberBlock());
	public static final ExperienceAbsorberBlock EXPERIENCE_ABSORBER = BLOCKS.register(new ExperienceAbsorberBlock());
	public static final FarmerBlock BASIC_FARMER = BLOCKS.register(new FarmerBlock(MachineTier.BASIC));
	public static final FarmerBlock ADVANCED_FARMER = BLOCKS.register(new FarmerBlock(MachineTier.ADVANCED));
	public static final FarmerBlock IMPROVED_FARMER = BLOCKS.register(new FarmerBlock(MachineTier.IMPROVED));
	public static final FarmerBlock PERFECTED_FARMER = BLOCKS.register(new FarmerBlock(MachineTier.PERFECTED));
	public static final MobGrinderBlock MOB_GRINDER = BLOCKS.register(new MobGrinderBlock());
	public static final BlockBreakerBlock BLOCK_BREAKER = BLOCKS.register(new BlockBreakerBlock());
	public static final BedrockBreakerBlock BEDROCK_BREAKER = BLOCKS.register(new BedrockBreakerBlock());
	public static final QuarryBlock QUARRY = BLOCKS.register(new QuarryBlock());
	public static final VoidOreMinerBlock BASIC_VOID_ORE_MINER = BLOCKS.register(new VoidOreMinerBlock(MachineTier.BASIC));
	public static final VoidOreMinerBlock ADVANCED_VOID_ORE_MINER = BLOCKS.register(new VoidOreMinerBlock(MachineTier.ADVANCED));
	public static final VoidOreMinerBlock IMPROVED_VOID_ORE_MINER = BLOCKS.register(new VoidOreMinerBlock(MachineTier.IMPROVED));
	public static final VoidOreMinerBlock PERFECTED_VOID_ORE_MINER = BLOCKS.register(new VoidOreMinerBlock(MachineTier.PERFECTED));
	public static final DyePressBlock DYE_PRESS = BLOCKS.register(new DyePressBlock());
	public static final DyerBlock DYE_MIXER = BLOCKS.register(new DyerBlock());
	public static final TradingMachineBlock TRADING_MACHINE = BLOCKS.register(new TradingMachineBlock());
	public static final EnchanterBlock ENCHANTER = BLOCKS.register(new EnchanterBlock());
	public static final DisenchanterBlock DISENCHANTER = BLOCKS.register(new DisenchanterBlock());
	public static final EnchantmentCombinerBlock ENCHANTMENT_COMBINER = BLOCKS.register(new EnchantmentCombinerBlock());
	public static final Computer COMPUTER = BLOCKS.register(new Computer());
	public static final ChunkLoader CHUNK_LOADER = BLOCKS.register(new ChunkLoader());
	public static final HyperFurnaceBlock HYPER_FURNACE_1 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.BASIC, 1));
	public static final HyperFurnaceBlock HYPER_FURNACE_2 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.BASIC, 2));
	public static final HyperFurnaceBlock HYPER_FURNACE_3 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.BASIC, 3));
	public static final HyperFurnaceBlock HYPER_FURNACE_4 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.ADVANCED, 4));
	public static final HyperFurnaceBlock HYPER_FURNACE_5 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.ADVANCED, 5));
	public static final HyperFurnaceBlock HYPER_FURNACE_6 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.ADVANCED, 6));
	public static final HyperFurnaceBlock HYPER_FURNACE_7 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.IMPROVED, 7));
	public static final HyperFurnaceBlock HYPER_FURNACE_8 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.IMPROVED, 8));
	public static final HyperFurnaceBlock HYPER_FURNACE_9 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.IMPROVED, 9));
	public static final HyperFurnaceBlock HYPER_FURNACE_10 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.PERFECTED, 10));
	public static final HyperFurnaceBlock HYPER_FURNACE_11 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.PERFECTED, 11));
	public static final HyperFurnaceBlock HYPER_FURNACE_12 = BLOCKS.register(new HyperFurnaceBlock(MachineTier.PERFECTED, 12));
	public static final CrusherBlock BASIC_CRUSHER = BLOCKS.register(new CrusherBlock(MachineTier.BASIC));
	public static final CrusherBlock ADVANCED_CRUSHER = BLOCKS.register(new CrusherBlock(MachineTier.ADVANCED));
	public static final CrusherBlock IMPROVED_CRUSHER = BLOCKS.register(new CrusherBlock(MachineTier.IMPROVED));
	public static final CrusherBlock PERFECTED_CRUSHER = BLOCKS.register(new CrusherBlock(MachineTier.PERFECTED));
	public static final MineralExtractorBlock BASIC_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractorBlock(MachineTier.BASIC));
	public static final MineralExtractorBlock ADVANCED_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractorBlock(MachineTier.ADVANCED));
	public static final MineralExtractorBlock IMPROVED_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractorBlock(MachineTier.IMPROVED));
	public static final MineralExtractorBlock PERFECTED_MINERAL_EXTRACTOR = BLOCKS.register(new MineralExtractorBlock(MachineTier.PERFECTED));
	public static final AerialExtractorBlock BASIC_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractorBlock(MachineTier.BASIC));
	public static final AerialExtractorBlock ADVANCED_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractorBlock(MachineTier.ADVANCED));
	public static final AerialExtractorBlock IMPROVED_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractorBlock(MachineTier.IMPROVED));
	public static final AerialExtractorBlock PERFECTED_AERIAL_EXTRACTOR = BLOCKS.register(new AerialExtractorBlock(MachineTier.PERFECTED));
	public static final SawmillBlock BASIC_SAWMILL = BLOCKS.register(new SawmillBlock(MachineTier.BASIC));
	public static final SawmillBlock ADVANCED_SAWMILL = BLOCKS.register(new SawmillBlock(MachineTier.ADVANCED));
	public static final SawmillBlock IMPROVED_SAWMILL = BLOCKS.register(new SawmillBlock(MachineTier.IMPROVED));
	public static final SawmillBlock PERFECTED_SAWMILL = BLOCKS.register(new SawmillBlock(MachineTier.PERFECTED));
	public static final SmelteryBlock BASIC_SMELTERY = BLOCKS.register(new SmelteryBlock(MachineTier.BASIC));
	public static final SmelteryBlock ADVANCED_SMELTERY = BLOCKS.register(new SmelteryBlock(MachineTier.ADVANCED));
	public static final SmelteryBlock IMPROVED_SMELTERY = BLOCKS.register(new SmelteryBlock(MachineTier.IMPROVED));
	public static final SmelteryBlock PERFECTED_SMELTERY = BLOCKS.register(new SmelteryBlock(MachineTier.PERFECTED));
	public static final CompressorBlock BASIC_COMPRESSOR = BLOCKS.register(new CompressorBlock(MachineTier.BASIC));
	public static final CompressorBlock ADVANCED_COMPRESSOR = BLOCKS.register(new CompressorBlock(MachineTier.ADVANCED));
	public static final CompressorBlock IMPROVED_COMPRESSOR = BLOCKS.register(new CompressorBlock(MachineTier.IMPROVED));
	public static final CompressorBlock PERFECTED_COMPRESSOR = BLOCKS.register(new CompressorBlock(MachineTier.PERFECTED));
	public static final BioPressBlock BASIC_BIO_PRESS = BLOCKS.register(new BioPressBlock(MachineTier.BASIC));
	public static final BioPressBlock ADVANCED_BIO_PRESS = BLOCKS.register(new BioPressBlock(MachineTier.ADVANCED));
	public static final BioPressBlock IMPROVED_BIO_PRESS = BLOCKS.register(new BioPressBlock(MachineTier.IMPROVED));
	public static final BioPressBlock PERFECTED_BIO_PRESS = BLOCKS.register(new BioPressBlock(MachineTier.PERFECTED));
	public static final LatexExtractorBlock LATEX_EXTRACTOR = BLOCKS.register(new LatexExtractorBlock());
	public static final CrafterBlock BASIC_CRAFTER = BLOCKS.register(new CrafterBlock(MachineTier.BASIC));
	public static final CrafterBlock ADVANCED_CRAFTER = BLOCKS.register(new CrafterBlock(MachineTier.ADVANCED));
	public static final CrafterBlock IMPROVED_CRAFTER = BLOCKS.register(new CrafterBlock(MachineTier.IMPROVED));
	public static final CrafterBlock PERFECTED_CRAFTER = BLOCKS.register(new CrafterBlock(MachineTier.PERFECTED));
	public static final CraftingFactoryBlock BASIC_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactoryBlock(MachineTier.BASIC));
	public static final CraftingFactoryBlock ADVANCED_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactoryBlock(MachineTier.ADVANCED));
	public static final CraftingFactoryBlock IMPROVED_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactoryBlock(MachineTier.IMPROVED));
	public static final CraftingFactoryBlock PERFECTED_CRAFTING_FACTORY = BLOCKS.register(new CraftingFactoryBlock(MachineTier.PERFECTED));
	public static final CarbonPressBlock BASIC_CARBON_PRESS = BLOCKS.register(new CarbonPressBlock(MachineTier.BASIC));
	public static final CarbonPressBlock ADVANCED_CARBON_PRESS = BLOCKS.register(new CarbonPressBlock(MachineTier.ADVANCED));
	public static final CarbonPressBlock IMPROVED_CARBON_PRESS = BLOCKS.register(new CarbonPressBlock(MachineTier.IMPROVED));
	public static final CarbonPressBlock PERFECTED_CARBON_PRESS = BLOCKS.register(new CarbonPressBlock(MachineTier.PERFECTED));
	public static final DryerBlock BASIC_DRYER = BLOCKS.register(new DryerBlock(MachineTier.BASIC));
	public static final DryerBlock ADVANCED_DRYER = BLOCKS.register(new DryerBlock(MachineTier.ADVANCED));
	public static final DryerBlock IMPROVED_DRYER = BLOCKS.register(new DryerBlock(MachineTier.IMPROVED));
	public static final DryerBlock PERFECTED_DRYER = BLOCKS.register(new DryerBlock(MachineTier.PERFECTED));
	public static final FreezerBlock BASIC_FREEZER = BLOCKS.register(new FreezerBlock(MachineTier.BASIC));
	public static final FreezerBlock ADVANCED_FREEZER = BLOCKS.register(new FreezerBlock(MachineTier.ADVANCED));
	public static final FreezerBlock IMPROVED_FREEZER = BLOCKS.register(new FreezerBlock(MachineTier.IMPROVED));
	public static final FreezerBlock PERFECTED_FREEZER = BLOCKS.register(new FreezerBlock(MachineTier.PERFECTED));
	public static final HoneyExtractorBlock HONEY_EXTRACTOR = BLOCKS.register(new HoneyExtractorBlock());
	public static final StarMakerBlock STAR_MAKER = BLOCKS.register(new StarMakerBlock());
	public static final RocketAssemblerBlock ROCKET_ASSEMBLER = BLOCKS.register(new RocketAssemblerBlock());

	// Generators
	public static final FurnaceGeneratorBlock FURNACE_GENERATOR = BLOCKS.register(new FurnaceGeneratorBlock());
	public static final FluidGeneratorBlock THERMO_GENERATOR = BLOCKS.register(new FluidGeneratorBlock("Thermo Generator", HeadTextures.THERMO_GENERATOR, MachineTier.BASIC, FluidType.LAVA));
	public static final SolarPanelBlock BASIC_SOLAR_PANEL = BLOCKS.register(new SolarPanelBlock(HeadTextures.SOLAR_PANEL_WHITE, MachineTier.BASIC));
	public static final SolarPanelBlock ADVANCED_SOLAR_PANEL = BLOCKS.register(new SolarPanelBlock(HeadTextures.SOLAR_PANEL_YELLOW, MachineTier.ADVANCED));
	public static final SolarPanelBlock IMPROVED_SOLAR_PANEL = BLOCKS.register(new SolarPanelBlock(HeadTextures.SOLAR_PANEL_AQUA, MachineTier.IMPROVED));
	public static final SolarPanelBlock PERFECTED_SOLAR_PANEL = BLOCKS.register(new SolarPanelBlock(HeadTextures.SOLAR_PANEL_MAGENTA, MachineTier.PERFECTED));
	public static final FluidGeneratorBlock BASIC_BIO_GENERATOR = BLOCKS.register(new FluidGeneratorBlock("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.BASIC, FluidType.BIO_MASS));
	public static final FluidGeneratorBlock ADVANCED_BIO_GENERATOR = BLOCKS.register(new FluidGeneratorBlock("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.ADVANCED, FluidType.BIO_MASS));
	public static final FluidGeneratorBlock IMPROVED_BIO_GENERATOR = BLOCKS.register(new FluidGeneratorBlock("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.IMPROVED, FluidType.BIO_MASS));
	public static final FluidGeneratorBlock PERFECTED_BIO_GENERATOR = BLOCKS.register(new FluidGeneratorBlock("Bio Generator", HeadTextures.BIO_GENERATOR, MachineTier.PERFECTED, FluidType.BIO_MASS));
	public static final FluidGeneratorBlock DIESEL_GENERATOR = BLOCKS.register(new FluidGeneratorBlock("§eDiesel Generator", HeadTextures.DIESEL_GENERATOR, null, FluidType.REFINED_OIL));
	public static final LightningGeneratorBlock LIGHTNING_GENERATOR = BLOCKS.register(new LightningGeneratorBlock());
	public static final CreativeGeneratorBlock CREATIVE_GENERATOR = BLOCKS.register(new CreativeGeneratorBlock());

	// Storage
	public static final BackpackBlock BROWN_BACKPACK = BLOCKS.register(new BackpackBlock("brown_backpack", "§fBrown Backpack", HeadTextures.BROWN_BACKPACK, 1));
	public static final BackpackBlock COPPER_BACKPACK = BLOCKS.register(new BackpackBlock("copper_backpack", "§cCopper Backpack", HeadTextures.COPPER_BACKPACK, 2));
	public static final BackpackBlock IRON_BACKPACK = BLOCKS.register(new BackpackBlock("iron_backpack", "§7Iron Backpack", HeadTextures.IRON_BACKPACK, 3));
	public static final BackpackBlock GOLDEN_BACKPACK = BLOCKS.register(new BackpackBlock("golden_backpack", "§eGolden Backpack", HeadTextures.GOLDEN_BACKPACK, 4));
	public static final BackpackBlock DIAMOND_BACKPACK = BLOCKS.register(new BackpackBlock("diamond_backpack", "§bDiamond Backpack", HeadTextures.DIAMOND_BACKPACK, 5));
	public static final BackpackBlock NETHERITE_BACKPACK = BLOCKS.register(new BackpackBlock("netherite_backpack", "§dNetherite Backpack", HeadTextures.NETHERITE_BACKPACK, 6));
	public static final ItemStorageCrateBlock OAK_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("oak_storage_crate", HeadTextures.OAK_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock SPRUCE_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("spruce_storage_crate", HeadTextures.SPRUCE_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock BIRCH_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("birch_storage_crate", HeadTextures.BIRCH_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock JUNGLE_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("jungle_storage_crate", HeadTextures.JUNGLE_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock ACACIA_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("acacia_storage_crate", HeadTextures.ACACIA_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock DARK_OAK_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("dark_oak_storage_crate", HeadTextures.DARK_OAK_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock MANGROVE_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("mangrove_storage_crate", HeadTextures.MANGROVE_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock CHERRY_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("cherry_storage_crate", HeadTextures.CHERRY_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock PALE_OAK_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("pale_oak_storage_crate", HeadTextures.PALE_OAK_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock BAMBOO_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("bamboo_storage_crate", HeadTextures.BAMBOO_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock CRIMSON_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("crimson_storage_crate", HeadTextures.CRIMSON_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock WARPED_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("warped_storage_crate", HeadTextures.WARPED_STORAGE_CRATE, 1, 8));
	public static final ItemStorageCrateBlock IRON_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("iron_storage_crate", HeadTextures.IRON_STORAGE_CRATE, 7, 16));
	public static final ItemStorageCrateBlock GOLDEN_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("golden_storage_crate", HeadTextures.GOLDEN_STORAGE_CRATE, 14, 64));
	public static final ItemStorageCrateBlock DIAMOND_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("diamond_storage_crate", HeadTextures.DIAMOND_STORAGE_CRATE, 28, 256));
	public static final ItemStorageCrateBlock EMERALD_STORAGE_CRATE = BLOCKS.register(new ItemStorageCrateBlock("emerald_storage_crate", HeadTextures.EMERALD_STORAGE_CRATE, 56, 1024));
	public static final ItemStorageCrateBlock CLOWNFISH_STORAGE_SKULL_BLOCK = BLOCKS.register(new ItemStorageCrateBlock("clownfish_storage_crate", HeadTextures.BLUE_STORAGE_CRATE, 112, 4096));
	public static final ClownfishChestBlock CLOWNFISH_CHEST = BLOCKS.register(new ClownfishChestBlock());
	public static final EnderChestBlock ENDER_CHEST = BLOCKS.register(new EnderChestBlock());
	public static final TrashcanBlock TRASHCAN = BLOCKS.register(new TrashcanBlock());
	public static final ItemPipeBlock ITEM_PIPE = BLOCKS.register(new ItemPipeBlock());
	public static final ItemExtractionPipeBlock ITEM_EXTRACTION_PIPE = BLOCKS.register(new ItemExtractionPipeBlock());
	public static final ItemInsertionPipeBlock ITEM_INSERTION_PIPE = BLOCKS.register(new ItemInsertionPipeBlock());
	public static final ItemSpeedPipeBlock ITEM_SPEED_PIPE = BLOCKS.register(new ItemSpeedPipeBlock(4.0D));
	public static final ItemDirectionalPipeBlock ITEM_DIRECTIONAL_PIPE = BLOCKS.register(new ItemDirectionalPipeBlock());
	public static final ItemVacuumPipeBlock ITEM_VACUUM_PIPE = BLOCKS.register(new ItemVacuumPipeBlock());
	public static final ItemVoidPipeBlock ITEM_VOID_PIPE = BLOCKS.register(new ItemVoidPipeBlock());
	public static final ItemFilterPipeBlock ITEM_FILTER_PIPE = BLOCKS.register(new ItemFilterPipeBlock());
	public static final StorageConnectorBlock<ItemStack> ITEM_STORAGE_CONNECTOR = BLOCKS.register(new StorageConnectorBlock<>("item_storage_connector", HeadTextures.STORAGE_CONNECTOR, ItemStack.class));
	public static final StorageMainComponentBlock<ItemStack> ITEM_STORAGE_MAIN_COMPONENT = BLOCKS.register(new StorageMainComponentBlock<>("item_storage_main_component", HeadTextures.STORAGE_MAIN_COMPONENT, ItemStack::getMaxStackSize, ItemStack.class));
	public static final ItemStorageImporterBlock STORAGE_IMPORTER = BLOCKS.register(new ItemStorageImporterBlock());
	public static final ItemStorageExporterBlock STORAGE_EXPORTER = BLOCKS.register(new ItemStorageExporterBlock());
	public static final ItemStorageReaderBlock STORAGE_READER = BLOCKS.register(new ItemStorageReaderBlock());
	public static final EnderAccessorBlock ENDER_ACCESSOR = BLOCKS.register(new EnderAccessorBlock());
	public static final ItemStorageMonitorBlock STORAGE_MONITOR = BLOCKS.register(new ItemStorageMonitorBlock());

	// Fluids
	public static final TankBlock BASIC_TANK = BLOCKS.register(new TankBlock(MachineTier.BASIC, 1000));
	public static final TankBlock ADVANCED_TANK = BLOCKS.register(new TankBlock(MachineTier.ADVANCED, 4000));
	public static final TankBlock IMPROVED_TANK = BLOCKS.register(new TankBlock(MachineTier.IMPROVED, 16000));
	public static final TankBlock PERFECTED_TANK = BLOCKS.register(new TankBlock(MachineTier.PERFECTED, 64000));
	public static final FluidPumpBlock FLUID_PUMP = BLOCKS.register(new FluidPumpBlock());
	public static final OilPumpBlock OIL_PUMP = BLOCKS.register(new OilPumpBlock());
	public static final OilRefineryBlock OIL_REFINERY = BLOCKS.register(new OilRefineryBlock());
	public static final EnderTankBlock ENDER_TANK = BLOCKS.register(new EnderTankBlock());
	public static final WasteBarrelBlock WASTE_BARREL = BLOCKS.register(new WasteBarrelBlock());

	// Energy
	public static final BatteryBlock BATTERY_RED = BLOCKS.register(new BatteryBlock("battery_red", HeadTextures.BATTERY_RED, 3, 1000));
	public static final BatteryBlock BATTERY_ORANGE = BLOCKS.register(new BatteryBlock("battery_orange", HeadTextures.BATTERY_ORANGE, 5, 4000));
	public static final BatteryBlock BATTERY_YELLOW = BLOCKS.register(new BatteryBlock("battery_yellow", HeadTextures.BATTERY_YELLOW, 8, 16000));
	public static final BatteryBlock BATTERY_GREEN = BLOCKS.register(new BatteryBlock("battery_green", HeadTextures.BATTERY_GREEN, 13, 64000));
	public static final BatteryBlock BATTERY_CYAN = BLOCKS.register(new BatteryBlock("battery_cyan", HeadTextures.BATTERY_CYAN, 21, 256000));
	public static final BatteryBlock BATTERY_BLUE = BLOCKS.register(new BatteryBlock("battery_blue", HeadTextures.BATTERY_BLUE, 34, 1024000));
	public static final BatteryBlock BATTERY_PURPLE = BLOCKS.register(new BatteryBlock("battery_purple", HeadTextures.BATTERY_PURPLE, 55, 4096000));
	public static final BatteryBlock BATTERY_BLACK = BLOCKS.register(new BatteryBlock("battery_black", HeadTextures.BATTERY_BLACK, 89, 16384000));
	public static final LEDBlock LED_PURPLE = BLOCKS.register(new LEDBlock("led_purple", HeadTextures.LED_PURPLE));
	public static final LEDBlock LED_BLUE = BLOCKS.register(new LEDBlock("led_blue", HeadTextures.LED_BLUE));
	public static final LEDBlock LED_CYAN = BLOCKS.register(new LEDBlock("led_cyan", HeadTextures.LED_CYAN));
	public static final LEDBlock LED_GREEN = BLOCKS.register(new LEDBlock("led_green", HeadTextures.LED_GREEN));
	public static final LEDBlock LED_ORANGE = BLOCKS.register(new LEDBlock("led_orange", HeadTextures.LED_ORANGE));
	public static final LEDBlock LED_RED = BLOCKS.register(new LEDBlock("led_red", HeadTextures.LED_RED));
	public static final TesseractBlock TESSERACT = BLOCKS.register(new TesseractBlock());

	// Fun
	public static final CardPileBlock CARD_PILE = BLOCKS.register(new CardPileBlock());

	// Plants
	public static final CustomBlock PLANT_FRUIT = BLOCKS.register(new CustomBlock("plant_fruit", HeadTextures.GLOWBERRY));
	public static final PlantBushBlock PLANT_BUSH = BLOCKS.register(new PlantBushBlock("plant_bush", Material.JUNGLE_LEAVES, PLANT_FRUIT));
	public static final PlantStemBlock PLANT_STEM = BLOCKS.register(new PlantStemBlock("plant_stem", Material.OAK_SAPLING, PLANT_BUSH));
	public static final CustomBlock RED_APPLE = BLOCKS.register(new CustomBlock("red_apple", HeadTextures.RED_APPLE));
	public static final FruitTreeLeavesBlock RED_APPLE_LEAVES = BLOCKS.register(new FruitTreeLeavesBlock("red_apple_leaves", Material.OAK_LEAVES, RED_APPLE, true));
	public static final FruitTreeSaplingBlock RED_APPLE_SAPLING = BLOCKS.register(new FruitTreeSaplingBlock("red_apple_sapling", Material.OAK_SAPLING, TreeType.TREE, Material.OAK_LOG, RED_APPLE_LEAVES));
	public static final CustomBlock GREEN_APPLE = BLOCKS.register(new CustomBlock("green_apple", HeadTextures.GREEN_APPLE));
	public static final FruitTreeLeavesBlock GREEN_APPLE_LEAVES = BLOCKS.register(new FruitTreeLeavesBlock("green_apple_leaves", Material.OAK_LEAVES, GREEN_APPLE, true));
	public static final FruitTreeSaplingBlock GREEN_APPLE_SAPLING = BLOCKS.register(new FruitTreeSaplingBlock("green_apple_sapling", Material.OAK_SAPLING, TreeType.TREE, Material.OAK_LOG, GREEN_APPLE_LEAVES));
	public static final CustomBlock ORANGE = BLOCKS.register(new CustomBlock("orange", HeadTextures.ORANGE));
	public static final FruitTreeLeavesBlock ORANGE_LEAVES = BLOCKS.register(new FruitTreeLeavesBlock("orange_leaves", Material.JUNGLE_LEAVES, ORANGE, true));
	public static final FruitTreeSaplingBlock ORANGE_SAPLING = BLOCKS.register(new FruitTreeSaplingBlock("orange_sapling", Material.JUNGLE_SAPLING, TreeType.TREE, Material.JUNGLE_LOG, ORANGE_LEAVES));

	// Plushies
	public static final CustomBlock FALSE_SYMMETRY_PLUSHIE = BLOCKS.register(new CustomBlock("false_symmetry_plushie", HeadTextures.FALSE_SYMMETRY));
	public static final CustomBlock XISUMA_PLUSHIE = BLOCKS.register(new CustomBlock("xisuma_plushie", HeadTextures.XISUMA));
	public static final CustomBlock ZEDAPH_PLUSHIE = BLOCKS.register(new CustomBlock("zedaph_plushie", HeadTextures.ZEDAPH));
	public static final CustomBlock XB_CRAFTED_PLUSHIE = BLOCKS.register(new CustomBlock("xb_crafted_plushie", HeadTextures.XB_CRAFTED));
	public static final CustomBlock WELSKNIGHT_PLUSHIE = BLOCKS.register(new CustomBlock("welsknight_plushie", HeadTextures.WELSKNIGHT));
	public static final CustomBlock TIN_FOIL_CHEF_PLUSHIE = BLOCKS.register(new CustomBlock("tin_foil_chef_plushie", HeadTextures.TIN_FOIL_CHEF));
	public static final CustomBlock MUMBO_JUMBO_PLUSHIE = BLOCKS.register(new CustomBlock("mumbo_jumbo_plushie", HeadTextures.MUMBO_JUMBO));
	public static final CustomBlock JOE_HILLS_SAYS_PLUSHIE = BLOCKS.register(new CustomBlock("joe_hills_says_plushie", HeadTextures.JOE_HILLS_SAYS));
	public static final CustomBlock HYPNOTIZD_PLUSHIE = BLOCKS.register(new CustomBlock("hypnotizd_plushie", HeadTextures.HYPNOTIZD));
	public static final CustomBlock GRIAN_PLUSHIE = BLOCKS.register(new CustomBlock("grian_plushie", HeadTextures.GRIAN));
	public static final CustomBlock GUINEA_PIG_GRIAN_PLUSHIE = BLOCKS.register(new CustomBlock("guinea_pig_grian_plushie", HeadTextures.GRIAN_GUINEA_PIG));
	public static final CustomBlock POULTRY_MAN_PLUSHIE = BLOCKS.register(new CustomBlock("poultry_man_plushie", HeadTextures.POULTRY_MAN));
	public static final CustomBlock VINTAGE_BEEF_PLUSHIE = BLOCKS.register(new CustomBlock("vintage_beef_plushie", HeadTextures.VINTAGE_BEEF));
	public static final CustomBlock GOOD_TIMES_WITH_SCAR_PLUSHIE = BLOCKS.register(new CustomBlock("good_times_with_scar_plushie", HeadTextures.GOOD_TIMES_WITH_SCAR));
	public static final CustomBlock JELLIE_PLUSHIE = BLOCKS.register(new CustomBlock("jellie_plushie", HeadTextures.JELLIE));
	public static final CustomBlock KERALIS_PLUSHIE = BLOCKS.register(new CustomBlock("keralis_plushie", HeadTextures.KERALIS));
	public static final CustomBlock FRENCHRALIS_PLUSHIE = BLOCKS.register(new CustomBlock("frenchralis_plushie", HeadTextures.FRENCHRALIS));
	public static final CustomBlock I_JEVIN_PLUSHIE = BLOCKS.register(new CustomBlock("ijevin_plushie", HeadTextures.I_JEVIN));
	public static final CustomBlock ETHOSLAB_PLUSHIE = BLOCKS.register(new CustomBlock("ethoslab_plushie", HeadTextures.ETHOSLAB));
	public static final CustomBlock ISKALL85_PLUSHIE = BLOCKS.register(new CustomBlock("iskall85_plushie", HeadTextures.ISKALL85));
	public static final CustomBlock TANGO_TEK_PLUSHIE = BLOCKS.register(new CustomBlock("tango_tek_plushie", HeadTextures.TANGO_TEK));
	public static final CustomBlock IMPULS_SV_PLUSHIE = BLOCKS.register(new CustomBlock("impulse_sv_plushie", HeadTextures.IMPULSE_SV));
	public static final CustomBlock STRESSMONSTER101_PLUSHIE = BLOCKS.register(new CustomBlock("stressmonster101_plushie", HeadTextures.STRESSMONSTER101));
	public static final CustomBlock BDOUBLEO100_PLUSHIE = BLOCKS.register(new CustomBlock("bdoubleo100_plushie", HeadTextures.BDOUBLEO100));
	public static final CustomBlock BDOUBLEO100_SMILE_PLUSHIE = BLOCKS.register(new CustomBlock("bdoubleo100_smile_plushie", HeadTextures.BDOUBLEO100_SMILE));
	public static final CustomBlock DOCM77_PLUSHIE = BLOCKS.register(new CustomBlock("docm77_plushie", HeadTextures.DOCM77));
	public static final CustomBlock CUBFAN135_PLUSHIE = BLOCKS.register(new CustomBlock("cubfan135_plushie", HeadTextures.CUBFAN135));
	public static final CustomBlock DOCTOR_CUBFAN135_PLUSHIE = BLOCKS.register(new CustomBlock("doctor_cubfan135_plushie", HeadTextures.DOCTOR_CUBFAN135));
	public static final CustomBlock PHARAO_CUBFAN135_PLUSHIE = BLOCKS.register(new CustomBlock("pharao_cubfan135_plushie", HeadTextures.PHARAO_CUBFAN135));
	public static final CustomBlock ZOMBIE_CLEO_PLUSHIE = BLOCKS.register(new CustomBlock("zombie_cleo_plushie", HeadTextures.ZOMBIE_CLEO));
	public static final CustomBlock REN_THE_DOG_PLUSHIE = BLOCKS.register(new CustomBlock("ren_the_dog_plushie", HeadTextures.REN_THE_DOG));
	public static final CustomBlock REN_BOB_PLUSHIE = BLOCKS.register(new CustomBlock("ren_bob_plushie", HeadTextures.REN_BOB));
	public static final CustomBlock PEARLESCENT_MOON_PLUSHIE = BLOCKS.register(new CustomBlock("pearlescent_moon_plushie", HeadTextures.PEARLESCENT_MOON));
	public static final CustomBlock GEMINI_TAY_PLUSHIE = BLOCKS.register(new CustomBlock("gemini_tay_plushie", HeadTextures.GEMINI_TAY));
	public static final CustomBlock JOEYGRACEFFA_PLUSHIE = BLOCKS.register(new CustomBlock("joeygraceffa_plushie", HeadTextures.JOEYGRACEFFA));
	public static final CustomBlock SHUBBLE_YT_PLUSHIE = BLOCKS.register(new CustomBlock("shubble_yt_plushie", HeadTextures.SHUBBLE_YT));
	public static final CustomBlock SOLIDARITY_GAMING_PLUSHIE = BLOCKS.register(new CustomBlock("solidarity_gaming_plushie", HeadTextures.SOLIDARITY_GAMING));
	public static final CustomBlock SMALISHBEANS_PLUSHIE = BLOCKS.register(new CustomBlock("smalishbeans_plushie", HeadTextures.SMALISHBEANS));
	public static final CustomBlock SMAJOR1995_PLUSHIE = BLOCKS.register(new CustomBlock("smajor1995_plushis", HeadTextures.SMAJOR1995));
	public static final CustomBlock PIXLRIFFS_PLUSHIE = BLOCKS.register(new CustomBlock("pixlriffs_plushie", HeadTextures.PIXLRIFFS));
	public static final CustomBlock MYTHICAL_SAUSAGE_PLUSHIE = BLOCKS.register(new CustomBlock("mythical_sausage_plushie", HeadTextures.MYTHICAL_SAUSAGE));
	public static final CustomBlock LDS_SHADOWLADY_PLUSHIE = BLOCKS.register(new CustomBlock("lds_shadowlady", HeadTextures.LDS_SHADOWLADY));
	public static final CustomBlock KATHERINEELIZ_PLUSHIE = BLOCKS.register(new CustomBlock("katherineeliz_plushie", HeadTextures.KATHERINEELIZ));
	public static final CustomBlock FWHIP_PLUSHIE = BLOCKS.register(new CustomBlock("fwhip_plushie", HeadTextures.FWHIP));

	public static void init() {}


	public static AbstractCustomBlock getCustomBlockFromIdentifier(String identifier) {
		return BLOCKS.filterFirst(custom -> custom.getIdentifier().equals(identifier));
	}


	public static AbstractCustomBlock getCustomBlockFromStack(ItemStack stack) {
		String identifier = AbstractCustomItem.getIdentifier(stack);
		if(identifier == null)
			return null;

		return getCustomBlockFromIdentifier(identifier);
	}


	public static AbstractCustomBlock getCustomBlockFromHolder(PersistentDataHolder holder) {
		String identifier = Properties.IDENTIFIER.fetch(holder);
		if(identifier == null)
			return null;

		return getCustomBlockFromIdentifier(identifier);
	}


	public static AbstractCustomBlock getCustomBlockFromBlock(Block block) {
		return CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block);
	}

	private static final CachingSupplier<List<BlockInventoryProvider>> GUI_PROVIDER_SUPPLIER = new CachingSupplier<>(() -> BLOCKS.filterByClass(BlockInventoryProvider.class));

	public static List<BlockInventoryProvider> getGuiProviders() {
		return GUI_PROVIDER_SUPPLIER.get();
	}


	public static Stream<Listener> getCustomListeners() {
		return BLOCKS.stream().filter(AbstractCustomBlock::hasListener).map(AbstractCustomBlock::getListener);
	}

}
