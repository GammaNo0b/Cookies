
package me.gamma.cookies.objects.item;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.event.PlayerArmorEquipEvent;



public abstract class AbstractCustomItem {
	
	public static boolean isCustomItem(ItemStack stack) {
		return stack != null && stack.hasItemMeta() && stack.getItemMeta().hasLocalizedName();
	}

	public abstract String getIdentifier();

	public abstract String getDisplayName();


	public List<String> getDescription() {
		return new ArrayList<>();
	}


	public abstract Material getMaterial();

	public abstract Recipe getRecipe();
	
	public int getCustomModelData() {
		return -1;
	}


	public ItemStack createDefaultItemStack() {
		ItemStack stack = new ItemStack(this.getMaterial());
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(this.getDisplayName());
		meta.setLocalizedName(this.getIdentifier());
		int customModelData = this.getCustomModelData();
		if(customModelData >= 0)
			meta.setCustomModelData(customModelData);
		List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
		if(!this.getDescription().isEmpty()) {
			lore.add("");
			lore.addAll(this.getDescription());
		}
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}


	public boolean isInstanceOf(ItemStack stack) {
		return stack != null && stack.getItemMeta() != null && stack.getItemMeta().hasLocalizedName() && this.getIdentifier().equals(stack.getItemMeta().getLocalizedName());
	}


	public boolean hasListener() {
		return this.getCustomListener() != null;
	}


	public Listener getCustomListener() {
		return null;
	}


	public boolean hasTask() {
		return this.getCustomTask() != null;
	}


	public long getTaskDelay() {
		return 0;
	}


	public Runnable getCustomTask() {
		return null;
	}


	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
	}


	public void onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
	}


	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
	}


	public void onBlockLeftClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
	}


	public void onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
	}


	public void onBlockBreak(Player player, ItemStack stack, BlockBreakEvent event) {
	}


	public void onEntityDamage(Player player, ItemStack stack, Entity damaged, EntityDamageByEntityEvent event) {
	}


	public void onEntityKill(Player player, Entity killed, EntityDeathEvent event) {
	}


	public void onPlayerConsumesItem(Player player, ItemStack stack, PlayerItemConsumeEvent event) {
	}


	public void onPlayerArmorEquipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
	}


	public void onPlayerArmorUnequipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
	}

}
