
package me.gamma.cookies.objects.block.skull.storage;


import java.util.ArrayList;
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
import me.gamma.cookies.objects.block.ItemProvider;
import me.gamma.cookies.objects.block.ItemSupplier;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class StorageImporter extends AbstractGuiProvidingSkullBlock implements BlockTicker, StorageComponent {

	private static final BooleanProperty WHITELIST = new BooleanProperty("whitelist");
	private static final BooleanProperty IGNORE_NBT = new BooleanProperty("ignorenbt");

	private final Set<Location> locations = new HashSet<>();

	public StorageImporter() {
		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LIGHT_BLUE_STORAGE_PART;
	}


	@Override
	public String getRegistryName() {
		return "storage_importer";
	}


	@Override
	public String getDisplayName() {
		return "§9Storage Importer";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), 4, RecipeCategory.STORAGE, RecipeType.ENGINEER);
		recipe.setShape("IAI", "AHA", "IAI");
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('A', CustomItemSetup.ALUMINUM_INGOT.createDefaultItemStack());
		return recipe;
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
		final ItemStack filler = InventoryManager.filler(Material.BLUE_STAINED_GLASS_PANE);
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
		IGNORE_NBT.transfer(block, meta);;
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
						if(tile instanceof ItemSupplier) {
							if(this.importFromItemSupplier(monitor, block, state, (ItemSupplier) tile)) {
								break;
							}
						}
					} else if(state instanceof BlockInventoryHolder) {
						if(this.importFromInventory(monitor, block, ((BlockInventoryHolder) state).getInventory())) {
							break;
						}
					}
				}
			}
		}
	}


	private boolean importFromInventory(TileState monitor, TileState importer, Inventory inventory) {
		final ItemFilter filter = this.getFilter(importer);
		for(int i = 0; i < inventory.getSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if(filter.matches(stack)) {
				StorageMonitor.addItemStack(monitor, stack);
				inventory.setItem(i, null);
				return true;
			}
		}
		return false;
	}


	private boolean importFromItemSupplier(TileState monitor, TileState importer, TileState block, ItemSupplier supplier) {
		List<ItemProvider> providers = supplier.getOutputStackHolders(block);
		final ItemFilter filter = this.getFilter(importer);
		for(int i = 0; i < providers.size(); i++) {
			ItemProvider provider = providers.get(i);
			ItemStack stack = provider.get();
			if(filter.matches(stack)) {
				StorageMonitor.addItemStack(monitor, stack);
				provider.set(null);
				return true;
			}
		}
		return false;
	}


	private ItemFilter getFilter(TileState block) {
		return new ItemFilter(this.getImportableItems(block), WHITELIST.fetch(block), IGNORE_NBT.fetch(block));
	}


	private List<ItemStack> getImportableItems(TileState block) {
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
