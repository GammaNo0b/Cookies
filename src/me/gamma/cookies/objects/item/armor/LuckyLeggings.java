
package me.gamma.cookies.objects.item.armor;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;



public class LuckyLeggings extends AbstractCustomArmorItem {

	private int tier;
	private String name;
	private ItemStack previous;

	public LuckyLeggings(int tier, ItemStack previous) {
		this.tier = tier;
		this.name = "§2Lucky Leggings §a" + Utilities.romanNumber(this.tier);
		this.previous = previous.clone();
	}


	@Override
	public String getRegistryName() {
		return "lucky_leggings_" + this.tier;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.LEGGINGS;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("EPE", "L L", "B B");
		recipe.setIngredient('E', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('B', Material.LAPIS_BLOCK);
		recipe.setIngredient('P', this.previous);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(51, 153, 0));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onPlayerArmorUnequipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		player.removePotionEffect(PotionEffectType.LUCK);
	}


	@Override
	public void onPlayerArmorEquipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, this.tier - 1, false, false));
	}

}
