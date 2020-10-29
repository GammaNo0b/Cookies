
package me.gamma.cookies.setup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.item.AluminumFoil;
import me.gamma.cookies.objects.item.AngelWings;
import me.gamma.cookies.objects.item.ArmorType;
import me.gamma.cookies.objects.item.BerryPants;
import me.gamma.cookies.objects.item.CactusShirt;
import me.gamma.cookies.objects.item.Cheese;
import me.gamma.cookies.objects.item.Cheeseburger;
import me.gamma.cookies.objects.item.ColoredArmorPiece;
import me.gamma.cookies.objects.item.Cookie;
import me.gamma.cookies.objects.item.CookieCookBook;
import me.gamma.cookies.objects.item.CopperWire;
import me.gamma.cookies.objects.item.CrystalizedEyeOfEnder;
import me.gamma.cookies.objects.item.CustomSkullItem;
import me.gamma.cookies.objects.item.DeluxeCheeseburger;
import me.gamma.cookies.objects.item.Donut;
import me.gamma.cookies.objects.item.Dough;
import me.gamma.cookies.objects.item.Drink;
import me.gamma.cookies.objects.item.Dust;
import me.gamma.cookies.objects.item.EnderCrystal;
import me.gamma.cookies.objects.item.FarmerBoots;
import me.gamma.cookies.objects.item.GildedPaper;
import me.gamma.cookies.objects.item.GlowHat;
import me.gamma.cookies.objects.item.GoldCoin;
import me.gamma.cookies.objects.item.Hamburger;
import me.gamma.cookies.objects.item.InexhaustibleGoldBag;
import me.gamma.cookies.objects.item.Ingot;
import me.gamma.cookies.objects.item.InvisibilityHat;
import me.gamma.cookies.objects.item.ItemTicker;
import me.gamma.cookies.objects.item.KnockbackStick;
import me.gamma.cookies.objects.item.Lettuce;
import me.gamma.cookies.objects.item.LettuceSeeds;
import me.gamma.cookies.objects.item.LightningBow;
import me.gamma.cookies.objects.item.LuckyLeggings;
import me.gamma.cookies.objects.item.LumberAxe;
import me.gamma.cookies.objects.item.MeasuringTape;
import me.gamma.cookies.objects.item.MilkBottle;
import me.gamma.cookies.objects.item.MilkShake;
import me.gamma.cookies.objects.item.NoobSword;
import me.gamma.cookies.objects.item.PlayerTracker;
import me.gamma.cookies.objects.item.PortableCraftingTable;
import me.gamma.cookies.objects.item.PortableCustomCraftingTable;
import me.gamma.cookies.objects.item.PortableEngineeringStation;
import me.gamma.cookies.objects.item.PortableKitchen;
import me.gamma.cookies.objects.item.RabbitBoots;
import me.gamma.cookies.objects.item.RainbowArmorPiece;
import me.gamma.cookies.objects.item.RainbowDust;
import me.gamma.cookies.objects.item.RedstoneFrequencyGadget;
import me.gamma.cookies.objects.item.Salt;
import me.gamma.cookies.objects.item.ScubaHelmet;
import me.gamma.cookies.objects.item.SlimeBoots;
import me.gamma.cookies.objects.item.SlimeSling;
import me.gamma.cookies.objects.item.TurtleShell;
import me.gamma.cookies.objects.item.VanillaRecipeBook;
import me.gamma.cookies.objects.item.VeinMinerPickaxe;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.RecipeCategory;



public class CustomItemSetup {

	public static final List<AbstractCustomItem> customItems = new ArrayList<>();
	public static final List<ItemTicker> itemTickers = new ArrayList<>();

	// Weapons
	public static final KnockbackStick KNOCKBACK_STICK = registerCustomItem(new KnockbackStick());
	public static final NoobSword NOOB_SWORD = registerCustomItem(new NoobSword());
	public static final LightningBow LIGHTNING_BOW = registerCustomItem(new LightningBow());

	// Tools
	public static final VeinMinerPickaxe VEIN_MINER_PICKAXE = registerCustomItem(new VeinMinerPickaxe());
	public static final LumberAxe LUMBER_AXE = registerCustomItem(new LumberAxe());
	public static final SlimeSling SLIME_SLING = registerCustomItem(new SlimeSling());
	public static final PlayerTracker PLAYER_TRACKER = registerCustomItem(new PlayerTracker());

