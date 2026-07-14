
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.DyePressBlock;



public class DyePress extends AbstractCraftingMachine<DyePress, DyePressBlock> {

	public DyePress(DyePressBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.STICKY_PISTON;
	}


	@Override
	public DyePress castTileEntity() {
		return this;
	}

}
