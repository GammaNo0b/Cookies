
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Items.*;
import static me.gamma.cookies.object.block.Backpack.BACKPACKS;
import static me.gamma.cookies.object.block.Backpack.uuid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

import me.gamma.cookies.object.IItemSupplier;
import me.gamma.cookies.object.block.machine.AbstractCraftingMachine;
import me.gamma.cookies.object.block.machine.AbstractMachine;
import me.gamma.cookies.object.block.network.item.StorageCrateBlock;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomBlockItem;
import me.gamma.cookies.object.recipe.CookieRecipe;
import me.gamma.cookies.object.recipe.CustomRecipe;
import me.gamma.cookies.object.recipe.CustomRecipeChoice;
import me.gamma.cookies.object.recipe.RecipeCategory;
import me.gamma.cookies.object.recipe.RecipeShape;
import me.gamma.cookies.object.recipe.RecipeType;
import me.gamma.cookies.object.recipe.machine.MachineRecipe;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.RecipeUtils;
import me.gamma.cookies.util.collection.Pair;



public class RecipeInit {

	public static final Registry<Recipe> RECIPES = new Registry<>();
	public static final Registry<CookieRecipe> COOKIE_RECIPES = new Registry<>();
	public static final Registry<MachineRecipe> MACHINE_RECIPES = new Registry<>();

	private static final HashMap<ItemStack, HashSet<Recipe>> recipeByResult = new HashMap<>();
	private static final HashMap<ItemStack, HashSet<Recipe>> recipeByIngredient = new HashMap<>();

	private static void loadBukkitRecipes() {
		Iterator<Recipe> iterator = Bukkit.recipeIterator();
		while(iterator.hasNext()) {
			final Recipe recipe = iterator.next();

			ItemStack result = recipe.getResult().clone();
			result.setAmount(1);
			HashSet<Recipe> byResult = recipeByResult.get(result);
			if(byResult == null) {
				byResult = new HashSet<>();
				recipeByResult.put(result, byResult);
			}
			byResult.add(recipe);

			List<ItemStack> ingredients;
			if(recipe instanceof CookingRecipe<?> cooking) {
				ingredients = RecipeUtils.getItemsFromChoice(cooking.getInputChoice());
			} else if(recipe instanceof ShapedRecipe shaped) {
				ingredients = shaped.getChoiceMap().values().stream().map(RecipeUtils::getItemsFromChoice).flatMap(List::stream).toList();
			} else if(recipe instanceof ShapelessRecipe shapeless) {
				ingredients = shapeless.getChoiceList().stream().map(RecipeUtils::getItemsFromChoice).flatMap(List::stream).toList();
			} else if(recipe instanceof SmithingRecipe smithing) {
				ingredients = Stream.of(smithing.getBase(), smithing.getAddition()).map(RecipeUtils::getItemsFromChoice).flatMap(List::stream).toList();
			} else if(recipe instanceof StonecuttingRecipe stonecutting) {
				ingredients = RecipeUtils.getItemsFromChoice(stonecutting.getInputChoice());
			} else {
				ingredients = List.of();
			}

			ingredients.forEach(stack -> {
				HashSet<Recipe> byIngredient = recipeByIngredient.get(stack);
				if(byIngredient == null) {
					byIngredient = new HashSet<>();
					recipeByIngredient.put(stack, byIngredient);
				}
				byIngredient.add(recipe);
			});
		}
	}


	private static void registerRecipes() {

	}


