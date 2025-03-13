
package me.gamma.cookies.object.property;


import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.RedstoneMode;
import me.gamma.cookies.object.item.ItemFilter;



public class Properties {

	public static final ByteProperty CHANNEL = new ByteProperty("channel");
	public static final FluidProperty FLUID = new FluidProperty("fluid");
	public static final IntegerProperty HONEY = new IntegerProperty("honey", 0, Integer.MAX_VALUE);
	public static final StringProperty IDENTIFIER = new StringProperty("identifier");
	public static final EnergyProperty INTERNAL_STORAGE = new EnergyProperty("internalstorage", 0, Integer.MAX_VALUE);
	public static final ItemStackProperty INPUT = new ItemStackProperty("input");
	public static final BooleanProperty INVERTED = new BooleanProperty("inverted");
	public static final ItemStackProperty ITEM = new ItemStackProperty("item");
	public static final LongProperty LAST_USED = new LongProperty("lastused");
	public static final IntegerProperty LAVA = new IntegerProperty("lava", 0, Integer.MAX_VALUE);
	public static final ItemStackProperty OUTPUT = new ItemStackProperty("output");
	public static final UUIDProperty OWNER = new UUIDProperty("owner");
	public static final VectorProperty POS = new VectorProperty("pos");
	public static final BooleanProperty POWERED = new BooleanProperty("powered");
	public static final IntegerProperty PRIORITY = new IntegerProperty("priority");
	public static final ItemStackProperty PROCESSING = new ItemStackProperty("processing");
	public static final IntegerProperty PROGRESS = new IntegerProperty("progress");
	public static final ByteProperty REDSTONE_FREQUENCY = new ByteProperty("frequency");
	public static final EnumProperty<RedstoneMode> REDSTONE_MODE = new EnumProperty<>("redstonemode", RedstoneMode.class);
	public static final DoubleProperty REST_SPEED = new DoubleProperty("restspeed");
	public static final IntegerProperty SIZE = new IntegerProperty("size");
	public static final IntegerProperty STORAGE_CAPACITY = new IntegerProperty("storagecapacity");
	public static final StringProperty TITLE = new StringProperty("title");
	public static final UUIDProperty UUID = new UUIDProperty("uuid");
	public static final VectorProperty WIRE_POS = new VectorProperty("wirepos");
	public static final StringProperty WIRE_ITEM = new StringProperty("wirename");
	public static final ListProperty<Vector, VectorProperty> WIRE_POSITIONS = new ListProperty<>("wirepositions", VectorProperty::new);
	public static final ListProperty<String, StringProperty> WIRE_ITEMS = new ListProperty<>("wireitems", StringProperty::new);

	public static PropertyCompound<ItemFilter> createItemFilterProperty() {
		PropertyCompound.Builder<ItemFilter> builder = new PropertyCompound.Builder<>(args -> {
			if(args.length != ItemFilter.SIZE + 3)
				return null;

			ItemFilter filter = new ItemFilter((ItemFilter.CountComparison) args[0], (boolean) args[1], (boolean) args[2]);
			for(int i = 0; i < ItemFilter.SIZE; i++)
				filter.setFilterItem(i, (ItemStack) args[i + 3]);
			return filter;
		});
		builder.addProperty(new EnumProperty<>("compareCount", ItemFilter.CountComparison.class), ItemFilter::getCountComparator);
		builder.addProperty(new BooleanProperty("whitelist"), ItemFilter::isWhitelisted);
		builder.addProperty(new BooleanProperty("ignoreNBT"), ItemFilter::isIgnoreNBT);
		for(int i = 0; i < ItemFilter.SIZE; i++) {
			final int j = i;
			builder.addProperty(new ItemStackProperty("filteritem" + i), filter -> filter.getFilterItem(j));
		}
		return builder.build();
	}

}
