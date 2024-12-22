
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.gamma.cookies.init.RecipeInit;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.recipe.CustomRecipe;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.collection.CachingSupplier;



public class Crafter extends AbstractItemProcessingMachine {

	public static final int PROCESS_DURATION = 160;
	public static final int OUTPUT_SLOT = 43;

	private static final Set<ShapedRecipe> shapedRecipes = new HashSet<>();
	private static final Set<ShapelessRecipe> shapelessRecipes = new HashSet<>();
	private static final CachingSupplier<Set<CustomRecipe>> customRecipeSupplier = new CachingSupplier<>(() -> RecipeInit.COOKIE_RECIPES.stream().filter(r -> r instanceof CustomRecipe).map(r -> (CustomRecipe) r).collect(Collectors.toSet()));

	private static final int MAX_PATTERNS = 8;

	public static final ItemStackProperty PROCESSING = Properties.PROCESSING;
	public static final BooleanProperty REUSE = new BooleanProperty("reuse");
	public static final IntegerProperty PATTERN = new IntegerProperty("pattern");
	public static final ItemStackProperty[] RESULTS = new ItemStackProperty[MAX_PATTERNS];
	public static final BooleanProperty[] REUSE_RESULTS = new BooleanProperty[MAX_PATTERNS];
	public static final ItemStackProperty[][] PATTERNS = new ItemStackProperty[MAX_PATTERNS][9];

	public static final ItemStack PATTERN_STACK = InventoryUtils.filler(Material.STRUCTURE_VOID);

	static {
		for(int i = 0; i < RESULTS.length; i++)
			RESULTS[i] = new ItemStackProperty("result" + i);

		for(int i = 0; i < REUSE_RESULTS.length; i++)
			REUSE_RESULTS[i] = new BooleanProperty("reuse" + i);

		for(int j = 0; j < PATTERNS.length; j++) {
			ItemStackProperty[] pattern = PATTERNS[j];
			for(int i = 0; i < pattern.length; i++)
				pattern[i] = new ItemStackProperty(("pattern" + j) + i);
		}

		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		while(iterator.hasNext()) {
			Recipe recipe = iterator.next();
			if(recipe instanceof ShapedRecipe shaped) {
				shapedRecipes.add(shaped);
			} else if(recipe instanceof ShapelessRecipe shapeless) {
				shapelessRecipes.add(shapeless);
			}
		}
	}

	private int patterns;

	public Crafter(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.patterns = config.getInt("patterns", 1);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		super.buildBlockItemProperties(builder).add(PROCESSING).add(PATTERN);
		for(int i = 0; i < this.patterns; i++)
			builder.addAll(PATTERNS[i]);
		return builder.addAll(RESULTS).addAll(REUSE_RESULTS);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(REUSE);
	}


	private void updatePattern(TileState block, Inventory inventory) {
		int selected = PATTERN.fetch(block);
		ItemStackProperty[] pattern = PATTERNS[selected];
		for(int i = 0; i < 9; i++) {
			int r = i / 3;
			int c = i - r * 3;
			ItemStack stack = pattern[i].fetch(block);
			inventory.setItem(9 * r + c + 9, ItemUtils.isEmpty(stack) ? PATTERN_STACK : stack);
		}

		ItemStack result = RESULTS[selected].fetch(block);
		inventory.setItem(37, ItemUtils.isEmpty(result) ? InventoryUtils.filler(Material.BARRIER) : result);
	}


	private void savePattern(TileState block, Inventory inventory) {
		int selected = PATTERN.fetch(block);
		ItemStackProperty[] pattern = PATTERNS[selected];
		for(int i = 0; i < 9; i++) {
			int r = i / 3;
			int c = i - r * 3;
			ItemStack stack = inventory.getItem(9 * r + c + 9);
			pattern[i].store(block, stack = ItemUtils.equals(stack, PATTERN_STACK) ? null : stack);
		}

		block.update();
	}


	private void editPattern(TileState block, Inventory inventory) {
		int selected = PATTERN.fetch(block);
		Recipe recipe = this.findRecipe(inventory);
		ItemStack result = recipe == null ? null : recipe.getResult();
		this.setResult(block, inventory, result, selected, selected);
		inventory.setItem(37, ItemUtils.isEmpty(result) ? InventoryUtils.filler(Material.BARRIER) : result);
		RESULTS[selected].store(block, result);
		block.update();
	}


	private void setResult(TileState block, Inventory inventory, ItemStack result, int pattern, int selected) {
		ItemBuilder builder = (ItemUtils.isEmpty(result) ? new ItemBuilder(Material.PAPER) : new ItemBuilder(result));
		boolean reuse = REUSE_RESULTS[pattern].fetch(block);
		builder.setName("§6Pattern " + (pattern + 1)).setAmount(1).setLore(Arrays.asList("§7Re-use output: " + (reuse ? "§aenabled" : "§cdisabled")));
		if(pattern == selected && !ItemUtils.isEnchanted(result))
			builder.addEnchantment(Enchantment.PROTECTION, 1).setItemFlag(ItemFlag.HIDE_ENCHANTS);
		inventory.setItem(45 + pattern, builder.build());
	}


