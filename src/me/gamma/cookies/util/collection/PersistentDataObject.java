
package me.gamma.cookies.util.collection;


import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.property.AbstractProperty;
import me.gamma.cookies.object.property.BooleanProperty;



public class PersistentDataObject {

	private final PersistentDataContainer container;

	public PersistentDataObject(PersistentDataContainer container) {
		this.container = container;
	}


	public PersistentDataContainer getContainer() {
		return this.container;
	}


	public <C> C get(AbstractProperty<C> property) {
		return property.fetch(this.container);
	}


	public <C> void set(AbstractProperty<C> property, C value) {
		property.store(this.container, value);
	}


	public <C> boolean has(AbstractProperty<C> property) {
		return property.isPropertyOf(this.container);
	}


	public <T, Z> Z get(String name, PersistentDataType<T, Z> type) {
		return this.container.get(new NamespacedKey(Cookies.INSTANCE, name), type);
	}


	public <T, Z> void set(String name, PersistentDataType<T, Z> type, Z value) {
		this.container.set(new NamespacedKey(Cookies.INSTANCE, name), type, value);
	}


	public <T, Z> boolean has(String name, PersistentDataType<T, Z> type) {
		return this.container.has(new NamespacedKey(Cookies.INSTANCE, name), type);
	}


	public Boolean getBoolean(String name) {
		return this.get(new BooleanProperty(name));
	}


	public boolean getBoolean(String name, boolean defaultValue) {
		Boolean b = this.getBoolean(name);
		return b == null ? defaultValue : b;
	}


	public void setBoolean(String name, boolean value) {
		this.set(new BooleanProperty(name), value);
	}


	public Byte getByte(String name) {
		return this.get(name, PersistentDataType.BYTE);
	}


	public byte getByte(String name, byte defaultValue) {
		Byte b = this.getByte(name);
		return b == null ? defaultValue : b;
	}


	public void setByte(String name, byte value) {
		this.set(name, PersistentDataType.BYTE, value);
	}


	public Short getShort(String name) {
		return this.get(name, PersistentDataType.SHORT);
	}


	public short getShort(String name, short defaultValue) {
		Short s = this.getShort(name);
		return s == null ? defaultValue : s;
	}


	public void setShort(String name, short value) {
		this.set(name, PersistentDataType.SHORT, value);
	}


	public Integer getInteger(String name) {
		return this.get(name, PersistentDataType.INTEGER);
	}


	public int getInteger(String name, int defaultValue) {
		Integer i = this.getInteger(name);
		return i == null ? defaultValue : i;
	}


	public void setInteger(String name, int value) {
		this.set(name, PersistentDataType.INTEGER, value);
	}


	public Long getLong(String name) {
		return this.get(name, PersistentDataType.LONG);
	}


	public long getLong(String name, long defaultValue) {
		Long l = this.getLong(name);
		return l == null ? defaultValue : l;
	}


	public void setLong(String name, long value) {
		this.set(name, PersistentDataType.LONG, value);
	}


	public Float getFloat(String name) {
		return this.get(name, PersistentDataType.FLOAT);
	}


	public float getFloat(String name, float defaultValue) {
		Float f = this.getFloat(name);
		return f == null ? defaultValue : f;
	}


	public void setFloat(String name, float value) {
		this.set(name, PersistentDataType.FLOAT, value);
	}


	public Double getDouble(String name) {
		return this.get(name, PersistentDataType.DOUBLE);
	}


	public double getDouble(String name, double defaultValue) {
		Double d = this.getDouble(name);
		return d == null ? defaultValue : d;
	}


	public void setDouble(String name, double value) {
		this.set(name, PersistentDataType.DOUBLE, value);
	}


	public String getString(String name) {
		return this.get(name, PersistentDataType.STRING);
	}


	public String getString(String name, String defaultValue) {
		String s = this.getString(name);
		return s == null ? defaultValue : s;
	}


	public void setString(String name, String value) {
		this.set(name, PersistentDataType.STRING, value);
	}


	public byte[] getBytes(String name) {
		return this.get(name, PersistentDataType.BYTE_ARRAY);
	}


	public byte[] getBytes(String name, byte[] defaultValue) {
		byte[] a = this.getBytes(name);
		return a == null ? defaultValue : a;
	}


	public void setBytes(String name, byte[] value) {
		this.set(name, PersistentDataType.BYTE_ARRAY, value);
	}


	public int[] getInts(String name) {
		return this.get(name, PersistentDataType.INTEGER_ARRAY);
	}


	public int[] getInts(String name, int[] defaultValue) {
		int[] a = this.getInts(name);
		return a == null ? defaultValue : a;
	}


	public void setInts(String name, int[] value) {
		this.set(name, PersistentDataType.INTEGER_ARRAY, value);
	}


	public long[] getLongs(String name) {
		return this.get(name, PersistentDataType.LONG_ARRAY);
	}


	public long[] getLongs(String name, long[] defaultValue) {
		long[] a = this.getLongs(name);
		return a == null ? defaultValue : a;
	}


	public void setLongs(String name, long[] value) {
		this.set(name, PersistentDataType.LONG_ARRAY, value);
	}


	public PersistentDataContainer getContainer(String name) {
		return this.get(name, PersistentDataType.TAG_CONTAINER);
	}


	public PersistentDataContainer getContainer(String name, PersistentDataContainer defaultValue) {
		PersistentDataContainer c = this.getContainer();
		return c == null ? defaultValue : c;
	}


	public void setContainer(String name, PersistentDataContainer container) {
		this.set(name, PersistentDataType.TAG_CONTAINER, container);
	}


	public List<PersistentDataContainer> getContainers(String name) {
		return this.get(name, PersistentDataType.LIST.dataContainers());
	}


	public List<PersistentDataContainer> getContainers(String name, List<PersistentDataContainer> defaultValue) {
		List<PersistentDataContainer> a = this.getContainers(name);
		return a == null ? defaultValue : a;
	}


	public void setContainers(String name, List<PersistentDataContainer> containers) {
		this.set(name, PersistentDataType.LIST.dataContainers(), containers);
	}

}
