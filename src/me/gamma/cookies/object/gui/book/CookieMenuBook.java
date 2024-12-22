
package me.gamma.cookies.object.gui.book;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.object.recipe.RecipeCategory;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;



public class CookieMenuBook implements Book<Boolean> {

	public static final String CHEATING_KEY = "cheating";

	@Override
	public String getIdentifier() {
		return "cookie_menu_book";
	}


	@Override
	public String getTitle(int page, Boolean data) {
		return "§6Cookie " + (data ? "Cheat" : "Cook") + " Book";
	}


	@Override
	public int pages(Boolean data) {
		return (RecipeCategory.categories.size() + 20) / 21;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public Inventory createGui(BookData<Boolean> data) {
		Inventory gui = Book.super.createGui(data);
		InventoryUtils.fillLeftRight(gui, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_THREE_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, Boolean data) {
		return CollectionUtils.subList(RecipeCategory.categories, page * 21, 21).stream().map(RecipeCategory::getIcon).toArray(ItemStack[]::new);
	}


	@Override
	public Boolean loadData(Inventory inventory) {
		return InventoryUtils.isMarked(this.getIdentifierStack(inventory), CHEATING_KEY);
	}


	@Override
	public void saveData(Inventory inventory, Boolean data) {
		if(data)
			InventoryUtils.markItem(this.getIdentifierStack(inventory), CHEATING_KEY);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory inventory, ItemStack stack, InventoryClickEvent event, int page) {
		RecipeCategory category = RecipeCategory.getCategoryFromIconStack(stack);
		if(category != null)
			CookieCategoryBook.openBook(player, category, this.loadData(inventory));
	}


	public static void openBook(HumanEntity player, boolean cheating) {
		BookInit.COOKIE_MENU_BOOK.open(player, cheating);
	}

}
