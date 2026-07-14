
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;

import me.gamma.cookies.object.block.machine.CompressorBlock;



public class Compressor extends AbstractCraftingMachine<Compressor, CompressorBlock> {

	public Compressor(CompressorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.PISTON;
	}


	@Override
	public Compressor castTileEntity() {
		return this;
	}

}
