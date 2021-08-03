
package me.gamma.cookies.objects.item.tools;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class SlimeSling extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "slime_sling";
	}


	@Override
	public String getDisplayName() {
		return "§aSlime Sling";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Throws you into the air!");
	}


	@Override
	public Material getMaterial() {
		return Material.TRIDENT;
	}
	
	
	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.SLIME_SLING;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.TOOLS, RecipeType.CUSTOM);
		recipe.setShape("SBS", "O O", " O ");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('B', Material.SLIME_BLOCK);
		recipe.setIngredient('O', Material.SLIME_BALL);
		return recipe;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {
			
			@EventHandler
			public void onTridentThrow(ProjectileLaunchEvent event) {
				if(event.getEntity() instanceof Trident && event.getEntity().getShooter() instanceof Player && isInstanceOf(((Player) event.getEntity().getShooter()).getInventory().getItemInMainHand())) {
					Player player = (Player) event.getEntity().getShooter();
					event.setCancelled(true);
					if(((Entity) player).isOnGround()) {
						RayTraceResult result = player.rayTraceBlocks(5.0D);
						if(result != null && result.getHitBlock() != null && result.getHitBlock().getType().isSolid()) {
							player.setVelocity(player.getEyeLocation().toVector().subtract(result.getHitPosition()).normalize().multiply(10.0D).multiply(new Vector(1.0D, 0.3D, 1.0D)));
						}
					}
				}
			}

		};
	}

}
