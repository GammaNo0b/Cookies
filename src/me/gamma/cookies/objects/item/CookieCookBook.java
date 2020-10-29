
package me.gamma.cookies.objects.item;


import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.block.skull.AbstractMachine;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.recipe.AdvancedMachineRecipe;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.MachineRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.objects.recipe.SimpleMachineRecipe;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;



public class CookieCookBook extends AbstractCustomItem {

	private static final Map<UUID, Deque<Inventory>> inventoryHistory = new HashMap<>();

	private static BooleanProperty OPEN_ON_EAT = BooleanProperty.create("eat");

	private static final String RECIPE_CATEGORY_LIST_TITLE = "§6Cookie §bCategories";
	private static final String RECIPE_LIST_TITLE = "§6Cookies §2Recipes";
	private static final String RECIPE_TITLE = "§6Cookie §2Recipe";
	private static final String MACHINE_RECIPE_LIST_TITLE = "§4Machine §cRecipes";

	@Override
	public String getIdentifier() {
		return "cookie_cook_book";
	}


	@Override
	public String getDisplayName() {
		return "§6Cookie Cook Book";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Collection of all Recipes", "§7written down in a Cook Book.");
	}


	@Override
	public Material getMaterial() {
		return Material.COOKIE;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		OPEN_ON_EAT.store(meta, true);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape(" H ", "SGB", " C ");
		recipe.setIngredient('H', Material.HONEY_BOTTLE);
		recipe.setIngredient('S', Material.SUGAR);
		recipe.setIngredient('G', Material.BOOK);
		recipe.setIngredient('B', Material.COCOA_BEANS);
		recipe.setIngredient('C', Material.COOKIE);
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		if(player.isSneaking()) {
			ItemMeta meta = stack.getItemMeta();
			if(OPEN_ON_EAT.toggle(meta)) {
				player.sendMessage("§aOpen on Eat enabled!");
			} else {
				player.sendMessage("§cOpen on Eat disabled!");
			}
			stack.setItemMeta(meta);
			event.setCancelled(true);
		} else {
			if(!OPEN_ON_EAT.fetch(stack.getItemMeta())) {
				openRecipeCategoryList(player, 0, false);
				event.setCancelled(true);
			}
		}
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(player.isSneaking()) {
			ItemMeta meta = stack.getItemMeta();
			if(OPEN_ON_EAT.toggle(meta)) {
				player.sendMessage("§aOpen on Eat enabled!");
			} else {
				player.sendMessage("§cOpen on Eat disabled!");
			}
			stack.setItemMeta(meta);
			event.setCancelled(true);
		} else {
			if(!OPEN_ON_EAT.fetch(stack.getItemMeta())) {
				openRecipeCategoryList(player, 0, false);
				event.setCancelled(true);
			}
		}
	}


