
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataContainer;



public class LongArrayProperty extends PrimitiveProperty<long[]> {

	public LongArrayProperty(String name) {
		super(name, long[].class);
	}


	@Override
	public long[] emptyValue(PersistentDataContainer container) {
		return new long[0];
	}

}
