
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.util.NBTUtils;



public class BigItemStackProperty extends Property<String, BigItemStack> {

	public BigItemStackProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<String, BigItemStack> getPersistentDataType() {
		return new PersistentDataType<String, BigItemStack>() {

			@Override
			public String toPrimitive(BigItemStack stack, PersistentDataAdapterContext context) {
				return NBTUtils.convertNBTToString(NBTUtils.convertBigItemStackToNBT(stack));
			}


			@Override
			public Class<String> getPrimitiveType() {
				return String.class;
			}


			@Override
			public Class<BigItemStack> getComplexType() {
				return BigItemStack.class;
			}


			@Override
			public BigItemStack fromPrimitive(String value, PersistentDataAdapterContext context) {
				return NBTUtils.convertNBTtoBigItemStack(NBTUtils.convertStringToNBT(value));
			}

		};
	}


	@Override
	public BigItemStack emptyValue(PersistentDataContainer container) {
		return BigItemStack.EMPTY;
	}

}