	@Override
	public void onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
		openRecipeCategoryList(player, 0, false);
		event.setCancelled(true);
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
			gui.setItem(i, new ItemBuilder(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < gui.getSize(); i++) {
			if((page - 1) * 36 + i >= categories.size())
				break;
			gui.setItem(i + 9, categories.get((page - 1) * 36 + i).getIcon());
		}
		gui.setItem(gui.getSize() - 6, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.PAPER).setName("§6Current Page: §e" + page).build());
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
			gui.setItem(i, new ItemBuilder(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(cheat ? Material.YELLOW_STAINED_GLASS_PANE : Material.ORANGE_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < gui.getSize(); i++) {
			if((page - 1) * 36 + i >= recipes.size())
				break;
			gui.setItem(i + 9, new ItemBuilder(recipes.get((page - 1) * 36 + i).getResult()).setAmount(1).build());
		}
		gui.setItem(4, category.getIcon());
		gui.setItem(gui.getSize() - 6, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.PAPER).setName("§6Current Page: §e" + page).build());
		gui.setItem(gui.getSize() - 4, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	public static void openMachineRecipeList(HumanEntity player, AbstractMachine machine, int page, int currentPage) {
		List<MachineRecipe> recipes = machine.getMachineRecipes();
		int lastPage = (recipes.size() + 8) / 9;
		if(lastPage == 0) {
			return;
		}
		page = ((page + lastPage - 1) % lastPage) + 1;
		Inventory gui = Bukkit.createInventory(null, 45, MACHINE_RECIPE_LIST_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < 9; i++) {
			if((page - 1) * 9 + i >= recipes.size()) {
				gui.setItem(9 + i, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(18 + i, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").build());
				gui.setItem(27 + i, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").build());
			} else {
				MachineRecipe recipe = recipes.get((page - 1) * 9 + i);
				gui.setItem(9 + i, recipe.getResult());
				if(recipe instanceof SimpleMachineRecipe) {
					gui.setItem(18 + i, recipe.getIngredients()[0]);
					gui.setItem(27 + i, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").build());
				} else if(recipe instanceof AdvancedMachineRecipe) {
					AdvancedMachineRecipe advanced = (AdvancedMachineRecipe) recipe;
					if(advanced.getIngredients().length <= 2) {
						gui.setItem(18 + i, recipe.getIngredients()[0]);
						if(advanced.getIngredients().length == 2)
							gui.setItem(27 + i, recipe.getIngredients()[1]);
						else
							gui.setItem(27 + i, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").build());
					} else {
						gui.setItem(27 + i, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ").build());
					}
				}
			}
		}
		gui.setItem(0, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName(" ").setCustomModelData(currentPage).build());
		gui.setItem(4, machine.createDefaultItemStack());
		gui.setItem(gui.getSize() - 6, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.PAPER).setName("§6Current Page: §e" + page).build());
		gui.setItem(gui.getSize() - 4, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
		player.openInventory(gui);
		if(player instanceof Player)
			((Player) player).playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 10.0F, 1.0F);
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onInventoryClick(InventoryClickEvent event) {
				final HumanEntity player = event.getWhoClicked();
				final String title = event.getView().getTitle();
				final int slot = event.getSlot();
				final ItemStack stack = event.getCurrentItem();
				final Inventory gui = event.getInventory();
				if(title.equals(RECIPE_CATEGORY_LIST_TITLE)) {
					// Recipe Categories
					event.setCancelled(true);
					final boolean cheat = gui.getItem(0).getType() == Material.YELLOW_STAINED_GLASS_PANE;
					final int page;
					try {
						page = Integer.parseInt(gui.getItem(gui.getSize() - 5).getItemMeta().getDisplayName().substring(18));
					} catch(NumberFormatException e) {
						e.printStackTrace();
						return;
					}
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
					final boolean cheat = gui.getItem(0).getType() == Material.YELLOW_STAINED_GLASS_PANE;
					final RecipeCategory category = RecipeCategory.getCategoryFromIconStack(gui.getItem(2));
					final int page;
					try {
						page = Integer.parseInt(gui.getItem(gui.getSize() - 5).getItemMeta().getDisplayName().substring(18));
					} catch(NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					if(slot >= 9 && slot < gui.getSize() - 9) {
						if(stack != null) {
							if(cheat) {
								event.getWhoClicked().getInventory().addItem(stack);
							} else {
								List<Recipe> recipes = RecipeManager.getCookieRecipesFromStack(stack).stream().map(recipe -> (Recipe) recipe).collect(Collectors.toList());
								recipes.addAll(Bukkit.getRecipesFor(stack).stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList()));
								if(recipes.size() > 0) {
									registerInventory(player, gui);
									RecipeManager.openRecipeGUI(recipes.get(0), gui.getItem(slot), 0, page, player, RECIPE_TITLE);
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
					RecipeCategory category = RecipeCategory.getCategoryFromIconStack(gui.getItem(2));
					if(category == null)
						category = RecipeCategory.getCategoryFromResult(gui.getItem(24));
					final int page;
					try {
						page = Integer.parseInt(gui.getItem(4).getItemMeta().getDisplayName().substring(23));
					} catch(NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					if(slot > 9 && slot < 13 || slot > 18 && slot < 22 || slot > 27 && slot < 31) {
						if(stack != null) {
							List<Recipe> recipes = RecipeManager.getCookieRecipesFromStack(stack).stream().map(recipe -> (Recipe) recipe).collect(Collectors.toList());
							recipes.addAll(Bukkit.getRecipesFor(stack).stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList()));
							if(recipes.size() > 0) {
								registerInventory(player, gui);
								RecipeManager.openRecipeGUI(recipes.get(0), gui.getItem(slot), 0, page, player, RECIPE_TITLE);
							}
						}
					} else if(slot == 4) {
						openLastInventory(player, false);
					} else if(slot == 38 || slot == 42) {
						try {
							int variation = Integer.parseInt(event.getInventory().getItem(22).getItemMeta().getDisplayName().substring(22));
							ItemStack result = gui.getItem(24);
							List<Recipe> recipes = RecipeManager.getCookieRecipesFromStack(result).stream().map(recipe -> (Recipe) recipe).collect(Collectors.toList());
							recipes.addAll(Bukkit.getRecipesFor(stack).stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList()));
							if(recipes.size() > 1) {
								variation = (variation + recipes.size() + (slot - 40) / 2) % recipes.size();
								RecipeManager.openRecipeGUI(recipes.get(variation), result, variation, page, player, RECIPE_TITLE);
							}
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					} else if(AbstractMachine.isMachine(event.getInventory().getItem(24).getItemMeta())) {
						AbstractMachine machine = CustomBlockSetup.getMachineFromStack(event.getInventory().getItem(24));
						if(machine != null) {
							registerInventory(player, gui);
							openMachineRecipeList(player, machine, 1, page);
						}
					}
				} else if(title.equals(MACHINE_RECIPE_LIST_TITLE)) {
					event.setCancelled(true);
					final AbstractMachine machine = CustomBlockSetup.getMachineFromStack(event.getInventory().getItem(4));
					final int page;
					try {
						page = Integer.parseInt(gui.getItem(40).getItemMeta().getDisplayName().substring(18));
					} catch(NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					final int currentPage = gui.getItem(0).getItemMeta().getCustomModelData();
					if(slot == 4) {
						openLastInventory(player, false);
					} else if(slot == 39 || slot == 41) {
						if(machine.getMachineRecipes().size() > 9)
							openMachineRecipeList(player, machine, page + slot - 40, currentPage);
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
	}

}
