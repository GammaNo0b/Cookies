
package me.gamma.cookies.object.gui.book;


import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.object.gui.book.VanillaRecipeBook.SortType;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class VanillaRecipeBook implements Book<SortType> {

	private static final int SORT_TYPE_SLOT = 26;

	static {
		SortType.values();
	}

	@Override
	public String getIdentifier() {
		return "vanilla_recipe_book";
	}


	@Override
	public String getTitle(int page, SortType data) {
		return "§aVanilla Recipe Book";
	}


	@Override
	public int pages(SortType data) {
		return (SortType.count + 27) / 28;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Inventory createGui(BookData<SortType> data) {
		Inventory gui = Book.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.LIME_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}
		filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i = 1; i < 5; i++) {
			gui.setItem(9 * i, filler);
			gui.setItem(9 * i + 8, filler);
		}
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, SortType data) {
		return data.getItems(page * 28, 28);
	}


	@Override
	public ItemStack getItemFiller() {
		return InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
	}


	@Override
	public SortType loadData(Inventory inventory) {
		return SortType.fromStack(inventory.getItem(SORT_TYPE_SLOT));
	}


	public void saveData(Inventory inventory, SortType data) {
		inventory.setItem(SORT_TYPE_SLOT, data.createIcon());
	}


	@Override
	public void onInventoryClick(HumanEntity player, Inventory gui, ItemStack stack, int slot, InventoryClickEvent event, int page) {
		if(slot == SORT_TYPE_SLOT)
			this.openGui(player, new BookData<>(page, EnumUtils.cycle(this.loadData(gui))), false, true);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {
		RecipeBook.openBook(player, stack, ResultChoice.ORDERED);
	}


	public static void openBook(HumanEntity player) {
		BookInit.VANILLA_RECIPE_BOOK.open(player, SortType.DEFAULT);
	}

	public static enum SortType {

		DEFAULT(Material.GRASS_BLOCK, (m1, m2) -> m1.ordinal() - m2.ordinal()),
		NAME(Material.NAME_TAG, (m1, m2) -> m1.name().compareTo(m2.name())),
		MAX_STACKSIZE(Material.MAP, (m1, m2) -> m1.getMaxStackSize() - m2.getMaxStackSize());

		private static final int count = (int) Arrays.stream(Material.values()).filter(Material::isItem).filter(Predicate.not(Material::isAir)).filter(m -> Bukkit.getRecipesFor(new ItemStack(m)).size() > 0).count();

		private final Material icon;
		private final ItemStack[] materials;
		private final String name;

		private SortType(Material icon, Comparator<Material> comparator) {
			this.icon = icon;
			this.materials = Arrays.stream(Material.values()).filter(Material::isItem).filter(Predicate.not(Material::isAir)).filter(m -> Bukkit.getRecipesFor(new ItemStack(m)).size() > 0).sorted(comparator).map(ItemStack::new).toArray(ItemStack[]::new);
			this.name = Utils.toCapitalWords(this);
		}


		public ItemStack[] getItems(int start, int max) {
			int length = Math.min(max, this.materials.length - start);
			ItemStack[] items = new ItemStack[length];
			for(int i = 0, j = start; i < length; i++, j++)
				items[i] = new ItemStack(this.materials[j]);
			return items;
		}


		public ItemStack createIcon() {
			ItemStack icon = new ItemBuilder(this.icon).setName("§3" + this.name).build();
			InventoryUtils.storeIntInStack(icon, "sorttype", this.ordinal());
			return icon;
		}


		public SortType loop() {
			return values()[(this.ordinal() + 1) % values().length];
		}


		public static SortType fromStack(ItemStack stack) {
			for(SortType type : values())
				if(stack.getType() == type.icon)
					return type;
			return null;
		}

	}

}