	// Armor
	public static final GlowHat GLOW_HAT = registerCustomItem(new GlowHat());
	public static final ScubaHelmet SCUBA_HELMET = registerCustomItem(new ScubaHelmet());
	public static final InvisibilityHat INVISIBILITY_HAT = registerCustomItem(new InvisibilityHat());
	public static final CactusShirt CACTUS_SHIRT = registerCustomItem(new CactusShirt());
	public static final TurtleShell TURTLE_SHELL = registerCustomItem(new TurtleShell());
	public static final BerryPants BERRY_PANTS = registerCustomItem(new BerryPants());
	public static final LuckyLeggings LUCKY_LEGGINGS_1 = registerCustomItem(new LuckyLeggings(1, new ItemStack(Material.EMERALD)));
	public static final LuckyLeggings LUCKY_LEGGINGS_2 = registerCustomItem(new LuckyLeggings(2, LUCKY_LEGGINGS_1.createDefaultItemStack()));
	public static final LuckyLeggings LUCKY_LEGGINGS_3 = registerCustomItem(new LuckyLeggings(3, LUCKY_LEGGINGS_2.createDefaultItemStack()));
	public static final FarmerBoots FARMER_BOOTS = registerCustomItem(new FarmerBoots());
	public static final RabbitBoots RABBIT_BOOTS = registerCustomItem(new RabbitBoots());
	public static final SlimeBoots SLIME_BOOTS = registerCustomItem(new SlimeBoots());
	public static final AngelWings ANGEL_WINGS = registerCustomItem(new AngelWings());
	public static final RainbowArmorPiece RAINBOW_HELMET = registerCustomItem(new RainbowArmorPiece(ArmorType.HELMET));
	public static final RainbowArmorPiece RAINBOW_CHESTPLATE = registerCustomItem(new RainbowArmorPiece(ArmorType.CHESTPLATE));
	public static final RainbowArmorPiece RAINBOW_LEGGINGS = registerCustomItem(new RainbowArmorPiece(ArmorType.LEGGINGS));
	public static final RainbowArmorPiece RAINBOW_BOOTS = registerCustomItem(new RainbowArmorPiece(ArmorType.BOOTS));
	public static final ColoredArmorPiece COLORED_HELMET = registerCustomItem(new ColoredArmorPiece(ArmorType.HELMET));
	public static final ColoredArmorPiece COLORED_CHESTPLATE = registerCustomItem(new ColoredArmorPiece(ArmorType.CHESTPLATE));
	public static final ColoredArmorPiece COLORED_LEGGINGS = registerCustomItem(new ColoredArmorPiece(ArmorType.LEGGINGS));
	public static final ColoredArmorPiece COLORED_BOOTS = registerCustomItem(new ColoredArmorPiece(ArmorType.BOOTS));

	// Resources
	public static final Dust COAL_DUST = registerCustomItem(new Dust("coal_dust", "§8Coal Dust", Material.GUNPOWDER, CustomModelDataValues.COAL_DUST));
	public static final Dust IRON_DUST = registerCustomItem(new Dust("iron_dust", "§7Iron Dust", Material.GUNPOWDER, CustomModelDataValues.IRON_DUST));
	public static final Dust GOLD_DUST = registerCustomItem(new Dust("gold_dust", "§eGold Dust", Material.GLOWSTONE_DUST, CustomModelDataValues.GOLD_DUST));
	public static final Dust COPPER_DUST = registerCustomItem(new Dust("copper_dust", "§6Copper Dust", Material.GLOWSTONE_DUST, CustomModelDataValues.COPPER_DUST));
	public static final Dust ALUMINUM_DUST = registerCustomItem(new Dust("aluminum_dust", "§7Aluminum Dust", Material.SUGAR, CustomModelDataValues.ALUMINUM_DUST));
	public static final Dust LEAD_DUST = registerCustomItem(new Dust("lead_dust", "§1Lead Dust", Material.GUNPOWDER, CustomModelDataValues.LEAD_DUST));
	public static final Dust SILVER_DUST = registerCustomItem(new Dust("silver_dust", "§7Silver Dust", Material.SUGAR, CustomModelDataValues.SILVER_DUST));
	public static final Dust STEEL_DUST = registerCustomItem(new Dust("steel_dust", "§8Steel Dust", Material.GUNPOWDER, CustomModelDataValues.STEEL_DUST));
	public static final Ingot COPPER_INGOT = registerCustomItem(new Ingot("copper_ingot", "§6Copper Ingot", Material.BRICK, CustomModelDataValues.COPPER_INGOT));
	public static final Ingot ALUMINUM_INGOT = registerCustomItem(new Ingot("aluminum_ingot", "§7Aluminum Ingot", Material.IRON_INGOT, CustomModelDataValues.ALUMINUM_INGOT));
	public static final Ingot LEAD_INGOT = registerCustomItem(new Ingot("lead_ingot", "§1Lead Ingot", Material.IRON_INGOT, CustomModelDataValues.LEAD_INGOT));
	public static final Ingot SILVER_INGOT = registerCustomItem(new Ingot("silver_ingot", "§7Silver Ingot", Material.IRON_INGOT, CustomModelDataValues.SILVER_INGOT));
	public static final Ingot STEEL_INGOT = registerCustomItem(new Ingot("steel_ingot", "§8Steel Ingot", Material.IRON_INGOT, CustomModelDataValues.STEEL_INGOT));
	public static final CustomSkullItem CARBON = registerCustomItem(new CustomSkullItem("carbon", "§8Carbon", () -> RecipeCategory.RESOURCES, HeadTextures.CARBON));
	public static final CustomSkullItem COMPRESSED_CARBON = registerCustomItem(new CustomSkullItem("compressed_carbon", "§8Compressed Carbon", () -> RecipeCategory.RESOURCES, HeadTextures.COMPRESSED_CARBON));
	public static final CustomSkullItem CARBON_CHUNK = registerCustomItem(new CustomSkullItem("carbon_chunk", "§8Carbon Chunk", () -> RecipeCategory.RESOURCES, HeadTextures.CARBON_CHUNK));
	public static final CustomSkullItem CARBONADO = registerCustomItem(new CustomSkullItem("carbonado", "§8Carbonado", () -> RecipeCategory.RESOURCES, HeadTextures.CARBONADO));
	public static final CopperWire COPPER_WIRE = registerCustomItem(new CopperWire());
	public static final AluminumFoil ALUMINUM_FOIL = registerCustomItem(new AluminumFoil());
	public static final GildedPaper GILDED_PAPER = registerCustomItem(new GildedPaper());
	public static final GoldCoin GOLD_COIN = registerCustomItem(new GoldCoin());
	public static final RainbowDust RAINBOW_DUST = registerCustomItem(new RainbowDust());
	public static final EnderCrystal ENDER_CRYSTAL = registerCustomItem(new EnderCrystal());
	
