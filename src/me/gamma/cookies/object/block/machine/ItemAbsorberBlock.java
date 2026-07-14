
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.ItemAbsorber;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;



public class ItemAbsorberBlock extends AbstractGuiMachineBlock<ItemAbsorberBlock, ItemAbsorber> {

	private int frequency;
	private int range;

	public ItemAbsorberBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.frequency = config.getInt("frequency", 20);
		this.range = config.getInt("range", 0);
	}


	public int getFrequency() {
		return this.frequency;
	}


	public int getRange() {
		return this.range;
	}


	@Override
	public String getTitle() {
		return "§bItem Absorber";
	}


	@Override
	public String getMachineRegistryName() {
		return "item_absorber";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_ABSORBER;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 28;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 19;
	}


	@Override
	public int getUpgradeSlot() {
		return 10;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 1;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(9, 11, 18, 20, 27, 29))
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(12, 17, 21, 26, 30, 35))
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	public ItemAbsorberBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemAbsorber createNewTileEntity(Block block) {
		return new ItemAbsorber(this, block);
	}

}