	private void toggleReuseResult(TileState block, Inventory inventory, int pattern, int selected) {
		REUSE_RESULTS[pattern].toggle(block);
		block.update();
		this.setResult(block, inventory, inventory.getItem(45 + pattern), pattern, selected);
	}


	private void selectPattern(TileState block, Inventory inventory, int pattern) {
		int selected = PATTERN.fetch(block);
		if(pattern == selected)
			return;

		this.savePattern(block, inventory);
		this.setResult(block, inventory, RESULTS[selected].fetch(block), selected, pattern);
		PATTERN.store(block, pattern);
		block.update();
		this.updatePattern(block, inventory);
		this.setResult(block, inventory, RESULTS[pattern].fetch(block), pattern, pattern);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		int selected = PATTERN.fetch(block);
		for(int i = 0; i < this.patterns; i++)
			this.setResult(block, inventory, RESULTS[i].fetch(block), i, selected);
		this.updatePattern(block, inventory);
	}


	@Override
	public void saveInventory(TileState block, Inventory inventory) {
		super.saveInventory(block, inventory);
		this.savePattern(block, inventory);
		block.update();
	}


	@Override
	protected int createNextProcess(TileState block) {
		super.createNextProcess(block);

		Inventory gui = this.getGui(block);

		Map<ItemStack, Integer> resources = new HashMap<>();
		ItemStack[][] inputs = new ItemStack[3][3];
		for(int r = 1; r < 4; r++) {
			for(int c = 6; c < 9; c++) {
				ItemStack stack = gui.getItem(r * 9 + c);
				if(ItemUtils.isEmpty(stack))
					continue;

				stack = stack.clone();
				int amount = stack.getAmount();
				stack.setAmount(1);

				int i = resources.getOrDefault(stack, 0);
				resources.put(stack, i + amount);
				inputs[r - 1][c - 6] = stack;
			}
		}
		int resourceCount = resources.values().stream().mapToInt(i -> i).sum();

		Recipe[] recipes = this.findRecipes(block, gui);
		for(int i = 0; i < recipes.length; i++) {
			Recipe recipe = recipes[i];
			if(recipe == null)
				continue;

			List<RecipeChoice> ingredients = getIngredients(recipe);

			// cancel if less resources than ingredients
			if(ingredients.size() > resourceCount)
				continue;

			ItemStack[] mapping = new ItemStack[ingredients.size()];
			if(!checkResources(ingredients, resources, mapping, 0))
				continue;

			loop: for(int j = 0; j < mapping.length; j++) {
				ItemStack resource = mapping[j];

				for(int r = 1; r < 4; r++) {
					for(int c = 6; c < 9; c++) {
						ItemStack stack = gui.getItem(r * 9 + c);
						if(!ItemUtils.equals(resource, stack))
							continue;

						Material rest = ItemUtils.getCraftingRemainingItem(stack.getType());
						ItemUtils.increaseItem(stack, -1);
						if(rest != null)
							this.addInput(gui, new ItemStack(rest));
						continue loop;
					}
				}
			}

			PROCESSING.store(block, recipe instanceof CustomRecipe custom ? custom.getResult(inputs) : recipe.getResult());
			REUSE.store(block, REUSE_RESULTS[i].fetch(block));
			return PROCESS_DURATION;
		}

		return 0;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		super.finishProcess(block);

		Inventory gui = this.getGui(block);

		ItemStack processing = PROCESSING.fetch(block);

		if(REUSE.fetch(block)) {
			this.addInput(gui, processing);
		} else {
			gui.setItem(OUTPUT_SLOT, ItemUtils.insertItemStack(processing, gui.getItem(OUTPUT_SLOT)));
		}
		this.tryPushItems(block);
		PROCESSING.store(block, processing);
		return ItemUtils.isEmpty(processing);
	}


	private void addInput(Inventory gui, ItemStack stack) {
		loop: for(int r = 1; r < 4; r++) {
			for(int c = 6; c < 9; c++) {
				int i = r * 9 + c;
				gui.setItem(i, ItemUtils.insertItemStack(stack, gui.getItem(i)));
				if(ItemUtils.isEmpty(stack))
					break loop;
			}
		}
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.CRAFTING_TABLE;
	}


	@Override
	public String getTitle() {
		return this.tier.getDescription() + " Crafter";
	}


	@Override
	public String getMachineRegistryName() {
		return "crafter";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.CRAFTER;
	}


	@Override
	protected int getInputModeSlot() {
		return 23;
	}


