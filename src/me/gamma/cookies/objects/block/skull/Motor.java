package me.gamma.cookies.objects.block.skull;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;


public class Motor extends AbstractSkullBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.MOTOR;
	}


	@Override
	public String getIdentifier() {
		return "motor";
	}


	@Override
	public String getDisplayName() {
		return "§7Motor";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ELECTRIC_COMPONENTS, RecipeType.ENGINEER);
		recipe.setShape(" M ", "WIW", "MAM");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		recipe.setIngredient('W', CustomItemSetup.COPPER_WIRE.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.ELECTROMAGNET.createDefaultItemStack());
		return recipe;
	}

}
