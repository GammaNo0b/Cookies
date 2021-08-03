
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;



public class VectorProperty extends Property<long[], Vector> {

	public VectorProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<long[], Vector> getPersistentDataType() {
		return new PersistentDataType<long[], Vector>() {

			@Override
			public long[] toPrimitive(Vector vector, PersistentDataAdapterContext context) {
				return new long[] {
					Double.doubleToLongBits(vector.getX()), Double.doubleToLongBits(vector.getY()), Double.doubleToLongBits(vector.getZ())
				};
			}


			@Override
			public Class<long[]> getPrimitiveType() {
				return long[].class;
			}


			@Override
			public Class<Vector> getComplexType() {
				return Vector.class;
			}


			@Override
			public Vector fromPrimitive(long[] value, PersistentDataAdapterContext context) {
				return new Vector(Double.longBitsToDouble(value[0]), Double.longBitsToDouble(value[1]), Double.longBitsToDouble(value[2]));
			}
		};
	}


	@Override
	public Vector emptyValue() {
		return new Vector(0.0D, 0.0D, 0.0D);
	}

}
