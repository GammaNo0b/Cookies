
package me.gamma.cookies.object.item.tools;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.ItemTicker;
import me.gamma.cookies.object.property.UUIDProperty;
import me.gamma.cookies.util.ItemBuilder;



public class PlayerTracker extends AbstractCustomItem implements ItemTicker {

	private static final String TITLE = "§2Select Tracking Player";

	private static final UUIDProperty TRACKED = new UUIDProperty("tracked");

	private final Set<UUID> players = new HashSet<>();
	private Map<ItemStack, UUID> tracked = new HashMap<>();

	public PlayerTracker() {
		this.registerItemTicker();
	}


	@Override
	public String getIdentifier() {
		return "player_tracker";
	}


	@Override
	public String getTitle() {
		return "§5Player Tracker";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("Trackes the selected player.");
	}


	@Override
	public Material getMaterial() {
		return Material.COMPASS;
	}


	@Override
	protected void editItemMeta(ItemMeta meta) {
		meta.setUnbreakable(false);
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		if(player.isSneaking()) {
			this.toggleTracking(player, stack);
		} else {
			this.openSelectorGui(player, 1);
		}
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(player.isSneaking()) {
			this.toggleTracking(player, stack);
		} else {
			this.openSelectorGui(player, 1);
		}
		return true;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}


	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return true;
	}


	@Override
	public List<ItemStack> getStacksFromPlayer(Player player) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for(ItemStack stack : player.getInventory().getContents()) {
			if(this.isInstanceOf(stack)) {
				list.add(stack);
			}
		}
		return list;
	}


	@Override
	public void tick(Player player, ItemStack stack) {
		UUID uuid = TRACKED.fetch(stack.getItemMeta());
		if(uuid != null) {
			Player tracked = Bukkit.getPlayer(uuid);
			if(tracked != null && tracked.isOnline()) {
				CompassMeta meta = (CompassMeta) stack.getItemMeta();
				if(meta.isUnbreakable()) {
					meta.setLodestone(player.getLocation());
					meta.setLodestoneTracked(false);
					stack.setItemMeta(meta);
				}
			}
		}
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onInventoryInteract(InventoryClickEvent event) {
				if(event.getView().getTitle().equals(TITLE) && event.getWhoClicked() instanceof Player) {
					event.setCancelled(true);
					int slot = event.getSlot();
					if(slot >= 9 && slot < 45) {
						HumanEntity player = event.getWhoClicked();
						ItemStack held = player.getInventory().getItemInMainHand();
						if(isInstanceOf(held)) {
							ItemStack stack = event.getInventory().getItem(slot);
							if(stack != null && stack.getType() == Material.PLAYER_HEAD) {
								OfflinePlayer track = ((SkullMeta) stack.getItemMeta()).getOwningPlayer();
								if(track.isOnline() && track instanceof Player) {
									ItemMeta meta = stack.getItemMeta();
									TRACKED.store(meta, track.getUniqueId());
								}
							}
						}
					} else if(slot == 48 || slot == 50) {
						openSelectorGui((Player) event.getWhoClicked(), Integer.parseInt(event.getInventory().getItem(49).getItemMeta().getDisplayName().substring(18)) + slot - 49);
					}
				}

				if(isInstanceOf(event.getCurrentItem()))
					tracked.remove(event.getCurrentItem());
				if(isInstanceOf(event.getCursor()))
					tracked.remove(event.getCursor());
			}

		};
	}


	private void toggleTracking(Player player, ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		if(meta.isUnbreakable()) {
			meta.setUnbreakable(false);
			((CompassMeta) meta).setLodestone(null);
			player.sendMessage("§cDisabled Player Tracker");
		} else {
			meta.setUnbreakable(true);
			player.sendMessage("§aEnabled Player Tracker");
		}
		stack.setItemMeta(meta);
	}


	private void openSelectorGui(Player player, int page) {
		List<Player> players = this.getPlayersSorted((Player) player);
		if(players == null) {
			player.sendMessage("§3No other Players online!");
			return;
		}
		int lastpage = (players.size() + 35) / 36;
		page = (page + lastpage - 1) % lastpage + 1;
		Inventory gui = Bukkit.createInventory(null, 54, TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(45 + i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < 36; i++) {
			int index = (page - 1) * 36 + i;
			if(index < players.size())
				gui.setItem(i + 9, this.createSkull(players.get(index)));
		}
		gui.setItem(48, new ItemBuilder(Material.PAPER).setName("§6   <---   §ePrevious Page").build());
		gui.setItem(49, new ItemBuilder(Material.PAPER).setName("§eCurrent Page: §e" + page).build());
		gui.setItem(50, new ItemBuilder(Material.PAPER).setName("§e   Next Page   §6--->").build());
		player.openInventory(gui);
	}


	private List<Player> getPlayersSorted(Player except) {
		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		players.remove(except);
		if(players.isEmpty())
			return null;
		players.sort((player1, player2) -> player1.getName().compareTo(player2.getName()));
		return players;
	}


	private ItemStack createSkull(Player player) {
		ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) stack.getItemMeta();
		meta.setDisplayName("§a" + player.getName());
		meta.setOwningPlayer(player);
		stack.setItemMeta(meta);
		return stack;
	}

}
