
package me.gamma.cookies.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.gamma.cookies.Cookies;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;



public interface INBTRegistry<N extends NBTBase> {

	static List<INBTRegistry<?>> instances = new ArrayList<>();

	String getRegistryName();

	void load(N nbt);

	N save();


	@SuppressWarnings("unchecked")
	default void castAndLoad(NBTBase nbtbase) {
		load((N) nbtbase);
	}


	default void register() {
		instances.add(this);
	}


	static void loadRegistries() {
		try {
			NBTTagCompound compound = NBTUtils.readNBT(new FileInputStream(getStorageFile()));
			instances.forEach(reg -> {
				NBTBase nbt = compound.get(reg.getRegistryName());
				if(nbt != null)
					reg.castAndLoad(nbt);
			});
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	static void saveRegistries() {
		try {
			NBTTagCompound compound = new NBTTagCompound();
			instances.forEach(reg -> compound.set(reg.getRegistryName(), reg.save()));
			NBTUtils.writeNBT(new FileOutputStream(getStorageFile()), compound);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	static File getStorageFile() throws IOException {
		File file = new File(Cookies.INSTANCE.getDataFolder(), "cookies.nbt");
		if(!file.exists()) {
			File parent = file.getParentFile();
			if(parent != null) {
				parent.mkdirs();
			}
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write("{}");
			writer.close();
		}
		return file;
	}

}
