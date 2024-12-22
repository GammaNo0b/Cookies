
package me.gamma.cookies.object.block.machine;


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



public class Disenchanter extends AbstractProcessingMachine {

	public static final ItemStackProperty INPUT = Properties.INPUT;
	public static final ItemStackProperty OUTPUT = Properties.OUTPUT;
	public static final ItemStackProperty PROCESSING = Properties.PROCESSING;
	public static final ItemStackProperty ENCHANTED_BOOK = new ItemStackProperty("enchantedbook");
	public static final IntegerProperty BOOKS = new IntegerProperty("books");

	public static final int INPUT_SLOT = 20;
	public static final int BOOKS_SLOT = 21;
	public static final int OUTPUT_SLOT = 23;
	public static final int ENCHANTED_BOOK_SLOT = 24;

	public static final int DISENCHANT_DURATION = 200;

	public Disenchanter() {
		super(null);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		inventory.setItem(INPUT_SLOT, INPUT.fetch(block));
		inventory.setItem(BOOKS_SLOT, new ItemStack(Material.BOOK, BOOKS.fetch(block)));
		inventory.setItem(OUTPUT_SLOT, OUTPUT.fetch(block));
		inventory.setItem(ENCHANTED_BOOK_SLOT, ENCHANTED_BOOK.fetch(block));
		super.setupInventory(block, inventory);
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		INPUT.store(block, inventory.getItem(INPUT_SLOT));
		ItemStack bookstack = inventory.getItem(BOOKS_SLOT);
		BOOKS.store(block, ItemUtils.isEmpty(bookstack) ? 0 : bookstack.getAmount());
		OUTPUT.store(block, inventory.getItem(OUTPUT_SLOT));
		ENCHANTED_BOOK.store(block, inventory.getItem(ENCHANTED_BOOK_SLOT));
		block.update();
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(INPUT).add(OUTPUT).add(PROCESSING).add(ENCHANTED_BOOK).add(BOOKS);
	}


	@Override
	protected int createNextProcess(TileState block) {
		Inventory gui = this.getGui(block);

		ItemStack stack = gui.getItem(INPUT_SLOT);
		if(ItemUtils.isEmpty(stack) || stack.getEnchantments().isEmpty())
			return 0;

		ItemStack books = gui.getItem(BOOKS_SLOT);
		if(!ItemUtils.isType(books, Material.BOOK))
			return 0;

		books.setAmount(books.getAmount() - 1);
		PROCESSING.store(block, stack);
		gui.setItem(INPUT_SLOT, null);
		block.update();

		return DISENCHANT_DURATION;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {}


	@Override
	protected boolean finishProcess(TileState block) {
		Inventory gui = this.getGui(block);

		if(!ItemUtils.isEmpty(gui.getItem(OUTPUT_SLOT)) || !ItemUtils.isEmpty(gui.getItem(ENCHANTED_BOOK_SLOT)))
			return false;

		ItemStack stack = PROCESSING.fetch(block);
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);

		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		for(Enchantment enchantment : stack.getEnchantments().keySet())
			meta.addStoredEnchant(enchantment, stack.removeEnchantment(enchantment), true);
		book.setItemMeta(meta);

		PROCESSING.storeEmpty(block);
		gui.setItem(OUTPUT_SLOT, stack);
		gui.setItem(ENCHANTED_BOOK_SLOT, book);
		block.update();

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return progress < 0.9D ? Material.BOOK : Material.ENCHANTED_BOOK;
	}


	@Override
	public String getMachineRegistryName() {
		return "disenchanter";
	}


	@Override
	public String getTitle() {
		return "§bDisenchanter";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.DISENCHANTMENT_TABLE;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, null);
		ItemStack filler = InventoryUtils.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i : new int[] { 0, 1, 7, 8, 9, 17 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.YELLOW_STAINED_GLASS_PANE);
		for(int i : new int[] { 2, 6 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : new int[] { 3, 5 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i : new int[] { 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44 })
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
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemUtils.dropItem(INPUT.fetch(block), block.getLocation());
		ItemUtils.dropItem(OUTPUT.fetch(block), block.getLocation());
		ItemStack processing = PROCESSING.fetch(block);
		ItemUtils.dropItem(processing, block.getLocation());
		if(!ItemUtils.isEmpty(processing))
			ItemUtils.dropItem(new ItemStack(Material.BOOK), block.getLocation());
		ItemUtils.dropItem(ENCHANTED_BOOK.fetch(block), block.getLocation());
		ItemUtils.dropItem(new ItemStack(Material.BOOK, BOOKS.fetch(block)), block.getLocation());
		return super.onBlockBreak(player, block, event);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, block, gui, event);
		int slot = event.getSlot();
		return slot != INPUT_SLOT && slot != BOOKS_SLOT && slot != OUTPUT_SLOT && slot != ENCHANTED_BOOK_SLOT;
	}

}
