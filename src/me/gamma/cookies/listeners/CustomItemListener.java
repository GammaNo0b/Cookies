
package me.gamma.cookies.listeners;


import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.item.PlayerRegister;
import me.gamma.cookies.setup.CustomItemSetup;



public class CustomItemListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		if(stack != null) {
			AbstractCustomItem item = CustomItemSetup.getItemByStack(stack);
			if(item != null) {
				item.onBlockBreak(event.getPlayer(), stack, event);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for(AbstractCustomItem item : CustomItemSetup.customItems) {
			if(item instanceof PlayerRegister) {
				PlayerRegister register = (PlayerRegister) item;
				if(register.shouldRegisterPlayer(event.getPlayer())) {
					register.registerPlayer(event.getPlayer());
				}
			}
		}
	}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack stack = event.getItem();
		if(stack != null) {
			AbstractCustomItem item = CustomItemSetup.getItemByStack(stack);
			if(item != null) {
				Player player = event.getPlayer();
				switch (event.getAction()) {
					case RIGHT_CLICK_AIR:
						item.onAirRightClick(player, stack, event);
						break;
					case RIGHT_CLICK_BLOCK:
						item.onBlockRightClick(player, stack, event.getClickedBlock(), event);
						break;
					case LEFT_CLICK_AIR:
						item.onAirLeftClick(player, stack, event);
						break;
					case LEFT_CLICK_BLOCK:
						item.onBlockLeftClick(player, stack, event.getClickedBlock(), event);
						break;
					default:
						break;
				}
			}
		}
	}


	@EventHandler
	public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
		ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
		if(stack != null) {
			AbstractCustomItem item = CustomItemSetup.getItemByStack(stack);
			if(item != null) {
				item.onEntityRightClick(event.getPlayer(), stack, event.getRightClicked(), event);
			}
		}
	}


	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			ItemStack stack = player.getInventory().getItemInMainHand();
			if(stack != null) {
				AbstractCustomItem item = CustomItemSetup.getItemByStack(stack);
				if(item != null) {
					item.onEntityDamage(player, stack, event.getEntity(), event);
				}
			}
		}
	}


	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity killed = event.getEntity();
		Player killer = killed.getKiller();
		if(killer != null) {
			ItemStack stack = killer.getInventory().getItemInMainHand();
			if(stack != null) {
				AbstractCustomItem item = CustomItemSetup.getItemByStack(stack);
				if(item != null) {
					item.onEntityKill(killer, killed, event);
				}
			}
		}
	}


	@EventHandler
	public void onPlayerConsumesItem(PlayerItemConsumeEvent event) {
		AbstractCustomItem item = CustomItemSetup.getItemByStack(event.getItem());
		if(item != null) {
			item.onPlayerConsumesItem(event.getPlayer(), event.getItem(), event);
		}
	}


	@EventHandler
	public void onPlayerArmorEquip(PlayerArmorEquipEvent event) {
		AbstractCustomItem oldItem = CustomItemSetup.getItemByStack(event.getOldArmor());
		if(oldItem != null) {
			oldItem.onPlayerArmorUnequipItem(event.getPlayer(), event.getOldArmor(), event);
		}
		AbstractCustomItem newItem = CustomItemSetup.getItemByStack(event.getNewArmor());
		if(newItem != null) {
			newItem.onPlayerArmorEquipItem(event.getPlayer(), event.getNewArmor(), event);
		}
	}

}
