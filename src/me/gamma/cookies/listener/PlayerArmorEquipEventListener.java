
package me.gamma.cookies.listener;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.event.PlayerArmorEquipEvent.EquipMethod;
import me.gamma.cookies.object.item.armor.ArmorType;
import me.gamma.cookies.util.ItemUtils;



public class PlayerArmorEquipEventListener implements Listener {

	private static final List<Material> blockedBlocks = Arrays.asList(Material.FURNACE, Material.CHEST, Material.TRAPPED_CHEST, Material.BEACON, Material.DISPENSER, Material.DROPPER, Material.HOPPER, Material.CRAFTING_TABLE, Material.ENCHANTING_TABLE, Material.ENDER_CHEST, Material.ANVIL, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.LIGHT_GRAY_BED, Material.WHITE_BED, Material.YELLOW_BED, Material.OAK_FENCE_GATE, Material.SPRUCE_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.IRON_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.SPRUCE_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.SPRUCE_BUTTON, Material.OAK_BUTTON, Material.DARK_OAK_BUTTON, Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.STONE_BUTTON, Material.OAK_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.IRON_TRAPDOOR, Material.REPEATER, Material.COMPARATOR, Material.OAK_FENCE, Material.SPRUCE_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.DARK_OAK_FENCE, Material.ACACIA_FENCE, Material.NETHER_BRICK_FENCE, Material.BREWING_STAND, Material.CAULDRON, Material.DARK_OAK_SIGN, Material.OAK_SIGN, Material.DARK_OAK_WALL_SIGN, Material.OAK_WALL_SIGN, Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN, Material.SPRUCE_SIGN, Material.SPRUCE_WALL_SIGN, Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN, Material.LEVER, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED, Material.LIGHT_BLUE_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED, Material.PURPLE_BED, Material.RED_BED, Material.LIGHT_GRAY_BED, Material.WHITE_BED, Material.YELLOW_BED, Material.DAYLIGHT_DETECTOR);

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event) {
		if(event.isCancelled() || event.getAction() == InventoryAction.NOTHING || (event.getSlotType() != SlotType.ARMOR && event.getSlotType() != SlotType.CONTAINER && event.getSlotType() != SlotType.QUICKBAR) || event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER || (event.getInventory().getType() != InventoryType.CRAFTING && event.getInventory().getType() != InventoryType.PLAYER) || !(event.getWhoClicked() instanceof Player))
			return;
		boolean shift = event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT;
		boolean number = event.getClick() == ClickType.NUMBER_KEY;
		Player player = (Player) event.getWhoClicked();
		ArmorType type = ArmorType.get(shift ? event.getCurrentItem() : event.getCursor());
		if(!shift && type != null && event.getRawSlot() != type.getRawSlot())
			return;
		if(shift) {
			if(type != null) {
				boolean equipping = true;
				if(event.getRawSlot() == type.getRawSlot()) {
					equipping = false;
				}
				if(equipping == ItemUtils.isEmpty(type.getArmor(player.getInventory()))) {
					PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(player, type, EquipMethod.SHIFT_CLICK, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null);
					Bukkit.getPluginManager().callEvent(equipEvent);
					if(equipEvent.isCancelled()) {
						event.setCancelled(true);
					}
				}
			}
		} else {
			ItemStack oldArmor = event.getCurrentItem();
			ItemStack newArmor = event.getCursor();
			if(number) {
				if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
					ItemStack hotbarStack = event.getClickedInventory().getItem(event.getHotbarButton());
					if(!ItemUtils.isEmpty(hotbarStack)) {
						type = ArmorType.get(hotbarStack);
						newArmor = hotbarStack;
						oldArmor = event.getClickedInventory().getItem(event.getSlot());
					} else {
						type = ArmorType.get(!ItemUtils.isEmpty(oldArmor) ? oldArmor : newArmor);
					}
				}
			} else if(ItemUtils.isEmpty(event.getCursor()) && !ItemUtils.isEmpty(event.getCurrentItem()))
				type = ArmorType.get(oldArmor);
			if(type != null && event.getRawSlot() == type.getRawSlot()) {
				EquipMethod method = EquipMethod.PICK_DROP;
				if(event.getAction() == InventoryAction.HOTBAR_SWAP || number)
					method = EquipMethod.HOTBAR_SWAP;
				PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(player, type, method, oldArmor, newArmor);
				Bukkit.getPluginManager().callEvent(equipEvent);
				if(equipEvent.isCancelled()) {
					event.setCancelled(true);
				}
			}
		}
	}


	@EventHandler
	public void inventoryDragEvent(InventoryDragEvent event) {
		ArmorType type = ArmorType.get(event.getOldCursor());
		if(event.getRawSlots().isEmpty() || !(event.getWhoClicked() instanceof Player))
			return;
		if(type != null && type.getRawSlot() == event.getRawSlots().stream().findFirst().orElse(0).intValue()) {
			PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent((Player) event.getWhoClicked(), type, EquipMethod.DRAG, null, event.getOldCursor());
			Bukkit.getPluginManager().callEvent(equipEvent);
			if(equipEvent.isCancelled()) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
				for(Material m : blockedBlocks)
					if(event.getClickedBlock().getType() == m)
						return;
			}
			ArmorType type = ArmorType.get(event.getItem());
			if(type != null && ItemUtils.isEmpty(type.getArmor(event.getPlayer().getInventory()))) {
				PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(event.getPlayer(), type, EquipMethod.HOTBAR, null, event.getItem());
				Bukkit.getPluginManager().callEvent(equipEvent);
				if(equipEvent.isCancelled()) {
					event.setCancelled(true);
					event.getPlayer().updateInventory();
				}
			}
		}
	}


	@EventHandler
	public void itemBreakEvent(PlayerItemBreakEvent event) {
		ArmorType type = ArmorType.get(event.getBrokenItem());
		if(type != null) {
			Player player = event.getPlayer();
			ItemStack cloned = event.getBrokenItem().clone();
			PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(player, type, EquipMethod.BROKE, event.getBrokenItem(), null);
			Bukkit.getPluginManager().callEvent(equipEvent);
			if(equipEvent.isCancelled()) {
				ItemMeta meta = cloned.getItemMeta();
				if(meta instanceof Damageable) {
					Damageable damageable = (Damageable) meta;
					damageable.setDamage(damageable.getDamage() - 1);
					cloned.setItemMeta((ItemMeta) damageable);
				}
				type.setArmor(player.getInventory(), cloned);
			}
		}
	}


	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent event) {
		for(ItemStack stack : event.getEntity().getInventory().getArmorContents()) {
			if(!ItemUtils.isEmpty(stack)) {
				Bukkit.getPluginManager().callEvent(new PlayerArmorEquipEvent(event.getEntity(), ArmorType.get(stack), EquipMethod.DEATH, stack, null));
			}
		}
	}


	@EventHandler
	public void dispenseArmorEvent(BlockDispenseArmorEvent event) {
		final ItemStack stack = event.getItem();
		final ArmorType type = ArmorType.get(event.getItem());
		if(type != null && event.getTargetEntity() instanceof Player && ItemUtils.isEmpty(type.getArmor(((Player) event.getTargetEntity()).getInventory()))) {
			final Player player = (Player) event.getTargetEntity();
			PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(player, type, EquipMethod.DISPENSER, null, stack);
			Bukkit.getPluginManager().callEvent(equipEvent);
			if(equipEvent.isCancelled()) {
				event.setCancelled(true);
			}
		}
	}

}
