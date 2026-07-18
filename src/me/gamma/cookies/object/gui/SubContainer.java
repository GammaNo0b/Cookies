
package me.gamma.cookies.object.gui;


import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;



/**
 * Only use a subset of slots of a container.
 */
public class SubContainer implements Container {

	private final Container container;
	private final int[] slots;

	public SubContainer(Container container, int[] slots) {
		this.container = container;
		this.slots = slots;
	}


	@Override
	public void clearContent() {
		for(int slot : this.slots)
			this.container.setItem(slot, ItemStack.EMPTY);
	}


	@Override
	public int getContainerSize() {
		return this.slots.length;
	}


	@Override
	public boolean isEmpty() {
		for(int i = 0; i < this.slots.length; ++i)
			if(!this.getItem(i).isEmpty())
				return false;

		return true;
	}


	@Override
	public ItemStack getItem(int slot) {
		return this.container.getItem(this.slots[slot]);
	}


	@Override
	public ItemStack removeItem(int slot, int count) {
		return this.container.removeItem(this.slots[slot], count);
	}


	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.container.removeItemNoUpdate(this.slots[slot]);
	}


	@Override
	public void setItem(int slot, ItemStack itemStack) {
		this.container.setItem(this.slots[slot], itemStack);
	}


	@Override
	public int getMaxStackSize() {
		return this.container.getMaxStackSize();
	}


	@Override
	public void setChanged() {
		this.container.setChanged();
	}


	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}


	@Override
	public List<ItemStack> getContents() {
		List<ItemStack> allContents = this.container.getContents();
		List<ItemStack> contents = NonNullList.createWithCapacity(this.slots.length);
		for(int slot : this.slots)
			contents.add(allContents.get(this.slots[slot]));
		return contents;
	}


	@Override
	public void onOpen(CraftHumanEntity who) {
		this.container.onOpen(who);
	}


	@Override
	public void onClose(CraftHumanEntity who) {
		this.container.onClose(who);
	}


	@Override
	public List<HumanEntity> getViewers() {
		return this.container.getViewers();
	}


	@Override
	public InventoryHolder getOwner() {
		return this.container.getOwner();
	}


	@Override
	public void setMaxStackSize(int size) {
		this.container.setMaxStackSize(size);
	}


	@Override
	public Location getLocation() {
		return this.container.getLocation();
	}

}
