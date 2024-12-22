
package me.gamma.cookies.object.property;

import org.bukkit.persistence.PersistentDataContainer;

public class StringProperty extends PrimitiveProperty<String> {

	public StringProperty(String name) {
		super(name, String.class);
	}


	@Override
	public String emptyValue(PersistentDataContainer container) {
		return "";
	}

}
