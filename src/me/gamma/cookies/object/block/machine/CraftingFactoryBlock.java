
package me.gamma.cookies.object.block.machine;


import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.CraftingFactory;



public class CraftingFactoryBlock extends AbstractProcessingMachineBlock<CraftingFactoryBlock, CraftingFactory> {

	private int maxSteps;

	public CraftingFactoryBlock(MachineTier tier) {
		super(tier);
	}


	public int getMaxSteps() {
		return this.maxSteps;
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.maxSteps = config.getInt("maxSteps", 1);
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Crafting Factory";
	}


	@Override
	public String getMachineRegistryName() {
		return "crafting_factory";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRAFTING_FACTORY;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = Bukkit.createInventory(null, 54, this.getTitle(block));
		ItemStack filler = MachineConstants.BORDER_MATERIAL;
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}
		filler = MachineConstants.INPUT_BORDER_MATERIAL;
		for(int i = 1; i <= 4; i++)
			gui.setItem(i * 9 + 1, filler);
		gui.setItem(11, filler);
		gui.setItem(38, filler);
		filler = MachineConstants.OUTPUT_BORDER_MATERIAL;
		for(int i = 1; i <= 4; i++)
			gui.setItem(i * 9 + 7, filler);
		gui.setItem(15, filler);
		gui.setItem(42, filler);
		filler = MachineConstants.FILLER_MATERIAL;
		for(int i = 1; i <= 4; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		gui.setItem(12, filler);
		gui.setItem(14, filler);
		gui.setItem(39, filler);
		gui.setItem(41, filler);

		return gui;
	}


	@Override
	public int getProgressSlot() {
		return 31;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 40;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 49;
	}


	@Override
	public CraftingFactoryBlock castCustomBlock() {
		return this;
	}


	@Override
	public CraftingFactory createNewTileEntity(Block block) {
		return new CraftingFactory(this, block);
	}

}
