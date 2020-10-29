
package me.gamma.cookies.objects.block;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.NBTUtils;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.NBTTagList;



public interface BlockRegister {

	String getIdentifier();

	Set<Location> getLocations();


	default void load(NBTTagList list) {
		Set<Location> locations = this.getLocations();
		locations.clear();
		list.stream().filter(nbt -> nbt instanceof NBTTagCompound).map(nbt -> (NBTTagCompound) nbt).map(BlockRegister::nbtToLocation).forEach(locations::add);
	}


	default NBTTagList save() {
		NBTTagList list = new NBTTagList();
		this.getLocations().stream().map(BlockRegister::locationToNBT).forEach(list::add);
		return list;
	}
	
	
	public static Location nbtToLocation(NBTTagCompound compound) {
		World world = Bukkit.getWorld(compound.getString("world"));
		if(world == null)
			return null;
		return new Location(world, compound.getInt("x"), compound.getInt("y"), compound.getInt("z"));
	}
	
	public static NBTTagCompound locationToNBT(Location location) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("world", location.getWorld().getName());
		compound.setInt("x", location.getBlockX());
		compound.setInt("y", location.getBlockY());
		compound.setInt("z", location.getBlockZ());
		return compound;
	}


	public static File getConfigFile() {
		return new File(Cookies.INSTANCE.getDataFolder(), "cookies.dat");
	}


	public static void loadConfigs() {
		try {
			NBTTagCompound compound = NBTUtils.readNBT(new FileInputStream(getConfigFile()));
			CustomBlockSetup.blockRegisters.forEach(register -> register.load(compound.getList(register.getIdentifier(), 10)));
		} catch(FileNotFoundException e) {
			System.err.println("Error while loading Block Register Locations! " + e.getMessage());
		}
	}


	public static void saveConfigs() {
		try {
			NBTTagCompound compound = new NBTTagCompound();
			CustomBlockSetup.blockRegisters.forEach(register -> compound.set(register.getIdentifier(), register.save()));
			NBTUtils.writeNBT(new FileOutputStream(getConfigFile()), compound);
		} catch(FileNotFoundException e) {
			System.err.println("Error while saving Block Register Locations! " + e.getMessage());
		}
	}

}
