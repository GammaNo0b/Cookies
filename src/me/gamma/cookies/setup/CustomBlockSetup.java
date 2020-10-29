
package me.gamma.cookies.setup;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.BlockRegister;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.objects.block.skull.AbstractMachine;
import me.gamma.cookies.objects.block.skull.Accumulator;
import me.gamma.cookies.objects.block.skull.AdvancedCarbonPress;
import me.gamma.cookies.objects.block.skull.AdvancedCompressor;
import me.gamma.cookies.objects.block.skull.AdvancedCrusher;
import me.gamma.cookies.objects.block.skull.AdvancedMachineCasing;
import me.gamma.cookies.objects.block.skull.AdvancedSmeltery;
import me.gamma.cookies.objects.block.skull.BlockBreaker;
import me.gamma.cookies.objects.block.skull.CarbonPress;
import me.gamma.cookies.objects.block.skull.ChunkLoader;
import me.gamma.cookies.objects.block.skull.ClownfishChest;
import me.gamma.cookies.objects.block.skull.ClownfishStorageSkullBlock;
import me.gamma.cookies.objects.block.skull.CobblestoneGenerator;
import me.gamma.cookies.objects.block.skull.Compressor;
import me.gamma.cookies.objects.block.skull.CopperCoil;
import me.gamma.cookies.objects.block.skull.Crusher;
import me.gamma.cookies.objects.block.skull.Cube;
import me.gamma.cookies.objects.block.skull.DyeFabricator;
import me.gamma.cookies.objects.block.skull.Electromagnet;
import me.gamma.cookies.objects.block.skull.ExperienceAbsorber;
import me.gamma.cookies.objects.block.skull.Farmer;
import me.gamma.cookies.objects.block.skull.IllusionCube;
import me.gamma.cookies.objects.block.skull.ImprovedCarbonPress;
import me.gamma.cookies.objects.block.skull.ImprovedCompressor;
import me.gamma.cookies.objects.block.skull.ImprovedCrusher;
import me.gamma.cookies.objects.block.skull.ImprovedMachineCasing;
import me.gamma.cookies.objects.block.skull.ImprovedSmeltery;
import me.gamma.cookies.objects.block.skull.ItemAbsorber;
import me.gamma.cookies.objects.block.skull.LED;
import me.gamma.cookies.objects.block.skull.MachineCasing;
import me.gamma.cookies.objects.block.skull.MobGrinder;
import me.gamma.cookies.objects.block.skull.Motor;
import me.gamma.cookies.objects.block.skull.PerfectedCrusher;
import me.gamma.cookies.objects.block.skull.PerfectedMachineCasing;
import me.gamma.cookies.objects.block.skull.PerfectedSmeltery;
import me.gamma.cookies.objects.block.skull.Quarry;
import me.gamma.cookies.objects.block.skull.RainbowCube;
import me.gamma.cookies.objects.block.skull.Smeltery;
import me.gamma.cookies.objects.block.skull.StorageConnector;
import me.gamma.cookies.objects.block.skull.StorageMonitor;
import me.gamma.cookies.objects.block.skull.StorageSkullBlockTier1;
import me.gamma.cookies.objects.block.skull.StorageSkullBlockTier2;
import me.gamma.cookies.objects.block.skull.StorageSkullBlockTier3;
import me.gamma.cookies.objects.block.skull.StorageSkullBlockTier4;
import me.gamma.cookies.objects.block.skull.WirelessRedstoneReceiver;
import me.gamma.cookies.objects.block.skull.WirelessRedstoneTransmitter;



public class CustomBlockSetup {

	public static final List<AbstractTileStateBlock> customBlocks = new ArrayList<>();
	public static final List<GuiProvider> guiBlocks = new ArrayList<>();
	public static final List<BlockRegister> blockRegisters = new ArrayList<>();
	public static final List<BlockTicker> blockTickers = new ArrayList<>();

	// Fun
	public static final RainbowCube RAINBOW_CUBE = registerCustomBlock(new RainbowCube());
	public static final IllusionCube ILLUSION_CUBE = registerCustomBlock(new IllusionCube());

	// Resources
	public static final Cube CUBE = registerCustomBlock(new Cube());

	// Storage
	public static final ClownfishChest CLOWNFISH_CHEST = registerCustomBlock(new ClownfishChest());
	public static final StorageSkullBlockTier1 STORAGE_BLOCK_TIER_1 = registerCustomBlock(new StorageSkullBlockTier1());
	public static final StorageSkullBlockTier2 STORAGE_BLOCK_TIER_2 = registerCustomBlock(new StorageSkullBlockTier2());
	public static final StorageSkullBlockTier3 STORAGE_BLOCK_TIER_3 = registerCustomBlock(new StorageSkullBlockTier3());
	public static final StorageSkullBlockTier4 STORAGE_BLOCK_TIER_4 = registerCustomBlock(new StorageSkullBlockTier4());
	public static final ClownfishStorageSkullBlock CLOWNFISG_STORAGE_BLOCK_TIER_4 = registerCustomBlock(new ClownfishStorageSkullBlock());
	public static final StorageConnector STORAGE_CONNECTOR = registerCustomBlock(new StorageConnector());
	public static final StorageMonitor STORAGE_MONITOR = registerCustomBlock(new StorageMonitor());

