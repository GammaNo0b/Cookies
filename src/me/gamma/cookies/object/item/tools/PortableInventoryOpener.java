
package me.gamma.cookies.object.item.tools;


import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.item.AbstractCustomItem;



public abstract class PortableInventoryOpener extends AbstractCustomItem {

	protected abstract void openInventory(Player player);


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.openInventory(player);
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.openInventory(player);
		return true;
	}


	@Override
	public boolean onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
		this.openInventory(player);
		return true;
	}

}
