
package me.gamma.cookies.objects.block.skull.storage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.ItemFilter;
import me.gamma.cookies.objects.block.ItemProvider;
import me.gamma.cookies.objects.block.StorageProvider;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.property.EnumProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.BigItemStack;
import me.gamma.cookies.util.ComplexInteger;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.level.block.entity.TileEntity;



public class StorageMonitor extends AbstractGuiProvidingSkullBlock implements StorageComponent {

	private static final EnumProperty<StorageMonitor.SortType> SORT_TYPE = new EnumProperty<>("sorttype", StorageMonitor.SortType.class);
	private static final BooleanProperty SORT_INVERTED = new BooleanProperty("sortinverted");
	private static final IntegerProperty TOTAL_STORED_ITEMS = new IntegerProperty("storeditems");
	private static final IntegerProperty CONNECTED_INVENTORIES = new IntegerProperty("connectedinventories");
	private static final IntegerProperty CONNECTED_STORAGE_BLOCKS = new IntegerProperty("connectedstorageblocks");
	private static final IntegerProperty COMPONENTS = new IntegerProperty("components");

	private final Integer[] border = new Integer[] {
		0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53
	};
	public static final String STATS_TITLE = "�9Storage �sSystem �bStatistics";

	public static boolean isStorageMonitor(TileState block) {
		return "storage_monitor".equals(IDENTIFIER.fetch(block));
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_MONITOR;
	}


	@Override
	public String getDisplayName() {
		return "�9Storage �3Monitor";
	}


