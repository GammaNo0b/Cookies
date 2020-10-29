
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;



public class CrystalizedEyeOfEnder extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "crystalized_eye_of_ender";
	}


	@Override
	public String getDisplayName() {
		return "§3Crystalized Eye of Ender";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Multi-Use Eye of Ender.");
	}


	@Override
	public Material getMaterial() {
		return Material.ENDER_EYE;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("IPG", "ECE", "GPI");
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('P', Material.ENDER_PEARL);
		recipe.setIngredient('E', Material.ENDER_EYE);
		recipe.setIngredient('C', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
		return recipe;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {
			
			@EventHandler
			public void onEntitySpawn(EntitySpawnEvent event) {
				if(event.getEntity() instanceof EnderSignal) {
					EnderSignal entity = (EnderSignal) event.getEntity();
					if(isInstanceOf(entity.getItem())) {
						Location target = entity.getTargetLocation().clone();
						event.setCancelled(true);
						EnderSignal eye = (EnderSignal) entity.getWorld().spawnEntity(event.getLocation(), EntityType.ENDER_SIGNAL);
						eye.setTargetLocation(target);
						eye.setDropItem(false);
						event.getLocation().getWorld().playSound(event.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 10.0F, 1.0F);
					}
				}
			}

		};
	}

}
