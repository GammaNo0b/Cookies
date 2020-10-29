
package me.gamma.cookies.objects.block.multi;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.gamma.cookies.listeners.CustomCraftingListener;
import me.gamma.cookies.objects.recipe.RecipeType;



public class CustomCraftingTable extends MultiBlock {

	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			new Material[][] {
				new Material[] {
					Material.CARTOGRAPHY_TABLE, Material.CRAFTING_TABLE, Material.LOOM, Material.FLETCHING_TABLE
				}
			}, new Material[][] {
				new Material[] {
					Material.DARK_OAK_FENCE, Material.AIR, Material.AIR, Material.BIRCH_FENCE
				}
			}, new Material[][] {
				new Material[] {
					Material.SMITHING_TABLE, Material.SMOKER, Material.BLAST_FURNACE, Material.FURNACE
				}
			}
		};
	}


	@Override
	public Vector getTrigger() {
		return new Vector(3, 0, 0);
	}


	@Override
	public void onClick(Player player, Location location) {
		CustomCraftingListener.openCustomCraftingGui(player, RecipeType.CUSTOM);
	}

}
