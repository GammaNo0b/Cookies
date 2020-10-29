package me.gamma.cookies.objects.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class ScubaHelmet extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "scuba_helmet";
	}


	@Override
	public String getDisplayName() {
		return "§6Scuba Helmet";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Usefull for Underwater Explorations!");
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_HELMET;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("OGO", "L L");
		recipe.setIngredient('O', Material.ORANGE_DYE);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('L', Material.LEATHER);
		return recipe;
	}
	
	
	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.ORANGE);
		stack.setItemMeta(meta);
		return stack;
	}
	
	
	@Override
	public Listener getCustomListener() {
		return new Listener() {
			
			 @EventHandler
			 public void onHelmetEquip(PlayerArmorEquipEvent event) {
				 if(event.getType() == ArmorType.HELMET) {
					 if(isInstanceOf(event.getOldArmor()))
						 event.getPlayer().removePotionEffect(PotionEffectType.WATER_BREATHING);
					 if(isInstanceOf(event.getNewArmor()))
						 event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 1, false, false));
				 }
			 }
			
		};
	}

}