	// Miscellaneous
	public static final VanillaRecipeBook VANILLA_RECIPE_BOOK = registerCustomItem(new VanillaRecipeBook());
	public static final CookieCookBook COOKIE_COOK_BOOK = registerCustomItem(new CookieCookBook());
	public static final PortableCraftingTable PORTABLE_CRAFTING_TABLE = registerCustomItem(new PortableCraftingTable());
	public static final PortableCustomCraftingTable PORTABLE_CUSTOM_CRAFTING_TABLE = registerCustomItem(new PortableCustomCraftingTable());
	public static final PortableEngineeringStation PROTABLE_ENGINEERING_STATION = registerCustomItem(new PortableEngineeringStation());
	public static final PortableKitchen PORTABLE_KITCHEN = registerCustomItem(new PortableKitchen());
	public static final MeasuringTape MEASURING_TAPE = registerCustomItem(new MeasuringTape());
	public static final CrystalizedEyeOfEnder CRYSTALIZED_EYE_OF_ENDER = registerCustomItem(new CrystalizedEyeOfEnder());

	// Redstone
	public static final RedstoneFrequencyGadget REDSTONE_FREQUENCY_GADGET = registerCustomItem(new RedstoneFrequencyGadget());
	
	// Magic
	public static final InexhaustibleGoldBag INEXHAUSTIBLE_GOLD_BAG = registerCustomItem(new InexhaustibleGoldBag());
	
	// Plants
	public static final Lettuce LETTUCE = registerCustomItem(new Lettuce());
	
	// Kitchen Ingredients
	public static final Salt SALT = registerCustomItem(new Salt());
	public static final Dough DOUGH = registerCustomItem(new Dough());
	public static final Cheese CHEESE = registerCustomItem(new Cheese());

	// Food
	public static final Cookie COOKIE = registerCustomItem(new Cookie());
	public static final Donut CHOCOLATE_DONUT = registerCustomItem(new Donut("chocolate_donut", "§6Chocolate Donut", 6, 5, DOUGH.createDefaultItemStack(), new ItemStack(Material.COCOA_BEANS), HeadTextures.CHOCOLATE_DONUT));
	public static final Donut GLAZED_DONUT = registerCustomItem(new Donut("glazed_donut", "§7Glazed Donut", 6, 5, DOUGH.createDefaultItemStack(), new ItemStack(Material.SUGAR), HeadTextures.GLAZED_DONUT));
	public static final Donut GREEN_DONUT = registerCustomItem(new Donut("green_glazed_donut", "§aGlazed Donut", 8, 6, GLAZED_DONUT.createDefaultItemStack(), new ItemStack(Material.LIME_DYE), HeadTextures.GREEN_GLAZED_DONUT));
	public static final Donut MAGENTA_DONUT = registerCustomItem(new Donut("magenta_glazed_donut", "§dGlazed Donut", 8, 6, GLAZED_DONUT.createDefaultItemStack(), new ItemStack(Material.MAGENTA_DYE), HeadTextures.MAGENTA_GLAZED_DONUT));
	public static final Donut ORANGE_DONUT = registerCustomItem(new Donut("orange_glazed_donut", "§6Glazed Donut", 8, 6, GLAZED_DONUT.createDefaultItemStack(), new ItemStack(Material.ORANGE_DYE), HeadTextures.ORANGE_GLAZED_DONUT));
	public static final Hamburger HAMBURGER = registerCustomItem(new Hamburger());
	public static final Cheeseburger CHEESEBURGER = registerCustomItem(new Cheeseburger());
	public static final DeluxeCheeseburger DELUXE_CHEESEBURGER = registerCustomItem(new DeluxeCheeseburger());
	
