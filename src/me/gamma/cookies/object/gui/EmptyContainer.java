
package me.gamma.cookies.object.gui;


import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;



public class EmptyContainer implements Container {

	private final BlockInventoryHolder holder;

	public EmptyContainer(BlockInventoryHolder holder) {
		this.holder = holder;
	}


	@Override
	public void clearContent() {}


	@Override
	public int getContainerSize() {
		return 1;
	}


	@Override
	public boolean isEmpty() {
		return true;
	}


	@Override
	public ItemStack getItem(int slot) {
		return ItemStack.EMPTY;
	}


	@Override
	public ItemStack removeItem(int slot, int count) {
		return ItemStack.EMPTY;
	}


	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ItemStack.EMPTY;
	}


	@Override
	public void setItem(int slot, ItemStack itemStack) {}


	@Override
	public int getMaxStackSize() {
		return MAX_STACK;
	}


	@Override
	public void setChanged() {}


	@Override
	public boolean stillValid(Player player) {
		return false;
	}


	@Override
	public List<ItemStack> getContents() {
		return NonNullList.create();
	}


	@Override
	public void onOpen(CraftHumanEntity who) {}


	@Override
	public void onClose(CraftHumanEntity who) {}


	@Override
	public List<HumanEntity> getViewers() {
		return List.of();
	}


	@Override
	public InventoryHolder getOwner() {
		return this.holder;
	}


	@Override
	public void setMaxStackSize(int size) {}


	@Override
	public Location getLocation() {
		return this.holder.getBlock().getLocation();
	}

}
