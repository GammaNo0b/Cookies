
package me.gamma.cookies.objects.item;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.Utilities;



public abstract class AbstractSkullItem extends AbstractCustomItem {

	protected abstract String getBlockTexture();


	@Override
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		Utilities.setSkullTexture(stack, this.getBlockTexture());
		return stack;
	}

}
