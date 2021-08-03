
package me.gamma.cookies.objects.block;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.objects.IItemSupplier;
import me.gamma.cookies.objects.block.machine.Machine;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.property.StringProperty;



public abstract class AbstractTileStateBlock implements IItemSupplier {

	protected static final StringProperty IDENTIFIER = AbstractCustomItem.IDENTIFIER;

	public abstract String getRegistryName();

	public abstract String getDisplayName();


	public List<String> getDescription() {
		return new ArrayList<>();
	}


	protected abstract Material getMaterial();

	public abstract Recipe getRecipe();


	public List<Recipe> getRecipes() {
		return Arrays.asList(this.getRecipe());
	}


	public boolean isInstanceOf(PersistentDataHolder holder) {
		return this.getRegistryName().equals(IDENTIFIER.fetch(holder));
	}


	public boolean isInstanceOf(TileState block) {
		return isInstanceOf((PersistentDataHolder) block);
	}


	public ItemStack createDefaultItemStack() {
		ItemStack stack = new ItemStack(this.getMaterial());
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(this.getDisplayName());
		List<String> lore = new ArrayList<>();
		if(this instanceof Machine) {
			lore.addAll(((Machine) this).getMachineDescription());
			lore.add("");
		}
		if(!this.getDescription().isEmpty()) {
			lore.addAll(this.getDescription());
			lore.add("");
		}
		if(meta.getLore() != null && !meta.getLore().isEmpty()) {
			lore.addAll(meta.getLore());
		}
		meta.setLore(lore);
		IDENTIFIER.store(meta, getRegistryName());
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public ItemStack get() {
		return this.createDefaultItemStack();
	}


	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		IDENTIFIER.transfer(usedItem.getItemMeta(), block);
		if(this instanceof Ownable)
			((Ownable) this).setOwner(block, player.getUniqueId());
		if(this instanceof BlockRegister)
			((BlockRegister) this).getLocations().add(block.getLocation());
		block.update();
	}


	public ItemStack onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(this instanceof BlockRegister)
			((BlockRegister) this).getLocations().remove(block.getLocation());
		return this.createDefaultItemStack();
	}


	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	public boolean onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	public boolean onBlockRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	public boolean onBlockLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		return false;
	}


	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		return false;
	}


	public boolean onBlockLeftClick(Player player, TileState block, PlayerInteractEvent event) {
		return false;
	}


	public boolean hasCustomListener() {
		return this.getCustomListener() != null;
	}


	public Listener getCustomListener() {
		return null;
	}

}
