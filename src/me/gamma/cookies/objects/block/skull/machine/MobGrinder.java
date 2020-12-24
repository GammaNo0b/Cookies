
package me.gamma.cookies.objects.block.skull.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.objects.IItemSupplier;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.recipe.CookieRecipe;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;
import me.gamma.headdrops.list.EntityHeads;



public class MobGrinder extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable {

	public static final int MAX_UPGRADES = 15;

	public static Set<Location> locations = new HashSet<>();

	private static final ByteProperty TICK_COUNT = ByteProperty.create("ticks");
	private static final ByteProperty RANGE = ByteProperty.create("range");
	private static final ByteProperty SHARPNESS = ByteProperty.create("sharpness");
	private static final ByteProperty LOOTING = ByteProperty.create("looting");
	private static final ByteProperty FIREASPECT = ByteProperty.create("fireaspect");
	private static final ByteProperty BEHEADING = ByteProperty.create("beheading");


	public MobGrinder() {
		register();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MOB_GRINDER;
	}


	@Override
	public String getDisplayName() {
		return "§cMob §6Grinder";
	}


	@Override
	public String getRegistryName() {
		return "mob_grinder";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Grinds Mobs for you!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("DSD", "GMG", "ISI");
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('P', Material.DIAMOND_SWORD);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('M', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('S', Material.IRON_SWORD);
		return recipe;
	}


	@Override
	public int getRows() {
		return 3;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		TICK_COUNT.storeEmpty(meta);
		for(ByteProperty property : getProperties()) {
			property.storeEmpty(meta);
		}
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		TICK_COUNT.transfer(usedItem.getItemMeta(), block);
		for(ByteProperty property : getProperties()) {
			property.transfer(usedItem.getItemMeta(), block);
		}
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack stack = super.onBlockBreak(player, block, event);
		ItemMeta meta = stack.getItemMeta();
		TICK_COUNT.transfer(block, meta);
		ByteProperty[] properties = getProperties();
		IItemSupplier[] upgrades = getUpgrades();
		for(int i = 0; i < properties.length; i++) {
			byte amount = properties[i].fetch(block);
			if(amount > 0) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemBuilder(upgrades[i].get()).setAmount(amount).build());
			}
		}
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		ByteProperty[] properties = getProperties();
		for(int i = 0; i < properties.length; i++) {
			ItemStack stack = gui.getItem(11 + i);
			properties[i].store(block, (byte) (stack != null && stack.getType() != Material.AIR && stack.getType() != Material.BARRIER ? stack.getAmount() : 0));
		}
		block.update();
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		for(int i = 0; i < 9; i++) {
			if(i != 4)
				gui.setItem(i, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").build());
			gui.setItem(18 + i, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").build());
		}
		gui.setItem(9, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(10, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(16, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(17, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(" ").build());
		gui.setItem(20, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§cSharpness").build());
		gui.setItem(21, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9Looting").build());
		gui.setItem(22, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Fireaspect").build());
		gui.setItem(23, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§2Beheading").build());
		gui.setItem(24, new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setName("§eRange").build());
		IItemSupplier[] upgrades = getUpgrades();
		ByteProperty[] properties = getProperties();
		for(int i = 0; i < upgrades.length; i++) {
			ItemStack stack = new ItemBuilder(upgrades[i].get()).setAmount(properties[i].fetch(block)).build();
			if(stack.getAmount() > 0) {
				gui.setItem(11 + i, stack);
			} else {
				gui.setItem(11 + i, new ItemBuilder(Material.BARRIER).setName(" ").build());
			}
		}
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		int upgrade = slot - 11;
		IItemSupplier[] upgrades = getUpgrades();
		if(upgrade >= 0 && upgrade < upgrades.length) {
			ItemStack stack = upgrades[upgrade].get();
			ItemStack cursor = event.getCursor();
			ItemStack current = event.getCurrentItem();
			if(cursor == null || cursor.getType() == Material.AIR) {
				if(current == null || current.getType() != Material.BARRIER) {
					Utilities.giveItemToPlayer(clicker, current);
					gui.setItem(slot, new ItemBuilder(Material.BARRIER).setName(" ").build());
				}
				return true;
			}
			if(current == null || current.getType() == Material.AIR || current.getType() == Material.BARRIER) {
				if(CookieRecipe.sameIngredient(cursor, stack)) {
					int amount = Math.min(MAX_UPGRADES, cursor.getAmount());
					cursor.setAmount(cursor.getAmount() - amount);
					gui.setItem(slot, new ItemBuilder(stack).setAmount(amount).build());
					return true;
				}
			}
			if(current != null && CookieRecipe.sameIngredients(stack, current, cursor)) {
				int canstore = MAX_UPGRADES - current.getAmount();
				if(canstore <= 0) {
					return true;
				}
				int store = Math.min(canstore, cursor.getAmount());
				cursor.setAmount(cursor.getAmount() - store);
				current.setAmount(current.getAmount() + store);
				return true;
			}
		}
		return true;
	}


	@SuppressWarnings("deprecation")
	@Override
	public void tick(TileState block) {
		byte ticks = TICK_COUNT.fetch(block);
		if(++ticks > 20) {
			ticks = 0;
			BlockFace facing = ((Skull) block).getRotation().getOppositeFace();
			int range = 1 + RANGE.fetch(block);
			double delta = range / 2.0D;
			Vector direction = facing.getDirection();
			Vector origin = direction.clone().multiply(range / 2.0D + 0.5D);
			for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D).add(origin), delta, 1.0D, delta)) {
				if(entity instanceof LivingEntity) {
					LivingEntity living = (LivingEntity) entity;
					if(living.isDead())
						continue;
					LOOTING.transfer(block, living);
					BEHEADING.transfer(block, living);
					int fireticks = FIREASPECT.fetch(block);
					if(fireticks > 0) {
						living.setFireTicks(fireticks * 40);
					}
					living.damage((SHARPNESS.fetch(block) + 1) * 2.5D);
					Bukkit.getScheduler().runTaskLater(Cookies.INSTANCE, () -> {
						try {
							LOOTING.remove(living);
							BEHEADING.remove(living);
						} catch(Throwable t) {}
					}, 20);
				}
			}
		}
		TICK_COUNT.store(block, ticks);
		block.update();
	}


	public String vectorToString(Vector vector) {
		return String.format("%.3f | %.3f | %.3f", vector.getX(), vector.getY(), vector.getZ());
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onEntityKill(EntityDeathEvent event) {
				LivingEntity entity = event.getEntity();
				Byte looting = LOOTING.fetch(entity);
				List<ItemStack> drops = new ArrayList<ItemStack>(event.getDrops());
				if(looting != null)
					for(int i = 0; i < looting; i++)
						event.getDrops().addAll(drops);
				Byte beheading = BEHEADING.fetch(entity);
				if(beheading != null && Cookies.isInstalled("HeadDrops")) {
					ItemStack head = EntityHeads.getEntityHeadFromEntity(entity, (double) beheading / (double) MAX_UPGRADES);
					if(head != null) {
						event.getDrops().add(head);
					}
				}
			}

		};
	}


	private static ByteProperty[] getProperties() {
		return new ByteProperty[] {
			SHARPNESS, LOOTING, FIREASPECT, BEHEADING, RANGE
		};
	}


	private static IItemSupplier[] getUpgrades() {
		return new IItemSupplier[] {
			CustomItemSetup.SHARPNESS_MOBGRINDER_UPGRADE, CustomItemSetup.LOOTING_MOBGRINDER_UPGRADE, CustomItemSetup.FIREASPECT_MOBGRINDER_UPGRADE, CustomItemSetup.BEHEADING_MOBGRINDER_UPGRADE, CustomItemSetup.RANGE_MOBGRINDER_UPGRADE
		};
	}

}
