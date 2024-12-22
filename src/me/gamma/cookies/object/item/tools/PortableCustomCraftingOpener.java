
package me.gamma.cookies.object.item.tools;


import org.bukkit.entity.Player;

import me.gamma.cookies.object.recipe.RecipeType;



public class PortableCustomCraftingOpener extends PortableInventoryOpener {

	private final String texture;
	private final RecipeType type;

	public PortableCustomCraftingOpener(String texture, RecipeType type) {
		this.texture = texture;
		this.type = type;
	}


	@Override
	protected void openInventory(Player player) {
		this.type.open(player);
	}


	@Override
	public String getIdentifier() {
		return this.type.getName().replaceAll("§[0-9a-f]", "").toLowerCase().replace(' ', '_');
	}


	@Override
	public String getTitle() {
		return "§5Portable " + this.type.getName();
	}


	@Override
	protected String getBlockTexture() {
		return this.texture;
	}

}
