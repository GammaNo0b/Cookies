
package me.gamma.cookies.object.tile.generator;


import org.bukkit.block.Block;
import org.bukkit.block.data.type.Furnace;

import me.gamma.cookies.object.block.generator.FurnaceGeneratorBlock;



public class FurnaceGenerator extends AbstractGenerator<FurnaceGenerator, FurnaceGeneratorBlock> {

	public FurnaceGenerator(FurnaceGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected boolean fullfillsGeneratingConditions() {
		Block down = this.block.getRelative(0, -1, 0);
		return down.getBlockData() instanceof Furnace furnace && furnace.isLit();
	}


	@Override
	public FurnaceGenerator castTileEntity() {
		return this;
	}

}
