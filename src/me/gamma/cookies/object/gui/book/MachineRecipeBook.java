
package me.gamma.cookies.object.gui.book;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.object.block.machine.AbstractCraftingMachineBlock;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.item.resources.MachineItem;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.RecipeUtils;



public class MachineRecipeBook implements Book<AbstractCraftingMachineBlock<?, ?>> {

	public static final String KEY_LOCATION = "location";
	public static final String KEY_WORLD = "world";

	@Override
	public String getIdentifier() {
		return "machine_recipe_book";
	}


	@Override
	public String getTitle(int page, AbstractCraftingMachineBlock<?, ?> data) {
		return "§9Machine Recipes";
	}


	@Override
	public int pages(AbstractCraftingMachineBlock<?, ?> data) {
		return (data.getAllMachineRecipes().size() + 27) / 28;
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
	public Inventory createGui(BookData<AbstractCraftingMachineBlock<?, ?>> data) {
		Inventory gui = Book.super.createGui(data);
		InventoryUtils.fillLeftRight(gui, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		gui.setItem(2, new MachineItem(data.data()).get());
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, AbstractCraftingMachineBlock<?, ?> data) {
		return CollectionUtils.subList(data.getAllMachineRecipes(), page * 28, 28).stream().map(r -> r.createIcon(cycle)).toArray(ItemStack[]::new);
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
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, int page, AbstractCraftingMachineBlock<?, ?> data, InventoryClickEvent event) {
		int slot = event.getSlot();
		int row = slot / 9;
		int column = slot - row * 9;
		int index = row * 7 + column - 8;
		MachineRecipe recipe = data.getAllMachineRecipes().get(page * 28 + index);
		Inventory inventory = Bukkit.createInventory(null, 5 * 9, MachineRecipe.MACHINE_RECIPE_TITLE);
		RecipeUtils.initializeRecipeInventory(inventory);
		inventory.setItem(4, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§cBack").build());
		History.proceed(player, RecipeUtils.getInventoryTask(inventory, ResultChoice.ORDERED, recipe));
	}


	@Override
	public void close(HumanEntity player, Inventory inventory) {
		History.travelBack(player);
	}


	public static void openBook(HumanEntity player, AbstractCraftingMachineBlock<?, ?> machine) {
		BookInit.MACHINE_RECIPE_BOOK.open(player, machine);
	}

}
