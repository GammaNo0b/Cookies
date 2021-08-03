
package me.gamma.cookies.objects.block.skull;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.block.BlockRegister;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class MonsterAttractor extends AbstractSkullBlock implements Switchable, BlockRegister {

	public static final double MAX_ATTRACTION_DISTANCE_SQUARED = 4096.0D;

	private final HashSet<Location> locations = new HashSet<>();

	public MonsterAttractor() {
		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MONSTER_SPAWNER;
	}


	@Override
	public String getRegistryName() {
		return "monster_attractor";
	}


	@Override
	public String getDisplayName() {
		return "§2Monster Attractor";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("SSC");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('C', Material.COBBLESTONE);
		return recipe;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		this.locations.add(block.getLocation());
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		this.locations.remove(block.getLocation());
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onMonsterSpawn(CreatureSpawnEvent event) {
				LivingEntity entity = event.getEntity();
				if(event.getSpawnReason() != SpawnReason.NATURAL)
					return;
				if(entity instanceof Monster || entity instanceof Slime) {
					for(Location location : MonsterAttractor.this.locations) {
						if(location.getWorld().equals(event.getLocation().getWorld())) {
							if(MonsterAttractor.this.isBlockPowered(location.getBlock())) {
								if(location.distanceSquared(event.getLocation()) <= MAX_ATTRACTION_DISTANCE_SQUARED) {
									entity.teleport(location.clone().add(0.5D, 0.5D, 0.5D));
									break;
								}
							}
						}
					}
				}
			}

		};
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}

}
