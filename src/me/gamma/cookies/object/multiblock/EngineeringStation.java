
package me.gamma.cookies.object.multiblock;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.recipe.RecipeType;



public class EngineeringStation extends MultiBlock {

	public EngineeringStation() {
		super(RecipeType.ENGINEER.getName());
	}


	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			{
				{
					Material.BLAST_FURNACE, Material.CARTOGRAPHY_TABLE, Material.SMITHING_TABLE
				}
			}, {
				{
					Material.STONE_BRICK_WALL, null, null
				}
			}, {
				{
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
	public void onClick(Player player, Location location, PlayerInteractEvent event) {
		RecipeType.ENGINEER.open(player);
	}

}
