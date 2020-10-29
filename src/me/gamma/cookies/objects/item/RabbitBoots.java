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


public class RabbitBoots extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "rabbit_boots";
	}


	@Override
	public String getDisplayName() {
		return "§6Rabbit Boots";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Jump as high as rabbits!");
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_BOOTS;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("L L", "P P");
		recipe.setIngredient('L', Material.RABBIT_HIDE);
		recipe.setIngredient('P', Material.RABBIT_FOOT);
		return recipe;
	}
	
	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(145, 118, 77));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onFeetEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == ArmorType.BOOTS) {
					if(isInstanceOf(event.getOldArmor()))
						event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
					if(isInstanceOf(event.getNewArmor()))
						event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2, false, false));
				}
			}

		};
	}

}
