package me.gamma.cookies.objects.list;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.block.MachineTier;
import me.gamma.cookies.objects.block.skull.machine.MachineCasing;
import me.gamma.cookies.setup.CustomItemSetup;

public class TieredMaterials {
	
	public static ItemStack getIngot(MachineTier tier) {
		switch(tier) {
			case BASIC:
				return new ItemStack(Material.IRON_INGOT);
			case ADVANCED:
				return CustomItemSetup.COPPER_INGOT.createDefaultItemStack();
			case IMPROVED:
				return CustomItemSetup.STEEL_INGOT.createDefaultItemStack();
			case PERFECTED:
				return new ItemStack(Material.NETHERITE_INGOT);
			default:
				return null;
		}
	}
	
	public static ItemStack getGem(MachineTier tier) {
		switch(tier) {
			case BASIC:
				return new ItemStack(Material.REDSTONE);
			case ADVANCED:
				return new ItemStack(Material.LAPIS_LAZULI);
			case IMPROVED:
				return new ItemStack(Material.EMERALD);
			case PERFECTED:
				return new ItemStack(Material.DIAMOND);
			default:
				return null;
		}
	}
	
	public static ItemStack getCore(MachineTier tier) {
		return MachineCasing.getByTier(tier).createDefaultItemStack();
	}

}
