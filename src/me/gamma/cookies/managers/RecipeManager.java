
package me.gamma.cookies.managers;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.block.skull.AbstractMachine;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ItemBuilder;



public class RecipeManager {

	private static final List<Recipe> registeredRecipes = new ArrayList<>();
	private static final List<CookieRecipe> registeredCookieRecipes = new ArrayList<>();

	public static void loadRecipes() {
		registerRecipes(CustomItemSetup.customItems.stream().map(AbstractCustomItem::getRecipe).filter(recipe -> recipe != null).collect(Collectors.toList()));
		registerRecipes(CustomBlockSetup.customBlocks.stream().map(AbstractTileStateBlock::getRecipe).filter(recipe -> recipe != null).collect(Collectors.toList()));
	}


	public static void registerRecipes(Collection<Recipe> recipes) {
		recipes.forEach(RecipeManager::registerRecipe);
	}


	public static <R extends Recipe> R registerRecipe(R recipe) {
		registeredRecipes.add(recipe);
		if(recipe instanceof CookieRecipe) {
			registeredCookieRecipes.add((CookieRecipe) recipe);
		} else {
			Bukkit.addRecipe(recipe);
		}
		return recipe;
	}


	public static List<Recipe> getRegisteredRecipes() {
		return registeredRecipes;
	}


	public static List<CookieRecipe> getRegisteredCustomRecipes() {
		return registeredCookieRecipes;
	}


	public static List<CookieRecipe> getRegisteredCustomRecipes(RecipeType type) {
		return registeredCookieRecipes.stream().filter(recipe -> recipe.getType() == type).collect(Collectors.toList());
	}


	public static Recipe getRecipeFromResult(ItemStack result) {
		for(Recipe recipe : registeredRecipes) {
			if(CookieRecipe.sameIngredient(recipe.getResult(), result)) {
				return recipe;
			}
		}

		return null;
	}


	public static List<CookieRecipe> getCookieRecipesFromStack(ItemStack stack) {
		return registeredCookieRecipes.stream().filter(recipe -> CookieRecipe.sameIngredient(stack, recipe.getResult())).collect(Collectors.toList());
	}


	@SuppressWarnings("deprecation")
	public static void openRecipeGUI(Recipe recipe, ItemStack result, int variation, int currentPage, HumanEntity player, String title) {
		Material craftingType = Material.CRAFTING_TABLE;
		String craftingTypeName = "§cUnknown";
		result.setAmount(recipe.getResult().getAmount());
		Inventory gui = Bukkit.createInventory(null, 5 * 9, title);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(gui.getSize() - 1 - i, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 0; i < gui.getSize() / 9; i++) {
			gui.setItem(i * 9, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(i * 9 + 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(" ").build());
		}
		for(int i = 4; i < 8; i++) {
			for(int j = 1; j < 4; j++) {
				gui.setItem(j * 9 + i, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(" ").build());
			}
		}
		gui.setItem(4, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§e<-- §aBack to Page §b" + currentPage).build());
		gui.setItem(24, result);
		gui.setItem(22, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§4Recipe Variation: §c" + variation).build());
		gui.setItem(38, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§e<-   §6Previous Variation").build());
		gui.setItem(42, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Next variation   §e->").build());
		if(AbstractMachine.isMachine(result.getItemMeta())) {
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
				gui.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
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
				gui.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
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
				gui.setItem(slot, new ItemBuilder(Material.BROWN_STAINED_GLASS_PANE).setName(" ").build());
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
				gui.setItem(slot, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName(" ").build());
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
				gui.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build());
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
				gui.setItem(slot, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").build());
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

}
