
package me.gamma.cookies.object.gui.book;


import java.util.HashMap;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.gui.book.StorageBook.NetworkInfo;
import me.gamma.cookies.object.network.Network;
import me.gamma.cookies.object.network.NetworkManager;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.Pair;



public class StorageBook implements Book<NetworkInfo> {

	private static final String ID_KEY = "networkid";

	@Override
	public String getIdentifier() {
		return "storage_book";
	}


	@Override
	public String getTitle(int page, NetworkInfo data) {
		return String.format("§6Item Storage §e#%08x §4[§c%d§4]", data.id, data.network.getSize());
	}


	@Override
	public int pages(NetworkInfo data) {
		return (data.resources.size() + 27) / 28;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public ItemStack getTurnLeftIcon() {
		return new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("§3<---").build();
	}


	@Override
	public ItemStack getTurnRightIcon() {
		return new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("§3--->").build();
	}


	@Override
	public ItemStack getCloseIcon() {
		return new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§eClose").build();
	}


	@Override
	public ItemStack getItemFiller() {
		return InventoryUtils.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
	}


	@Override
	public Inventory createGui(BookData<NetworkInfo> data) {
		Inventory gui = Book.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.BLUE_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 45, filler);
		}
		for(int i = 1; i <= 4; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, NetworkInfo data) {
		return ArrayUtils.array(CollectionUtils.subList(CollectionUtils.merge(data.resources, (t, i) -> {
			if(t.getType().isAir() || i == 0)
				return StorageBook.this.getItemFiller();
			ItemStack type = t.clone();
			ItemMeta meta = type.getItemMeta();
			String name = meta.hasDisplayName() ? meta.getDisplayName() : "§7" + Utils.toCapitalWords(type.getType());
			meta.setDisplayName(name + " §3Amount: §b" + i);
			type.setItemMeta(meta);
			return type;
		}).collect(Collectors.toList()), page * 28, 28), ItemStack[]::new);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {
		int slot = event.getSlot();
		int row = slot / 9;
		int column = slot - row * 9;
		int index = page * 28 + row * 7 + column - 8;
		NetworkInfo data = this.loadData(gui);
		ItemStack request = data.resources.keySet().stream().skip(index).findFirst().get();
		if(ItemUtils.isEmpty(request))
			return;

		Pair<ItemStack, Integer> result = data.network.fetch(request, request.getMaxStackSize(), Filter.empty());
		ItemStack item = result.left.clone();
		item.setAmount(result.right);
		ItemUtils.giveItemToPlayer(player, item);
		this.openGui(player, new BookData<>(page, data), false, false);
	}


	@Override
	public void onPlayerInventoryClick(HumanEntity player, PlayerInventory inventory, ItemStack stack, int slot, InventoryClickEvent event, int page) {
		if(ItemUtils.isEmpty(stack))
			return;

		NetworkInfo data = this.loadData(event.getInventory());
		stack.setAmount(data.network.store(stack, stack.getAmount()));
		this.openGui(player, new BookData<>(page, data), false, false);
	}


	@Override
	public NetworkInfo loadData(Inventory inventory) {
		return new NetworkInfo(InventoryUtils.getIntFromStack(this.getIdentifierStack(inventory), ID_KEY));
	}


	@Override
	public void saveData(Inventory inventory, NetworkInfo data) {
		InventoryUtils.storeIntInStack(this.getIdentifierStack(inventory), ID_KEY, data.id);
	}


	@Override
	public void openGui(HumanEntity player, BookData<NetworkInfo> data, boolean history, boolean playsound) {
		data.data().refreshResources();
		Book.super.openGui(player, data, history, playsound);
	}


	public static void openBook(HumanEntity player, Network<ItemStack> network) {
		BookInit.STORAGE_BOOK.open(player, new NetworkInfo(network));
	}

	static class NetworkInfo {

		private final int id;
		private final Network<ItemStack> network;
		private HashMap<ItemStack, Integer> resources;

		NetworkInfo(Network<ItemStack> network) {
			this.id = network.getId();
			this.network = network;
			this.refreshResources();
		}


		NetworkInfo(int id) {
			this.id = id;
			this.network = NetworkManager.getNetwork(id, ItemStack.class);
			this.refreshResources();
		}


		void refreshResources() {
			this.resources = this.network.listResources();
		}

	}

}
