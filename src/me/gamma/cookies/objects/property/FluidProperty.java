
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.objects.Fluid;
import me.gamma.cookies.objects.FluidType;



public class FluidProperty extends Property<Integer, Fluid> {

	private final FluidType type;

	public FluidProperty(String name, FluidType type) {
		super(name);
		this.type = type;
	}


	public FluidType getType() {
		return this.type;
	}


	@Override
	public PersistentDataType<Integer, Fluid> getPersistentDataType() {
		return new PersistentDataType<Integer, Fluid>() {

			@Override
			public Class<Integer> getPrimitiveType() {
				return Integer.class;
			}


			@Override
			public Class<Fluid> getComplexType() {
				return Fluid.class;
			}


			@Override
			public Integer toPrimitive(Fluid complex, PersistentDataAdapterContext context) {
				return complex.getMillibuckets();
			}


			@Override
			public Fluid fromPrimitive(Integer primitive, PersistentDataAdapterContext context) {
				return new Fluid(FluidProperty.this.type, primitive);
			}

		};
	}


	@Override
	public Fluid emptyValue() {
		return new Fluid(this.type, 0);
	}

}
