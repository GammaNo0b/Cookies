
package me.gamma.cookies.managers;


import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.gamma.cookies.objects.block.machine.Machine;
import me.gamma.cookies.objects.block.machine.MachineUpgrade;
import me.gamma.cookies.objects.property.VectorProperty;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class InventoryManager {

	private static final List<Material> materials_by_default = Arrays.asList(Material.values()).stream().filter(Material::isItem).filter(material -> Bukkit.getRecipesFor(new ItemStack(material)).size() > 0).collect(Collectors.toList());
	private static final List<Material> materials_by_name = materials_by_default.stream().sorted((m1, m2) -> m1.name().compareTo(m2.name())).collect(Collectors.toList());

	private static final Map<UUID, Deque<Inventory>> inventoryHistory = new HashMap<>();

	private static final String VANILLA_RECIPE_LIST_TITLE = "§aVanilla §dRecipes";
	private static final String VANILLA_RECIPE_TITLE = "§aVanilla §dRecipe";
	private static final String RECIPE_CATEGORY_LIST_TITLE = "§6Cookie §bCategories";
	private static final String RECIPE_LIST_TITLE = "§6Cookies §2Recipes";
	private static final String RECIPE_TITLE = "§6Cookie §2Recipe";
	private static final String MACHINE_RECIPE_LIST_TITLE = "§6Machine §cRecipes";
	private static final String MACHINE_RECIPE_TITLE = "§6Machine §cRecipe";
	private static final String MACHINE_UPGRADE_TITLE = "§bMachine Upgrades";

	public static void openVanillaRecipeList(HumanEntity player) {
		openVanillaRecipeList(player, 1, SortType.DEFAULT);
	}


	public static void openVanillaRecipeList(HumanEntity player, int page, SortType type) {
		List<Material> materials = type.getMaterials();
		int lastPage = (materials.size() + 35) / 36;
		if(lastPage == 0) {
			return;
		}
		page = ((page + lastPage - 1) % lastPage) + 1;
		Inventory gui = Bukkit.createInventory(null, 6 * 9, VANILLA_RECIPE_LIST_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler(Material.MAGENTA_STAINED_GLASS_PANE));
			gui.setItem(53 - i, filler(Material.MAGENTA_STAINED_GLASS_PANE));
		}
		for(int i = 0; i < 36; i++) {
			if((page - 1) * 36 + i >= materials.size())
				break;
			gui.setItem(i + 9, new ItemStack(materials.get((page - 1) * 36 + i)));
		}
		gui.setItem(4, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName("§5Sort Type: §d" + type.getName()).setCustomModelData(page).build());
		gui.setItem(48, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(49, new ItemBuilder(Material.PAPER).setName("§6Current Page: §e" + page).build());
		gui.setItem(50, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	public static void openRecipeCategoryList(HumanEntity player, int page, boolean cheat) {
		List<RecipeCategory> categories = RecipeCategory.getCategoriesWithEntries(cheat);
		int lastPage = (categories.size() + 35) / 36;
		if(lastPage == 0) {
			return;
		}
		page = ((page + lastPage - 1) % lastPage) + 1;
		int rows = Math.min(4, (categories.size() + 8) / 9);
		Inventory gui = Bukkit.createInventory(null, (rows + 2) * 9, RECIPE_CATEGORY_LIST_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE));
			gui.setItem(gui.getSize() - 1 - i, filler(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE));
		}
		for(int i = 0; i < gui.getSize(); i++) {
			if((page - 1) * 36 + i >= categories.size())
				break;
			gui.setItem(i + 9, categories.get((page - 1) * 36 + i).getIcon());
		}
		gui.setItem(gui.getSize() - 6, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.MAP).setName("§6Current Page: §e" + page).setCustomModelData(page).build());
		gui.setItem(gui.getSize() - 4, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	public static void openRecipeList(HumanEntity player, RecipeCategory category, int page, boolean cheat) {
		if(category == null) {
			return;
		}
		List<Recipe> recipes = category.getRecipes();
		int lastPage = (recipes.size() + 35) / 36;
		if(lastPage == 0) {
			return;
		}
		page = ((page + lastPage - 1) % lastPage) + 1;
		int rows = Math.min(4, (recipes.size() + 8) / 9);
		Inventory gui = Bukkit.createInventory(null, (rows + 2) * 9, RECIPE_LIST_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE));
			gui.setItem(gui.getSize() - 1 - i, filler(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE));
		}
		for(int i = 0; i < gui.getSize() - 18; i++) {
			if((page - 1) * 36 + i >= recipes.size())
				break;
			gui.setItem(i + 9, new ItemBuilder(recipes.get((page - 1) * 36 + i).getResult()).setAmount(1).build());
		}
		gui.setItem(4, category.getIcon());
		gui.setItem(gui.getSize() - 6, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.MAP).setName("§6Current Page: §e" + page).setCustomModelData(page).build());
		gui.setItem(gui.getSize() - 4, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	@SuppressWarnings("deprecation")
	public static void openRecipeGUI(Recipe recipe, ItemStack result, int variation, HumanEntity player, String title) {
		Material craftingType = Material.CRAFTING_TABLE;
		String craftingTypeName = "§cUnknown";
		result.setAmount(recipe.getResult().getAmount());
		Inventory gui = Bukkit.createInventory(null, 5 * 9, title);
		ItemStack filler = filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(gui.getSize() - 1 - i, filler);
		}
		for(int i = 0; i < gui.getSize() / 9; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		for(int i = 4; i < 8; i++) {
			for(int j = 1; j < 4; j++) {
				gui.setItem(j * 9 + i, filler(Material.LIME_STAINED_GLASS_PANE));
			}
		}
		gui.setItem(4, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§e<-- §aBack").build());
		gui.setItem(24, result);
		gui.setItem(22, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§4Recipe Variation: §c" + variation).setCustomModelData(variation).build());
		gui.setItem(38, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§e<-   §6Previous Variation").build());
		gui.setItem(42, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Next variation   §e->").build());
		if(Machine.isMachine(result.getItemMeta())) {
			gui.setItem(6, new ItemBuilder(Material.BOOK).setName("§6Show Machine Recipes").build());
		}
		if(recipe instanceof ShapelessRecipe) {
			craftingTypeName = "§6Shapeless Crafting";
			int i = 0;
			for(ItemStack ingredient : ((ShapelessRecipe) recipe).getIngredientList()) {
				int column = i % 3;
				int row = (i - column) / 3 + 1;
				gui.setItem(row * 9 + column + 1, ingredient);
				i++;
			}
		} else if(recipe instanceof ShapedRecipe) {
			craftingTypeName = "§6Shaped Crafting";
			ShapedRecipe shaped = (ShapedRecipe) recipe;
			Map<Character, ItemStack> ingredientMap = shaped.getIngredientMap();
			ItemStack[] pattern = new ItemStack[9];
			String[] shape = shaped.getShape();
			for(int i = 0; i < shape.length; i++) {
				String str = shape[i];
				for(int j = 0; j < str.length(); j++) {
					char c = str.charAt(j);
					pattern[i * 3 + j] = ingredientMap.get(c);
				}
			}
			for(int i = 0; i < pattern.length; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, pattern[i]);
			}
		} else if(recipe instanceof CustomRecipe) {
			craftingType = null;
			craftingTypeName = null;
			CustomRecipe custom = (CustomRecipe) recipe;
			Map<Character, ItemStack> ingredientMap = custom.getIngredientMap();
			String[] shape = custom.getShape();
			for(int i = 0; i < shape.length; i++) {
				String str = shape[i];
				for(int j = 0; j < str.length(); j++) {
					char c = str.charAt(j);
					gui.setItem((i + 1) * 9 + j + 1, ingredientMap.get(c));
				}
			}
			gui.setItem(2, new ItemBuilder(custom.getCategory().getIcon()).setName(custom.getCategory().getName()).build());
			gui.setItem(40, new ItemBuilder(custom.getType().getIcon()).setName(custom.getType().getName()).build());
		} else if(recipe instanceof FurnaceRecipe) {
			craftingType = Material.FURNACE;
			craftingTypeName = "§8Furnace";
			FurnaceRecipe furnace = (FurnaceRecipe) recipe;
			for(int i = 0; i < 9; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, filler(Material.GRAY_STAINED_GLASS_PANE));
			}
			gui.setItem(20, furnace.getInput());
			int burntime = furnace.getCookingTime();
			int itemsPerCoal = 1600 / burntime;
			gui.setItem(29, new ItemBuilder(Material.COAL).setName("§eCooking Time§8: §c" + burntime + " §8| §6" + itemsPerCoal + " §aItems §6per §8Coal").build());
		} else if(recipe instanceof BlastingRecipe) {
			craftingType = Material.BLAST_FURNACE;
			craftingTypeName = "§7Blasting";
			BlastingRecipe blasting = (BlastingRecipe) recipe;
			for(int i = 0; i < 9; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, filler(Material.GRAY_STAINED_GLASS_PANE));
			}
			gui.setItem(20, blasting.getInput());
			int burntime = blasting.getCookingTime();
			int itemsPerCoal = 3200 / burntime;
			gui.setItem(29, new ItemBuilder(Material.CHARCOAL).setName("§eCooking Time§8: §c" + burntime + " §8| §6" + itemsPerCoal + " §aItems §6per §8Coal").build());
		} else if(recipe instanceof SmokingRecipe) {
			craftingType = Material.SMOKER;
			craftingTypeName = "§6Smoking";
			SmokingRecipe smoking = (SmokingRecipe) recipe;
			for(int i = 0; i < 9; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, filler(Material.BROWN_STAINED_GLASS_PANE));
			}
			gui.setItem(20, smoking.getInput());
			int burntime = smoking.getCookingTime();
			int itemsPerCoal = 3200 / burntime;
			gui.setItem(29, new ItemBuilder(Material.CHARCOAL).setName("§eCooking Time§8: §c" + burntime + " §8| §6" + itemsPerCoal + " §aItems §6per §8Coal").build());
		} else if(recipe instanceof CampfireRecipe) {
			craftingType = Material.CAMPFIRE;
			craftingTypeName = "§6Campfire";
			CampfireRecipe campfire = (CampfireRecipe) recipe;
			for(int i = 0; i < 9; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, filler(Material.ORANGE_STAINED_GLASS_PANE));
			}
			gui.setItem(20, campfire.getInput());
			int burntime = campfire.getCookingTime();
			gui.setItem(29, new ItemBuilder(Material.CAMPFIRE).setName("§eCooking Time§8: §c" + burntime).build());
		} else if(recipe instanceof StonecuttingRecipe) {
			craftingType = Material.STONECUTTER;
			craftingTypeName = "§7Stonecutting";
			StonecuttingRecipe cutting = (StonecuttingRecipe) recipe;
			for(int i = 0; i < 9; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, filler(Material.GRAY_STAINED_GLASS_PANE));
			}
			gui.setItem(20, cutting.getInput());
			gui.setItem(29, new ItemStack(Material.STONECUTTER));
		} else if(recipe instanceof SmithingRecipe) {
			craftingType = Material.SMITHING_TABLE;
			craftingTypeName = "§8Smithing";
			SmithingRecipe smithing = (SmithingRecipe) recipe;
			for(int i = 0; i < 9; i++) {
				int column = i % 3;
				int row = (i - column) / 3;
				int slot = (row + 1) * 9 + (column + 1);
				gui.setItem(slot, filler(Material.BLACK_STAINED_GLASS_PANE));
			}
			gui.setItem(11, smithing.getAddition().getItemStack());
			gui.setItem(20, smithing.getBase().getItemStack());
			gui.setItem(29, new ItemStack(Material.SMITHING_TABLE));
		}
		if(craftingType != null && craftingTypeName != null)
			gui.setItem(40, new ItemBuilder(craftingType).setName(craftingTypeName).build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	public static void openMachineRecipeList(HumanEntity player, Machine machine, int page) {
		List<MachineRecipe> recipes = machine.getMachineRecipes();
		int lastPage = (recipes.size() + 35) / 36;
		if(lastPage == 0) {
			return;
		}
		page = ((page + lastPage - 1) % lastPage) + 1;
		Inventory gui = Bukkit.createInventory(null, (2 + Math.min(4, (recipes.size() + 8) / 9)) * 9, MACHINE_RECIPE_LIST_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler(Material.CYAN_STAINED_GLASS_PANE));
			gui.setItem(gui.getSize() - 1 - i, filler(Material.CYAN_STAINED_GLASS_PANE));
		}
		for(int i = 0; i < gui.getSize() - 18; i++) {
			if((page - 1) * 36 + i >= recipes.size()) {
				gui.setItem(9 + i, filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
			} else {
				MachineRecipe recipe = recipes.get((page - 1) * 36 + i);
				gui.setItem(9 + i, recipe.createIcon());
			}
		}
		gui.setItem(4, new ItemBuilder(machine.createIcon()).build());
		gui.setItem(gui.getSize() - 6, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.MAP).setName("§6Current Page: §e" + page).setCustomModelData(page).build());
		gui.setItem(gui.getSize() - 4, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	public static void openMachineRecipe(HumanEntity player, Machine machine, MachineRecipe recipe) {
		Inventory gui = recipe.display(MACHINE_RECIPE_TITLE);
		gui.setItem(4, new ItemBuilder(machine.createDefaultItemStack()).build());
		player.openInventory(gui);
	}


	public static void openUpgradeInventory(HumanEntity player, Machine machine, TileState block) {
		int upgradeCount = machine.getUpgradeSlots();
		ItemStack filler = filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		int rows = Math.min(4, (upgradeCount + 6) / 7);
		Inventory gui = Bukkit.createInventory(null, 18 + rows * 9, MACHINE_UPGRADE_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(gui.getSize() - 1 - i, filler);
		}
		for(int i = 1; i <= rows; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		MachineUpgrade[] upgrades = machine.getAllowedMachineUpgrades();
		int i;
		for(i = 0; i < upgrades.length; i++) {
			MachineUpgrade upgrade = upgrades[i];
			int amount = upgrade.fetch(block);
			if(amount > 0) {
				int r = i / 7;
				int c = i - r * 7;
				gui.setItem(r * 9 + c + 10, new ItemBuilder(upgrade.getItem().get()).setAmount(amount).build());
			}
		}
		filler = filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < rows * 7; i++) {
			int r = i / 7;
			int c = i - r * 7;
			gui.setItem(r * 9 + c + 10, filler);
		}
		ItemStack icon = machine.createIcon();
		setIdentifierStack(block.getLocation(), icon);
		gui.setItem(4, icon);
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	public static Listener getInventoryListener() {
		return new Listener() {

			@SuppressWarnings("deprecation")
			@EventHandler
			public void onInventoryClick(InventoryClickEvent event) {
				final HumanEntity player = event.getWhoClicked();
				final String title = event.getView().getTitle();
				final int slot = event.getSlot();
				final ItemStack stack = event.getCurrentItem();
				final Inventory gui = event.getInventory();
				if(title.equals(VANILLA_RECIPE_LIST_TITLE)) {
					// Recipe List
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					SortType type = SortType.byName(gui.getItem(4).getItemMeta().getDisplayName().substring(15));
					final int page = gui.getItem(4).getItemMeta().getCustomModelData();
					if(slot >= 9 && slot < 45) {
						if(stack != null) {
							List<Recipe> recipes = RecipeManager.getAllRecipesFor(stack);
							if(recipes.size() > 0) {
								registerInventory(player, gui);
								openRecipeGUI(recipes.get(0), recipes.get(0).getResult(), 0, player, VANILLA_RECIPE_TITLE);
							}
						}
					} else if(slot == 48) {
						openVanillaRecipeList(player, page - 1, type);
					} else if(slot == 50) {
						openVanillaRecipeList(player, page + 1, type);
					} else if(slot == 4) {
						openVanillaRecipeList(player, page, type.loop());
					}
				} else if(title.equals(VANILLA_RECIPE_TITLE)) {
					// Recipe
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					if(slot > 9 && slot < 13 || slot > 18 && slot < 22 || slot > 27 && slot < 31) {
						if(stack != null && !isFiller(stack)) {
							List<Recipe> recipes = RecipeManager.getAllRecipesFor(stack);
							if(recipes.size() > 0) {
								registerInventory(player, gui);
								openRecipeGUI(recipes.get(0), recipes.get(0).getResult(), 0, player, VANILLA_RECIPE_TITLE);
							}
						}
					} else if(slot == 4) {
						openLastInventory(player, false);
					} else if(slot == 38 || slot == 42) {
						int variation = gui.getItem(22).getItemMeta().getCustomModelData();
						List<Recipe> recipes = RecipeManager.getAllRecipesFor(gui.getItem(24));
						if(recipes.size() > 1) {
							variation = (variation + recipes.size() + (slot - 40) / 2) % recipes.size();
							openRecipeGUI(recipes.get(variation), recipes.get(0).getResult(), variation, player, VANILLA_RECIPE_TITLE);
						}
					}
				} else if(title.equals(RECIPE_CATEGORY_LIST_TITLE)) {
					// Recipe Categories
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					final boolean cheat = gui.getItem(0).getType() == Material.YELLOW_STAINED_GLASS_PANE;
					final int page = gui.getItem(gui.getSize() - 5).getItemMeta().getCustomModelData();
					if(slot >= 9 && slot < gui.getSize() - 9) {
						if(stack != null) {
							RecipeCategory category = RecipeCategory.getCategoryFromIconStack(stack);
							if(category != null) {
								registerInventory(player, gui);
								openRecipeList(player, category, page, cheat);
							}
						}
					} else if(slot == gui.getSize() - 6) {
						openRecipeCategoryList(player, page - 1, cheat);
					} else if(slot == gui.getSize() - 4) {
						openRecipeCategoryList(player, page + 1, cheat);
					}
				} else if(title.equals(RECIPE_LIST_TITLE)) {
					// Recipe List
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					final boolean cheat = gui.getItem(0).getType() == Material.YELLOW_STAINED_GLASS_PANE;
					final RecipeCategory category = RecipeCategory.getCategoryFromIconStack(gui.getItem(4));
					final int page = gui.getItem(gui.getSize() - 5).getItemMeta().getCustomModelData();
					if(slot >= 9 && slot < gui.getSize() - 9) {
						if(stack != null) {
							if(cheat) {
								event.getWhoClicked().getInventory().addItem(stack);
							} else {
								List<Recipe> recipes = RecipeManager.getAllRecipesFor(stack).stream().map(recipe -> (Recipe) recipe).collect(Collectors.toList());
								recipes.addAll(RecipeManager.getAllRecipesFor(stack).stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList()));
								if(recipes.size() > 0) {
									registerInventory(player, gui);
									openRecipeGUI(recipes.get(0), gui.getItem(slot).clone(), 0, player, RECIPE_TITLE);
								}
							}
						}
					} else if(slot == 4) {
						openLastInventory(player, cheat);
					} else if(slot == gui.getSize() - 6) {
						openRecipeList(player, category, page - 1, cheat);
					} else if(slot == gui.getSize() - 4) {
						openRecipeList(player, category, page + 1, cheat);
					}
				} else if(title.equals(RECIPE_TITLE)) {
					// Recipe
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					RecipeCategory category = RecipeCategory.getCategoryFromIconStack(gui.getItem(2));
					if(category == null)
						category = RecipeCategory.getCategoryFromResult(gui.getItem(24));
					if(slot > 9 && slot < 13 || slot > 18 && slot < 22 || slot > 27 && slot < 31) {
						if(stack != null) {
							List<Recipe> recipes = RecipeManager.getAllRecipesFor(stack).stream().map(recipe -> (Recipe) recipe).collect(Collectors.toList());
							recipes.addAll(RecipeManager.getAllRecipesFor(stack).stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList()));
							if(recipes.size() > 0) {
								registerInventory(player, gui);
								openRecipeGUI(recipes.get(0), gui.getItem(slot).clone(), 0, player, RECIPE_TITLE);
							}
						}
					} else if(slot == 4) {
						openLastInventory(player, false);
					} else if(slot == 38 || slot == 42) {
						int variation = event.getInventory().getItem(22).getItemMeta().getCustomModelData();
						ItemStack result = gui.getItem(24);
						List<Recipe> recipes = RecipeManager.getAllRecipesFor(result).stream().map(recipe -> (Recipe) recipe).collect(Collectors.toList());
						recipes.addAll(RecipeManager.getAllRecipesFor(stack).stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList()));
						if(recipes.size() > 1) {
							variation = (variation + recipes.size() + (slot - 40) / 2) % recipes.size();
							openRecipeGUI(recipes.get(variation), result.clone(), variation, player, RECIPE_TITLE);
						}
					} else if(Machine.isMachine(event.getInventory().getItem(24).getItemMeta())) {
						Machine machine = CustomBlockSetup.getMachineFromStack(event.getInventory().getItem(24));
						if(machine != null) {
							registerInventory(player, gui);
							openMachineRecipeList(player, machine, 1);
						}
					}
				} else if(title.equals(MACHINE_RECIPE_LIST_TITLE)) {
					// Machine Recipe List
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					final Machine machine = CustomBlockSetup.getMachineFromStack(event.getInventory().getItem(4));
					final int page = gui.getItem(gui.getSize() - 5).getItemMeta().getCustomModelData();
					if(slot == 4) {
						openLastInventory(player, false);
					} else if(slot == gui.getSize() - 6 || slot == gui.getSize() - 4) {
						if(machine.getMachineRecipes().size() > 36)
							openMachineRecipeList(player, machine, page + slot - gui.getSize() + 5);
					} else if(slot > 8 && slot < gui.getSize() - 9) {
						int index = (page - 1) * 36 + slot - 9;
						List<MachineRecipe> recipes = machine.getMachineRecipes();
						if(index < recipes.size()) {
							registerInventory(player, gui);
							openMachineRecipe(player, machine, recipes.get(index));
						}
					}
				} else if(title.equals(MACHINE_RECIPE_TITLE)) {
					// Machine Recipe
					event.setCancelled(true);
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					if(slot == 4) {
						openLastInventory(player, false);
					}
				} else if(title.equals(MACHINE_UPGRADE_TITLE)) {
					// Machine Upgrades
					if(event.getClickedInventory() instanceof PlayerInventory)
						return;
					event.setCancelled(true);
					final Machine machine = CustomBlockSetup.getMachineFromStack(event.getInventory().getItem(4));
					Location location = getIdentifierLocation(player.getWorld(), gui.getItem(4));
					Block block = location.getBlock();
					if(block.getState() instanceof TileState && player instanceof Player) {
						int r = slot % 9;
						int c = slot - r * 9;
						System.out.println("r: " + r + ", c: " + c);
						if(0 < r && r * 9 < gui.getSize() - 9 && 0 < c && c < 8) {
							ItemStack current = event.getCurrentItem();
							ItemStack cursor = event.getCursor();
							System.out.println("Current: " + current);
							System.out.println("Cursor: " + cursor);
							MachineUpgrade currentUpgrade = MachineUpgrade.fromStack(current);
							MachineUpgrade cursorUpgrade = MachineUpgrade.fromStack(cursor);
							System.out.println("Current Upgrade: " + (currentUpgrade == null ? "null" : currentUpgrade.getRegistryName()));
							System.out.println("Cursor Upgrade: " + (cursorUpgrade == null ? "null" : cursorUpgrade.getRegistryName()));
							if(cursorUpgrade == null) {
								if(Utilities.isEmpty(cursor)) {
									event.setCursor(current.clone());
									current.setAmount(0);
								}
							} else if(machine.supportMachineUpgrade(cursorUpgrade)) {
								System.out.println("is supported");
								if(currentUpgrade == null) {
									System.out.println("transfer all");
									int transfer = Math.max(cursorUpgrade.getMaxStackSize(), cursor.getAmount());
									ItemStack newCurrent = cursor.clone();
									newCurrent.setAmount(transfer);
									event.setCurrentItem(newCurrent);
								} else {
									if(currentUpgrade.equals(cursorUpgrade)) {
										int transfer = Math.max(cursorUpgrade.getMaxStackSize(), cursor.getAmount()) - current.getAmount();
										current.setAmount(current.getAmount() + transfer);
										cursor.setAmount(cursor.getAmount() - transfer);
									}
								}
							}
						}
						machine.onUpgradeInventoryInteract((Player) player, (TileState) location.getBlock().getState(), gui, event);
					}
					if(slot == 4) {
						openLastInventory(player, false);
					}
				}
			}


			@EventHandler
			public void onInventoryClose(InventoryCloseEvent event) {
				final String title = event.getView().getTitle();
				final HumanEntity player = event.getPlayer();
				final Inventory gui = event.getInventory();
				if(title.equals(MACHINE_UPGRADE_TITLE)) {
					final Machine machine = CustomBlockSetup.getMachineFromStack(event.getInventory().getItem(4));
					Location location = getIdentifierLocation(player.getWorld(), gui.getItem(4));
					Block block = location.getBlock();
					if(block.getState() instanceof TileState && player instanceof Player) {
						TileState state = (TileState) block.getState();
						int rows = gui.getSize() / 9 - 2;
						for(int i = 0; i < rows * 7; i++) {
							int r = i % 7;
							int c = i - r * 9;
							ItemStack stack = gui.getItem(r * 9 + c + 10);
							if(Utilities.isEmpty(stack))
								return;
							MachineUpgrade upgrade = MachineUpgrade.fromStack(stack);
							if(upgrade == null)
								continue;
							upgrade.store(state, stack.getAmount());
						}
						machine.onUpgradeInventoryClose((Player) player, state, gui, event);
					}
				}
			}

		};
	}


	public static void registerInventory(HumanEntity player, Inventory gui) {
		UUID uuid = player.getUniqueId();
		if(!inventoryHistory.containsKey(uuid)) {
			inventoryHistory.put(uuid, new ArrayDeque<>());
		}
		inventoryHistory.get(uuid).addLast(gui);
	}


	public static void openLastInventory(HumanEntity player, boolean cheat) {
		UUID uuid = player.getUniqueId();
		Inventory gui = null;
		if(inventoryHistory.containsKey(uuid)) {
			gui = inventoryHistory.get(uuid).removeLast();
		}
		if(gui != null) {
			player.openInventory(gui);
		} else {
			openRecipeCategoryList(player, 0, cheat);
		}
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}

	private static final VectorProperty LOCATION = new VectorProperty("location");

	public static void setIdentifierStack(Location location, ItemStack identifier) {
		ItemMeta meta = identifier.getItemMeta();
		LOCATION.store(meta, location.toVector());
		identifier.setItemMeta(meta);
	}


	public static Location getIdentifierLocation(World world, ItemStack identifier) {
		Vector vector = LOCATION.fetch(identifier.getItemMeta());
		if(vector == null)
			return null;
		return vector.toLocation(world);
	}


	public static ItemStack filler(Material material) {
		return new ItemBuilder(material).setName(" ").setLocalizedName("_filler_").build();
	}


	public static boolean isFiller(ItemStack stack) {
		if(stack == null)
			return false;
		if(!stack.hasItemMeta())
			return false;
		if(!stack.getItemMeta().hasLocalizedName())
			return false;
		return stack.getItemMeta().getLocalizedName().equals("_filler_");
	}

	private enum SortType {

		DEFAULT(() -> materials_by_default), NAME(() -> materials_by_name);

		private Supplier<List<Material>> supplier;
		private String name;

		private SortType(Supplier<List<Material>> supplier) {
			this.supplier = supplier;
			this.name = Utilities.toCapitalWords(this.name());
		}


		public List<Material> getMaterials() {
			return this.supplier.get();
		}


		public String getName() {
			return this.name;
		}


		public SortType loop() {
			return values()[(this.ordinal() + 1) % values().length];
		}


		public static SortType byName(String name) {
			for(SortType type : values()) {
				if(type.name().equalsIgnoreCase(name)) {
					return type;
				}
			}
			return null;
		}

	}

}
