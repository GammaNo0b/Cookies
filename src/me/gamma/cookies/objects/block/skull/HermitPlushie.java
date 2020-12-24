
package me.gamma.cookies.objects.block.skull;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class HermitPlushie extends AbstractSkullBlock {

	private String identifier;
	private String name;
	private String texture;
	private ItemStack wool;
	private ItemStack ingredient1;
	private ItemStack ingredient2;
	private ItemStack ingredient3;

	public HermitPlushie(String identifier, String name, String texture, Material wool, Material ingredient1, Material ingredient2, Material ingredient3) {
		this(identifier, name, texture, new ItemStack(wool), new ItemStack(ingredient1), new ItemStack(ingredient2), new ItemStack(ingredient3));
	}


	public HermitPlushie(String identifier, String name, String texture, ItemStack wool, ItemStack ingredient1, ItemStack ingredient2, ItemStack ingredient3) {
		this.identifier = identifier;
		this.name = name;
		this.texture = texture;
		this.wool = wool;
		this.ingredient1 = ingredient1;
		this.ingredient2 = ingredient2;
		this.ingredient3 = ingredient3;
	}


	@Override
	public String getBlockTexture() {
		return texture;
	}


	@Override
	public String getRegistryName() {
		return identifier;
	}


	@Override
	public String getDisplayName() {
		return name + " Plushie";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.HERMIT_PLUSHIES, RecipeType.CUSTOM);
		recipe.setShape("1W2", " 3 ");
		recipe.setIngredient('W', wool);
		recipe.setIngredient('1', ingredient1);
		recipe.setIngredient('2', ingredient2);
		recipe.setIngredient('3', ingredient3);
		return recipe;
	}

}
