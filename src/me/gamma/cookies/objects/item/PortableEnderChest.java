package me.gamma.cookies.objects.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;


public class PortableEnderChest extends PortableInventoryOpener {

	@Override
	protected void openInventory(Player player) {
		player.openInventory(player.getEnderChest());
	}


	@Override
	protected String getBlockTexture() {
		return HeadTextures.ENDER_CHEST;
	}


	@Override
	public String getIdentifier() {
		return "portable_ender_chest";
	}


	@Override
	public String getDisplayName() {
		return "§3Portable Ender Chest";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("PEP", "GCG", "PYP");
		recipe.setIngredient('P', Material.ENDER_EYE);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('C', Material.ENDER_CHEST);
		recipe.setIngredient('Y', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
		return recipe;
	}

}
