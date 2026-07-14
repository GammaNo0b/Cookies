
package me.gamma.cookies.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.DataStorage;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.item.ItemFilter.CountComparison;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;
import net.minecraft.nbt.CompoundTag;



public class PersistentDataUtils {

	public static <T extends DataStorage> T get(PersistentDataObject object, String key, T storage) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return storage;

		storage.load(null, o);
		return storage;
	}


	public static void set(PersistentDataObject object, String key, DataStorage storage) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		storage.save(null, o);
		object.setObject(key, o);
	}


	public static NamespacedKey getKey(PersistentDataObject object, String key) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		String namespace = o.getString("namespace");
		if(namespace == null)
			return null;

		String name = o.getString("key");
		if(name == null)
			return null;

		return new NamespacedKey(namespace, name);
	}


	public static void setKey(PersistentDataObject object, String key, NamespacedKey namespacedKey) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		o.setString("namespace", namespacedKey.getNamespace());
		o.setString("key", namespacedKey.getKey());
		object.setObject(key, o);
	}


	public static Vector loadVector(PersistentDataObject object) {
		return loadVector(object, new Vector());
	}


	public static Vector loadVector(PersistentDataObject object, Vector dest) {
		Double x = object.getDouble("x");
		if(x == null)
			return null;
		Double y = object.getDouble("y");
		if(y == null)
			return null;
		Double z = object.getDouble("z");
		if(z == null)
			return null;

		dest.setX(x);
		dest.setY(y);
		dest.setZ(z);

		return dest;
	}


	public static Vector getVector(PersistentDataObject object, String key) {
		return getVector(object, key, new Vector());
	}


	public static Vector getVector(PersistentDataObject object, String key, Vector dest) {
		PersistentDataObject pos = object.getObject(key);
		if(pos == null)
			return null;

		return loadVector(pos, dest);
	}


	public static void saveVector(PersistentDataObject object, Vector vector) {
		object.setDouble("x", vector.getX());
		object.setDouble("y", vector.getY());
		object.setDouble("z", vector.getZ());
	}


	public static void setVector(PersistentDataObject object, String key, Vector vector) {
		PersistentDataObject pos = new PersistentDataObject(object.getAdapterContext());
		saveVector(pos, vector);
		object.setObject(key, pos);
	}


	public static Color getColor(PersistentDataObject object, String key) {
		Integer argb = object.getInteger(key);
		return argb == null ? null : Color.fromARGB(argb);
	}


	public static void setColor(PersistentDataObject object, String key, Color color) {
		object.setInteger(key, color.asARGB());
	}


	public static Location loadLocation(PersistentDataObject object) {
		Vector vector = loadVector(object);
		if(vector == null)
			return null;

		String name = object.getString("world");
		if(name == null)
			return null;

		World world = Bukkit.getWorld(name);
		if(world == null)
			return null;

		return vector.toLocation(world);
	}


	public static Location getLocation(PersistentDataObject object, String key) {
		PersistentDataObject loc = object.getObject(key);
		if(loc == null)
			return null;

		return loadLocation(loc);
	}


	public static void saveLocation(PersistentDataObject object, Location location) {
		saveVector(object, location.toVector());
		object.setString("world", location.getWorld().getName());
	}


	public static void setLocation(PersistentDataObject object, String key, Location location) {
		PersistentDataObject loc = new PersistentDataObject(object.getAdapterContext());
		saveLocation(loc, location);
		object.setObject(key, loc);
	}


	public static UUID getUUID(PersistentDataObject object, String key) {
		Long most = object.getLong(key + "1");
		if(most == null)
			return null;

		Long least = object.getLong(key + "2");
		if(least == null)
			return null;

		return new UUID(most, least);
	}


	public static void setUUID(PersistentDataObject object, String key, UUID uuid) {
		object.setLong(key + "1", uuid.getMostSignificantBits());
		object.setLong(key + "2", uuid.getLeastSignificantBits());
	}


	public static <E extends Enum<E>> E getEnum(PersistentDataObject object, String key, Class<E> enumClass) {
		Integer index = object.getInteger(key);
		if(index == null)
			return null;

		E[] elements = enumClass.getEnumConstants();
		if(index < 0 || index >= elements.length)
			return null;

		return elements[index];
	}


	public static <E extends Enum<E>> void setEnum(PersistentDataObject object, String key, E element) {
		object.setInteger(key, element.ordinal());
	}


	public static ItemStack loadItemStack(PersistentDataObject object) {
		CompoundTag nbt = NBTUtils.convertPersistentDataToNBT(object.getContainer());
		return NBTUtils.convertNBTtoItemStack(nbt);
	}


	public static ItemStack getItemStack(PersistentDataObject object, String key) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		return loadItemStack(o);
	}


	public static void saveItemStack(PersistentDataObject object, ItemStack stack) {
		CompoundTag compound = NBTUtils.convertItemStackToNBT(stack);
		PersistentDataContainer container = NBTUtils.convertNBTToPersistentData(compound, object.getAdapterContext());
		container.copyTo(object.getContainer(), true);
	}


	public static void setItemStack(PersistentDataObject object, String key, ItemStack stack) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		saveItemStack(o, stack);
		object.setObject(key, o);
	}


	public static BigItemStack loadBigItemStack(PersistentDataObject object, BigItemStack stack) {
		ItemStack type = getItemStack(object, "type");
		if(type == null)
			return null;

		int amount = object.getInteger("amount", 0);
		int maxStackSize = object.getInteger("maxstacksize", 0);
		boolean locked = object.getBoolean("locked", false);

		if(stack == null) {
			return new BigItemStack(type, amount, maxStackSize);
		} else {
			stack.setType(type);
			stack.setAmount(amount);
			stack.setMaxStackSize(maxStackSize);
			stack.setLocked(locked);

			return stack;
		}
	}


	public static BigItemStack getBigItemStack(PersistentDataObject object, String key, BigItemStack stack) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		return loadBigItemStack(o, stack);
	}


	public static void saveBigItemStack(PersistentDataObject object, BigItemStack stack) {
		setItemStack(object, "type", stack.getType());
		object.setInteger("amount", stack.getAmount());
		object.setInteger("maxstacksize", stack.getMaxStackSize());
		object.setBoolean("locked", stack.isLocked());
	}


	public static void setBigItemStack(PersistentDataObject object, String key, BigItemStack stack) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		saveBigItemStack(o, stack);
		object.setObject(key, o);
	}


	public static Inventory loadInventory(PersistentDataObject object, Inventory inventory) {
		List<PersistentDataObject> contents = object.getObjectList("contents");
		if(contents == null)
			return null;

		for(int slot = 0; slot < inventory.getSize() && slot < contents.size(); slot++)
			inventory.setItem(slot, loadItemStack(contents.get(slot)));

		return inventory;
	}


	public static Pair<Inventory, String> loadInventory(PersistentDataObject object) {
		Integer size = object.getInteger("size");
		if(size == null)
			return null;

		String title = object.getString("title");
		if(title == null)
			return null;

		return new Pair<>(loadInventory(object, Bukkit.createInventory(null, size)), title);
	}


	public static Pair<Inventory, String> getInventory(PersistentDataObject object, String key) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		return loadInventory(o);
	}


	public static void saveInventory(PersistentDataObject object, Inventory inventory, String title) {
		final int size = inventory.getSize();

		object.setInteger("size", size);
		object.setString("title", title);

		List<PersistentDataObject> contents = new ArrayList<>(size);
		for(int slot = 0; slot < size; slot++) {
			ItemStack stack = inventory.getItem(slot);
			PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
			saveItemStack(o, stack);
			contents.add(o);
		}

		object.setObjectList("contents", contents);
	}


	public static void setInventory(PersistentDataObject object, String key, Inventory inventory, String title) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		saveInventory(o, inventory, title);
		object.setObject(key, o);
	}


	public static Fluid loadFluid(PersistentDataObject object) {
		return loadFluid(object, null);
	}


	public static Fluid loadFluid(PersistentDataObject object, Fluid fluid) {
		if(fluid == null)
			fluid = new Fluid(FluidType.EMPTY);

		FluidType type = getEnum(object, "type", FluidType.class);
		if(type == null)
			type = FluidType.EMPTY;
		fluid.setType(type);

		fluid.setMillibuckets(object.getInteger("amount", 0));
		return fluid;
	}


	public static Fluid getFluid(PersistentDataObject object, String key, Fluid fluid) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		return loadFluid(o, fluid);
	}


	public static Fluid getFluid(PersistentDataObject object, String key) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		return loadFluid(o);
	}


	public static void saveFluid(PersistentDataObject object, Fluid fluid) {
		setEnum(object, "type", fluid.getType());
		object.setInteger("amount", fluid.getMillibuckets());
	}


	public static void setFluid(PersistentDataObject object, String key, Fluid fluid) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		saveFluid(o, fluid);
		object.setObject(key, o);
	}


	public static ItemFilter loadItemFilter(PersistentDataObject object) {
		CountComparison comparison = getEnum(object, "countcomparison", CountComparison.class);
		if(comparison == null)
			comparison = CountComparison.IGNORE;

		boolean whitelisted = object.getBoolean("whitelisted", true);
		boolean ignoreNBT = object.getBoolean("ignorenbt", true);

		ItemFilter filter = new ItemFilter(comparison, whitelisted, ignoreNBT);

		List<ItemStack> items = new ArrayList<>();
		getList(object, "items", items, PersistentDataUtils::loadItemStack);
		for(int i = 0; i < Math.min(items.size(), ItemFilter.SIZE); i++)
			filter.setFilterItem(i, items.get(i));

		return filter;
	}


	public static ItemFilter getItemFilter(PersistentDataObject object, String key) {
		PersistentDataObject o = object.getObject(key);
		if(o == null)
			return null;

		return loadItemFilter(o);
	}


	public static void saveItemFilter(PersistentDataObject object, ItemFilter filter) {
		setEnum(object, "countcomparison", filter.getCountComparator());
		object.setBoolean("whitelisted", filter.isWhitelisted());
		object.setBoolean("ignorenbt", filter.isIgnoreNBT());

		setList(object, "items", Arrays.asList(filter.getItems()), PersistentDataUtils::saveItemStack);
	}


	public static void setItemFilter(PersistentDataObject object, String key, ItemFilter filter) {
		PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
		saveItemFilter(o, filter);
		object.setObject(key, o);
	}


	public static <T> boolean getArray(PersistentDataObject object, String key, T[] array, DataLoader<T> elementLoader) {
		List<PersistentDataObject> elements = object.getObjectList(key);
		if(elements == null || elements.isEmpty())
			return false;

		for(int i = 0; i < elements.size() && i < array.length; ++i)
			array[i] = elementLoader.load(elements.get(i));

		return true;
	}


	public static <T> void setArray(PersistentDataObject object, String key, T[] array, DataSaver<T> elementSaver) {
		List<PersistentDataObject> elements = new ArrayList<>(array.length);

		for(int i = 0; i < array.length; ++i) {
			PersistentDataObject element = new PersistentDataObject(object.getAdapterContext());
			elementSaver.save(element, array[i]);
			elements.add(element);
		}

		object.setObjectList(key, elements);
	}


	public static <T> List<T> getList(PersistentDataObject object, String key, List<T> list, DataLoader<T> elementLoader) {
		List<PersistentDataObject> elements = object.getObjectList(key);
		if(elements == null)
			return null;

		if(list == null)
			list = new ArrayList<>();

		for(PersistentDataObject element : elements) {
			T value = elementLoader.load(element);
			if(value == null)
				continue;

			list.add(value);
		}

		return list;
	}


	public static <T> void setList(PersistentDataObject object, String key, List<T> list, DataSaver<T> elementSaver) {
		List<PersistentDataObject> elements = new ArrayList<>();

		for(T value : list) {
			PersistentDataObject element = new PersistentDataObject(object.getAdapterContext());
			elementSaver.save(element, value);
			elements.add(element);
		}

		object.setObjectList(key, elements);
	}


	public static <K, V> Map<K, V> getMap(PersistentDataObject object, String key, Map<K, V> map, DataGetter<K> keyGetter, DataGetter<V> valueGetter) {
		List<PersistentDataObject> list = object.getObjectList(key);
		if(list == null)
			return null;

		if(map == null)
			map = new HashMap<>();

		for(PersistentDataObject element : list) {
			K k = keyGetter.get(element, "key");
			if(k == null)
				continue;

			V v = valueGetter.get(element, "value");
			if(v == null)
				continue;

			map.put(k, v);
		}

		return map;
	}


	public static <K, V> void setMap(PersistentDataObject object, String key, Map<K, V> map, DataSetter<K> keySetter, DataSetter<V> valueSetter) {
		List<PersistentDataObject> list = new ArrayList<>();

		for(Map.Entry<K, V> entry : map.entrySet()) {
			PersistentDataObject element = new PersistentDataObject(object.getAdapterContext());
			keySetter.set(element, "key", entry.getKey());
			valueSetter.set(element, "value", entry.getValue());
		}

		object.setObjectList(key, list);
	}

	@FunctionalInterface
	public static interface DataGetter<T> {

		T get(PersistentDataObject data, String key);

	}

	@FunctionalInterface
	public static interface DataSetter<T> {

		void set(PersistentDataObject data, String key, T value);

	}

	@FunctionalInterface
	public static interface DataLoader<T> {

		T load(PersistentDataObject data);

	}

	@FunctionalInterface
	public static interface DataSaver<T> {

		void save(PersistentDataObject data, T value);

	}

}
