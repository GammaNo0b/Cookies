
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.AerialExtractorBlock;



public class AerialExtractor extends AbstractCraftingMachine<AerialExtractor, AerialExtractorBlock> {

	public AerialExtractor(AerialExtractorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.IRON_BARS;
	}


	@Override
	public AerialExtractor castTileEntity() {
		return this;
	}

}
