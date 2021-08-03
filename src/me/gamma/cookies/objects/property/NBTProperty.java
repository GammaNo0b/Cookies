
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.Utilities;
import net.minecraft.nbt.NBTTagCompound;



public class NBTProperty extends Property<String, NBTTagCompound> {

	public NBTProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<String, NBTTagCompound> getPersistentDataType() {
		return new PersistentDataType<String, NBTTagCompound>() {

			@Override
			public NBTTagCompound fromPrimitive(String primitive, PersistentDataAdapterContext context) {
				return Utilities.StringToNBT(primitive);
			}


			@Override
			public Class<String> getPrimitiveType() {
				return String.class;
			}


			@Override
			public Class<NBTTagCompound> getComplexType() {
				return NBTTagCompound.class;
			}


			@Override
			public String toPrimitive(NBTTagCompound value, PersistentDataAdapterContext context) {
				return value.toString();
			}

		};
	}


	@Override
	public NBTTagCompound emptyValue() {
		return new NBTTagCompound();
	}

}
