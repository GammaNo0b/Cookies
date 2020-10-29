
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.ItemStackProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ItemBuilder;



public class ClownfishChest extends AbstractGuiProvidingSkullBlock {

	public static final String BACKPACK_TITLE = "§6Clownfish §9Backpack";

	@Override
	public String getBlockTexture() {
		return HeadTextures.CLOWNFISH_CHEST;
	}


	@Override
	public String getDisplayName() {
		return "§6Clownfish §9Chest";
	}


	@Override
	public String getIdentifier() {
		return "clownfish_chest";
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Stores Items like a normal Chest BUT...", "§7  - has more slots (35 in total)", "§7  - works as backpack");
	}


	@Override
	public int getRows() {
		return 4;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_CHEST_OPEN;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.STORAGE, RecipeType.CUSTOM);
		recipe.setShape("", "WFW", "PCP");
		recipe.setIngredient('W', new ItemStack(Material.BLUE_WOOL));
		recipe.setIngredient('F', new ItemBuilder(Material.TROPICAL_FISH).setName("Clownfish").build());
		recipe.setIngredient('P', new ItemStack(Material.BIRCH_PLANKS));
		recipe.setIngredient('C', CustomBlockSetup.STORAGE_BLOCK_TIER_1.createDefaultItemStack());
		return recipe;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		for(int i = 0; i < gui.getSize(); i++) {
			if(i != this.getIdentifierSlot()) {
				gui.setItem(i, createSlotProperty(i).fetch(block));
			}
		}
		return gui;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack item = super.createDefaultItemStack();
		ItemMeta meta = item.getItemMeta();
		for(int i = 0; i < this.getRows() * 9; i++) {
			if(i != this.getIdentifierSlot()) {
				createSlotProperty(i).storeEmpty(meta);
			}
		}
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		for(int i = 0; i < this.getRows() * 9; i++) {
			if(i != this.getIdentifierSlot()) {
				createSlotProperty(i).transfer(usedItem.getItemMeta(), block);
			}
		}
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		ItemStack item = super.onBlockBreak(player, block, event);
		ItemMeta meta = item.getItemMeta();
		for(int i = 0; i < this.getRows() * 9; i++) {
			if(i != this.getIdentifierSlot()) {
				createSlotProperty(i).transfer(block, meta);
			}
		}
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		player.playSound(player.getLocation(), Sound.BLOCK_WOOL_HIT, 0.2F, 1);
		player.openInventory(this.createBackpackGUI(player, stack));
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		return event.getSlot() == this.getIdentifierSlot();
	}


	@Override
	public void onInventoryClose(Player player, TileState block, Inventory gui, InventoryCloseEvent event) {
		for(int i = 0; i < gui.getSize(); i++) {
			if(i != this.getIdentifierSlot()) {
				createSlotProperty(i).store(block, gui.getItem(i));
			}
		}
		block.update();
	}


	public boolean onBackpackGuiInteract(Player player, ItemStack backpack, Inventory gui, InventoryClickEvent event) {
		return event.getSlot() == this.getIdentifierSlot();
	}


	public boolean onBackpackPlayerInventoryInteract(Player player, ItemStack backpack, Inventory gui, InventoryClickEvent event) {
		return event.getSlot() == player.getInventory().getHeldItemSlot();
	}


	public void onBackpackClose(Player player, ItemStack backpack, Inventory gui, InventoryCloseEvent event) {
		ItemMeta meta = backpack.getItemMeta();
		for(int i = 0; i < gui.getSize(); i++) {
			if(i != this.getIdentifierSlot()) {
				createSlotProperty(i).store(meta, gui.getItem(i));
			}
		}
		backpack.setItemMeta(meta);
		player.getInventory().setItemInMainHand(backpack);
	}


	private Inventory createBackpackGUI(Player player, ItemStack backpack) {
		Inventory gui = Bukkit.createInventory(null, this.getRows() * 9, BACKPACK_TITLE);
		gui.setItem(this.getIdentifierSlot(), new ItemBuilder(Material.TROPICAL_FISH).setName(BACKPACK_TITLE).build());
		for(int i = 0; i < gui.getSize(); i++) {
			if(i != this.getIdentifierSlot()) {
				gui.setItem(i, createSlotProperty(i).fetch(backpack.getItemMeta()));
			}
		}
		return gui;
	}


	private static ItemStackProperty createSlotProperty(int slot) {
		return ItemStackProperty.create("item" + slot);
	}

}
