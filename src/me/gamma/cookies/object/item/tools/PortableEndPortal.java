
package me.gamma.cookies.object.item.tools;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.util.GameProfileHelper;



public class PortableEndPortal extends AbstractCustomItem {

	public static final BooleanProperty OPEN = new BooleanProperty("open");

	@Override
	protected String getBlockTexture() {
		return HeadTextures.END_PORTAL_CLOSED;
	}


	@Override
	public String getIdentifier() {
		return "portable_end_portal";
	}


	@Override
	public String getTitle() {
		return "§aPortable §2End §3Portal";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Right click the dragon egg with this item to activate the portal.");
	}


	@Override
	public ItemStack get() {
		ItemStack stack = super.get();
		ItemMeta meta = stack.getItemMeta();
		OPEN.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.handleRightClick(player, stack, null);
		return super.onAirRightClick(player, stack, event);
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(super.onBlockRightClick(player, stack, block, event) || block.getType() == Material.DRAGON_EGG) {
			this.handleRightClick(player, stack, block);
			return true;
		}

		return false;
	}


	private void handleRightClick(Player player, ItemStack portal, Block block) {
		if(this.isOpen(portal)) {
			Environment dimension = player.getWorld().getEnvironment();
			if(dimension == Environment.NORMAL) {
				for(World world : Bukkit.getWorlds()) {
					if(world.getEnvironment() == Environment.THE_END) {
						this.teleportEnd(player, world);
						return;
					}
				}
				player.sendMessage("§cIt seems like there does not exist the end world on this server!");
			} else if(dimension == Environment.THE_END) {
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
		} else if(block != null && block.getType() == Material.DRAGON_EGG) {
			this.setOpen(portal, true);
		} else {
			player.sendMessage("§cThis Portal is not already opened!");
		}
	}


	private void teleportOverworld(Player player, World world) {
		Location location = player.getRespawnLocation();
		if(location == null)
			location = world.getSpawnLocation();
		player.teleport(location);
	}


	private void teleportEnd(Player player, World world) {
		player.teleport(new Location(world, 100, 50, 0));
	}


	public boolean isOpen(ItemStack portal) {
		if(!this.isInstanceOf(portal))
			return false;
		return OPEN.fetch(portal.getItemMeta());
	}


	public void setOpen(ItemStack portal, boolean open) {
		if(!this.isInstanceOf(portal))
			return;
		ItemMeta meta = portal.getItemMeta();
		OPEN.store(meta, open);
		portal.setItemMeta(meta);
		GameProfileHelper.setSkullTexture(portal, open ? HeadTextures.END_PORTAL_OPEN : HeadTextures.END_PORTAL_CLOSED);
	}

}
