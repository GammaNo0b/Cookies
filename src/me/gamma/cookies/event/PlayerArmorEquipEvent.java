
package me.gamma.cookies.event;


import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.item.armor.ArmorType;



public class PlayerArmorEquipEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled = false;

	private ArmorType type;
	private EquipMethod method;
	private ItemStack oldArmor, newArmor;

	public PlayerArmorEquipEvent(Player player, ArmorType type, EquipMethod method, ItemStack oldArmor, ItemStack newArmor) {
		super(player);
		this.type = type;
		this.method = method;
		this.oldArmor = oldArmor;
		this.newArmor = newArmor;
	}


	public static final HandlerList getHandlerList() {
		return handlers;
	}


	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}


	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}


	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}


	public ArmorType getType() {
		return type;
	}


	public EquipMethod getMethod() {
		return method;
	}


	public ItemStack getOldArmor() {
		return oldArmor;
	}


	public ItemStack getNewArmor() {
		return newArmor;
	}

	public static enum EquipMethod {
		SHIFT_CLICK, DRAG, PICK_DROP, HOTBAR, HOTBAR_SWAP, DISPENSER, BROKE, DEATH;
	}

}
