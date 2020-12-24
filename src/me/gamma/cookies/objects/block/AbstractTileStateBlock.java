
package me.gamma.cookies.objects.block;


import java.util.ArrayList;
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
import me.gamma.cookies.objects.property.Properties;
import me.gamma.cookies.objects.property.StringProperty;
import me.gamma.cookies.objects.property.UUIDProperty;



public abstract class AbstractTileStateBlock implements IItemSupplier {

	protected static final StringProperty IDENTIFIER = Properties.IDENTIFIER;
	protected static final UUIDProperty OWNER = Properties.OWNER;

	public abstract String getRegistryName();

	public abstract String getDisplayName();


	public List<String> getDescription() {
		return new ArrayList<>();
	}


	protected abstract Material getMaterial();

	public abstract Recipe getRecipe();


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


	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {}


	public void onAirLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {}


	public void onBlockRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {}


	public void onBlockLeftClick(Player player, ItemStack stack, PlayerInteractEvent event) {}


	public void onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {}


	public void onBlockLeftClick(Player player, TileState block, PlayerInteractEvent event) {}


	public Listener getCustomListener() {
		return null;
	}

}
