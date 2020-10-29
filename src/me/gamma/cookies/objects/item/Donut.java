
package me.gamma.cookies.objects.item;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class Donut extends AbstractSkullItem implements Food {

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
	public String getIdentifier() {
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


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.eat(player, stack);
		event.setCancelled(true);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.eat(player, stack);
		event.setCancelled(true);
	}


	private void eat(Player player, ItemStack stack) {
		if(player.getFoodLevel() < 20) {
			stack.setAmount(stack.getAmount() - 1);
			player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 10.0F, 1.0F);
			player.setFoodLevel(Math.min(20, player.getFoodLevel() + this.hunger));
			player.setSaturation(Math.min(20F, player.getSaturation() + this.saturation));
		}
	}

}
