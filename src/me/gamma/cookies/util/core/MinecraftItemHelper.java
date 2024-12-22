
package me.gamma.cookies.util.core;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R2.util.CraftMagicNumbers;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.EnumColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDye;
import net.minecraft.world.level.block.BlockComposter;



/**
 * Utility class for items using the minecraft code and not bukkit code.
 * 
 * @author gamma
 *
 */
public class MinecraftItemHelper {

	/**
	 * Returns the number of ticks the given stack takes to burn in a furnace.
	 * 
	 * @param stack the stack
	 * @return the burntime
	 */
	public static int getFuel(ItemStack stack) {
		return ((MinecraftServer) ((CraftServer) Bukkit.getServer()).getServer()).bo().b(CraftItemStack.asNMSCopy(stack));
	}


	/**
	 * Returns the chance the given material increases the composter level. Returns -1 if the material cannot be composted.
	 * 
	 * @param material the material
	 * @return the composting chance
	 */
	public static float getCompostChance(Material material) {
		// BlockComposter#COMPOSTABLES
		return BlockComposter.f.getFloat(CraftMagicNumbers.getItem(material));
	}


	/**
	 * Returns the color in RGB format of the given dye.
	 * 
	 * @param material the dye material
	 * @return the color
	 */
	public static int getDyeColor(Material material) {
		Item item = CraftMagicNumbers.getItem(material);
		if(!(item instanceof ItemDye dye))
			return -1;

		EnumColor color = dye.b();
		return color.g();
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
		// ItemStack#getComponents()
		return CraftItemStack.asNMSCopy(stack).a().d();
	}

}
