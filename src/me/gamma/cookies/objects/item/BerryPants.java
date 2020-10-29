
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
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



public class BerryPants extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "berry_pants";
	}


	@Override
	public String getDisplayName() {
		return "§cBerry Pants";
	}
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Prevents you from Berries poking you.");
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_LEGGINGS;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(128, 8, 8));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("LLL", "S S", "B B");
		recipe.setIngredient('B', Material.SWEET_BERRIES);
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('L', Material.LEATHER);
		return recipe;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onBerryDamage(EntityDamageByBlockEvent event) {
				if(event.getCause() == DamageCause.CONTACT) {
					if(event.getDamager().getType() == Material.SWEET_BERRY_BUSH) {
						if(event.getEntity() instanceof Player) {
							Player player = (Player) event.getEntity();
							ItemStack leggings = player.getInventory().getLeggings();
							if(leggings != null) {
								event.setCancelled(leggings.getItemMeta().hasLocalizedName() && leggings.getItemMeta().getLocalizedName().equals(getIdentifier()));
							}
						}
					}
				}
			}

		};
	}

}
