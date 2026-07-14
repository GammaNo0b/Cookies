
package me.gamma.cookies.object.block.machine;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.Quarry;
import me.gamma.cookies.util.InventoryUtils;



public class QuarryBlock extends AbstractItemProcessingMachineBlock<QuarryBlock, Quarry> {

	private List<String> dimensions;
	private boolean blacklisted;

	public QuarryBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.dimensions = config.getStringList("dimensions");
		this.blacklisted = config.getBoolean("blacklisted", true);
	}


	@Override
	public String getTitle() {
		return "§dQuarry";
	}


	@Override
	public String getMachineRegistryName() {
		return "quarry";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.QUARRY;
	}


	public boolean isValidDimension(World world) {
		return this.dimensions.contains(world.getName()) != this.blacklisted;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public int getInputModeSlot() {
		return 38;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillTopBottom(gui, filler);
		gui.setItem(9, filler);
		gui.setItem(27, filler);
		for(int i : new int[] { 12, 21, 28, 29, 30 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : new int[] { 14, 23, 32 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public QuarryBlock castCustomBlock() {
		return this;
	}


	@Override
	public Quarry createNewTileEntity(Block block) {
		return new Quarry(this, block);
	}

}
