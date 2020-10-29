
package me.gamma.cookies.objects.item;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ItemBuilder;



public class PlayerTracker extends AbstractCustomItem {

	private static final String TITLE = "§2Select Tracking Player";

	private Map<ItemStack, Player> tracked = new HashMap<>();

	@Override
	public String getIdentifier() {
		return "player_tracker";
	}


	@Override
	public String getDisplayName() {
		return "§5Player Tracker";
	}


	@Override
	public Material getMaterial() {
		return Material.COMPASS;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.TOOLS, RecipeType.CUSTOM);
		recipe.setShape("   ", " C ", "   ");
		recipe.setIngredient('C', Material.COMPASS);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		meta.setUnbreakable(false);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		event.setCancelled(true);
		if(player.isSneaking()) {
			this.toggleTracking(player, stack);
		} else {
			this.openSelectorGui(player, 1);
		}
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
		if(player.isSneaking()) {
			this.toggleTracking(player, stack);
		} else {
			this.openSelectorGui(player, 1);
		}
	}


	@Override
	public Runnable getCustomTask() {
		return () -> {
			for(ItemStack tracker : tracked.keySet()) {
				if(tracker != null && tracker.getType() == Material.COMPASS && tracker.hasItemMeta()) {
					Player track = tracked.get(tracker);
					if(track != null) {
						CompassMeta meta = (CompassMeta) tracker.getItemMeta();
						if(meta.isUnbreakable()) {
							meta.setLodestone(track.getLocation());
							meta.setLodestoneTracked(false);
							tracker.setItemMeta(meta);
						}
					}
				}
			}
		};
	}


	@Override
	public Listener getCustomListener() {
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
									tracked.put(held, (Player) track);
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