	// Drinks
	public static final Drink APPLE_JUICE = registerCustomItem(new Drink("§cApple Juice", "apple_juice", 4, 6, new ItemStack(Material.APPLE), Color.RED));
	public static final Drink GOLDEN_APPLE_JUICE = registerCustomItem(new Drink("§6Golden Apple Juice", "golden_apple_juice", 8, 12, new ItemStack(Material.GOLDEN_APPLE), Color.fromRGB(250, 200, 0)));
	public static final Drink ENCHANTED_GOLDEN_APPLE_JUICE = registerCustomItem(new Drink("§dEnchanted Golden Apple Juice", "enchanted_golden_apple_juice", 12, 18, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), Color.fromRGB(255, 0, 238)));
	public static final Drink SWEET_BERRY_JUICE = registerCustomItem(new Drink("§cSweet Berry Juice", "sweet_berry_juice", 4, 6, new ItemStack(Material.SWEET_BERRIES), Color.RED));
	public static final Drink BEETROOT_JUICE = registerCustomItem(new Drink("§cBeetroot Juice", "beetroot_juice", 4, 6, new ItemStack(Material.BEETROOT), Color.RED));
	public static final Drink CARROT_JUICE = registerCustomItem(new Drink("§6Carrot Juice", "carrot_juice", 4, 6, new ItemStack(Material.CARROT), Color.ORANGE));
	public static final Drink MELON_JUICE = registerCustomItem(new Drink("§cMelon Juice", "melon_juice", 4, 6, new ItemStack(Material.MELON_SLICE), Color.fromRGB(255, 75, 20)));
	public static final Drink PUMPKIN_JUICE = registerCustomItem(new Drink("§cPumpkin Juice", "pumpkin_juice", 4, 6, new ItemStack(Material.PUMPKIN), Color.fromRGB(255, 120, 0)));
	public static final Drink CHOCOLATE = registerCustomItem(new Drink("§6Chocolate", "chocolate", 6, 10, new ItemStack(Material.COCOA_BEANS), Color.fromRGB(140, 90, 10)));
	public static final Drink MILK_BOTTLE = registerCustomItem(new MilkBottle());
	public static final MilkShake SWEET_BERRY_MILK_SHAKE = registerCustomItem(new MilkShake("§cSweet Berry Milk Shake", "sweet_berry_milk_shake", 6, 8, new ItemStack(Material.SWEET_BERRIES), Color.RED));
	public static final MilkShake MELON_MILK_SHAKE = registerCustomItem(new MilkShake("§cMelon Milk Shake", "melon_milk_shake", 6, 8, new ItemStack(Material.MELON_SLICE), Color.fromRGB(255, 75, 20)));

	// Other
	public static final LettuceSeeds LETTUCE_SEEDS = registerCustomItem(new LettuceSeeds());
	
	public static <S extends AbstractCustomItem> S registerCustomItem(S item) {
		customItems.add(item);
		if(item instanceof ItemTicker) {
			itemTickers.add((ItemTicker) item);
		}
		return item;
	}


	public static Stream<Listener> getCustomListeners() {
		return customItems.stream().filter(AbstractCustomItem::hasListener).map(AbstractCustomItem::getCustomListener);
	}


	public static Map<Runnable, Long> getCustomTasks() {
		Map<Runnable, Long> tasks = new HashMap<>();
		for(AbstractCustomItem item : customItems) {
			if(item.hasTask()) {
				tasks.put(item.getCustomTask(), item.getTaskDelay());
			}
		}
		for(ItemTicker ticker : itemTickers) {
			tasks.put(ticker::tickPlayers, ticker.getDelay());
		}
		return tasks;
	}


	public static AbstractCustomItem getItemByStack(ItemStack stack) {
		if(stack == null) {
			return null;
		}
		ItemMeta meta = stack.getItemMeta();
		if(meta == null) {
			return null;
		}
		for(AbstractCustomItem item : customItems) {
			if(item.getIdentifier().equals(meta.getLocalizedName())) {
				return item;
			}
		}
		return null;
	}

}
