
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;



/**
 * Property that stores data of a primitive type.
 * 
 * @author gamma
 *
 * @param <P> the primitive type
 */
public abstract class PrimitiveProperty<P> extends Property<P, P> {

	private final Class<P> clazz;

	public PrimitiveProperty(String name, Class<P> clazz) {
		super(name);
		this.clazz = clazz;
	}


	@Override
	public PersistentDataType<P, P> getPersistentDataType() {
		return new PersistentDataType<P, P>() {

			@Override
			public P fromPrimitive(P primitive, PersistentDataAdapterContext context) {
				return primitive;
			}


			@Override
			public Class<P> getPrimitiveType() {
				return clazz;
			}


			@Override
			public Class<P> getComplexType() {
				return clazz;
			}


			@Override
			public P toPrimitive(P value, PersistentDataAdapterContext context) {
				return value;
			}

		};
	}

}
