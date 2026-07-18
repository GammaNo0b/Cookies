
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.machine.CraftingFactoryBlock;
import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.gui.ItemInventoryHolder;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class CraftingFactory extends AbstractProcessingMachine<CraftingFactory, CraftingFactoryBlock> implements ItemConsumer, ItemSupplier, ItemInventoryHolder {

	private static final String KEY_PROCESSING_STEPS = "steps";
	private static final String KEY_PROCESSING_STEP = "step";

	public static final int PROCESS_DURATION = 20;

	public static final IntegerProperty PROCESSING_STEPS = new IntegerProperty("processing_steps");

	private static final int[] craftingSlots = { 29, 30, 32, 33 };

	private final int craftingSteps;
	private byte itemInputAccessFlags = 0x3f;
	private byte itemOutputAccessFlags = 0x3f;

	private final ProcessingStep[] steps;
	private int processingStep;
	private ItemStack processing = null;

	public CraftingFactory(CraftingFactoryBlock customBlock, Block block) {
		super(customBlock, block);

		this.craftingSteps = customBlock.getMaxSteps();
		this.steps = new ProcessingStep[this.craftingSteps];
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		ItemConsumer.super.load(chunk, data);
		ItemSupplier.super.load(chunk, data);

		Inventory gui = this.getInventory();
		for(int i = 0; i < this.craftingSteps; i++)
			gui.setItem(20 + i, PersistentDataUtils.getItemStack(data, "processing" + i));

		ProcessingStep.loadSteps(this.steps, data.getInteger(KEY_PROCESSING_STEPS, 0));
		this.processingStep = data.getInteger(KEY_PROCESSING_STEP, 0);
		this.processing = PersistentDataUtils.getItemStack(data, "processing");

		for(int i = 0; i < this.craftingSteps; i++)
			gui.setItem(craftingSlots[i], this.steps[i].icon);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		ItemConsumer.super.save(chunk, data);
		ItemSupplier.super.save(chunk, data);

		Inventory gui = this.getInventory();
		for(int i = 0; i < this.craftingSteps; i++)
			PersistentDataUtils.setItemStack(data, "processing" + i, gui.getItem(20 + i));

		data.setInteger(KEY_PROCESSING_STEPS, ProcessingStep.saveSteps(this.steps));
		data.setInteger(KEY_PROCESSING_STEP, this.processingStep);
		PersistentDataUtils.setItemStack(data, "processing", this.processing);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);

		ItemStack filler = MachineConstants.FILLER_MATERIAL;
		for(int i = this.craftingSteps; i < 4; i++) {
			inventory.setItem(21 + i, filler);
			inventory.setItem(craftingSlots[i], filler);
		}
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 29 || slot == 30 || slot == 32 || slot == 33)
			if(this.toggleProcessingSlot(block, gui, slot))
				return true;

		if(19 < slot && slot < 21 + this.craftingSteps)
			return false;

		return super.onMainInventoryInteract(player, gui, event);
	}


	private boolean toggleProcessingSlot(Block block, Inventory gui, int slot) {
		int index = switch (slot) {
			case 29 -> 0;
			case 30 -> 1;
			case 32 -> 2;
			case 33 -> 3;
			default -> -1;
		};

		if(slot == -1 || index > this.craftingSteps)
			return true;

		this.steps[index] = EnumUtils.cycle(this.steps[index]);
		gui.setItem(slot, steps[index].icon);

		return false;
	}


	@Override
	public void tick() {
		super.tick();
		this.tryPushItems();
	}


	@Override
	protected int createNextProcess() {
		for(int i = this.steps.length; i > 0; i--) {
			ItemProvider input = ItemProvider.fromInventory(this.getInventory(), 19 + i);
			ProcessingStep step = steps[i - 1];
			ItemStack result = step.craft(input);
			if(result == null)
				continue;

			this.processingStep = i;
			this.processing = result;

			return PROCESS_DURATION;
		}
		return 0;
	}


	@Override
	protected boolean finishProcess() {
		ItemProvider output = ItemProvider.fromInventory(this.getInventory(), 20 + this.processingStep);
		if(!output.isEmpty()) {
			if(!output.match(this.processing))
				return false;

			int rest = output.set(this.processing, this.processing.getAmount());
			if(rest == 0) {
				this.processing = null;
				return true;
			}

			this.processing.setAmount(rest);
			return false;
		}

		output.setType(this.processing);
		output.set(this.processing, this.processing.getAmount());

		this.tryPushItems();

		return true;
	}


	@Override
	public List<Provider<ItemStack>> getItemInputs() {
		return List.of(ItemProvider.fromInventory(this.getInventory(), 20));
	}


	@Override
	public List<Provider<ItemStack>> getItemOutputs() {
		return List.of(ItemProvider.fromInventory(this.getInventory(), 20 + this.craftingSteps));
	}


	@Override
	public int[] getInputSlots() {
		return new int[] { 20 };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 20 + this.craftingSteps };
	}


	@Override
	public Inventory getInventory() {
		return super.getInventory();
	}


	@Override
	public byte getItemInputAccessFlags() {
		return this.itemInputAccessFlags;
	}


	@Override
	public void setItemInputAccessFlags(byte flags) {
		this.itemInputAccessFlags = flags;
	}


	@Override
	public byte getItemOutputAccessFlags() {
		return this.itemOutputAccessFlags;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {
		this.itemOutputAccessFlags = flags;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.STICK;
	}


	@Override
	public CraftingFactory castTileEntity() {
		return this;
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


		public static void loadSteps(ProcessingStep[] steps, int value) {
			for(int i = 0; i < steps.length; i++)
				steps[i] = loadStep(i, value);
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
