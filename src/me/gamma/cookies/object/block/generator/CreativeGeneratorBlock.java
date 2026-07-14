
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.generator.CreativeGenerator;



public class CreativeGeneratorBlock extends AbstractGeneratorBlock<CreativeGeneratorBlock, CreativeGenerator> {

	public CreativeGeneratorBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {}


	@Override
	public String getGeneratorRegistryName() {
		return "creative_generator";
	}


	@Override
	public String getTitle() {
		return "§dCreative Generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CREATIVE_GENERATOR;
	}


	@Override
	public int getMaximumEnergyGeneration() {
		return Integer.MAX_VALUE;
	}


	@Override
	public int getInternalCapacity() {
		return Integer.MAX_VALUE;
	}


	@Override
	public CreativeGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public CreativeGenerator createNewTileEntity(Block block) {
		return new CreativeGenerator(this, block);
	}

}
