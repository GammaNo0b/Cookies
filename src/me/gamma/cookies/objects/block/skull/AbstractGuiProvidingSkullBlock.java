
package me.gamma.cookies.objects.block.skull;


import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.objects.block.Ownable;



public abstract class AbstractGuiProvidingSkullBlock extends AbstractSkullBlock implements Ownable, GuiProvider {

	@Override
	public void onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			event.setCancelled(true);
			if(this.isOwner(block, player)) {
				this.openGui(player, block);
			} else {
				player.sendMessage("§4You don't own this " + this.getDisplayName() + "§4!");
			}
		}
	}

}
