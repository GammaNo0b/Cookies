
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.ITEMS;

import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.network.energy.TesseractBlock;
import me.gamma.cookies.object.block.network.fluid.EnderTankBlock;
import me.gamma.cookies.object.block.network.item.EnderChestBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomBlockItem;
import me.gamma.cookies.object.item.CustomCraftingTableBlueprint;
import me.gamma.cookies.object.item.CustomItem;
import me.gamma.cookies.object.item.PlayerRegister;
import me.gamma.cookies.object.item.armor.AngelWings;
import me.gamma.cookies.object.item.armor.ArmorType;
import me.gamma.cookies.object.item.armor.BerryPants;
import me.gamma.cookies.object.item.armor.CactusShirt;
import me.gamma.cookies.object.item.armor.ColoredArmorPiece;
import me.gamma.cookies.object.item.armor.FarmerBoots;
import me.gamma.cookies.object.item.armor.GlowHat;
import me.gamma.cookies.object.item.armor.HasteArmorPiece;
import me.gamma.cookies.object.item.armor.InvisibilityHat;
import me.gamma.cookies.object.item.armor.LuckyLeggings;
import me.gamma.cookies.object.item.armor.RabbitBoots;
import me.gamma.cookies.object.item.armor.RainbowArmorPiece;
import me.gamma.cookies.object.item.armor.ScubaHelmet;
import me.gamma.cookies.object.item.armor.TurtleShell;
import me.gamma.cookies.object.item.resources.BackpackItem;
import me.gamma.cookies.object.item.resources.BatteryItem;
import me.gamma.cookies.object.item.resources.CardItem;
import me.gamma.cookies.object.item.resources.CardPileItem;
import me.gamma.cookies.object.item.resources.EnderLinkedBlockItem;
import me.gamma.cookies.object.item.resources.GeneratorItem;
import me.gamma.cookies.object.item.resources.ItemStorageCrateItem;
import me.gamma.cookies.object.item.resources.Lootbox;
import me.gamma.cookies.object.item.resources.MachineItem;
import me.gamma.cookies.object.item.resources.MagicMetal;
import me.gamma.cookies.object.item.resources.ResistorItem;
import me.gamma.cookies.object.item.resources.TankItem;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.item.tools.Airgun;
import me.gamma.cookies.object.item.tools.AngelBlockItem;
import me.gamma.cookies.object.item.tools.CookieCookBook;
import me.gamma.cookies.object.item.tools.DragonEye;
import me.gamma.cookies.object.item.tools.EnergyMeasureGadget;
import me.gamma.cookies.object.item.tools.FarmerScythe;
import me.gamma.cookies.object.item.tools.ItemFilterItem;
import me.gamma.cookies.object.item.tools.KnockbackStick;
import me.gamma.cookies.object.item.tools.LightningBow;
import me.gamma.cookies.object.item.tools.LumberAxe;
import me.gamma.cookies.object.item.tools.MeasuringTape;
import me.gamma.cookies.object.item.tools.MiniaturizingWand;
import me.gamma.cookies.object.item.tools.MultiBlockBookItem;
import me.gamma.cookies.object.item.tools.NoobSword;
import me.gamma.cookies.object.item.tools.PlayerTracker;
import me.gamma.cookies.object.item.tools.PortableCraftingTable;
import me.gamma.cookies.object.item.tools.PortableCustomCraftingOpener;
import me.gamma.cookies.object.item.tools.PortableEndPortal;
import me.gamma.cookies.object.item.tools.PortableEnderChest;
import me.gamma.cookies.object.item.tools.PortableNetherPortal;
import me.gamma.cookies.object.item.tools.Pouch;
import me.gamma.cookies.object.item.tools.RocketLauncher;
import me.gamma.cookies.object.item.tools.SlimeSling;
import me.gamma.cookies.object.item.tools.SpawnerWand;
import me.gamma.cookies.object.item.tools.TradingCardItem;
import me.gamma.cookies.object.item.tools.VanillaRecipeBookItem;
import me.gamma.cookies.object.item.tools.VeinMinerPickaxe;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.recipe.RecipeType;
import me.gamma.cookies.object.tile.ClownfishChest;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.CachingSupplier;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.core.MinecraftItemHelper;



public class Items {

