
package me.gamma.cookies.objects.item.armor;


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
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class GlowHat extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "glow_hat";
	}


	@Override
	public String getDisplayName() {
		return "§eGlow Hat";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Makes the night brighter.");
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_HELMET;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("LCL", "G G");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('C', Material.PRISMARINE_CRYSTALS);
		recipe.setIngredient('G', Material.GLOWSTONE_DUST);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(232, 255, 0));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onHatEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == ArmorType.HELMET) {
					if(isInstanceOf(event.getOldArmor()))
						event.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
					if(isInstanceOf(event.getNewArmor()))
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
				}
			}

		};
	}

}
