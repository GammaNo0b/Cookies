
package me.gamma.cookies.objects.block.multi;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.gamma.cookies.listeners.CustomCraftingListener;
import me.gamma.cookies.objects.recipe.RecipeType;



public class EngineeringStation extends MultiBlock {

	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			new Material[][] {
				new Material[] {
					Material.BLAST_FURNACE, Material.CARTOGRAPHY_TABLE, Material.SMITHING_TABLE
				}
			}, new Material[][] {
				new Material[] {
					Material.STONE_BRICK_WALL, null, null
				}
			}, new Material[][] {
				new Material[] {
					Material.NETHER_BRICK_FENCE, null, null
				}
			}
		};
	}


	@Override
	public Vector getTrigger() {
		return new Vector(1, 0, 0);
	}


	@Override
	public void onClick(Player player, Location location) {
		CustomCraftingListener.openCustomCraftingGui(player, RecipeType.ENGINEER);
	}

}
