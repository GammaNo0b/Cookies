
package me.gamma.cookies.objects.item.armor;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class FarmerBoots extends AbstractCustomArmorItem {

	@Override
	public String getRegistryName() {
		return "farmer_boots";
	}


	@Override
	public String getDisplayName() {
		return "§eFarmer's Boots";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Keeps your farmland tilled", "§7when trampling over it.");
	}


	@Override
	public ArmorType getArmorType() {
		return ArmorType.BOOTS;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("L L", "H H");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('H', Material.HAY_BLOCK);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.YELLOW);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onFarmlandTrample(PlayerInteractEvent event) {
				if(event.getAction() == Action.PHYSICAL) {
					Block block = event.getClickedBlock();
					if(block != null && block.getType() == Material.FARMLAND) {
						ItemStack boots = event.getPlayer().getInventory().getBoots();
						event.setCancelled(FarmerBoots.this.isInstanceOf(boots));
					}
				}
			}

		};
	}

}
