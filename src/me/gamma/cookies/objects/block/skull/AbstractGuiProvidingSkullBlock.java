
package me.gamma.cookies.objects.block.skull;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.block.GuiProvider;
import me.gamma.cookies.objects.block.Ownable;



public abstract class AbstractGuiProvidingSkullBlock extends AbstractSkullBlock implements Ownable, GuiProvider {

	private final Map<Location, List<Player>> viewers = new HashMap<Location, List<Player>>();

	@Override
	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			event.setCancelled(true);
			if(this.canAccess(block, player)) {
				this.openGui(player, block);
				return true;
			} else {
				player.sendMessage("§4You don't own this " + this.getDisplayName() + "§4!");
			}
		}
		return false;
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(!this.canAccess(block, player)) {
			event.setCancelled(true);
			return null;
		}
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public List<Player> getViewers(TileState block) {
		List<Player> viewers = this.viewers.get(block.getLocation());
		if(viewers == null) {
			viewers = new ArrayList<>();
			this.viewers.put(block.getLocation(), viewers);
		}
		return viewers;
	}

}
