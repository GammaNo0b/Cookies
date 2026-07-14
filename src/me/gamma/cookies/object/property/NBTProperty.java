
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;



public class NBTProperty extends Property<PersistentDataContainer, CompoundTag> {

	public NBTProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<PersistentDataContainer, CompoundTag> getPersistentDataType() {
		return new PersistentDataType<PersistentDataContainer, CompoundTag>() {

			@Override
			public CompoundTag fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
				return NBTUtils.convertPersistentDataToNBT(primitive);
			}


			@Override
			public Class<PersistentDataContainer> getPrimitiveType() {
				return PersistentDataContainer.class;
			}


			@Override
			public Class<CompoundTag> getComplexType() {
				return CompoundTag.class;
			}


			@Override
			public PersistentDataContainer toPrimitive(CompoundTag value, PersistentDataAdapterContext context) {
				return NBTUtils.convertNBTToPersistentData(value, context);
			}

		};
	}


	@Override
	public CompoundTag emptyValue(PersistentDataContainer container) {
		return new CompoundTag();
	}

}
