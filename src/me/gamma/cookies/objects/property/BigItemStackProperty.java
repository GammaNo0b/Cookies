
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.util.BigItemStack;
import me.gamma.cookies.util.Utilities;



public class BigItemStackProperty extends Property<String, BigItemStack> {

	public BigItemStackProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<String, BigItemStack> getPersistentDataType() {
		return new PersistentDataType<String, BigItemStack>() {

			@Override
			public String toPrimitive(BigItemStack stack, PersistentDataAdapterContext context) {
				return Utilities.BigItemStackToNBT(stack).asString();
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
				return Utilities.NBTtoBigItemStack(Utilities.StringToNBT(value));
			}
		};
	}
	
	
	@Override
	public BigItemStack emptyValue() {
		return BigItemStack.EMPTY;
	}

}
