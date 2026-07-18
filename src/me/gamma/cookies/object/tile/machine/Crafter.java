
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.block.machine.CrafterBlock;
import me.gamma.cookies.object.recipe.CustomRecipe;
import me.gamma.cookies.util.EnumUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.CachingSupplier;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Crafter extends AbstractItemProcessingMachine<Crafter, CrafterBlock> {

	private static final String KEY_PATTERNS = "patterns";
	private static final String KEY_SELECTED = "selected";
	private static final String KEY_REUSE = "reuse";
	private static final String KEY_STRATEGY = "strategy";
	private static final String KEY_PROCESSING = "processing";

	public static final int PROCESS_DURATION = 160;
	public static final int OUTPUT_SLOT = 43;
	public static final int CRAFTING_STRATEGY_SLOT = 3;

	public static final ItemStack PATTERN_STACK = InventoryUtils.filler(Material.STRUCTURE_VOID);

	private static final Set<ShapedRecipe> shapedRecipes = new HashSet<>();
	private static final Set<ShapelessRecipe> shapelessRecipes = new HashSet<>();
	private static final CachingSupplier<Set<CustomRecipe>> customRecipeSupplier = new CachingSupplier<>(() -> RecipeInit.COOKIE_RECIPES.stream().filter(r -> r instanceof CustomRecipe).map(r -> (CustomRecipe) r).collect(Collectors.toSet()));

	static {
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
	private int selectedPattern = 0;
	private final Pattern[] patterns;
	private boolean reuse = false;
	private CraftingStrategy strategy = CraftingStrategy.IN_ORDER;
	private ItemStack processing = null;

	public Crafter(CrafterBlock customBlock, Block block) {
		super(customBlock, block);

		this.patterns = new Pattern[customBlock.getPatterns()];
		for(int i = 0; i < this.patterns.length; i++)
			this.patterns[i] = new Pattern(i);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		List<PersistentDataObject> patterns = data.getObjectList(KEY_PATTERNS, new ArrayList<>());
		int i = 0;
		for(; i < patterns.size(); i++)
			this.patterns[i].load(null, patterns.get(i));
		for(; i < this.patterns.length; i++)
			this.patterns[i].reset();

		this.selectedPattern = data.getInteger(KEY_SELECTED, 0);
		this.reuse = data.getBoolean(KEY_REUSE, false);
		this.strategy = PersistentDataUtils.getEnum(data, KEY_STRATEGY, CraftingStrategy.class);
		if(this.strategy == null)
			this.strategy = CraftingStrategy.IN_ORDER;
		this.processing = PersistentDataUtils.getItemStack(data, KEY_PROCESSING);

		Inventory gui = this.getInventory();
		for(Pattern pattern : this.patterns)
			pattern.updateIcon(gui);

		Pattern selected = this.patterns[this.selectedPattern];
		selected.findRecipe();
		selected.update(gui);

		gui.setItem(CRAFTING_STRATEGY_SLOT, this.strategy.createIcon());

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		List<PersistentDataObject> patterns = new ArrayList<>(this.patterns.length);
		for(int i = 0; i < this.patterns.length; i++) {
			PersistentDataObject object = new PersistentDataObject(data.getAdapterContext());
			this.patterns[i].save(null, object);
			patterns.add(object);
		}
		data.setObjectList(KEY_PATTERNS, patterns);

		data.setInteger(KEY_SELECTED, this.selectedPattern);
		data.setBoolean(KEY_REUSE, this.reuse);
		PersistentDataUtils.setEnum(data, KEY_STRATEGY, this.strategy);
		PersistentDataUtils.setItemStack(data, KEY_PROCESSING, this.processing);

		return true;
	}


	@Override
	public int[] getInputSlots() {
		return new int[] { 15, 16, 17, 24, 25, 26, 33, 34, 35 };
	}


	@Override
	public int[] getOutputSlots() {
		return new int[] { 43 };
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, gui, event))
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
				this.patterns[this.selectedPattern].edit(gui);
				return true;
			} else if(6 <= c && c < 9) {
				return false;
			}
		} else if(slot == OUTPUT_SLOT) {
			return false;
		}

		if(slot == CRAFTING_STRATEGY_SLOT) {
			this.strategy = EnumUtils.cycle(this.strategy, event.getClick().isRightClick() ? -1 : 1);
			gui.setItem(CRAFTING_STRATEGY_SLOT, this.strategy.createIcon());
		}

		if(r == 5 && c < this.patterns.length) {
			switch (event.getClick()) {
				case LEFT:
					this.selectPattern(gui, c);
					break;
				case RIGHT:
					this.toggleReuseResult(gui, c);
					break;
				default:
					break;
			}
			return true;
		}

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		return event.getClick() == ClickType.DOUBLE_CLICK || event.getClick().isShiftClick();
	}


	@Override
	protected int createNextProcess() {
		Inventory gui = this.getInventory();

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

		Function<Recipe, Boolean> crafter = recipe -> {
			if(recipe == null)
				return false;

			List<RecipeChoice> ingredients = getIngredients(recipe);

			// cancel if less resources than ingredients
			if(ingredients.size() > resourceCount)
				return false;

			ItemStack[] mapping = new ItemStack[ingredients.size()];
			if(!checkResources(ingredients, resources, mapping, 0))
				return false;

			loop: for(int j = 0; j < mapping.length; j++) {
				ItemStack resource = mapping[j];

				for(int r = 1; r < 4; r++) {
					for(int c = 6; c < 9; c++) {
						ItemStack stack = gui.getItem(r * 9 + c);
						if(!ItemUtils.equals(resource, stack))
							continue;

						Material rest = stack.getType().getCraftingRemainingItem();
						ItemUtils.increaseItem(stack, -1);
						if(rest != null)
							this.addInput(gui, new ItemStack(rest));
						continue loop;
					}
				}
			}

			return true;
		};

		Pattern pattern = this.strategy.startNextCrafting(this.patterns, crafter);
		if(pattern == null)
			return 0;

		this.processing = pattern.recipe.getResult();
		this.reuse = pattern.reuse;

		return PROCESS_DURATION;
	}


	@Override
	protected boolean finishProcess() {
		super.finishProcess();

		Inventory gui = this.getInventory();

		if(this.reuse) {
			this.addInput(gui, this.processing);
		} else {
			this.processing = this.storeOutput(this.processing);
		}
		this.tryPushItems();

		return ItemUtils.isEmpty(this.processing);
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


	private void toggleReuseResult(Inventory inventory, int pattern) {
		Pattern p = this.patterns[pattern];
		p.reuse = !p.reuse;
		p.updateIcon(inventory);
	}


	private void selectPattern(Inventory inventory, int pattern) {
		if(pattern == this.selectedPattern)
			return;

		Pattern selected = this.patterns[this.selectedPattern];
		this.selectedPattern = pattern;
		selected.updateIcon(inventory);
		this.patterns[this.selectedPattern].update(inventory);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.CRAFTING_TABLE;
	}


	@Override
	public Crafter castTileEntity() {
		return this;
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

	private class Pattern implements DataStorage {

		private final int index;
		private final ItemStack[] pattern = new ItemStack[9];
		private Recipe recipe = null;
		private boolean reuse = false;

		public Pattern(int index) {
			this.index = index;
		}


		/**
		 * Resets this pattern.
		 */
		private void reset() {
			Arrays.fill(this.pattern, null);
			this.recipe = null;
			this.reuse = false;
		}


		/**
		 * Tries to find and update a recipe for the current pattern.
		 */
		private void findRecipe() {
			ItemStack[][] matrix = new ItemStack[3][3];
			for(int r = 0; r < 3; r++)
				for(int c = 0; c < 3; c++)
					matrix[r][c] = this.pattern[3 * r + c];

			this.recipe = Crafter.findRecipe(matrix);
		}


		/**
		 * Updates the icon of this pattern in the given inventory.
		 * 
		 * @param inventory the inventory
		 */
		private void updateIcon(Inventory inventory) {
			ItemBuilder builder = (this.recipe == null ? new ItemBuilder(Material.PAPER) : new ItemBuilder(this.recipe.getResult()));
			builder.setName("§6Pattern " + (this.index + 1)).setAmount(1).setLore(Arrays.asList("§7Re-use output: " + (this.reuse ? "§aenabled" : "§cdisabled")));
			if(this.index == Crafter.this.selectedPattern && !ItemUtils.isEnchanted(builder.build()))
				builder.addEnchantment(Enchantment.PROTECTION, 1).setItemFlag(ItemFlag.HIDE_ENCHANTS);
			inventory.setItem(45 + this.index, builder.build());
		}


		/**
		 * Displays this pattern in the given inventory.
		 * 
		 * @param inventory the inventory
		 */
		private void update(Inventory inventory) {
			for(int i = 0; i < 9; i++) {
				int r = i / 3;
				int c = i - r * 3;
				ItemStack stack = this.pattern[i];
				inventory.setItem(9 * r + c + 9, ItemUtils.isEmpty(stack) ? PATTERN_STACK : stack);
			}

			inventory.setItem(37, this.recipe == null ? InventoryUtils.filler(Material.BARRIER) : this.recipe.getResult());
			this.updateIcon(inventory);
		}


		/**
		 * Extracts the pattern from the given inventory.
		 * 
		 * @param inventory the inventory
		 */
		private void extract(Inventory inventory) {
			for(int i = 0; i < 9; i++) {
				int r = i / 3;
				int c = i - r * 3;
				ItemStack stack = inventory.getItem(9 * r + c + 9);
				this.pattern[i] = ItemUtils.equals(stack, PATTERN_STACK) ? null : stack;
			}
		}


		/**
		 * Get's called when the recipe pattern in the inventory is edited.
		 * 
		 * @param inventory the inventory
		 */
		private void edit(Inventory inventory) {
			this.extract(inventory);
			this.findRecipe();
			this.update(inventory);
		}


		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			for(int i = 0; i < 9; i++)
				this.pattern[i] = PersistentDataUtils.getItemStack(data, "pattern" + i);

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			for(int i = 0; i < 9; i++)
				PersistentDataUtils.setItemStack(data, "pattern" + i, this.pattern[i]);

			return false;
		}

	}

	/**
	 * Used to select the next pattern to craft.
	 */
	private enum CraftingStrategy {

		IN_ORDER("Selects the first pattern possible.") {

			@Override
			Pattern startNextCrafting(Pattern[] patterns, Function<Recipe, Boolean> crafter) {
				for(Pattern pattern : patterns)
					if(crafter.apply(pattern.recipe))
						return pattern;

				return null;
			}

		},
		ROUND_ROBIN("Selects one pattern after the other.") {

			int last = 0;

			@Override
			Pattern startNextCrafting(Pattern[] patterns, Function<Recipe, Boolean> crafter) {
				int i = last + 1;
				while(i != last) {
					if(i >= patterns.length)
						i = 0;

					Pattern pattern = patterns[i];
					if(crafter.apply(pattern.recipe)) {
						this.last = i;
						return pattern;
					}

					i++;
				}

				return null;
			}

		},
		LAST_CRAFTED("Selects the last pattern used and goes in order otherwise.") {

			int last = 0;

			@Override
			Pattern startNextCrafting(Pattern[] patterns, Function<Recipe, Boolean> crafter) {
				Pattern pattern = patterns[last];
				if(crafter.apply(pattern.recipe))
					return pattern;

				for(int i = 0; i < patterns.length; i++) {
					if(i != last) {
						pattern = patterns[i];
						if(crafter.apply(pattern.recipe))
							return pattern;
					}
				}

				return null;
			}

		};

		private final String description;

		private CraftingStrategy(String description) {
			this.description = description;
		}


		ItemStack createIcon() {
			return new ItemBuilder(Material.CRAFTING_TABLE).setName(Utils.toCapitalWords(this)).addLore(this.description).build();
		}


		/**
		 * Specifies the order in which the different patterns are to be tried to craft.
		 * 
		 * @param patterns the available patterns
		 * @param crafter  the crafter
		 * @return the pattern beeing crafted
		 */
		abstract Pattern startNextCrafting(Pattern[] patterns, Function<Recipe, Boolean> crafter);

	}

}
