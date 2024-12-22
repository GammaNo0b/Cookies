
package me.gamma.cookies.util.core;


import org.bukkit.craftbukkit.v1_21_R2.CraftRegistry;
import org.bukkit.persistence.PersistentDataContainer;

import net.minecraft.core.IRegistryCustom;



public class MinecraftPersistentDataHelper {

	public static PersistentDataContainer createNewPersistentDataContainer(PersistentDataContainer registryParent) {
		return registryParent.getAdapterContext().newPersistentDataContainer();
	}


	public static IRegistryCustom getRegistryAccess() {
		return CraftRegistry.getMinecraftRegistry();
	}

}
