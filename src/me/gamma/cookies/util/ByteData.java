package me.gamma.cookies.util;

import java.util.ArrayList;

public class ByteData {

	private ArrayList<Byte> data;

	public ByteData() {
		this.data = new ArrayList<>();
	}

	public void addByte(byte b) {
		this.data.add(b);
	}

	public void addShort(short s) {
		this.addBytes(ByteUtils.toByteArray(s));
	}
	
	public void addInt(int x) {
		this.addBytes(ByteUtils.toByteArray(x));
	}
	
	public void addLong(long l) {
		this.addBytes(ByteUtils.toByteArray(l));
	}
	
	public void addFloat(float f) {
		this.addBytes(ByteUtils.toByteArray(f));
	}
	
	public void addDouble(double d) {
		this.addBytes(ByteUtils.toByteArray(d));
	}

	public void addBytes(byte... bytes) {
		for (byte b : bytes) {
			this.addByte(b);
		}
	}
	
	public void addShorts(short[] shorts) {
		this.addBytes(ByteUtils.toByteArray(shorts));
	}
	
	public void addInts(int[] ints) {
		this.addBytes(ByteUtils.toByteArray(ints));
	}
	
	public void addLongs(long[] longs) {
		this.addBytes(ByteUtils.toByteArray(longs));
	}
	
	public void addFloats(float[] floats) {
		this.addBytes(ByteUtils.toByteArray(floats));
	}
	
	public void addDoubles(double[] doubles) {
		this.addBytes(ByteUtils.toByteArray(doubles));
	}

	public byte[] toByteArray() {
		byte[] bytes = new byte[this.data.size()];
		for (int i = 0; i < this.data.size(); i++) {
			bytes[i] = data.get(i);
		}
		return bytes;
	}

}
