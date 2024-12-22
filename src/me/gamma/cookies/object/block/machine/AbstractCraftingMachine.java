
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.book.MachineRecipeBook;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.item.MachineItem;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.collection.Pair;



public abstract class AbstractCraftingMachine extends AbstractItemProcessingMachine implements ItemConsumer, ItemSupplier {

	public static final ListProperty<ItemStack, ItemStackProperty> RESULTS = new ListProperty<>("results", ItemStackProperty::new);
	public static final StringProperty LAST_RECIPE = new StringProperty("lastrecipe");

	public AbstractCraftingMachine(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);
		/*
		 * List<MachineRecipe> list = this.getMachineRecipes(null);
		 * 
		 * File file = new File(Cookies.INSTANCE.getDataFolder(), "generated/recipes/" + this.getMachineRegistryName() + "_" + this.tier.name().toLowerCase()
		 * + ".json"); if(!file.exists()) try { file.getParentFile().mkdirs(); file.createNewFile(); } catch(IOException e) { e.printStackTrace(); }
		 * try(FileWriter writer = new FileWriter(file)) { JsonObject object = new JsonObject(); if(this.tier == null) {
		 * System.err.println(this.getMachineRegistryName() + " has no tier but recipes!"); } else { JsonArray recipes = new JsonArray(this.recipes.size());
		 * for(MachineRecipe recipe : list) recipes.add(recipe.saveRecipe()); object.add(this.tier.name().toLowerCase(), recipes); }
		 * writer.write(object.toString()); } catch(IOException e) { System.err.println(this.getMachineRegistryName()); e.printStackTrace(); }
		 * 
		 * try(FileReader reader = new FileReader(Cookies.INSTANCE.getResourceFile("recipes/" + this.getMachineRegistryName() + ".json"))) { JsonElement
		 * element = JsonParser.parseReader(reader); if(!element.isJsonObject()) { System.err.println("Expected JSON object for machine recipes '" +
		 * this.getMachineRegistryName() + "'."); return; }
		 * 
		 * JsonObject obj = element.getAsJsonObject(); element = obj.get("recipes"); if(!element.isJsonArray()) {
		 * System.err.println("Expected JSON array for machine recipes '" + this.getMachineRegistryName() + "'."); return; }
		 * 
		 * JsonArray array = element.getAsJsonArray(); for(int i = 0; i < array.size(); i++) { try { element = array.get(i);
		 * this.recipes.add(MachineRecipe.loadRecipe(element.getAsJsonObject(), this)); } catch(IllegalStateException e) {
		 * System.err.println("Unable to read recipe " + i + "."); e.printStackTrace(); } } } catch(IOException e) {
		 * System.err.println("Unable to read recipes for machine '" + this.getMachineRegistryName() + "'."); e.printStackTrace(); }
		 * catch(IllegalArgumentException e) { e.printStackTrace(); }
		 */
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		inventory.setItem(this.getRecipesSlot(), new ItemBuilder(Material.KNOWLEDGE_BOOK).setName("§bMachine Recipes").build());
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(RESULTS).add(LAST_RECIPE);
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		for(ItemStack stack : RESULTS.fetch(block))
			drops.add(stack);

		return drops;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		int slot = event.getSlot();
		if(slot == this.getRecipesSlot()) {
			History.add(player, new StaticInventoryTask(gui));
			MachineRecipeBook.openBook(player, new Pair<>(block, (MachineItem) Items.getCustomItemFromCustomBlock(this)));
		}

		return true;
	}


	@Override
	public void getAllowedUpgrades(ArrayList<MachineUpgrade> upgrades) {
		super.getAllowedUpgrades(upgrades);
		upgrades.add(MachineUpgrade.LUCK);
	}


	private int checkRecipe(TileState block, ItemStack[] inputs, MachineRecipe recipe) {
		Map<RecipeChoice, Integer> uses = recipe.matches(inputs);
		if(uses == null)
			return 0;

		RESULTS.store(block, recipe.getOutputs(this.getUpgradeValue(block, MachineUpgrade.LUCK)));

		this.consumeInputs(block, Stream.of(recipe.getIngredients()).map(ingredient -> RecipeInventoryTask.getRawItemFromItemChoice(ingredient, ResultChoice.ORDERED, uses.get(ingredient))).collect(Collectors.toList()));

		return recipe.getDuration();
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);

		ItemStack[] inputs = this.getUseableItems(block);

		List<MachineRecipe> recipes = this.getMachineRecipes(block);

		String lastRecipe = LAST_RECIPE.fetch(block);
		if(lastRecipe != null && !lastRecipe.isBlank()) {
			for(MachineRecipe recipe : recipes) {
				if(lastRecipe.equals(recipe.getIdentifier())) {
					int duration = this.checkRecipe(block, inputs, recipe);
					if(duration > 0)
						return duration;

					break;
				}
			}
		}

		for(MachineRecipe recipe : recipes) {
			int duration = this.checkRecipe(block, inputs, recipe);
			if(duration > 0) {
				LAST_RECIPE.store(block, recipe.getIdentifier());
				return duration;
			}
		}

		return 0;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		List<ItemStack> results = RESULTS.fetch(block);
		while(!this.storeOutputs(block, results)) {
			if(!this.tryPushItems(block)) {
				RESULTS.store(block, results);
				return false;
			}
		}

		RESULTS.store(block, results);
		while(this.tryPushItems(block));

		return true;
	}


	/**
	 * Returns the list of machine recipes for this machine.
	 * 
	 * @param block the block
	 * @return the list of recipes
	 */
	public abstract List<MachineRecipe> getMachineRecipes(TileState block);


	/**
	 * Returns the slot to store the recipe book button.
	 * 
	 * @return the slot
	 */
	protected int getRecipesSlot() {
		return MachineConstants.RECIPES_SLOT;
	}


	/**
	 * Checks if the given {@link PersistentDataHolder} is a crafting machine.
	 * 
	 * @param holder the holder
	 * @return if the holder is a crafting machine
	 */
	public static boolean isCraftingMachine(PersistentDataHolder holder) {
		return Blocks.getCustomBlockFromHolder(holder) instanceof AbstractCraftingMachine;
	}

}
