
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class CactusShirt extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "cactus_shirt";
	}


	@Override
	public String getDisplayName() {
		return "§2Cactus Shirt";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Makes you invulnerable to Cacti.");
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_CHESTPLATE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("G G", "LCL", "LGL");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('C', Material.CACTUS);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.addEnchant(Enchantment.THORNS, 5, true);
		meta.setColor(Color.fromRGB(0, 140, 20));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onBerryDamage(EntityDamageByBlockEvent event) {
				if(event.getCause() == DamageCause.CONTACT) {
					if(event.getDamager().getType() == Material.CACTUS) {
						if(event.getEntity() instanceof Player) {
							Player player = (Player) event.getEntity();
							ItemStack chestplate = player.getInventory().getChestplate();
							if(chestplate != null) {
								event.setCancelled(chestplate.getItemMeta().hasLocalizedName() && chestplate.getItemMeta().getLocalizedName().equals(getIdentifier()));
							}
						}
					}
				}
			}

		};
	}

}
