package me.gamma.cookies.objects.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;


public class Cookie extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "cookie";
	}


	@Override
	public String getDisplayName() {
		return Utilities.colorize("Cookie", Utilities.RAINBOW_COLOR_SEQUENCE, 1);
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Gives you usefull Status Effects", "§7when eaten.");
	}


	@Override
	public Material getMaterial() {
		return Material.COOKIE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.FOOD, RecipeType.KITCHEN);
		recipe.setShape("187", "2C6", "345");
		recipe.setIngredient('1', Material.RED_DYE);
		recipe.setIngredient('2', Material.ORANGE_DYE);
		recipe.setIngredient('3', Material.YELLOW_DYE);
		recipe.setIngredient('4', Material.LIME_DYE);
		recipe.setIngredient('5', Material.GREEN_DYE);
		recipe.setIngredient('6', Material.CYAN_DYE);
		recipe.setIngredient('7', Material.BLUE_DYE);
		recipe.setIngredient('8', Material.PURPLE_DYE);
		recipe.setIngredient('C', Material.COOKIE);
		return recipe;
	}
	
	@Override
	public void onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		player.setHealth(20.0D);
		player.setFoodLevel(20);
		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 6000, 3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6000, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 2));
		player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 1));
	}

}
