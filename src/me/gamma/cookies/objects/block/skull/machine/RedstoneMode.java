
package me.gamma.cookies.objects.block.skull.machine;


import java.util.function.UnaryOperator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ItemBuilder;



public enum RedstoneMode {

	REDSTONE_ON("§cRedstone On", Material.REDSTONE, b -> b),
	REDSTONE_OFF("§cRedstone Off", Material.REDSTONE_TORCH, b -> !b),
	ALWAYS_ON("§cAlways On", Material.GLOWSTONE_DUST, b -> true),
	ALWAYS_OFF("§cAlways Off", Material.GUNPOWDER, b -> false);

	private final ItemStack icon;
	private final UnaryOperator<Boolean> activateFunction;

	private RedstoneMode(String description, Material icon, UnaryOperator<Boolean> activateFunction) {
		this.icon = new ItemBuilder(icon).setName(description).build();
		this.activateFunction = activateFunction;
	}


	public ItemStack getIcon() {
		return this.icon;
	}


	public boolean isActive(boolean powered) {
		return this.activateFunction.apply(powered);
	}

}
