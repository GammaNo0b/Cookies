package me.gamma.cookies.objects.block.multi;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.gamma.cookies.listeners.CustomCraftingListener;
import me.gamma.cookies.objects.recipe.RecipeType;


public class Kitchen extends MultiBlock {

	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			new Material[][] {
				new Material[] {
					Material.SMOKER, Material.CRAFTING_TABLE, Material.BOOKSHELF
				}
			}, new Material[][] {
				new Material[] {
					Material.STONE_PRESSURE_PLATE, Material.POTTED_RED_TULIP, Material.BARREL
				}
			}, new Material[][] {
				new Material[] {
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
	public void onClick(Player player, Location location) {
		CustomCraftingListener.openCustomCraftingGui(player, RecipeType.KITCHEN);
	}

}
