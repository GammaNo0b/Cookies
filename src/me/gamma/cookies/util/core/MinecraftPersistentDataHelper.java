
package me.gamma.cookies.util.core;


import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.persistence.PersistentDataContainer;

import net.minecraft.core.RegistryAccess;



public class MinecraftPersistentDataHelper {

	public static PersistentDataContainer createNewPersistentDataContainer(PersistentDataContainer registryParent) {
		return registryParent.getAdapterContext().newPersistentDataContainer();
	}


	public static RegistryAccess getRegistryAccess() {
		return CraftRegistry.getMinecraftRegistry();
	}

}
