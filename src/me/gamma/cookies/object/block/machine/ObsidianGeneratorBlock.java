
package me.gamma.cookies.object.block.machine;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.ObsidianGenerator;
import me.gamma.cookies.util.InventoryUtils;



public class ObsidianGeneratorBlock extends AbstractItemGenerationMachineBlock<ObsidianGeneratorBlock, ObsidianGenerator> {

	private static final int[] output_border_slots = { 14, 15, 16, 17, 23, 26, 32, 35, 41, 42, 43, 44 };

	private int capacity;
	private int obsidianCoolingTime;

	public ObsidianGeneratorBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("lavaCapacity", 4000);
		this.obsidianCoolingTime = config.getInt("obsidianCoolingTime", 1200);
	}


	public int getCapacity() {
		return this.capacity;
	}


	public int getObsidianCoolingTime() {
		return this.obsidianCoolingTime;
	}


	@Override
	public String getMachineRegistryName() {
		return "obsidian_generator";
	}


	@Override
	public String getTitle() {
		return "§eObsidian Generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.OBSIDIAN_GENERATOR;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = Bukkit.createInventory(null, 54, this.getTitle(block));

		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = MachineConstants.INPUT_BORDER_MATERIAL;
		ItemStack filler2 = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 1; i <= 4; i++) {
			int j = 9 * i;
			gui.setItem(j, filler);
			gui.setItem(j + 1, filler2);
			gui.setItem(j + 2, filler2);
			gui.setItem(j + 3, filler);
		}

		filler = MachineConstants.OUTPUT_BORDER_MATERIAL;
		for(int i : output_border_slots)
			gui.setItem(i, filler);

		return gui;
	}


	@Override
	public ObsidianGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public ObsidianGenerator createNewTileEntity(Block block) {
		return new ObsidianGenerator(this, block);
	}

}
