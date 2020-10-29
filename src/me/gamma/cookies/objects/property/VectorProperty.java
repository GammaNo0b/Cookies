
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;



public class VectorProperty extends Property<Vector> {

	protected VectorProperty(String name) {
		super(name);
	}


	@Override
	public void store(PersistentDataHolder holder, Vector value) {
		if(value == null) {
			holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.LONG_ARRAY, new long[0]);
			return;
		}
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.LONG_ARRAY, new long[] {Double.doubleToLongBits(value.getX()), Double.doubleToLongBits(value.getY()), Double.doubleToLongBits(value.getZ())});
	}


	@Override
	public Vector fetch(PersistentDataHolder holder) {
		long[] l = holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.LONG_ARRAY);
		if(l.length < 3) {
			return null;
		}
		return new Vector(Double.longBitsToDouble(l[0]), Double.longBitsToDouble(l[1]), Double.longBitsToDouble(l[2]));
	}


	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.LONG_ARRAY);
	}
	
	
	public static VectorProperty create(String name) {
		return new VectorProperty(name);
	}

}
