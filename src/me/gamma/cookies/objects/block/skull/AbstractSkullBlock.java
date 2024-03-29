
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.util.Utilities;



public abstract class AbstractSkullBlock extends AbstractTileStateBlock {

	public abstract String getBlockTexture();


	@Override
	protected Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack itemStack = super.createDefaultItemStack();
		Utilities.setSkullTexture(itemStack, this.getBlockTexture());
		return itemStack;
	}

}
