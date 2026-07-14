
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.FreezerBlock;



public class Freezer extends AbstractCraftingMachine<Freezer, FreezerBlock> {

	public Freezer(FreezerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PRISMARINE_SHARD;
	}


	@Override
	public Freezer castTileEntity() {
		return this;
	}

}
