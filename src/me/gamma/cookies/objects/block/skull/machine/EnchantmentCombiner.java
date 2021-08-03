
package me.gamma.cookies.objects.block.skull.machine;


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



public class EnchantmentCombiner extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable, ItemConsumer, ItemSupplier {

	public static final IntegerProperty PROGRESS = new IntegerProperty("progress");
	public static final ItemStackProperty ENCHANTED_BOOK_1 = new ItemStackProperty("enchantedbook1");
	public static final ItemStackProperty ENCHANTED_BOOK_2 = new ItemStackProperty("enchantedbook2");
	public static final ItemStackProperty PROCESSING_1 = new ItemStackProperty("processing1");
	public static final ItemStackProperty PROCESSING_2 = new ItemStackProperty("processing2");
	public static final ItemStackProperty OUTPUT = new ItemStackProperty("output");
	public static final IntegerProperty BOOKS = new IntegerProperty("books");

	private final HashSet<Location> locations = new HashSet<>();
	private final HashMap<Location, Inventory> inventories = new HashMap<>();

	public EnchantmentCombiner() {
		register();
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
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
	public List<ItemProvider> getInputStackHolders(TileState block) {
		return ItemProvider.fromInventory(this.inventories.get(block.getLocation()), 20, 21);
	}


	@Override
	public List<ItemProvider> getOutputStackHolders(TileState block) {
		return ItemProvider.fromInventory(this.inventories.get(block.getLocation()), 23, 24);
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
				gui.setItem(20, ENCHANTED_BOOK_1.fetch(block));
				gui.setItem(21, ENCHANTED_BOOK_2.fetch(block));
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
				ENCHANTED_BOOK_1.store(block, gui.getItem(20));
				ENCHANTED_BOOK_2.store(block, gui.getItem(21));
				OUTPUT.store(block, gui.getItem(23));
				ItemStack bookstack = gui.getItem(24);
				BOOKS.store(block, Utilities.isEmpty(bookstack) ? 0 : bookstack.getAmount());
				block.update();
			}
		}
	}


	private Inventory createInventory(TileState block) {
		Inventory gui = super.createMainGui(null, block);
		ItemStack filler = InventoryManager.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i : new int[] {
			0, 1, 7, 8, 9, 17
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] {
			2, 3, 5, 6
		})
			gui.setItem(i, filler);
		filler = InventoryManager.filler(Material.GRAY_STAINED_GLASS_PANE);
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
		ENCHANTED_BOOK_1.storeEmpty(block);
		ENCHANTED_BOOK_2.storeEmpty(block);
		PROCESSING_1.storeEmpty(block);
		PROCESSING_2.storeEmpty(block);
		OUTPUT.storeEmpty(block);
		BOOKS.storeEmpty(block);
		super.onBlockPlace(player, usedItem, block, event);
		this.inventories.put(block.getLocation(), this.createInventory(block));
		this.setProgress(block, 0);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		this.inventories.remove(block.getLocation());
		Utilities.dropItem(ENCHANTED_BOOK_1.fetch(block), block.getLocation());
		Utilities.dropItem(ENCHANTED_BOOK_2.fetch(block), block.getLocation());
		Utilities.dropItem(PROCESSING_1.fetch(block), block.getLocation());
		Utilities.dropItem(PROCESSING_2.fetch(block), block.getLocation());
		Utilities.dropItem(OUTPUT.fetch(block), block.getLocation());
		Utilities.dropItem(new ItemStack(Material.BOOK, BOOKS.fetch(block)), block.getLocation());
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		final int slot = event.getSlot();
		final ItemStack cursor = event.getCursor();
		if(slot == 20 || slot == 21)
			return !Utilities.isEmpty(cursor) && cursor.getType() != Material.ENCHANTED_BOOK;
		if(slot == 23 || slot == 24)
			return !Utilities.isEmpty(cursor);
		return true;
	}


	@Override
	public void tick(TileState block) {
		Inventory inventory = this.inventories.get(block.getLocation());
		ENCHANTED_BOOK_1.store(block, inventory.getItem(20));
		ENCHANTED_BOOK_2.store(block, inventory.getItem(21));
		OUTPUT.store(block, inventory.getItem(23));
		ItemStack bookstack = inventory.getItem(24);
		int books = Utilities.isEmpty(bookstack) ? 0 : bookstack.getAmount();
		BOOKS.store(block, books);

		int progress = PROGRESS.fetch(block);
		ItemStack processing1 = PROCESSING_1.fetch(block);
		ItemStack processing2 = PROCESSING_2.fetch(block);
		if(progress < 100 && !Utilities.isEmpty(processing1))
			progress++;
		if(progress >= 100 && !Utilities.isEmpty(processing1)) {
			if(Utilities.isEmpty(OUTPUT.fetch(block)) && books < 64) {
				ItemStack combined = new ItemStack(Material.ENCHANTED_BOOK);
				EnchantmentStorageMeta meta = (EnchantmentStorageMeta) combined.getItemMeta();
				EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) processing1.getItemMeta();
				EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) processing2.getItemMeta();
				Map<Enchantment, Integer> map1 = meta1.getStoredEnchants();
				Map<Enchantment, Integer> map2 = meta2.getStoredEnchants();
				Set<Enchantment> enchantments = new HashSet<>();
				enchantments.addAll(map1.keySet());
				enchantments.addAll(map2.keySet());
				for(Enchantment e : enchantments) {
					Integer l1 = map1.get(e);
					Integer l2 = map2.get(e);
					int l = 0;
					if(l1 == null) {
						l = l2;
					} else if(l2 == null) {
						l = l1;
					} else if(l1 == l2) {
						l = l1 + 1;
					} else {
						l = Math.max(l1, l2);
					}
					meta.addStoredEnchant(e, l, true);
				}
				combined.setItemMeta(meta);
				this.setOutput(block, combined);
				PROCESSING_1.storeEmpty(block);
				PROCESSING_2.storeEmpty(block);
				processing1 = null;
				this.setBooks(block, books + 1);
				progress = 0;
			}
		}
		if(Utilities.isEmpty(processing1)) {
			ItemStack book1 = ENCHANTED_BOOK_1.fetch(block);
			ItemStack book2 = ENCHANTED_BOOK_2.fetch(block);
			if(!Utilities.isEmpty(book1) && !Utilities.isEmpty(book2)) {
				PROCESSING_1.store(block, book1);
				PROCESSING_2.store(block, book2);
				this.setProgress(block, 0);
				book1.setAmount(book1.getAmount() - 1);
				book2.setAmount(book2.getAmount() - 1);
				this.setFirstEnchantedBook(block, book1);
				this.setSecondEnchantedBook(block, book2);
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


	private void setFirstEnchantedBook(TileState block, ItemStack input) {
		this.inventories.get(block.getLocation()).setItem(20, input);
		ENCHANTED_BOOK_1.store(block, input);
	}


	private void setSecondEnchantedBook(TileState block, ItemStack book) {
		this.inventories.get(block.getLocation()).setItem(21, book);
		ENCHANTED_BOOK_2.store(block, book);
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


	@Override
	public String getBlockTexture() {
		return HeadTextures.ENCHANTMENT_COMBINER;
	}


	@Override
	public String getRegistryName() {
		return "enchantment_combiner";
	}


	@Override
	public String getDisplayName() {
		return "§6Enchantment Combiner";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("NDN", " A ", "III");
		recipe.setIngredient('N', Material.NETHERITE_INGOT);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('A', Material.ANVIL);
		recipe.setIngredient('I', Material.IRON_BLOCK);
		return recipe;
	}

}
