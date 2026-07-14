
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.SmelteryBlock;



public class Smeltery extends AbstractCraftingMachine<Smeltery, SmelteryBlock> {

	public Smeltery(SmelteryBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public Material getProgressMaterial(double progress) {
		return Material.FLINT_AND_STEEL;
	}


	@Override
	public Smeltery castTileEntity() {
		return this;
	}

}
