
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.CarbonPressBlock;



public class CarbonPress extends AbstractCraftingMachine<CarbonPress, CarbonPressBlock> {

	public CarbonPress(CarbonPressBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PISTON;
	}


	@Override
	public CarbonPress castTileEntity() {
		return this;
	}

}
