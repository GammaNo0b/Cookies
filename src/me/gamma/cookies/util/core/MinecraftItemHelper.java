
package me.gamma.cookies.util.core;


import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;



/**
 * Utility class for items using the minecraft code and not bukkit code.
 * 
 * @author gamma
 *
 */
public class MinecraftItemHelper {

	/**
	 * Returns the rarity of the given item.
	 * 
	 * @param stack the item
	 * @return the rarity
	 */
	public static Rarity getItemRarity(ItemStack stack) {
		return CraftItemStack.asNMSCopy(stack).getRarity();
	}


	/**
	 * Returns the color of the given rarity.
	 * 
	 * @param rarity the rarity
	 * @return the color
	 */
	public static Color getItemRarityColor(ItemRarity rarity) {
		return getItemRarityColor(switch (rarity) {
			case COMMON -> Rarity.COMMON;
			case UNCOMMON -> Rarity.UNCOMMON;
			case RARE -> Rarity.RARE;
			case EPIC -> Rarity.EPIC;
		});
	}


	/**
	 * Returns the color of the given rarity.
	 * 
	 * @param rarity the rarity
	 * @return the color
	 */
	public static Color getItemRarityColor(Rarity rarity) {
		return Color.fromRGB(switch (rarity) {
			case COMMON -> 0xFFFFFF;
			case UNCOMMON -> 0xFFFF35;
			case RARE -> 0x55FFFF;
			case EPIC -> 0xFF55FF;
		});
	}


	/**
	 * Returns the number of ticks the given stack takes to burn in a furnace.
	 * 
	 * @param stack the stack
	 * @return the burntime
	 */
	public static int getFuel(ItemStack stack) {
		return ((MinecraftServer) ((CraftServer) Bukkit.getServer()).getServer()).fuelValues().burnDuration(CraftItemStack.asNMSCopy(stack));
	}


	/**
	 * Returns the color in RGB format of the given dye.
	 * 
	 * @param stack the dye stack
	 * @return the color
	 */
	public static int getDyeColor(ItemStack stack) {
		net.minecraft.world.item.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
		if(!(nmsstack.getItem() instanceof DyeItem))
			return -1;

		DyeColor color = nmsstack.get(DataComponents.DYE);
		return color.getTextColor();
	}


	/**
	 * Returns the corresponding {@link Item} of the given type or null, if the given clazz does not match the item.
	 * 
	 * @param <I>   the item type
	 * @param type  the material
	 * @param clazz the item class
	 * @return the cast item or null, if the wrong class was specified
	 */
	public static <I extends Item> I getItem(Material type, Class<I> clazz) {
		Item item = CraftMagicNumbers.getItem(type);
		if(item == null)
			return null;

		if(!clazz.isAssignableFrom(item.getClass()))
			return null;

		return clazz.cast(item);
	}


	/**
	 * Returns the number of components the given stack has stored.
	 * 
	 * @param stack the stack
	 * @return the number of components
	 */
	public static int getNumberComponents(ItemStack stack) {
		return CraftItemStack.asNMSCopy(stack).getComponents().size();
	}

}
