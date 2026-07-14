
package me.gamma.cookies.object.block.network.item;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.item.EnderAccessor;



public class EnderAccessorBlock extends AbstractCustomTileBlock<EnderAccessorBlock, EnderAccessor> {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ENDER_ACCESSOR;
	}


	@Override
	public String getIdentifier() {
		return "ender_accessor";
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		return player != null && super.canPlace(player, block);
	}


	@Override
	public EnderAccessorBlock castCustomBlock() {
		return this;
	}


	@Override
	public EnderAccessor createNewTileEntity(Block block) {
		return new EnderAccessor(this, block);
	}

}
