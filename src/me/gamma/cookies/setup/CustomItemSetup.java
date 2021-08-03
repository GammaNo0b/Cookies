
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
import me.gamma.cookies.objects.item.CustomItem;
import me.gamma.cookies.objects.item.ItemTicker;
import me.gamma.cookies.objects.item.armor.AngelWings;
import me.gamma.cookies.objects.item.armor.ArmorType;
import me.gamma.cookies.objects.item.armor.BerryPants;
import me.gamma.cookies.objects.item.armor.CactusShirt;
import me.gamma.cookies.objects.item.armor.ColoredArmorPiece;
import me.gamma.cookies.objects.item.armor.FarmerBoots;
import me.gamma.cookies.objects.item.armor.GlowHat;
import me.gamma.cookies.objects.item.armor.HasteArmorPiece;
import me.gamma.cookies.objects.item.armor.InvisibilityHat;
import me.gamma.cookies.objects.item.armor.LuckyLeggings;
import me.gamma.cookies.objects.item.armor.RabbitBoots;
import me.gamma.cookies.objects.item.armor.RainbowArmorPiece;
import me.gamma.cookies.objects.item.armor.ScubaHelmet;
import me.gamma.cookies.objects.item.armor.SlimeBoots;
import me.gamma.cookies.objects.item.armor.TurtleShell;
import me.gamma.cookies.objects.item.food.Cheese;
import me.gamma.cookies.objects.item.food.Cheeseburger;
import me.gamma.cookies.objects.item.food.Cookie;
import me.gamma.cookies.objects.item.food.DeluxeCheeseburger;
import me.gamma.cookies.objects.item.food.Donut;
import me.gamma.cookies.objects.item.food.Dough;
import me.gamma.cookies.objects.item.food.Drink;
import me.gamma.cookies.objects.item.food.Flour;
import me.gamma.cookies.objects.item.food.Fruit;
import me.gamma.cookies.objects.item.food.GreenApple;
import me.gamma.cookies.objects.item.food.Hamburger;
import me.gamma.cookies.objects.item.food.Lettuce;
import me.gamma.cookies.objects.item.food.MilkBottle;
import me.gamma.cookies.objects.item.food.MilkShake;
import me.gamma.cookies.objects.item.food.Salt;
import me.gamma.cookies.objects.item.food.Toast;
import me.gamma.cookies.objects.item.resources.AluminumFoil;
import me.gamma.cookies.objects.item.resources.BronzeCoin;
import me.gamma.cookies.objects.item.resources.CopperWire;
import me.gamma.cookies.objects.item.resources.CrystalizedEyeOfEnder;
import me.gamma.cookies.objects.item.resources.Dust;
import me.gamma.cookies.objects.item.resources.EnderCrystal;
import me.gamma.cookies.objects.item.resources.GildedPaper;
import me.gamma.cookies.objects.item.resources.GoldCoin;
import me.gamma.cookies.objects.item.resources.InexhaustibleGoldBag;
import me.gamma.cookies.objects.item.resources.Ingot;
import me.gamma.cookies.objects.item.resources.RainbowDust;
import me.gamma.cookies.objects.item.resources.SilverCoin;
import me.gamma.cookies.objects.item.tools.CookieCookBook;
import me.gamma.cookies.objects.item.tools.FarmerScythe;
import me.gamma.cookies.objects.item.tools.KnockbackStick;
import me.gamma.cookies.objects.item.tools.LightningBow;
import me.gamma.cookies.objects.item.tools.LumberAxe;
import me.gamma.cookies.objects.item.tools.MachineUpgradeBase;
import me.gamma.cookies.objects.item.tools.MachineUpgradeItem;
import me.gamma.cookies.objects.item.tools.MeasuringTape;
import me.gamma.cookies.objects.item.tools.NoobSword;
import me.gamma.cookies.objects.item.tools.PlayerTracker;
import me.gamma.cookies.objects.item.tools.PortableCraftingTable;
import me.gamma.cookies.objects.item.tools.PortableCustomCraftingTable;
import me.gamma.cookies.objects.item.tools.PortableEndPortal;
import me.gamma.cookies.objects.item.tools.PortableEnderChest;
import me.gamma.cookies.objects.item.tools.PortableEngineeringStation;
import me.gamma.cookies.objects.item.tools.PortableKitchen;
import me.gamma.cookies.objects.item.tools.PortableNetherPortal;
import me.gamma.cookies.objects.item.tools.RedstoneFrequencyGadget;
import me.gamma.cookies.objects.item.tools.SlimeInABucket;
import me.gamma.cookies.objects.item.tools.SlimeSling;
import me.gamma.cookies.objects.item.tools.VanillaRecipeBook;
import me.gamma.cookies.objects.item.tools.VeinMinerPickaxe;
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
	public static final FarmerScythe FARMER_SCYTHE = registerCustomItem(new FarmerScythe());
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
	public static final HasteArmorPiece HASTE_HELMET = registerCustomItem(new HasteArmorPiece(ArmorType.HELMET));
	public static final HasteArmorPiece HASTE_CHESTPLATE = registerCustomItem(new HasteArmorPiece(ArmorType.CHESTPLATE));
	public static final HasteArmorPiece HASTE_LEGGINGS = registerCustomItem(new HasteArmorPiece(ArmorType.LEGGINGS));
	public static final HasteArmorPiece HASTE_BOOTS = registerCustomItem(new HasteArmorPiece(ArmorType.BOOTS));

	// Resources
	public static final Dust COAL_DUST = registerCustomItem(new Dust("coal_dust", "§8Coal Dust", Material.GUNPOWDER, CustomModelDataValues.COAL_DUST));
	public static final Dust IRON_DUST = registerCustomItem(new Dust("iron_dust", "§7Iron Dust", Material.GUNPOWDER, CustomModelDataValues.IRON_DUST));
	public static final Dust GOLD_DUST = registerCustomItem(new Dust("gold_dust", "§eGold Dust", Material.GLOWSTONE_DUST, CustomModelDataValues.GOLD_DUST));
	public static final Dust COPPER_DUST = registerCustomItem(new Dust("copper_dust", "§6Copper Dust", Material.GLOWSTONE_DUST, CustomModelDataValues.COPPER_DUST));
	public static final Dust ALUMINUM_DUST = registerCustomItem(new Dust("aluminum_dust", "§7Aluminum Dust", Material.SUGAR, CustomModelDataValues.ALUMINUM_DUST));
	public static final Dust NICKEL_DUST = registerCustomItem(new Dust("nickel_dust", "§aNickel Dust", Material.GUNPOWDER, CustomModelDataValues.NICKEL_DUST));
	public static final Dust LEAD_DUST = registerCustomItem(new Dust("lead_dust", "§1Lead Dust", Material.GUNPOWDER, CustomModelDataValues.LEAD_DUST));
	public static final Dust SILVER_DUST = registerCustomItem(new Dust("silver_dust", "§7Silver Dust", Material.SUGAR, CustomModelDataValues.SILVER_DUST));
	public static final Dust STEEL_DUST = registerCustomItem(new Dust("steel_dust", "§8Steel Dust", Material.GUNPOWDER, CustomModelDataValues.STEEL_DUST));
	public static final CustomItem SOUL_DUST = registerCustomItem(new CustomItem("soul_dust", "§6Soul Dust", Material.GUNPOWDER, () -> RecipeCategory.RESOURCES, CustomModelDataValues.SOUL_DUST));
	public static final Ingot ALUMINUM_INGOT = registerCustomItem(new Ingot("aluminum_ingot", "§7Aluminum Ingot", Material.IRON_INGOT, CustomModelDataValues.ALUMINUM_INGOT));
	public static final Ingot NICKEL_INGOT = registerCustomItem(new Ingot("nickel_ingot", "§aNickel Ingot", Material.IRON_INGOT, CustomModelDataValues.NICKEL_INGOT));
	public static final Ingot LEAD_INGOT = registerCustomItem(new Ingot("lead_ingot", "§1Lead Ingot", Material.IRON_INGOT, CustomModelDataValues.LEAD_INGOT));
	public static final Ingot SILVER_INGOT = registerCustomItem(new Ingot("silver_ingot", "§7Silver Ingot", Material.IRON_INGOT, CustomModelDataValues.SILVER_INGOT));
	public static final Ingot STEEL_INGOT = registerCustomItem(new Ingot("steel_ingot", "§8Steel Ingot", Material.IRON_INGOT, CustomModelDataValues.STEEL_INGOT));
	public static final CopperWire COPPER_WIRE = registerCustomItem(new CopperWire());
	public static final AluminumFoil ALUMINUM_FOIL = registerCustomItem(new AluminumFoil());
	public static final GildedPaper GILDED_PAPER = registerCustomItem(new GildedPaper());
	public static final BronzeCoin BRONZE_COIN = registerCustomItem(new BronzeCoin());
	public static final SilverCoin SILVER_COIN = registerCustomItem(new SilverCoin());
	public static final GoldCoin GOLD_COIN = registerCustomItem(new GoldCoin());
	public static final RainbowDust RAINBOW_DUST = registerCustomItem(new RainbowDust());
	public static final EnderCrystal ENDER_CRYSTAL = registerCustomItem(new EnderCrystal());
	public static final CustomItem CACTUS_SEEDS = registerCustomItem(new CustomItem("cactus_seeds", "§2Cactus Seeds", Material.CACTUS, () -> RecipeCategory.RESOURCES, CustomModelDataValues.CACTUS_SEEDS));
	public static final CustomItem CARROT_SEEDS = registerCustomItem(new CustomItem("carrot_seeds", "§6Carrot Seeds", Material.CARROT, () -> RecipeCategory.RESOURCES, CustomModelDataValues.CARROT_SEEDS));
	public static final CustomItem POTATO_SEEDS = registerCustomItem(new CustomItem("potato_seeds", "§ePotato Seeds", Material.POTATO, () -> RecipeCategory.RESOURCES, CustomModelDataValues.POTATO_SEEDS));
	public static final CustomItem SUGAR_CANE_SEEDS = registerCustomItem(new CustomItem("sugar_cane_seeds", "§aSugar Cane Seeds", Material.SUGAR_CANE, () -> RecipeCategory.RESOURCES, CustomModelDataValues.SUGAR_CANE_SEEDS));

	// Miscellaneous
	public static final VanillaRecipeBook VANILLA_RECIPE_BOOK = registerCustomItem(new VanillaRecipeBook());
	public static final CookieCookBook COOKIE_COOK_BOOK = registerCustomItem(new CookieCookBook());
	public static final PortableCraftingTable PORTABLE_CRAFTING_TABLE = registerCustomItem(new PortableCraftingTable());
	public static final PortableEnderChest PORTABLE_ENDER_CHEST = registerCustomItem(new PortableEnderChest());
	public static final PortableCustomCraftingTable PORTABLE_CUSTOM_CRAFTING_TABLE = registerCustomItem(new PortableCustomCraftingTable());
	public static final PortableEngineeringStation PROTABLE_ENGINEERING_STATION = registerCustomItem(new PortableEngineeringStation());
	public static final PortableKitchen PORTABLE_KITCHEN = registerCustomItem(new PortableKitchen());
	public static final PortableNetherPortal PORTABLE_NETHER_PORTAL = registerCustomItem(new PortableNetherPortal());
	public static final PortableEndPortal PORTABLE_END_PORTAL = registerCustomItem(new PortableEndPortal());
	public static final MeasuringTape MEASURING_TAPE = registerCustomItem(new MeasuringTape());
	public static final CrystalizedEyeOfEnder CRYSTALIZED_EYE_OF_ENDER = registerCustomItem(new CrystalizedEyeOfEnder());

	// Redstone
	public static final RedstoneFrequencyGadget REDSTONE_FREQUENCY_GADGET = registerCustomItem(new RedstoneFrequencyGadget());

	// Technical Components
	public static final MachineUpgradeBase UPGRADE_BASE = registerCustomItem(new MachineUpgradeBase());
	public static final MachineUpgradeItem UPGRADE_SHARPNESS = registerCustomItem(new MachineUpgradeItem("§bSharpness", CustomModelDataValues.UPGRADE_SHARPNESS, new ItemStack(Material.IRON_SWORD), new ItemStack(Material.DIAMOND_SWORD)));
	public static final MachineUpgradeItem UPGRADE_FORTUNE = registerCustomItem(new MachineUpgradeItem("§9Fortune", CustomModelDataValues.UPGRADE_FORTUNE, new ItemStack(Material.LAPIS_BLOCK)));
	public static final MachineUpgradeItem UPGRADE_FIREASPECT = registerCustomItem(new MachineUpgradeItem("§6Fireaspect", CustomModelDataValues.UPGRADE_FIREASPECT, new ItemStack(Material.FLINT_AND_STEEL)));
	public static final MachineUpgradeItem UPGRADE_BEHEADING = registerCustomItem(new MachineUpgradeItem("§8Beheading", CustomModelDataValues.UPGRADE_BEHEADING, new ItemStack(Material.ZOMBIE_HEAD), new ItemStack(Material.SKELETON_SKULL), new ItemStack(Material.WITHER_SKELETON_SKULL), new ItemStack(Material.CREEPER_HEAD)));
	public static final MachineUpgradeItem UPGRADE_RANGE = registerCustomItem(new MachineUpgradeItem("§eRange", CustomModelDataValues.UPGRADE_RANGE, new ItemStack(Material.BLAZE_ROD), new ItemStack(Material.LEAD)));
	public static final MachineUpgradeItem UPGRADE_SPEED = registerCustomItem(new MachineUpgradeItem("§fSpeed", CustomModelDataValues.UPGRADE_SPEED, new ItemStack(Material.SUGAR)));
	public static final MachineUpgradeItem UPGRADE_EFFICIENCY = registerCustomItem(new MachineUpgradeItem("§aEfficiency", CustomModelDataValues.UPGRADE_EFFICIENCY, new ItemStack(Material.EMERALD)));

	// Magic
	public static final InexhaustibleGoldBag INEXHAUSTIBLE_GOLD_BAG = registerCustomItem(new InexhaustibleGoldBag());

	// Plants
	public static final Lettuce LETTUCE = registerCustomItem(new Lettuce());
	public static final GreenApple GREEN_APPLE = registerCustomItem(new GreenApple());
	public static final Fruit PINECONE = registerCustomItem(new Fruit("pinecone", "§6Pinecone", HeadTextures.PINECONE, Material.SPRUCE_LEAVES, 80, 0, 0));
	public static final Fruit ORANGE = registerCustomItem(new Fruit("orange", "§6Orange", HeadTextures.ORANGE, Material.JUNGLE_LEAVES, 120));
	public static final Fruit LEMON = registerCustomItem(new Fruit("lemon", "§eLemon", HeadTextures.LEMON, Material.JUNGLE_LEAVES, 160));
	public static final Fruit KIWI = registerCustomItem(new Fruit("kiwi", "§aKiwi", HeadTextures.KIWI, Material.JUNGLE_LEAVES, 200));
	public static final Fruit PEACH = registerCustomItem(new Fruit("peach", "§6Peach", HeadTextures.PEACH, Material.OAK_LEAVES, 40));
	public static final Fruit PLUM = registerCustomItem(new Fruit("plum", "§5Plum", HeadTextures.PLUM, Material.OAK_LEAVES, 160));
	public static final Fruit BANANA = registerCustomItem(new Fruit("banana", "§eBanana", HeadTextures.BANANA, Material.JUNGLE_LEAVES, 160));
	public static final Fruit APRICOT = registerCustomItem(new Fruit("apricot", "§6Apricot", HeadTextures.APRICOT, Material.ACACIA_LEAVES, 160));
	public static final Fruit PAPAYA = registerCustomItem(new Fruit("papaya", "§6Papaya", HeadTextures.PAPAYA, Material.ACACIA_LEAVES, 200));
	public static final Fruit POMEGRANATE = registerCustomItem(new Fruit("pomegranate", "§4Pomegranate", HeadTextures.POMEGRANATE, Material.DARK_OAK_LEAVES, 160));
	public static final Fruit COCONUT = registerCustomItem(new Fruit("coconut", "§6Coconut", HeadTextures.COCONUT, Material.JUNGLE_LEAVES, 120));
	public static final Fruit PEAR = registerCustomItem(new Fruit("pear", "§aPear", HeadTextures.PEAR, Material.BIRCH_LEAVES, 120));
	public static final Fruit CHERRY = registerCustomItem(new Fruit("cherry", "§4Cherry", HeadTextures.CHERRY, Material.DARK_OAK_LEAVES, 120));
	public static final Fruit MANGO = registerCustomItem(new Fruit("mango", "§aMango", HeadTextures.MANGO, Material.ACACIA_LEAVES, 200));

	// Kitchen Ingredients
	public static final Salt SALT = registerCustomItem(new Salt());
	public static final Flour FLOUR = registerCustomItem(new Flour());
	public static final Dough DOUGH = registerCustomItem(new Dough());
	public static final Cheese CHEESE = registerCustomItem(new Cheese());

	// Food
	public static final Toast TOAST = registerCustomItem(new Toast());
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
	public static final Drink ORANGE_JUICE = registerCustomItem(ORANGE.createDrink(Color.fromRGB(255, 170, 0)));
	public static final Drink LEMON_JUICE = registerCustomItem(LEMON.createDrink(Color.fromRGB(255, 251, 133)));
	public static final Drink KIWI_JUICE = registerCustomItem(KIWI.createDrink(Color.fromRGB(141, 181, 103)));
	public static final Drink PEACH_JUICE = registerCustomItem(PEACH.createDrink(Color.fromRGB(255, 162, 23)));
	public static final Drink PLUM_JUICE = registerCustomItem(PLUM.createDrink(Color.fromRGB(80, 0, 191)));
	public static final Drink BANANA_JUICE = registerCustomItem(BANANA.createDrink(Color.fromRGB(251, 255, 171)));
	public static final Drink APRICOT_JUICE = registerCustomItem(APRICOT.createDrink(Color.fromRGB(255, 153, 0)));
	public static final Drink PAPAYA_JUICE = registerCustomItem(PAPAYA.createDrink(Color.fromRGB(252, 73, 3)));
	public static final Drink POMEGRANATE_JUICE = registerCustomItem(POMEGRANATE.createDrink(Color.fromRGB(163, 0, 0)));
	public static final Drink COCONUT_MILK = registerCustomItem(new Drink("§fCoconut Milk", "coconut_milk", 6, 8, COCONUT.createDefaultItemStack(), Color.fromRGB(242, 242, 242)));
	public static final Drink PEAR_JUICE = registerCustomItem(PEAR.createDrink(Color.fromRGB(183, 255, 66)));
	public static final Drink CHERRY_JUICE = registerCustomItem(CHERRY.createDrink(Color.fromRGB(140, 0, 21)));
	public static final Drink MANGO_JUICE = registerCustomItem(MANGO.createDrink(Color.fromRGB(255, 187, 0)));
	public static final Drink CHOCOLATE = registerCustomItem(new Drink("§6Chocolate", "chocolate", 6, 10, new ItemStack(Material.COCOA_BEANS), Color.fromRGB(140, 90, 10)));
	public static final Drink MILK_BOTTLE = registerCustomItem(new MilkBottle());
	public static final MilkShake SWEET_BERRY_MILK_SHAKE = registerCustomItem(new MilkShake("§cSweet Berry Milk Shake", "sweet_berry_milk_shake", 6, 8, new ItemStack(Material.SWEET_BERRIES), Color.RED));
	public static final MilkShake MELON_MILK_SHAKE = registerCustomItem(new MilkShake("§cMelon Milk Shake", "melon_milk_shake", 6, 8, new ItemStack(Material.MELON_SLICE), Color.fromRGB(255, 75, 20)));

	// Fun
	public static final SlimeInABucket SLIME_IN_A_BUCKET = registerCustomItem(new SlimeInABucket());

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
			if(item.getRegistryName().equals(AbstractCustomItem.IDENTIFIER.fetch(meta))) {
				return item;
			}
		}
		return null;
	}

}
