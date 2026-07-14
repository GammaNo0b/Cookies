
package me.gamma.cookies.object.tile.generator;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.generator.SolarPanelBlock;
import me.gamma.cookies.util.ItemBuilder;



public class SolarPanel extends AbstractGuiGenerator<SolarPanel, SolarPanelBlock> {

	private static final int INFO_SLOT = 4;

	public SolarPanel(SolarPanelBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		this.updateSun();
	}


	@Override
	public void tick() {
		this.updateSun();
		super.tick();
	}


	@Override
	protected boolean fullfillsGeneratingConditions() {
		return this.block.getLightFromSky() > 0;
	}


	@Override
	public int getEnergyGeneration() {
		return (int) Math.round(this.customBlock.getMaximumEnergyGeneration() * this.getSunlight());
	}


	public double getSunlight() {
		double light = Math.sin(this.block.getWorld().getTime() / 12000.0D * Math.PI) + 0.25D;
		light = Math.clamp(light, 0.0D, 1.0D);
		return light * this.block.getLightFromSky() / 15.0D;
	}


	/**
	 * Updates information about the sun and energy currently generating.
	 * 
	 * @param block the block
	 */
	protected void updateSun() {
		long time = block.getWorld().getTime();
		long h = time / 1000;
		long m = time - h * 1000;
		long hour = (h + 8) % 24;
		long minutes = m * 60 / 1000;

		double sunlight = this.getSunlight();
		int maxEnergyGeneration = this.customBlock.getMaximumEnergyGeneration();
		int energyGeneration = this.getEnergyGeneration();

		ItemBuilder builder = new ItemBuilder(Material.CLOCK);
		builder.setName(String.format("§3Time: §b%02d§8:§b%02d§b", hour, minutes));
		builder.addLore(String.format("  §eSunlight: §6%.0f%%", sunlight * 100));
		builder.addLore(String.format("  §8Generating: §7%d §8/ §7%d §7CC/t", energyGeneration, maxEnergyGeneration));
		this.getInventory().setItem(INFO_SLOT, builder.build());
	}


	@Override
	public SolarPanel castTileEntity() {
		return this;
	}

}
