
package me.gamma.cookies.objects.item.armor;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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



public class TurtleShell extends AbstractCustomArmorItem {

	@Override
	public String getRegistryName() {
		return "turtle_shell";
	}


	@Override
	public String getDisplayName() {
		return "§aTurtle Shell";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Protected like Turtles!");
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.CHESTPLATE;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("S S", "SSS", "SSS");
		recipe.setIngredient('S', Material.SCUTE);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(20, 200, 0));
		meta.addEnchant(Enchantment.DURABILITY, 5, true);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onChestEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == ArmorType.CHESTPLATE) {
					if(isInstanceOf(event.getOldArmor()))
						event.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
					if(isInstanceOf(event.getNewArmor()))
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, false, false));
				}
			}

		};
	}

}
