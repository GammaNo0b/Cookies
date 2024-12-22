
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.BOOKS;

import me.gamma.cookies.object.gui.book.CookieCategoryBook;
import me.gamma.cookies.object.gui.book.CookieMenuBook;
import me.gamma.cookies.object.gui.book.ItemFilterBook;
import me.gamma.cookies.object.gui.book.MachineRecipeBook;
import me.gamma.cookies.object.gui.book.MultiBlockBook;
import me.gamma.cookies.object.gui.book.MultiBlockBuildingBook;
import me.gamma.cookies.object.gui.book.RecipeBook;
import me.gamma.cookies.object.gui.book.StorageBook;
import me.gamma.cookies.object.gui.book.VanillaRecipeBook;



public class BookInit {

	public static VanillaRecipeBook VANILLA_RECIPE_BOOK;
	public static CookieMenuBook COOKIE_MENU_BOOK;
	public static CookieCategoryBook COOKIE_CATEGORY_BOOK;
	public static MachineRecipeBook MACHINE_RECIPE_BOOK;
	public static RecipeBook RECIPE_BOOK;
	public static MultiBlockBook MULTI_BLOCK_BOOK;
	public static MultiBlockBuildingBook MULTI_BLOCK_BUILDING_BOOK;
	public static StorageBook STORAGE_BOOK;
	public static ItemFilterBook ITEM_FILTER_BOOK;

	public static void init() {
		VANILLA_RECIPE_BOOK = BOOKS.register(new VanillaRecipeBook());
		COOKIE_MENU_BOOK = BOOKS.register(new CookieMenuBook());
		COOKIE_CATEGORY_BOOK = BOOKS.register(new CookieCategoryBook());
		MACHINE_RECIPE_BOOK = BOOKS.register(new MachineRecipeBook());
		RECIPE_BOOK = BOOKS.register(new RecipeBook());
		MULTI_BLOCK_BOOK = BOOKS.register(new MultiBlockBook());
		MULTI_BLOCK_BUILDING_BOOK = BOOKS.register(new MultiBlockBuildingBook());
		STORAGE_BOOK = BOOKS.register(new StorageBook());
		ITEM_FILTER_BOOK = BOOKS.register(new ItemFilterBook());
	}

}
