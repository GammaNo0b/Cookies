
package me.gamma.cookies.feature;


import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;



public class KnockDoorFeature extends SimpleCookieListener {

	@EventHandler
	public void onDoorKnock(PlayerInteractEvent event) {
		if(!this.enabled)
			return;

		if(event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		if(!event.getClickedBlock().getType().name().contains("DOOR"))
			return;

		Block block = event.getClickedBlock();
		block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.BLOCKS, 1.0F, 2.0F);
	}

}
