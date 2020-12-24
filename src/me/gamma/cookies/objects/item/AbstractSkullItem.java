
package me.gamma.cookies.objects.item;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.Utilities;



public abstract class AbstractSkullItem extends AbstractCustomItem {

	protected abstract String getBlockTexture();


	@Override
	public Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		Utilities.setSkullTexture(stack, this.getBlockTexture());
		return stack;
	}
	
	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		event.setCancelled(true);
	}
	
	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
	}

}
