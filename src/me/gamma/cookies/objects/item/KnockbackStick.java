package me.gamma.cookies.objects.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class KnockbackStick extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "knockback_stick";
	}


	@Override
	public String getDisplayName() {
		return "§dKnockback Stick";
	}


	@Override
	public Material getMaterial() {
		return Material.STICK;
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Knocks your enemies far away.");
	}
	
	
	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.WEAPONS, RecipeType.CUSTOM);
		recipe.setShape("W", "L", "S");
		recipe.setIngredient('W', Material.OAK_WOOD);
		recipe.setIngredient('L', Material.OAK_LOG);
		recipe.setIngredient('S', Material.STICK);
		return recipe;
	}

}
