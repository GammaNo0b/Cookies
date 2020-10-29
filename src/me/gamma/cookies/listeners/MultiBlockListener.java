
package me.gamma.cookies.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.gamma.cookies.objects.block.multi.MultiBlock;
import me.gamma.cookies.setup.MultiBlockSetup;



public class MultiBlockListener implements Listener {

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && !event.getPlayer().isSneaking()) {
			for(MultiBlock multiblock : MultiBlockSetup.multiBlocks) {
				if(multiblock.getTriggerMaterial() == event.getClickedBlock().getType()) {
					if(multiblock.isMultiBlock(event.getClickedBlock())) {
						event.setCancelled(true);
						multiblock.onClick(event.getPlayer(), event.getClickedBlock().getLocation());
					}
				}
			}
		}
	}

}
