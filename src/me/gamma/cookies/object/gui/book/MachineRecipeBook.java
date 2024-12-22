
package me.gamma.cookies.object.gui.book;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.machine.AbstractCraftingMachine;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.MachineItem;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.collection.Pair;



public class MachineRecipeBook implements Book<Pair<TileState, MachineItem>> {

	public static final String KEY_LOCATION = "location";
	public static final String KEY_WORLD = "world";

	@Override
	public String getIdentifier() {
		return "machine_recipe_book";
	}


	@Override
	public String getTitle(int page, Pair<TileState, MachineItem> data) {
		return "§9Machine Recipes";
	}


	@Override
	public int pages(Pair<TileState, MachineItem> data) {
		return (((AbstractCraftingMachine) data.right.getBlock()).getMachineRecipes(data.left).size() + 27) / 28;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int getTurnLeftSlot() {
		return 47;
	}


	@Override
	public int getTurnRightSlot() {
		return 51;
	}


	@Override
	public int getCloseSlot() {
		return 4;
	}


	@Override
	public ItemStack getTurnLeftIcon() {
		return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§3<---").build();
	}


	@Override
	public ItemStack getTurnRightIcon() {
		return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§3--->").build();
	}


	@Override
	public ItemStack getCloseIcon() {
		return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§9<-- §1Back").build();
	}


	@Override
	public Inventory createGui(BookData<Pair<TileState, MachineItem>> data) {
		Inventory gui = Book.super.createGui(data);
		InventoryUtils.fillLeftRight(gui, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, Pair<TileState, MachineItem> data) {
		return CollectionUtils.subList(((AbstractCraftingMachine) data.right.getBlock()).getMachineRecipes(data.left), page * 28, 28).stream().map(r -> r.createIcon(cycle)).toArray(ItemStack[]::new);
	}


	@Override
	public boolean updateItems() {
		return true;
	}


	@Override
	public ItemStack getItemFiller() {
		return InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
	}


	@Override
	public Pair<TileState, MachineItem> loadData(Inventory inventory) {
		if(inventory.getSize() <= 2)
			return null;

		ItemStack stack = inventory.getItem(2);
		if(ItemUtils.isEmpty(stack))
			return null;

		AbstractCustomItem item = Items.getCustomItemFromStack(stack);
		if(!(item instanceof MachineItem machine))
			return null;

		Location location = InventoryUtils.getLocationFromStack(stack, KEY_LOCATION, KEY_WORLD);
		if(location == null || !(location.getBlock().getState() instanceof TileState block))
			return new Pair<>(null, machine);

		return new Pair<>(block, machine);
	}


	@Override
	public void saveData(Inventory inventory, Pair<TileState, MachineItem> data) {
		ItemStack item = data.right.get();
		if(data.left != null)
			InventoryUtils.storeLocationInStack(item, KEY_LOCATION, KEY_WORLD, data.left.getLocation());
		inventory.setItem(2, item);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {
		int slot = event.getSlot();
		int row = slot / 9;
		int column = slot - row * 9;
		int index = row * 7 + column - 8;
		Pair<TileState, MachineItem> data = this.loadData(gui);
		MachineRecipe recipe = ((AbstractCraftingMachine) data.right.getBlock()).getMachineRecipes(data.left).get(page * 28 + index);
		Inventory inventory = Bukkit.createInventory(null, 5 * 9, MachineRecipe.MACHINE_RECIPE_TITLE);
		RecipeUtils.initializeRecipeInventory(inventory);
		inventory.setItem(4, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§cBack").build());
		History.proceed(player, RecipeUtils.getInventoryTask(inventory, ResultChoice.ORDERED, recipe));
	}


	@Override
	public void close(HumanEntity player, Inventory inventory) {
		History.travelBack(player);
	}


	public static void openBook(HumanEntity player, Pair<TileState, MachineItem> machine) {
		BookInit.MACHINE_RECIPE_BOOK.open(player, machine);
	}

}
