
package me.gamma.cookies.object.block.machine;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.MobGrinder;
import me.gamma.cookies.util.InventoryUtils;



public class MobGrinderBlock extends AbstractGuiMachineBlock<MobGrinderBlock, MobGrinder> {

	private int frequency;
	private double damage;
	private int maxHits;

	public MobGrinderBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.frequency = config.getInt("frequency", 20);
		this.damage = config.getDouble("damage", 0.0D);
		this.maxHits = config.getInt("maxHits", 1);
	}


	public int getFrequency() {
		return this.frequency;
	}


	public double getDamage() {
		return this.damage;
	}


	public int getMaxHits() {
		return this.maxHits;
	}


	@Override
	public String getTitle() {
		return "§bMob Grinder";
	}


	@Override
	public String getMachineRegistryName() {
		return "mob_grinder";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MOB_GRINDER;
	}


	@Override
	public int rows() {
		return 3;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 11;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 12;
	}


	@Override
	public int getUpgradeSlot() {
		return 14;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 15;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = Bukkit.createInventory(null, this.rows() * 9);
		InventoryUtils.fillLeftRight(gui, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 10; i < 17; i++)
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	public MobGrinderBlock castCustomBlock() {
		return this;
	}


	@Override
	public MobGrinder createNewTileEntity(Block block) {
		return new MobGrinder(this, block);
	}

}
