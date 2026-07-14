
package me.gamma.cookies.util.core;


import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

import me.gamma.cookies.Cookies;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueOutput;



/**
 * Utility class for manipulating entities using the minecraft code and not bukkit code.
 * 
 * @author gamma
 *
 */
public class MinecraftEntityHelper {

	/**
	 * Sets whether the given chicken should be a jockey or not.
	 * 
	 * Chicken jockeys do not lay eggs, but also despawn when no players are near.
	 * 
	 * @param chicken the chicken
	 * @param jockey  if the chicken should become a jockey
	 */
	public static void setChickenJockey(Chicken chicken, boolean jockey) {
		if(chicken instanceof CraftChicken c)
			c.getHandle().setChickenJockey(jockey);
	}


	/**
	 * Stores the given entity in an nbt tag compound and returns the latter.
	 * 
	 * @param entity the entity
	 * @return the entity stored in an nbt tag compound
	 */
	public static CompoundTag saveEntity(Entity entity) {
		if(!(entity instanceof CraftEntity craftentity))
			return new CompoundTag();

		try(ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(craftentity.getHandle().problemPath(), Cookies.LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(reporter, craftentity.getHandle().registryAccess());
			craftentity.getHandle().save(output);
			return output.buildResult();
		}
	}


	/**
	 * Marks the given attacker as the last entity attacking the given entity.
	 * 
	 * @param entity   the attacked entity
	 * @param attacker the attacking player
	 */
	public static void setLastHurtByPlayer(LivingEntity entity, HumanEntity attacker) {
		((CraftLivingEntity) entity).getHandle().setLastHurtByPlayer(((CraftHumanEntity) attacker).getHandle(), 20);
	}

}
