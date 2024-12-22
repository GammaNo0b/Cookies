
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.Block;
import org.bukkit.block.TileState;

import me.gamma.cookies.object.block.machine.MachineTier;



public class SolarPanel extends AbstractGenerator {

	private final String texture;

	public SolarPanel(String texture, MachineTier tier) {
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
	public int getEnergyGeneration(TileState block) {
		return (int) (this.getMaximumEnergyGeneration() * this.getSunlight(block.getBlock()));
	}


	@Override
	protected boolean fullfillsGeneratingConditions(TileState block) {
		return true;
	}


	@Override
	public String getGeneratorRegistryName() {
		return "solar_panel";
	}


	public double getSunlight(Block block) {
		return block.getLightFromSky() / 15.0D;
	}

}
