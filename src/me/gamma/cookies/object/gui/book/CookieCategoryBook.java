
package me.gamma.cookies.object.gui.book;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.book.CookieCategoryBook.CategoryInformation;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.recipe.RecipeCategory;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;



public class CookieCategoryBook implements Book<CategoryInformation> {

	public static final int CATEGORY_SLOT = 2;
	public static final String CHEATING_KEY = "cheating";

	@Override
	public String getIdentifier() {
		return "cookie_category_book";
	}


	@Override
	public String getTitle(int page, CategoryInformation data) {
		return data.category.getName();
	}


	@Override
	public int pages(CategoryInformation data) {
		return (data.category.getItems().size() + 27) / 28;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Inventory createGui(BookData<CategoryInformation> data) {
		Inventory gui = Book.super.createGui(data);
		InventoryUtils.fillLeftRight(gui, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		return gui;
	}


	@Override
	public void setupControls(Inventory gui, int page, CategoryInformation data) {
		Book.super.setupControls(gui, page, data);
		gui.setItem(CATEGORY_SLOT, data.category.getIcon());
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, CategoryInformation data) {
		return CollectionUtils.subList(data.category.getItems(), page * 28, 28).stream().map(s -> {
			s.setAmount(1);
			return s;
		}).toArray(ItemStack[]::new);
	}


	@Override
	public CategoryInformation loadData(Inventory inventory) {
		RecipeCategory category = RecipeCategory.getCategoryFromIconStack(inventory.getItem(CATEGORY_SLOT));
		boolean cheating = InventoryUtils.isMarked(this.getIdentifierStack(inventory), CHEATING_KEY);
		return new CategoryInformation(category, cheating);
	}


	@Override
	public void saveData(Inventory inventory, CategoryInformation data) {
		if(data.cheating)
			InventoryUtils.markItem(this.getIdentifierStack(inventory), CHEATING_KEY);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {
		CategoryInformation data = this.loadData(gui);
		if(data.cheating) {
			player.getInventory().addItem(this.getItem(gui, event.getSlot()));
		} else {
			RecipeBook.openBook(player, stack, ResultChoice.ORDERED);
		}
	}


	@Override
	public void close(HumanEntity player, Inventory inventory) {
		History.travelBack(player);
	}

	public static class CategoryInformation {

		private final RecipeCategory category;
		private final boolean cheating;

		public CategoryInformation(RecipeCategory category, boolean cheating) {
			this.category = category;
			this.cheating = cheating;
		}

	}

	public static void openBook(HumanEntity player, RecipeCategory category, boolean cheating) {
		BookInit.COOKIE_CATEGORY_BOOK.open(player, new CategoryInformation(category, cheating));
	}

}
