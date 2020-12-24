
package me.gamma.cookies.objects.block.skull.machine;


import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.property.VectorProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public class Quarry extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable {

	public static Set<Location> locations = new HashSet<>();

	private static final VectorProperty BREAK_POS = VectorProperty.create("BreakPos");
	
	
	public Quarry() {
		register();
	}

	@Override
	public String getBlockTexture() {
		return HeadTextures.QUARRY;
	}


	@Override
	public String getDisplayName() {
		return "§aQuarry";
	}


	@Override
	public String getRegistryName() {
		return "quarry";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Mines down to bedrock!");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.CUSTOM);
		recipe.setShape("DPD", "GBG", "IEI");
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('P', Material.DIAMOND_PICKAXE);
		recipe.setIngredient('G', Material.GOLD_INGOT);
		recipe.setIngredient('B', CustomBlockSetup.BLOCK_BREAKER.createDefaultItemStack());
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('E', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
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
		return ConfigValues.QUARRY_DELAY;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	private int getWidth() {
		return ConfigValues.QUARRY_RANGE;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		BREAK_POS.store(block, new Vector(0, block.getY() - 1, 0));
		for(int i = 0; i < 4; i++) {
			createToolProperty(i).transfer(usedItem.getItemMeta(), block);
		}
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack item = super.onBlockBreak(player, block, event);
		ItemMeta meta = item.getItemMeta();
		for(int i = 0; i < 4; i++) {
			createToolProperty(i).transfer(block, meta);
		}
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack item = super.createDefaultItemStack();
		ItemMeta meta = item.getItemMeta();
		for(int i = 0; i < 4; i++) {
			createToolProperty(i).storeEmpty(meta);
		}
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = Bukkit.createInventory(null, InventoryType.HOPPER, this.getDisplayName());
		gui.setItem(this.getIdentifierSlot(), super.createMainGui(player, block).getItem(this.getIdentifierSlot()));
		for(int i = 0; i < 4; i++) {
			gui.setItem(i, createToolProperty(i).fetch(block));
		}
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState inventoryHolder, Inventory gui, InventoryClickEvent event) {
		return event.getSlot() == 4;
	}


	@Override
	public void onInventoryClose(Player player, TileState inventoryHolder, Inventory gui, InventoryCloseEvent event) {
		for(int i = 0; i < 4; i++) {
			createToolProperty(i).store(inventoryHolder, gui.getItem(i));
		}
		inventoryHolder.update();
	}


	@Override
	public void tick(TileState block) {
		Vector pos = BREAK_POS.fetch(block);
		if(pos == null) {
			return;
		}
		Block breakBlock = block.getLocation().clone().add(pos).add(0, -block.getY(), 0).getBlock();
		Collection<ItemStack> drops = null;
		for(int i = 0; i < 4; i++) {
			ItemStack tool = createToolProperty(i).fetch(block);
			drops = breakBlock.getDrops(tool);
			if(!drops.isEmpty()) {
				break;
			}
		}
		if(drops == null) {
			drops = breakBlock.getDrops();
		}
		if(breakBlock.getState() instanceof BlockInventoryHolder) {
			drops.addAll(Arrays.asList(((BlockInventoryHolder) breakBlock.getState()).getInventory().getContents()));
			((BlockInventoryHolder) breakBlock.getState()).getInventory().clear();
		}
		for(ItemStack drop : drops) {
			if(drop != null) {
				ItemStack rest = Utilities.transferItem(drop, block.getBlock(), Utilities.faces);
				if(rest != null) {
					block.getWorld().dropItem(block.getLocation(), rest);
				}
			}
		}
		breakBlock.setType(Material.AIR);
		do {
			pos = nextPos(pos);
			if(pos == null) {
				break;
			}
		} while(BlockBreaker.bannedBreakBlocks.contains(block.getLocation().clone().add(pos).add(0, -block.getY(), 0).getBlock().getType()));
		BREAK_POS.store(block, pos);
		block.update();
	}


	private Vector nextPos(Vector current) {
		int x = current.getBlockX();
		int y = current.getBlockY();
		int z = current.getBlockZ();
		if(x >= this.getWidth()) {
			x = 0;
			if(z >= this.getWidth()) {
				z = 0;
				if(y > 0) {
					y--;
				} else {
					return null;
				}
			} else {
				z++;
			}
		} else {
			x++;
		}
		return new Vector(x, y, z);
	}


	private static ItemStackProperty createToolProperty(int slot) {
		return ItemStackProperty.create("tool" + slot);
	}

}
