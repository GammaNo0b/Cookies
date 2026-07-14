
package me.gamma.cookies.object.block.machine;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.Crafter;
import me.gamma.cookies.util.InventoryUtils;



public class CrafterBlock extends AbstractItemProcessingMachineBlock<CrafterBlock, Crafter> {

	private int patterns;

	public CrafterBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.patterns = config.getInt("patterns", 1);
	}


	@Override
	public String getTitle() {
		return this.tier.getDescription() + " Crafter";
	}


	@Override
	public String getMachineRegistryName() {
		return "crafter";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRAFTER;
	}


	public int getPatterns() {
		return this.patterns;
	}


	@Override
	public int getInputModeSlot() {
		return 23;
	}


	@Override
	public int getOutputModeSlot() {
		return 41;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = Bukkit.createInventory(null, 54, this.getTitle(block));
		ItemStack filler = MachineConstants.BORDER_MATERIAL;
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}
		gui.setItem(41, filler);
		filler = MachineConstants.INPUT_BORDER_MATERIAL;
		gui.setItem(14, filler);
		gui.setItem(23, filler);
		gui.setItem(32, filler);
		filler = MachineConstants.OUTPUT_BORDER_MATERIAL;
		gui.setItem(42, filler);
		gui.setItem(44, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] { 12, 21, 30, 36, 37, 38, 39 })
			gui.setItem(i, filler);
		filler = MachineConstants.FILLER_MATERIAL;
		return gui;
	}


	@Override
	public CrafterBlock castCustomBlock() {
		return this;
	}


	@Override
	public Crafter createNewTileEntity(Block block) {
		return new Crafter(this, block);
	}

}
