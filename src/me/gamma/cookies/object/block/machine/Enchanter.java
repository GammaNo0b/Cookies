
package me.gamma.cookies.object.block.machine;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;



public class Enchanter extends AbstractProcessingMachine implements ItemConsumer, ItemSupplier {

	public static final ItemStackProperty INPUT = Properties.INPUT;
	public static final ItemStackProperty OUTPUT = Properties.OUTPUT;
	public static final ItemStackProperty ENCHANTED_BOOK = new ItemStackProperty("enchantedbook");
	public static final ItemStackProperty PROCESSING = Properties.PROCESSING;
	public static final ItemStackProperty ENCHANTED_BOOK_PROCESSING = new ItemStackProperty("enchantedbookprocessing");
	public static final IntegerProperty BOOKS = new IntegerProperty("books");

	public static final int INPUT_SLOT = 20;
	public static final int ENCHANTED_BOOK_SLOT = 21;
	public static final int OUTPUT_SLOT = 23;
	public static final int BOOKS_SLOT = 24;

	public static final int ENCHANT_DURATION = 200;

	public Enchanter() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§bEnchanter";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ENCHANTMENT_TABLE;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i : new int[] { 0, 1, 7, 8, 9, 17 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.RED_STAINED_GLASS_PANE);
		for(int i : new int[] { 2, 6 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : new int[] { 3, 5 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i : new int[] { 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] { 10, 11, 12, 19, 28, 29, 30 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.LIME_STAINED_GLASS_PANE);
		for(int i : new int[] { 14, 15, 16, 25, 32, 33, 34 })
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		inventory.setItem(INPUT_SLOT, INPUT.fetch(block));
		inventory.setItem(ENCHANTED_BOOK_SLOT, ENCHANTED_BOOK.fetch(block));
		inventory.setItem(OUTPUT_SLOT, OUTPUT.fetch(block));
		inventory.setItem(BOOKS_SLOT, new ItemStack(Material.BOOK, BOOKS.fetch(block)));
		super.setupInventory(block, inventory);
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		INPUT.store(block, inventory.getItem(INPUT_SLOT));
		ENCHANTED_BOOK.store(block, inventory.getItem(ENCHANTED_BOOK_SLOT));
		OUTPUT.store(block, inventory.getItem(OUTPUT_SLOT));
		ItemStack bookstack = inventory.getItem(BOOKS_SLOT);
		BOOKS.store(block, ItemUtils.isEmpty(bookstack) ? 0 : bookstack.getAmount());
		block.update();
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(INPUT).add(OUTPUT).add(ENCHANTED_BOOK).add(PROCESSING).add(ENCHANTED_BOOK_PROCESSING).add(BOOKS).add(ITEM_INPUT_ACCESS_FLAGS, (byte) 0x3F).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}

	@Override
	public void tick(TileState block) {
		this.tryPullItems(block);
		super.tick(block);
	}

	@Override
	protected int createNextProcess(TileState block) {

		Inventory gui = this.getGui(block);

		ItemStack stack = gui.getItem(INPUT_SLOT);
		if(ItemUtils.isEmpty(stack))
			return 0;

		if(!ItemUtils.isCustomItem(stack) && stack.getType() == Material.GOLD_INGOT) {
			PROCESSING.store(block, stack);
			ItemUtils.increaseItem(gui.getItem(INPUT_SLOT), -1);
		} else {
			ItemStack book = gui.getItem(ENCHANTED_BOOK_SLOT);
			if(!ItemUtils.isType(book, Material.ENCHANTED_BOOK))
				return 0;

			PROCESSING.store(block, stack);
			ENCHANTED_BOOK_PROCESSING.store(block, book);
			ItemUtils.increaseItem(gui.getItem(INPUT_SLOT), -1);
			gui.setItem(ENCHANTED_BOOK_SLOT, null);
		}
		block.update();

		return ENCHANT_DURATION;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {}


	@Override
	protected boolean finishProcess(TileState block) {
		Inventory gui = this.getGui(block);

		ItemStack stack = PROCESSING.fetch(block);
		if(!ItemUtils.isCustomItem(stack) && stack.getType() == Material.GOLD_INGOT) {
			ItemStack result = Items.MAGIC_METAL.get();
			ItemStack output = gui.getItem(OUTPUT_SLOT);
			if(ItemUtils.isEmpty(output)) {
				gui.setItem(OUTPUT_SLOT, result);
			} else if(ItemUtils.equals(result, output) && output.getAmount() < output.getMaxStackSize()) {
				ItemUtils.increaseItem(output, 1);
			} else {
				return false;
			}

			PROCESSING.storeEmpty(block);
			block.update();

			return true;
		}

		ItemStack books = gui.getItem(BOOKS_SLOT);
		if(!ItemUtils.isEmpty(gui.getItem(OUTPUT_SLOT)) || !ItemUtils.isEmpty(books) && (!ItemUtils.isType(books, Material.BOOK) || books.getAmount() >= books.getMaxStackSize()))
			return false;

		ItemStack book = ENCHANTED_BOOK_PROCESSING.fetch(block);

		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		for(Enchantment enchantment : meta.getStoredEnchants().keySet())
			stack.addUnsafeEnchantment(enchantment, meta.getStoredEnchantLevel(enchantment));

		PROCESSING.storeEmpty(block);
		ENCHANTED_BOOK_PROCESSING.storeEmpty(block);

		if(ItemUtils.isEmpty(books)) {
			gui.setItem(BOOKS_SLOT, new ItemStack(Material.BOOK));
		} else {
			books.setAmount(books.getAmount() + 1);
		}
		gui.setItem(OUTPUT_SLOT, stack);
		this.tryPushItems(block);
		block.update();

		return true;
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs(TileState block) {
		return List.of(ItemProvider.fromInventory(this.getGui(block), INPUT_SLOT), ItemProvider.fromInventory(this.getGui(block), ENCHANTED_BOOK_SLOT, Material.ENCHANTED_BOOK));
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(TileState block) {
		return ItemProvider.fromInventory(this.getGui(block), OUTPUT_SLOT, BOOKS_SLOT);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemUtils.dropItem(INPUT.fetch(block), block.getLocation());
		ItemUtils.dropItem(OUTPUT.fetch(block), block.getLocation());
		ItemUtils.dropItem(PROCESSING.fetch(block), block.getLocation());
		ItemUtils.dropItem(ENCHANTED_BOOK_PROCESSING.fetch(block), block.getLocation());
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


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.ENCHANTED_BOOK;
	}


	@Override
	public String getMachineRegistryName() {
		return "enchanter";
	}

}
