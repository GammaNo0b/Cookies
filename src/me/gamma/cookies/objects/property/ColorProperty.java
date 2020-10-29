package me.gamma.cookies.objects.property;

import org.bukkit.Color;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

public class ColorProperty extends Property<Color> {

	protected ColorProperty(String name) {
		super(name);
	}

	@Override
	public void store(PersistentDataHolder holder, Color value) {
		if(value == null) {
			value = Color.BLACK;
		}
		holder.getPersistentDataContainer().set(this.getKey(), PersistentDataType.INTEGER, value.asRGB());
	}

	@Override
	public Color fetch(PersistentDataHolder holder) {
		return Color.fromRGB(holder.getPersistentDataContainer().get(this.getKey(), PersistentDataType.INTEGER));
	}

	@Override
	public boolean isPropertyOf(PersistentDataHolder holder) {
		return holder.getPersistentDataContainer().has(this.getKey(), PersistentDataType.INTEGER);
	}
	
	
	public static ColorProperty create(String name) {
		return new ColorProperty(name);
	}

}
