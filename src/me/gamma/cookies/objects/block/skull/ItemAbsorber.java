
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utilities;



public class ItemAbsorber extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable {

	public static Set<Location> locations = new HashSet<>();

	public static final ByteProperty EXTRACT_SIDES = ByteProperty.create("extractsides");

	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_ABSORBER;
	}


	@Override
	public String getDisplayName() {
		return "§cItem §6Absorber";
	}


	@Override
	public String getIdentifier() {
		return "item_absorber";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Absorbes nearby Items and stores them if possible.");
	}


	@Override
	public long getDelay() {
		return ConfigValues.ITEM_ABSORBER_DELAY;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && this.isInstanceOf(block) && block instanceof Skull;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("IGI", "CME", "III");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('C', CustomBlockSetup.STORAGE_BLOCK_TIER_2.createDefaultItemStack());
		recipe.setIngredient('E', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.MACHINE_CASING.createDefaultItemStack());
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
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack item = super.createDefaultItemStack();
		ItemMeta meta = item.getItemMeta();
		EXTRACT_SIDES.store(meta, (byte) 63);
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		byte sides = EXTRACT_SIDES.fetch(block);
		final int[] slots = new int[] {
			1, 2, 3, 5, 6, 7
		};
		for(int i = 0; i < Utilities.faces.length; i++) {
			if((sides >> i & 1) == 1) {
				gui.setItem(slots[i], new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§2" + Utilities.faces[i].name() + " §8- §aActivated").build());
			} else {
				gui.setItem(slots[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§4" + Utilities.faces[i].name() + " §8- §cDectivated").build());
			}
		}
		return gui;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		EXTRACT_SIDES.transfer(usedItem.getItemMeta(), block);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(isOwner(block, event.getPlayer())) {
			ItemStack item = this.createDefaultItemStack();
			ItemMeta meta = item.getItemMeta();
			EXTRACT_SIDES.transfer(block, meta);
			item.setItemMeta(meta);
			return item;
		} else {
			event.getPlayer().sendMessage("§cYou are not owning this Item Absorber!");
			return null;
		}
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot > 0 && slot < 8 && slot != 4) {
			int slots[] = new int[] {
				-1, 0, 1, 2, -1, 3, 4, 5, -1
			};
			configureAbsorberBlockSide(block, slots[slot], gui.getItem(slot).getType().name().contains("RED_"));
			if(gui.getItem(slot).getType() == Material.RED_STAINED_GLASS_PANE) {
				gui.setItem(slot, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§2" + Utilities.faces[slots[slot]].name() + " §8- §aActivated").build());
			} else {
				gui.setItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§4" + Utilities.faces[slots[slot]].name() + " §8- §cDectivated").build());
			}
			block.update();
		}
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public void tick(TileState block) {
		int range = ConfigValues.ITEM_ABSORBER_RANGE;
		for(Entity entity : block.getLocation().getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D), range, range, range)) {
			if(entity instanceof Item) {
				Item item = (Item) entity;
				ItemStack rest = Utilities.transferItem(item.getItemStack(), block.getBlock(), this.getActiveSides(block));
				if(rest != null) {
					if(rest.getType() != Material.AIR) {
						item.setItemStack(rest);
						return;
					}
				}
				item.remove();
			}
		}
	}


	private BlockFace[] getActiveSides(TileState block) {
		BlockFace[] faces = new BlockFace[6];
		byte sides = EXTRACT_SIDES.fetch(block);
		for(int i = 0; i < faces.length; i++) {
			if((sides >> i & 1) == 1) {
				faces[i] = Utilities.faces[i];
			} else {
				faces[i] = null;
			}
		}
		return faces;
	}


	private void configureAbsorberBlockSide(TileState block, int side, boolean activated) {
		try {
			byte sides = EXTRACT_SIDES.fetch(block);
			if(activated) {
				sides |= 1 << side;
			} else {
				sides &= ~(1 << side);
			}
			EXTRACT_SIDES.store(block, sides);
			block.update();
		} catch(NullPointerException | IllegalArgumentException e) {}

	}

}
