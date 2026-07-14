
package me.gamma.cookies.object.tile.generator;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.LightningRod;

import me.gamma.cookies.object.block.generator.LightningGeneratorBlock;



public class LightningGenerator extends AbstractGenerator<LightningGenerator, LightningGeneratorBlock> {

	public LightningGenerator(LightningGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	private Boolean checkForLightningRod() {
		Block above = this.block.getRelative(0, 1, 0);
		if(above.getType() != Material.LIGHTNING_ROD)
			return null;

		if(!(above.getBlockData() instanceof LightningRod rod))
			return null;

		return rod.isPowered();
	}


	@Override
	protected boolean fullfillsGeneratingConditions() {
		Boolean result = this.checkForLightningRod();
		return result != null && result;
	}


	@Override
	public LightningGenerator castTileEntity() {
		return this;
	}

}
