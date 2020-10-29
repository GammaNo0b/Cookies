package me.gamma.cookies.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

	/**
	 * Reads a single Byte from the Input Stream. Inverse to
	 * {@link #writeByte(OutputStream, byte)}.
	 * 
	 * @param stream The Input Stream.
	 * @return b The Byte that got read.
	 */
	public static byte readByte(InputStream stream) {
		try {
			byte b = (byte) (stream.read() & 0xFF);
			return b;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Writes a single Byte to the Output Stream. Inverse to
	 * {@link #readByte(InputStream)}.
	 * 
	 * @param stream The Output Stream.
	 * @param b      The Byte to be written.
	 */
	public static void writeByte(OutputStream stream, byte b) {
		try {
			stream.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a single Unsigned Byte from the Input Stream.
	 * 
	 * @param stream The Input Stream.
	 * @return The Byte as Integer.
	 */
	public static int readUnsignedByte(InputStream stream) {
		try {
			return (stream.read() & 0xFF);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Reads a Short from the Input Stream. Inverse to
	 * {@link #writeShort(OutputStream, short)}.
	 * 
	 * @param stream The Input Stream.
	 * @return The Short that got read.
	 */
	public static short readShort(InputStream stream) {
		return ByteUtils.toShort(readBytes(stream, Short.BYTES));
	}

	public static void writeShort(OutputStream stream, short s) {
		writeBytes(stream, ByteUtils.toByteArray(s));
	}

	public static int readInt(InputStream stream) {
		return ByteUtils.toInt(readBytes(stream, Integer.BYTES));
	}

	public static void writeInt(OutputStream stream, int x) {
		writeBytes(stream, ByteUtils.toByteArray(x));
	}

	public static long readLong(InputStream stream) {
		return ByteUtils.toLong(readBytes(stream, Long.BYTES));
	}

	public static void writeLong(OutputStream stream, long l) {
		writeBytes(stream, ByteUtils.toByteArray(l));
	}

	public static float readFloat(InputStream stream) {
		return Float.intBitsToFloat(readInt(stream));
	}

	public static void writeFloat(OutputStream stream, float f) {
		writeInt(stream, Float.floatToIntBits(f));
	}

	public static double readDouble(InputStream stream) {
		return Double.longBitsToDouble(readLong(stream));
	}

	public static void writeDouble(OutputStream stream, double d) {
		writeLong(stream, Double.doubleToLongBits(d));
	}

	public static String readString(InputStream stream, int length) {
		return new String(readBytes(stream, length));
	}

	public static void writeString(OutputStream stream, String string) {
		writeBytes(stream, string.getBytes());
	}

	public static byte[] readBytes(InputStream stream, int bytes) {
		bytes = Math.max(0, bytes);
		byte[] abyte = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			abyte[i] = readByte(stream);
		}
		return abyte;
	}

	public static void writeBytes(OutputStream stream, byte[] bytes) {
		for (byte b : bytes) {
			writeByte(stream, b);
		}
	}

	public static int[] readUnsignedBytes(InputStream stream, int bytes) {
		bytes = Math.max(0, bytes);
		int[] abyte = new int[bytes];
		for (int i = 0; i < bytes; i++) {
			abyte[i] = readUnsignedByte(stream);
		}
		return abyte;
	}

	public static short[] readShorts(InputStream stream, int shorts) {
		short[] ashort = new short[shorts];
		for (int i = 0; i < shorts; i++) {
			ashort[i] = readShort(stream);
		}
		return ashort;
	}

	public static void writeShorts(OutputStream stream, short[] shorts) {
		for (short s : shorts) {
			writeShort(stream, s);
		}
	}

	public static int[] readInts(InputStream stream, int ints) {
		int[] aint = new int[ints];
		for (int i = 0; i < ints; i++) {
			aint[i] = readInt(stream);
		}
		return aint;
	}

	public static void writeInts(OutputStream stream, int[] ints) {
		for (int i : ints) {
			writeInt(stream, i);
		}
	}

	public static long[] readLongs(InputStream stream, int longs) {
		long[] along = new long[longs];
		for (int i = 0; i < longs; i++) {
			along[i] = readLong(stream);
		}
		return along;
	}

	public static void writeLongs(OutputStream stream, long[] longs) {
		for (long l : longs) {
			writeLong(stream, l);
		}
	}
	
	public static void print(InputStream stream) {
		print(stream, -1);
	}
	
	public static void print(InputStream stream, int length) {
		print(stream, length, -1);
	}
	
	public static void print(InputStream stream, int length, int bytesPerRow) {
		int unsignedByte = -1;
		int bytesRead = 0;
		try {
			while((unsignedByte = stream.read()) != -1 && (bytesRead++ < length || length == -1)) {
				System.out.print(ByteUtils.hexcode((byte) (unsignedByte & 0xFF)));
				if(bytesPerRow != -1 && bytesRead % (bytesPerRow - 1) == 0) {
					System.out.println();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
