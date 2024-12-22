
package me.gamma.cookies.feature;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.init.Blocks;



public class ChairFeature implements CookieListener {

	private boolean enabled = false;

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	private final HashMap<UUID, Chair> chairs = new HashMap<>();
	private final ConfigurationSection config;

	public ChairFeature() {
		this.config = Cookies.INSTANCE.getConfig().getConfigurationSection("chairs");
	}


	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		if(!this.enabled)
			return;

		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(block == null)
			return;

		Location location = block.getLocation();
		BlockData data = block.getBlockData();
		String name = block.getType().name();
		if(data instanceof Slab slab) {
			if(slab.getType() == Type.BOTTOM) {
				this.createChair(player, location, "slab");
			}
		} else if(data instanceof Stairs stairs) {
			if(stairs.getHalf() == Half.BOTTOM) {
				switch (stairs.getFacing()) {
					case NORTH:
						this.createChair(player, location, "stairs-north");
						break;
					case SOUTH:
						this.createChair(player, location, "stairs-south");
						break;
					case EAST:
						this.createChair(player, location, "stairs-east");
						break;
					case WEST:
						this.createChair(player, location, "stairs-west");
						break;
					default:
						break;
				}
			}
		} else if(data instanceof Bed) {
			this.createChair(event.getPlayer(), location, "bed");
		} else if(block.getState() instanceof Skull skull && Blocks.getCustomBlockFromBlock(skull) == null) {
			this.createChair(player, location, "head");
		} else if(name.contains("CARPET")) {
			this.createChair(player, location, "carpet");
		}
	}


	@EventHandler
	public void onChairLeave(EntityDismountEvent event) {
		if(!(event.getDismounted() instanceof Arrow))
			return;
		if(!(event.getEntity() instanceof Player player))
			return;
		this.removeChair(player);
	}


	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		this.removeChair(event.getPlayer());
	}


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		this.removeChair(event.getEntity());
	}


	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		this.removeChair(event.getPlayer());
	}


	private void createChair(Player player, Location location, String path) {
		ConfigurationSection section = this.config.getConfigurationSection(path);
		double x = section.getDouble("x", 0.5D);
		double y = section.getDouble("y", 0.0D);
		double z = section.getDouble("z", 0.5D);
		this.createChair(player, location.add(x, y, z));
	}


	private void createChair(Player player, Location location) {
		if(player.hasMetadata("sitting"))
			return;
		Location ploc = player.getLocation();
		Arrow chair = location.getWorld().spawnArrow(location, new Vector(0, 0, 0), 0, 0);
		chair.setSilent(true);
		chair.setGravity(false);
		chair.setInvulnerable(true);
		chair.addPassenger(player);
		chair.setMetadata("chair", new FixedMetadataValue(Cookies.INSTANCE, true));
		// for(Player onlinePlayer : Bukkit.getOnlinePlayers())
		// if(onlinePlayer.getUniqueId() != player.getUniqueId())
		// ((CraftPlayer) onlinePlayer).getHandle().b.a(new PacketPlayOutEntityDestroy(chair.getEntityId()));
		player.setMetadata("sitting", new FixedMetadataValue(Cookies.INSTANCE, true));
		chairs.put(player.getUniqueId(), new Chair(chair, ploc));
	}


	public void removeChair(Player player) {
		if(player.hasMetadata("sitting")) {
			player.removeMetadata("sitting", Cookies.INSTANCE);
			try {
				Chair data = this.chairs.remove(player.getUniqueId());
				data.getChair().remove();
				player.teleport(data.getEnterLocation(player.getLocation()));
			} catch(NullPointerException e) {}
		}
	}


	public void clearAllChairs() {
		for(Map.Entry<UUID, Chair> entry : this.chairs.entrySet()) {
			Chair chair = entry.getValue();
			chair.getChair().remove();

			Player player = Bukkit.getPlayer(entry.getKey());
			if(player != null) {
				player.removeMetadata("sitting", Cookies.INSTANCE);
				player.teleport(chair.getEnterLocation(player.getLocation()));
			}
		}
	}

	private class Chair {

		private Arrow chair;
		private Location enter;

		public Chair(Arrow chair, Location enter) {
			this.chair = chair;
			this.enter = enter;
		}


		public Arrow getChair() {
			return this.chair;
		}


		public Location getEnterLocation() {
			return new Location(this.enter.getWorld(), this.enter.getX(), this.enter.getY(), this.enter.getZ());
		}


		public Location getEnterLocation(Location preset) {
			Location enter = getEnterLocation();
			enter.setPitch(preset.getPitch());
			enter.setYaw(preset.getYaw());
			return enter;
		}

	}

}
