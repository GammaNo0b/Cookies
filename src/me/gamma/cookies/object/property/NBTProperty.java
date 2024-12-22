
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;



public class NBTProperty extends Property<PersistentDataContainer, NBTTagCompound> {

	public NBTProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<PersistentDataContainer, NBTTagCompound> getPersistentDataType() {
		return new PersistentDataType<PersistentDataContainer, NBTTagCompound>() {

			@Override
			public NBTTagCompound fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
				return NBTUtils.convertPersistentDataToNBT(primitive);
			}


			@Override
			public Class<PersistentDataContainer> getPrimitiveType() {
				return PersistentDataContainer.class;
			}


			@Override
			public Class<NBTTagCompound> getComplexType() {
				return NBTTagCompound.class;
			}


			@Override
			public PersistentDataContainer toPrimitive(NBTTagCompound value, PersistentDataAdapterContext context) {
				return NBTUtils.convertNBTToPersistentData(value, context);
			}

		};
	}


	@Override
	public NBTTagCompound emptyValue(PersistentDataContainer container) {
		return new NBTTagCompound();
	}

}
