
package me.gamma.cookies.object.block.network.item;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockTicker;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.UpdatingGuiProvider;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.property.BigItemStackProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class StorageCrateBlock extends AbstractCustomBlock implements ItemStorage, Ownable, UpdatingGuiProvider, BlockTicker {

	public static final IntegerProperty SELECTED_PAGE = new IntegerProperty("selectedpage");

	private static final int PAGE_SIZE = 4 * 7;
	public static final String ITEM_MARK = "item";
	private static final ItemStack EMPTY_SLOT = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

	private final String identifier;
	private final String texture;
	private final int capacity;

	private final Set<Location> locations = new HashSet<>();
	private final HashMap<Location, Inventory> inventories = new HashMap<>();

	public StorageCrateBlock(String identifier, String texture, int capacity) {
		this.identifier = identifier;
		this.texture = texture;
		this.capacity = capacity;

		this.register();
	}


	@Override
	public boolean shouldCorrectFacing(TileState block) {
		return false;
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		this.updateInventory(block, inventory, null);
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public long getDelay() {
		return 10;
	}


	@Override
	public void tick(TileState block) {
		this.updateInventory(block, this.getGui(block), null);
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getTitle(TileState data) {
		return "Storage Crate Block";
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public Map<Location, Inventory> getInventoryMap() {
		return this.inventories;
	}


	@Override
	public Inventory createGui(TileState data) {
		Inventory gui = UpdatingGuiProvider.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 45, filler);
		}
		for(int i = 1; i <= 4; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		for(int i = 1; i <= 4; i++)
			for(int j = 1; j <= 7; j++)
				gui.setItem(i * 9 + j, EMPTY_SLOT);
		gui.setItem(47, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8<---").build());
		gui.setItem(51, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8--->").build());
		gui.setItem(4, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8Close").build());
		return gui;
	}


	private ItemStack removeStack(TileState block, int index, boolean removeStack) {
		index += SELECTED_PAGE.fetch(block) * PAGE_SIZE;
		List<Provider<ItemStack>> providers = this.getItemProviders(block);
		if(index >= providers.size())
			return null;

		Provider<ItemStack> provider = providers.get(index);
		return removeStack ? ItemProvider.get(provider) : ItemProvider.get(provider, 1);
	}

	/*
	 * private void updateInventory(TileState block, int index) { int slot = index % PAGE_SIZE; List<Provider<ItemStack>> providers =
	 * this.getItemProviders(block); if(index >= providers.size()) return;
	 * 
	 * Inventory inventory = this.getGui(block); Provider<ItemStack> provider = providers.get(index); ItemStack type = provider.getType();
	 * if(provider.isEmpty()) { type = EMPTY_SLOT; } else { ItemMeta meta = type.getItemMeta(); String name = meta.hasDisplayName() ?
	 * meta.getDisplayName() : "§f" + Utils.toCapitalWords(type.getType()); meta.setDisplayName(name + " §3Amount: §b" + provider.amount());
	 * type.setItemMeta(meta); } inventory.setItem(slot / 7 * 9 + slot % 7 + 10, type); }
	 */


	private void updateInventory(TileState block, Inventory inventory, ItemStack type) {
		int page = SELECTED_PAGE.fetch(block);
		int index = page * PAGE_SIZE;
		List<Provider<ItemStack>> providers = this.getItemProviders(block);
		for(int r = 1; r <= 4 && index < providers.size(); r++) {
			for(int c = 1; c <= 7 && index < providers.size(); c++, index++) {
				Provider<ItemStack> provider = providers.get(index);
				if(type != null && !provider.match(type))
					continue;

				ItemStack stack = provider.getType();
				if(provider.isEmpty()) {
					stack = EMPTY_SLOT;
				} else {
					ItemMeta meta = stack.getItemMeta();
					String name = meta.hasDisplayName() ? meta.getDisplayName() : "§f" + Utils.toCapitalWords(stack.getType());
					meta.setDisplayName(name + " §3Amount: §b" + provider.amount());
					stack.setItemMeta(meta);
				}
				inventory.setItem(r * 9 + c, stack);
			}
		}
	}


	private void updateSlot(TileState block, int index, int slot) {
		Inventory inventory = this.getGui(block);
		int page = SELECTED_PAGE.fetch(block);
		index += page * PAGE_SIZE;
		List<Provider<ItemStack>> providers = this.getItemProviders(block);
		if(index >= providers.size())
			return;

		Provider<ItemStack> provider = providers.get(index);
		ItemStack type = provider.getType();
		if(provider.isEmpty()) {
			type = EMPTY_SLOT;
		} else {
			ItemMeta meta = type.getItemMeta();
			String name = meta.hasDisplayName() ? meta.getDisplayName() : "§f" + Utils.toCapitalWords(type.getType());
			meta.setDisplayName(name + " §3Amount: §b" + provider.amount());
			type.setItemMeta(meta);
		}
		inventory.setItem(slot, type);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState data, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 4) {
			player.closeInventory();
			return true;
		} else if(slot == 47) {
			int pages = this.getItemProviders(data).size() / PAGE_SIZE;
			if(pages > 0) {
				SELECTED_PAGE.cycle(data, -1, pages);
				this.updateInventory(data, gui, null);
			}
			data.update();
			return true;
		} else if(slot == 51) {
			int pages = this.getItemProviders(data).size() / PAGE_SIZE;
			if(pages > 0) {
				SELECTED_PAGE.cycle(data, 1, pages);
				this.updateInventory(data, gui, null);
			}
			data.update();
			return true;
		}

		ClickType click = event.getClick();
		boolean removeAll = click.isLeftClick();
		if(!removeAll && !click.isRightClick())
			return true;

		int row = slot / 9 - 1;
		int column = slot - row * 9 - 10;
		if(row < 0 || 4 <= row || column < 0 || 7 <= column)
			return true;

		int index = row * 7 + column;

		ItemUtils.giveItemToPlayer(player, this.removeStack(data, index, removeAll));
		this.updateSlot(data, index, slot);
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState data, PlayerInventory gui, InventoryClickEvent event) {
		ItemStack stack = event.getCurrentItem();

		if(ItemUtils.isEmpty(stack))
			return true;

		ClickType click = event.getClick();
		boolean addAll = click.isLeftClick();
		if(!addAll && !click.isRightClick())
			return true;

		if(addAll) {
			gui.setItem(event.getSlot(), this.addStack(data, stack));
		} else {
			gui.setItem(event.getSlot(), this.addStack(data, stack, 1));
		}

		this.updateInventory(data, this.getGui(data), stack);
		return true;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3F).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F).add(SELECTED_PAGE).add(StorageCrateBlock::createProperty, this.getCapacity());
	}


	@Override
	public byte getItemInputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public byte getItemOutputAccessFlags(PersistentDataHolder holder) {
		return 0x3F;
	}


	@Override
	public List<Provider<ItemStack>> getItemProviders(PersistentDataHolder holder) {
		return IntStream.range(0, this.getCapacity()).mapToObj(StorageCrateBlock::createProperty).map(p -> (Provider<ItemStack>) ItemProvider.fromBigItemStackProperty(p, holder)).toList();
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(player.isSneaking())
			return false;

		if(!this.canAccess(block, player))
			return false;

		this.openGui(player, block);
		return true;
	}


	private int getCapacity() {
		return this.capacity;
	}


	private static BigItemStackProperty createProperty(int slot) {
		return new BigItemStackProperty(String.valueOf(slot));
	}

	/*
	 * private BigItemStackProperty createProperty(TileState block, int slot) { final Location location = block.getLocation(); return new
	 * BigItemStackProperty(String.valueOf(slot)) {
	 * 
	 * @Override public void store(PersistentDataContainer container, BigItemStack value) { super.store(container, value);
	 * if(location.getBlock().getState() instanceof TileState b) StorageCrateBlock.this.updateInventory(b, slot); }
	 * 
	 * }; }
	 */


	/**
	 * Stores the items inside the ingredient storage crate items inside into the result storage crate item. Used for crafting to not loose any materials
	 * stored in item crates.
	 * 
	 * @param result      the result storage crate item
	 * @param ingredients the storage crate item ingredients
	 */
	public static void storeItems(ItemStack result, ItemStack... ingredients) {
		ItemMeta meta = result.getItemMeta();

		if(!(Blocks.getCustomBlockFromHolder(meta) instanceof StorageCrateBlock crate))
			return;

		List<Provider<ItemStack>> providers = crate.getItemProviders(meta);

		for(ItemStack ingredient : ingredients) {
			if(ItemUtils.isEmpty(ingredient))
				continue;

			ItemMeta m = ingredient.getItemMeta();
			if(!(Blocks.getCustomBlockFromHolder(m) instanceof StorageCrateBlock c))
				continue;

			List<Provider<ItemStack>> list = c.getItemProviders(m);
			loop: for(Provider<ItemStack> p : list) {
				if(p.isEmpty())
					continue;

				for(Provider<ItemStack> provider : providers) {
					if(provider.isEmpty()) {
						provider.setType(p.getType());
						p.set(provider.set(p.get(Integer.MAX_VALUE)));
						if(p.isEmpty())
							continue loop;
					}

					if(!p.match(provider.getType()))
						continue;

					p.set(provider.set(p.get(Integer.MAX_VALUE)));
					if(p.isEmpty())
						continue loop;
				}

				System.out.println("[DEBUG] Could not store items from ingredients! (StorageCrateBlock#storeItems(ItemStack, ItemStack[])");
			}
		}

		result.setItemMeta(meta);
	}

}
