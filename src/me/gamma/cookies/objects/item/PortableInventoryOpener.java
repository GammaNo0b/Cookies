package me.gamma.cookies.objects.item;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public abstract class PortableInventoryOpener extends AbstractSkullItem {
	
	protected abstract void openInventory(Player player);
	
	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.openInventory(player);
		event.setCancelled(true);
	}
	
	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.openInventory(player);
		event.setCancelled(true);
	}

}