	@Override
	public String getRegistryName() {
		return "storage_monitor";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("�7Displays all available Items from", "�7a Storage System.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.STORAGE, RecipeType.ENGINEER);
		recipe.setShape("GPG", "ICI", "GRG");
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('P', Material.GLASS);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('C', CustomBlockSetup.STORAGE_SKULL_BLOCK_TIER_2.createDefaultItemStack());
		recipe.setIngredient('R', CustomBlockSetup.STORAGE_CONNECTOR.createDefaultItemStack());
		return recipe;
	}


	@Override
	public int getRows() {
		return 6;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		SORT_TYPE.storeEmpty(block);
		SORT_INVERTED.storeEmpty(block);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(this.canAccess(block, event.getPlayer())) {
			return super.onBlockBreak(player, block, event);
		} else {
			event.getPlayer().sendMessage("�cYou are not owning this Storage Monitor!");
			return null;
		}
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			event.setCancelled(true);
			if(this.canAccess(block, player)) {
				player.playSound(player.getLocation(), this.getSound(), 0.2F, 1);
				Inventory gui = this.createMainGui(player, block);
				player.openInventory(this.constructPage(player, block, gui, 0));
			} else {
				player.sendMessage("�cYou are not owning this Storage Monitor!");
			}
		}
		return false;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		for(int i : border) {
			if(gui.getItem(i) == null) {
				gui.setItem(i, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName(" ").build());
			}
		}
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		int page = Integer.parseInt(event.getInventory().getItem(49).getItemMeta().getDisplayName().split("\\[")[1].split("\\]")[0].substring(2).split("�")[0]) - 1;
		int pages = Integer.parseInt(event.getInventory().getItem(49).getItemMeta().getDisplayName().split("\\[")[2].split("\\]")[0].substring(2).split("�")[0]);
		if(Arrays.asList(border).contains(slot)) {
			if(slot == 18) {
				clicker.openInventory(this.createStats(block));
			} else if(slot == 26) {
				SORT_TYPE.cycle(block);
				clicker.openInventory(this.constructPage(clicker, block, this.createMainGui(clicker, block), page));
			} else if(slot == 35) {
				SORT_INVERTED.toggle(block);
				clicker.openInventory(this.constructPage(clicker, block, this.createMainGui(clicker, block), page));
			} else if(slot == 48) {
				if(page == 0) {
					page = pages - 1;
				} else {
					page--;
				}

				clicker.openInventory(this.constructPage(clicker, block, this.createMainGui(clicker, block), page));
			} else if(slot == 49) {
				clicker.openInventory(this.constructPage(clicker, block, this.createMainGui(clicker, block), page));
			} else if(slot == 50) {
				if(page >= pages) {
					page = 0;
				} else {
					page++;
				}

				clicker.openInventory(this.constructPage(clicker, block, this.createMainGui(clicker, block), page));
			}

		} else {
			Map<Location, Inventory> locatedInventories = new HashMap<>();
			Map<Location, List<BigItemStack>> storageBlockContents = new HashMap<>();
			List<Location> components = new ArrayList<>();
			components.add(block.getLocation());
			if(!getConnectedInventories(block.getBlock(), locatedInventories, storageBlockContents, components, true)) {
				clicker.sendMessage("�cThis System has too much Connectors! Maximum Amount: �b" + ConfigValues.MAX_STORAGE_CONNECTORS);
				return true;
			}

			List<Inventory> inventories = new ArrayList<>();
			locatedInventories.values().forEach(inventories::add);

			List<Location> storageBlocks = new ArrayList<>();
			storageBlockContents.keySet().forEach(storageBlocks::add);

			ItemStack calculated = this.calculateItemStack(event.getCurrentItem());
			ItemStack stack = removeItemStack(inventories, storageBlocks, new ItemFilter(Arrays.asList(calculated), true, false));
			if(stack != null) {
				Utilities.giveItemToPlayer(clicker, stack);
				clicker.openInventory(constructPage(clicker, block, this.createMainGui(clicker, block), page));
			}
		}
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player clicker, TileState inventoryHolder, Inventory gui, InventoryClickEvent event) {
		Map<Location, Inventory> locatedInventories = new HashMap<>();
		Map<Location, List<BigItemStack>> storageBlockContents = new HashMap<>();
		List<Location> components = new ArrayList<>();
		components.add(inventoryHolder.getLocation());
		if(!getConnectedInventories(inventoryHolder.getBlock(), locatedInventories, storageBlockContents, components, true)) {
			clicker.sendMessage("�cThis System has too many Connectors! Maximum Amount: �b" + Cookies.getPlugin(Cookies.class).getConfig().getInt("max-connectors"));
			return true;
		}

		ItemStack item = event.getCurrentItem();
		for(Location storageBlockLocation : storageBlockContents.keySet()) {
			Block current = storageBlockLocation.getBlock();
			if(current.getState() instanceof TileState) {
				if(StorageSkullBlock.isStorageBlock((TileState) current.getState())) {
					if(item != null) {
						ItemStack rest = StorageProvider.storeItem((TileState) current.getState(), item);
						if(rest == null) {
							event.getWhoClicked().getInventory().removeItem(item);
							item = null;
							break;
						} else {
							item = rest;
						}
					}
				}
			}
		}

		if(item != null) {
			List<Inventory> inventories = new ArrayList<>();
			locatedInventories.forEach((key, value) -> inventories.add(value));

			ItemStack rest = addItemStack(inventories, storageBlockContents.keySet(), item.clone());
			event.getWhoClicked().getInventory().removeItem(Utilities.subtractItemStack(item, rest));
		}

		int page = Integer.parseInt(event.getInventory().getItem(49).getItemMeta().getDisplayName().split("\\[")[1].split("\\]")[0].substring(2).split("�")[0]) - 1;
		clicker.openInventory(this.constructPage(clicker, inventoryHolder, this.createMainGui(clicker, inventoryHolder), page));

		return true;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onInventoryClick(InventoryClickEvent event) {
				if(event.getView().getTitle().equals(STATS_TITLE)) {
					event.setCancelled(true);
				}
			}

		};
	}


	private Inventory createStats(TileState block) {
		Inventory gui = Bukkit.createInventory(null, 3 * 9, STATS_TITLE);
		gui.setItem(13, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("�9Location: �3" + block.getLocation().getBlockX() + " : " + block.getLocation().getBlockY() + " : " + block.getLocation().getBlockZ()).build());
		int[] border = new int[] {
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26
		};
		for(int i : border) {
			gui.setItem(i, InventoryManager.filler(Material.YELLOW_STAINED_GLASS_PANE));
		}
		int totalStoredItems = TOTAL_STORED_ITEMS.fetch(block);
		int inventories = CONNECTED_INVENTORIES.fetch(block);
		int storageBlocks = CONNECTED_STORAGE_BLOCKS.fetch(block);
		int connectors = COMPONENTS.fetch(block);
		gui.setItem(10, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("�cTotal Stored Items�8: �4" + totalStoredItems).build());
		gui.setItem(11, InventoryManager.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
		gui.setItem(12, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("�aConnected Inventories�8: �2" + inventories).build());
		gui.setItem(14, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("�dStorage Blocks�8: �5" + storageBlocks).build());
		gui.setItem(15, InventoryManager.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
		gui.setItem(16, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("�bComponents�8: �3" + connectors).build());
		return gui;
	}


	/**
	 * Constructs the Page from the activated Storage Monitor.
	 * 
	 * @param player          The Player to sent Error Messages
	 * @param inventoryHolder The Storage Monitor.
	 * @param gui             The basic Layout of the Inventory.
	 * @param page            The Page to be displayed.
	 * @return An filled Inventory with all Items from the given Page.
	 */
	private Inventory constructPage(@Nullable Player player, TileState block, Inventory gui, int page) {
		SortType sorttype = SORT_TYPE.fetch(block);
		boolean inverted = SORT_INVERTED.fetch(block);
		ComplexInteger totalAmount = new ComplexInteger();
		Map<Location, Inventory> inventories = new HashMap<>();
		Map<Location, List<BigItemStack>> storageBlockContents = new HashMap<>();
		List<Location> components = new ArrayList<>();
		components.add(block.getLocation());
		getConnectedInventories(block.getBlock(), inventories, storageBlockContents, components, false);
		Map<ItemStack, Integer> allItems = this.getAllItems(inventories, storageBlockContents, totalAmount);
		List<BigItemStack> sorted = this.sortItems(allItems, sorttype, inverted);
		List<List<BigItemStack>> pages = this.getItemPages(sorted);
		gui.setItem(18, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("�eStorage System Statistics").build());
		gui.setItem(26, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("�9Sort Type:").addLore("�r�3" + sorttype.getName()).build());
		gui.setItem(35, new ItemBuilder(inverted ? Material.RED_STAINED_GLASS_PANE : Material.GREEN_STAINED_GLASS_PANE).setName(inverted ? "�cDescending" : "�aAscending").build());
		gui.setItem(48, new ItemBuilder(Material.PAPER).setName("�8<- �7Previous Page").build());
		gui.setItem(49, new ItemBuilder(Material.PAPER).setName("�7Current Page�8: �6[�e" + (page + 1) + "�6] �8/ �6[�e" + pages.size() + "�6]").build());
		gui.setItem(50, new ItemBuilder(Material.PAPER).setName("�7Next Page �8->").build());
		List<BigItemStack> items = pages.get(page % pages.size());
		for(BigItemStack stack : items) {
			boolean hasName = stack.getStack().getItemMeta().hasDisplayName();
			gui.addItem(new ItemBuilder(stack.getStack()).setName((hasName ? stack.getStack().getItemMeta().getDisplayName() : "���6" + Utilities.toCapitalWords(stack.getStack().getType().toString().replace('_', ' '))) + " �7- �e" + stack.getAmount()).build());
		}

		TOTAL_STORED_ITEMS.store(block, totalAmount.getValue());
		CONNECTED_INVENTORIES.store(block, inventories.size());
		CONNECTED_STORAGE_BLOCKS.store(block, storageBlockContents.size());
		COMPONENTS.store(block, components.size());
		block.update();

		return gui;
	}


	/**
	 * Calculates the ItemStack from a Stored ItemStack.
	 * 
	 * @param clicked The clicked ItemStack.
	 * @return The Calculates ItemStack.
	 */
	private ItemStack calculateItemStack(ItemStack clicked) {
		if(clicked == null) {
			return null;
		}

		int available = Integer.parseInt(clicked.getItemMeta().getDisplayName().split("�7- �e")[1]);
		int amount = Math.min(available, clicked.getType().getMaxStackSize());
		if(clicked.getItemMeta().getDisplayName().contains("���")) {
			return new ItemBuilder(clicked).setAmount(amount).setName(null).build();
		} else {
			return new ItemBuilder(clicked).setAmount(amount).setName(clicked.getItemMeta().getDisplayName().split(" �7- �e")[0]).build();
		}
	}


	public static ItemStack addItemStack(TileState block, ItemStack item) {
		if(!isStorageMonitor(block))
			return item;

		Map<Location, Inventory> locatedInventories = new HashMap<>();
		Map<Location, List<BigItemStack>> storageBlockContents = new HashMap<>();
		List<Location> components = new ArrayList<>();
		components.add(block.getLocation());
		if(!getConnectedInventories(block.getBlock(), locatedInventories, storageBlockContents, components, true)) {
			return item;
		}

		List<Location> storageBlocks = new ArrayList<>();
		storageBlockContents.keySet().forEach(storageBlocks::add);

		List<Inventory> inventories = new ArrayList<>();
		locatedInventories.values().forEach(inventories::add);

		return addItemStack(inventories, storageBlocks, item);
	}


	public static ItemStack addItemStack(List<Inventory> inventories, Collection<Location> storageBlocks, ItemStack item) {
		for(Location storageBlockLocation : storageBlocks) {
			Block block = storageBlockLocation.getWorld().getBlockAt(storageBlockLocation);
			if(block.getState() instanceof TileState) {
				TileState state = (TileState) block.getState();
				if(StorageSkullBlock.isStorageBlock(state)) {
					ItemStack rest = StorageProvider.storeItem(state, item);
					if(rest == null) {
						return null;
					} else {
						item = rest;
					}
				}
			}
		}

		for(Inventory inventory : inventories) {
			Map<Integer, ItemStack> rest = inventory.addItem(item);
			if(rest.isEmpty()) {
				return null;
			} else {
				Iterator<Entry<Integer, ItemStack>> iterator = rest.entrySet().iterator();
				if(iterator.hasNext()) {
					ItemStack restItem = iterator.next().getValue();
					if(restItem == null) {
						return null;
					} else {
						item = restItem;
					}
				}
			}
		}

		return item;
	}


	public static ItemStack removeItemStack(TileState block, ItemFilter filter) {
		if(!isStorageMonitor(block))
			return null;

		Map<Location, Inventory> locatedInventories = new HashMap<>();
		Map<Location, List<BigItemStack>> storageBlockContents = new HashMap<>();
		List<Location> components = new ArrayList<>();
		components.add(block.getLocation());
		if(!getConnectedInventories(block.getBlock(), locatedInventories, storageBlockContents, components, false))
			return null;

		List<Inventory> inventories = new ArrayList<>(locatedInventories.values());
		List<Location> storageBlocks = new ArrayList<>(storageBlockContents.keySet());
		return removeItemStack(inventories, storageBlocks, filter);
	}


	public static ItemStack removeItemStack(List<Inventory> inventories, List<Location> storageBlocks, ItemFilter filter) {
		for(Location storageBlockLocation : storageBlocks) {
			Block current = storageBlockLocation.getBlock();
			if(current.getState() instanceof TileState) {
				if(StorageSkullBlock.isStorageBlock((TileState) current.getState())) {
					ItemStack request = StorageProvider.requestItem((TileState) current.getState(), filter);
					if(request != null && request.getAmount() > 0) {
						return request;
					}
				}
			}
		}

		for(Inventory inventory : inventories) {
			for(ItemStack stack : inventory) {
				ItemStack matching;
				if((matching = filter.getMatchingStack(stack)) != null) {
					matching = matching.clone();
					inventory.remove(matching);
					return matching;
				}
			}
		}

		return null;
	}


	private List<BigItemStack> sortItems(Map<ItemStack, Integer> items, SortType type, boolean inverted) {
		List<BigItemStack> stacks = new ArrayList<>();
		for(ItemStack key : items.keySet()) {
			BigItemStack stack = new BigItemStack(key);
			stack.set(items.get(key));
			stacks.add(stack);
		}
		stacks.sort(type.getComparator());
		if(inverted)
			Collections.reverse(stacks);
		return stacks;
	}


	/**
	 * Returns a List of ItemStack Maps so that every Map contains at most the same Amount of Items as one GUI can hold.
	 * 
	 * @param items Map with all Items as Key and their Amount as Value.
	 * @return List of Maps with all Items
	 */
	private List<List<BigItemStack>> getItemPages(List<BigItemStack> items) {
		List<List<BigItemStack>> pages = new ArrayList<>();
		List<BigItemStack> tmpList = new ArrayList<>();
		int index = 0;
		for(BigItemStack stack : items) {
			if(index++ < this.getFreeSlots()) {
				tmpList.add(stack);
			} else {
				index = 1;
				pages.add(new ArrayList<>(tmpList));
				tmpList = new ArrayList<>();
				tmpList.add(stack);
			}

		}

		pages.add(new ArrayList<>(tmpList));
		return pages;
	}


	/**
	 * Returns a Map with all stored Items as Key and their Amount as Value
	 * 
	 * @param inventories          All connected Inventories.
	 * @param storageBlockContents Map with the Location of a Storage Block as Key and a List of its ItemStocks stored as BiTypes as Value.
	 * @param components           List with already checked components.
	 * @return Map with all stored Items
	 */
	private Map<ItemStack, Integer> getAllItems(Map<Location, Inventory> inventories, Map<Location, List<BigItemStack>> storageBlockContents, ComplexInteger totalAmount) {
		Map<ItemStack, Integer> items = new HashMap<>();
		for(Location key : storageBlockContents.keySet()) {
			List<BigItemStack> itemStocks = storageBlockContents.get(key);
			for(BigItemStack itemStock : itemStocks) {
				ItemStack itemStack = itemStock.getStack().clone();
				int amount = itemStock.getAmount();
				totalAmount.addInteger(amount);
				if(itemStack.getType() != Material.AIR && amount > 0) {
					if(!items.containsKey(itemStack)) {
						items.put(itemStack, amount);
					} else {
						items.put(itemStack, items.get(itemStack) + amount);
					}
				}
			}
		}

		for(Location key : inventories.keySet()) {
			Inventory inventory = inventories.get(key);
			for(ItemStack item : inventory.getContents()) {
				if(item != null && item.getType() != Material.AIR) {
					ItemStack single = new ItemBuilder(item).setAmount(1).build();
					int amount = item.getAmount();
					totalAmount.addInteger(amount);
					if(!items.containsKey(single)) {
						items.put(single, amount);
					} else {
						items.put(single, items.get(single) + amount);
					}
				}
			}
		}

		return items;
	}


	/**
	 * Adds all Inventories attached to the Block itself or attached Connectors to the Map. Returns false if the amount of Connectors is bigger than
	 * allowed, otherwise true.
	 * 
	 * @param block         The Starting Block.
	 * @param inventories   The Map to store all Inventories and the Locations of their Holders. Should be set to an Empty Map when using recursive.
	 * @param storageBlocks The Map to store all Storage Block Contents and the Location of is. Should be set to an Empty Map when using recusive.
	 * @param components    The List to store all Location of already checked Storage Components. Should be set to an Empty List when using recursive.
	 * @return Returns if the amount of Connectors is under the Limit.
	 */
	public static boolean getConnectedInventories(Block block, Map<Location, Inventory> inventories, Map<Location, List<BigItemStack>> storageBlockContents, List<Location> components, boolean store) {
		for(BlockFace face : Utilities.faces) {
			Block relative = block.getRelative(face);
			Location location = relative.getLocation();
			BlockState state = relative.getState();
			if(state instanceof TileState) {
				if(StorageConnector.isConnector((TileState) state)) {
					if(!components.contains(location)) {
						components.add(location);
						if(components.size() > ConfigValues.MAX_STORAGE_CONNECTORS) {
							return false;
						}
						if(!getConnectedInventories(relative, inventories, storageBlockContents, components, store)) {
							return false;
						}
					}
				} else if(StorageSkullBlock.isStorageBlock((TileState) state)) {
					if(!storageBlockContents.containsKey(location)) {
						storageBlockContents.put(location, StorageProvider.getContent((TileState) state));
					}
				}
			}
			if(state instanceof BlockInventoryHolder) {
				if(!inventories.containsKey(location)) {
					if(store) {
						Inventory inventory = ((BlockInventoryHolder) state).getInventory();
						inventories.put(location, inventory);
					} else {
						TileEntity tile = ((CraftWorld) relative.getWorld()).getHandle().getTileEntity(new BlockPosition(relative.getX(), relative.getY(), relative.getZ()));
						if(tile != null) {
							NBTTagCompound tag = tile.save(new NBTTagCompound());
							if(tag.hasKeyOfType("Items", 9)) {
								NBTTagList list = tag.getList("Items", 10);
								if(list.size() > 0) {
									Inventory inventory = Bukkit.createInventory(null, (list.size() + 8) / 9 * 9);
									for(NBTBase nbt : list) {
										if(nbt instanceof NBTTagCompound) {
											net.minecraft.world.item.ItemStack nmsstack = net.minecraft.world.item.ItemStack.a((NBTTagCompound) nbt);
											ItemStack bukkitstack = CraftItemStack.asBukkitCopy(nmsstack);
											inventory.addItem(bukkitstack);
										}
									}
									inventories.put(location, inventory);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}


	public List<ItemProvider> createItemProviders(Block block, List<Location> connectors, List<Location> components) {
		List<ItemProvider> providers = new ArrayList<>();
		for(BlockFace face : Utilities.faces) {
			Block relative = block.getRelative(face);
			if(relative.getState() instanceof TileState) {
				TileState state = (TileState) relative.getState();
				if(StorageConnector.isConnector(state)) {
					if(!connectors.contains(state.getLocation())) {
						connectors.add(state.getLocation());
						if(connectors.size() > ConfigValues.MAX_STORAGE_CONNECTORS)
							return null;
						List<ItemProvider> list = createItemProviders(relative, connectors, components);
						if(list == null)
							return null;
						providers.addAll(list);
					}
				} else if(StorageSkullBlock.isStorageBlock(state)) {
					providers.addAll(StorageSkullBlock.createItemProviders(state));
				}
			}
			if(relative.getState() instanceof BlockInventoryHolder) {
				BlockInventoryHolder holder = (BlockInventoryHolder) relative.getState();
				providers.addAll(ItemProvider.createItemProviders(holder.getInventory()));
			}
		}
		return providers;
	}

	public static enum SortType {

		COUNT((stack1, stack2) -> stack1.getAmount() - stack2.getAmount()), NAME((stack1, stack2) -> {
			ItemMeta meta1 = stack1.getStack().getItemMeta();
			String str1 = meta1 != null && meta1.hasDisplayName() ? meta1.getDisplayName().replaceAll("�[0-9a-f]", "") : stack1.getStack().getType().name().toLowerCase();
			ItemMeta meta2 = stack2.getStack().getItemMeta();
			String str2 = meta2 != null && meta2.hasDisplayName() ? meta2.getDisplayName().replaceAll("�[0-9a-f]", "") : stack2.getStack().getType().name().toLowerCase();
			return str1.compareTo(str2);
		}), ID((stack1, stack2) -> stack1.getStack().getType().name().compareTo(stack2.getStack().getType().name()));

		private Comparator<BigItemStack> comparator;
		private String name;

		private SortType(Comparator<BigItemStack> comparator) {
			this.comparator = comparator;
			this.name = Utilities.toCapitalWords(this.name());
		}


		public Comparator<BigItemStack> getComparator() {
			return comparator;
		}


		public String getName() {
			return name;
		}

	}

}
