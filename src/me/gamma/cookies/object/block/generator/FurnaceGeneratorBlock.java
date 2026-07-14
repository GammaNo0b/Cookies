
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.Block;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.generator.FurnaceGenerator;



public class FurnaceGeneratorBlock extends AbstractGeneratorBlock<FurnaceGeneratorBlock, FurnaceGenerator> {

	public FurnaceGeneratorBlock() {
		super(null);
	}


	@Override
	public String getGeneratorRegistryName() {
		return "furnace_generator";
	}


	@Override
	public String getTitle() {
		return "§fFurnace Generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FURNACE_GENERATOR;
	}


	@Override
	public FurnaceGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public FurnaceGenerator createNewTileEntity(Block block) {
		return new FurnaceGenerator(this, block);
	}

}
