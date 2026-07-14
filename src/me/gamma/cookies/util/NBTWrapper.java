
package me.gamma.cookies.util;


import java.util.Optional;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;



public class NBTWrapper {

	private final CompoundTag tag;

	public NBTWrapper() {
		this(new CompoundTag());
	}


	public NBTWrapper(CompoundTag tag) {
		this.tag = tag;
	}


	public CompoundTag getTag() {
		return this.tag;
	}


	public int getSize() {
		return this.tag.size();
	}


	public boolean isEmpty() {
		return this.tag.isEmpty();
	}


	public boolean containsKey(final String key) {
		return this.tag.contains(key);
	}


	public void remove(final String key) {
		this.tag.remove(key);
	}


	public void putNBT(final String key, final Tag nbt) {
		this.tag.put(key, nbt);
	}


	public Tag getNBT(final String key) {
		return this.tag.get(key);
	}


	public CompoundTag getNBTTagCompound(final String key) {
		return this.tag.getCompoundOrEmpty(key);
	}


	public ListTag getNBTTagList(final String key) {
		return this.tag.getListOrEmpty(key);
	}


	public void putBoolean(final String key, final boolean value) {
		this.tag.putBoolean(key, value);
	}


	public Optional<Boolean> getBoolean(final String key) {
		return this.tag.getBoolean(key);
	}


	public boolean getBoolean(final String key, final boolean def) {
		return this.tag.getBooleanOr(key, def);
	}


	public void putByte(final String key, final byte value) {
		this.tag.putByte(key, value);
	}


	public Optional<Byte> getByte(final String key) {
		return this.tag.getByte(key);
	}


	public byte getByte(final String key, final byte def) {
		return this.tag.getByteOr(key, def);
	}


	public void putShort(final String key, final short value) {
		this.tag.putShort(key, value);
	}


	public Optional<Short> getShort(final String key) {
		return this.tag.getShort(key);
	}


	public short getShort(final String key, final short def) {
		return this.tag.getShortOr(key, def);
	}


	public void putInt(final String key, final int value) {
		this.tag.putInt(key, value);
	}


	public Optional<Integer> getInt(final String key) {
		return this.tag.getInt(key);
	}


	public int getInt(final String key, final int def) {
		return this.tag.getIntOr(key, def);
	}


	public void putLong(final String key, final long value) {
		this.tag.putLong(key, value);
	}


	public Optional<Long> getLong(final String key) {
		return this.tag.getLong(key);
	}


	public long getLong(final String key, final long def) {
		return this.tag.getLongOr(key, def);
	}


	public void putFloat(final String key, final float value) {
		this.tag.putFloat(key, value);
	}


	public Optional<Float> getFloat(final String key) {
		return this.tag.getFloat(key);
	}


	public float getFloat(final String key, final float def) {
		return this.tag.getFloatOr(key, def);
	}


	public void putDouble(final String key, final double value) {
		this.tag.putDouble(key, value);
	}


	public Optional<Double> getDouble(final String key) {
		return this.tag.getDouble(key);
	}


	public double getDouble(final String key, final double def) {
		return this.tag.getDoubleOr(key, def);
	}


	public void putString(final String key, final String value) {
		this.tag.putString(key, value);
	}


	public Optional<String> getString(final String key) {
		return this.tag.getString(key);
	}


	public String getString(final String key, final String def) {
		return this.tag.getStringOr(key, def);
	}


	public void putBytes(final String key, final byte[] value) {
		this.tag.putByteArray(key, value);
	}


	public Optional<byte[]> getBytes(final String key) {
		return this.tag.getByteArray(key);
	}


	public void putInts(final String key, final int[] value) {
		this.tag.putIntArray(key, value);
	}


	public Optional<int[]> getInts(final String key) {
		return this.tag.getIntArray(key);
	}


	public void putLongs(final String key, final long[] value) {
		this.tag.putLongArray(key, value);
	}


	public Optional<long[]> getLongs(final String key) {
		return this.tag.getLongArray(key);
	}

}
