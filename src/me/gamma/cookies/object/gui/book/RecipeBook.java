
package me.gamma.cookies.object.gui.book;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.init.RecipeInit;
import me.gamma.cookies.object.block.machine.AbstractCraftingMachine;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.book.RecipeBook.RecipeInformation;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.MachineItem;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.collection.Pair;



public class RecipeBook implements Book<RecipeInformation> {

	public static final String KEY_RESULT_CHOICE = "resultchoice";
	public static final int RESULT_CHOICE_SLOT = 18;
	public static final int RESULT_SLOT = 24;

	@Override
	public String getIdentifier() {
		return "recipe_book";
	}


	@Override
	public String getTitle(int page, RecipeInformation data) {
		return "§6Crafting Recipe";
	}


	@Override
	public int pages(RecipeInformation data) {
		return data.variations;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public ItemStack getTurnLeftIcon() {
		return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§e<---  §6Previous Variation").build();
	}


	@Override
	public ItemStack getTurnRightIcon() {
		return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§6Next Variation  §e--->").build();
	}


	@Override
	public ItemStack getCloseIcon() {
		return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§e<-- §aBack").build();
	}


	@Override
	public int[] getItemSlots() {
		return new int[0];
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, RecipeInformation data) {
		return new ItemStack[0];
	}


	@Override
	public RecipeInformation loadData(Inventory inventory) {
		return new RecipeInformation(inventory.getItem(RESULT_SLOT), ResultChoice.byId(InventoryUtils.getStringFromStack(inventory.getItem(RESULT_CHOICE_SLOT), KEY_RESULT_CHOICE)));
	}


	@Override
	public void saveData(Inventory inventory, RecipeInformation data) {}


	@Override
	public Inventory createGui(BookData<RecipeInformation> data) {
		Inventory gui = Book.super.createGui(data);
		RecipeUtils.initializeRecipeInventory(gui);
		return gui;
	}


	@Override
	public void setupControls(Inventory gui, int page, RecipeInformation data) {
		Book.super.setupControls(gui, page, data);

		Recipe recipe = data.getRecipe(page);
		gui.setItem(RESULT_SLOT, data.getRecipe(page).getResult());
		ItemStack resultchoiceicon = data.resultchoice.createIcon();
		InventoryUtils.storeStringInStack(resultchoiceicon, KEY_RESULT_CHOICE, data.resultchoice.getId());
		gui.setItem(RESULT_CHOICE_SLOT, resultchoiceicon);
		gui.setItem(31, RecipeUtils.getIconForRecipe(recipe));

		if(AbstractCraftingMachine.isCraftingMachine(data.result.getItemMeta()))
			gui.setItem(6, new ItemBuilder(Material.KNOWLEDGE_BOOK).setName("§6Show Machine Recipes").build());
	}


	@Override
	public InventoryTask createInventoryTask(Inventory inventory, BookData<RecipeInformation> data) {
		return RecipeUtils.getInventoryTask(inventory, data.data().resultchoice, data.data().getRecipe(data.page()));
	}


	@Override
	public void onInventoryClick(HumanEntity player, Inventory gui, ItemStack stack, int slot, InventoryClickEvent event, int page) {
		int row = slot / 9;
		int column = slot - row * 9;
		if(1 <= row && row <= 3 && 1 <= column && column <= 3)
			if(!ItemUtils.isEmpty(stack) && !InventoryUtils.isFiller(stack))
				openBook(player, stack, this.loadData(gui).resultchoice);

		if(slot == RESULT_CHOICE_SLOT) {
			this.openGui(player, new BookData<>(page, new RecipeInformation(gui.getItem(RESULT_SLOT), EnumUtils.cycle(this.loadData(gui).resultchoice))), false, true);
		} else if(slot == 6) {
			AbstractCustomItem item = Items.getCustomItemFromStack(gui.getItem(RESULT_SLOT));
			if(item != null && item instanceof MachineItem machine)
				MachineRecipeBook.openBook(player, new Pair<>(null, machine));
		}
	}


	@Override
	public void close(HumanEntity player, Inventory inventory) {
		History.travelBack(player);
	}


	public static void openBook(HumanEntity player, ItemStack stack, ResultChoice resultchoice) {
		RecipeInformation data = new RecipeInformation(stack, resultchoice);
		if(data.variations > 0)
			BookInit.RECIPE_BOOK.open(player, data);
	}

	public static class RecipeInformation {

		private final RecipeInventoryTask.ResultChoice resultchoice;
		private final ItemStack result;
		private final List<Recipe> recipes;
		private final int variations;

		public RecipeInformation(ItemStack result, ResultChoice resultchoice) {
			this.result = result;
			this.resultchoice = resultchoice;
			this.recipes = RecipeInit.getAllRecipesFor(result);
			this.variations = this.recipes.size();
		}


		public Recipe getRecipe(int variation) {
			return this.recipes.get(((variation % this.variations) + this.variations) % this.variations);
		}

	}

}
