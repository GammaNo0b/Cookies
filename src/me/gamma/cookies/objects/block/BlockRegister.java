
package me.gamma.cookies.objects.block;


import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.gamma.cookies.util.INBTRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;



public interface BlockRegister extends INBTRegistry<NBTTagList> {

	Set<Location> getLocations();


	@Override
	default void load(NBTTagList list) {
		Set<Location> locations = this.getLocations();
		locations.clear();
		list.stream().filter(nbt -> nbt instanceof NBTTagCompound).map(nbt -> (NBTTagCompound) nbt).map(BlockRegister::nbtToLocation).forEach(locations::add);
	}


	@Override
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

}
