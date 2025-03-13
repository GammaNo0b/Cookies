
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.RecipeUtils;



public class CraftingFactory extends AbstractProcessingMachine implements ItemConsumer, ItemSupplier {

	public static final int PROCESS_DURATION = 20;

	public static final IntegerProperty PROCESSING_STEPS = new IntegerProperty("processing_steps");
	public static final IntegerProperty PROCESSING_STEP = new IntegerProperty("processing_step");
	public static final ItemStackProperty PROCESSING = Properties.PROCESSING;
	public static final ItemStackProperty[] PROCESSING_STACKS = ArrayUtils.generate(5, i -> new ItemStackProperty("item_" + i), ItemStackProperty[]::new);

	private int maxSteps;

	public CraftingFactory(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.maxSteps = config.getInt("maxSteps", 1);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_OUTPUT_ACCESS_FLAGS, (byte) 0x3F).add(PROCESSING_STEPS).add(PROCESSING_STEP).add(PROCESSING).addAll(PROCESSING_STACKS);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);

		for(int i = 0; i <= this.maxSteps; i++)
			inventory.setItem(20 + i, PROCESSING_STACKS[i].fetch(block));
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		super.saveInventory(block, inventory);

		for(int i = 0; i <= this.maxSteps; i++)
			PROCESSING_STACKS[i].store(block, inventory.getItem(20 + i));

		block.update();
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.tryPushItems(block);
	}


	@Override
	protected int createNextProcess(TileState block) {
		ProcessingStep[] steps = ProcessingStep.loadSteps(this.maxSteps, PROCESSING_STEPS.fetch(block));
		ItemProvider[] providers = ArrayUtils.generate(this.maxSteps + 1, i -> ItemProvider.fromInventory(this.getGui(block), 20 + i), ItemProvider[]::new);
		for(int i = steps.length; i > 0; i--) {
			ItemProvider input = providers[i - 1];
			ProcessingStep step = steps[i - 1];
			ItemStack result = step.craft(input);
			if(result == null)
				continue;

			PROCESSING_STEP.store(block, i);
			PROCESSING.store(block, result);
			return PROCESS_DURATION;
		}
		return 0;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		int step = PROCESSING_STEP.fetch(block);
		ItemProvider output = ItemProvider.fromInventory(this.getGui(block), 20 + step);
		ItemStack result = PROCESSING.fetch(block);

		if(!output.isEmpty()) {
			if(!output.match(result))
				return false;

			int rest = output.set(result, result.getAmount());
			if(rest == 0)
				return true;

			result.setAmount(rest);
			PROCESSING.store(block, result);
			return false;
		}

		output.setType(result);
		output.set(result, result.getAmount());

		this.tryPushItems(block);

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.STICK;
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Crafting Factory";
	}


	@Override
	public String getMachineRegistryName() {
		return "crafting_factory";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRAFTING_FACTORY;
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs(PersistentDataHolder holder) {
		return holder instanceof TileState block ? List.of(ItemProvider.fromInventory(this.getGui(block), 20)) : List.of();
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs(PersistentDataHolder holder) {
		return holder instanceof TileState block ? List.of(ItemProvider.fromInventory(this.getGui(block), 20 + this.maxSteps)) : List.of();
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = Bukkit.createInventory(null, 54, this.getTitle());
		ItemStack filler = MachineConstants.BORDER_MATERIAL;
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}
		filler = MachineConstants.INPUT_BORDER_MATERIAL;
		for(int i = 1; i <= 4; i++)
			gui.setItem(i * 9 + 1, filler);
		gui.setItem(11, filler);
		gui.setItem(38, filler);
		filler = MachineConstants.OUTPUT_BORDER_MATERIAL;
		for(int i = 1; i <= 4; i++)
			gui.setItem(i * 9 + 7, filler);
		gui.setItem(15, filler);
		gui.setItem(42, filler);
		filler = MachineConstants.FILLER_MATERIAL;
		for(int i = 1; i <= 4; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		gui.setItem(12, filler);
		gui.setItem(14, filler);
		gui.setItem(39, filler);
		gui.setItem(41, filler);

		int[] slots = { 29, 30, 32, 33 };
		ProcessingStep[] steps = ProcessingStep.loadSteps(this.maxSteps, PROCESSING_STEPS.fetch(block));
		int i = 0;
		for(; i < this.maxSteps; i++) {
			gui.setItem(slots[i], steps[i].icon);
		}
		for(; i < 4; i++) {
			gui.setItem(21 + i, filler);
			gui.setItem(slots[i], filler);
		}

		return gui;
	}


	@Override
	protected int getProgressSlot() {
		return 31;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 40;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 49;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 29 || slot == 30 || slot == 32 || slot == 33)
			if(this.toggleProcessingSlot(block, gui, slot))
				return true;

		if(19 < slot && slot < 21 + this.tier.getTier())
			return false;

		return super.onMainInventoryInteract(player, block, gui, event);
	}


	private boolean toggleProcessingSlot(TileState block, Inventory gui, int slot) {
		int index = switch (slot) {
			case 29 -> 0;
			case 30 -> 1;
			case 32 -> 2;
			case 33 -> 3;
			default -> -1;
		};

		if(slot == -1 || index > this.tier.ordinal())
			return true;

		ProcessingStep[] steps = ProcessingStep.loadSteps(this.maxSteps, PROCESSING_STEPS.fetch(block));
		steps[index] = EnumUtils.cycle(steps[index]);
		gui.setItem(slot, steps[index].icon);
		PROCESSING_STEPS.store(block, ProcessingStep.saveSteps(steps));
		block.update();

		return false;
	}

	public static enum ProcessingStep {

		NONE(Material.BARRIER, "§cNone"),
		CRAFT_1x1(Material.STONE_BUTTON, "§6Craft 1x1"),
		CRAFT_1x2(Material.STONE_PRESSURE_PLATE, "§6Craft 1x2"),
		CRAFT_1x3(Material.STONE_BRICK_SLAB, "§6Craft 1x3"),
		CRAFT_2x1(Material.CHISELED_STONE_BRICKS, "§6Craft 2x1"),
		CRAFT_2x2(Material.STONE_BRICKS, "§6Craft 2x2"),
		CRAFT_2x3(Material.STONE_BRICK_WALL, "§6Craft 2x3"),
		CRAFT_3x3(Material.CRAFTING_TABLE, "§6Craft 3x3"),
		SMELTING(Material.FURNACE, "§8Smelting");

		static {
			Iterator<Recipe> iterator = Bukkit.recipeIterator();
			while(iterator.hasNext()) {
				Recipe recipe = iterator.next();
				if(recipe instanceof ShapelessRecipe shapeless) {
					List<RecipeChoice> choices = shapeless.getChoiceList();
					if(choices.size() == 1)
						CRAFT_1x1.registerRecipe(new Processor(choices.get(0), 1, shapeless.getResult()));
				} else if(recipe instanceof ShapedRecipe shaped) {
					registerRecipe(shaped, 1, 2, CRAFT_1x2);
					registerRecipe(shaped, 1, 3, CRAFT_1x3);
					registerRecipe(shaped, 2, 1, CRAFT_2x1);
					registerRecipe(shaped, 2, 2, CRAFT_2x2);
					registerRecipe(shaped, 2, 3, CRAFT_2x3);
					registerRecipe(shaped, 3, 3, CRAFT_3x3);
				} else if(recipe instanceof FurnaceRecipe furnace) {
					SMELTING.registerRecipe(new Processor(furnace.getInputChoice(), 1, furnace.getResult()));
				}
			}
		}

		private static void registerRecipe(ShapedRecipe recipe, int rows, int columns, ProcessingStep step) {
			String[] shape = recipe.getShape();
			if(shape.length != rows)
				return;

			String str = shape[0];
			if(str.length() != columns)
				return;

			Map<Character, RecipeChoice> map = recipe.getChoiceMap();
			RecipeChoice choice = map.get(str.charAt(0));
			if(choice == null)
				return;

			loop: for(ItemStack stack : RecipeUtils.getItemsFromChoice(choice)) {
				for(int i = 0; i < rows; i++) {
					String line = shape[i];
					for(int j = 0; j < columns; j++) {
						RecipeChoice c = map.get(line.charAt(j));
						if(c == null || !c.test(stack))
							continue loop;
					}
				}
				step.registerRecipe(new Processor(new RecipeChoice.ExactChoice(stack), rows * columns, recipe.getResult()));
			}
		}

		private static final int BIT_MASK = 0xF;
		private static final int BIT_SIZE = 4;

		private final ItemStack icon;
		private final ArrayList<Processor> recipes = new ArrayList<>();

		private ProcessingStep(Material icon, String name) {
			this.icon = new ItemBuilder(icon).setName(name).build();
		}


		public void registerRecipe(Processor recipe) {
			this.recipes.add(recipe);
		}


		public ItemStack craft(ItemProvider input) {
			if(this == NONE)
				return input.get();

			for(Processor recipe : this.recipes) {
				if(!input.match(recipe.ingredient))
					continue;

				if(!input.check(recipe.amount))
					continue;

				input.remove(recipe.amount);
				return recipe.getResult().clone();
			}
			return null;
		}


		public static ProcessingStep loadStep(int index, int value) {
			return values()[(value >> index * BIT_SIZE) & BIT_MASK];
		}


		public static ProcessingStep[] loadSteps(int max, int value) {
			ProcessingStep[] steps = new ProcessingStep[max];
			for(int i = 0; i < max; i++)
				steps[i] = loadStep(i, value);
			return steps;
		}


		public static int saveSteps(ProcessingStep[] steps) {
			int value = 0;
			for(int i = 0; i < steps.length; i++)
				value |= (steps[i].ordinal() << i * BIT_SIZE);
			return value;
		}

	}

	public static class Processor implements Recipe {

		private final RecipeChoice ingredient;
		private final int amount;
		private final ItemStack result;

		public Processor(RecipeChoice ingredient, int amount, ItemStack result) {
			this.ingredient = ingredient;
			this.amount = amount;
			this.result = result;
		}


		@Override
		public ItemStack getResult() {
			return this.result;
		}

	}

}