	// Electric Components
	public static final CopperCoil COPPER_COIL = registerCustomBlock(new CopperCoil());
	public static final Electromagnet ELECTROMAGNET = registerCustomBlock(new Electromagnet());
	public static final Motor MOTOR = registerCustomBlock(new Motor());
	public static final Accumulator ACCUMULATOR = registerCustomBlock(new Accumulator());
	public static final LED LED = registerCustomBlock(new LED());

	// Machines
	public static final MachineCasing MACHINE_CASING = registerCustomBlock(new MachineCasing());
	public static final AdvancedMachineCasing ADVANCED_MACHINE_CASING = registerCustomBlock(new AdvancedMachineCasing());
	public static final ImprovedMachineCasing IMPROVED_MACHINE_CASING = registerCustomBlock(new ImprovedMachineCasing());
	public static final PerfectedMachineCasing PERFECTED_MACHINE_CASING = registerCustomBlock(new PerfectedMachineCasing());
	public static final CobblestoneGenerator COBBLESTONE_GENERATOR = registerCustomBlock(new CobblestoneGenerator());
	public static final ItemAbsorber ITEM_ABSORBER = registerCustomBlock(new ItemAbsorber());
	public static final ExperienceAbsorber EXPERIENCE_ABSORBER = registerCustomBlock(new ExperienceAbsorber());
	public static final Farmer FARMER = registerCustomBlock(new Farmer());
	public static final MobGrinder MOB_GRINDER = registerCustomBlock(new MobGrinder());
	public static final BlockBreaker BLOCK_BREAKER = registerCustomBlock(new BlockBreaker());
	public static final Quarry QUARRY = registerCustomBlock(new Quarry());
	public static final ChunkLoader CHUNK_LOADER = registerCustomBlock(new ChunkLoader());
	public static final DyeFabricator DYE_FABRICATOR = registerCustomBlock(new DyeFabricator());
	public static final Crusher CRUSHER = registerCustomBlock(new Crusher());
	public static final AdvancedCrusher ADVANCED_CRUSHER = registerCustomBlock(new AdvancedCrusher());
	public static final ImprovedCrusher IMPROVED_CRUSHER = registerCustomBlock(new ImprovedCrusher());
	public static final PerfectedCrusher PERFECTED_CRUSHER = registerCustomBlock(new PerfectedCrusher());
	public static final Smeltery SMELTERY = registerCustomBlock(new Smeltery());
	public static final AdvancedSmeltery ADVANCED_SMELTERY = registerCustomBlock(new AdvancedSmeltery());
	public static final ImprovedSmeltery IMPROVED_SMELTERY = registerCustomBlock(new ImprovedSmeltery());
	public static final PerfectedSmeltery PERFECTED_SMELTERY = registerCustomBlock(new PerfectedSmeltery());
	public static final Compressor COMPRESSOR = registerCustomBlock(new Compressor());
	public static final AdvancedCompressor ADVANCED_COMPRESSOR = registerCustomBlock(new AdvancedCompressor());
	public static final ImprovedCompressor IMPROVED_COMPRESSOR = registerCustomBlock(new ImprovedCompressor());
	public static final CarbonPress CARBON_PRESS = registerCustomBlock(new CarbonPress());
	public static final AdvancedCarbonPress ADVANCED_CARBON_PRESS = registerCustomBlock(new AdvancedCarbonPress());
	public static final ImprovedCarbonPress IMPROVED_CARBON_PRESS = registerCustomBlock(new ImprovedCarbonPress());

	// Redstone
	public static final WirelessRedstoneTransmitter WIRELESS_REDSTONE_TRANSMITTER = registerCustomBlock(new WirelessRedstoneTransmitter());
	public static final WirelessRedstoneReceiver WIRELESS_REDSTONE_RECEIVER = registerCustomBlock(new WirelessRedstoneReceiver());

	public static <T extends AbstractTileStateBlock> T registerCustomBlock(T block) {
		customBlocks.add(block);
		if(block instanceof GuiProvider) {
			guiBlocks.add((GuiProvider) block);
		}
		if(block instanceof BlockRegister) {
			blockRegisters.add((BlockRegister) block);
		}
		if(block instanceof BlockTicker) {
			blockTickers.add((BlockTicker) block);
		}
		return block;
	}


	public static Stream<Listener> getCustomListeners() {
		return customBlocks.stream().map(AbstractTileStateBlock::getCustomListener).filter(listener -> listener != null);
	}


	public static AbstractMachine getMachineFromStack(ItemStack stack) {
		if(!AbstractMachine.isMachine(stack.getItemMeta()))
			return null;
		for(BlockTicker ticker : blockTickers)
			if(ticker instanceof AbstractMachine && ((AbstractMachine) ticker).isInstanceOf(stack.getItemMeta()))
				return (AbstractMachine) ticker;
		return null;
	}

}
