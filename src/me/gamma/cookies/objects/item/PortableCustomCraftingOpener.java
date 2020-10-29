package me.gamma.cookies.objects.item;

import org.bukkit.entity.Player;

import me.gamma.cookies.listeners.CustomCraftingListener;
import me.gamma.cookies.objects.recipe.RecipeType;


public abstract class PortableCustomCraftingOpener extends PortableInventoryOpener {
	
	protected abstract RecipeType getType();

	@Override
	protected void openInventory(Player player) {
		CustomCraftingListener.openCustomCraftingGui(player, this.getType());
	}


	@Override
	public String getIdentifier() {
		return this.getType().getName().replaceAll("§[0-9a-f]", "").toLowerCase().replace(' ', '_');
	}


	@Override
	public String getDisplayName() {
		return "§5Portable " + this.getType().getName();
	}

}
