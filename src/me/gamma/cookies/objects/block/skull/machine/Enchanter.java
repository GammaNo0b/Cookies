
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.ItemConsumer;
import me.gamma.cookies.objects.block.ItemProvider;
import me.gamma.cookies.objects.block.ItemSupplier;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;
import net.minecraft.nbt.NBTTagList;



public class Enchanter extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable, ItemConsumer, ItemSupplier {

	public static final IntegerProperty PROGRESS = new IntegerProperty("progress");
	public static final ItemStackProperty INPUT = new ItemStackProperty("input");
	public static final ItemStackProperty OUTPUT = new ItemStackProperty("output");
	public static final ItemStackProperty ENCHANTED_BOOK = new ItemStackProperty("enchantedbook");
	public static final ItemStackProperty PROCESSING = new ItemStackProperty("processing");
	public static final ItemStackProperty ENCHANTED_BOOK_PROCESSING = new ItemStackProperty("enchantedbookprocessing");
	public static final IntegerProperty BOOKS = new IntegerProperty("books");

	private final HashSet<Location> locations = new HashSet<>();
	private final HashMap<Location, Inventory> inventories = new HashMap<>();

	public Enchanter() {
		register();
	}


	@Override
	public int getRows() {
		return 5;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ENCHANTMENT_TABLE;
	}


	@Override
	public String getRegistryName() {
		return "enchanter";
	}


