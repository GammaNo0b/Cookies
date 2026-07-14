
package me.gamma.cookies.object.block.network.fluid;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.fluid.WasteBarrel;



public class WasteBarrelBlock extends AbstractCustomTileBlock<WasteBarrelBlock, WasteBarrel> {

	@Override
	public String getIdentifier() {
		return "waste_barrel";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.WASTE_BARREL;
	}


	@Override
	public WasteBarrelBlock castCustomBlock() {
		return this;
	}


	@Override
	public WasteBarrel createNewTileEntity(Block block) {
		return new WasteBarrel(this, block);
	}

}
