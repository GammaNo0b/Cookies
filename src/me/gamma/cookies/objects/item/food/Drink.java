
package me.gamma.cookies.objects.item.food;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.PotionMeta;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class Drink extends AbstractCustomItem {

	private String name;
	private String identifier;
	private int hunger;
	private int saturation;
	private ItemStack ingredient;
	private Color color;

	public Drink(String name, String identifier, int hunger, int saturation, ItemStack ingredient, Color color) {
		this.name = name;
		this.identifier = identifier;
		this.hunger = hunger;
		this.saturation = saturation;
		this.ingredient = ingredient;
		this.color = color;
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
		return Material.POTION;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.DRINKS, RecipeType.KITCHEN);
		recipe.setShape("I", "B");
		recipe.setIngredient('I', this.ingredient);
		recipe.setIngredient('B', Material.GLASS_BOTTLE);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		PotionMeta meta = (PotionMeta) stack.getItemMeta();
		meta.setColor(this.color);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		player.setFoodLevel(Math.min(20, player.getFoodLevel() + this.hunger));
		player.setSaturation(Math.min(20F, player.getSaturation() + this.saturation));
	}

}
