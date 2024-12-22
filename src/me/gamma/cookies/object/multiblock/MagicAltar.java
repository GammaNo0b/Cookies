package me.gamma.cookies.object.multiblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.recipe.RecipeType;


public class MagicAltar extends MultiBlock {

	public MagicAltar() {
		super(RecipeType.ALTAR.getName());
	}


	@Override
	public Material[][][] getStructure() {
		return new Material[][][] {
			{
				{
					Material.BOOKSHELF, Material.ENCHANTING_TABLE, Material.CRYING_OBSIDIAN
				}
			}, {
				{
					Material.SOUL_TORCH, null, Material.PURPLE_CANDLE
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
		RecipeType.ALTAR.open(player);
	}

}
