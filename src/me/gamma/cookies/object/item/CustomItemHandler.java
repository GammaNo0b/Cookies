
package me.gamma.cookies.object.item;


import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.event.PlayerArmorEquipEvent;



public interface CustomItemHandler {

	/**
	 * Get's executed when the player clicks in the air with the right mouse button with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return true;
	}


	/**
	 * Get's executed when the player clicks in the air with the left mouse button with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player clicks a block with the right mouse button with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param block  the clicked block
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return !block.getType().isInteractable() || player.isSneaking();
	}


	/**
	 * Get's executed when the player clicks a block with the left mouse button with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param block  the clicked block
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player breaks a block with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param block  the broken block
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onBlockBreak(Player player, ItemStack stack, Block block, BlockBreakEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player clicks an entity with the right mouse button with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param entity the clicked entity
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
		return player.isSneaking();
	}


	/**
	 * Get's executed when the player damages an entity with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param entity the damaged entity
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onEntityDamage(Player player, ItemStack stack, Entity entity, EntityDamageByEntityEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player kills an entity with an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param entity the killed entity
	 * @param event  the fired event
	 */
	default void onEntityKill(Player player, ItemStack stack, Entity entity, EntityDeathEvent event) {}


	/**
	 * Get's executed when the player consumes an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	default boolean onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player equips an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	@Deprecated
	default boolean onPlayerArmorEquipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		return false;
	}


	/**
	 * Get's executed when the player unequips an instance of the handling item.
	 * 
	 * @param player the player
	 * @param stack  the item
	 * @param event  the fired event
	 * @return whether the event should be cancelled or not
	 */
	@Deprecated
	default boolean onPlayerArmorUnequipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		return false;
	}

}
