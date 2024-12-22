
package me.gamma.cookies.object.item.resources;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.item.CustomItem;



public class ResistorItem extends CustomItem {

	public ResistorItem(String name, Material material) {
		super(name, material);
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return block.getType() == Material.CAKE || super.onBlockRightClick(player, stack, block, event);
	}

}
