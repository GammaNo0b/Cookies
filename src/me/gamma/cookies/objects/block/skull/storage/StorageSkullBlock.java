
package me.gamma.cookies.objects.block.skull.storage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.StorageProvider;
import me.gamma.cookies.objects.block.skull.AbstractGuiProvidingSkullBlock;
import me.gamma.cookies.objects.property.BigItemStackProperty;
import me.gamma.cookies.objects.property.ByteProperty;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.property.Properties;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.BigItemStack;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.MathHelper;
import me.gamma.cookies.util.Utilities;



public class StorageSkullBlock extends AbstractGuiProvidingSkullBlock implements StorageProvider, StorageComponent {

	public static final ByteProperty STORAGE_TIER = Properties.STORAGE_TIER;
	public static final IntegerProperty STORAGE_CAPACITY = Properties.STORAGE_CAPACITY;

	public static boolean isStorageBlock(TileState block) {
		return STORAGE_CAPACITY.isPropertyOf(block);
	}

	private String texture;
	private int tier;
	private ItemStack materialIngredient;
	private ItemStack middleIngredient;
	private ItemStack centerIngredient;
	private ItemStack previousIngredient;

	public StorageSkullBlock(String texture, int tier, ItemStack materialIngredient, ItemStack middleIngredient, ItemStack centerIngredient, ItemStack previousIngredient) {
		this.texture = texture;
		this.tier = tier;
		this.materialIngredient = materialIngredient;
		this.middleIngredient = middleIngredient;
		this.centerIngredient = centerIngredient;
		this.previousIngredient = previousIngredient;

	}


	@Override
	public String getBlockTexture() {
		return texture;
	}


	public int getTier() {
		return tier;
	}


	@Override
	public int getCapacity() {
		return (int) MathHelper.intpow(4, this.getTier() - 1);
	}


	@Override
	public String getDisplayName() {
		return "§5Storage §dBlock §cTier §4" + this.getTier();
	}


	@Override
	public String getRegistryName() {
		return "storage_block_tier_" + this.getTier();
	}


	@Override
	public int getIdentifierSlot() {
		return this.getCapacity() <= 4 ? 8 : 4;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_BARREL_OPEN;
	}


	@Override
	public int getRows() {
		return Math.min(4, (this.getCapacity() + 7) / 8);
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.STORAGE, RecipeType.CUSTOM);
		recipe.setShape("ISI", "MCM", "ISI");
		recipe.setIngredient('I', materialIngredient);
		recipe.setIngredient('M', middleIngredient);
		recipe.setIngredient('C', centerIngredient);
		recipe.setIngredient('S', previousIngredient);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack item = super.createDefaultItemStack();
		ItemMeta meta = item.getItemMeta();
		meta.setLore(Arrays.asList("§cTier§8: §4" + getTier(), "§3Capacity§8: §b" + this.getCapacity()));
		STORAGE_TIER.store(meta, (byte) this.getTier());
		STORAGE_CAPACITY.store(meta, this.getCapacity());
		for(BigItemStackProperty property : this.createProperties()) {
			property.storeEmpty(meta);
		}
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.PAPER).setName("§6Page§8: §e0").build());
		return this.constructPage(block, gui, 0);
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		STORAGE_TIER.transfer(usedItem.getItemMeta(), block);
		STORAGE_CAPACITY.transfer(usedItem.getItemMeta(), block);
		for(BigItemStackProperty property : this.createProperties()) {
			property.transfer(usedItem.getItemMeta(), block);
		}
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(!this.canAccess(block, event.getPlayer())) {
			return null;
		}
		ItemStack item = super.onBlockBreak(player, block, event);
		ItemMeta meta = item.getItemMeta();
		STORAGE_TIER.transfer(block, meta);
		STORAGE_CAPACITY.transfer(block, meta);
		for(BigItemStackProperty property : this.createProperties()) {
			property.transfer(block, meta);
		}
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int[] itemSlots = new int[] {
			0, 1, 2, 3, -1, 4, 5, 6, 7, 8, 9, 10, 11, -1, 12, 13, 14, 15, 16, 17, 18, 19, -1, 20, 21, 22, 23, 24, 25, 26, 27, -1, 28, 29, 30, 31
		};
		if(event.getSlot() < 0 || event.getSlot() > itemSlots.length)
			return true;
		int index = itemSlots[event.getSlot()];
		if(index >= 0) {
			ItemStack item = event.getCurrentItem();
			if(item != null && item.getType() != Material.BARRIER) {
				Utilities.giveItemToPlayer(player, StorageProvider.requestItem(block, index));
				int page = Integer.parseInt(gui.getItem(gui.getSize() - 5).getItemMeta().getDisplayName().substring(12));
				player.openInventory(this.constructPage(block, this.createMainGui(player, block), page));
			}
		} else if(event.getSlot() == gui.getSize() - 5) {
			int page = Integer.parseInt(gui.getItem(gui.getSize() - 5).getItemMeta().getDisplayName().substring(12));
			player.openInventory(this.constructPage(block, this.createMainGui(player, block), event.isLeftClick() ? ++page : --page));
		}
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if(item != null && item.getType() != Material.AIR && item.getType() != Material.BARRIER) {
			int amount = item.getAmount();
			ItemStack rest = StorageProvider.storeItem(block, item);
			if(rest == null) {
				clicker.getInventory().removeItem(item);
			} else {
				ItemStack remove = item.clone();
				remove.setAmount(amount - rest.getAmount());
				clicker.getInventory().removeItem(remove);
			}
			gui = this.createMainGui(clicker, block);
			int page = Integer.parseInt(gui.getItem(gui.getSize() - 5).getItemMeta().getDisplayName().substring(12));
			clicker.openInventory(this.constructPage(block, this.createMainGui(clicker, block), page));
		}
		return true;
	}


	private Inventory constructPage(TileState block, Inventory gui, int page) {
		List<List<BigItemStack>> pages = this.getPages(StorageProvider.getContent(block), 32);
		page = (page + pages.size()) % pages.size();
		gui.setItem(gui.getSize() - 5, new ItemBuilder(Material.PAPER).setName("§6Page§8: §e" + page).build());
		List<BigItemStack> content = pages.get(page);
		int[] itemSlots = new int[] {
			0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 35
		};
		for(int i = 0; i < content.size() && i < 32; i++) {
			BigItemStack stack = content.get(i);
			if(stack.isEmpty()) {
				gui.setItem(itemSlots[i], new ItemBuilder(Material.BARRIER).setName("§4Empty").addLore("§3Amount§8: §9-").build());
			} else {
				ItemStack item = stack.getStack().clone();
				String name = "§cItem§8: " + (item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : "§6" + Utilities.toCapitalWords(item.getType().toString().replace('_', ' ')));
				gui.setItem(itemSlots[i], new ItemBuilder(item).setName(name).addLore("§3Amount§8: §b" + stack.getAmount()).build());
			}
		}
		return gui;
	}


	private List<List<BigItemStack>> getPages(List<BigItemStack> content, int size) {
		List<List<BigItemStack>> pages = new ArrayList<>();
		List<BigItemStack> page = new ArrayList<>();
		int index = 0;
		for(BigItemStack stack : content) {
			if(index++ < size) {
				page.add(stack);
			} else {
				index = 1;
				pages.add(new ArrayList<>(page));
				page = new ArrayList<>();
				page.add(stack);
			}
		}
		pages.add(new ArrayList<>(page));
		return pages;
	}


	public static byte getTier(TileState block) {
		return STORAGE_TIER.fetch(block);
	}

}
