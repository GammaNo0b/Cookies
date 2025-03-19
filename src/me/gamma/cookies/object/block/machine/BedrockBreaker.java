
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class BedrockBreaker extends AbstractItemGenerationMachine {

	public static final IntegerProperty PROCESSING_DUSTS = new IntegerProperty("processingdusts");

	private final Random random = new Random();

	private int miningDuration;
	private int minBedrockDusts;
	private int maxBedrockDusts;

	public BedrockBreaker() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.miningDuration = config.getInt("miningDuration", 1200);
		this.minBedrockDusts = config.getInt("minBedrockDusts", 1);
		this.maxBedrockDusts = config.getInt("maxBedrockDusts", 1);
	}


	@Override
	public String getTitle() {
		return "§dBedrock Breaker";
	}


	@Override
	public String getMachineRegistryName() {
		return "bedrock_breaker";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.BEDROCK_BREAKER;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.NETHERITE_PICKAXE;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(PROCESSING_DUSTS);
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 24 };
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 38;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 29;
	}


	@Override
	protected int getUpgradeSlot() {
		return 11;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 2;
	}


	@Override
	protected int getProgressSlot() {
		return 20;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillBorder(gui, filler);
		for(int i : ArrayUtils.array(13, 22, 31))
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(10, 12, 19, 21, 28, 30))
			gui.setItem(i, filler);
		for(int i : ArrayUtils.array(14, 15, 16, 23, 25, 32, 33, 34))
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	protected int createNextProcess(TileState block) {
		Block down = block.getBlock().getRelative(BlockFace.DOWN);
		if(down.getType() != Material.BEDROCK)
			return 0;

		down.setType(Material.AIR);
		PROCESSING_DUSTS.store(block, this.random.nextInt(this.minBedrockDusts, this.maxBedrockDusts + 1));
		return this.miningDuration;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		ItemStack dust = new ItemBuilder(Items.BEDROCK_DUST).setAmount(PROCESSING_DUSTS.fetch(block)).build();
		ArrayList<ItemStack> items = new ArrayList<>();
		items.add(dust);
		boolean stored = this.storeOutputs(block, items);
		int rest = stored || items.isEmpty() ? 0 : items.get(0).getAmount();
		PROCESSING_DUSTS.store(block, rest);

		while(this.tryPushItems(block));

		return stored;
	}

}
