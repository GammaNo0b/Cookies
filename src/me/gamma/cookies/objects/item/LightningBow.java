
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class LightningBow extends AbstractCustomItem {
	
	private static final ByteProperty IS_LIGHTNING_PROJECTILE = ByteProperty.create("islightningprojectile");

	@Override
	public String getIdentifier() {
		return "lightning_bow";
	}


	@Override
	public String getDisplayName() {
		return "§bLightning Bow";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Summons lightnings.");
	}


	@Override
	public Material getMaterial() {
		return Material.BOW;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.WEAPONS, RecipeType.CUSTOM);
		recipe.setShape("SL ", "P B", "SL ");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('P', Material.BLAZE_POWDER);
		recipe.setIngredient('L', Material.END_ROD);
		return recipe;
	}
	
	@Override
	public Listener getCustomListener() {
		return new Listener() {
			
			@EventHandler
			public void onBowShoot(EntityShootBowEvent event) {
				if(event.getBow().getItemMeta().hasLocalizedName() && event.getBow().getItemMeta().getLocalizedName().equals(getIdentifier())) {
					IS_LIGHTNING_PROJECTILE.store(event.getProjectile(), (byte) 1);
				}
			}
			
			@EventHandler
			public void onProjectileCollide(ProjectileHitEvent event) {
				if(IS_LIGHTNING_PROJECTILE.isPropertyOf(event.getEntity())) {
					event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
				}
			}
			
		};
	}

}
