
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;



public abstract class PrimitiveProperty<P> extends Property<P, P> {

	public PrimitiveProperty(String name) {
		super(name);
	}


	public abstract Class<P> getPrimitiveClass();


	@Override
	public PersistentDataType<P, P> getPersistentDataType() {
		return new PersistentDataType<P, P>() {

			@Override
			public P fromPrimitive(P primitive, PersistentDataAdapterContext context) {
				return primitive;
			}


			@Override
			public Class<P> getPrimitiveType() {
				return getPrimitiveClass();
			}


			@Override
			public Class<P> getComplexType() {
				return getPrimitiveClass();
			}


			@Override
			public P toPrimitive(P value, PersistentDataAdapterContext context) {
				return value;
			}

		};
	}

}
