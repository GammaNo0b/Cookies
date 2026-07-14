
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.MineralExtractorBlock;



public class MineralExtractor extends AbstractCraftingMachine<MineralExtractor, MineralExtractorBlock> {

	public MineralExtractor(MineralExtractorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public Material getProgressMaterial(double progress) {
		switch (this.customBlock.getTier()) {
			case BASIC:
				return Material.IRON_SHOVEL;
			case ADVANCED:
				return Material.GOLDEN_SHOVEL;
			case IMPROVED:
				return Material.DIAMOND_SHOVEL;
			case PERFECTED:
				return Material.NETHERITE_SHOVEL;
			default:
				return null;
		}
	}


	@Override
	public MineralExtractor castTileEntity() {
		return this;
	}

}