	@Override
	protected int getOutputModeSlot() {
		return 41;
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 15, 16, 17, 24, 25, 26, 33, 34, 35 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 43 };
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = Bukkit.createInventory(null, 54, this.getTitle());
		ItemStack filler = MachineConstants.BORDER_MATERIAL;
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}
		gui.setItem(41, filler);
		filler = MachineConstants.INPUT_BORDER_MATERIAL;
		gui.setItem(14, filler);
		gui.setItem(23, filler);
		gui.setItem(32, filler);
		filler = MachineConstants.OUTPUT_BORDER_MATERIAL;
		gui.setItem(42, filler);
		gui.setItem(44, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] { 12, 21, 30, 36, 37, 38, 39 })
			gui.setItem(i, filler);
		filler = MachineConstants.FILLER_MATERIAL;
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		if(event.getClick() == ClickType.DOUBLE_CLICK)
			return true;

		int slot = event.getSlot();
		int r = slot / 9;
		int c = slot - r * 9;
		if(0 < r && r <= 3) {
			if(0 <= c && c < 3) {

				ItemStack stack;
				if(event.getClick().isRightClick()) {
					stack = PATTERN_STACK;
				} else {
					stack = event.getCursor().clone();
					if(ItemUtils.isEmpty(stack)) {
						stack = PATTERN_STACK;
					} else {
						stack.setAmount(1);
					}
				}

				gui.setItem(slot, stack);
				this.editPattern(block, gui);
				return true;
			} else if(6 <= c && c < 9) {
				return false;
			}
		} else if(slot == OUTPUT_SLOT) {
			return false;
		}

		if(r == 5 && c < this.patterns) {
			switch (event.getClick()) {
				case LEFT:
					this.selectPattern(block, gui, c);
					break;
				case RIGHT:
					this.toggleReuseResult(block, gui, c, PATTERN.fetch(block));
					break;
				default:
					break;
			}
			return true;
		}

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState block, PlayerInventory gui, InventoryClickEvent event) {
		return event.getClick() == ClickType.DOUBLE_CLICK || event.getClick().isShiftClick();
	}


	private Recipe findRecipe(Inventory gui) {
		ItemStack[][] matrix = new ItemStack[3][3];
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 3; c++) {
				ItemStack stack = gui.getItem(r * 9 + c + 9);
				matrix[r][c] = ItemUtils.equals(stack, PATTERN_STACK) ? null : stack;
			}
		}

		return findRecipe(matrix);
	}


	private Recipe[] findRecipes(TileState block, Inventory gui) {
		Recipe[] recipes = new Recipe[this.patterns];
		int selected = PATTERN.fetch(block);

		for(int i = 0; i < this.patterns; i++) {
			if(i == selected) {
				recipes[i] = this.findRecipe(gui);
				continue;
			}

			ItemStackProperty[] pattern = PATTERNS[i];
			ItemStack[][] matrix = new ItemStack[3][3];
			for(int r = 0; r < 3; r++)
				for(int c = 0; c < 3; c++)
					matrix[r][c] = pattern[r * 3 + c].fetch(block);

			recipes[i] = findRecipe(matrix);
		}

		return recipes;
	}


	private static Recipe findRecipe(ItemStack[][] matrix) {
		for(ShapedRecipe recipe : shapedRecipes)
			if(RecipeUtils.validateRecipe(recipe, matrix))
				return recipe;

		for(ShapelessRecipe recipe : shapelessRecipes)
			if(RecipeUtils.validateRecipe(recipe, matrix))
				return recipe;

		for(CustomRecipe recipe : customRecipeSupplier.get())
			if(RecipeUtils.matchShaped(recipe, matrix))
				return recipe;

		return null;
	}


	private static List<RecipeChoice> getIngredients(Recipe recipe) {
		if(recipe instanceof ShapedRecipe shaped) {
			Map<Character, RecipeChoice> ingredients = shaped.getChoiceMap();
			return Stream.of(shaped.getShape()).flatMapToInt(String::chars).mapToObj(i -> ingredients.get((char) i)).filter(Objects::nonNull).toList();
		} else if(recipe instanceof ShapelessRecipe shapeless) {
			return shapeless.getChoiceList();
		} else if(recipe instanceof CustomRecipe custom) {
			Map<Character, RecipeChoice> ingredients = custom.getIngredientMap();
			return Stream.of(custom.getShape()).flatMapToInt(String::chars).mapToObj(i -> ingredients.get((char) i)).filter(Objects::nonNull).toList();
		} else {
			return new ArrayList<>();
		}
	}


	private static boolean checkResources(List<RecipeChoice> ingredients, Map<ItemStack, Integer> resources, ItemStack[] mapping, int index) {
		if(index >= mapping.length)
			return true;

		RecipeChoice ingredient = ingredients.get(index);

		for(Map.Entry<ItemStack, Integer> entry : resources.entrySet()) {
			ItemStack stack = entry.getKey();
			if(!ingredient.test(stack))
				continue;

			int amount = entry.getValue();
			if(amount <= 0)
				continue;

			Map<ItemStack, Integer> subResources = new HashMap<>(resources);

			if(amount > 1) {
				subResources.put(stack, amount - 1);
			} else {
				subResources.remove(stack);
			}

			mapping[index] = stack;

			if(checkResources(ingredients, subResources, mapping, index + 1))
				return true;

			mapping[index] = null;
		}

		return false;
	}

}
