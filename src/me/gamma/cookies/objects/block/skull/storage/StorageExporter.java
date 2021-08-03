
package me.gamma.cookies.objects.block.skull.storage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.ItemFilter;
import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.ItemConsumer;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class StorageExporter extends AbstractGuiProvidingSkullBlock implements BlockTicker, StorageComponent {

	private static final BooleanProperty WHITELIST = new BooleanProperty("whitelist");
	private static final BooleanProperty IGNORE_NBT = new BooleanProperty("ignorenbt");

	private final Set<Location> locations = new HashSet<>();

	public StorageExporter() {
		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ORANGE_STORAGE_PART;
	}


	@Override
	public String getRegistryName() {
		return "storage_exporter";
	}


	@Override
	public String getDisplayName() {
		return "§6Storage Exporter";
	}


	@Override
	public int getRows() {
		return 3;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 4, RecipeCategory.STORAGE, RecipeType.ENGINEER);
		recipe.setShape("GCG", "CHC", "GCG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('C', Material.COPPER_INGOT);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		WHITELIST.store(meta, true);
		IGNORE_NBT.store(meta, true);
		for(ItemStackProperty property : createProperties())
			property.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		final ItemStack filler = InventoryManager.filler(Material.ORANGE_STAINED_GLASS_PANE);
		final ItemStack barrier = InventoryManager.filler(Material.BARRIER);
		for(int i = 0; i < 9; i++) {
			if(i != 4)
				gui.setItem(i, filler);
			gui.setItem(i + 18, filler);
		}
		gui.setItem(9, filler);
		gui.setItem(17, filler);
		ItemStackProperty[] properties = createProperties();
		for(int i = 0; i < properties.length; i++) {
			ItemStack filter = properties[i].fetch(block);
			if(filter != null && filter.getType() != Material.AIR) {
				gui.setItem(i + 10, filter);
			} else {
				gui.setItem(i + 10, barrier);
			}
		}
		gui.setItem(21, WHITELIST.fetch(block) ? new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§fWhitelist").build() : new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8Blacklist").build());
		gui.setItem(23, IGNORE_NBT.fetch(block) ? new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§bIgnore NBT").build() : new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9Accept NBT").build());
		return gui;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		WHITELIST.transfer(usedItem.getItemMeta(), block);
		IGNORE_NBT.transfer(usedItem.getItemMeta(), block);
		for(ItemStackProperty property : createProperties())
			property.transfer(usedItem.getItemMeta(), block);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack stack = super.onBlockBreak(player, block, event);
		ItemMeta meta = stack.getItemMeta();
		WHITELIST.transfer(block, meta);
		IGNORE_NBT.transfer(block, meta);
		for(ItemStackProperty property : createProperties())
			property.transfer(block, meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		final int slot = event.getSlot();
		boolean update = false;
		if(slot == 21) {
			update = true;
			WHITELIST.toggle(block);
		} else if(slot == 23) {
			update = true;
			IGNORE_NBT.toggle(block);
		} else if(slot > 9 && slot < 17) {
			update = true;
			ItemStack filter = event.getCursor().clone();
			filter.setAmount(1);
			createProperties()[slot - 10].store(block, filter);
		}

		if(update) {
			block.update();
			ItemStack cursor = event.getCursor().clone();
			event.getCursor().setAmount(0);
			player.openInventory(this.createMainGui(player, block));
			player.getOpenInventory().setCursor(cursor);
		}

		return true;
	}


	@Override
	public long getDelay() {
		return ConfigValues.STORAGE_SYSTEM_TRANSFER_SPEED;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return true;
	}


	@Override
	public void tick(TileState block) {
		TileState monitor = this.getStorageMonitor(block);
		if(monitor != null) {
			for(BlockFace face : Utilities.faces) {
				Block relative = block.getBlock().getRelative(face);
				if(relative.getState() instanceof TileState) {
					TileState state = (TileState) relative.getState();
					AbstractTileStateBlock tile = CustomBlockSetup.getCustomBlockFromTileState(state);
					if(tile != null) {
						if(tile instanceof ItemConsumer) {
							if(this.exportToItemConsumer(monitor, block, state, (ItemConsumer) tile))
								break;
						}
					} else if(state instanceof BlockInventoryHolder) {
						if(this.exportToInventory(monitor, block, ((BlockInventoryHolder) state).getInventory())) {
							break;
						}
					}
				}
			}
		}
	}


	private ItemFilter getFilter(TileState block) {
		return new ItemFilter(this.getExportableItems(block), WHITELIST.fetch(block), IGNORE_NBT.fetch(block));
	}


	private boolean exportToInventory(TileState monitor, TileState exporter, Inventory inventory) {
		ItemStack remove = StorageMonitor.removeItemStack(monitor, this.getFilter(exporter));
		if(remove != null) {
			boolean exit = false;
			HashMap<Integer, ItemStack> map = inventory.addItem(remove);
			for(ItemStack rest : map.values()) {
				ItemStack drop = StorageMonitor.addItemStack(monitor, rest);
				if(drop != null) {
					Utilities.dropItem(drop, exporter.getLocation().add(0.5D, 0.5D, 0.5D));
				}
				if(rest == null || rest.getAmount() != remove.getAmount())
					exit = true;
			}
			if(exit)
				return true;
		}
		return false;
	}


	private boolean exportToItemConsumer(TileState monitor, TileState exporter, TileState block, ItemConsumer consumer) {
		ItemStack remove = StorageMonitor.removeItemStack(monitor, this.getFilter(exporter));
		if(remove != null && remove.getType() != Material.AIR && remove.getAmount() > 0) {
			ItemStack rest = consumer.addItem(block, remove);
			if(rest != null) {
				ItemStack drop = StorageMonitor.addItemStack(monitor, rest);
				if(drop != null) {
					Utilities.dropItem(drop, exporter.getLocation());
				}
			}
			if(rest == null || rest.getAmount() != remove.getAmount())
				return true;
		}
		return false;
	}


	private List<ItemStack> getExportableItems(TileState block) {
		ItemStackProperty[] properties = createProperties();
		List<ItemStack> items = new ArrayList<>(properties.length);
		for(ItemStackProperty property : properties) {
			ItemStack stack = property.fetch(block);
			if(stack == null || stack.getType() == Material.AIR || stack.getAmount() == 0)
				continue;
			items.add(stack);
		}
		return items;
	}


	private static ItemStackProperty[] createProperties() {
		ItemStackProperty[] properties = new ItemStackProperty[7];
		for(int i = 0; i < properties.length; i++)
			properties[i] = new ItemStackProperty("filter" + i);
		return properties;
	}

}
