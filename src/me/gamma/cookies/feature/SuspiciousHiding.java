
package me.gamma.cookies.feature;


import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrushableBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemUtils;



public class SuspiciousHiding extends SimpleCookieListener {

	private static final Map<Material, Material> suspiciousBlocks = Map.of(Material.SAND, Material.SUSPICIOUS_SAND, Material.GRAVEL, Material.SUSPICIOUS_GRAVEL);

	@EventHandler
	public void onSuspiciousBlockClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(!player.isSneaking())
			return;

		Block clicked = event.getClickedBlock();
		if(clicked == null)
			return;

		Material suspiciousType = suspiciousBlocks.get(clicked.getType());
		if(suspiciousType == null)
			return;

		ItemStack brush = player.getInventory().getItemInOffHand();
		if(!ItemUtils.isType(brush, Material.BRUSH))
			return;

		ItemStack stack = player.getInventory().getItemInMainHand();
		if(ItemUtils.isEmpty(stack))
			return;

		clicked.setType(suspiciousType);
		BrushableBlock brushable = (BrushableBlock) clicked.getState();
		brushable.setItem(ItemUtils.cloneItem(stack, 1));
		brushable.update();

		if(player.getGameMode() != GameMode.CREATIVE) {
			ItemUtils.damageItem(brush, 1);
			ItemUtils.increaseItem(stack, -1);
		}

		event.setCancelled(true);
	}

}
