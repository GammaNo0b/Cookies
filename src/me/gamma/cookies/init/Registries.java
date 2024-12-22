
package me.gamma.cookies.init;


import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.gui.InventoryHandler;
import me.gamma.cookies.object.gui.book.Book;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.multiblock.MultiBlock;



public class Registries {

	public static final Registry<Registry<?>> REGISTRIES = new Registry<>();

	public static final Registry<WorldPersistentDataStorage> STORAGES = register();
	public static final Registry<AbstractCustomItem> ITEMS = register();
	public static final Registry<AbstractCustomBlock> BLOCKS = register();
	public static final Registry<Config> CONFIGS = register();
	public static final Registry<MultiBlock> MULTIBLOCKS = register(MultiBlockInit.MULTIBLOCKS);
	public static final Registry<InventoryHandler> INVENTORY_HANDLERS = register();
	public static final Registry<Book<?>> BOOKS = register();

	public static <T> Registry<T> register() {
		return register(new Registry<>());
	}


	public static <T> Registry<T> register(Registry<T> registry) {
		REGISTRIES.register(registry);
		return registry;
	}

}
