
package me.gamma.cookies.object.multiblock;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.recipe.RecipeType;



public class Kitchen extends MultiBlock {

	public Kitchen() {
		super(RecipeType.KITCHEN.getName());
	}


	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			{
				{
					Material.SMOKER, Material.CRAFTING_TABLE, Material.BOOKSHELF
				}
			}, {
				{
					Material.STONE_PRESSURE_PLATE, Material.POTTED_RED_TULIP, Material.BARREL
				}
			}, {
				{
					Material.BRICK_STAIRS, Material.BRICK_STAIRS, Material.BRICKS
				}
			}
		};
	}


	@Override
	public Vector getTrigger() {
		return new Vector(0, 1, 0);
	}


	@Override
	public void onClick(Player player, Location location, PlayerInteractEvent event) {
		RecipeType.KITCHEN.open(player);
	}

}
