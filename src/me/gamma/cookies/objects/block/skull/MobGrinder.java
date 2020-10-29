
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;



public class MobGrinder extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable {

	public static Set<Location> locations = new HashSet<>();

	private static final ByteProperty DAMAGE = ByteProperty.create("damage");
	private static final ByteProperty ATTACK_SPEED = ByteProperty.create("attackSpeed");
	private static final ByteProperty TICK_COUNT = ByteProperty.create("ticks");

	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_MONITOR;
	}


	@Override
	public String getDisplayName() {
		return "§cMob §6Grinder";
	}


	@Override
	public String getIdentifier() {
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
		return 1;
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
	public boolean isActiveOnRedstone() {
		return true;
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
		ATTACK_SPEED.storeEmpty(meta);
		DAMAGE.storeEmpty(meta);
		TICK_COUNT.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		ATTACK_SPEED.transfer(usedItem.getItemMeta(), block);
		DAMAGE.transfer(usedItem.getItemMeta(), block);
		TICK_COUNT.transfer(usedItem.getItemMeta(), block);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack stack = super.onBlockBreak(player, block, event);
		ItemMeta meta = stack.getItemMeta();
		ATTACK_SPEED.transfer(block, meta);
		DAMAGE.transfer(block, meta);
		TICK_COUNT.transfer(block, meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		gui.setItem(0, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§cDecrease").build());
		gui.setItem(1, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§4Attack Damage: §c" + DAMAGE.fetch(block) + " §4HP").build());
		gui.setItem(2, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§cIncrease").build());
		gui.setItem(6, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§dDecrease").build());
		gui.setItem(7, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName("§5Attack Speed: §d" + ATTACK_SPEED.fetch(block) + " §5ticks").build());
		gui.setItem(8, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§dIncrease").build());
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		if(event.getSlot() == 0) {
			byte damage = DAMAGE.fetch(block);
			if(--damage < 0) {
				damage = 63;
			}
			DAMAGE.store(block, damage);
			block.update();
			clicker.openInventory(this.createMainGui(clicker, block));
		} else if(event.getSlot() == 2) {
			byte damage = DAMAGE.fetch(block);
			if(++damage > 63) {
				damage = 0;
			}
			DAMAGE.store(block, damage);
			block.update();
			clicker.openInventory(this.createMainGui(clicker, block));
		} else if(event.getSlot() == 6) {
			byte speed = ATTACK_SPEED.fetch(block);
			if((speed -= 5) < 0) {
				speed = 100;
			}
			ATTACK_SPEED.store(block, speed);
			block.update();
			clicker.openInventory(this.createMainGui(clicker, block));
		} else if(event.getSlot() == 8) {
			byte speed = ATTACK_SPEED.fetch(block);
			if((speed += 5) > 100) {
				speed = 0;
			}
			ATTACK_SPEED.store(block, speed);
			block.update();
			clicker.openInventory(this.createMainGui(clicker, block));
		}
		return true;
	}


	@SuppressWarnings("deprecation")
	@Override
	public void tick(TileState block) {
		int ticks = TICK_COUNT.fetch(block);
		if(ticks++ > ATTACK_SPEED.fetch(block)) {
			ticks = 0;
			Block relative = block.getLocation().getBlock().getRelative(((Skull) block).getRotation().getOppositeFace());
			for(Entity entity : block.getWorld().getNearbyEntities(relative.getLocation().add(0.5D, 0.5D, 0.5D), 0.5D, 0.5D, 0.5D)) {
				if(entity instanceof LivingEntity) {
					double damage = DAMAGE.fetch(block);
					if(damage > 0) {
						LivingEntity living = (LivingEntity) entity;
						living.damage(damage);
					}
				}
			}
		}
		TICK_COUNT.store(block, (byte) ticks);
		block.update();
	}

}
