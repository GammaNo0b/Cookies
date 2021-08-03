
package me.gamma.cookies.objects.property;


import org.bukkit.Color;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;



public class ColorProperty extends Property<Integer, Color> {

	public ColorProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<Integer, Color> getPersistentDataType() {
		return new PersistentDataType<Integer, Color>() {

			@Override
			public Integer toPrimitive(Color color, PersistentDataAdapterContext context) {
				return color.asRGB();
			}


			@Override
			public Class<Integer> getPrimitiveType() {
				return Integer.class;
			}


			@Override
			public Class<Color> getComplexType() {
				return Color.class;
			}


			@Override
			public Color fromPrimitive(Integer value, PersistentDataAdapterContext context) {
				return Color.fromRGB(value);
			}
		};
	}


	@Override
	public Color emptyValue() {
		return Color.BLACK;
	}

}