	private static void registerRecipeCategories() {
		RecipeCategory c = RecipeCategory.MISCELLANEOUS;
		c.registerItem(CUSTOM_CRAFTING_TABLE_BLUEPRINT);
		c.registerItem(VANILLA_RECIPE_BOOK);
		c.registerItem(COOKIE_COOK_BOOK);
		c.registerItem(MULTI_BLOCK_BOOK);
		c.registerItem(PORTABLE_CRAFTING_TABLE);
		c.registerItem(PORTABLE_CUSTOM_CRAFTING_TABLE);
		c.registerItem(PORTABLE_ENGINEERING_STATION);
		c.registerItem(PORTABLE_MAGIC_ALTAR);
		c.registerItem(PORTABLE_KITCHEN);
		c.registerItem(PORTABLE_NETHER_PORTAL);
		c.registerItem(PORTABLE_END_PORTAL);
		c.registerItem(PORTABLE_ENDER_CHEST);

		c = RecipeCategory.TOOLS;
		c.registerItem(VEIN_MINER_PICKAXE);
		c.registerItem(LUMBER_AXE);
		c.registerItem(FARMER_SCYTHE);
		c.registerItem(SLIME_SLING);
		c.registerItem(PLAYER_TRACKER);
		c.registerItem(MEASURING_TAPE);
		c.registerItem(POUCH);
		c.registerItem(ANGEL_BLOCK);

		c = RecipeCategory.WEAPONS;
		c.registerItem(KNOCKBACK_STICK);
		c.registerItem(NOOB_SWORD);
		c.registerItem(LIGHTNING_BOW);
		c.registerItem(AIRGUN);
		c.registerItem(ROCKET_LAUNCHER);

		c = RecipeCategory.ARMOR;
		c.registerItem(GLOW_HAT);
		c.registerItem(SCUBA_HELMET);
		c.registerItem(INVISIBILITY_HAT);
		c.registerItem(CACTUS_SHIRT);
		c.registerItem(TURTLE_SHELL);
		c.registerItem(BERRY_PANTS);
		c.registerItem(LUCKY_LEGGINGS_1);
		c.registerItem(LUCKY_LEGGINGS_2);
		c.registerItem(LUCKY_LEGGINGS_3);
		c.registerItem(FARMER_BOOTS);
		c.registerItem(RABBIT_BOOTS);
		c.registerItem(ANGEL_WINGS);
		c.registerItem(RAINBOW_HELMET);
		c.registerItem(RAINBOW_CHESTPLATE);
		c.registerItem(RAINBOW_LEGGINGS);
		c.registerItem(RAINBOW_BOOTS);
		c.registerItem(COLORED_HELMET);
		c.registerItem(COLORED_CHESTPLATE);
		c.registerItem(COLORED_LEGGINGS);
		c.registerItem(COLORED_BOOTS);
		c.registerItem(HASTE_HELMET);
		c.registerItem(HASTE_CHESTPLATE);
		c.registerItem(HASTE_LEGGINGS);
		c.registerItem(HASTE_BOOTS);

		c = RecipeCategory.RESOURCES;
		c.registerItem(STONE_PEBBLE);
		c.registerItem(ANDESITE_PEBBLE);
		c.registerItem(DIORITE_PEBBLE);
		c.registerItem(GRANITE_PEBBLE);
		c.registerItem(CALCITE_PEBBLE);
		c.registerItem(TUFF_PEBBLE);
		c.registerItem(DEEPSLATE_PEBBLE);
		c.registerItem(COMPRESSED_COBBLESTONE);
		c.registerItem(DOUBLE_COMPRESSED_COBBLESTONE);
		c.registerItem(TRIPLE_COMPRESSED_COBBLESTONE);
		c.registerItem(QUADRUPLE_COMPRESSED_COBBLESTONE);
		c.registerItem(QUINTUPLE_COMPRESSED_COBBLESTONE);
		c.registerItem(SEXTUPLE_COMPRESSED_COBBLESTONE);
		c.registerItem(SEPTUPLE_COMPRESSED_COBBLESTONE);
		c.registerItem(OCTUPLE_COMPRESSED_COBBLESTONE);
		c.registerItem(DUST);
		c.registerItem(RED_DUST);
		c.registerItem(BLACK_SAND);
		c.registerItem(CRUSHED_NETHERRACK);
		c.registerItem(CRUSHED_END_STONE);
		c.registerItem(CRUSHED_OBSIDIAN);
		c.registerItem(CRUSHED_CRYING_OBSIDIAN);
		c.registerItem(SILICON);
		c.registerItem(STEEL_COMPOUND);
		c.registerItem(PULVERIZED_COAL);
		c.registerItem(SULFUR);
		c.registerItem(QUARTZ_DUST);
		c.registerItem(IRON_DUST);
		c.registerItem(GOLD_DUST);
		c.registerItem(COPPER_DUST);
		c.registerItem(ALUMINUM_DUST);
		c.registerItem(TIN_DUST);
		c.registerItem(MAGNESIUM_DUST);
		c.registerItem(NICKEL_DUST);
		c.registerItem(LEAD_DUST);
		c.registerItem(SILVER_DUST);
		c.registerItem(LITHIUM_DUST);
		c.registerItem(STEEL_DUST);
		c.registerItem(SOUL_DUST);
		c.registerItem(ALUMINUM_INGOT);
		c.registerItem(TIN_INGOT);
		c.registerItem(MAGNESIUM_INGOT);
		c.registerItem(NICKEL_INGOT);
		c.registerItem(LEAD_INGOT);
		c.registerItem(SILVER_INGOT);
		c.registerItem(STEEL_INGOT);
		c.registerItem(BRONZE_INGOT);
		c.registerItem(ELECTRUM_INGOT);
		c.registerItem(INVAR_INGOT);
		c.registerItem(ALUMINUM_STEEL_INGOT);
		c.registerItem(HARDENED_METAL);
		c.registerItem(REDSTONE_ALLOY);
		c.registerItem(HARDENED_ALLOY);
		c.registerItem(NETHER_STEEL);
		c.registerItem(BLAZING_ALLOY);
		c.registerItem(ENDER_STEEL);
		c.registerItem(ENDERIUM);
		c.registerItem(ENERGETIC_ALLOY);
		c.registerItem(REDSTONIUM);
		c.registerItem(CARBON);
		c.registerItem(COMPRESSED_CARBON);
		c.registerItem(CARBON_CHUNK);
		c.registerItem(CARBONADO);
		c.registerItem(STICKY_BALL);
		c.registerItem(RUBBER);
		c.registerItem(RUBBER_SHEETS);
		c.registerItem(PLASTIC_SHEET);
		c.registerItem(GILDED_PAPER);
		c.registerItem(RAINBOW_DUST);

		c = RecipeCategory.REDSTONE;
		c.registerItem(REDSTONE_FREQUENCY_GADGET);
		c.registerItem(WIRELESS_REDSTONE_TRANSMITTER);
		c.registerItem(WIRELESS_REDSTONE_RECEIVER);
		c.registerItem(REDSTONE_OR_GATE);
		c.registerItem(REDSTONE_AND_GATE);
		c.registerItem(REDSTONE_XOR_GATE);

		c = RecipeCategory.ELECTRIC_COMPONENTS;
		c.registerItem(COPPER_WIRE);
		c.registerItem(COPPER_COIL);
		c.registerItem(INSULATED_COPPER_WIRE);
		c.registerItem(ELECTROMAGNET);
		c.registerItem(MOTOR);
		c.registerItem(LASER);
		c.registerItem(ACCUMULATOR);
		c.registerItem(ENERGY_MEASURE_GADGET);
		c.registerItem(RESISTOR_1);
		c.registerItem(RESISTOR_2);
		c.registerItem(RESISTOR_3);
		c.registerItem(RESISTOR_4);
		c.registerItem(RESISTOR_5);
		c.registerItem(CAPACITOR_1);
		c.registerItem(CAPACITOR_2);
		c.registerItem(CAPACITOR_3);
		c.registerItem(CAPACITOR_4);
		c.registerItem(CAPACITOR_5);
		c.registerItem(ELECTRICAL_CIRCUIT_1);
		c.registerItem(ELECTRICAL_CIRCUIT_2);
		c.registerItem(ELECTRICAL_CIRCUIT_3);
		c.registerItem(ELECTRICAL_CIRCUIT_4);
		c.registerItem(ELECTRICAL_CIRCUIT_5);
		c.registerItem(PHOTOVOLTAIC_CELL_1);
		c.registerItem(PHOTOVOLTAIC_CELL_2);
		c.registerItem(PHOTOVOLTAIC_CELL_3);
		c.registerItem(PHOTOVOLTAIC_CELL_4);

		c = RecipeCategory.TECHNICAL_COMPONENTS;
		c.registerItem(CIRCUIT_BOARD);
		c.registerItem(BASIC_MACHINE_CASING);
		c.registerItem(ADVANCED_MACHINE_CASING);
		c.registerItem(IMPROVED_MACHINE_CASING);
		c.registerItem(PERFECTED_MACHINE_CASING);
		c.registerItem(LIGHT_CONVEYOR_BELT);
		c.registerItem(MEDIUM_CONVEYOR_BELT);
		c.registerItem(HEAVY_CONVEYOR_BELT);
		c.registerItem(UPGRADE_BASE);
		c.registerItem(UPGRADE_SPEED);
		c.registerItem(UPGRADE_EFFICIENCY);
		c.registerItem(UPGRADE_ENERGY_STORAGE);
		c.registerItem(UPGRADE_RANGE);
		c.registerItem(UPGRADE_LUCK);

		c = RecipeCategory.MACHINES;
		c.registerItem(COBBLESTONE_GENERATOR);
		c.registerItem(STONE_GENERATOR);
		c.registerItem(DRIPSTONE_GENERATOR);
		c.registerItem(BASALT_GENERATOR);
		c.registerItem(LAVA_GENERATOR);
		c.registerItem(BASIC_LAVA_GENERATOR);
		c.registerItem(ADVANCED_LAVA_GENERATOR);
		c.registerItem(IMPROVED_LAVA_GENERATOR);
		c.registerItem(PERFECTED_LAVA_GENERATOR);
		c.registerItem(OBSIDIAN_GENERATOR);
		c.registerItem(ITEM_ABSORBER);
		c.registerItem(EXPERIENCE_ABSORBER);
		c.registerItem(BASIC_FARMER);
		c.registerItem(ADVANCED_FARMER);
		c.registerItem(IMPROVED_FARMER);
		c.registerItem(PERFECTED_FARMER);
		c.registerItem(MOB_GRINDER);
		c.registerItem(BLOCK_BREAKER);
		c.registerItem(QUARRY);
		c.registerItem(BASIC_VOID_ORE_MINER);
		c.registerItem(ADVANCED_VOID_ORE_MINER);
		c.registerItem(IMPROVED_VOID_ORE_MINER);
		c.registerItem(PERFECTED_VOID_ORE_MINER);
		c.registerItem(DYE_PRESS);
		c.registerItem(DYE_MIXER);
		c.registerItem(ENCHANTER);
		c.registerItem(DISENCHANTER);
		c.registerItem(ENCHANTMENT_COMBINER);
		c.registerItem(COMPUTER);
		c.registerItem(CHUNK_LOADER);
		c.registerItem(HYPER_FURNACE_1);
		c.registerItem(HYPER_FURNACE_2);
		c.registerItem(HYPER_FURNACE_3);
		c.registerItem(HYPER_FURNACE_4);
		c.registerItem(HYPER_FURNACE_5);
		c.registerItem(HYPER_FURNACE_6);
		c.registerItem(HYPER_FURNACE_7);
		c.registerItem(HYPER_FURNACE_8);
		c.registerItem(HYPER_FURNACE_9);
		c.registerItem(HYPER_FURNACE_10);
		c.registerItem(HYPER_FURNACE_11);
		c.registerItem(HYPER_FURNACE_12);
		c.registerItem(BASIC_CRUSHER);
		c.registerItem(ADVANCED_CRUSHER);
		c.registerItem(IMPROVED_CRUSHER);
		c.registerItem(PERFECTED_CRUSHER);
		c.registerItem(BASIC_MINERAL_EXTRACTOR);
		c.registerItem(ADVANCED_MINERAL_EXTRACTOR);
		c.registerItem(IMPROVED_MINERAL_EXTRACTOR);
		c.registerItem(PERFECTED_MINERAL_EXTRACTOR);
		c.registerItem(BASIC_AERIAL_EXTRACTOR);
		c.registerItem(ADVANCED_AERIAL_EXTRACTOR);
		c.registerItem(IMPROVED_AERIAL_EXTRACTOR);
		c.registerItem(PERFECTED_AERIAL_EXTRACTOR);
		c.registerItem(BASIC_SAWMILL);
		c.registerItem(ADVANCED_SAWMILL);
		c.registerItem(IMPROVED_SAWMILL);
		c.registerItem(PERFECTED_SAWMILL);
		c.registerItem(BASIC_SMELTERY);
		c.registerItem(ADVANCED_SMELTERY);
		c.registerItem(IMPROVED_SMELTERY);
		c.registerItem(PERFECTED_SMELTERY);
		c.registerItem(BASIC_COMPRESSOR);
		c.registerItem(ADVANCED_COMPRESSOR);
		c.registerItem(IMPROVED_COMPRESSOR);
		c.registerItem(PERFECTED_COMPRESSOR);
		c.registerItem(BASIC_BIO_PRESS);
		c.registerItem(ADVANCED_BIO_PRESS);
		c.registerItem(IMPROVED_BIO_PRESS);
		c.registerItem(PERFECTED_BIO_PRESS);
		c.registerItem(LATEX_EXTRACTOR);
		c.registerItem(BASIC_CRAFTER);
		c.registerItem(ADVANCED_CRAFTER);
		c.registerItem(IMPROVED_CRAFTER);
		c.registerItem(PERFECTED_CRAFTER);
		c.registerItem(BASIC_CRAFTING_FACTORY);
		c.registerItem(ADVANCED_CRAFTING_FACTORY);
		c.registerItem(IMPROVED_CRAFTING_FACTORY);
		c.registerItem(PERFECTED_CRAFTING_FACTORY);
		c.registerItem(BASIC_CARBON_PRESS);
		c.registerItem(ADVANCED_CARBON_PRESS);
		c.registerItem(IMPROVED_CARBON_PRESS);
		c.registerItem(PERFECTED_CARBON_PRESS);
		c.registerItem(BASIC_FREEZER);
		c.registerItem(ADVANCED_FREEZER);
		c.registerItem(IMPROVED_FREEZER);
		c.registerItem(PERFECTED_FREEZER);
		c.registerItem(HONEY_EXTRACTOR);
		c.registerItem(STAR_MAKER);
		c.registerItem(ROCKET_ASSEMBLER);

		c = RecipeCategory.GENERATORS;
		c.registerItem(FURNACE_GENERATOR);
		c.registerItem(THERMO_GENERATOR);
		c.registerItem(BASIC_SOLAR_PANEL);
		c.registerItem(ADVANCED_SOLAR_PANEL);
		c.registerItem(IMPROVED_SOLAR_PANEL);
		c.registerItem(PERFECTED_SOLAR_PANEL);
		c.registerItem(BASIC_BIO_GENERATOR);
		c.registerItem(ADVANCED_BIO_GENERATOR);
		c.registerItem(IMPROVED_BIO_GENERATOR);
		c.registerItem(PERFECTED_BIO_GENERATOR);

		c = RecipeCategory.STORAGE;
		c.registerItem(BROWN_BACKPACK);
		c.registerItem(COPPER_BACKPACK);
		c.registerItem(IRON_BACKPACK);
		c.registerItem(GOLDEN_BACKPACK);
		c.registerItem(DIAMOND_BACKPACK);
		c.registerItem(NETHERITE_BACKPACK);
		c.registerItem(OAK_STORAGE_CRATE);
		c.registerItem(SPRUCE_STORAGE_CRATE);
		c.registerItem(BIRCH_STORAGE_CRATE);
		c.registerItem(JUNGLE_STORAGE_CRATE);
		c.registerItem(ACACIA_STORAGE_CRATE);
		c.registerItem(DARK_OAK_STORAGE_CRATE);
		c.registerItem(MANGROVE_STORAGE_CRATE);
		c.registerItem(CHERRY_STORAGE_CRATE);
		c.registerItem(PALE_OAK_STORAGE_CRATE);
		c.registerItem(BAMBOO_STORAGE_CRATE);
		c.registerItem(CRIMSON_STORAGE_CRATE);
		c.registerItem(WARPED_STORAGE_CRATE);
		c.registerItem(IRON_STORAGE_CRATE);
		c.registerItem(GOLDEN_STORAGE_CRATE);
		c.registerItem(DIAMOND_STORAGE_CRATE);
		c.registerItem(EMERALD_STORAGE_CRATE);
		c.registerItem(CLOWNFISH_STORAGE_CRATE);
		c.registerItem(ENDER_CHEST);
		c.registerItem(TRASHCAN);
		c.registerItem(STORAGE_CASING);
		c.registerItem(STORAGE_CONNECTOR);
		c.registerItem(STORAGE_IMPORTER);
		c.registerItem(STORAGE_EXPORTER);
		c.registerItem(STORAGE_READER);
		c.registerItem(ENDER_ACCESSOR);
		c.registerItem(STORAGE_MONITOR);
		c.registerItem(NONSTACKABLE_FILTER);
		c.registerItem(DAMAGEABLE_FILTER);
		c.registerItem(NBT_FILTER);
		c.registerItem(ENCHANTED_FILTER);
		c.registerItem(FOOD_FILTER);
		c.registerItem(COOKIES_FILTER);

		c = RecipeCategory.FLUIDS;
		c.registerItem(BASIC_TANK);
		c.registerItem(ADVANCED_TANK);
		c.registerItem(IMPROVED_TANK);
		c.registerItem(PERFECTED_TANK);
		c.registerItem(FLUID_PUMP);
		c.registerItem(ENDER_TANK);
		c.registerItem(WASTE_BARREL);

		c = RecipeCategory.ENERGY;
		c.registerItem(BATTERY_RED);
		c.registerItem(BATTERY_YELLOW);
		c.registerItem(BATTERY_GREEN);
		c.registerItem(BATTERY_CYAN);
		c.registerItem(BATTERY_PURPLE);
		c.registerItem(BATTERY_BLACK);
		c.registerItem(LED_PURPLE);
		c.registerItem(LED_RED);
		c.registerItem(LED_ORANGE);
		c.registerItem(LED_GREEN);
		c.registerItem(LED_BLUE);
		c.registerItem(TESSERACT);

		c = RecipeCategory.MAGIC;
		c.registerItem(MAGIC_METAL);
		c.registerItem(BASIC_WAND);
		c.registerItem(MAGIC_WAND);
		c.registerItem(POWERED_WAND);
		c.registerItem(EMPOWERED_WAND);
		c.registerItem(FLOWERING_FLOWER);
		c.registerItem(FOREST_BUNDLE);
		c.registerItem(FOOD_BUNDLE);
		c.registerItem(ORGANIC_MATTER);
		c.registerItem(PEARLSTONE);
		c.registerItem(OVERWORLD_ARTEFACT);
		c.registerItem(MAGIC_ARTEFACT);
		c.registerItem(NETHER_CORE);
		c.registerItem(NETHER_CRYSTAL);
		c.registerItem(ENDER_CORE);
		c.registerItem(ENDER_CRYSTAL);
		c.registerItem(FLESH_CLUMP);
		c.registerItem(FIRE_CRYSTAL);
		c.registerItem(MONSTER_CORE);
		c.registerItem(SPAWNER_CRYSTAL);
		c.registerItem(SPAWNER_WAND);
		c.registerItem(DRAGON_EYE);
		c.registerItem(STICKY_GOO);
		c.registerItem(COMPACT_PEBBLE);
		c.registerItem(COMPACT);
		c.registerItem(MINIATURIZING_WAND);

		c = RecipeCategory.FUN;
		c.registerItem(CARD_PILE);
		c.registerItem(CARD_PILE::get32CardDeck);
		c.registerItem(CARD_PILE::get52CardDeck);

		c = RecipeCategory.PLUSHIES;
		c.registerItem(FALSE_SYMMETRY_PLUSHIE);
		c.registerItem(XISUMA_PLUSHIE);
		c.registerItem(ZEDAPH_PLUSHIE);
		c.registerItem(XB_CRAFTED_PLUSHIE);
		c.registerItem(WELSKNIGHT_PLUSHIE);
		c.registerItem(TIN_FOIL_CHEF_PLUSHIE);
		c.registerItem(MUMBO_JUMBO_PLUSHIE);
		c.registerItem(JOE_HILLS_SAYS_PLUSHIE);
		c.registerItem(HYPNOTIZD_PLUSHIE);
		c.registerItem(GRIAN_PLUSHIE);
		c.registerItem(GUINEA_PIG_GRIAN_PLUSHIE);
		c.registerItem(POULTRY_MAN_PLUSHIE);
		c.registerItem(VINTAGE_BEEF_PLUSHIE);
		c.registerItem(GOOD_TIMES_WITH_SCAR_PLUSHIE);
		c.registerItem(JELLIE_PLUSHIE);
		c.registerItem(KERALIS_PLUSHIE);
		c.registerItem(FRENCHRALIS_PLUSHIE);
		c.registerItem(I_JEVIN_PLUSHIE);
		c.registerItem(ETHOSLAB_PLUSHIE);
		c.registerItem(ISKALL85_PLUSHIE);
		c.registerItem(TANGO_TEK_PLUSHIE);
		c.registerItem(IMPULS_SV_PLUSHIE);
		c.registerItem(STRESSMONSTER101_PLUSHIE);
		c.registerItem(BDOUBLEO100_PLUSHIE);
		c.registerItem(BDOUBLEO100_SMILE_PLUSHIE);
		c.registerItem(DOCM77_PLUSHIE);
		c.registerItem(CUBFAN135_PLUSHIE);
		c.registerItem(DOCTOR_CUBFAN135_PLUSHIE);
		c.registerItem(PHARAO_CUBFAN135_PLUSHIE);
		c.registerItem(ZOMBIE_CLEO_PLUSHIE);
		c.registerItem(REN_THE_DOG_PLUSHIE);
		c.registerItem(REN_BOB_PLUSHIE);
		c.registerItem(PEARLESCENT_MOON_PLUSHIE);
		c.registerItem(GEMINI_TAY_PLUSHIE);
		c.registerItem(JOEYGRACEFFA_PLUSHIE);
		c.registerItem(SHUBBLE_YT_PLUSHIE);
		c.registerItem(SOLIDARITY_GAMING_PLUSHIE);
		c.registerItem(SMALISHBEANS_PLUSHIE);
		c.registerItem(SMAJOR1995_PLUSHIE);
		c.registerItem(PIXLRIFFS_PLUSHIE);
		c.registerItem(MYTHICAL_SAUSAGE_PLUSHIE);
		c.registerItem(LDS_SHADOWLADY_PLUSHIE);
		c.registerItem(KATHERINEELIZ_PLUSHIE);
		c.registerItem(FWHIP_PLUSHIE);
	}


