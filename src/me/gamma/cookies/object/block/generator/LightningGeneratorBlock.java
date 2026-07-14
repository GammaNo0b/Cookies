
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.Block;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.generator.LightningGenerator;



public class LightningGeneratorBlock extends AbstractGeneratorBlock<LightningGeneratorBlock, LightningGenerator> {

	public LightningGeneratorBlock() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§dLightning Generator";
	}


	@Override
	protected String getGeneratorRegistryName() {
		return "lightning_generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LIGHTNING_GENERATOR;
	}


	@Override
	public LightningGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public LightningGenerator createNewTileEntity(Block block) {
		return new LightningGenerator(this, block);
	}

}
