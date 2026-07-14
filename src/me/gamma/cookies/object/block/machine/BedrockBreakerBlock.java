
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.BedrockBreaker;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;



public class BedrockBreakerBlock extends AbstractItemGenerationMachineBlock<BedrockBreakerBlock, BedrockBreaker> {

	private int miningDuration;
	private int minBedrockDusts;
	private int maxBedrockDusts;

	public BedrockBreakerBlock() {
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
	public BedrockBreakerBlock castCustomBlock() {
		return this;
	}


	@Override
	public BedrockBreaker createNewTileEntity(Block block) {
		return new BedrockBreaker(this, block);
	}


	public int getMiningDuration() {
		return this.miningDuration;
	}


	public int getMinBedrockDusts() {
		return this.minBedrockDusts;
	}


	public int getMaxBedrockDusts() {
		return this.maxBedrockDusts;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 38;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 29;
	}


	@Override
	public int getUpgradeSlot() {
		return 11;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 2;
	}


	@Override
	public int getProgressSlot() {
		return 20;
	}


	@Override
	public Inventory createGui(Block block) {
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

}
