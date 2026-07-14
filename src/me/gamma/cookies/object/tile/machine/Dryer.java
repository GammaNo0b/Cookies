
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.DryerBlock;



public class Dryer extends AbstractCraftingMachine<Dryer, DryerBlock> {

	public Dryer(DryerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.FLINT_AND_STEEL;
	}


	@Override
	public Dryer castTileEntity() {
		return this;
	}

}
