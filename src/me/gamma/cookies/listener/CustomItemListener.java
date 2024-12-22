
package me.gamma.cookies.listener;


import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.PlayerRegister;



public class CustomItemListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		for(PlayerRegister register : Items.getPlayerRegister())
			if(register.shouldRegisterPlayer(player))
				register.registerPlayer(player);
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		AbstractCustomItem item = Items.getCustomItemFromStack(stack);
		if(item != null)
			if(item.onBlockBreak(event.getPlayer(), stack, event.getBlock(), event))
				event.setCancelled(true);
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getHand() != EquipmentSlot.HAND)
			return;

		// TODO
		/*
		 * Figure out how exactly this is working (catch duplicate event)
		 * 
		 * if(event.useInteractedBlock() == Result.DENY) return;
		 */

		ItemStack stack = event.getItem();
		AbstractCustomItem item = Items.getCustomItemFromStack(stack);
		if(item != null) {
			Player player = event.getPlayer();
			Action action = event.getAction();
			if(action == Action.RIGHT_CLICK_AIR) {
				if(item.onAirRightClick(player, stack, event))
					event.setCancelled(true);
			} else if(action == Action.RIGHT_CLICK_BLOCK) {
				if(item.onBlockRightClick(player, stack, event.getClickedBlock(), event))
					event.setCancelled(true);
			} else if(action == Action.LEFT_CLICK_AIR) {
				if(item.onAirLeftClick(player, stack, event))
					event.setCancelled(true);
			} else if(action == Action.LEFT_CLICK_BLOCK) {
				if(item.onBlockLeftClick(player, stack, event.getClickedBlock(), event))
					event.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		AbstractCustomItem item = Items.getCustomItemFromStack(stack);
		if(item != null)
			item.onEntityRightClick(event.getPlayer(), stack, event.getRightClicked(), event);
	}


	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player player) {
			ItemStack stack = player.getInventory().getItemInMainHand();
			AbstractCustomItem item = Items.getCustomItemFromStack(stack);
			if(item != null)
				if(item.onEntityDamage(player, stack, event.getEntity(), event))
					event.setCancelled(true);
		}
	}


	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity killed = event.getEntity();
		Player killer = killed.getKiller();
		if(killer != null) {
			ItemStack stack = killer.getInventory().getItemInMainHand();
			AbstractCustomItem item = Items.getCustomItemFromStack(stack);
			if(item != null)
				item.onEntityKill(killer, stack, killed, event);
		}
	}


	@EventHandler
	public void onPlayerConsumesItem(PlayerItemConsumeEvent event) {
		AbstractCustomItem item = Items.getCustomItemFromStack(event.getItem());
		if(item != null)
			if(item.onPlayerConsumesItem(event.getPlayer(), event.getItem(), event))
				event.setCancelled(true);
	}


	@EventHandler
	@Deprecated
	public void onPlayerArmorEquip(PlayerArmorEquipEvent event) {
		AbstractCustomItem oldItem = Items.getCustomItemFromStack(event.getOldArmor());
		if(oldItem != null)
			if(oldItem.onPlayerArmorUnequipItem(event.getPlayer(), event.getOldArmor(), event))
				event.setCancelled(true);
		AbstractCustomItem newItem = Items.getCustomItemFromStack(event.getNewArmor());
		if(newItem != null)
			if(newItem.onPlayerArmorEquipItem(event.getPlayer(), event.getNewArmor(), event))
				event.setCancelled(true);
	}

}
