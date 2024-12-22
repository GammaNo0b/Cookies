
package me.gamma.cookies.object.item.tools;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;



public class PortableNetherPortal extends AbstractCustomItem {

	@Override
	protected String getBlockTexture() {
		return HeadTextures.NETHER_PORTAL;
	}


	@Override
	public String getIdentifier() {
		return "portable_nether_portal";
	}


	@Override
	public String getTitle() {
		return "§5Portable §cNether §6Portal";
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.handleRightClick(player);
		return super.onAirRightClick(player, stack, event);
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleRightClick(player);
		return super.onBlockRightClick(player, stack, block, event);
	}


	private void handleRightClick(Player player) {
		Environment dimension = player.getWorld().getEnvironment();
		if(dimension == Environment.NORMAL) {
			for(World world : Bukkit.getWorlds()) {
				if(world.getEnvironment() == Environment.NETHER) {
					this.teleportNether(player, world);
					return;
				}
			}
			player.sendMessage("§cIt seems like there does not exist a nether on this server!");
		} else if(dimension == Environment.NETHER) {
			for(World world : Bukkit.getWorlds()) {
				if(world.getEnvironment() == Environment.NORMAL) {
					this.teleportOverworld(player, world);
					return;
				}
			}
			player.sendMessage("§cIt seems like there does not exist an overworld on this server!");
		} else {
			player.sendMessage("§cThis Item doesn't work in this dimension!");
		}
	}


	private void teleportOverworld(Player player, World world) {
		Location location = player.getRespawnLocation();
		if(location == null)
			location = world.getSpawnLocation();
		player.teleport(location);
	}


	private void teleportNether(Player player, World world) {
		Location location = world.getSpawnLocation();
		Block block = location.getBlock();
		if(block.getType().isSolid())
			block.setType(Material.AIR);
		Block down = block.getRelative(BlockFace.DOWN);
		if(!down.getType().isSolid())
			down.setType(Material.NETHERRACK);
		Block up = block.getRelative(BlockFace.UP);
		if(up.getType().isSolid())
			up.setType(Material.AIR);
		player.teleport(location);
	}

}