	public static void init() {
		loadBukkitRecipes();

		registerRecipeCategories();

		registerRecipes();

		final Material GEM_1 = Material.REDSTONE;
		final Material GEM_2 = Material.QUARTZ;
		final Material GEM_3 = Material.EMERALD;
		final Material GEM_4 = Material.DIAMOND;
		final Material IINGOT_1 = Material.IRON_INGOT;
		final AbstractCustomItem IINGOT_2 = STEEL_INGOT;
		final AbstractCustomItem IINGOT_3 = HARDENED_METAL;
		final AbstractCustomItem IINGOT_4 = BLAZING_ALLOY;
		final AbstractCustomItem IINGOT_5 = ENDERIUM;
		final Material FINGOT_1 = Material.COPPER_INGOT;
		final AbstractCustomItem FINGOT_2 = LEAD_INGOT;
		final AbstractCustomItem FINGOT_3 = INVAR_INGOT;
		final AbstractCustomItem FINGOT_4 = NETHER_STEEL;
		// final AbstractCustomItem FINGOT_5 = ENDER_STEEL;
		final Material EINGOT_1 = Material.IRON_INGOT;
		final AbstractCustomItem EINGOT_2 = SILVER_INGOT;
		final AbstractCustomItem EINGOT_3 = ELECTRUM_INGOT;
		final AbstractCustomItem EINGOT_4 = ENERGETIC_ALLOY;
		final Material RED_1 = Material.REDSTONE;
		final AbstractCustomItem RED_2 = REDSTONE_ALLOY;
		final AbstractCustomItem RED_3 = REDSTONIUM;

		CustomRecipe recipe;
		RecipeType type;

		// Miscellaneous

		type = RecipeType.CUSTOM;

		recipe = customRecipe(CUSTOM_CRAFTING_TABLE_BLUEPRINT, type, " F ", "BPS", " C ");
		recipe.setIngredient('F', Material.FURNACE);
		recipe.setIngredient('B', Material.BRICKS);
		recipe.setIngredient('P', Material.PAPER);
		recipe.setIngredient('S', Material.COBBLESTONE);
		recipe.setIngredient('C', Material.CRAFTING_TABLE);

		recipe = customRecipe(VANILLA_RECIPE_BOOK, type, " S ", "IBN", " E ");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('I', Material.INK_SAC);
		recipe.setIngredient('N', Material.IRON_NUGGET);
		recipe.setIngredient('E', Material.EMERALD);

		recipe = shapelessRecipe(COOKIE_COOK_BOOK, type, " H ", "SGB", " C ");
		recipe.setIngredient('H', Material.HONEY_BOTTLE);
		recipe.setIngredient('S', Material.SUGAR);
		recipe.setIngredient('G', Material.BOOK);
		recipe.setIngredient('B', Material.COCOA_BEANS);
		recipe.setIngredient('C', Material.COOKIE);

		recipe = customRecipe(MULTI_BLOCK_BOOK, type, " S ", "RBH", " D ");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('R', Material.BRICKS);
		recipe.setIngredient('H', Material.BOOKSHELF);
		recipe.setIngredient('D', CUSTOM_CRAFTING_TABLE_BLUEPRINT);
		recipe.setIngredient('B', Material.BOOK);

		recipe = customRecipe(PORTABLE_CRAFTING_TABLE, type, "CWS");
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('W', Material.STRING);

		recipe = customRecipe(PORTABLE_NETHER_PORTAL, type, "OOO", "OSO", "OOO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('S', NETHER_CRYSTAL);

		recipe = customRecipe(PORTABLE_END_PORTAL, type, "OEO", "SSS");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('S', Material.END_STONE);
		recipe.setIngredient('E', ENDER_CRYSTAL);

		recipe = customRecipe(PORTABLE_ENDER_CHEST, type, "OOO", "OEO", "OOO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('E', ENDER_CRYSTAL);

		recipe = customRecipe(MEASURING_TAPE, type, "IPY");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('Y', Material.IRON_NUGGET);

		recipe = customRecipe(POUCH, type, "HLH", "LCL", "HLH");
		recipe.setIngredient('H', Material.RABBIT_HIDE);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('C', Material.CHEST);

		recipe = customRecipe(ANGEL_BLOCK, type, "GFG", "FOF", "GFG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('F', Material.FEATHER);
		recipe.setIngredient('O', Material.OBSIDIAN);

		// Tools

		recipe = customRecipe(VEIN_MINER_PICKAXE, type, "RCR", " P ", " S ");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('C', Material.COMPASS);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('S', Material.STICK);

		recipe = customRecipe(LUMBER_AXE, type, "DD", "DW", " W");
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('W', Material.SPRUCE_LOG);

		recipe = customRecipe(FARMER_SCYTHE, type, " II", "IS ", " S ");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('S', Material.STICK);

		recipe = customRecipe(SLIME_SLING, type, "SBS", "O O", " O ");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('B', Material.SLIME_BLOCK);
		recipe.setIngredient('O', Material.SLIME_BALL);

		recipe = customRecipe(PLAYER_TRACKER, type, "   ", " C ", "   ");
		recipe.setIngredient('C', Material.COMPASS);

		// Weapons

		recipe = customRecipe(KNOCKBACK_STICK, type, "W", "L", "S");
		recipe.setIngredient('W', Material.OAK_WOOD);
		recipe.setIngredient('L', Material.OAK_LOG);
		recipe.setIngredient('S', Material.STICK);

		recipe = customRecipe(NOOB_SWORD, type, "F", "C", "S");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('C', Material.CLAY_BALL);
		recipe.setIngredient('F', Material.FLINT);

		recipe = customRecipe(LIGHTNING_BOW, type, "SL ", "P B", "SL ");
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('P', Material.BLAZE_POWDER);
		recipe.setIngredient('L', Material.END_ROD);

		// Armor

		recipe = customRecipe(GLOW_HAT, type, "LCL", "G G");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('C', Material.PRISMARINE_CRYSTALS);
		recipe.setIngredient('G', Material.GLOWSTONE_DUST);

		recipe = customRecipe(SCUBA_HELMET, type, "OGO", "L L");
		recipe.setIngredient('O', Material.ORANGE_DYE);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('L', Material.LEATHER);

		recipe = customRecipe(INVISIBILITY_HAT, type, "BNB", "P P");
		recipe.setIngredient('P', Material.GLASS_PANE);
		recipe.setIngredient('B', Material.GLASS);
		recipe.setIngredient('N', Material.NETHER_STAR);

		recipe = customRecipe(CACTUS_SHIRT, type, "G G", "LCL", "LGL");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('C', Material.CACTUS);

		recipe = customRecipe(TURTLE_SHELL, type, "S S", "SSS", "SSS");
		recipe.setIngredient('S', Material.TURTLE_SCUTE);

		recipe = customRecipe(BERRY_PANTS, type, "LLL", "S S", "B B");
		recipe.setIngredient('B', Material.SWEET_BERRIES);
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('L', Material.LEATHER);

		recipe = customRecipe(LUCKY_LEGGINGS_1, type, "EPE", "L L", "B B");
		recipe.setIngredient('E', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('B', Material.LAPIS_BLOCK);
		recipe.setIngredient('P', Material.LEATHER_LEGGINGS);

		recipe = customRecipe(LUCKY_LEGGINGS_2, type, "EPE", "L L", "B B");
		recipe.setIngredient('E', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('B', Material.LAPIS_BLOCK);
		recipe.setIngredient('P', LUCKY_LEGGINGS_1);

		recipe = customRecipe(LUCKY_LEGGINGS_3, type, "EPE", "L L", "B B");
		recipe.setIngredient('E', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('B', Material.LAPIS_BLOCK);
		recipe.setIngredient('P', LUCKY_LEGGINGS_2);

		recipe = customRecipe(FARMER_BOOTS, type, "L L", "H H");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('H', Material.HAY_BLOCK);

		recipe = customRecipe(RABBIT_BOOTS, type, "L L", "P P");
		recipe.setIngredient('L', Material.RABBIT_HIDE);
		recipe.setIngredient('P', Material.RABBIT_FOOT);

		recipe = customRecipe(ANGEL_WINGS, type, "F F", "FEF", "DND");
		recipe.setIngredient('F', Material.FEATHER);
		recipe.setIngredient('E', Material.ELYTRA);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('N', Material.NETHER_STAR);

		recipe = customRecipe(RAINBOW_BOOTS, type, " R ", "RAR", " R ");
		recipe.setIngredient('A', Material.LEATHER_BOOTS);
		recipe.setIngredient('R', RAINBOW_DUST);

		recipe = customRecipe(RAINBOW_LEGGINGS, type, " R ", "RAR", " R ");
		recipe.setIngredient('A', Material.LEATHER_LEGGINGS);
		recipe.setIngredient('R', RAINBOW_DUST);

		recipe = customRecipe(RAINBOW_CHESTPLATE, type, " R ", "RAR", " R ");
		recipe.setIngredient('A', Material.LEATHER_CHESTPLATE);
		recipe.setIngredient('R', RAINBOW_DUST);

		recipe = customRecipe(RAINBOW_HELMET, type, " R ", "RAR", " R ");
		recipe.setIngredient('A', Material.LEATHER_HELMET);
		recipe.setIngredient('R', RAINBOW_DUST);

		recipe = customRecipe(COLORED_BOOTS, type, "PR", "BG");
		recipe.setIngredient('P', Material.LEATHER_BOOTS);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);

		recipe = customRecipe(COLORED_LEGGINGS, type, "PR", "BG");
		recipe.setIngredient('P', Material.LEATHER_LEGGINGS);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);

		recipe = customRecipe(COLORED_CHESTPLATE, type, "PR", "BG");
		recipe.setIngredient('P', Material.LEATHER_CHESTPLATE);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);

		recipe = customRecipe(COLORED_HELMET, type, "PR", "BG");
		recipe.setIngredient('P', Material.LEATHER_HELMET);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);

		recipe = customRecipe(HASTE_BOOTS, type, "GSG", "SAS", "GSG");
		recipe.setIngredient('A', Material.LEATHER_BOOTS);
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('S', Material.SUGAR);

		recipe = customRecipe(HASTE_LEGGINGS, type, "GSG", "SAS", "GSG");
		recipe.setIngredient('A', Material.LEATHER_LEGGINGS);
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('S', Material.SUGAR);

		recipe = customRecipe(HASTE_CHESTPLATE, type, "GSG", "SAS", "GSG");
		recipe.setIngredient('A', Material.LEATHER_CHESTPLATE);
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('S', Material.SUGAR);

		recipe = customRecipe(HASTE_HELMET, type, "GSG", "SAS", "GSG");
		recipe.setIngredient('A', Material.LEATHER_HELMET);
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('S', Material.SUGAR);

		// Resources

		recipe = shapelessRecipe(STICKY_BALL, 1, type, "BWC", "SG ");
		recipe.setIngredient('B', Material.WATER_BUCKET);
		recipe.setIngredient('W', Material.WHEAT);
		recipe.setIngredient('C', Material.SUGAR_CANE);
		recipe.setIngredient('S', Material.SUGAR);
		recipe.setIngredient('G', Material.GUNPOWDER);

		recipe = customRecipe(RUBBER_SHEETS, 6, type, "RR");
		recipe.setIngredient('R', RUBBER);

		recipe = customRecipe(GILDED_PAPER, 8, type, "PPP", "PGP", "PPP");
		recipe.setIngredient('P', Material.PAPER);
		recipe.setIngredient('G', Material.GOLD_INGOT);

		recipe = customRecipe(RAINBOW_DUST, type, "187", "2G6", "345");
		recipe.setIngredient('1', Material.RED_DYE);
		recipe.setIngredient('2', Material.ORANGE_DYE);
		recipe.setIngredient('3', Material.YELLOW_DYE);
		recipe.setIngredient('4', Material.LIME_DYE);
		recipe.setIngredient('5', Material.GREEN_DYE);
		recipe.setIngredient('6', Material.CYAN_DYE);
		recipe.setIngredient('7', Material.BLUE_DYE);
		recipe.setIngredient('8', Material.PURPLE_DYE);
		recipe.setIngredient('G', Material.GLOWSTONE_DUST);

		recipe = customRecipe(IItemSupplier.of(Material.COBBLESTONE), 9, type, "C");
		recipe.setIngredient('C', COMPRESSED_COBBLESTONE);

		recipe = customRecipe(COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', DOUBLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(DOUBLE_COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', TRIPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(TRIPLE_COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', QUADRUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(QUADRUPLE_COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', QUINTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(QUINTUPLE_COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', SEXTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(SEXTUPLE_COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', SEPTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(SEPTUPLE_COMPRESSED_COBBLESTONE, 9, type, "C");
		recipe.setIngredient('C', OCTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', Material.COBBLESTONE);

		recipe = customRecipe(DOUBLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', COMPRESSED_COBBLESTONE);

		recipe = customRecipe(TRIPLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', DOUBLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(QUADRUPLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', TRIPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(QUINTUPLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', QUADRUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(SEXTUPLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', QUINTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(SEPTUPLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', SEXTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(OCTUPLE_COMPRESSED_COBBLESTONE, type, "CCC", "CCC", "CCC");
		recipe.setIngredient('C', SEPTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(STEEL_COMPOUND, type, " C ", "CIC", " C ");
		recipe.setIngredient('C', Tag.ITEMS_COALS);
		recipe.setIngredient('I', Material.RAW_IRON);

		// Bags

		type = RecipeType.CUSTOM;

		// Redstone

		type = RecipeType.ENGINEER;

		recipe = customRecipe(REDSTONE_FREQUENCY_GADGET, type, " T ", "GEG", " R ");
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('R', Material.REDSTONE);

		recipe = customRecipe(WIRELESS_REDSTONE_TRANSMITTER, type, "RET", "SSS");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('S', Material.STONE);

		recipe = customRecipe(WIRELESS_REDSTONE_RECEIVER, type, "TCR", "SSS");
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('C', Material.COMPARATOR);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('S', Material.STONE);

		recipe = customRecipe(REDSTONE_OR_GATE, type, "R", "S");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);

		recipe = customRecipe(REDSTONE_AND_GATE, type, " T ", "TRT", "SSS");
		recipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);

		recipe = customRecipe(REDSTONE_XOR_GATE, type, "T  ", "RTR", "SSS");
		recipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);

		// Electric Components

		recipe = customRecipe(COPPER_WIRE, 8, type, "CCC");
		recipe.setIngredient('C', Material.COPPER_INGOT);

		recipe = customRecipe(COPPER_COIL, type, " W ", "WSW", " W ");
		recipe.setIngredient('S', Material.STICK);
		recipe.setIngredient('W', COPPER_WIRE);

		recipe = customRecipe(INSULATED_COPPER_WIRE, type, "WR");
		recipe.setIngredient('W', COPPER_COIL);
		recipe.setIngredient('R', RUBBER);

		recipe = customRecipe(INSULATED_COPPER_WIRE, type, "WSP");
		recipe.setIngredient('W', COPPER_COIL);
		recipe.setIngredient('S', new RecipeChoice.ExactChoice(new ItemStack(Material.SLIME_BALL), STICKY_BALL.get()));
		recipe.setIngredient('P', Material.PAPER);

		recipe = customRecipe(RESISTOR_1, 4, type, "WCW");
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('C', Material.COAL);

		recipe = customRecipe(RESISTOR_2, type, "RRR", "WCW");
		recipe.setIngredient('R', RESISTOR_1);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('C', CARBON);

		recipe = customRecipe(RESISTOR_3, type, "RRR", "WCW");
		recipe.setIngredient('R', RESISTOR_2);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('C', COMPRESSED_CARBON);

		recipe = customRecipe(RESISTOR_4, type, "RRR", "WCW");
		recipe.setIngredient('R', RESISTOR_3);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('C', CARBON_CHUNK);

		recipe = customRecipe(RESISTOR_5, type, "RRR", "WCW");
		recipe.setIngredient('R', RESISTOR_4);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('C', CARBONADO);

		recipe = customRecipe(CAPACITOR_1, 4, type, "ICI");
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('C', Material.COAL);

		recipe = customRecipe(CAPACITOR_2, type, "RRR", "ICI");
		recipe.setIngredient('R', CAPACITOR_1);
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('C', CARBON);

		recipe = customRecipe(CAPACITOR_3, type, "RRR", "ICI");
		recipe.setIngredient('R', CAPACITOR_2);
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('C', COMPRESSED_CARBON);

		recipe = customRecipe(CAPACITOR_4, type, "RRR", "ICI");
		recipe.setIngredient('R', CAPACITOR_3);
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('C', CARBON_CHUNK);

		recipe = customRecipe(CAPACITOR_5, type, "RRR", "ICI");
		recipe.setIngredient('R', CAPACITOR_4);
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('C', CARBONADO);

		recipe = customRecipe(ELECTROMAGNET, type, "I", "C", "I");
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('C', COPPER_COIL);

		recipe = customRecipe(MOTOR, type, " M ", "WIW", "MAM");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('A', Material.COAL);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('M', ELECTROMAGNET);

		recipe = customRecipe(LASER, type, "III", "CGG", "III");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('G', Material.GLASS_PANE);
		recipe.setIngredient('C', COPPER_COIL);
		recipe.setIngredient('M', ELECTROMAGNET);

		recipe = customRecipe(ACCUMULATOR, type, "ASA", "ARA", "ACA");
		recipe.setIngredient('S', SILVER_INGOT);
		recipe.setIngredient('C', MAGNESIUM_INGOT);
		recipe.setIngredient('R', SULFUR);
		recipe.setIngredient('A', PLASTIC_SHEET);

		recipe = customRecipe(ELECTRICAL_CIRCUIT_1, type, "RC", "PW");
		recipe.setIngredient('P', CIRCUIT_BOARD);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('R', RESISTOR_1);
		recipe.setIngredient('C', CAPACITOR_1);

		recipe = customRecipe(ELECTRICAL_CIRCUIT_2, type, "RC", "PW");
		recipe.setIngredient('P', ELECTRICAL_CIRCUIT_1);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('R', RESISTOR_2);
		recipe.setIngredient('C', CAPACITOR_2);

		recipe = customRecipe(ELECTRICAL_CIRCUIT_3, type, "RC", "PW");
		recipe.setIngredient('P', ELECTRICAL_CIRCUIT_2);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('R', RESISTOR_3);
		recipe.setIngredient('C', CAPACITOR_3);

		recipe = customRecipe(ELECTRICAL_CIRCUIT_4, type, "RC", "PW");
		recipe.setIngredient('P', ELECTRICAL_CIRCUIT_3);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('R', RESISTOR_4);
		recipe.setIngredient('C', CAPACITOR_4);

		recipe = customRecipe(ELECTRICAL_CIRCUIT_5, type, "RC", "PW");
		recipe.setIngredient('P', ELECTRICAL_CIRCUIT_4);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('R', RESISTOR_5);
		recipe.setIngredient('C', CAPACITOR_5);

		recipe = customRecipe(ENERGY_MEASURE_GADGET, type, " R ", "WCW", " A ");
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('C', ELECTRICAL_CIRCUIT_2);
		recipe.setIngredient('A', ALUMINUM_INGOT);

		recipe = customRecipe(PHOTOVOLTAIC_CELL_1, 3, type, "GGG", "ARA", "BIB");
		recipe.setIngredient('G', Material.GLASS_PANE);
		recipe.setIngredient('A', Material.AMETHYST_SHARD);
		recipe.setIngredient('B', Material.QUARTZ);
		recipe.setIngredient('I', EINGOT_1);
		recipe.setIngredient('R', RED_1);

		recipe = customRecipe(PHOTOVOLTAIC_CELL_2, 3, type, "GGG", "ARA", "BIB");
		recipe.setIngredient('G', PHOTOVOLTAIC_CELL_1);
		recipe.setIngredient('A', Material.LAPIS_LAZULI);
		recipe.setIngredient('B', QUARTZ_DUST);
		recipe.setIngredient('I', EINGOT_2);
		recipe.setIngredient('R', RED_2);

		recipe = customRecipe(PHOTOVOLTAIC_CELL_3, 3, type, "GGG", "ARA", "BIB");
		recipe.setIngredient('G', PHOTOVOLTAIC_CELL_2);
		recipe.setIngredient('A', Material.GLOW_INK_SAC);
		recipe.setIngredient('B', Material.GLOW_BERRIES);
		recipe.setIngredient('I', EINGOT_3);
		recipe.setIngredient('R', RED_2);

		recipe = customRecipe(PHOTOVOLTAIC_CELL_4, 3, type, "GGG", "ARA", "BIB");
		recipe.setIngredient('G', PHOTOVOLTAIC_CELL_3);
		recipe.setIngredient('A', Material.GLOWSTONE_DUST);
		recipe.setIngredient('B', Material.BLAZE_POWDER);
		recipe.setIngredient('I', EINGOT_4);
		recipe.setIngredient('R', RED_3);

		// Technical Components

		recipe = customRecipe(CIRCUIT_BOARD, 8, type, "SS");
		recipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);

		recipe = customRecipe(BASIC_MACHINE_CASING, type, "GIG", "ICI", "GIG");
		recipe.setIngredient('G', GEM_1);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('C', CIRCUIT_BOARD);

		recipe = customRecipe(ADVANCED_MACHINE_CASING, type, "GIG", "ICI", "GIG");
		recipe.setIngredient('G', GEM_2);
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_MACHINE_CASING, type, "GIG", "ICI", "GIG");
		recipe.setIngredient('G', GEM_3);
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_MACHINE_CASING, type, "GIG", "ICI", "GIG");
		recipe.setIngredient('G', GEM_4);
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(LIGHT_CONVEYOR_BELT, 3, type, "TTT", "M R");
		recipe.setIngredient('T', Material.DRIED_KELP);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('R', RUBBER);

		recipe = customRecipe(MEDIUM_CONVEYOR_BELT, 3, type, "TTT", "M R");
		recipe.setIngredient('T', LIGHT_CONVEYOR_BELT);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('R', RUBBER);

		recipe = customRecipe(HEAVY_CONVEYOR_BELT, 3, type, "TTT", "M R");
		recipe.setIngredient('T', MEDIUM_CONVEYOR_BELT);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('R', RUBBER);

		recipe = customRecipe(UPGRADE_BASE, type, "PWP", "SRS", "PWP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('S', SILICON);
		recipe.setIngredient('R', Material.REDSTONE);

		recipe = customRecipe(UPGRADE_SPEED, type, "GAG", "BUB", "GAG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('A', Material.REDSTONE);
		recipe.setIngredient('B', Material.SUGAR);
		recipe.setIngredient('U', UPGRADE_BASE);

		recipe = customRecipe(UPGRADE_EFFICIENCY, type, "GAG", "BUB", "GAG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('A', EINGOT_2);
		recipe.setIngredient('B', Material.DIAMOND);
		recipe.setIngredient('U', UPGRADE_BASE);

		recipe = customRecipe(UPGRADE_ENERGY_STORAGE, type, "GAG", "BUB", "GCG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('A', EINGOT_3);
		recipe.setIngredient('B', COPPER_WIRE);
		recipe.setIngredient('C', BATTERY_GREEN);
		recipe.setIngredient('U', UPGRADE_BASE);

		recipe = customRecipe(UPGRADE_RANGE, type, "GAG", "BUB", "GAG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('A', Material.LEAD);
		recipe.setIngredient('B', LEAD_DUST);
		recipe.setIngredient('U', UPGRADE_BASE);

		recipe = customRecipe(UPGRADE_LUCK, type, "GAG", "BUB", "GAG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('A', Material.EMERALD);
		recipe.setIngredient('B', Material.LAPIS_LAZULI);
		recipe.setIngredient('U', UPGRADE_BASE);

		// Machines

		Function<List<Pair<Integer, Integer>>, CustomRecipe.ResultProducer> MACHINE_UPGRADER = positions -> {
			return (result, ingredients) -> {
				ItemStack[] machines = new ItemStack[positions.size()];
				for(int i = 0; i < positions.size(); i++) {
					Pair<Integer, Integer> pos = positions.get(i);
					machines[i] = ingredients[pos.left][pos.right];
				}
				AbstractMachine.upgradeMachine(result, machines);
				return result;
			};
		};

		recipe = customRecipe(COBBLESTONE_GENERATOR, type, "CPC", "WML", "CCC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);

		recipe = customRecipe(STONE_GENERATOR, type, "SLS", "PMS", "SWS");
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);

		recipe = customRecipe(DRIPSTONE_GENERATOR, type, "CWC", "PMC", "CDC");
		recipe.setIngredient('C', Material.COBBLESTONE);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('D', Material.POINTED_DRIPSTONE);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);

		recipe = customRecipe(BASALT_GENERATOR, type, "BPB", "WML", "BSB");
		recipe.setIngredient('B', Material.BASALT);
		recipe.setIngredient('W', Material.BLUE_ICE);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		recipe.setIngredient('S', Material.SOUL_SOIL);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);

		recipe = customRecipe(LAVA_GENERATOR, type, "ILI", "GSG", "IMI");
		recipe.setIngredient('I', FINGOT_1);
		recipe.setIngredient('L', Material.LAVA_BUCKET);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('S', Material.POINTED_DRIPSTONE);
		recipe.setIngredient('M', Material.CAULDRON);

		recipe = customRecipe(BASIC_LAVA_GENERATOR, type, "ILI", "LCL", "ILI");
		recipe.setIngredient('I', FINGOT_1);
		recipe.setIngredient('L', LAVA_GENERATOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_LAVA_GENERATOR, type, "ILI", "LCL", "ILI");
		recipe.setIngredient('I', FINGOT_2);
		recipe.setIngredient('L', BASIC_LAVA_GENERATOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_LAVA_GENERATOR, type, "ILI", "LCL", "ILI");
		recipe.setIngredient('I', FINGOT_3);
		recipe.setIngredient('L', ADVANCED_LAVA_GENERATOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_LAVA_GENERATOR, type, "ILI", "LCL", "ILI");
		recipe.setIngredient('I', FINGOT_4);
		recipe.setIngredient('L', IMPROVED_LAVA_GENERATOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(OBSIDIAN_GENERATOR, type, "SOS", "BTB", "SMS");
		recipe.setIngredient('S', IINGOT_2);
		recipe.setIngredient('B', Material.BLUE_ICE);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('T', Material.TINTED_GLASS);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(ITEM_ABSORBER, type, "IGI", "SME", "ICI");
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('S', Material.CHEST);
		recipe.setIngredient('E', ENDER_CRYSTAL);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('C', ELECTRICAL_CIRCUIT_4);

		recipe = customRecipe(EXPERIENCE_ABSORBER, type, "IGI", "XME", "ICI");
		recipe.setIngredient('I', FINGOT_3);
		recipe.setIngredient('G', MAGIC_METAL);
		recipe.setIngredient('X', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('E', ENDER_CRYSTAL);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('C', ELECTRICAL_CIRCUIT_4);

		recipe = customRecipe(BASIC_FARMER, type, " B ", "MCH", "III");
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('H', Material.STONE_HOE);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('B', Material.BONE_MEAL);

		recipe = customRecipe(ADVANCED_FARMER, type, " B ", "MCH", "III").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('H', Material.IRON_HOE);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('B', BASIC_FARMER);

		recipe = customRecipe(IMPROVED_FARMER, type, " B ", "MCH", "III").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('H', Material.DIAMOND_HOE);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('B', ADVANCED_FARMER);

		recipe = customRecipe(PERFECTED_FARMER, type, " B ", "MCH", "III").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('H', Material.NETHERITE_HOE);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('B', IMPROVED_FARMER);

		recipe = customRecipe(MOB_GRINDER, type, "SBS", "HMH", "SCS");
		recipe.setIngredient('S', IINGOT_2);
		recipe.setIngredient('B', Material.DIAMOND_SWORD);
		recipe.setIngredient('H', IINGOT_3);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('C', ELECTRICAL_CIRCUIT_4);

		recipe = customRecipe(BLOCK_BREAKER, type, "IHI", "GCG", "IAI");
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('G', IINGOT_2);
		recipe.setIngredient('H', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('A', ELECTRICAL_CIRCUIT_2);

		recipe = customRecipe(QUARRY, type, "IBI", "PMP", "NEN").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('P', Material.NETHERITE_PICKAXE);
		recipe.setIngredient('B', BLOCK_BREAKER);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('I', EINGOT_4);
		recipe.setIngredient('N', IINGOT_5);
		recipe.setIngredient('E', ELECTRICAL_CIRCUIT_5);

		recipe = customRecipe(BASIC_VOID_ORE_MINER, type, "IQI", "NMN", "IBI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('Q', QUARRY);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('I', EINGOT_4);
		recipe.setIngredient('N', IINGOT_5);
		recipe.setIngredient('B', Material.BEACON);

		recipe = customRecipe(ADVANCED_VOID_ORE_MINER, type, "IQI", "NMN", "IBI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('Q', BASIC_VOID_ORE_MINER);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('I', EINGOT_4);
		recipe.setIngredient('N', IINGOT_5);
		recipe.setIngredient('B', Material.BEACON);

		recipe = customRecipe(IMPROVED_VOID_ORE_MINER, type, "IQI", "NMN", "IBI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('Q', ADVANCED_VOID_ORE_MINER);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('I', EINGOT_4);
		recipe.setIngredient('N', IINGOT_5);
		recipe.setIngredient('B', Material.BEACON);

		recipe = customRecipe(PERFECTED_VOID_ORE_MINER, type, "IQI", "NMN", "IBI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('Q', IMPROVED_VOID_ORE_MINER);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('I', EINGOT_4);
		recipe.setIngredient('N', IINGOT_5);
		recipe.setIngredient('B', Material.BEACON);

		recipe = customRecipe(DYE_PRESS, type, " H ", "PDG", "MCI");
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('D', Material.GRAY_DYE);
		recipe.setIngredient('G', Material.GLASS_PANE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('I', FINGOT_2);

		recipe = customRecipe(DYE_MIXER, type, "IGI", "RMB", "ICI");
		recipe.setIngredient('I', FINGOT_2);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);
		recipe.setIngredient('C', Material.CAULDRON);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);

		recipe = customRecipe(ENCHANTER, type, " B ", "DCD", "OMO");
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('M', MAGIC_METAL);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(DISENCHANTER, type, " B ", "ECE", "OMO");
		recipe.setIngredient('B', Material.BOOK);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('M', MAGIC_METAL);
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(ENCHANTMENT_COMBINER, type, "SDS", "UAE", "GCG").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 0), new Pair<>(1, 2))));
		recipe.setIngredient('S', SILVER_INGOT);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('D', MAGIC_METAL);
		recipe.setIngredient('A', Material.ANVIL);
		recipe.setIngredient('E', ENCHANTER);
		recipe.setIngredient('U', DISENCHANTER);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(CHUNK_LOADER, type, "NSN", "RMT", "IDI");
		recipe.setIngredient('N', ENDERIUM);
		recipe.setIngredient('S', Material.NETHER_STAR);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('T', Material.REDSTONE_TORCH);
		recipe.setIngredient('I', REDSTONIUM);
		recipe.setIngredient('D', Material.DIAMOND);

		recipe = customRecipe(COMPUTER, type, "SRS", "LGC", "SAS");
		recipe.setIngredient('S', STEEL_INGOT);
		recipe.setIngredient('R', Material.REDSTONE);
		recipe.setIngredient('L', Material.REDSTONE_LAMP);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('C', ELECTRICAL_CIRCUIT_4);
		recipe.setIngredient('A', ELECTRICAL_CIRCUIT_5);

		recipe = customRecipe(HYPER_FURNACE_1, type, " I ", "HFH", "EME");
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('F', Material.FURNACE);

		recipe = customRecipe(HYPER_FURNACE_2, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_1);

		recipe = customRecipe(HYPER_FURNACE_3, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_2);

		recipe = customRecipe(HYPER_FURNACE_4, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_3);

		recipe = customRecipe(HYPER_FURNACE_5, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_4);

		recipe = customRecipe(HYPER_FURNACE_6, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_5);

		recipe = customRecipe(HYPER_FURNACE_7, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_6);

		recipe = customRecipe(HYPER_FURNACE_8, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_7);

		recipe = customRecipe(HYPER_FURNACE_9, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_8);

		recipe = customRecipe(HYPER_FURNACE_10, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_9);

		recipe = customRecipe(HYPER_FURNACE_11, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_10);

		recipe = customRecipe(HYPER_FURNACE_12, type, " I ", "HFH", "EME").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('E', MOTOR);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('F', HYPER_FURNACE_11);

		recipe = customRecipe(BASIC_CRUSHER, type, " H ", "PCP", "MAM");
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('P', Material.STONE_PICKAXE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('A', IINGOT_1);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_CRUSHER, type, " H ", "PCP", "MAM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('H', BASIC_CRUSHER);
		recipe.setIngredient('P', Material.IRON_PICKAXE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('A', IINGOT_2);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_CRUSHER, type, " H ", "PCP", "MAM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('H', ADVANCED_CRUSHER);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('A', IINGOT_3);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_CRUSHER, type, " H ", "PCP", "MAM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('H', IMPROVED_CRUSHER);
		recipe.setIngredient('P', Material.NETHERITE_PICKAXE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('A', IINGOT_4);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(BASIC_MINERAL_EXTRACTOR, type, " T ", "MCH", "SSS");
		recipe.setIngredient('T', Material.HOPPER);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_1);

		recipe = customRecipe(ADVANCED_MINERAL_EXTRACTOR, type, " T ", "MCH", "SSS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('T', BASIC_MINERAL_EXTRACTOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_2);

		recipe = customRecipe(IMPROVED_MINERAL_EXTRACTOR, type, " T ", "MCH", "SSS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('T', ADVANCED_MINERAL_EXTRACTOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_3);

		recipe = customRecipe(PERFECTED_MINERAL_EXTRACTOR, type, " T ", "MCH", "SSS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('T', IMPROVED_MINERAL_EXTRACTOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_4);

		recipe = customRecipe(BASIC_AERIAL_EXTRACTOR, type, " B ", "MCE", "SSS");
		recipe.setIngredient('B', Material.IRON_BARS);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('E', ELECTROMAGNET);
		recipe.setIngredient('S', IINGOT_1);

		recipe = customRecipe(ADVANCED_AERIAL_EXTRACTOR, type, " B ", "MCE", "SSS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('B', BASIC_AERIAL_EXTRACTOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('E', ELECTROMAGNET);
		recipe.setIngredient('S', IINGOT_2);

		recipe = customRecipe(IMPROVED_AERIAL_EXTRACTOR, type, " B ", "MCE", "SSS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('B', ADVANCED_AERIAL_EXTRACTOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('E', ELECTROMAGNET);
		recipe.setIngredient('S', IINGOT_3);

		recipe = customRecipe(PERFECTED_AERIAL_EXTRACTOR, type, " B ", "MCE", "SSS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(0, 1))));
		recipe.setIngredient('B', IMPROVED_AERIAL_EXTRACTOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('E', ELECTROMAGNET);
		recipe.setIngredient('S', IINGOT_4);

		recipe = customRecipe(BASIC_SAWMILL, type, " I ", "MSM", "ICI");
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('S', Material.STONECUTTER);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_SAWMILL, type, " I ", "MSM", "ICI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('S', BASIC_SAWMILL);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_SAWMILL, type, " I ", "MSM", "ICI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('S', ADVANCED_SAWMILL);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_SAWMILL, type, " I ", "MSM", "ICI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('S', IMPROVED_SAWMILL);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(BASIC_SMELTERY, type, " S ", "HTH", "SCS");
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_1);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('T', Material.HOPPER);

		recipe = customRecipe(ADVANCED_SMELTERY, type, " S ", "HTH", "SCS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_2);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('T', BASIC_SMELTERY);

		recipe = customRecipe(IMPROVED_SMELTERY, type, " S ", "HTH", "SCS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_3);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('T', ADVANCED_SMELTERY);

		recipe = customRecipe(PERFECTED_SMELTERY, type, " S ", "HTH", "SCS").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('H', COPPER_COIL);
		recipe.setIngredient('S', IINGOT_4);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('T', IMPROVED_SMELTERY);

		recipe = customRecipe(BASIC_COMPRESSOR, type, " S ", "PTP", "MCM");
		recipe.setIngredient('S', IINGOT_1);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', Material.HOPPER);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_COMPRESSOR, type, " S ", "PTP", "MCM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_2);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', BASIC_COMPRESSOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_COMPRESSOR, type, " S ", "PTP", "MCM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_3);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', ADVANCED_COMPRESSOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_COMPRESSOR, type, " S ", "PTP", "MCM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_4);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('T', IMPROVED_COMPRESSOR);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(BASIC_BIO_PRESS, type, "ISI", "PCM", "FXF");
		recipe.setIngredient('S', Material.MOSS_BLOCK);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('X', Material.BUCKET);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('F', FINGOT_1);

		recipe = customRecipe(ADVANCED_BIO_PRESS, type, "ISI", "PCM", "FXF").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(2, 1))));
		recipe.setIngredient('S', Material.MOSS_BLOCK);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('X', BASIC_BIO_PRESS);
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('F', FINGOT_2);

		recipe = customRecipe(IMPROVED_BIO_PRESS, type, "ISI", "PCM", "FXF").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(2, 1))));
		recipe.setIngredient('S', Material.MOSS_BLOCK);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('X', ADVANCED_BIO_PRESS);
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('F', FINGOT_3);

		recipe = customRecipe(PERFECTED_BIO_PRESS, type, "ISI", "PCM", "FXF").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(2, 1))));
		recipe.setIngredient('S', Material.MOSS_BLOCK);
		recipe.setIngredient('P', Material.PISTON);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('X', IMPROVED_BIO_PRESS);
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('F', FINGOT_4);

		recipe = customRecipe(LATEX_EXTRACTOR, type, " P ", "MCM", "IBI");
		recipe.setIngredient('P', FINGOT_1);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('B', Material.BUCKET);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(BASIC_CRAFTER, type, " G ", "ICI", "MTM");
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('T', Material.CRAFTING_TABLE);
		recipe.setIngredient('G', GEM_1);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_CRAFTER, type, " G ", "ICI", "MTM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(2, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('T', BASIC_CRAFTER);
		recipe.setIngredient('G', GEM_2);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_CRAFTER, type, " G ", "ICI", "MTM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(2, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('T', ADVANCED_CRAFTER);
		recipe.setIngredient('G', GEM_3);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_CRAFTER, type, " G ", "ICI", "MTM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(2, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('T', IMPROVED_CRAFTER);
		recipe.setIngredient('G', GEM_4);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(BASIC_CRAFTING_FACTORY, type, " I ", "CPF", "MBM");
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		recipe.setIngredient('F', Material.FURNACE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('B', BASIC_MACHINE_CASING);
		recipe.setIngredient('P', Material.CHEST);

		recipe = customRecipe(ADVANCED_CRAFTING_FACTORY, type, " I ", "CPF", "MBM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		recipe.setIngredient('F', Material.FURNACE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('B', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('P', BASIC_CRAFTING_FACTORY);

		recipe = customRecipe(IMPROVED_CRAFTING_FACTORY, type, " I ", "CPF", "MBM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		recipe.setIngredient('F', Material.FURNACE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('B', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('P', ADVANCED_CRAFTING_FACTORY);

		recipe = customRecipe(PERFECTED_CRAFTING_FACTORY, type, " I ", "CPF", "MBM").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('C', Material.CRAFTING_TABLE);
		recipe.setIngredient('F', Material.FURNACE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('B', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('P', IMPROVED_CRAFTING_FACTORY);

		recipe = customRecipe(BASIC_CARBON_PRESS, type, " S ", "MTM", "KCK");
		recipe.setIngredient('S', IINGOT_1);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('T', Material.HOPPER);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('K', Material.COAL);

		recipe = customRecipe(ADVANCED_CARBON_PRESS, type, " S ", "MTM", "KCK").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_2);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('T', BASIC_CARBON_PRESS);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('K', CARBON);

		recipe = customRecipe(IMPROVED_CARBON_PRESS, type, " S ", "MTM", "KCK").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_3);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('T', ADVANCED_CARBON_PRESS);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('K', CARBON_CHUNK);

		recipe = customRecipe(PERFECTED_CARBON_PRESS, type, " S ", "MTM", "KCK").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_4);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('T', IMPROVED_CARBON_PRESS);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('K', CARBONADO);

		recipe = customRecipe(BASIC_FREEZER, type, " S ", "MFM", "ICI");
		recipe.setIngredient('S', IINGOT_1);
		recipe.setIngredient('F', Material.HOPPER);
		recipe.setIngredient('I', Material.SNOW_BLOCK);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_FREEZER, type, " S ", "MFM", "ICI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_2);
		recipe.setIngredient('F', BASIC_FREEZER);
		recipe.setIngredient('I', Material.ICE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_FREEZER, type, " S ", "MFM", "ICI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_3);
		recipe.setIngredient('F', ADVANCED_FREEZER);
		recipe.setIngredient('I', Material.PACKED_ICE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_FREEZER, type, " S ", "MFM", "ICI").setResultProducer(MACHINE_UPGRADER.apply(List.of(new Pair<>(1, 1))));
		recipe.setIngredient('S', IINGOT_4);
		recipe.setIngredient('F', IMPROVED_FREEZER);
		recipe.setIngredient('I', Material.BLUE_ICE);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(HONEY_EXTRACTOR, type, "FGF", "IBI", "FCF");
		recipe.setIngredient('F', FINGOT_1);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('G', GEM_1);
		recipe.setIngredient('B', Material.GLASS_BOTTLE);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(STAR_MAKER, type, "ETE", "GCP", "IMI");
		recipe.setIngredient('E', EINGOT_2);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('T', Material.PISTON);
		recipe.setIngredient('G', Material.GUNPOWDER);
		recipe.setIngredient('P', Material.FIREWORK_STAR);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(ROCKET_ASSEMBLER, type, "ETE", "GCP", "IMI");
		recipe.setIngredient('E', EINGOT_2);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('T', Material.PISTON);
		recipe.setIngredient('G', Material.GUNPOWDER);
		recipe.setIngredient('P', Material.PAPER);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);

		// Generators

		recipe = customRecipe(FURNACE_GENERATOR, type, " I ", "MCM", "KFK");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('M', MOTOR);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);
		recipe.setIngredient('K', COPPER_COIL);
		recipe.setIngredient('F', Material.FURNACE);

		recipe = customRecipe(THERMO_GENERATOR, type, "IBI", "CNC", "IWI");
		recipe.setIngredient('I', LEAD_INGOT);
		recipe.setIngredient('B', Material.IRON_BARS);
		recipe.setIngredient('C', COPPER_COIL);
		recipe.setIngredient('N', Material.IRON_NUGGET);
		recipe.setIngredient('W', Material.WATER_BUCKET);

		recipe = customRecipe(BASIC_SOLAR_PANEL, type, "PPP", "SRS", "SCS");
		recipe.setIngredient('P', PHOTOVOLTAIC_CELL_1);
		recipe.setIngredient('R', RED_1);
		recipe.setIngredient('C', EINGOT_1);
		recipe.setIngredient('S', Material.DAYLIGHT_DETECTOR);

		recipe = customRecipe(ADVANCED_SOLAR_PANEL, 2, type, "PPP", "SRS", "SCS");
		recipe.setIngredient('P', PHOTOVOLTAIC_CELL_2);
		recipe.setIngredient('R', RED_2);
		recipe.setIngredient('C', EINGOT_2);
		recipe.setIngredient('S', BASIC_SOLAR_PANEL);

		recipe = customRecipe(IMPROVED_SOLAR_PANEL, 2, type, "PPP", "SRS", "SCS");
		recipe.setIngredient('P', PHOTOVOLTAIC_CELL_3);
		recipe.setIngredient('R', RED_2);
		recipe.setIngredient('C', EINGOT_3);
		recipe.setIngredient('S', ADVANCED_SOLAR_PANEL);

		recipe = customRecipe(PERFECTED_SOLAR_PANEL, 2, type, "PPP", "SRS", "SCS");
		recipe.setIngredient('P', PHOTOVOLTAIC_CELL_4);
		recipe.setIngredient('R', RED_3);
		recipe.setIngredient('C', EINGOT_4);
		recipe.setIngredient('S', IMPROVED_SOLAR_PANEL);

		recipe = customRecipe(BASIC_BIO_GENERATOR, type, "IOI", "DMC", "FPF");
		recipe.setIngredient('O', Material.MOSS_BLOCK);
		recipe.setIngredient('D', MOTOR);
		recipe.setIngredient('C', COPPER_COIL);
		recipe.setIngredient('M', BASIC_MACHINE_CASING);
		recipe.setIngredient('P', Material.FURNACE);
		recipe.setIngredient('I', IINGOT_1);
		recipe.setIngredient('F', FINGOT_1);

		recipe = customRecipe(ADVANCED_BIO_GENERATOR, type, "IOI", "DMC", "FPF");
		recipe.setIngredient('O', Material.MOSS_BLOCK);
		recipe.setIngredient('D', MOTOR);
		recipe.setIngredient('C', COPPER_COIL);
		recipe.setIngredient('M', ADVANCED_MACHINE_CASING);
		recipe.setIngredient('P', BASIC_BIO_GENERATOR);
		recipe.setIngredient('I', IINGOT_2);
		recipe.setIngredient('F', FINGOT_2);

		recipe = customRecipe(IMPROVED_BIO_GENERATOR, type, "IOI", "DMC", "FPF");
		recipe.setIngredient('O', Material.MOSS_BLOCK);
		recipe.setIngredient('D', MOTOR);
		recipe.setIngredient('C', COPPER_COIL);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('P', ADVANCED_BIO_GENERATOR);
		recipe.setIngredient('I', IINGOT_3);
		recipe.setIngredient('F', FINGOT_3);

		recipe = customRecipe(PERFECTED_BIO_GENERATOR, type, "IOI", "DMC", "FPF");
		recipe.setIngredient('O', Material.MOSS_BLOCK);
		recipe.setIngredient('D', MOTOR);
		recipe.setIngredient('C', COPPER_COIL);
		recipe.setIngredient('M', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('P', IMPROVED_BIO_GENERATOR);
		recipe.setIngredient('I', IINGOT_4);
		recipe.setIngredient('F', FINGOT_4);

		// Storage

		type = RecipeType.CUSTOM;

		CustomRecipe.ResultProducer BACKPACK_UPGRADER = (result, ingredients) -> {
			BACKPACKS.upgradeInventory(uuid.fetch(ingredients[1][1].getItemMeta()), uuid.fetch(result.getItemMeta()));
			return result;
		};

		recipe = customRecipe(BROWN_BACKPACK, type, "LSL", "TCT", "LSL");
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('S', Material.STRING);
		recipe.setIngredient('T', Material.STICK);
		recipe.setIngredient('C', Material.CHEST);

		recipe = customRecipe(COPPER_BACKPACK, type, "XLX", "CBC", "XLX").setResultProducer(BACKPACK_UPGRADER);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('X', Material.COPPER_INGOT);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('B', BROWN_BACKPACK);

		recipe = customRecipe(IRON_BACKPACK, type, "XLX", "CBC", "XLX").setResultProducer(BACKPACK_UPGRADER);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('X', Material.IRON_INGOT);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('B', COPPER_BACKPACK);

		recipe = customRecipe(GOLDEN_BACKPACK, type, "XLX", "CBC", "XLX").setResultProducer(BACKPACK_UPGRADER);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('X', Material.GOLD_INGOT);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('B', IRON_BACKPACK);

		recipe = customRecipe(DIAMOND_BACKPACK, type, "XLX", "CBC", "XLX").setResultProducer(BACKPACK_UPGRADER);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('X', Material.DIAMOND);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('B', GOLDEN_BACKPACK);

		recipe = customRecipe(NETHERITE_BACKPACK, type, "XLX", "CBC", "XLX").setResultProducer(BACKPACK_UPGRADER);
		recipe.setIngredient('L', Material.LEATHER);
		recipe.setIngredient('X', Material.NETHERITE_INGOT);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('B', DIAMOND_BACKPACK);

		CustomRecipe.ResultProducer CRATE_COMBINER = (result, ingredients) -> {
			ItemStack left = ingredients[1][0];
			ItemStack right = ingredients[1][2];
			StorageCrateBlock.storeItems(result, left, right);
			return result;
		};

		recipe = customRecipe(OAK_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.OAK_LOG);
		recipe.setIngredient('P', Material.OAK_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(SPRUCE_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.SPRUCE_LOG);
		recipe.setIngredient('P', Material.SPRUCE_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(BIRCH_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.BIRCH_LOG);
		recipe.setIngredient('P', Material.BIRCH_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(JUNGLE_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.JUNGLE_LOG);
		recipe.setIngredient('P', Material.JUNGLE_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(ACACIA_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.ACACIA_LOG);
		recipe.setIngredient('P', Material.ACACIA_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(DARK_OAK_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.DARK_OAK_LOG);
		recipe.setIngredient('P', Material.DARK_OAK_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(MANGROVE_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.MANGROVE_LOG);
		recipe.setIngredient('P', Material.MANGROVE_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(CHERRY_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.CHERRY_LOG);
		recipe.setIngredient('P', Material.CHERRY_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(PALE_OAK_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.PALE_OAK_LOG);
		recipe.setIngredient('P', Material.PALE_OAK_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(BAMBOO_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.BAMBOO_BLOCK);
		recipe.setIngredient('P', Material.BAMBOO_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(CRIMSON_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.CRIMSON_STEM);
		recipe.setIngredient('P', Material.CRIMSON_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(WARPED_STORAGE_CRATE, type, "LPL", "CIC", "LPL");
		recipe.setIngredient('L', Material.WARPED_STEM);
		recipe.setIngredient('P', Material.WARPED_PLANKS);
		recipe.setIngredient('C', Material.BARREL);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(IRON_STORAGE_CRATE, type, "INI", "SGS", "INI").setResultProducer(CRATE_COMBINER);
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('N', Material.IRON_NUGGET);
		recipe.setIngredient('S', new CustomRecipeChoice(OAK_STORAGE_CRATE, SPRUCE_STORAGE_CRATE, BIRCH_STORAGE_CRATE, JUNGLE_STORAGE_CRATE, ACACIA_STORAGE_CRATE, DARK_OAK_STORAGE_CRATE, MANGROVE_STORAGE_CRATE, CHERRY_STORAGE_CRATE, PALE_OAK_STORAGE_CRATE, BAMBOO_STORAGE_CRATE, CRIMSON_STORAGE_CRATE, WARPED_STORAGE_CRATE));
		recipe.setIngredient('G', Material.GOLD_INGOT);

		recipe = customRecipe(GOLDEN_STORAGE_CRATE, type, "GNG", "SDS", "GNG").setResultProducer(CRATE_COMBINER);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('N', Material.GOLD_NUGGET);
		recipe.setIngredient('S', IRON_STORAGE_CRATE);
		recipe.setIngredient('D', Material.DIAMOND);

		recipe = customRecipe(DIAMOND_STORAGE_CRATE, type, "DED", "SNS", "DED").setResultProducer(CRATE_COMBINER);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('S', GOLDEN_STORAGE_CRATE);
		recipe.setIngredient('N', Material.EMERALD);

		recipe = customRecipe(EMERALD_STORAGE_CRATE, type, "DED", "SNS", "DED").setResultProducer(CRATE_COMBINER);
		recipe.setIngredient('D', Material.EMERALD);
		recipe.setIngredient('E', Material.AMETHYST_SHARD);
		recipe.setIngredient('S', DIAMOND_STORAGE_CRATE);
		recipe.setIngredient('N', Material.NETHERITE_INGOT);

		recipe = customRecipe(CLOWNFISH_STORAGE_CRATE, type, "LCL", "SNS", "LCL").setResultProducer(CRATE_COMBINER);
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('C', Material.TROPICAL_FISH);
		recipe.setIngredient('S', EMERALD_STORAGE_CRATE);
		recipe.setIngredient('N', Material.NETHER_STAR);

		recipe = customRecipe(ENDER_CHEST, type, "OEO", "BCB", "ODO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('D', Material.GRAY_DYE);
		recipe.setIngredient('C', Material.CHEST);
		recipe.setIngredient('E', ENDER_CRYSTAL);

		recipe = customRecipe(TRASHCAN, type, "CTC", "SLS", "SSS");
		recipe.setIngredient('C', Material.COBBLESTONE_SLAB);
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('T', Material.IRON_TRAPDOOR);
		recipe.setIngredient('L', Material.LAVA_BUCKET);

		type = RecipeType.ENGINEER;

		recipe = customRecipe(STORAGE_CASING, 4, type, "PTP", "TMT", "PTP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('T', TIN_INGOT);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(STORAGE_CONNECTOR, type, "IYI", "YCY", "IYI");
		recipe.setIngredient('Y', Material.YELLOW_DYE);
		recipe.setIngredient('I', TIN_INGOT);
		recipe.setIngredient('C', STORAGE_CASING);

		recipe = customRecipe(STORAGE_IMPORTER, type, "IHI", "GCG", "IGI");
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('I', TIN_INGOT);
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('C', STORAGE_CASING);

		recipe = customRecipe(STORAGE_EXPORTER, type, "IRI", "RCR", "IHI");
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('I', TIN_INGOT);
		recipe.setIngredient('H', Material.HOPPER);
		recipe.setIngredient('C', STORAGE_CASING);

		recipe = customRecipe(STORAGE_READER, type, "IOI", "LCR", "IOI");
		recipe.setIngredient('O', Material.ORANGE_DYE);
		recipe.setIngredient('I', TIN_INGOT);
		recipe.setIngredient('R', Material.COMPARATOR);
		recipe.setIngredient('L', Material.REDSTONE);
		recipe.setIngredient('C', STORAGE_CASING);

		recipe = customRecipe(ENDER_ACCESSOR, type, "EPE", "BCB", "EYE");
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('E', ENDER_STEEL);
		recipe.setIngredient('P', Material.ENDER_CHEST);
		recipe.setIngredient('C', STORAGE_CASING);
		recipe.setIngredient('Y', ENDER_CRYSTAL);

		recipe = customRecipe(STORAGE_MONITOR, type, "APA", "NME", "BCB");
		recipe.setIngredient('A', TIN_INGOT);
		recipe.setIngredient('B', IINGOT_4);
		recipe.setIngredient('P', Material.GLASS);
		recipe.setIngredient('M', IMPROVED_MACHINE_CASING);
		recipe.setIngredient('C', STORAGE_CONNECTOR);
		recipe.setIngredient('N', STORAGE_IMPORTER);
		recipe.setIngredient('E', STORAGE_EXPORTER);

		type = RecipeType.CUSTOM;

		recipe = shapelessRecipe(NONSTACKABLE_FILTER, type, "PI");
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('I', Tag.ITEMS_CHEST_BOATS);

		recipe = shapelessRecipe(DAMAGEABLE_FILTER, type, "PI");
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('I', Material.GOLDEN_PICKAXE);

		recipe = shapelessRecipe(NBT_FILTER, type, "PI");
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('I', Material.LEATHER_HELMET);

		recipe = shapelessRecipe(ENCHANTED_FILTER, type, "PI");
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('I', Material.ENCHANTED_BOOK);

		recipe = shapelessRecipe(FOOD_FILTER, type, "PI");
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('I', Material.CAKE);

		recipe = shapelessRecipe(COOKIES_FILTER, type, "PI");
		recipe.setIngredient('P', GILDED_PAPER);
		recipe.setIngredient('I', COOKIE_COOK_BOOK);

		// Fluids

		recipe = customRecipe(BASIC_TANK, type, "IGI", "P P", "ICI");
		recipe.setIngredient('I', FINGOT_1);
		recipe.setIngredient('G', GEM_1);
		recipe.setIngredient('P', Material.GLASS_PANE);
		recipe.setIngredient('C', BASIC_MACHINE_CASING);

		recipe = customRecipe(ADVANCED_TANK, type, "IGI", "P P", "ICI");
		recipe.setIngredient('I', FINGOT_2);
		recipe.setIngredient('G', GEM_2);
		recipe.setIngredient('P', BASIC_TANK);
		recipe.setIngredient('C', ADVANCED_MACHINE_CASING);

		recipe = customRecipe(IMPROVED_TANK, type, "IGI", "P P", "ICI");
		recipe.setIngredient('I', FINGOT_3);
		recipe.setIngredient('G', GEM_3);
		recipe.setIngredient('P', ADVANCED_TANK);
		recipe.setIngredient('C', IMPROVED_MACHINE_CASING);

		recipe = customRecipe(PERFECTED_TANK, type, "IGI", "P P", "ICI");
		recipe.setIngredient('I', FINGOT_4);
		recipe.setIngredient('G', GEM_4);
		recipe.setIngredient('P', IMPROVED_TANK);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);

		recipe = customRecipe(FLUID_PUMP, type, "IGI", "PTP", "IMI");
		recipe.setIngredient('I', FINGOT_1);
		recipe.setIngredient('G', IINGOT_1);
		recipe.setIngredient('P', Material.GLASS_PANE);
		recipe.setIngredient('T', BASIC_TANK);
		recipe.setIngredient('M', MOTOR);

		recipe = customRecipe(ENDER_TANK, type, "OEO", "BCB", "ODO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('D', Material.GRAY_DYE);
		recipe.setIngredient('C', Material.BUCKET);
		recipe.setIngredient('E', ENDER_CRYSTAL);

		recipe = customRecipe(WASTE_BARREL, type, "CTC", "SLS", "SSS");
		recipe.setIngredient('C', Material.COBBLESTONE_SLAB);
		recipe.setIngredient('S', Material.STONE);
		recipe.setIngredient('T', Material.IRON_TRAPDOOR);
		recipe.setIngredient('L', Material.SPONGE);

		// Energy

		recipe = customRecipe(BATTERY_RED, type, "PLP", "CDC", "PSP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('C', Material.RED_DYE);
		recipe.setIngredient('D', SULFUR);
		recipe.setIngredient('L', LITHIUM_DUST);
		recipe.setIngredient('S', SILVER_DUST);

		recipe = customRecipe(BATTERY_YELLOW, type, "PBP", "CDC", "PBP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('C', Material.YELLOW_DYE);
		recipe.setIngredient('D', SULFUR);
		recipe.setIngredient('B', BATTERY_RED);

		recipe = customRecipe(BATTERY_GREEN, type, "PBP", "CDC", "PBP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('C', Material.GREEN_DYE);
		recipe.setIngredient('D', SULFUR);
		recipe.setIngredient('B', BATTERY_YELLOW);

		recipe = customRecipe(BATTERY_CYAN, type, "PBP", "CDC", "PBP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('C', Material.CYAN_DYE);
		recipe.setIngredient('D', SULFUR);
		recipe.setIngredient('B', BATTERY_GREEN);

		recipe = customRecipe(BATTERY_PURPLE, type, "PBP", "CDC", "PBP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('C', Material.PURPLE_DYE);
		recipe.setIngredient('D', SULFUR);
		recipe.setIngredient('B', BATTERY_CYAN);

		recipe = customRecipe(BATTERY_BLACK, type, "PBP", "CDC", "PBP");
		recipe.setIngredient('P', PLASTIC_SHEET);
		recipe.setIngredient('C', Material.BLACK_DYE);
		recipe.setIngredient('D', SULFUR);
		recipe.setIngredient('B', BATTERY_PURPLE);

		recipe = customRecipe(LED_PURPLE, type, " R ", "RWR", "AIA");
		recipe.setIngredient('R', Material.PURPLE_STAINED_GLASS_PANE);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('A', ALUMINUM_INGOT);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(LED_BLUE, type, " R ", "RWR", "AIA");
		recipe.setIngredient('R', Material.BLUE_STAINED_GLASS_PANE);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('A', ALUMINUM_INGOT);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(LED_GREEN, type, " R ", "RWR", "AIA");
		recipe.setIngredient('R', Material.GREEN_STAINED_GLASS_PANE);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('A', ALUMINUM_INGOT);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(LED_ORANGE, type, " R ", "RWR", "AIA");
		recipe.setIngredient('R', Material.ORANGE_STAINED_GLASS_PANE);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('A', ALUMINUM_INGOT);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(LED_RED, type, " R ", "RWR", "AIA");
		recipe.setIngredient('R', Material.RED_STAINED_GLASS_PANE);
		recipe.setIngredient('W', COPPER_WIRE);
		recipe.setIngredient('A', ALUMINUM_INGOT);
		recipe.setIngredient('I', Material.IRON_INGOT);

		recipe = customRecipe(TESSERACT, type, "EYE", "BCB", "APA");
		recipe.setIngredient('E', ENDERIUM);
		recipe.setIngredient('A', ENERGETIC_ALLOY);
		recipe.setIngredient('B', BATTERY_BLACK);
		recipe.setIngredient('Y', ENDER_CRYSTAL);
		recipe.setIngredient('C', PERFECTED_MACHINE_CASING);
		recipe.setIngredient('P', ELECTRICAL_CIRCUIT_5);

		// Magic

		type = RecipeType.ALTAR;

		recipe = customRecipe(MAGIC_METAL, type, "XLX", "EGE", "XLX");
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('L', Material.LAPIS_LAZULI);
		recipe.setIngredient('X', Material.EXPERIENCE_BOTTLE);

		recipe = customRecipe(BASIC_WAND, type, " NR", "NRN", "RN ");
		recipe.setIngredient('N', Material.GOLD_NUGGET);
		recipe.setIngredient('R', Material.BLAZE_ROD);

		recipe = customRecipe(MAGIC_WAND, type, " NR", "NRN", "RN ");
		recipe.setIngredient('N', Material.GOLD_INGOT);
		recipe.setIngredient('R', BASIC_WAND);

		recipe = customRecipe(POWERED_WAND, type, " NR", "NRN", "RN ");
		recipe.setIngredient('N', MAGIC_METAL);
		recipe.setIngredient('R', MAGIC_WAND);

		recipe = customRecipe(EMPOWERED_WAND, type, " NR", "NRN", "RN ");
		recipe.setIngredient('N', Material.NETHERITE_INGOT);
		recipe.setIngredient('R', POWERED_WAND);

		recipe = shapelessRecipe(FLOWERING_FLOWER, type, "ABC", "DEF", "GHI");
		recipe.setIngredient('A', new RecipeChoice.MaterialChoice(Material.POPPY, Material.RED_TULIP));
		recipe.setIngredient('B', Material.ORANGE_TULIP);
		recipe.setIngredient('C', Material.DANDELION);
		recipe.setIngredient('D', Material.SPORE_BLOSSOM);
		recipe.setIngredient('E', Material.WITHER_ROSE);
		recipe.setIngredient('F', new RecipeChoice.MaterialChoice(Material.LILY_OF_THE_VALLEY, Material.AZURE_BLUET, Material.WHITE_TULIP, Material.OXEYE_DAISY));
		recipe.setIngredient('G', new RecipeChoice.MaterialChoice(Material.ALLIUM, Material.PINK_TULIP));
		recipe.setIngredient('H', Material.CORNFLOWER);
		recipe.setIngredient('I', Material.BLUE_ORCHID);

		recipe = shapelessRecipe(FOREST_BUNDLE, type, "ABC", "DEF", "GHI");
		recipe.setIngredient('A', Material.MANGROVE_PROPAGULE);
		recipe.setIngredient('B', Material.BIRCH_SAPLING);
		recipe.setIngredient('C', Material.ACACIA_SAPLING);
		recipe.setIngredient('D', Material.SPRUCE_SAPLING);
		recipe.setIngredient('E', Material.CHERRY_SAPLING);
		recipe.setIngredient('F', Material.OAK_SAPLING);
		recipe.setIngredient('G', Material.JUNGLE_SAPLING);
		recipe.setIngredient('H', Material.DARK_OAK_SAPLING);
		recipe.setIngredient('I', Material.BAMBOO);

		recipe = shapelessRecipe(FOOD_BUNDLE, type, "ABC", "DEF", "GHI");
		recipe.setIngredient('A', new RecipeChoice.MaterialChoice(Material.GLOW_BERRIES, Material.SWEET_BERRIES));
		recipe.setIngredient('B', Material.APPLE);
		recipe.setIngredient('C', Material.GLISTERING_MELON_SLICE);
		recipe.setIngredient('D', new RecipeChoice.MaterialChoice(Material.PUMPKIN, Material.MELON));
		recipe.setIngredient('E', new RecipeChoice.MaterialChoice(Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_MUTTON, Material.COOKED_PORKCHOP, Material.COOKED_RABBIT));
		recipe.setIngredient('F', Material.BREAD);
		recipe.setIngredient('G', Material.GOLDEN_CARROT);
		recipe.setIngredient('H', Material.BEETROOT_SOUP);
		recipe.setIngredient('I', Material.BAKED_POTATO);

		recipe = shapelessRecipe(ORGANIC_MATTER, type, "ABC", "DEF", "GHI");
		recipe.setIngredient('A', Material.VINE);
		recipe.setIngredient('B', Material.LILY_PAD);
		recipe.setIngredient('C', Material.SEA_PICKLE);
		recipe.setIngredient('D', Material.SEAGRASS);
		recipe.setIngredient('E', Material.CACTUS);
		recipe.setIngredient('F', Material.BIG_DRIPLEAF);
		recipe.setIngredient('G', Material.SUGAR_CANE);
		recipe.setIngredient('H', Material.KELP);
		recipe.setIngredient('I', Material.BAMBOO);

		recipe = shapelessRecipe(PEARLSTONE, type, "ABC", "DEF", "GHI");
		recipe.setIngredient('A', Material.RAW_IRON);
		recipe.setIngredient('B', Material.REDSTONE);
		recipe.setIngredient('C', Material.RAW_COPPER);
		recipe.setIngredient('D', Material.LAPIS_LAZULI);
		recipe.setIngredient('E', Material.AMETHYST_SHARD);
		recipe.setIngredient('F', Material.DIAMOND);
		recipe.setIngredient('G', Material.NETHERITE_SCRAP);
		recipe.setIngredient('H', Material.EMERALD);
		recipe.setIngredient('I', Material.RAW_GOLD);

		recipe = shapelessRecipe(OVERWORLD_ARTEFACT, type, " B ", "FOE", " P ");
		recipe.setIngredient('F', FLOWERING_FLOWER);
		recipe.setIngredient('B', FOREST_BUNDLE);
		recipe.setIngredient('O', PEARLSTONE);
		recipe.setIngredient('P', ORGANIC_MATTER);
		recipe.setIngredient('E', FOOD_BUNDLE);

		recipe = customRecipe(MAGIC_ARTEFACT, type, "XAM", "EGE", "MAX");
		recipe.setIngredient('X', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('M', MAGIC_METAL);
		recipe.setIngredient('A', Material.AMETHYST_SHARD);
		recipe.setIngredient('E', Material.EMERALD);
		recipe.setIngredient('G', Material.ENCHANTED_GOLDEN_APPLE);

		recipe = customRecipe(NETHER_CORE, type, "GMD", "BTB", "DMG");
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('D', Material.GLOWSTONE_DUST);
		recipe.setIngredient('M', Material.MAGMA_CREAM);
		recipe.setIngredient('B', Material.BLAZE_POWDER);
		recipe.setIngredient('T', Material.GHAST_TEAR);

		recipe = customRecipe(NETHER_CRYSTAL, type, "GCI", "CNC", "ICG");
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('I', Material.NETHERITE_INGOT);
		recipe.setIngredient('C', NETHER_CORE);
		recipe.setIngredient('N', Material.NETHER_STAR);

		recipe = customRecipe(ENDER_CORE, type, "ESY", "FPF", "YSE");
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('Y', Material.ENDER_EYE);
		recipe.setIngredient('S', Material.SHULKER_SHELL);
		recipe.setIngredient('F', Material.CHORUS_FRUIT);
		recipe.setIngredient('P', Material.CHORUS_FLOWER);

		recipe = customRecipe(ENDER_CRYSTAL, type, "SCY", "CEC", "YCS");
		recipe.setIngredient('S', Material.SHULKER_SHELL);
		recipe.setIngredient('Y', Material.END_CRYSTAL);
		recipe.setIngredient('C', ENDER_CORE);
		recipe.setIngredient('E', Material.ELYTRA);

		recipe = shapelessRecipe(FLESH_CLUMP, type, "ABC", "DEF", "GHI");
		recipe.setIngredient('A', Material.ROTTEN_FLESH);
		recipe.setIngredient('B', Material.BONE);
		recipe.setIngredient('C', Material.STRING);
		recipe.setIngredient('D', Material.SPIDER_EYE);
		recipe.setIngredient('E', Material.GUNPOWDER);
		recipe.setIngredient('F', Material.SLIME_BALL);
		recipe.setIngredient('G', Material.PHANTOM_MEMBRANE);
		recipe.setIngredient('H', Material.ENDER_PEARL);
		recipe.setIngredient('I', Material.LEATHER);

		recipe = customRecipe(FIRE_CRYSTAL, type, "PBD", "GST", "DMP");
		recipe.setIngredient('P', Material.BLAZE_POWDER);
		recipe.setIngredient('D', Material.GLOWSTONE_DUST);
		recipe.setIngredient('B', Material.BLAZE_ROD);
		recipe.setIngredient('M', Material.MAGMA_CREAM);
		recipe.setIngredient('G', Material.GUNPOWDER);
		recipe.setIngredient('T', Material.GHAST_TEAR);
		recipe.setIngredient('S', Material.WITHER_SKELETON_SKULL);

		recipe = shapelessRecipe(MONSTER_CORE, type, " C ", "CFC", " C ");
		recipe.setIngredient('C', FLESH_CLUMP);
		recipe.setIngredient('F', FIRE_CRYSTAL);

		recipe = customRecipe(SPAWNER_CRYSTAL, type, " E ", "OCM", " N ");
		recipe.setIngredient('N', NETHER_CRYSTAL);
		recipe.setIngredient('E', ENDER_CRYSTAL);
		recipe.setIngredient('O', OVERWORLD_ARTEFACT);
		recipe.setIngredient('M', MAGIC_ARTEFACT);
		recipe.setIngredient('C', MONSTER_CORE);

		recipe = customRecipe(SPAWNER_WAND, type, "  C", " W ", "W  ");
		recipe.setIngredient('W', EMPOWERED_WAND);
		recipe.setIngredient('C', SPAWNER_CRYSTAL);

		recipe = customRecipe(DRAGON_EYE, type, "IPG", "ECE", "GPI");
		recipe.setIngredient('I', Material.IRON_NUGGET);
		recipe.setIngredient('G', Material.GOLD_NUGGET);
		recipe.setIngredient('P', Material.ENDER_PEARL);
		recipe.setIngredient('E', Material.ENDER_EYE);
		recipe.setIngredient('C', ENDER_CRYSTAL);

		recipe = customRecipe(STICKY_GOO, type, "SIZ", "ECE", "ZWS");
		recipe.setIngredient('S', Material.SLIME_BALL);
		recipe.setIngredient('Z', Material.SUGAR);
		recipe.setIngredient('E', Material.ENDER_PEARL);
		recipe.setIngredient('I', new RecipeChoice.MaterialChoice(Material.INK_SAC, Material.GLOW_INK_SAC));
		recipe.setIngredient('W', Material.WATER_BUCKET);
		recipe.setIngredient('C', Material.COBWEB);

		recipe = customRecipe(COMPACT_PEBBLE, type, "234", "1C5", "876");
		recipe.setIngredient('1', STONE_PEBBLE);
		recipe.setIngredient('2', ANDESITE_PEBBLE);
		recipe.setIngredient('3', DIORITE_PEBBLE);
		recipe.setIngredient('4', GRANITE_PEBBLE);
		recipe.setIngredient('5', CALCITE_PEBBLE);
		recipe.setIngredient('6', TUFF_PEBBLE);
		recipe.setIngredient('7', DEEPSLATE_PEBBLE);
		recipe.setIngredient('8', Material.FLINT);
		recipe.setIngredient('C', OCTUPLE_COMPRESSED_COBBLESTONE);

		recipe = customRecipe(COMPACT, type, "GFD", "PNP", "DFG");
		recipe.setIngredient('G', Material.GUNPOWDER);
		recipe.setIngredient('D', Material.GRAY_DYE);
		recipe.setIngredient('F', Material.FIREWORK_STAR);
		recipe.setIngredient('P', COMPACT_PEBBLE);
		recipe.setIngredient('N', STICKY_GOO);

		recipe = customRecipe(MINIATURIZING_WAND, type, "  C", " W ", "W  ");
		recipe.setIngredient('W', EMPOWERED_WAND);
		recipe.setIngredient('C', COMPACT);

		// Fun

		type = RecipeType.CUSTOM;

		recipe = customRecipe(CARD_PILE, type, " S ", "S S", " S ");
		recipe.setIngredient('S', PLASTIC_SHEET);

		recipe = customRecipe(CARD_PILE::get32CardDeck, type, "PBP", "PRP", " S ");
		recipe.setIngredientExact('S', CARD_PILE);
		recipe.setIngredient('P', Material.PAPER);
		recipe.setIngredient('B', Material.BLACK_DYE);
		recipe.setIngredient('R', Material.RED_DYE);

		recipe = customRecipe(CARD_PILE::get52CardDeck, type, " B ", "PRP", " S ");
		recipe.setIngredientExact('S', CARD_PILE::get32CardDeck);
		recipe.setIngredient('P', Material.PAPER);
		recipe.setIngredient('B', Material.BLACK_DYE);
		recipe.setIngredient('R', Material.RED_DYE);

		// Plushies

		recipe = customRecipe(FALSE_SYMMETRY_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.DIAMOND_SWORD);
		recipe.setIngredient('2', Material.CYAN_TERRACOTTA);
		recipe.setIngredient('3', Material.YELLOW_DYE);

		recipe = customRecipe(XISUMA_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.GREEN_WOOL);
		recipe.setIngredient('1', Material.PHANTOM_MEMBRANE);
		recipe.setIngredient('2', Material.HONEYCOMB);
		recipe.setIngredient('3', Material.TURTLE_SCUTE);

		recipe = customRecipe(ZEDAPH_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BROWN_WOOL);
		recipe.setIngredient('1', Material.REDSTONE);
		recipe.setIngredient('2', Material.LAPIS_LAZULI);
		recipe.setIngredient('3', Material.HONEY_BLOCK);

		recipe = customRecipe(XB_CRAFTED_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.RED_WOOL);
		recipe.setIngredient('1', Material.PINK_CARPET);
		recipe.setIngredient('2', Material.BRICKS);
		recipe.setIngredient('3', Material.SAND);

		recipe = customRecipe(WELSKNIGHT_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIGHT_BLUE_WOOL);
		recipe.setIngredient('1', Material.CHAINMAIL_CHESTPLATE);
		recipe.setIngredient('2', Material.BRICK);
		recipe.setIngredient('3', Material.STONE_BRICKS);

		recipe = customRecipe(TIN_FOIL_CHEF_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.CYAN_WOOL);
		recipe.setIngredient('1', Material.OAK_PLANKS);
		recipe.setIngredient('2', RAINBOW_DUST);
		recipe.setIngredient('3', Material.IRON_HOE);

		recipe = customRecipe(MUMBO_JUMBO_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BLACK_WOOL);
		recipe.setIngredient('1', Material.WHITE_DYE);
		recipe.setIngredient('2', Material.BAMBOO);
		recipe.setIngredient('3', Material.REDSTONE);

		recipe = customRecipe(JOE_HILLS_SAYS_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.CYAN_WOOL);
		recipe.setIngredient('1', Material.WHITE_DYE);
		recipe.setIngredient('2', Material.YELLOW_DYE);
		recipe.setIngredient('3', Material.BLUE_DYE);

		recipe = customRecipe(HYPNOTIZD_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.GRAY_WOOL);
		recipe.setIngredient('1', Material.BLACK_DYE);
		recipe.setIngredient('2', Material.YELLOW_DYE);
		recipe.setIngredient('3', Material.YELLOW_STAINED_GLASS);

		recipe = customRecipe(GRIAN_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.TNT);
		recipe.setIngredient('1', Material.RED_DYE);
		recipe.setIngredient('2', Material.DARK_PRISMARINE);
		recipe.setIngredient('3', RAINBOW_DUST);

		recipe = customRecipe(GUINEA_PIG_GRIAN_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BROWN_WOOL);
		recipe.setIngredient('1', Material.RABBIT_FOOT);
		recipe.setIngredient('2', Material.GOLDEN_CARROT);
		recipe.setIngredient('3', Material.ORANGE_DYE);

		recipe = customRecipe(POULTRY_MAN_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.BROWN_DYE);
		recipe.setIngredient('2', Material.ORANGE_DYE);
		recipe.setIngredient('3', Material.EGG);

		recipe = customRecipe(VINTAGE_BEEF_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BLUE_WOOL);
		recipe.setIngredient('1', Material.WHITE_DYE);
		recipe.setIngredient('2', Material.BLACK_DYE);
		recipe.setIngredient('3', Material.TURTLE_SCUTE);

		recipe = customRecipe(GOOD_TIMES_WITH_SCAR_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BROWN_WOOL);
		recipe.setIngredient('1', Material.GRASS_BLOCK);
		recipe.setIngredient('2', Material.SAND);
		recipe.setIngredient('3', Material.STONE);

		recipe = customRecipe(JELLIE_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.GRAY_DYE);
		recipe.setIngredient('2', Material.LIGHT_GRAY_DYE);
		recipe.setIngredient('3', Material.STRING);

		recipe = customRecipe(KERALIS_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.ORANGE_WOOL);
		recipe.setIngredient('1', Material.YELLOW_DYE);
		recipe.setIngredient('2', Material.SCAFFOLDING);
		recipe.setIngredient('3', Material.LIGHT_BLUE_CARPET);

		recipe = customRecipe(FRENCHRALIS_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.RED_DYE);
		recipe.setIngredient('2', Material.BLACK_DYE);
		recipe.setIngredient('3', Material.YELLOW_DYE);

		recipe = customRecipe(I_JEVIN_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIGHT_BLUE_WOOL);
		recipe.setIngredient('1', Material.SLIME_BALL);
		recipe.setIngredient('2', Material.LIGHT_BLUE_DYE);
		recipe.setIngredient('3', Material.REDSTONE);

		recipe = customRecipe(ETHOSLAB_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIGHT_GRAY_WOOL);
		recipe.setIngredient('1', Material.REDSTONE);
		recipe.setIngredient('2', Material.GRAY_DYE);
		recipe.setIngredient('3', Material.GREEN_TERRACOTTA);

		recipe = customRecipe(ISKALL85_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIME_WOOL);
		recipe.setIngredient('1', Material.SLIME_BALL);
		recipe.setIngredient('2', Material.REDSTONE);
		recipe.setIngredient('3', Material.BONE);

		recipe = customRecipe(TANGO_TEK_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.RED_WOOL);
		recipe.setIngredient('1', Material.BLACK_DYE);
		recipe.setIngredient('2', Material.IRON_INGOT);
		recipe.setIngredient('3', Material.REDSTONE_BLOCK);

		recipe = customRecipe(IMPULS_SV_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.GRAY_WOOL);
		recipe.setIngredient('1', Material.BLACK_DYE);
		recipe.setIngredient('2', Material.YELLOW_DYE);
		recipe.setIngredient('3', Material.REDSTONE);

		recipe = customRecipe(STRESSMONSTER101_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.MAGENTA_WOOL);
		recipe.setIngredient('1', Material.PINK_DYE);
		recipe.setIngredient('2', Material.PURPLE_DYE);
		recipe.setIngredient('3', Material.CYAN_DYE);

		recipe = customRecipe(BDOUBLEO100_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.DIORITE);
		recipe.setIngredient('2', Material.SADDLE);
		recipe.setIngredient('3', Material.REDSTONE_ORE);

		recipe = customRecipe(BDOUBLEO100_SMILE_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.LIGHT_BLUE_DYE);
		recipe.setIngredient('2', Material.SUGAR);
		recipe.setIngredient('3', Material.BLACK_DYE);

		recipe = customRecipe(DOCM77_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIME_WOOL);
		recipe.setIngredient('1', Material.RED_DYE);
		recipe.setIngredient('2', Material.GRAY_DYE);
		recipe.setIngredient('3', Material.REDSTONE_BLOCK);

		recipe = customRecipe(CUBFAN135_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.WHITE_DYE);
		recipe.setIngredient('2', Material.BLACK_DYE);
		recipe.setIngredient('3', Material.POINTED_DRIPSTONE);

		recipe = customRecipe(DOCTOR_CUBFAN135_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.BLUE_DYE);
		recipe.setIngredient('2', Material.GOLD_BLOCK);
		recipe.setIngredient('3', Material.GLASS);

		recipe = customRecipe(PHARAO_CUBFAN135_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.RED_WOOL);
		recipe.setIngredient('1', Material.GOLD_INGOT);
		recipe.setIngredient('2', Material.LAPIS_LAZULI);
		recipe.setIngredient('3', Material.WHITE_DYE);

		recipe = customRecipe(ZOMBIE_CLEO_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.GREEN_WOOL);
		recipe.setIngredient('1', Material.ORANGE_DYE);
		recipe.setIngredient('2', Material.ROTTEN_FLESH);
		recipe.setIngredient('3', Material.ARMOR_STAND);

		recipe = customRecipe(REN_THE_DOG_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.RED_WOOL);
		recipe.setIngredient('1', Material.BLUE_DYE);
		recipe.setIngredient('2', Material.BLACK_STAINED_GLASS_PANE);
		recipe.setIngredient('3', Material.BROWN_DYE);

		recipe = customRecipe(REN_BOB_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.GREEN_WOOL);
		recipe.setIngredient('1', Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		recipe.setIngredient('2', Material.GOLD_INGOT);
		recipe.setIngredient('3', Material.BROWN_DYE);

		recipe = customRecipe(PEARLESCENT_MOON_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.LIGHT_BLUE_DYE);
		recipe.setIngredient('2', Material.BROWN_DYE);
		recipe.setIngredient('3', Material.BLACK_DYE);

		recipe = customRecipe(GEMINI_TAY_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIME_WOOL);
		recipe.setIngredient('1', Material.AMETHYST_SHARD);
		recipe.setIngredient('2', Material.ORANGE_DYE);
		recipe.setIngredient('3', Material.CYAN_DYE);

		recipe = customRecipe(JOEYGRACEFFA_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BLUE_WOOL);
		recipe.setIngredient('1', Material.ORANGE_DYE);
		recipe.setIngredient('2', Material.LIME_DYE);
		recipe.setIngredient('3', Material.BLUE_DYE);

		recipe = customRecipe(SHUBBLE_YT_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.LIME_WOOL);
		recipe.setIngredient('1', Material.BROWN_DYE);
		recipe.setIngredient('2', Material.RED_DYE);
		recipe.setIngredient('3', Material.YELLOW_DYE);

		recipe = customRecipe(SOLIDARITY_GAMING_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.RED_WOOL);
		recipe.setIngredient('1', Material.WHITE_DYE);
		recipe.setIngredient('2', Material.BLUE_DYE);
		recipe.setIngredient('3', Material.YELLOW_DYE);

		recipe = customRecipe(SMALISHBEANS_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.BROWN_DYE);
		recipe.setIngredient('2', Material.LIME_DYE);
		recipe.setIngredient('3', Material.WHITE_DYE);

		recipe = customRecipe(SMAJOR1995_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.CYAN_DYE);
		recipe.setIngredient('2', Material.LIGHT_BLUE_DYE);
		recipe.setIngredient('3', Material.BLUE_DYE);

		recipe = customRecipe(PIXLRIFFS_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BLUE_WOOL);
		recipe.setIngredient('1', Material.BROWN_DYE);
		recipe.setIngredient('2', Material.GREEN_DYE);
		recipe.setIngredient('3', Material.BLACK_DYE);

		recipe = customRecipe(MYTHICAL_SAUSAGE_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.CYAN_WOOL);
		recipe.setIngredient('1', Material.BROWN_DYE);
		recipe.setIngredient('2', Material.BLUE_DYE);
		recipe.setIngredient('3', Material.GRAY_DYE);

		recipe = customRecipe(LDS_SHADOWLADY_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.BLUE_WOOL);
		recipe.setIngredient('1', Material.CYAN_DYE);
		recipe.setIngredient('2', Material.PINK_DYE);
		recipe.setIngredient('3', Material.LIGHT_BLUE_DYE);

		recipe = customRecipe(KATHERINEELIZ_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.WHITE_WOOL);
		recipe.setIngredient('1', Material.LIGHT_BLUE_DYE);
		recipe.setIngredient('2', Material.BLACK_DYE);
		recipe.setIngredient('3', Material.WHITE_DYE);

		recipe = customRecipe(FWHIP_PLUSHIE, type, "1W2", " 3 ");
		recipe.setIngredient('W', Material.CYAN_WOOL);
		recipe.setIngredient('1', Material.BROWN_DYE);
		recipe.setIngredient('2', Material.BLUE_DYE);
		recipe.setIngredient('3', Material.BLACK_DYE);

		// machine recipes
		Registries.BLOCKS.filterByClass(AbstractCraftingMachine.class).stream().flatMap(m -> m.getMachineRecipes(null).stream()).forEach(RecipeInit::registerRecipe);

		testPlushieDuplicates();
	}


	@SuppressWarnings("deprecation")
	private static void testPlushieDuplicates() {
		try {
			HashSet<CustomRecipe> recipes = new HashSet<>();
			for(Field field : Items.class.getDeclaredFields()) {
				if(field.getType() == CustomBlockItem.class && field.getName().endsWith("_PLUSHIE")) {
					CustomBlockItem plushie = (CustomBlockItem) field.get(null);
					ItemStack stack = plushie.get();
					CustomRecipe recipe = null;
					for(CookieRecipe r : COOKIE_RECIPES) {
						if(r instanceof CustomRecipe) {
							if(ItemUtils.equals(stack, r.getResult())) {
								recipe = (CustomRecipe) r;
								break;
							}
						}
					}
					if(recipe == null)
						continue;

					if(!recipes.isEmpty()) {
						String[] shape = recipe.getShape();
						int rows = shape.length;
						int columns = shape[0].length();
						ItemStack[][] ingredients = new ItemStack[3][3];
						for(int i = 0; i < rows; i++) {
							for(int j = 0; j < columns; j++) {
								RecipeChoice choice = recipe.getIngredientMap().get(shape[i].charAt(j));
								ingredients[i][j] = choice == null ? null : choice.getItemStack();
							}
						}
						for(CustomRecipe r : recipes)
							if(r.matches(ingredients))
								System.err.println("Matching Plushie Recipes: '" + r.getResult().getItemMeta().getDisplayName() + "' and '" + plushie.getTitle() + "'!");
					}
					recipes.add(recipe);
				}
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}


	private static CustomRecipe customRecipe(IItemSupplier result, RecipeType type, String... shape) {
		return registerRecipe(new CustomRecipe(result.get(), type).setShape(shape));
	}


	private static CustomRecipe customRecipe(IItemSupplier result, int amount, RecipeType type, String... shape) {
		return registerRecipe(new CustomRecipe(result.get(), amount, type).setShape(shape));
	}


	private static CustomRecipe shapelessRecipe(IItemSupplier result, RecipeType type, String... shape) {
		return registerRecipe(new CustomRecipe(result.get(), type, RecipeShape.SHAPELESS).setShape(shape));
	}


	@SuppressWarnings("unused")
	private static CustomRecipe shapelessRecipe(IItemSupplier result, int amount, RecipeType type, String... shape) {
		return registerRecipe(new CustomRecipe(result.get(), amount, type, RecipeShape.SHAPELESS).setShape(shape));
	}


	public static <R extends Recipe> R registerRecipe(R recipe) {
		RECIPES.register(recipe);
		if(recipe instanceof CookieRecipe) {
			if(recipe instanceof MachineRecipe mrecipe) {
				if(MACHINE_RECIPES.filterFirst(mr -> mr.getIdentifier().equals(mrecipe.getIdentifier())) == null)
					MACHINE_RECIPES.register(mrecipe);
			} else {
				COOKIE_RECIPES.register((CookieRecipe) recipe);
			}
		} else {
			Bukkit.addRecipe(recipe);
		}
		return recipe;
	}


	public static List<Recipe> getAllRecipesFor(ItemStack result) {
		List<Recipe> recipes = new ArrayList<>();
		Bukkit.getRecipesFor(result).stream().filter(recipe -> ItemUtils.match(recipe.getResult(), result)).forEach(recipes::add);
		recipes.addAll(getCookieRecipesFromStack(result));
		recipes.addAll(getMachineRecipesFromStack(result));
		return recipes;
	}


	public static Recipe getRecipeFromResult(ItemStack result) {
		return RECIPES.filterFirst(recipe -> ItemUtils.match(result, recipe.getResult()));
	}


	public static List<CookieRecipe> getCookieRecipesFromStack(ItemStack stack) {
		return COOKIE_RECIPES.filter(recipe -> ItemUtils.match(recipe.getResult(), stack));
	}


	public static List<MachineRecipe> getMachineRecipesFromStack(ItemStack stack) {
		return MACHINE_RECIPES.filter(recipe -> ItemUtils.match(recipe.getResult(), stack));
	}


	public static List<CookieRecipe> getRegisteredCustomRecipes(RecipeType type) {
		return COOKIE_RECIPES.filter(recipe -> recipe.getType() == type);
	}


	public static List<Recipe> getBukkitRecipesFor(ItemStack result) {
		return new ArrayList<>(recipeByResult.get(result));
	}


	public static List<Recipe> getBukkitRecipesWith(ItemStack ingredient) {
		return new ArrayList<>(recipeByIngredient.get(ingredient));
	}

}
