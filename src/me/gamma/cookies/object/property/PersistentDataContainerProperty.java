
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataContainer;

import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;



public class PersistentDataContainerProperty extends PrimitiveProperty<PersistentDataContainer> {

	public PersistentDataContainerProperty(String name) {
		super(name, PersistentDataContainer.class);
	}


	@Override
	public PersistentDataContainer emptyValue(PersistentDataContainer container) {
		return MinecraftPersistentDataHelper.createNewPersistentDataContainer(container);
	}

}
