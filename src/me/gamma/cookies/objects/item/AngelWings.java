package me.gamma.cookies.objects.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;


public class AngelWings extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "angel_wings";
	}


	@Override
	public String getDisplayName() {
		return "§bAngel's Wings";
	}
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Fly like in Creative!");
	}


	@Override
	public Material getMaterial() {
		return Material.ELYTRA;
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.ANGEL_WINGS;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("G G", "GEG", "DND");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('E', Material.ELYTRA);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('N', Material.NETHER_STAR);
		return recipe;
	}
	
	@Override
	public Listener getCustomListener() {
		return new Listener() {
			
			@EventHandler
			public void onChestEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == ArmorType.CHESTPLATE) {
					if(isInstanceOf(event.getOldArmor()))
						event.getPlayer().setAllowFlight(false);
					if(isInstanceOf(event.getNewArmor()))
						event.getPlayer().setAllowFlight(true);
				}
			}
			
			@EventHandler
			public void onDamage(EntityDamageEvent event) {
				event.setCancelled(event.getCause() == DamageCause.FLY_INTO_WALL && event.getEntity() instanceof Player && isInstanceOf(ArmorType.CHESTPLATE.getArmor(((Player) event.getEntity()).getInventory())));
			}
			
		};
	}

}
