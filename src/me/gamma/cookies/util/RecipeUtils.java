
package me.gamma.cookies.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import me.gamma.cookies.object.gui.task.AdvancedMachineRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.CookingRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.CustomRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.RandomOutputRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.RandomOutputsRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.RecipeInventoryTask.ResultChoice;
import me.gamma.cookies.object.gui.task.ShapedRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.ShapelessRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.SimpleMachineRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.SmithingRecipeInventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.task.StonecuttingRecipeInventoryTask;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.MachineItem;
import me.gamma.cookies.object.recipe.CustomRecipe;
import me.gamma.cookies.object.recipe.CustomRecipeChoice;
import me.gamma.cookies.object.recipe.machine.AdvancedMachineRecipe;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputRecipe;
import me.gamma.cookies.object.recipe.machine.RandomOutputsRecipe;
import me.gamma.cookies.object.recipe.machine.SimpleMachineRecipe;



public class RecipeUtils {

	public static String getNameForRecipe(Recipe recipe) {
		if(recipe instanceof ShapedRecipe) {
			return "§6Shaped Crafting";
		} else if(recipe instanceof ShapelessRecipe) {
			return "§6Shapeless Crafting";
		} else if(recipe instanceof FurnaceRecipe) {
			return "§8Furnace";
		} else if(recipe instanceof BlastingRecipe) {
			return "§7Blasting";
		} else if(recipe instanceof SmokingRecipe) {
			return "§6Smoking";
		} else if(recipe instanceof CampfireRecipe) {
			return "§6Campfire";
		} else if(recipe instanceof StonecuttingRecipe) {
			return "§7Stonecutting";
		} else if(recipe instanceof SmithingRecipe) {
			return "§8Smithing";
		} else if(recipe instanceof CustomRecipe custom) {
			return custom.getType().getName();
		} else {
			return null;
		}
	}


	public static Material getIconMaterialForRecipe(Recipe recipe) {
		if(recipe instanceof ShapedRecipe) {
			return Material.CRAFTING_TABLE;
		} else if(recipe instanceof ShapelessRecipe) {
			return Material.CRAFTING_TABLE;
		} else if(recipe instanceof FurnaceRecipe) {
			return Material.FURNACE;
		} else if(recipe instanceof BlastingRecipe) {
			return Material.BLAST_FURNACE;
		} else if(recipe instanceof SmokingRecipe) {
			return Material.SMOKER;
		} else if(recipe instanceof CampfireRecipe) {
			return Material.CAMPFIRE;
		} else if(recipe instanceof StonecuttingRecipe) {
			return Material.STONECUTTER;
		} else if(recipe instanceof SmithingRecipe) {
			return Material.SMITHING_TABLE;
		} else if(recipe instanceof CustomRecipe custom) {
			return custom.getType().getIcon();
		} else {
			return null;
		}
	}


	public static ItemStack getIconForRecipe(Recipe recipe) {
		if(recipe instanceof MachineRecipe machine)
			return new MachineItem(machine.getMachine()).get();

		Material material = getIconMaterialForRecipe(recipe);
		if(material == null)
			return null;

		return new ItemBuilder(material).setName(getNameForRecipe(recipe)).build();
	}


