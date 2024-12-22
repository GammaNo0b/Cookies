
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.util.EnumUtils;



public class FluidProperty extends Property<Long, Fluid> {

	public FluidProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<Long, Fluid> getPersistentDataType() {
		return new PersistentDataType<Long, Fluid>() {

			@Override
			public Class<Long> getPrimitiveType() {
				return Long.class;
			}


			@Override
			public Class<Fluid> getComplexType() {
				return Fluid.class;
			}


			@Override
			public Long toPrimitive(Fluid complex, PersistentDataAdapterContext context) {
				return ((long) complex.getType().ordinal() << 32) | complex.getMillibuckets();
			}


			@Override
			public Fluid fromPrimitive(Long primitive, PersistentDataAdapterContext context) {
				return new Fluid(EnumUtils.byIndex(FluidType.class, (int) (primitive >> 32)), (int) (primitive & 0xFFFFFF));
			}

		};
	}


	@Override
	public Fluid emptyValue(PersistentDataContainer container) {
		return new Fluid(FluidType.EMPTY, 0);
	}

}
