
package me.gamma.cookies.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.server.v1_16_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R2.NBTTagCompound;



public class NBTUtils {

	public static NBTTagCompound readNBT(InputStream stream) {
		try {
			return NBTCompressedStreamTools.a(stream);
		} catch(IOException e) {
			e.printStackTrace();
			return new NBTTagCompound();
		}
	}
	
	public static boolean writeNBT(OutputStream stream, NBTTagCompound compound) {
		try {
			NBTCompressedStreamTools.a(compound, stream);
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
