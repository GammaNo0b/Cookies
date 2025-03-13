
package me.gamma.cookies.util;


import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.util.collection.Pair;



public class FluidUtils {

	/**
	 * Checks if the given fluid is empty.
	 * 
	 * @param fluid the fluid
	 * @return if the fluid is empty
	 */
	public static boolean isEmpty(Fluid fluid) {
		return fluid == null || fluid.isEmpty();
	}


	/**
	 * Returns a fluid and a rest item stack that can be extracted from the given stack. For example a water bucket would return 1000 millibuckets of
	 * water and an empty bucket.
	 * 
	 * @param stack the stack
	 * @return pair containing the extracted fluid and rest stack
	 */
	public static Pair<Fluid, ItemStack> getFluidFromItemStack(ItemStack stack) {
		FluidType fluid = null;
		int millibuckets = 0;
		Material result = null;

		switch (stack.getType()) {
			case WATER_BUCKET:
				fluid = FluidType.WATER;
				millibuckets = 1000;
				result = Material.BUCKET;
				break;
			case LAVA_BUCKET:
				fluid = FluidType.LAVA;
				millibuckets = 1000;
				result = Material.BUCKET;
				break;
			case MILK_BUCKET:
				fluid = FluidType.MILK;
				millibuckets = 1000;
				result = Material.BUCKET;
				break;
			case POTION:
				PotionMeta meta = (PotionMeta) stack.getItemMeta();
				if(meta.getBasePotionType() == PotionType.WATER) {
					fluid = FluidType.WATER;
					millibuckets = 333;
					result = Material.GLASS_BOTTLE;
				}
				break;
			case HONEY_BOTTLE:
				fluid = FluidType.HONEY;
				millibuckets = 250;
				result = Material.GLASS_BOTTLE;
				break;
			case EXPERIENCE_BOTTLE:
				fluid = FluidType.EXPERIENCE;
				millibuckets = 70;
				result = Material.GLASS_BOTTLE;
				break;
			default:
				break;
		}

		return new Pair<>(new Fluid(fluid, millibuckets * stack.getAmount()), new ItemStack(result, stack.getAmount()));
	}

}
