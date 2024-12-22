package me.gamma.cookies.util.io;

public class ByteUtils {

	public static byte[] toByteArray(short s) {
		final int BYTES = Short.BYTES;
		byte[] bytes = new byte[BYTES];
		for (int i = 0; i < BYTES; i++) {
			bytes[i] = (byte) ((s >> (BYTES - 1 - i) * 8) & 0xFF);
		}
		return bytes;
	}

	public static byte[] toByteArray(int x) {
		final int BYTES = Integer.BYTES;
		byte[] bytes = new byte[BYTES];
		for (int i = 0; i < BYTES; i++) {
			bytes[i] = (byte) ((x >> (BYTES - 1 - i) * 8) & 0xFF);
		}
		return bytes;
	}

	public static byte[] toByteArray(long l) {
		final int BYTES = Long.BYTES;
		byte[] bytes = new byte[BYTES];
		for (int i = 0; i < BYTES; i++) {
			bytes[i] = (byte) ((l >> (BYTES - 1 - i) * 8) & 0xFF);
		}
		return bytes;
	}
	
	public static byte[] toByteArray(float f) {
		return toByteArray(Float.floatToIntBits(f));
	}
	
	public static byte[] toByteArray(double d) {
		return toByteArray(Double.doubleToLongBits(d));
	}
	
	public static byte[] toByteArray(short[] ashort) {
		ByteData data = new ByteData();
		for(short s : ashort) {
			data.addShort(s);
		}
		return data.toByteArray();
	}
	
	public static byte[] toByteArray(int[] aint) {
		ByteData data = new ByteData();
		for(int x : aint) {
			data.addInt(x);
		}
		return data.toByteArray();
	}
	
	public static byte[] toByteArray(long[] along) {
		ByteData data = new ByteData();
		for(long l : along) {
			data.addLong(l);
		}
		return data.toByteArray();
	}
	
	public static byte[] toByteArray(float[] afloat) {
		ByteData data = new ByteData();
		for(float f : afloat) {
			data.addFloat(f);
		}
		return data.toByteArray();
	}
	
	public static byte[] toByteArray(double[] adouble) {
		ByteData data = new ByteData();
		for(double d : adouble) {
			data.addDouble(d);
		}
		return data.toByteArray();
	}

	public static short toShort(byte[] bytes) {
		final int BYTES = Short.BYTES;
		if (bytes.length != BYTES) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be exactly " + BYTES + "!");
		}
		short s = 0;
		for (int i = 0; i < bytes.length; i++) {
			s |= (((short) bytes[i] & 0xFF) << ((BYTES - 1 - i) * 8));
		}
		return s;
	}

	public static int toInt(byte[] bytes) {
		final int BYTES = Integer.BYTES;
		if (bytes.length != BYTES) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be exactly " + BYTES + "!");
		}
		int x = 0;
		for (int i = 0; i < bytes.length; i++) {
			x |= (((short) bytes[i] & 0xFF) << ((BYTES - 1 - i) * 8));
		}
		return x;
	}

	public static long toLong(byte[] bytes) {
		final int BYTES = Long.BYTES;
		if (bytes.length != BYTES) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be exactly " + BYTES + "!");
		}
		long l = 0;
		for (int i = 0; i < bytes.length; i++) {
			l |= (((short) bytes[i] & 0xFF) << ((BYTES - 1 - i) * 8));
		}
		return l;
	}

	public static float toFloat(byte[] bytes) {
		return Float.intBitsToFloat(toInt(bytes));
	}

	public static double toDouble(byte[] bytes) {
		return Double.longBitsToDouble(toLong(bytes));
	}
	
	public static short[] toShortArray(byte[] bytes) {
		final int BYTES = Short.BYTES;
		if(bytes.length % BYTES != 0) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be divisible by " + BYTES + "!");
		}
		short[] ashort = new short[bytes.length / BYTES];
		for(int i = 0; i < ashort.length; i++) {
			byte[] subbyte = new byte[BYTES];
			for(int j = 0; j < BYTES; j++) {
				subbyte[j] = bytes[i * BYTES + j];
			}
			ashort[i] = toShort(subbyte);
		}
		return ashort;
	}
	
	public static int[] toIntegerArray(byte[] bytes) {
		final int BYTES = Short.BYTES;
		if(bytes.length % BYTES != 0) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be divisible by " + BYTES + "!");
		}
		int[] aint = new int[bytes.length / BYTES];
		for(int i = 0; i < aint.length; i++) {
			byte[] subbyte = new byte[BYTES];
			for(int j = 0; j < BYTES; j++) {
				subbyte[j] = bytes[i * BYTES + j];
			}
			aint[i] = toInt(subbyte);
		}
		return aint;
	}
	
	public static long[] toLongArray(byte[] bytes) {
		final int BYTES = Short.BYTES;
		if(bytes.length % BYTES != 0) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be divisible by " + BYTES + "!");
		}
		long[] along = new long[bytes.length / BYTES];
		for(int i = 0; i < along.length; i++) {
			byte[] subbyte = new byte[BYTES];
			for(int j = 0; j < BYTES; j++) {
				subbyte[j] = bytes[i * BYTES + j];
			}
			along[i] = toLong(subbyte);
		}
		return along;
	}
	
	public static float[] toFloatArray(byte[] bytes) {
		final int BYTES = Float.BYTES;
		if(bytes.length % BYTES != 0) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be divisible by " + BYTES + "!");
		}
		float[] afloat = new float[bytes.length / BYTES];
		for(int i = 0; i < afloat.length; i++) {
			byte[] subbyte = new byte[BYTES];
			for(int j = 0; j < BYTES; j++) {
				subbyte[j] = bytes[i * BYTES + j];
			}
			afloat[i] = toFloat(subbyte);
		}
		return afloat;
	}
	
	public static double[] toDoubleArray(byte[] bytes) {
		final int BYTES = Double.BYTES;
		if(bytes.length % BYTES != 0) {
			throw new IndexOutOfBoundsException("The amount of Bytes has to be divisible by " + BYTES + "!");
		}
		double[] adouble = new double[bytes.length / BYTES];
		for(int i = 0; i < adouble.length; i++) {
			byte[] subbyte = new byte[BYTES];
			for(int j = 0; j < BYTES; j++) {
				subbyte[j] = bytes[i * BYTES + j];
			}
			adouble[i] = toDouble(subbyte);
		}
		return adouble;
	}

	public static String hexcode(byte b) {
		final char[] hexcode = "0123456789ABCDEF".toCharArray();
		char[] hex = new char[2];
		hex[0] = hexcode[(b >> 4) & 0xF];
		hex[1] = hexcode[b & 0xF];
		return new String(hex);
	}
	
	public static String hexcode(byte[] bytes, boolean space) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(hexcode(b));
			if(space) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

}
