
package me.gamma.cookies.object.recipe;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class RecipeCategory {

	public static final List<RecipeCategory> categories = new ArrayList<>();

	public static final RecipeCategory MISCELLANEOUS = register("§6Miscellaneous", Items.PORTABLE_CRAFTING_TABLE);
	public static final RecipeCategory TOOLS = register("§aTools", Material.IRON_PICKAXE);
	public static final RecipeCategory WEAPONS = register("§bWeapons", Material.DIAMOND_SWORD);
	public static final RecipeCategory ARMOR = register("§9Armor", Material.NETHERITE_CHESTPLATE);
	public static final RecipeCategory RESOURCES = register("§7Resources", Items.CARBON);
	public static final RecipeCategory BAGS = register("§6Bags", Items.POUCH);
	public static final RecipeCategory REDSTONE = register("§cRedstone", Material.REDSTONE);
	public static final RecipeCategory ELECTRIC_COMPONENTS = register("§cElectric Components", Items.MOTOR);
	public static final RecipeCategory TECHNICAL_COMPONENTS = register("§9Technical Components", Items.BASIC_MACHINE_CASING);
	public static final RecipeCategory MACHINES = register("§eMachines", Items.COBBLESTONE_GENERATOR);
	public static final RecipeCategory GENERATORS = register("§eGenerators", Items.THERMO_GENERATOR);
	public static final RecipeCategory STORAGE = register("§cStorage", Material.CHEST);
	public static final RecipeCategory FLUIDS = register("§bFluids", Material.WATER_BUCKET);
	public static final RecipeCategory ENERGY = register("§eEnergy", Material.REDSTONE_TORCH);
	public static final RecipeCategory MAGIC = register("§5Magic", Items.MAGIC_METAL);
	public static final RecipeCategory FUN = register("§bFun", Material.DIAMOND);
	public static final RecipeCategory PLUSHIES = register("§6Plushies", Items.GRIAN_PLUSHIE);

	private String name;
	private ItemStack icon;
	private List<ItemStack> items;

	private RecipeCategory(String name, ItemStack icon) {
		this.name = name;
		this.icon = icon;
		this.items = new ArrayList<>();
	}


	public boolean registerItem(ItemStack item) {
		if(this.items.contains(item))
			return false;

		this.items.add(item);
		return true;
	}


	public boolean registerItem(IItemSupplier item) {
		return this.registerItem(item.get());
	}


	public String getName() {
		return this.name;
	}


	public ItemStack getIcon() {
		return new ItemBuilder(this.icon).setName(this.name).setLore(new ArrayList<>()).build();
	}


	public List<ItemStack> getItems() {
		return this.items;
	}


	public static RecipeCategory register(String name, Material icon) {
		return register(name, new ItemStack(icon));
	}


	public static RecipeCategory register(String name, IItemSupplier icon) {
		return register(name, icon.get());
	}


	public static RecipeCategory register(String name, ItemStack icon) {
		RecipeCategory category = new RecipeCategory(name, icon);
		categories.add(category);
		return category;
	}


	public static RecipeCategory getCategoryFromIconStack(ItemStack stack) {
		if(ItemUtils.isEmpty(stack) || !stack.getItemMeta().hasDisplayName())
			return null;

		String name = stack.getItemMeta().getDisplayName();

		for(RecipeCategory category : categories) {
			if(name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}


	public static RecipeCategory getCategoryFromResult(ItemStack result) {
		for(RecipeCategory category : categories) {
			for(ItemStack item : category.items) {
				if(ItemUtils.similar(item, result)) {
					return category;
				}
			}
		}
		return null;
	}

}
