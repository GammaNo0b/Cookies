
package me.gamma.cookies.objects.item.food;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class Donut extends EdibleSkullItem {

	private String identifier;
	private String name;
	private int hunger;
	private int saturation;
	private ItemStack donut;
	private ItemStack ingredient;
	private String texture;

	public Donut(String identifier, String name, int hunger, int saturation, ItemStack donut, ItemStack ingredient, String texture) {
		this.identifier = identifier;
		this.name = name;
		this.hunger = hunger;
		this.saturation = saturation;
		this.donut = donut;
		this.ingredient = ingredient;
		this.texture = texture;
	}


	@Override
	protected String getBlockTexture() {
		return this.texture;
	}


	@Override
	public String getRegistryName() {
		return this.identifier;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}


	@Override
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FOOD, RecipeType.KITCHEN);
		recipe.setShape("I", "D");
		recipe.setIngredient('I', this.ingredient);
		recipe.setIngredient('D', this.donut);
		return recipe;
	}


	@Override
	public int getHunger() {
		return this.hunger;
	}


	@Override
	public int getSaturation() {
		return this.saturation;
	}

}
