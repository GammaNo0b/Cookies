
package me.gamma.cookies.object.item;


import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;



public interface NonInteractable extends CustomItemHandler {

	@Override
	default boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return true;
	}


	@Override
	default boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return true;
	}


	@Override
	default boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return true;
	}


	@Override
	default boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return true;
	}


	@Override
	default boolean onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
		return true;
	}


	@Override
	default boolean onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		return true;
	}

}
