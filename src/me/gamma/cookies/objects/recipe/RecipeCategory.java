
package me.gamma.cookies.objects.recipe;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class RecipeCategory {

	private static final List<RecipeCategory> categories = new ArrayList<>();

	public static final RecipeCategory MISCELLANEOUS = register("�6Miscellaneous", new ItemStack(Material.STICK));
	public static final RecipeCategory WEAPONS = register("�bWeapons", new ItemStack(Material.DIAMOND_SWORD));
	public static final RecipeCategory TOOLS = register("�aTools", new ItemStack(Material.IRON_PICKAXE));
	public static final RecipeCategory ARMOR = register("�9Armor", new ItemStack(Material.NETHERITE_CHESTPLATE));
	public static final RecipeCategory RESOURCES = register("�7Resources", new ItemStack(Material.PAPER));
	public static final RecipeCategory REDSTONE = register("�cRedstone", new ItemStack(Material.REDSTONE));
	public static final RecipeCategory ELECTRIC_COMPONENTS = register("�cElectric Components", CustomBlockSetup.MOTOR.createDefaultItemStack());
	public static final RecipeCategory MACHINES = register("�eMachines", CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
	public static final RecipeCategory STORAGE = register("�cStorage", new ItemStack(Material.CHEST));
	public static final RecipeCategory MAGIC = register("�5Magic", new ItemStack(Material.DRAGON_BREATH));
	public static final RecipeCategory PLANTS = register("�2Plants", new ItemStack(Material.OAK_SAPLING));
	public static final RecipeCategory KITCHEN_INGREDIENTS = register("�eKitchen Ingredients", new ItemStack(Material.APPLE));
	public static final RecipeCategory FOOD = register("�2Food", new ItemStack(Material.COOKIE));
	public static final RecipeCategory DRINKS = register("�cDrinks", new ItemStack(Material.POTION));
	public static final RecipeCategory FUN = register("�bFun", CustomBlockSetup.RAINBOW_CUBE.createDefaultItemStack());
	public static final RecipeCategory EASTERN = register(Utilities.colorize("Eastern", "9baecd".toCharArray(), 1), new ItemStack(Material.EGG), Utilities::isEaster);
	public static final RecipeCategory HALLOWEEN = register(Utilities.colorize("Halloween", "6e".toCharArray(), 1), new ItemStack(Material.JACK_O_LANTERN), Utilities::isHalloween);
	public static final RecipeCategory CHRISTMAS = register(Utilities.colorize("Christmas", "2cf".toCharArray(), 1), new ItemStack(Material.SPRUCE_SAPLING), Utilities::isChristmas);

	private String name;
	private ItemStack icon;
	private Supplier<Boolean> shouldShow = () -> true;
	private List<Recipe> recipes;

	private RecipeCategory(String name, ItemStack icon) {
		this.name = name;
		this.icon = new ItemBuilder(icon).setName(name).build();
		this.recipes = new ArrayList<>();
	}


	private RecipeCategory(String name, ItemStack icon, Supplier<Boolean> shouldShow) {
		this.name = name;
		this.icon = new ItemBuilder(icon).setName(name).build();
		this.shouldShow = shouldShow;
		this.recipes = new ArrayList<>();
	}


	public boolean registerRecipe(Recipe recipe) {
		for(Recipe registered : this.recipes)
			if(CookieRecipe.sameIngredient(registered.getResult(), recipe.getResult()))
				return false;
		this.recipes.add(recipe);
		return true;
	}


	public boolean show() {
		return this.shouldShow.get();
	}


	public String getName() {
		return name;
	}


	public ItemStack getIcon() {
		return icon;
	}


	public List<Recipe> getRecipes() {
		return recipes;
	}


	public static RecipeCategory register(String name, ItemStack icon) {
		return register(name, icon, null);
	}


	public static RecipeCategory register(String name, ItemStack icon, Supplier<Boolean> shouldShow) {
		if(icon == null)
			icon = new ItemStack(Material.STONE);
		RecipeCategory category;
		if(shouldShow == null)
			category = new RecipeCategory(name, icon);
		else
			category = new RecipeCategory(name, icon, shouldShow);
		categories.add(category);
		return category;
	}


	public static RecipeCategory getCategoryFromIconStack(ItemStack stack) {
		for(RecipeCategory category : categories) {
			if(stack != null && stack.getItemMeta() != null && stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().equals(category.getName())) {
				return category;
			}
		}
		return null;
	}
	
	
	public static RecipeCategory getCategoryFromResult(ItemStack result) {
		for(RecipeCategory category : categories) {
			for(Recipe recipe : category.getRecipes()) {
				if(CookieRecipe.sameIngredient(recipe.getResult(), result)) {
					return category;
				}
			}
		}
		return null;
	}


	public static List<RecipeCategory> getCategoriesWithEntries(boolean ignoreShouldShow) {
		return categories.stream().filter(category -> category.getRecipes().size() > 0).filter(category -> ignoreShouldShow || category.show()).collect(Collectors.toList());
	}

}
