
package me.gamma.cookies.listener;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.gamma.cookies.init.MultiBlockInit;
import me.gamma.cookies.object.multiblock.MultiBlock;



public class MultiBlockListener implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		if(event.useInteractedBlock() == Result.DENY)
			return;

		if(event.getHand() == EquipmentSlot.HAND && event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && !player.isSneaking()) {
			for(MultiBlock multiblock : MultiBlockInit.MULTIBLOCKS) {
				if(multiblock.getTriggerMaterial() == block.getType()) {
					if(multiblock.isMultiBlock(block)) {
						multiblock.onClick(player, block.getLocation(), event);
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

}
