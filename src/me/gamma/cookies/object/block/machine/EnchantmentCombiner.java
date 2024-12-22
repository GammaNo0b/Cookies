
package me.gamma.cookies.object.block.machine;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;



public class EnchantmentCombiner extends AbstractProcessingMachine {

	public static final IntegerProperty PROGRESS = Properties.PROGRESS;
	public static final ItemStackProperty ENCHANTED_BOOK_1 = new ItemStackProperty("enchantedbook1");
	public static final ItemStackProperty ENCHANTED_BOOK_2 = new ItemStackProperty("enchantedbook2");
	public static final ItemStackProperty PROCESSING_1 = new ItemStackProperty("processing1");
	public static final ItemStackProperty PROCESSING_2 = new ItemStackProperty("processing2");
	public static final ItemStackProperty OUTPUT = Properties.OUTPUT;
	public static final IntegerProperty BOOKS = new IntegerProperty("books");

	public static final int ENCHANTED_BOOK_SLOT_1 = 20;
	public static final int ENCHANTED_BOOK_SLOT_2 = 21;
	public static final int OUTPUT_SLOT = 23;
	public static final int BOOKS_SLOT = 24;

	public static final int COMBINING_DURATION = 200;

	public EnchantmentCombiner() {
		super(null);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		inventory.setItem(20, ENCHANTED_BOOK_1.fetch(block));
		inventory.setItem(21, ENCHANTED_BOOK_2.fetch(block));
		inventory.setItem(23, OUTPUT.fetch(block));
		inventory.setItem(24, new ItemStack(Material.BOOK, BOOKS.fetch(block)));
		super.setupInventory(block, inventory);
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		ENCHANTED_BOOK_1.store(block, inventory.getItem(20));
		ENCHANTED_BOOK_2.store(block, inventory.getItem(21));
		OUTPUT.store(block, inventory.getItem(23));
		ItemStack bookstack = inventory.getItem(24);
		BOOKS.store(block, ItemUtils.isEmpty(bookstack) ? 0 : bookstack.getAmount());
		block.update();
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i : new int[] { 0, 1, 7, 8, 9, 17 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] { 2, 3, 4, 5, 6 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		for(int i : new int[] { 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] { 10, 11, 12, 19, 28, 29, 30 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.LIME_STAINED_GLASS_PANE);
		for(int i : new int[] { 14, 15, 16, 25, 32, 33, 34 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : new int[] { 13, 31 })
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(ENCHANTED_BOOK_1).add(ENCHANTED_BOOK_2).add(PROCESSING_1).add(PROCESSING_2).add(OUTPUT).add(BOOKS);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemUtils.dropItem(ENCHANTED_BOOK_1.fetch(block), block.getLocation());
		ItemUtils.dropItem(ENCHANTED_BOOK_2.fetch(block), block.getLocation());
		ItemUtils.dropItem(PROCESSING_1.fetch(block), block.getLocation());
		ItemUtils.dropItem(PROCESSING_2.fetch(block), block.getLocation());
		ItemUtils.dropItem(OUTPUT.fetch(block), block.getLocation());
		ItemUtils.dropItem(new ItemStack(Material.BOOK, BOOKS.fetch(block)), block.getLocation());
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, block, gui, event);
		int slot = event.getSlot();
		return slot != ENCHANTED_BOOK_SLOT_1 && slot != ENCHANTED_BOOK_SLOT_2 && slot != OUTPUT_SLOT && slot != BOOKS_SLOT;
	}


	@Override
	public String getTitle() {
		return "§bEnchantment Combiner";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ENCHANTMENT_COMBINER;
	}


	@Override
	protected int createNextProcess(TileState block) {
		Inventory gui = this.getGui(block);

		ItemStack book1 = gui.getItem(ENCHANTED_BOOK_SLOT_1);
		if(ItemUtils.isEmpty(book1) || !ItemUtils.isType(book1, Material.ENCHANTED_BOOK))
			return 0;

		ItemStack book2 = gui.getItem(ENCHANTED_BOOK_SLOT_2);
		if(ItemUtils.isEmpty(book2) || !ItemUtils.isType(book2, Material.ENCHANTED_BOOK))
			return 0;

		PROCESSING_1.store(block, book1);
		PROCESSING_2.store(block, book2);
		gui.setItem(ENCHANTED_BOOK_SLOT_1, null);
		gui.setItem(ENCHANTED_BOOK_SLOT_2, null);
		block.update();

		return COMBINING_DURATION;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {}


	@Override
	protected boolean finishProcess(TileState block) {
		Inventory gui = this.getGui(block);

		ItemStack books = gui.getItem(BOOKS_SLOT);
		if(!ItemUtils.isEmpty(gui.getItem(OUTPUT_SLOT)) || !ItemUtils.isEmpty(books) && (!ItemUtils.isType(books, Material.BOOK) || books.getAmount() >= books.getMaxStackSize()))
			return false;

		ItemStack book1 = PROCESSING_1.fetch(block);
		ItemStack book2 = PROCESSING_2.fetch(block);

		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);

		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) book1.getItemMeta();
		EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) book2.getItemMeta();
		Map<Enchantment, Integer> map1 = meta1.getStoredEnchants();
		Map<Enchantment, Integer> map2 = meta2.getStoredEnchants();
		Set<Enchantment> enchantments = new HashSet<>();
		enchantments.addAll(map1.keySet());
		enchantments.addAll(map2.keySet());
		for(Enchantment enchantment : enchantments) {
			Integer level1 = map1.get(enchantment);
			Integer level2 = map2.get(enchantment);
			int l = 0;
			if(level1 == null) {
				l = level2;
			} else if(level2 == null) {
				l = level1;
			} else if(level1 == level2) {
				l = level1 + 1;
			} else {
				l = Math.max(level1, level2);
			}
			meta.addStoredEnchant(enchantment, l, true);
		}
		book.setItemMeta(meta);

		PROCESSING_1.storeEmpty(block);
		PROCESSING_2.storeEmpty(block);

		if(ItemUtils.isEmpty(books)) {
			gui.setItem(BOOKS_SLOT, new ItemStack(Material.BOOK));
		} else {
			books.setAmount(books.getAmount() + 1);
		}
		gui.setItem(OUTPUT_SLOT, book);
		block.update();

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.ENCHANTED_BOOK;
	}


	@Override
	public String getMachineRegistryName() {
		return "enchantment_combiner";
	}

}
