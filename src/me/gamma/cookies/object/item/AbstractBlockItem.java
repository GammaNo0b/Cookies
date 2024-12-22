
package me.gamma.cookies.object.item;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.property.PropertyBuilder;



public abstract class AbstractBlockItem<B extends AbstractCustomBlock> extends AbstractCustomItem {

	protected final B block;

	public AbstractBlockItem(B block) {
		this.block = block;

		this.block.setMainDrop(this);
	}


	/**
	 * Returns the block of this item.
	 * 
	 * @return the block
	 */
	public B getBlock() {
		return this.block;
	}


	@Override
	public String getIdentifier() {
		return this.block.getIdentifier();
	}


	@Override
	public Material getMaterial() {
		return this.block.getMaterial();
	}


	@Override
	protected String getBlockTexture() {
		return this.block.getBlockTexture();
	}


	@Override
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return this.block.buildBlockItemProperties(super.buildItemProperties(builder));
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		/*
		 * Block target = BlockUtils.getBlockToPlace(block, event.getBlockFace()); if(target != null) this.block.place(player, stack, target);
		 */
		return false;
	}

}
