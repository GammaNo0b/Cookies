
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class VanillaRecipeBook extends AbstractCustomItem {

	private static final List<Material> materials_by_default = Arrays.asList(Material.values()).stream().filter(Material::isItem).filter(material -> Bukkit.getRecipesFor(new ItemStack(material)).size() > 0).collect(Collectors.toList());
	private static final List<Material> materials_by_name = materials_by_default.stream().sorted((m1, m2) -> m1.name().compareTo(m2.name())).collect(Collectors.toList());
	
	private static final String RECIPE_LIST_TITLE = "§aVanilla §dRecipes";
	private static final String RECIPE_TITLE = "§aVanilla §dRecipe";

	@Override
	public String getIdentifier() {
		return "vanilla_recipe_book";
	}


	@Override
	public String getDisplayName() {
		return "§aVanilla Recipe Book";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Search for every Recipe in Vanilla!");
	}


	@Override
	public Material getMaterial() {
		return Material.KNOWLEDGE_BOOK;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape(" A ", "CBD", " E ");
		recipe.setIngredient('A', Material.STICK);
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('C', Material.INK_SAC);
		recipe.setIngredient('D', Material.IRON_NUGGET);
		recipe.setIngredient('E', Material.EMERALD);
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		event.setCancelled(true);
		openRecipeList(player, 1, SortType.DEFAULT);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		event.setCancelled(true);
		openRecipeList(player, 1, SortType.DEFAULT);
	}


	public static void openRecipeList(HumanEntity player, int page, SortType type) {
		List<Material> materials = type.getMaterials();
		int lastPage = (materials.size() + 35) / 36;
		if(lastPage == 0) {
			return;
		}
		page = ((page + lastPage - 1) % lastPage) + 1;
		Inventory gui = Bukkit.createInventory(null, 6 * 9, RECIPE_LIST_TITLE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(53 - i, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < 36; i++) {
			if((page - 1) * 36 + i >= materials.size())
				break;
			gui.setItem(i + 9, new ItemStack(materials.get((page - 1) * 36 + i)));
		}
		gui.setItem(4, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName("§5Sort Type: §d" + type.getName()).build());
		gui.setItem(48, new ItemBuilder(Material.PAPER).setName("§7<---   Previous Page").build());
		gui.setItem(49, new ItemBuilder(Material.PAPER).setName("§6Current Page: §e" + page).build());
		gui.setItem(50, new ItemBuilder(Material.PAPER).setName("§7Next Page   --->").build());
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
				if(title.equals(RECIPE_LIST_TITLE)) {
					// Recipe List
					SortType type = SortType.byName(event.getInventory().getItem(4).getItemMeta().getDisplayName().substring(15));
					event.setCancelled(true);
					final int page;
					try {
						page = Integer.parseInt(event.getInventory().getItem(49).getItemMeta().getDisplayName().substring(18));
					} catch(NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					if(slot >= 9 && slot < 45) {
						if(stack != null) {
							List<Recipe> recipes = Bukkit.getRecipesFor(stack);
							if(recipes.size() > 0) {
								RecipeManager.openRecipeGUI(recipes.get(0), recipes.get(0).getResult(), 0, page, player, RECIPE_TITLE);
							}
						}
					} else if(slot == 48) {
						openRecipeList(player, page - 1, type);
					} else if(slot == 50) {
						openRecipeList(player, page + 1, type);
					} else if(slot == 4) {
						openRecipeList(player, 1, type.loop());
					}
				} else if(title.equals(RECIPE_TITLE)) {
					// Recipe
					event.setCancelled(true);
					final int page;
					try {
						page = Integer.parseInt(event.getInventory().getItem(4).getItemMeta().getDisplayName().substring(23));
					} catch(NumberFormatException e) {
						e.printStackTrace();
						return;
					}
					if(slot > 9 && slot < 13 || slot > 18 && slot < 22 || slot > 27 && slot < 31) {
						if(stack != null && CookieRecipe.sameIngredient(stack, new ItemStack(stack.getType()))) {
							List<Recipe> recipes = Bukkit.getRecipesFor(stack);
							if(recipes.size() > 0) {
								RecipeManager.openRecipeGUI(recipes.get(0), recipes.get(0).getResult(), 0, page, player, RECIPE_TITLE);
							}
						}
					} else if(slot == 4) {
						openRecipeList(player, page, SortType.DEFAULT);
					} else if(slot == 38 || slot == 42) {
						try {
							int variation = Integer.parseInt(event.getInventory().getItem(22).getItemMeta().getDisplayName().substring(22));
							List<Recipe> recipes = Bukkit.getRecipesFor(event.getInventory().getItem(24));
							if(recipes.size() > 1) {
								variation = (variation + recipes.size() + (slot - 40) / 2) % recipes.size();
								RecipeManager.openRecipeGUI(recipes.get(variation), recipes.get(0).getResult(), variation, page, player, RECIPE_TITLE);
							}
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}

		};
	}

	private enum SortType {

		DEFAULT(() -> materials_by_default),
		NAME(() -> materials_by_name);
		
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
