
package me.gamma.cookies.object.multiblock;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.recipe.RecipeType;



public class CustomCraftingTable extends MultiBlock {

	public CustomCraftingTable() {
		super(RecipeType.CUSTOM.getName());
	}


	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			{
				{
					Material.CARTOGRAPHY_TABLE, Material.CRAFTING_TABLE, Material.LOOM, Material.FLETCHING_TABLE
				}
			}, {
				{
					Material.DARK_OAK_FENCE, null, null, Material.BIRCH_FENCE
				}
			}, {
				{
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
	public void onClick(Player player, Location location, PlayerInteractEvent event) {
		RecipeType.CUSTOM.open(player);
	}

}
