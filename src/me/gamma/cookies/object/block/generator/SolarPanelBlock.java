
package me.gamma.cookies.object.block.generator;


import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.tile.generator.SolarPanel;



public class SolarPanelBlock extends AbstractGuiGeneratorBlock<SolarPanelBlock, SolarPanel> {

	private final String texture;

	public SolarPanelBlock(String texture, MachineTier tier) {
		super(tier);
		this.texture = texture;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Solar Panel";
	}


	@Override
	public int getIdentifierSlot() {
		return 3;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 7;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 1;
	}


	@Override
	public int getUpgradeSlot() {
		return 2;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 0;
	}


	@Override
	public Inventory createGui(Block data) {
		Inventory gui = Bukkit.createInventory(null, InventoryType.DROPPER, this.getTitle(data));
		for(int slot : new int[] { 3, 5, 6, 8 })
			gui.setItem(slot, MachineConstants.FILLER_MATERIAL);
		return gui;
	}


	@Override
	public String getGeneratorRegistryName() {
		return "solar_panel";
	}


	@Override
	public SolarPanelBlock castCustomBlock() {
		return this;
	}


	@Override
	public SolarPanel createNewTileEntity(Block block) {
		return new SolarPanel(this, block);
	}

}
