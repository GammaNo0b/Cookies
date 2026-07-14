
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

import me.gamma.cookies.object.block.machine.AbstractCraftingMachineBlock;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.block.machine.MachineUpgrade;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.book.MachineRecipeBook;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractCraftingMachine<T extends AbstractCraftingMachine<T, B>, B extends AbstractCraftingMachineBlock<B, T>> extends AbstractItemProcessingMachine<T, B> {

	private static final String KEY_RESULTS = "results";
	private static final String KEY_LAST_RECIPE = "lastrecipe";

	protected final Map<String, MachineRecipe> recipes = new HashMap<>();

	private MachineRecipe lastRecipe = null;
	private final List<ItemStack> results = new ArrayList<>();

	public AbstractCraftingMachine(B customBlock, Block block) {
		super(customBlock, block);

		for(MachineRecipe recipe : customBlock.getMachineRecipes(block))
			this.recipes.put(recipe.getIdentifier(), recipe);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		List<PersistentDataObject> robjs = data.getObjectList(KEY_RESULTS);
		if(robjs != null) {
			for(PersistentDataObject robj : robjs) {
				ItemStack stack = PersistentDataUtils.loadItemStack(robj);
				if(!ItemUtils.isEmpty(stack))
					this.results.add(stack);
			}
		}

		String lastRecipeStr = data.getString(KEY_LAST_RECIPE);
		this.lastRecipe = this.recipes.get(lastRecipeStr);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		List<PersistentDataObject> robjs = new ArrayList<>();
		if(!this.results.isEmpty()) {
			for(ItemStack stack : this.results) {
				if(!ItemUtils.isEmpty(stack)) {
					PersistentDataObject robj = new PersistentDataObject(data.getAdapterContext());
					PersistentDataUtils.saveItemStack(robj, stack);
					robjs.add(robj);
				}
			}
		}
		data.setObjectList(KEY_RESULTS, robjs);

		if(this.lastRecipe != null)
			data.setString(KEY_LAST_RECIPE, this.lastRecipe.getIdentifier());

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		inventory.setItem(this.getRecipesSlot(), new ItemBuilder(Material.KNOWLEDGE_BOOK).setName("§bMachine Recipes").build());
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, gui, event))
			return false;

		int slot = event.getSlot();
		if(slot == this.getRecipesSlot()) {
			History.add(player, new StaticInventoryTask(gui));
			MachineRecipeBook.openBook(player, this.customBlock);
		}

		return true;
	}


	private int checkRecipe(Map<ItemStack, Integer> inputs, MachineRecipe recipe) {
		Map<RecipeChoice, Integer> uses = recipe.matches(inputs);
		if(uses == null)
			return 0;

		this.results.clear();
		this.results.addAll(recipe.getOutputs(this.getUpgradeValue(MachineUpgrade.LUCK)));

		this.consumeInputs(Stream.of(recipe.getIngredients()).map(ingredient -> RecipeInventoryTask.getRawItemFromItemChoice(ingredient, ResultChoice.ORDERED, uses.get(ingredient))).collect(Collectors.toList()));

		return recipe.getDuration();
	}


	@Override
	protected int createNextProcess() {
		super.createNextProcess();

		Map<ItemStack, Integer> inputs = this.getUseableItems();

		if(this.lastRecipe != null) {
			int duration = this.checkRecipe(inputs, this.lastRecipe);
			if(duration > 0)
				return duration;
		}

		for(MachineRecipe recipe : this.recipes.values()) {
			int duration = this.checkRecipe(inputs, recipe);
			if(duration > 0) {
				this.lastRecipe = recipe;
				return duration;
			}
		}

		return 0;
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		while(!this.storeOutputs(this.results))
			if(!this.tryPushItems())
				return false;

		while(this.tryPushItems());

		this.results.removeIf(ItemUtils::isEmpty);

		return true;
	}


	/**
	 * Returns the slot to store the recipe book button.
	 * 
	 * @return the slot
	 */
	protected int getRecipesSlot() {
		return MachineConstants.RECIPES_SLOT;
	}


	@Override
	public void getAllowedUpgrades(List<MachineUpgrade> upgrades) {
		super.getAllowedUpgrades(upgrades);
		upgrades.add(MachineUpgrade.LUCK);
	}


	public List<MachineRecipe> getMachineRecipes() {
		return List.copyOf(this.recipes.values());
	}

}