	public static void initializeRecipeInventory(Inventory inventory) {
		InventoryUtils.fillLeftRight(inventory, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(inventory, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler1 = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemStack filler2 = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemStack filler3 = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i = 1; i < inventory.getSize() / 9 - 1; i++) {
			for(int j = 1; j < 4; j++)
				inventory.setItem(i * 9 + j, filler1);
			inventory.setItem(i * 9 + 4, filler3);
			for(int j = 5; j < 8; j++)
				inventory.setItem(i * 9 + j, filler2);
		}
	}


	public static InventoryTask getInventoryTask(Inventory inventory, ResultChoice resultchoice, Recipe recipe) {
		if(recipe instanceof ShapedRecipe shaped) {
			return new ShapedRecipeInventoryTask(inventory, resultchoice, shaped);
		} else if(recipe instanceof ShapelessRecipe shapeless) {
			return new ShapelessRecipeInventoryTask(inventory, resultchoice, shapeless);
		} else if(recipe instanceof FurnaceRecipe furnace) {
			return new CookingRecipeInventoryTask<FurnaceRecipe>(inventory, resultchoice, furnace, Material.COAL, Material.GRAY_STAINED_GLASS_PANE);
		} else if(recipe instanceof BlastingRecipe blasting) {
			return new CookingRecipeInventoryTask<BlastingRecipe>(inventory, resultchoice, blasting, Material.CHARCOAL, Material.GRAY_STAINED_GLASS_PANE);
		} else if(recipe instanceof SmokingRecipe smoking) {
			return new CookingRecipeInventoryTask<SmokingRecipe>(inventory, resultchoice, smoking, Material.CHARCOAL, Material.BROWN_STAINED_GLASS_PANE);
		} else if(recipe instanceof CampfireRecipe campfire) {
			return new CookingRecipeInventoryTask<CampfireRecipe>(inventory, resultchoice, campfire, Material.CAMPFIRE, Material.ORANGE_STAINED_GLASS_PANE);
		} else if(recipe instanceof StonecuttingRecipe stonecutting) {
			return new StonecuttingRecipeInventoryTask(inventory, resultchoice, stonecutting);
		} else if(recipe instanceof SmithingRecipe smithing) {
			return new SmithingRecipeInventoryTask(inventory, resultchoice, smithing);
		} else if(recipe instanceof CustomRecipe custom) {
			return new CustomRecipeInventoryTask(inventory, resultchoice, custom);
		} else if(recipe instanceof SimpleMachineRecipe simple) {
			return new SimpleMachineRecipeInventoryTask(inventory, resultchoice, simple);
		} else if(recipe instanceof AdvancedMachineRecipe advanced) {
			return new AdvancedMachineRecipeInventoryTask(inventory, resultchoice, advanced);
		} else if(recipe instanceof RandomOutputRecipe random) {
			return new RandomOutputRecipeInventoryTask(inventory, resultchoice, random);
		} else if(recipe instanceof RandomOutputsRecipe randoms) {
			return new RandomOutputsRecipeInventoryTask(inventory, resultchoice, randoms);
		} else {
			return new StaticInventoryTask(inventory);
		}
	}


	public static List<ItemStack> getItemsFromChoice(RecipeChoice choice) {
		if(choice instanceof MaterialChoice c) {
			return c.getChoices().stream().map(ItemStack::new).toList();
		} else if(choice instanceof ExactChoice c) {
			return c.getChoices().stream().map(ItemStack::clone).toList();
		} else if(choice instanceof CustomRecipeChoice c) {
			return c.getChoices().stream().map(AbstractCustomItem::get).toList();
		} else {
			return List.of();
		}
	}


	public static boolean validateRecipe(ShapedRecipe recipe, ItemStack[][] matrix) {
		final int rows = matrix.length;
		final int columns = matrix[0].length;

		// calculate the height of the shape
		int height = Math.min(recipe.getShape().length, rows);
		if(height == 0)
			return false;

		// calculate the width of the shape
		int width = Math.min(recipe.getShape()[0].length(), columns);
		for(int i = 1; i < height; i++)
			if(Math.min(recipe.getShape()[i].length(), columns) != width)
				return false;

		// check possible square of the size shape.width * shape.height for the correct arrangement of the items
		for(int i = 0; i <= rows - height; i++) {
			loop: for(int j = 0; j <= columns - width; j++) {
				// check a square
				for(int x = 0; x < height; x++) {
					// check a row
					String row = recipe.getShape()[x];
					for(int y = 0; y < width; y++) {
						ItemStack stack = matrix[x + i][y + j];
						RecipeChoice choice = recipe.getChoiceMap().get(row.charAt(y));
						if(choice == null ? !ItemUtils.isEmpty(stack) : ItemUtils.isEmpty(stack) || !choice.test(stack))
							continue loop;
					}

					// ensure that the other items in this row but not in the square are null
					for(int y = 0; y < j; y++)
						if(!ItemUtils.isEmpty(matrix[x + i][y]))
							continue loop;

					for(int y = j + width; y < 3; y++)
						if(!ItemUtils.isEmpty(matrix[x + i][y]))
							continue loop;
				}

				// ensure that other items in all columns but not in the square are null
				for(int x = 0; x < i; x++)
					for(int y = 0; y < 3; y++)
						if(!ItemUtils.isEmpty(matrix[x][y]))
							continue loop;

				for(int x = i + height; x < 3; x++)
					for(int y = 0; y < 3; y++)
						if(!ItemUtils.isEmpty(matrix[x][y]))
							continue loop;

				return true;
			}
		}
		return false;
	}


	public static boolean validateRecipe(ShapelessRecipe recipe, ItemStack[][] matrix) {
		List<ItemStack> items = Arrays.stream(matrix).flatMap(Arrays::stream).filter(i -> !ItemUtils.isEmpty(i)).toList();
		List<RecipeChoice> choices = recipe.getChoiceList();

		if(items.size() != choices.size())
			return false;

		return matchShapeless(items, choices);
	}


	public static boolean matchExactly(CustomRecipe recipe, ItemStack[][] matrix) {
		final String[] shape = recipe.getShape();
		for(int i = 0; i < recipe.getRows(); i++) {
			if(i < shape.length) {
				String row = shape[i];
				for(int j = 0; j < recipe.getColumns(); j++) {
					if(j < row.length()) {
						if(!recipe.getIngredientMap().get(row.charAt(j)).test(matrix[i][j])) {
							return false;
						}
					} else {
						if(matrix[i][j] != null) {
							return false;
						}
					}
				}
			} else {
				for(int j = 0; j < recipe.getColumns(); j++) {
					if(matrix[i][j] != null) {
						return false;
					}
				}
			}
		}
		return true;
	}


	public static boolean matchShaped(CustomRecipe recipe, ItemStack[][] matrix) {
		final int rows = recipe.getRows();
		final int columns = recipe.getColumns();

		// calculate the height of the shape
		int height = Math.min(recipe.getShape().length, rows);
		if(height == 0)
			return false;

		// calculate the width of the shape
		int width = Math.min(recipe.getShape()[0].length(), columns);
		for(int i = 1; i < height; i++)
			if(Math.min(recipe.getShape()[i].length(), columns) != width)
				return false;

		// check possible square of the size shape.width * shape.height for the correct arrangement of the items
		for(int i = 0; i <= rows - height; i++) {
			for(int j = 0; j <= columns - width; j++) {
				// check a square
				boolean b = true;

				for(int x = 0; x < height; x++) {
					// check a row
					String row = recipe.getShape()[x];
					for(int y = 0; y < width; y++) {
						ItemStack stack = matrix[x + i][y + j];
						RecipeChoice choice = recipe.getIngredientMap().get(row.charAt(y));
						if(choice == null ? !ItemUtils.isEmpty(stack) : ItemUtils.isEmpty(stack) || !choice.test(stack)) {
							b = false;
							break;
						}
					}
					if(!b)
						break;

					// ensure that the other items in this row but not in the square are null
					for(int y = 0; y < j; y++) {
						if(!ItemUtils.isEmpty(matrix[x + i][y])) {
							b = false;
							break;
						}
					}
					if(!b)
						break;

					for(int y = j + width; y < 3; y++) {
						if(!ItemUtils.isEmpty(matrix[x + i][y])) {
							b = false;
							break;
						}
					}
					if(!b)
						break;
				}
				if(!b)
					continue;

				// ensure that other items in all columns but not in the square are null
				for(int x = 0; x < i; x++) {
					for(int y = 0; y < 3; y++) {
						if(!ItemUtils.isEmpty(matrix[x][y])) {
							b = false;
							break;
						}
					}
				}
				if(!b)
					continue;

				for(int x = i + height; x < 3; x++) {
					for(int y = 0; y < 3; y++) {
						if(!ItemUtils.isEmpty(matrix[x][y])) {
							b = false;
							break;
						}
					}
				}
				if(b)
					return true;
			}
		}
		return false;
	}


	public static boolean matchShapeless(CustomRecipe recipe, ItemStack[][] matrix) {
		List<ItemStack> items = Arrays.stream(matrix).flatMap(Arrays::stream).filter(Predicate.not(ItemUtils::isEmpty)).toList();
		List<RecipeChoice> choices = new ArrayList<>();
		for(String line : recipe.getShape()) {
			for(char c : line.toCharArray()) {
				RecipeChoice choice = recipe.getIngredientMap().get(c);
				if(choice != null)
					choices.add(choice);
			}
		}

		if(items.size() != choices.size())
			return false;

		return matchShapeless(items, choices);
	}


	private static boolean matchShapeless(List<ItemStack> items, List<RecipeChoice> choices) {
		if(items.isEmpty())
			return true;

		RecipeChoice choice = choices.get(0);
		List<RecipeChoice> subchoices = choices.subList(1, choices.size());

		for(int i = 0; i < items.size(); i++) {
			List<ItemStack> clone = new ArrayList<>(items);
			ItemStack stack = clone.remove(i);
			if(!choice.test(stack))
				continue;

			if(matchShapeless(clone, subchoices))
				return true;
		}

		return false;
	}

}