	// Miscellaneous
	public static final CustomCraftingTableBlueprint CUSTOM_CRAFTING_TABLE_BLUEPRINT = ITEMS.register(new CustomCraftingTableBlueprint());
	public static final VanillaRecipeBookItem VANILLA_RECIPE_BOOK = ITEMS.register(new VanillaRecipeBookItem());
	public static final CookieCookBook COOKIE_COOK_BOOK = ITEMS.register(new CookieCookBook());
	public static final MultiBlockBookItem MULTI_BLOCK_BOOK = ITEMS.register(new MultiBlockBookItem());
	public static final PortableCraftingTable PORTABLE_CRAFTING_TABLE = ITEMS.register(new PortableCraftingTable());
	public static final PortableCustomCraftingOpener PORTABLE_CUSTOM_CRAFTING_TABLE = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.FLETCHING_TABLE, RecipeType.CUSTOM));
	public static final PortableCustomCraftingOpener PORTABLE_ENGINEERING_STATION = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.SMITHING_TABLE, RecipeType.ENGINEER));
	public static final PortableCustomCraftingOpener PORTABLE_MAGIC_ALTAR = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.ENCHANTMENT_TABLE, RecipeType.ALTAR));
	public static final PortableCustomCraftingOpener PORTABLE_KITCHEN = ITEMS.register(new PortableCustomCraftingOpener(HeadTextures.SMOKER_OFF, RecipeType.KITCHEN));
	public static final PortableNetherPortal PORTABLE_NETHER_PORTAL = ITEMS.register(new PortableNetherPortal());
	public static final PortableEndPortal PORTABLE_END_PORTAL = ITEMS.register(new PortableEndPortal());
	public static final PortableEnderChest PORTABLE_ENDER_CHEST = ITEMS.register(new PortableEnderChest());
	public static final AngelBlockItem ANGEL_BLOCK = ITEMS.register(new AngelBlockItem());
	public static final MeasuringTape MEASURING_TAPE = ITEMS.register(new MeasuringTape());
	public static final Pouch POUCH = ITEMS.register(new Pouch());
	public static final Lootbox LOOTBOX = ITEMS.register(new Lootbox());

	// Tools
	public static final VeinMinerPickaxe VEIN_MINER_PICKAXE = ITEMS.register(new VeinMinerPickaxe());
	public static final LumberAxe LUMBER_AXE = ITEMS.register(new LumberAxe());
	public static final FarmerScythe FARMER_SCYTHE = ITEMS.register(new FarmerScythe());
	public static final SlimeSling SLIME_SLING = ITEMS.register(new SlimeSling());
	public static final PlayerTracker PLAYER_TRACKER = ITEMS.register(new PlayerTracker());
	public static final Airgun AIRGUN = ITEMS.register(new Airgun());
	public static final RocketLauncher ROCKET_LAUNCHER = ITEMS.register(new RocketLauncher());

	// Weapons
	public static final KnockbackStick KNOCKBACK_STICK = ITEMS.register(new KnockbackStick());
	public static final NoobSword NOOB_SWORD = ITEMS.register(new NoobSword());
	public static final LightningBow LIGHTNING_BOW = ITEMS.register(new LightningBow());

	// Armor
	public static final GlowHat GLOW_HAT = ITEMS.register(new GlowHat());
	public static final ScubaHelmet SCUBA_HELMET = ITEMS.register(new ScubaHelmet());
	public static final InvisibilityHat INVISIBILITY_HAT = ITEMS.register(new InvisibilityHat());
	public static final CactusShirt CACTUS_SHIRT = ITEMS.register(new CactusShirt());
	public static final TurtleShell TURTLE_SHELL = ITEMS.register(new TurtleShell());
	public static final BerryPants BERRY_PANTS = ITEMS.register(new BerryPants());
	public static final LuckyLeggings LUCKY_LEGGINGS_1 = ITEMS.register(new LuckyLeggings(1));
	public static final LuckyLeggings LUCKY_LEGGINGS_2 = ITEMS.register(new LuckyLeggings(2));
	public static final LuckyLeggings LUCKY_LEGGINGS_3 = ITEMS.register(new LuckyLeggings(3));
	public static final FarmerBoots FARMER_BOOTS = ITEMS.register(new FarmerBoots());
	public static final RabbitBoots RABBIT_BOOTS = ITEMS.register(new RabbitBoots());
	public static final AngelWings ANGEL_WINGS = ITEMS.register(new AngelWings());
	public static final RainbowArmorPiece RAINBOW_HELMET = ITEMS.register(new RainbowArmorPiece(ArmorType.HELMET));
	public static final RainbowArmorPiece RAINBOW_CHESTPLATE = ITEMS.register(new RainbowArmorPiece(ArmorType.CHESTPLATE));
	public static final RainbowArmorPiece RAINBOW_LEGGINGS = ITEMS.register(new RainbowArmorPiece(ArmorType.LEGGINGS));
	public static final RainbowArmorPiece RAINBOW_BOOTS = ITEMS.register(new RainbowArmorPiece(ArmorType.BOOTS));
	public static final ColoredArmorPiece COLORED_HELMET = ITEMS.register(new ColoredArmorPiece(ArmorType.HELMET));
	public static final ColoredArmorPiece COLORED_CHESTPLATE = ITEMS.register(new ColoredArmorPiece(ArmorType.CHESTPLATE));
	public static final ColoredArmorPiece COLORED_LEGGINGS = ITEMS.register(new ColoredArmorPiece(ArmorType.LEGGINGS));
	public static final ColoredArmorPiece COLORED_BOOTS = ITEMS.register(new ColoredArmorPiece(ArmorType.BOOTS));
	public static final HasteArmorPiece HASTE_HELMET = ITEMS.register(new HasteArmorPiece(ArmorType.HELMET));
	public static final HasteArmorPiece HASTE_CHESTPLATE = ITEMS.register(new HasteArmorPiece(ArmorType.CHESTPLATE));
	public static final HasteArmorPiece HASTE_LEGGINGS = ITEMS.register(new HasteArmorPiece(ArmorType.LEGGINGS));
	public static final HasteArmorPiece HASTE_BOOTS = ITEMS.register(new HasteArmorPiece(ArmorType.BOOTS));

	// Resources
	public static final CustomItem STONE_PEBBLE = ITEMS.register(new CustomItem("§fStone Pebble", HeadTextures.STONE_PEBBLE));
	public static final CustomItem ANDESITE_PEBBLE = ITEMS.register(new CustomItem("§fAndesite Pebble", HeadTextures.ANDESITE_PEBBLE));
	public static final CustomItem DIORITE_PEBBLE = ITEMS.register(new CustomItem("§fDiorite Pebble", HeadTextures.DIORITE_PEBBLE));
	public static final CustomItem GRANITE_PEBBLE = ITEMS.register(new CustomItem("§fGranite Pebble", HeadTextures.GRANITE_PEBBLE));
	public static final CustomItem CALCITE_PEBBLE = ITEMS.register(new CustomItem("§fCalcite Pebble", HeadTextures.CALCITE_PEBBLE));
	public static final CustomItem TUFF_PEBBLE = ITEMS.register(new CustomItem("§fTuff Pebble", HeadTextures.TUFF_PEBBLE));
	public static final CustomItem DEEPSLATE_PEBBLE = ITEMS.register(new CustomItem("§fDeepslate Pebble", HeadTextures.DEEPSLATE_PEBBLE));
	public static final CustomBlockItem COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.COMPRESSED_COBBLESTONE, "§fCompressed Cobblestone").setDescription("§7Contains 9 blocks of cobblestone."));
	public static final CustomBlockItem DOUBLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.DOUBLE_COMPRESSED_COBBLESTONE, "§fDouble Compressed Cobblestone").setDescription("§7Contains 81 blocks of cobblestone."));
	public static final CustomBlockItem TRIPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.TRIPLE_COMPRESSED_COBBLESTONE, "§fTriple Compressed Cobblestone").setDescription("§7Contains 729 blocks of cobblestone."));
	public static final CustomBlockItem QUADRUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.QUADRUPLE_COMPRESSED_COBBLESTONE, "§fQuadruple Compressed Cobblestone").setDescription("§7Contains 6561 blocks of cobblestone."));
	public static final CustomBlockItem QUINTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.QUINTUPLE_COMPRESSED_COBBLESTONE, "§fQuintuple Compressed Cobblestone").setDescription("§7Contains 59049 blocks of cobblestone."));
	public static final CustomBlockItem SEXTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.SEXTUPLE_COMPRESSED_COBBLESTONE, "§fSextuple Compressed Cobblestone").setDescription("§7Contains 531441 blocks of cobblestone."));
	public static final CustomBlockItem SEPTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.SEPTUPLE_COMPRESSED_COBBLESTONE, "§fSeptuple Compressed Cobblestone").setDescription("§7Contains 4782969 blocks of cobblestone."));
	public static final CustomBlockItem OCTUPLE_COMPRESSED_COBBLESTONE = ITEMS.register(new CustomBlockItem(Blocks.OCTUPLE_COMPRESSED_COBBLESTONE, "§fOctuple Compressed Cobblestone").setDescription("§7Contains 43046721 blocks of cobblestone."));
	public static final CustomItem DUST = ITEMS.register(new CustomItem("§fDust", HeadTextures.DUST));
	public static final CustomItem RED_DUST = ITEMS.register(new CustomItem("§fRed Dust", HeadTextures.RED_DUST));
	public static final CustomItem BLACK_SAND = ITEMS.register(new CustomItem("§fBlack Sand", HeadTextures.BLACK_SAND));
	public static final CustomItem CRUSHED_NETHERRACK = ITEMS.register(new CustomItem("§fCrushed Netherrack", HeadTextures.CRUSHED_NETHERRACK));
	public static final CustomItem CRUSHED_END_STONE = ITEMS.register(new CustomItem("§fCrushed End Stone", HeadTextures.CRUSHED_END_STONE));
	public static final CustomItem CRUSHED_OBSIDIAN = ITEMS.register(new CustomItem("§fCrushed Obsidian", HeadTextures.CRUSHED_OBSIDIAN));
	public static final CustomItem CRUSHED_CRYING_OBSIDIAN = ITEMS.register(new CustomItem("§fCrushed Crying Obsidian", HeadTextures.CRUSHED_CRYING_OBSIDIAN));
	public static final CustomItem SILICON = ITEMS.register(new CustomItem("§fSilicon", HeadTextures.SILICON));
	public static final CustomItem STEEL_COMPOUND = ITEMS.register(new CustomItem("§fSteel Compound", HeadTextures.STEEL_COMPOUND));
	public static final CustomItem PULVERIZED_COAL = ITEMS.register(new CustomItem("§fPulverized Coal", Material.GUNPOWDER));
	public static final CustomItem SULFUR = ITEMS.register(new CustomItem("§fSulfur", Material.GLOWSTONE_DUST));
	public static final CustomItem QUARTZ_DUST = ITEMS.register(new CustomItem("§fQuartz Dust", Material.SUGAR));
	public static final CustomItem IRON_DUST = ITEMS.register(new CustomItem("§fIron Dust", Material.GUNPOWDER));
	public static final CustomItem GOLD_DUST = ITEMS.register(new CustomItem("§fGold Dust", Material.GLOWSTONE_DUST));
	public static final CustomItem COPPER_DUST = ITEMS.register(new CustomItem("§fCopper Dust", Material.GLOWSTONE_DUST));
	public static final CustomItem ALUMINUM_DUST = ITEMS.register(new CustomItem("§fAluminum Dust", Material.SUGAR));
	public static final CustomItem TIN_DUST = ITEMS.register(new CustomItem("§fTin Dust", Material.SUGAR));
	public static final CustomItem NICKEL_DUST = ITEMS.register(new CustomItem("§fNickel Dust", Material.GLOWSTONE_DUST));
	public static final CustomItem LEAD_DUST = ITEMS.register(new CustomItem("§fLead Dust", Material.GUNPOWDER));
	public static final CustomItem SILVER_DUST = ITEMS.register(new CustomItem("§fSilver Dust", Material.SUGAR));
	public static final CustomItem LITHIUM_DUST = ITEMS.register(new CustomItem("§fLithium Dust", Material.SUGAR));
	public static final CustomItem MAGNESIUM_DUST = ITEMS.register(new CustomItem("§fMagnesium Dust", Material.SUGAR));
	public static final CustomItem STEEL_DUST = ITEMS.register(new CustomItem("§fSteel Dust", Material.GUNPOWDER));
	public static final CustomItem SOUL_DUST = ITEMS.register(new CustomItem("§fSoul Dust", Material.BROWN_DYE));
	public static final CustomItem ALUMINUM_INGOT = ITEMS.register(new CustomItem("§fAluminum Ingot", Material.IRON_INGOT));
	public static final CustomItem TIN_INGOT = ITEMS.register(new CustomItem("§fTin Ingot", Material.IRON_INGOT));
	public static final CustomItem NICKEL_INGOT = ITEMS.register(new CustomItem("§fNickel Ingot", Material.GOLD_INGOT));
	public static final CustomItem LEAD_INGOT = ITEMS.register(new CustomItem("§fLead Ingot", Material.IRON_INGOT));
	public static final CustomItem SILVER_INGOT = ITEMS.register(new CustomItem("§fSilver Ingot", Material.IRON_INGOT));
	public static final CustomItem MAGNESIUM_INGOT = ITEMS.register(new CustomItem("§fMagnesium Ingot", Material.IRON_INGOT));
	public static final CustomItem STEEL_INGOT = ITEMS.register(new CustomItem("§fSteel Ingot", Material.IRON_INGOT));
	public static final CustomItem BRONZE_INGOT = ITEMS.register(new CustomItem("§fBronze Ingot", Material.COPPER_INGOT));
	public static final CustomItem REDSTONE_ALLOY = ITEMS.register(new CustomItem("§eRedstone Alloy", Material.BRICK));
	public static final CustomItem ELECTRUM_INGOT = ITEMS.register(new CustomItem("§fElectrum Ingot", Material.GOLD_INGOT));
	public static final CustomItem INVAR_INGOT = ITEMS.register(new CustomItem("§fInvar Ingot", Material.IRON_INGOT));
	public static final CustomItem ALUMINUM_STEEL_INGOT = ITEMS.register(new CustomItem("§fAluminum Steel Ingot", Material.IRON_INGOT));
	public static final CustomItem HARDENED_METAL = ITEMS.register(new CustomItem("§eHardened Metal", Material.IRON_INGOT));
	public static final CustomItem HARDENED_ALLOY = ITEMS.register(new CustomItem("§eHardened Alloy", Material.NETHERITE_INGOT));
	public static final CustomItem NETHER_STEEL = ITEMS.register(new CustomItem("§eNether Steel", Material.NETHER_BRICK));
	public static final CustomItem BLAZING_ALLOY = ITEMS.register(new CustomItem("§eBlazing Alloy", Material.GOLD_INGOT));
	public static final CustomItem ENDER_STEEL = ITEMS.register(new CustomItem("§fEnder Steel", Material.IRON_INGOT));
	public static final CustomItem ENDERIUM = ITEMS.register(new CustomItem("§bEnderium", Material.NETHERITE_INGOT));
	public static final CustomItem ENERGETIC_ALLOY = ITEMS.register(new CustomItem("§eEnergetic Alloy", Material.GOLD_INGOT));
	public static final CustomItem REDSTONIUM = ITEMS.register(new CustomItem("§eRedstonium", Material.COPPER_INGOT));
	public static final CustomItem IRON_ROD = ITEMS.register(new CustomItem("§fIron Rod", Material.BREEZE_ROD));
	public static final CustomItem CARBON = ITEMS.register(new CustomItem("§fCarbon", HeadTextures.CARBON));
	public static final CustomItem COMPRESSED_CARBON = ITEMS.register(new CustomItem("§fCompressed Carbon", HeadTextures.COMPRESSED_CARBON));
	public static final CustomItem CARBON_CHUNK = ITEMS.register(new CustomItem("§fCarbon Chunk", HeadTextures.CARBON_CHUNK));
	public static final CustomItem CARBONADO = ITEMS.register(new CustomItem("§fCarbonado", HeadTextures.CARBONADO));
	public static final CustomItem BEDROCK_DUST = ITEMS.register(new CustomItem("§eBedrock Dust", Material.GUNPOWDER));
	public static final CustomItem STICKY_BALL = ITEMS.register(new CustomItem("§fSticky Ball", HeadTextures.STICKY_BALL));
	public static final CustomItem RUBBER = ITEMS.register(new CustomItem("§fRubber", HeadTextures.RUBBER));
	public static final CustomItem RUBBER_SHEETS = ITEMS.register(new CustomItem("§fRubber Sheets", Material.DRIED_KELP));
	public static final CustomItem PLASTIC_SHEET = ITEMS.register(new CustomItem("§fPlastic Sheet", Material.PAPER));
	public static final CustomItem GILDED_PAPER = ITEMS.register(new CustomItem("§fGilded Paper", Material.MAP));
	public static final CustomItem RAINBOW_DUST = ITEMS.register(new CustomItem(ColorUtils.color("Rainbow Dust", ColorUtils.RAINBOW_COLOR_SEQUENCE, 1), Material.GLOWSTONE_DUST));

	// Bags
	public static final CustomItem BAG_OF_COAL = ITEMS.register(new CustomItem("§0Bag of Coal", HeadTextures.BAG_OF_COAL));
	public static final CustomItem BAG_OF_IRON = ITEMS.register(new CustomItem("§7Bag of Iron", HeadTextures.BAG_OF_IRON));
	public static final CustomItem BAG_OF_GOLD = ITEMS.register(new CustomItem("§6Bag of Gold", HeadTextures.BAG_OF_GOLD));
	public static final CustomItem BAG_OF_REDSTONE = ITEMS.register(new CustomItem("§cBag of Redstone", HeadTextures.BAG_OF_REDSTONE));
	public static final CustomItem BAG_OF_LAPIS = ITEMS.register(new CustomItem("§9Bag of Lapis", HeadTextures.BAG_OF_LAPIS));
	public static final CustomItem BAG_OF_EMERALDS = ITEMS.register(new CustomItem("§aBag of Emeralds", HeadTextures.BAG_OF_EMERALDS));
	public static final CustomItem BAG_OF_DIAMONDS = ITEMS.register(new CustomItem("§bBag of Diamonds", HeadTextures.BAG_OF_DIAMONDS));
	public static final CustomItem BAG_OF_AMETHYSTS = ITEMS.register(new CustomItem("§5Bag of Amethysts", HeadTextures.BAG_OF_AMETHYSTS));

	public static final CustomItem BAG_OF_SEEDS = ITEMS.register(new CustomItem("§aBag of Seeds", HeadTextures.BAG_OF_SEEDS));
	public static final CustomItem BAG_OF_WHEAT = ITEMS.register(new CustomItem("§eBag of Wheat", HeadTextures.BAG_OF_WHEAT));
	public static final CustomItem BAG_OF_POTATOES = ITEMS.register(new CustomItem("§eBag of Potatoes", HeadTextures.BAG_OF_POTATOES));
	public static final CustomItem BAG_OF_POISONOUS_POTATOES = ITEMS.register(new CustomItem("§aBag of Poisonous_Potatoes", HeadTextures.BAG_OF_POISONOUS_POTATOES));
	public static final CustomItem BAG_OF_CARROTS = ITEMS.register(new CustomItem("§6Bag of Carrots", HeadTextures.BAG_OF_CARROTS));
	public static final CustomItem BAG_OF_BEETROOTS = ITEMS.register(new CustomItem("§4Bag of Beetroots", HeadTextures.BAG_OF_BEETROOTS));
	public static final CustomItem BAG_OF_PUMPKINS = ITEMS.register(new CustomItem("§6Bag of Pumpkins", HeadTextures.BAG_OF_PUMPKINS));
	public static final CustomItem BAG_OF_MELONS = ITEMS.register(new CustomItem("§2Bag of Melons", HeadTextures.BAG_OF_MELONS));
	public static final CustomItem BAG_OF_CACTI = ITEMS.register(new CustomItem("§2Bag of Cacti", HeadTextures.BAG_OF_CACTI));
	public static final CustomItem BAG_OF_SUGAR_CANE = ITEMS.register(new CustomItem("§aBag of Sugar_Cane", HeadTextures.BAG_OF_SUGAR_CANE));
	public static final CustomItem BAG_OF_BAMBOO = ITEMS.register(new CustomItem("§aBag of Bamboo", HeadTextures.BAG_OF_BAMBOO));
	public static final CustomItem BAG_OF_BROWN_MUSHROOMS = ITEMS.register(new CustomItem("§6Bag of Brown_Mushrooms", HeadTextures.BAG_OF_BROWN_MUSHROOMS));
	public static final CustomItem BAG_OF_RED_MUSHROOMS = ITEMS.register(new CustomItem("§cBag of Red_Mushrooms", HeadTextures.BAG_OF_RED_MUSHROOMS));
	public static final CustomItem BAG_OF_FLOWERS = ITEMS.register(new CustomItem("§fBag of Flowers", HeadTextures.BAG_OF_FLOWERS));

	public static final CustomItem BAG_OF_SNOW_BALLS = ITEMS.register(new CustomItem("§fBag of Snow_Balls", HeadTextures.BAG_OF_SNOW_BALLS));
	public static final CustomItem BAG_OF_BONE_MEAL = ITEMS.register(new CustomItem("§fBag of Bone_Meal", HeadTextures.BAG_OF_BONE_MEAL));
	public static final CustomItem BAG_OF_CLAY_BALLS = ITEMS.register(new CustomItem("§7Bag of Clay_Balls", HeadTextures.BAG_OF_CLAY_BALLS));
	public static final CustomItem BAG_OF_GUNPOWDER = ITEMS.register(new CustomItem("§8Bag of Gunpowder", HeadTextures.BAG_OF_GUNPOWDER));
	public static final CustomItem BAG_OF_GLOWSTONE_DUST = ITEMS.register(new CustomItem("§6Bag of Glowstone_Dust", HeadTextures.BAG_OF_GLOWSTONE_DUST));
	public static final CustomItem BAG_OF_NETHER_WARTS = ITEMS.register(new CustomItem("§4Bag of Nether_Warts", HeadTextures.BAG_OF_NETHER_WARTS));
	public static final CustomItem BAG_OF_SUGAR = ITEMS.register(new CustomItem("§fBag of Sugar", HeadTextures.BAG_OF_SUGAR));
	public static final CustomItem BAG_OF_SALT = ITEMS.register(new CustomItem("§fBag of Salt", HeadTextures.BAG_OF_SALT));
	public static final CustomItem BAG_OF_ENDER_PEARLS = ITEMS.register(new CustomItem("§3Bag of Ender_Pearls", HeadTextures.BAG_OF_ENDER_PEARLS));

	public static final CustomItem BASKET_OF_APPLES = ITEMS.register(new CustomItem("§cBasket of Apples", HeadTextures.BASKET_OF_APPLES));
	public static final CustomItem BASKET_OF_SWEET_BERRIES = ITEMS.register(new CustomItem("§cBasket of Sweet_Berries", HeadTextures.BASKET_OF_SWEET_BERRIES));
	public static final CustomItem BASKET_OF_GLOW_BERRIES = ITEMS.register(new CustomItem("§6Basket of Glow_Berries", HeadTextures.BASKET_OF_GLOW_BERRIES));
	public static final CustomItem BAG_OF_HONEY = ITEMS.register(new CustomItem("§6Bag of Honey", HeadTextures.BAG_OF_HONEY));
	public static final CustomItem BAG_OF_FISH = ITEMS.register(new CustomItem("§3Bag of Fish", HeadTextures.BAG_OF_FISH));

	// Redstone
	/*
	 * REDSTONE_FREQUENCY_GADGET = ITEMS.register(new RedstoneFrequencyGadget()); WIRELESS_REDSTONE_TRANSMITTER = ITEMS.register(new
	 * CustomBlockItem(Blocks.WIRELESS_REDSTONE_TRANSMITTER, "§cWireless Redstone Transmitter")); WIRELESS_REDSTONE_RECEIVER = ITEMS.register(new
	 * CustomBlockItem(Blocks.WIRELESS_REDSTONE_RECEIVER, "§cWireless Redstone Receiver")); REDSTONE_OR_GATE = ITEMS.register(new
	 * CustomBlockItem(Blocks.REDSTONE_OR_GATE, "§cRedstone OR Gate")); REDSTONE_AND_GATE = ITEMS.register(new CustomBlockItem(Blocks.REDSTONE_AND_GATE,
	 * "§cRedstone AND Gate")); REDSTONE_XOR_GATE = ITEMS.register(new CustomBlockItem(Blocks.REDSTONE_XOR_GATE, "§cRedstone XOR Gat"));
	 */

	// Electric Components
	public static final CustomItem COPPER_WIRE = ITEMS.register(new CustomItem("§fCopper Wire", Material.STRING));
	public static final CustomItem BRONZE_WIRE = ITEMS.register(new CustomItem("§fBronze Wire", Material.STRING));
	public static final CustomItem SILVER_WIRE = ITEMS.register(new CustomItem("§fSilver Wire", Material.STRING));
	public static final CustomItem ELECTRUM_WIRE = ITEMS.register(new CustomItem("§fElectrum Wire", Material.STRING));
	public static final CustomItem COPPER_COIL = ITEMS.register(new CustomItem("§fCopper Coil", HeadTextures.COPPER_COIL));
	public static final CustomItem BRONZE_COIL = ITEMS.register(new CustomItem("§fBronze Coil", HeadTextures.BRONZE_COIL));
	public static final CustomItem SILVER_COIL = ITEMS.register(new CustomItem("§fSilver Coil", HeadTextures.SILVER_COIL));
	public static final CustomItem ELECTRUM_COIL = ITEMS.register(new CustomItem("§fElectrum Coil", HeadTextures.ELECTRUM_COIL));
	public static final WireItem INSULATED_COPPER_WIRE = ITEMS.register(new WireItem("§fInsulated Copper Wire", HeadTextures.COPPER_WIRE, 8));
	public static final WireItem INSULATED_BRONZE_WIRE = ITEMS.register(new WireItem("§fInsulated Bronze Wire", HeadTextures.BRONZE_WIRE, 64));
	public static final WireItem INSULATED_SILVER_WIRE = ITEMS.register(new WireItem("§fInsulated Silver Wire", HeadTextures.SILVER_WIRE, 512));
	public static final WireItem INSULATED_ELECTRUM_WIRE = ITEMS.register(new WireItem("§fInsulated Electurm Wire", HeadTextures.ELECTRUM_WIRE, 4096));
	public static final EnergyMeasureGadget ENERGY_MEASURE_GADGET = ITEMS.register(new EnergyMeasureGadget());
	public static final ResistorItem RESISTOR_1 = ITEMS.register(new ResistorItem("§f1Ω Resistor", Material.ORANGE_CANDLE));
	public static final ResistorItem RESISTOR_2 = ITEMS.register(new ResistorItem("§f10Ω Resistor", Material.RED_CANDLE));
	public static final ResistorItem RESISTOR_3 = ITEMS.register(new ResistorItem("§f100Ω Resistor", Material.GREEN_CANDLE));
	public static final ResistorItem RESISTOR_4 = ITEMS.register(new ResistorItem("§f1kΩ Resistor", Material.GRAY_CANDLE));
	public static final ResistorItem RESISTOR_5 = ITEMS.register(new ResistorItem("§f10kΩ Resistor", Material.BLACK_CANDLE));
	public static final CustomItem CAPACITOR_1 = ITEMS.register(new CustomItem("§f1μF Capacitor", Material.BIRCH_BUTTON));
	public static final CustomItem CAPACITOR_2 = ITEMS.register(new CustomItem("§f10μF Capacitor", Material.BAMBOO_BUTTON));
	public static final CustomItem CAPACITOR_3 = ITEMS.register(new CustomItem("§f100μF Capacitor", Material.ACACIA_BUTTON));
	public static final CustomItem CAPACITOR_4 = ITEMS.register(new CustomItem("§f1mF Capacitor", Material.MANGROVE_BUTTON));
	public static final CustomItem CAPACITOR_5 = ITEMS.register(new CustomItem("§f10mF Capacitor", Material.DARK_OAK_BUTTON));
	public static final CustomItem ELECTROMAGNET = ITEMS.register(new CustomItem("§fElectromagnet", HeadTextures.MAGNET));
	public static final CustomItem MOTOR = ITEMS.register(new CustomItem("§fMotor", HeadTextures.ELECTROMOTOR));
	public static final CustomItem LASER = ITEMS.register(new CustomItem("§fLaser", HeadTextures.LASER));
	public static final CustomItem ELECTRICAL_CIRCUIT_1 = ITEMS.register(new CustomItem("§fBasic Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_1));
	public static final CustomItem ELECTRICAL_CIRCUIT_2 = ITEMS.register(new CustomItem("§fSimple Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_2));
	public static final CustomItem ELECTRICAL_CIRCUIT_3 = ITEMS.register(new CustomItem("§eMessy Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_3));
	public static final CustomItem ELECTRICAL_CIRCUIT_4 = ITEMS.register(new CustomItem("§eComplex Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_4));
	public static final CustomItem ELECTRICAL_CIRCUIT_5 = ITEMS.register(new CustomItem("§bPowerful Electrical Circuit", HeadTextures.ELECTRICAL_CIRCUIT_5));
	public static final CustomItem PHOTOVOLTAIC_CELL_1 = ITEMS.register(new CustomItem("§fPhotovoltaic Cell I", Material.WHITE_STAINED_GLASS_PANE));
	public static final CustomItem PHOTOVOLTAIC_CELL_2 = ITEMS.register(new CustomItem("§ePhotovoltaic Cell II", Material.YELLOW_STAINED_GLASS_PANE));
	public static final CustomItem PHOTOVOLTAIC_CELL_3 = ITEMS.register(new CustomItem("§bPhotovoltaic Cell III", Material.LIGHT_BLUE_STAINED_GLASS_PANE));
	public static final CustomItem PHOTOVOLTAIC_CELL_4 = ITEMS.register(new CustomItem("§dPhotovoltaic Cell IV", Material.MAGENTA_STAINED_GLASS_PANE));

	// Technical Components
	public static final CustomItem CIRCUIT_BOARD = ITEMS.register(new CustomItem("§fCircuit Board", Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
	public static final CustomBlockItem LIGHT_CONVEYOR_BELT = ITEMS.register(new CustomBlockItem(Blocks.LIGHT_CONVEYOR_BELT, "§fLight Conveyor Belt"));
	public static final CustomBlockItem MEDIUM_CONVEYOR_BELT = ITEMS.register(new CustomBlockItem(Blocks.MEDIUM_CONVEYOR_BELT, "§fMedium Conveyor Belt"));
	public static final CustomBlockItem HEAVY_CONVEYOR_BELT = ITEMS.register(new CustomBlockItem(Blocks.HEAVY_CONVEYOR_BELT, "§fHeavy Conveyor Belt"));
	public static final CustomItem UPGRADE_BASE = ITEMS.register(new CustomItem("§fMachine Upgrade Base", Material.MAP));
	public static final CustomItem UPGRADE_SPEED = ITEMS.register(new CustomItem("§fSpeed Upgrade", Material.MAP));
	public static final CustomItem UPGRADE_EFFICIENCY = ITEMS.register(new CustomItem("§fEfficiency Upgrade", Material.MAP));
	public static final CustomItem UPGRADE_ENERGY_STORAGE = ITEMS.register(new CustomItem("§fEnergy Storage Upgrade", Material.MAP));
	public static final CustomItem UPGRADE_RANGE = ITEMS.register(new CustomItem("§fRange Upgrade", Material.MAP));
	public static final CustomItem UPGRADE_LUCK = ITEMS.register(new CustomItem("§fLuck Upgrade", Material.MAP));
	public static final TradingCardItem TRADING_CARD = ITEMS.register(new TradingCardItem());

	// Machines
	public static final CustomBlockItem BASIC_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.BASIC_MACHINE_CASING, "§fBasic Machine Casing"));
	public static final CustomBlockItem ADVANCED_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.ADVANCED_MACHINE_CASING, "§eAdvanced Machine Casing"));
	public static final CustomBlockItem IMPROVED_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.IMPROVED_MACHINE_CASING, "§bImproved Machine Casing"));
	public static final CustomBlockItem PERFECTED_MACHINE_CASING = ITEMS.register(new CustomBlockItem(Blocks.PERFECTED_MACHINE_CASING, "§dPerfected Machine Casing"));
	public static final CustomBlockItem COBBLESTONE_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.COBBLESTONE_GENERATOR, "§fCobblestone Generator"));
	public static final CustomBlockItem STONE_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.STONE_GENERATOR, "§fStone Generator"));
	public static final CustomBlockItem DRIPSTONE_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.DRIPSTONE_GENERATOR, "§fDripstone Generator"));
	public static final CustomBlockItem BASALT_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.BASALT_GENERATOR, "§fBasalt Generator"));
	public static final CustomBlockItem LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.LAVA_GENERATOR, "§fLava Generator"));
	public static final CustomBlockItem BASIC_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.BASIC_LAVA_GENERATOR, "§fBasic Lava Generator"));
	public static final CustomBlockItem ADVANCED_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.ADVANCED_LAVA_GENERATOR, "§eAdvanced Lava Generator"));
	public static final CustomBlockItem IMPROVED_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.IMPROVED_LAVA_GENERATOR, "§bImproved Lava Generator"));
	public static final CustomBlockItem PERFECTED_LAVA_GENERATOR = ITEMS.register(new CustomBlockItem(Blocks.PERFECTED_LAVA_GENERATOR, "§dPerfected Lava Generator"));
	public static final MachineItem OBSIDIAN_GENERATOR = ITEMS.register(new MachineItem(Blocks.OBSIDIAN_GENERATOR).setDescription("§7Cools lava down to obsidian."));
	public static final MachineItem ITEM_ABSORBER = ITEMS.register(new MachineItem(Blocks.ITEM_ABSORBER).setDescription("§7Absorbes nearby items and stores them internal."));
	public static final MachineItem EXPERIENCE_ABSORBER = ITEMS.register(new MachineItem(Blocks.EXPERIENCE_ABSORBER).setDescription("§7Absorbes Experience and stores it for later."));
	public static final MachineItem BASIC_FARMER = ITEMS.register(new MachineItem(Blocks.BASIC_FARMER));
	public static final MachineItem ADVANCED_FARMER = ITEMS.register(new MachineItem(Blocks.ADVANCED_FARMER));
	public static final MachineItem IMPROVED_FARMER = ITEMS.register(new MachineItem(Blocks.IMPROVED_FARMER));
	public static final MachineItem PERFECTED_FARMER = ITEMS.register(new MachineItem(Blocks.PERFECTED_FARMER));
	public static final MachineItem MOB_GRINDER = ITEMS.register(new MachineItem(Blocks.MOB_GRINDER));
	public static final MachineItem BLOCK_BREAKER = ITEMS.register(new MachineItem(Blocks.BLOCK_BREAKER).setDescription("§7Breaks the block in front."));
	public static final MachineItem QUARRY = ITEMS.register(new MachineItem(Blocks.QUARRY));
	public static final MachineItem BEDROCK_BREAKER = ITEMS.register(new MachineItem(Blocks.BEDROCK_BREAKER));
	public static final MachineItem BASIC_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.BASIC_VOID_ORE_MINER));
	public static final MachineItem ADVANCED_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.ADVANCED_VOID_ORE_MINER));
	public static final MachineItem IMPROVED_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.IMPROVED_VOID_ORE_MINER));
	public static final MachineItem PERFECTED_VOID_ORE_MINER = ITEMS.register(new MachineItem(Blocks.PERFECTED_VOID_ORE_MINER));
	public static final MachineItem DYE_PRESS = ITEMS.register(new MachineItem(Blocks.DYE_PRESS));
	public static final MachineItem DYE_MIXER = ITEMS.register(new MachineItem(Blocks.DYE_MIXER));
	public static final MachineItem TRADING_MACHINE = ITEMS.register(new MachineItem(Blocks.TRADING_MACHINE).setDescription("§7Has to be placed above the workstation of the trading villager."));
	public static final MachineItem ENCHANTER = ITEMS.register(new MachineItem(Blocks.ENCHANTER).setDescription("§7Removed enchantments from books and stores them into an item.", "§7Can enchant gold ingots into magic metal."));
	public static final MachineItem DISENCHANTER = ITEMS.register(new MachineItem(Blocks.DISENCHANTER).setDescription("§7Removes enchantments from an item and stores them onto a book."));
	public static final MachineItem ENCHANTMENT_COMBINER = ITEMS.register(new MachineItem(Blocks.ENCHANTMENT_COMBINER).setDescription("§7Combines the enchantments of two books and stores all in one.", "§7Two enchantments of the same level will be combined with the level increased by one."));
	public static final CustomBlockItem COMPUTER = ITEMS.register(new CustomBlockItem(Blocks.COMPUTER, "§dComputer"));
	public static final CustomBlockItem CHUNK_LOADER = ITEMS.register(new CustomBlockItem(Blocks.CHUNK_LOADER, "§dChunk Loader"));
	public static final MachineItem HYPER_FURNACE_1 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_1));
	public static final MachineItem HYPER_FURNACE_2 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_2));
	public static final MachineItem HYPER_FURNACE_3 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_3));
	public static final MachineItem HYPER_FURNACE_4 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_4));
	public static final MachineItem HYPER_FURNACE_5 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_5));
	public static final MachineItem HYPER_FURNACE_6 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_6));
	public static final MachineItem HYPER_FURNACE_7 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_7));
	public static final MachineItem HYPER_FURNACE_8 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_8));
	public static final MachineItem HYPER_FURNACE_9 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_9));
	public static final MachineItem HYPER_FURNACE_10 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_10));
	public static final MachineItem HYPER_FURNACE_11 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_11));
	public static final MachineItem HYPER_FURNACE_12 = ITEMS.register(new MachineItem(Blocks.HYPER_FURNACE_12));
	public static final MachineItem BASIC_CRUSHER = ITEMS.register(new MachineItem(Blocks.BASIC_CRUSHER));
	public static final MachineItem ADVANCED_CRUSHER = ITEMS.register(new MachineItem(Blocks.ADVANCED_CRUSHER));
	public static final MachineItem IMPROVED_CRUSHER = ITEMS.register(new MachineItem(Blocks.IMPROVED_CRUSHER));
	public static final MachineItem PERFECTED_CRUSHER = ITEMS.register(new MachineItem(Blocks.PERFECTED_CRUSHER));
	public static final MachineItem BASIC_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.BASIC_MINERAL_EXTRACTOR));
	public static final MachineItem ADVANCED_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.ADVANCED_MINERAL_EXTRACTOR));
	public static final MachineItem IMPROVED_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.IMPROVED_MINERAL_EXTRACTOR));
	public static final MachineItem PERFECTED_MINERAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.PERFECTED_MINERAL_EXTRACTOR));
	public static final MachineItem BASIC_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.BASIC_AERIAL_EXTRACTOR));
	public static final MachineItem ADVANCED_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.ADVANCED_AERIAL_EXTRACTOR));
	public static final MachineItem IMPROVED_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.IMPROVED_AERIAL_EXTRACTOR));
	public static final MachineItem PERFECTED_AERIAL_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.PERFECTED_AERIAL_EXTRACTOR));
	public static final MachineItem BASIC_SAWMILL = ITEMS.register(new MachineItem(Blocks.BASIC_SAWMILL));
	public static final MachineItem ADVANCED_SAWMILL = ITEMS.register(new MachineItem(Blocks.ADVANCED_SAWMILL));
	public static final MachineItem IMPROVED_SAWMILL = ITEMS.register(new MachineItem(Blocks.IMPROVED_SAWMILL));
	public static final MachineItem PERFECTED_SAWMILL = ITEMS.register(new MachineItem(Blocks.PERFECTED_SAWMILL));
	public static final MachineItem BASIC_SMELTERY = ITEMS.register(new MachineItem(Blocks.BASIC_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
	public static final MachineItem ADVANCED_SMELTERY = ITEMS.register(new MachineItem(Blocks.ADVANCED_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
	public static final MachineItem IMPROVED_SMELTERY = ITEMS.register(new MachineItem(Blocks.IMPROVED_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
	public static final MachineItem PERFECTED_SMELTERY = ITEMS.register(new MachineItem(Blocks.PERFECTED_SMELTERY).setDescription("§7Smelts or alloys materials into ingots."));
	public static final MachineItem BASIC_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.BASIC_COMPRESSOR));
	public static final MachineItem ADVANCED_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.ADVANCED_COMPRESSOR));
	public static final MachineItem IMPROVED_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.IMPROVED_COMPRESSOR));
	public static final MachineItem PERFECTED_COMPRESSOR = ITEMS.register(new MachineItem(Blocks.PERFECTED_COMPRESSOR));
	public static final MachineItem BASIC_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.BASIC_BIO_PRESS));
	public static final MachineItem ADVANCED_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.ADVANCED_BIO_PRESS));
	public static final MachineItem IMPROVED_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.IMPROVED_BIO_PRESS));
	public static final MachineItem PERFECTED_BIO_PRESS = ITEMS.register(new MachineItem(Blocks.PERFECTED_BIO_PRESS));
	public static final MachineItem LATEX_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.LATEX_EXTRACTOR));
	public static final MachineItem BASIC_CRAFTER = ITEMS.register(new MachineItem(Blocks.BASIC_CRAFTER));
	public static final MachineItem ADVANCED_CRAFTER = ITEMS.register(new MachineItem(Blocks.ADVANCED_CRAFTER));
	public static final MachineItem IMPROVED_CRAFTER = ITEMS.register(new MachineItem(Blocks.IMPROVED_CRAFTER));
	public static final MachineItem PERFECTED_CRAFTER = ITEMS.register(new MachineItem(Blocks.PERFECTED_CRAFTER));
	public static final MachineItem BASIC_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.BASIC_CRAFTING_FACTORY));
	public static final MachineItem ADVANCED_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.ADVANCED_CRAFTING_FACTORY));
	public static final MachineItem IMPROVED_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.IMPROVED_CRAFTING_FACTORY));
	public static final MachineItem PERFECTED_CRAFTING_FACTORY = ITEMS.register(new MachineItem(Blocks.PERFECTED_CRAFTING_FACTORY));
	public static final MachineItem BASIC_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.BASIC_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
	public static final MachineItem ADVANCED_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.ADVANCED_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
	public static final MachineItem IMPROVED_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.IMPROVED_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
	public static final MachineItem PERFECTED_CARBON_PRESS = ITEMS.register(new MachineItem(Blocks.PERFECTED_CARBON_PRESS).setDescription("§7Compresses coal-related materials to even denser materials."));
	public static final MachineItem BASIC_FREEZER = ITEMS.register(new MachineItem(Blocks.BASIC_FREEZER));
	public static final MachineItem ADVANCED_FREEZER = ITEMS.register(new MachineItem(Blocks.ADVANCED_FREEZER));
	public static final MachineItem IMPROVED_FREEZER = ITEMS.register(new MachineItem(Blocks.IMPROVED_FREEZER));
	public static final MachineItem PERFECTED_FREEZER = ITEMS.register(new MachineItem(Blocks.PERFECTED_FREEZER));
	public static final MachineItem HONEY_EXTRACTOR = ITEMS.register(new MachineItem(Blocks.HONEY_EXTRACTOR));
	public static final MachineItem STAR_MAKER = ITEMS.register(new MachineItem(Blocks.STAR_MAKER));
	public static final MachineItem ROCKET_ASSEMBLER = ITEMS.register(new MachineItem(Blocks.ROCKET_ASSEMBLER));

	// Generators
	public static final GeneratorItem FURNACE_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.FURNACE_GENERATOR).setDescription("§7Generates Energy when placed above a Furnace, Blastfurnace or Smoker."));
	public static final GeneratorItem THERMO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.THERMO_GENERATOR).setDescription("§7Generates Energy from Lava."));
	public static final GeneratorItem BASIC_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.BASIC_SOLAR_PANEL));
	public static final GeneratorItem ADVANCED_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.ADVANCED_SOLAR_PANEL));
	public static final GeneratorItem IMPROVED_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.IMPROVED_SOLAR_PANEL));
	public static final GeneratorItem PERFECTED_SOLAR_PANEL = ITEMS.register(new GeneratorItem(Blocks.PERFECTED_SOLAR_PANEL));
	public static final GeneratorItem BASIC_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.BASIC_BIO_GENERATOR));
	public static final GeneratorItem ADVANCED_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.ADVANCED_BIO_GENERATOR));
	public static final GeneratorItem IMPROVED_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.IMPROVED_BIO_GENERATOR));
	public static final GeneratorItem PERFECTED_BIO_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.PERFECTED_BIO_GENERATOR));
	public static final GeneratorItem DIESEL_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.DIESEL_GENERATOR));
	public static final GeneratorItem LIGHTNING_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.LIGHTNING_GENERATOR));
	public static final GeneratorItem CREATIVE_GENERATOR = ITEMS.register(new GeneratorItem(Blocks.CREATIVE_GENERATOR));

	// Storage
	public static final BackpackItem BROWN_BACKPACK = ITEMS.register(new BackpackItem(Blocks.BROWN_BACKPACK));
	public static final BackpackItem COPPER_BACKPACK = ITEMS.register(new BackpackItem(Blocks.COPPER_BACKPACK));
	public static final BackpackItem IRON_BACKPACK = ITEMS.register(new BackpackItem(Blocks.IRON_BACKPACK));
	public static final BackpackItem GOLDEN_BACKPACK = ITEMS.register(new BackpackItem(Blocks.GOLDEN_BACKPACK));
	public static final BackpackItem DIAMOND_BACKPACK = ITEMS.register(new BackpackItem(Blocks.DIAMOND_BACKPACK));
	public static final BackpackItem NETHERITE_BACKPACK = ITEMS.register(new BackpackItem(Blocks.NETHERITE_BACKPACK));
	public static final ItemStorageCrateItem OAK_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.OAK_STORAGE_CRATE, "§fOak Storage Crate"));
	public static final ItemStorageCrateItem SPRUCE_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.SPRUCE_STORAGE_CRATE, "§fSpruce Storage Crate"));
	public static final ItemStorageCrateItem BIRCH_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.BIRCH_STORAGE_CRATE, "§fBirch Storage Crate"));
	public static final ItemStorageCrateItem JUNGLE_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.JUNGLE_STORAGE_CRATE, "§fJungle Storage Crate"));
	public static final ItemStorageCrateItem ACACIA_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.ACACIA_STORAGE_CRATE, "§fAcacia Storage Crate"));
	public static final ItemStorageCrateItem DARK_OAK_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.DARK_OAK_STORAGE_CRATE, "§fDark Oak Storage Crate"));
	public static final ItemStorageCrateItem MANGROVE_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.MANGROVE_STORAGE_CRATE, "§fMangrove Storage Crate"));
	public static final ItemStorageCrateItem CHERRY_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.CHERRY_STORAGE_CRATE, "§fCherry Storage Crate"));
	public static final ItemStorageCrateItem PALE_OAK_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.PALE_OAK_STORAGE_CRATE, "§fPale Oak Storage Crate"));
	public static final ItemStorageCrateItem BAMBOO_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.BAMBOO_STORAGE_CRATE, "§fBamboo Storage Crate"));
	public static final ItemStorageCrateItem CRIMSON_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.CRIMSON_STORAGE_CRATE, "§fCrimson Storage Crate"));
	public static final ItemStorageCrateItem WARPED_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.WARPED_STORAGE_CRATE, "§fWarped Storage Crate"));
	public static final ItemStorageCrateItem IRON_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.IRON_STORAGE_CRATE, "§fIron Storage Crate"));
	public static final ItemStorageCrateItem GOLDEN_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.GOLDEN_STORAGE_CRATE, "§eGolden Storage Crate"));
	public static final ItemStorageCrateItem DIAMOND_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.DIAMOND_STORAGE_CRATE, "§bDiamond Storage Crate"));
	public static final ItemStorageCrateItem EMERALD_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.EMERALD_STORAGE_CRATE, "§aEmerald Storage Crate"));
	public static final ItemStorageCrateItem CLOWNFISH_STORAGE_CRATE = ITEMS.register(new ItemStorageCrateItem(Blocks.CLOWNFISH_STORAGE_SKULL_BLOCK, "§9Clownfish Storage Crate"));
	public static final CustomBlockItem CLOWNFISH_CHEST = ITEMS.register(new CustomBlockItem(Blocks.CLOWNFISH_CHEST, ClownfishChest.TITLE));
	public static final EnderLinkedBlockItem<Inventory, EnderChestBlock> ENDER_CHEST = ITEMS.register(new EnderLinkedBlockItem<>(Blocks.ENDER_CHEST, "§eEnder Chest"));
	public static final CustomBlockItem TRASHCAN = ITEMS.register(new CustomBlockItem(Blocks.TRASHCAN, "§fTrashcan"));
	public static final CustomBlockItem ITEM_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_PIPE, "§fItem Pipe"));
	public static final CustomBlockItem ITEM_EXTRACTION_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_EXTRACTION_PIPE, "§fItem Extraction Pipe"));
	public static final CustomBlockItem ITEM_INSERTION_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_INSERTION_PIPE, "§fItem Insertion Pipe"));
	public static final CustomBlockItem ITEM_SPEED_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_SPEED_PIPE, "§fItem Speed Pipe"));
	public static final CustomBlockItem ITEM_DIRECTIONAL_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_DIRECTIONAL_PIPE, "§fItem Directional Pipe"));
	public static final CustomBlockItem ITEM_VACUUM_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_VACUUM_PIPE, "§fItem Vacuum Pipe"));
	public static final CustomBlockItem ITEM_VOID_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_VOID_PIPE, "§fItem Void Pipe"));
	public static final CustomBlockItem ITEM_FILTER_PIPE = ITEMS.register(new CustomBlockItem(Blocks.ITEM_FILTER_PIPE, "§fItem Filter Pipe"));
	public static final CustomItem STORAGE_CASING = ITEMS.register(new CustomItem("§fStorage Casing", HeadTextures.STORAGE_CASING));
	public static final CustomBlockItem STORAGE_CONNECTOR = ITEMS.register(new CustomBlockItem(Blocks.ITEM_STORAGE_CONNECTOR, "§fStorage Connector"));
	public static final CustomBlockItem STORAGE_MAIN_COMPONENT = ITEMS.register(new CustomBlockItem(Blocks.ITEM_STORAGE_MAIN_COMPONENT, "§fStorage Main Component").setDescription("§7Required for each network."));
	public static final CustomBlockItem STORAGE_IMPORTER = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_IMPORTER, "§eStorage Importer").setDescription("§7Import items from adjacent inventory holders and storage blocks."));
	public static final CustomBlockItem STORAGE_EXPORTER = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_EXPORTER, "§eStorage Exporter").setDescription("§7Exports items to adjacent inventory holders and storage blocks."));
	public static final CustomBlockItem STORAGE_READER = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_READER, "§eStorage Reader").setDescription("§7Emits an redstone signal if the filter matches any item in the facing storage."));
	public static final CustomBlockItem ENDER_ACCESSOR = ITEMS.register(new CustomBlockItem(Blocks.ENDER_ACCESSOR, "§bEnder Accessor").setDescription("§7Acts as a port to the enderchest of the owner of this block."));
	public static final CustomBlockItem STORAGE_MONITOR = ITEMS.register(new CustomBlockItem(Blocks.STORAGE_MONITOR, "§bStorage Monitor").setDescription("§7Lists all items in the network and lets the user interact with them."));
	public static final ItemFilterItem BLOCK_FILTER = ITEMS.register(ItemFilterItem.of("§fBlock Filter", "§7Filters items that are also blocks.", Material::isBlock));
	public static final ItemFilterItem NONSTACKABLE_FILTER = ITEMS.register(ItemFilterItem.of("§fNon-Stackable Filter", "§7Filters items that cannot be stacked.", m -> m.getMaxStackSize() == 1));
	public static final ItemFilterItem DAMAGEABLE_FILTER = ITEMS.register(ItemFilterItem.of("§fDamageable Filter", "§7Filters items that can be damaged.", m -> m.getMaxDurability() > 0));
	public static final ItemFilterItem DAMAGED_FILTER = ITEMS.register(new ItemFilterItem("§fDamaged Filter", "§7Filters items that are damaged.", stack -> stack.getItemMeta() instanceof Damageable damageable && damageable.hasDamage()));
	public static final ItemFilterItem NBT_FILTER = ITEMS.register(new ItemFilterItem("§fNBT Filter", "§7Filters items that store currently nbt data.", stack -> MinecraftItemHelper.getNumberComponents(stack) > 0));
	public static final ItemFilterItem ENCHANTED_FILTER = ITEMS.register(new ItemFilterItem("§fEnchanted Filter", "§7Filters enchanted items.", stack -> !stack.getEnchantments().isEmpty()));
	public static final ItemFilterItem FOOD_FILTER = ITEMS.register(ItemFilterItem.of("§fFood Filter", "§7Filters items that can be eaten.", Material::isEdible));
	public static final ItemFilterItem COMPOSTER_FILTER = ITEMS.register(ItemFilterItem.of("§fComposter Filter", "§7Filters items that can be composted.", Material::isCompostable));
	public static final ItemFilterItem FLAMMABLE_FILTER = ITEMS.register(ItemFilterItem.of("§fFlammable Filter", "§fFilters blocks that can catch fire.", Material::isFlammable));
	public static final ItemFilterItem BURNABLE_FILTER = ITEMS.register(ItemFilterItem.of("§fBurnable Filter", "§fFilters blocks that can burn.", Material::isBurnable));
	public static final ItemFilterItem FUEL_FILTER = ITEMS.register(ItemFilterItem.of("fFuel Filter", "§7Filters fuel items.", Material::isFuel));
	public static final ItemFilterItem RECORD_FILTER = ITEMS.register(ItemFilterItem.of("fFuel Filter", "§7Filters music discs.", Material::isRecord));
	public static final ItemFilterItem COOKIES_FILTER = ITEMS.register(new ItemFilterItem("§fCookies Filter", "§7Filters items that were added by the §6Cookies §7plugin.", ItemUtils::isCustomItem));

	// Fluids
	public static final TankItem BASIC_TANK = ITEMS.register(new TankItem(Blocks.BASIC_TANK));
	public static final TankItem ADVANCED_TANK = ITEMS.register(new TankItem(Blocks.ADVANCED_TANK));
	public static final TankItem IMPROVED_TANK = ITEMS.register(new TankItem(Blocks.IMPROVED_TANK));
	public static final TankItem PERFECTED_TANK = ITEMS.register(new TankItem(Blocks.PERFECTED_TANK));
	public static final MachineItem FLUID_PUMP = ITEMS.register(new MachineItem(Blocks.FLUID_PUMP));
	public static final MachineItem OIL_PUMP = ITEMS.register(new MachineItem(Blocks.OIL_PUMP));
	public static final MachineItem OIL_REFINERY = ITEMS.register(new MachineItem(Blocks.OIL_REFINERY));
	public static final EnderLinkedBlockItem<Fluid, EnderTankBlock> ENDER_TANK = ITEMS.register(new EnderLinkedBlockItem<>(Blocks.ENDER_TANK, "§eEnder Tank"));
	public static final CustomBlockItem WASTE_BARREL = ITEMS.register(new CustomBlockItem(Blocks.WASTE_BARREL, "§fWaste Barrel"));

	// Energy
	public static final BatteryItem BATTERY_RED = ITEMS.register(new BatteryItem(Blocks.BATTERY_RED, "§fRed Battery"));
	public static final BatteryItem BATTERY_ORANGE = ITEMS.register(new BatteryItem(Blocks.BATTERY_ORANGE, "§fOrange Battery"));
	public static final BatteryItem BATTERY_YELLOW = ITEMS.register(new BatteryItem(Blocks.BATTERY_YELLOW, "§fYellow Battery"));
	public static final BatteryItem BATTERY_GREEN = ITEMS.register(new BatteryItem(Blocks.BATTERY_GREEN, "§fGreen Battery"));
	public static final BatteryItem BATTERY_CYAN = ITEMS.register(new BatteryItem(Blocks.BATTERY_CYAN, "§fCyan Battery"));
	public static final BatteryItem BATTERY_BLUE = ITEMS.register(new BatteryItem(Blocks.BATTERY_BLUE, "§fBlue Battery"));
	public static final BatteryItem BATTERY_PURPLE = ITEMS.register(new BatteryItem(Blocks.BATTERY_PURPLE, "§fPurple Battery"));
	public static final BatteryItem BATTERY_BLACK = ITEMS.register(new BatteryItem(Blocks.BATTERY_BLACK, "§fBlack Battery"));
	public static final CustomBlockItem LED_PURPLE = ITEMS.register(new CustomBlockItem(Blocks.LED_PURPLE, "§5LED"));
	public static final CustomBlockItem LED_BLUE = ITEMS.register(new CustomBlockItem(Blocks.LED_BLUE, "§9LED"));
	public static final CustomBlockItem LED_CYAN = ITEMS.register(new CustomBlockItem(Blocks.LED_CYAN, "§3LED"));
	public static final CustomBlockItem LED_GREEN = ITEMS.register(new CustomBlockItem(Blocks.LED_GREEN, "§aLED"));
	public static final CustomBlockItem LED_ORANGE = ITEMS.register(new CustomBlockItem(Blocks.LED_ORANGE, "§6LED"));
	public static final CustomBlockItem LED_RED = ITEMS.register(new CustomBlockItem(Blocks.LED_RED, "§cLED"));
	public static final EnderLinkedBlockItem<Holder<Integer>, TesseractBlock> TESSERACT = ITEMS.register(new EnderLinkedBlockItem<>(Blocks.TESSERACT, "§dTesseract"));

	// Magic
	public static final MagicMetal MAGIC_METAL = ITEMS.register(new MagicMetal());
	public static final CustomItem BASIC_WAND = ITEMS.register(new CustomItem("§fBasic Wand", Material.BLAZE_ROD));
	public static final CustomItem MAGIC_WAND = ITEMS.register(new CustomItem("§fMagic Wand", Material.BLAZE_ROD));
	public static final CustomItem POWERED_WAND = ITEMS.register(new CustomItem("§ePowered Wand", Material.BLAZE_ROD));
	public static final CustomItem EMPOWERED_WAND = ITEMS.register(new CustomItem("§bEmpowered Wand", Material.BLAZE_ROD));
	public static final CustomItem FLOWERING_FLOWER = ITEMS.register(new CustomItem("§eFlowering Flower", Material.OXEYE_DAISY));
	public static final CustomItem FOREST_BUNDLE = ITEMS.register(new CustomItem("§eForest Bundle", Material.DARK_OAK_SAPLING));
	public static final CustomItem FOOD_BUNDLE = ITEMS.register(new CustomItem("§eFood Bundle", Material.RABBIT_STEW));
	public static final CustomItem ORGANIC_MATTER = ITEMS.register(new CustomItem("§eOrganic Matter", HeadTextures.SATURATED_MOSS));
	public static final CustomItem PEARLSTONE = ITEMS.register(new CustomItem("§ePearlstone", HeadTextures.RAINBOW_GEM));
	public static final CustomItem OVERWORLD_ARTEFACT = ITEMS.register(new CustomItem("§bOverworld Artefact", HeadTextures.GLOBE));
	public static final CustomItem MAGIC_ARTEFACT = ITEMS.register(new CustomItem("§bMagic Artefact", Material.ENCHANTED_GOLDEN_APPLE));
	public static final CustomItem NETHER_CORE = ITEMS.register(new CustomItem("§eNether Core", HeadTextures.NETHER_CORE));
	public static final CustomItem NETHER_CRYSTAL = ITEMS.register(new CustomItem("§bNether Crystal", Material.NETHER_STAR));
	public static final CustomItem ENDER_CORE = ITEMS.register(new CustomItem("§eEnder Core", HeadTextures.ENDER_CORE));
	public static final CustomItem ENDER_CRYSTAL = ITEMS.register(new CustomItem("§bEnder Crystal", HeadTextures.ENDER_CRYSTAL));
	public static final CustomItem FLESH_CLUMP = ITEMS.register(new CustomItem("§eFlesh Clump", HeadTextures.FLESH_CLUMP));
	public static final CustomItem FIRE_CRYSTAL = ITEMS.register(new CustomItem("§eFire Crystal", HeadTextures.FIRE_CRYSTAL));
	public static final CustomItem MONSTER_CORE = ITEMS.register(new CustomItem("§bMonster Core", HeadTextures.MONSTER_CORE));
	public static final CustomItem SPAWNER_CRYSTAL = ITEMS.register(new CustomItem("§dSpawner Crystal", HeadTextures.EMPTY_SPAWNER));
	public static final SpawnerWand SPAWNER_WAND = ITEMS.register(new SpawnerWand());
	public static final DragonEye DRAGON_EYE = ITEMS.register(new DragonEye());
	public static final CustomItem STICKY_GOO = ITEMS.register(new CustomItem("§fSticky Goo", HeadTextures.STICKY_GOO));
	public static final CustomItem COMPACT_PEBBLE = ITEMS.register(new CustomItem("§eCompact Pebble", Material.FLINT));
	public static final CustomItem COMPACT = ITEMS.register(new CustomItem("§bFat", HeadTextures.COMPACT));
	public static final MiniaturizingWand MINIATURIZING_WAND = ITEMS.register(new MiniaturizingWand());

	// Fun
	public static final List<CardItem> PLAYING_CARDS = ITEMS.register(Stream.of(CardItem.Color.values()).flatMap(color -> Stream.of(CardItem.Rank.values()).map(rank -> new CardItem(color, rank))));
	public static final CardPileItem CARD_PILE = ITEMS.register(new CardPileItem());

	// Plants
	public static final CustomBlockItem PLANT_FRUIT = ITEMS.register(new CustomBlockItem(Blocks.PLANT_FRUIT, "§fPlant Fruit"));
	public static final CustomBlockItem PLANT_STEM = ITEMS.register(new CustomBlockItem(Blocks.PLANT_STEM, "§fPlant Stem"));
	public static final CustomBlockItem RED_APPLE = ITEMS.register(new CustomBlockItem(Blocks.RED_APPLE, "§fRed Apple"));
	public static final CustomBlockItem RED_APPLE_SAPLING = ITEMS.register(new CustomBlockItem(Blocks.RED_APPLE_SAPLING, "§fRed Apple Sapling"));
	public static final CustomBlockItem GREEN_APPLE = ITEMS.register(new CustomBlockItem(Blocks.GREEN_APPLE, "§fGreen Apple"));
	public static final CustomBlockItem GREEN_APPLE_SAPLING = ITEMS.register(new CustomBlockItem(Blocks.GREEN_APPLE_SAPLING, "§fGreen Apple Sapling"));
	public static final CustomBlockItem ORANGE = ITEMS.register(new CustomBlockItem(Blocks.ORANGE, "§fOrange"));
	public static final CustomBlockItem ORANGE_SAPLING = ITEMS.register(new CustomBlockItem(Blocks.ORANGE_SAPLING, "§fOrange Sapling"));

	// Plushies
	public static final CustomBlockItem FALSE_SYMMETRY_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.FALSE_SYMMETRY_PLUSHIE, "§eFalseSymmetry"));
	public static final CustomBlockItem XISUMA_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.XISUMA_PLUSHIE, "§aXisuma"));
	public static final CustomBlockItem ZEDAPH_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ZEDAPH_PLUSHIE, "§6Zedaph"));
	public static final CustomBlockItem XB_CRAFTED_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.XB_CRAFTED_PLUSHIE, "§cXBCrafted"));
	public static final CustomBlockItem WELSKNIGHT_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.WELSKNIGHT_PLUSHIE, "§9Welsknight"));
	public static final CustomBlockItem TIN_FOIL_CHEF_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.TIN_FOIL_CHEF_PLUSHIE, "§7TinFoilChef"));
	public static final CustomBlockItem MUMBO_JUMBO_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.MUMBO_JUMBO_PLUSHIE, "§8MumboJumbo"));
	public static final CustomBlockItem JOE_HILLS_SAYS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.JOE_HILLS_SAYS_PLUSHIE, "§bJoeHillsSays"));
	public static final CustomBlockItem HYPNOTIZD_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.HYPNOTIZD_PLUSHIE, "§8Hypnotized"));
	public static final CustomBlockItem GRIAN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GRIAN_PLUSHIE, "§cGrian"));
	public static final CustomBlockItem GUINEA_PIG_GRIAN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GUINEA_PIG_GRIAN_PLUSHIE, "§6Guinea-Pig Grian"));
	public static final CustomBlockItem POULTRY_MAN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.POULTRY_MAN_PLUSHIE, "§6Poultry Man"));
	public static final CustomBlockItem VINTAGE_BEEF_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.VINTAGE_BEEF_PLUSHIE, "§9VintageBeef"));
	public static final CustomBlockItem GOOD_TIMES_WITH_SCAR_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GOOD_TIMES_WITH_SCAR_PLUSHIE, "§6GoodTimesWithScar"));
	public static final CustomBlockItem JELLIE_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.JELLIE_PLUSHIE, "§7Jellie"));
	public static final CustomBlockItem KERALIS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.KERALIS_PLUSHIE, "§eKeralis"));
	public static final CustomBlockItem FRENCHRALIS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.FRENCHRALIS_PLUSHIE, "§6Frenchralis"));
	public static final CustomBlockItem I_JEVIN_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.I_JEVIN_PLUSHIE, "§bIJevin"));
	public static final CustomBlockItem ETHOSLAB_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ETHOSLAB_PLUSHIE, "§8Ethoslab"));
	public static final CustomBlockItem ISKALL85_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ISKALL85_PLUSHIE, "§aIskall85"));
	public static final CustomBlockItem TANGO_TEK_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.TANGO_TEK_PLUSHIE, "§cTangoTek"));
	public static final CustomBlockItem IMPULS_SV_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.IMPULS_SV_PLUSHIE, "§eImpulseSV"));
	public static final CustomBlockItem STRESSMONSTER101_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.STRESSMONSTER101_PLUSHIE, "§dStressMonster101"));
	public static final CustomBlockItem BDOUBLEO100_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.BDOUBLEO100_PLUSHIE, "§9BDoubleO100"));
	public static final CustomBlockItem BDOUBLEO100_SMILE_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.BDOUBLEO100_SMILE_PLUSHIE, "§bBDoubleO100"));
	public static final CustomBlockItem DOCM77_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.DOCM77_PLUSHIE, "§aDocm77"));
	public static final CustomBlockItem CUBFAN135_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.CUBFAN135_PLUSHIE, "§fCubfan135"));
	public static final CustomBlockItem DOCTOR_CUBFAN135_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.DOCTOR_CUBFAN135_PLUSHIE, "§fDoctor Cubfan135"));
	public static final CustomBlockItem PHARAO_CUBFAN135_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.PHARAO_CUBFAN135_PLUSHIE, "§6Pharao Cubfan135"));
	public static final CustomBlockItem ZOMBIE_CLEO_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.ZOMBIE_CLEO_PLUSHIE, "§6ZombieCleo"));
	public static final CustomBlockItem REN_THE_DOG_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.REN_THE_DOG_PLUSHIE, "§cRenTheDog"));
	public static final CustomBlockItem REN_BOB_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.REN_BOB_PLUSHIE, "§cRenBob"));
	public static final CustomBlockItem PEARLESCENT_MOON_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.PEARLESCENT_MOON_PLUSHIE, "§bPearlescentMoon"));
	public static final CustomBlockItem GEMINI_TAY_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.GEMINI_TAY_PLUSHIE, "§aGeminiTay"));
	public static final CustomBlockItem JOEYGRACEFFA_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.JOEYGRACEFFA_PLUSHIE, "§3JoeyGraceffa"));
	public static final CustomBlockItem SHUBBLE_YT_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SHUBBLE_YT_PLUSHIE, "§cShubbleYT"));
	public static final CustomBlockItem SOLIDARITY_GAMING_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SOLIDARITY_GAMING_PLUSHIE, "§6SolidarityGaming"));
	public static final CustomBlockItem SMALISHBEANS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SMALISHBEANS_PLUSHIE, "§cSmalishbeans"));
	public static final CustomBlockItem SMAJOR1995_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.SMAJOR1995_PLUSHIE, "§bSmajor1995"));
	public static final CustomBlockItem PIXLRIFFS_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.PIXLRIFFS_PLUSHIE, "§9Pixlriffs"));
	public static final CustomBlockItem MYTHICAL_SAUSAGE_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.MYTHICAL_SAUSAGE_PLUSHIE, "§cMythicalSausage"));
	public static final CustomBlockItem LDS_SHADOWLADY_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.LDS_SHADOWLADY_PLUSHIE, "§dLDSShadowlady"));
	public static final CustomBlockItem KATHERINEELIZ_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.KATHERINEELIZ_PLUSHIE, "§bKatherineeliz"));
	public static final CustomBlockItem FWHIP_PLUSHIE = ITEMS.register(new CustomBlockItem(Blocks.FWHIP_PLUSHIE, "§cfWhip"));

	public static void init() {

		ITEMS.forEach(Configurable::configure);
	}


	public static AbstractCustomItem getCustomItemFromIdentifier(String identifier) {
		return ITEMS.filterFirst(item -> item.getIdentifier().equals(identifier));
	}


	public static AbstractCustomItem getCustomItemFromStack(ItemStack stack) {
		String identifier = AbstractCustomItem.getIdentifier(stack);
		if(identifier == null)
			return null;

		return getCustomItemFromIdentifier(identifier);
	}


	public static AbstractCustomItem getCustomItemFromCustomBlock(AbstractCustomBlock block) {
		return getCustomItemFromIdentifier(block.getIdentifier());
	}


	public static boolean isInstanceOf(ItemStack stack, Class<?> clazz) {
		AbstractCustomItem item = getCustomItemFromStack(stack);
		return item != null && clazz.isAssignableFrom(item.getClass());
	}

	private static CachingSupplier<List<PlayerRegister>> PLAYER_REGISTER_SUPPLIER = new CachingSupplier<>(() -> ITEMS.filterByClass(PlayerRegister.class));

	public static List<PlayerRegister> getPlayerRegister() {
		return PLAYER_REGISTER_SUPPLIER.get();
	}

}
