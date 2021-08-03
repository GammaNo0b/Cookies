package me.gamma.cookies.objects.item.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class NoobSword extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "noob_sword";
	}


	@Override
	public String getDisplayName() {
		return "§dNoob Sword";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Only for the biggest Noobs!");
	}


	@Override
	public Material getMaterial() {
		return Material.STONE_SWORD;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.WEAPONS, RecipeType.CUSTOM);
		recipe.setShape(" C ", " D ", " S ");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('D', Material.LAPIS_LAZULI);
		recipe.setIngredient('C', Material.CLAY_BALL);
		return recipe;
	}
	
	@Override
	public void onEntityDamage(Player player, ItemStack stack, Entity damaged, EntityDamageByEntityEvent event) {
		if(new Random().nextDouble() < 0.4) {
			player.damage(event.getDamage());
			player.sendMessage("§cYou hurted yourself!");
			event.setDamage(0.0D);
		}
	}

}
