
package me.gamma.cookies.objects.recipe;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemBuilder;



public class CustomRecipe implements CookieRecipe {

	private ItemStack result;
	private String[] shape;
	private Map<Character, ItemStack> ingredientMap;
	private RecipeCategory category;
	private RecipeType type;

	public CustomRecipe(ItemStack result, RecipeCategory category, RecipeType type) {
		this(result, 1, category, type);
	}


	public CustomRecipe(ItemStack result, int amount, RecipeCategory category, RecipeType type) {
		this.result = new ItemBuilder(result).setAmount(amount).build();
		this.ingredientMap = new HashMap<>();
		this.category = category;
		this.type = type;
		if(category != null)
			category.registerRecipe(this);
	}


	@Override
	public ItemStack getResult() {
		return this.result;
	}


	@Override
	public RecipeType getType() {
		return this.type;
	}


	public RecipeCategory getCategory() {
		return category;
	}


	public CustomRecipe setShape(String... shape) {
		this.shape = shape;
		return this;
	}


	public CustomRecipe setIngredient(char key, Material value) {
		this.setIngredient(key, new ItemStack(value));
		return this;
	}


	public CustomRecipe setIngredient(char key, ItemStack value) {
		value = value.clone();
		value.setAmount(1);
		this.ingredientMap.put(key, value);
		return this;
	}


	public String[] getShape() {
		return this.shape.clone();
	}


	public Map<Character, ItemStack> getIngredientMap() {
		return ingredientMap;
	}


	public boolean matches(ItemStack[][] items) {
		return this.matchShape(items);
	}


	public int getColumns() {
		return this.type.getWidth();
	}


	public int getRows() {
		return this.type.getHeight();
	}


	protected boolean matchExactly(ItemStack[][] items) {
		for(int i = 0; i < getRows(); i++) {
			if(i < this.shape.length) {
				String row = this.shape[i];
				for(int j = 0; j < getColumns(); j++) {
					if(j < row.length()) {
						if(!CookieRecipe.sameIngredient(items[i][j], this.ingredientMap.get(row.charAt(j)))) {
							return false;
						}
					} else {
						if(items[i][j] != null) {
							return false;
						}
					}
				}
			} else {
				for(int j = 0; j < this.getColumns(); j++) {
					if(items[i][j] != null) {
						return false;
					}
				}
			}
		}
		return true;
	}


	protected boolean matchIngredients(ItemStack[][] items) {
		List<ItemStack> list1 = new ArrayList<>();
		for(int i = 0; i < items.length; i++) {
			ItemStack[] a = items[i];
			for(int j = 0; j < a.length; j++) {
				ItemStack stack = a[j];
				if(stack != null)
					list1.add(stack);
			}
		}
		List<ItemStack> list2 = new ArrayList<>();
		for(int i = 0; i < this.shape.length; i++) {
			String row = this.shape[i];
			for(int j = 0; j < row.length(); j++) {
				ItemStack stack = this.ingredientMap.get(row.charAt(j));
				if(stack != null)
					list2.add(stack);
			}
		}
		list1.sort((stack1, stack2) -> stack1.getType().name().compareTo(stack2.getType().name()));
		list2.sort((stack1, stack2) -> stack1.getType().name().compareTo(stack2.getType().name()));
		return list1.equals(list2);
	}


	protected boolean matchShape(ItemStack[][] items) {
		final int rows = this.getRows();
		final int columns = this.getColumns();
		int height = Math.min(this.shape.length, rows);
		if(height == 0) {
			return false;
		}
		int width = Math.min(this.shape[0].length(), columns);
		for(int i = 1; i < height; i++)
			if(Math.min(this.shape[i].length(), columns) != width)
				return false;
		for(int i = 0; i <= rows - height; i++) {
			for(int j = 0; j <= columns - width; j++) {
				boolean b = true;
				for(int x = i; x < i + height; x++) {
					String row = this.shape[x - i];
					for(int y = j; y < j + width; y++) {
						if(!CookieRecipe.sameIngredient(items[x][y], this.ingredientMap.get(row.charAt(y - j)))) {
							b = false;
							break;
						}
					}
					if(!b)
						break;
					for(int y = 0; y < j; y++) {
						if(items[x][y] != null) {
							b = false;
							break;
						}
					}
					if(!b)
						break;
					for(int y = j + width; y < 3; y++) {
						if(items[x][y] != null) {
							b = false;
							break;
						}
					}
					if(!b)
						break;
				}
				if(!b)
					continue;
				for(int x = 0; x < i; x++) {
					for(int y = 0; y < 3; y++) {
						if(items[x][y] != null) {
							b = false;
							break;
						}
					}
				}
				if(!b)
					continue;
				for(int x = i + height; x < 3; x++) {
					for(int y = 0; y < 3; y++) {
						if(items[x][y] != null) {
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

}
