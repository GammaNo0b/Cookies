
package me.gamma.cookies.util.core;


import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.EnchantmentHelper;



public class MinecraftEnchantmentHelper {

	/**
	 * Enchants the given item stack randomly.
	 * 
	 * @param stack             the item
	 * @param world             the world
	 * @param enchantmentChoice the number of lapis
	 * @param enchantmentPower  the number of bookshelfs
	 * @param random            the random source
	 * @return the enchanted stack
	 */
	public static ItemStack enchantRandomly(ItemStack stack, int enchantmentChoice, int enchantmentPower, RandomSource random) {
		// from net.minecraft.world.inventory.ContainerEnchantTable

		net.minecraft.world.item.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
		int cost = EnchantmentHelper.getEnchantmentCost(random, enchantmentChoice, enchantmentPower, nmsstack);
		var possibleEnchantments = MinecraftPersistentDataHelper.getRegistryAccess().getOrThrow(Registries.ENCHANTMENT).value().get(EnchantmentTags.IN_ENCHANTING_TABLE);
		if(possibleEnchantments.isEmpty())
			return stack.clone();

		return CraftItemStack.asBukkitCopy(EnchantmentHelper.enchantItem(random, nmsstack, cost, possibleEnchantments.get().stream()));
	}

}
