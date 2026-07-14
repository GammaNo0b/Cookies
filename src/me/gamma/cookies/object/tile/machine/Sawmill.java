
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.SawmillBlock;



public class Sawmill extends AbstractCraftingMachine<Sawmill, SawmillBlock> {

	public Sawmill(SawmillBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		switch (this.customBlock.getTier()) {
			case BASIC:
				return Material.IRON_AXE;
			case ADVANCED:
				return Material.GOLDEN_AXE;
			case IMPROVED:
				return Material.DIAMOND_AXE;
			case PERFECTED:
				return Material.NETHERITE_AXE;
			default:
				return null;
		}
	}


	@Override
	public Sawmill castTileEntity() {
		return this;
	}

}
