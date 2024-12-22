
package me.gamma.cookies.util.core;


import org.bukkit.craftbukkit.v1_21_R2.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

import net.minecraft.nbt.NBTTagCompound;



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
			// EntityChicken#setChickenJockey(boolean)
			c.getHandle().w(jockey);
	}


	/**
	 * Stores the given entity in an nbt tag compound and returns the latter.
	 * 
	 * @param entity the entity
	 * @return the entity stored in an nbt tag compound
	 */
	public static NBTTagCompound saveEntity(Entity entity) {
		if(!(entity instanceof CraftEntity craftentity))
			return new NBTTagCompound();

		NBTTagCompound compound = new NBTTagCompound();
		// Entity#getEncodedId()
		compound.a("id", craftentity.getHandle().bK());
		// Entity#saveWithoutId(NBTTagCompound)
		craftentity.getHandle().f(compound);

		return compound;
	}


	/**
	 * Marks the given attacker as the last entity attacking the given entity.
	 * 
	 * @param entity   the attacked entity
	 * @param attacker the attacking player
	 */
	public static void setLastHurtByPlayer(LivingEntity entity, HumanEntity attacker) {
		// EntityLiving#setLastHurtByPlayer(EntityHuman)
		((CraftLivingEntity) entity).getHandle().c(((CraftHumanEntity) attacker).getHandle());
	}

}