	@Override
	public String getDisplayName() {
		return "§6Enchanter";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape(" B ", "DCD", "OOO");
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('C', Material.ENCHANTING_TABLE);
		return recipe;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public List<ItemProvider> getOutputStackHolders(TileState block) {
		return Arrays.asList(ItemProvider.fromInventory(this.inventories.get(block.getLocation()), 20));
	}


	@Override
	public List<ItemProvider> getInputStackHolders(TileState block) {
		return Arrays.asList(ItemProvider.fromInventory(this.inventories.get(block.getLocation()), 24));
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		return inventories.get(block.getLocation());
	}


	@Override
	public void load(NBTTagList list) {
		BlockTicker.super.load(list);
		this.fillInventoryMap();
	}


	@Override
	public NBTTagList save() {
		this.saveInventoryMap();
		return BlockTicker.super.save();
	}


	private void fillInventoryMap() {
		for(Location location : this.getLocations()) {
			if(location.getBlock().getState() instanceof TileState) {
				TileState block = (TileState) location.getBlock().getState();
				Inventory gui = this.createInventory(block);
				gui.setItem(20, INPUT.fetch(block));
				gui.setItem(21, ENCHANTED_BOOK.fetch(block));
				gui.setItem(23, OUTPUT.fetch(block));
				gui.setItem(24, new ItemStack(Material.BOOK, BOOKS.fetch(block)));
				this.inventories.put(location, gui);
				this.setProgress(block, PROGRESS.fetch(block));
			}
		}
	}


	private void saveInventoryMap() {
		for(Location location : this.getLocations()) {
			if(location.getBlock().getState() instanceof TileState) {
				TileState block = (TileState) location.getBlock().getState();
				Inventory gui = this.inventories.get(location);
				INPUT.store(block, gui.getItem(20));
				ENCHANTED_BOOK.store(block, gui.getItem(21));
				OUTPUT.store(block, gui.getItem(23));
				ItemStack bookstack = gui.getItem(24);
				BOOKS.store(block, Utilities.isEmpty(bookstack) ? 0 : bookstack.getAmount());
				block.update();
			}
		}
	}


	private Inventory createInventory(TileState block) {
		Inventory gui = super.createMainGui(null, block);
		ItemStack filler = InventoryManager.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i : new int[] {
			0, 1, 7, 8, 9, 17
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.RED_STAINED_GLASS_PANE);
		for(int i : new int[] {
			2, 3, 5, 6
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i : new int[] {
			18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] {
			10, 11, 12, 19, 28, 29, 30
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.LIME_STAINED_GLASS_PANE);
		for(int i : new int[] {
			14, 15, 16, 25, 32, 33, 34
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : new int[] {
			13, 31
		})
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		INPUT.storeEmpty(block);
		OUTPUT.storeEmpty(block);
		PROCESSING.storeEmpty(block);
		ENCHANTED_BOOK_PROCESSING.storeEmpty(block);
		ENCHANTED_BOOK.storeEmpty(block);
		BOOKS.storeEmpty(block);
		super.onBlockPlace(player, usedItem, block, event);
		this.inventories.put(block.getLocation(), this.createInventory(block));
		this.setProgress(block, 0);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		this.inventories.remove(block.getLocation());
		Utilities.dropItem(INPUT.fetch(block), block.getLocation());
		Utilities.dropItem(OUTPUT.fetch(block), block.getLocation());
		Utilities.dropItem(PROCESSING.fetch(block), block.getLocation());
		Utilities.dropItem(ENCHANTED_BOOK_PROCESSING.fetch(block), block.getLocation());
		Utilities.dropItem(ENCHANTED_BOOK.fetch(block), block.getLocation());
		Utilities.dropItem(new ItemStack(Material.BOOK, BOOKS.fetch(block)), block.getLocation());
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		final int slot = event.getSlot();
		final ItemStack cursor = event.getCursor();
		if(slot == 20)
			return false;
		if(slot == 21)
			return !Utilities.isEmpty(cursor) && cursor.getType() != Material.ENCHANTED_BOOK;
		if(slot == 23 || slot == 24)
			return !Utilities.isEmpty(cursor);
		return true;
	}


	@Override
	public void tick(TileState block) {
		Inventory inventory = this.inventories.get(block.getLocation());
		INPUT.store(block, inventory.getItem(20));
		OUTPUT.store(block, inventory.getItem(23));
		ENCHANTED_BOOK.store(block, inventory.getItem(21));
		ItemStack bookstack = inventory.getItem(24);
		int books = Utilities.isEmpty(bookstack) ? 0 : bookstack.getAmount();
		BOOKS.store(block, books);

		int progress = PROGRESS.fetch(block);
		ItemStack processing = PROCESSING.fetch(block);
		if(progress < 100 && !Utilities.isEmpty(processing))
			progress++;
		if(progress >= 100 && !Utilities.isEmpty(processing)) {
			if(Utilities.isEmpty(OUTPUT.fetch(block)) && books < 64) {
				ItemStack book = ENCHANTED_BOOK_PROCESSING.fetch(block);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
				Map<Enchantment, Integer> map = meta.getStoredEnchants();
				for(Enchantment e : map.keySet()) {
					int l = map.get(e);
					if(processing.getEnchantmentLevel(e) < l)
						processing.addUnsafeEnchantment(e, l);
				}
				this.setOutput(block, processing);
				PROCESSING.storeEmpty(block);
				ENCHANTED_BOOK_PROCESSING.storeEmpty(block);
				processing = null;
				this.setBooks(block, books + 1);
				progress = 0;
			}
		}
		if(Utilities.isEmpty(processing)) {
			ItemStack input = INPUT.fetch(block);
			ItemStack book = ENCHANTED_BOOK.fetch(block);
			if(!Utilities.isEmpty(book) && !Utilities.isEmpty(input)) {
				PROCESSING.store(block, input);
				ENCHANTED_BOOK_PROCESSING.store(block, book);
				this.setProgress(block, 0);
				input.setAmount(input.getAmount() - 1);
				this.setInput(block, input);
				this.setEnchantedBook(block, null);
			}
		}
		this.setProgress(block, progress);
		block.update();
	}


	private void setProgress(TileState block, int progress) {
		String string = Utilities.colorizeProgress(progress, 0.0D, 100.0D, "4c6ea2".toCharArray());
		this.inventories.get(block.getLocation()).setItem(22, new ItemBuilder(Material.ENCHANTED_BOOK).setName("§bProgress: " + string).build());
		PROGRESS.store(block, progress);
	}


	private void setInput(TileState block, ItemStack input) {
		this.inventories.get(block.getLocation()).setItem(20, input);
		INPUT.store(block, input);
	}


	private void setEnchantedBook(TileState block, ItemStack book) {
		this.inventories.get(block.getLocation()).setItem(21, book);
		ENCHANTED_BOOK.store(block, book);
	}


	private void setOutput(TileState block, ItemStack output) {
		this.inventories.get(block.getLocation()).setItem(23, output);
		OUTPUT.store(block, output);
	}


	private void setBooks(TileState block, int books) {
		this.inventories.get(block.getLocation()).setItem(24, new ItemStack(Material.BOOK, books));
		BOOKS.store(block, books);
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}

}
