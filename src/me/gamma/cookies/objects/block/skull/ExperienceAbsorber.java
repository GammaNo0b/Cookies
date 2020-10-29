
package me.gamma.cookies.objects.block.skull;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.ItemBuilder;



public class ExperienceAbsorber extends AbstractGuiProvidingSkullBlock implements BlockTicker, Switchable {

	public static Set<Location> locations = new HashSet<>();

	public static IntegerProperty STORED_XP = IntegerProperty.create("storedxp");

	@Override
	public String getBlockTexture() {
		return HeadTextures.EXPERIENCE_ABSORBER;
	}


	@Override
	public String getDisplayName() {
		return "§aExperience §eAbsorber";
	}


	@Override
	public String getIdentifier() {
		return "experience_absorber";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Absorbes Experience and stores it for later.");
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MACHINES, RecipeType.ENGINEER);
		recipe.setShape("IGI", "XME", "III");
		recipe.setIngredient('I', Material.IRON_INGOT);
		recipe.setIngredient('G', Material.GLASS);
		recipe.setIngredient('X', Material.EXPERIENCE_BOTTLE);
		recipe.setIngredient('E', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
		recipe.setIngredient('M', CustomBlockSetup.ADVANCED_MACHINE_CASING.createDefaultItemStack());
		return recipe;
	}


	@Override
	public int getRows() {
		return 3;
	}


	@Override
	public long getDelay() {
		return ConfigValues.EXPERIENCE_ABSORBER_DELAY;
	}


	@Override
	public boolean isActiveOnRedstone() {
		return true;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isBlockPowered(block) && block instanceof Skull;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public int getIdentifierSlot() {
		return 13;
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack item = super.createDefaultItemStack();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.addAll(Arrays.asList("", "§aStored §eXP§8: §d0"));
		meta.setLore(lore);
		STORED_XP.storeEmpty(meta);
		item.setItemMeta(meta);
		return item;
	}


	@Override
	public Inventory createMainGui(Player player, TileState block) {
		Inventory gui = super.createMainGui(player, block);
		setGuiXpInfo(player, block, gui);
		gui.setItem(1, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 1 Point").build());
		gui.setItem(2, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 10 Points").build());
		gui.setItem(6, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 100 Points").build());
		gui.setItem(7, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore all Points").build());
		gui.setItem(10, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 1 Point").build());
		gui.setItem(11, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 10 Points").build());
		gui.setItem(15, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 100 Points").build());
		gui.setItem(16, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop all Points").build());
		gui.setItem(19, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 1 Point").build());
		gui.setItem(20, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 10 Points").build());
		gui.setItem(24, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 100 Points").build());
		gui.setItem(25, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve all Points").build());
		return gui;
	}


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		super.onBlockPlace(player, usedItem, block, event);
		STORED_XP.transfer(usedItem.getItemMeta(), block);
		block.update();
	}


	@Override
	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(isOwner(block, event.getPlayer())) {
			ItemStack item = super.onBlockBreak(player, block, event);
			ItemMeta meta = item.getItemMeta();
			int storedXP = STORED_XP.fetch(block);
			STORED_XP.store(meta, storedXP);
			meta.setLore(Arrays.asList("§aStored §eXP§8: §d" + storedXP));
			item.setItemMeta(meta);
			return item;
		} else {
			event.getPlayer().sendMessage("§cYou are not owning this Storage Monitor!");
			return null;
		}
	}


	@Override
	public void onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			if(isOwner(block, player)) {
				super.onBlockRightClick(player, block, event);
			} else {
				player.sendMessage("§cYou are not owning this Storage Monitor!");
			}
		}
	}


	@Override
	public boolean onMainInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		switch (event.getSlot()) {
			case 1:
				transferXPToAbsorber(clicker, block, 1);
				break;
			case 2:
				transferXPToAbsorber(clicker, block, 10);
				break;
			case 6:
				transferXPToAbsorber(clicker, block, 100);
				break;
			case 7:
				transferXPToAbsorber(clicker, block, clicker.getTotalExperience());
				break;
			case 10:
				dropXP(clicker, block, 1);
				break;
			case 11:
				dropXP(clicker, block, 10);
				break;
			case 15:
				dropXP(clicker, block, 100);
				break;
			case 16:
				dropXP(clicker, block, STORED_XP.fetch(block));
				break;
			case 19:
				transferXPToPlayer(clicker, block, 1);
				break;
			case 20:
				transferXPToPlayer(clicker, block, 10);
				break;
			case 24:
				transferXPToPlayer(clicker, block, 100);
				break;
			case 25:
				transferXPToPlayer(clicker, block, STORED_XP.fetch(block));
				break;
		}
		setGuiXpInfo(clicker, block, event.getInventory());

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player clicker, TileState block, Inventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public void tick(TileState block) {
		int range = ConfigValues.EXPERIENCE_ABSORBER_RANGE;
		for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D), range, range, range)) {
			if(entity instanceof ExperienceOrb) {
				ExperienceOrb orb = (ExperienceOrb) entity;
				addAbsorberBlockXP(block, orb.getExperience());
				orb.remove();
			}
		}
	}


	private void setGuiXpInfo(Player viewer, TileState block, Inventory gui) {
		gui.setItem(4, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§2Stored Exp: §e" + STORED_XP.fetch(block)).build());
		gui.setItem(22, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName("§5Player Exp: §e" + viewer.getTotalExperience()).build());
	}


	private boolean transferXPToPlayer(Player player, TileState block, int amount) {
		if(amount < 0)
			return false;
		if(STORED_XP.fetch(block) >= amount) {
			player.giveExp(amount);
			addAbsorberBlockXP(block, -amount);
			return true;
		} else {
			player.sendMessage("§cNot enough Experience stored!");
			return false;
		}
	}


	private boolean transferXPToAbsorber(Player player, TileState block, int amount) {
		if(amount < 0)
			return false;
		if(player.getTotalExperience() >= amount) {
			player.giveExp(-amount);
			addAbsorberBlockXP(block, amount);
			return true;
		} else {
			player.sendMessage("§cNot enough Experience stored!");
			return false;
		}
	}


	private boolean dropXP(Player player, TileState block, int amount) {
		if(amount < 0)
			return false;
		if(STORED_XP.fetch(block) >= amount) {
			ExperienceOrb orb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
			orb.setExperience(amount);
			addAbsorberBlockXP(block, -amount);
			return true;
		} else {
			player.sendMessage("§cNot enough Experience stored!");
			return false;
		}
	}


	public static void addAbsorberBlockXP(TileState block, int xp) {
		STORED_XP.increase(block, xp);
		block.update();
	}

}
