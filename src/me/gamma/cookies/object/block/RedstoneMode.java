
package me.gamma.cookies.object.block;


import java.util.function.UnaryOperator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.GuiUtils;



public enum RedstoneMode implements GuiUtils.Menu {

	REDSTONE_ON("§cRedstone On", Material.REDSTONE, b -> b),
	REDSTONE_OFF("§4Redstone Off", Material.REDSTONE_TORCH, b -> !b),
	ALWAYS_ON("§6Always On", Material.GLOWSTONE_DUST, _ -> true),
	ALWAYS_OFF("§8Always Off", Material.GUNPOWDER, _ -> false);

	private final String title;
	private final ItemStack icon;
	private final UnaryOperator<Boolean> activateFunction;

	private RedstoneMode(String title, Material icon, UnaryOperator<Boolean> activateFunction) {
		this.title = title;
		this.icon = new ItemStack(icon);
		this.activateFunction = activateFunction;
	}


	public String getTitle() {
		return this.title;
	}


	public ItemStack getIcon() {
		return this.icon;
	}


	public boolean isActive(boolean powered) {
		return this.activateFunction.apply(powered);
	}


	@Override
	public int size() {
		return values().length;
	}


	@Override
	public String getName() {
		return "§cRedstone Mode";
	}


	@Override
	public int selected() {
		return this.ordinal();
	}


	@Override
	public ItemStack getIcon(int index) {
		return values()[index].getIcon();
	}


	@Override
	public String get(int index) {
		return values()[index].getTitle();
	}

}
