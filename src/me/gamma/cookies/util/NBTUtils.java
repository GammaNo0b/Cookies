
package me.gamma.cookies.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_21_R2.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R2.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTReadLimiter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.INamable;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.block.state.properties.BlockStateEnum;
import net.minecraft.world.level.block.state.properties.BlockStateInteger;
import net.minecraft.world.level.block.state.properties.IBlockState;



public class NBTUtils {

	/**
	 * Reads a {@link NBTTagCompound} from the given input stream. Returns the read compound or a new one if an error occurred.
	 * 
	 * @param stream the input stream
	 * @return the read or a new compoun
	 */
	public static NBTTagCompound readNBT(InputStream stream) {
		try {
			return NBTCompressedStreamTools.a(stream, NBTReadLimiter.a());
		} catch(IOException e) {
			e.printStackTrace();
			return new NBTTagCompound();
		}
	}


	/**
	 * Writes the {@link NBTTagCompound} to the given output stream. Returns true if the node was successfully written to the stream, otherwise false.
	 * 
	 * @param stream the output stream
	 * @param nbt    the compound
	 * @return if the compound was written successfully
	 */
	public static boolean writeNBT(OutputStream stream, NBTTagCompound nbt) {
		try {
			NBTCompressedStreamTools.a(nbt, stream);
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Converts the given String into a {@link NBTTagCompound}. If the conversion failed, it will return a new empty compound.
	 * 
	 * @param string the string to be converted
	 * @return the converted or a new compound
	 */
	public static NBTTagCompound convertStringToNBT(String string) {
		if(string == null)
			return new NBTTagCompound();
		try {
			return MojangsonParser.a(string);
		} catch(CommandSyntaxException e) {
			return new NBTTagCompound();
		}
	}


	/**
	 * Returns the given {@link NBTTagCompound} into it's String representation.
	 * 
	 * @param nbt the compound to be converted
	 * @return the converted String
	 */
	public static String convertNBTToString(NBTTagCompound nbt) {
		return nbt.toString();
	}


	/**
	 * Returns the given {@link PersistentDataContainer} into a {@link NBTTagCompound}.
	 * 
	 * @param container the container to be converted
	 * @return the converted compound
	 */
	public static NBTTagCompound convertPersistentDataToNBT(PersistentDataContainer container) {
		return container instanceof CraftPersistentDataContainer craftcontainer ? craftcontainer.toTagCompound() : new NBTTagCompound();
	}


	/**
	 * Returns the given {@link NBTTagCompound} tranformed into a {@link PersistentDataContainer} using the given persistent adapter context.
	 * 
	 * @param nbt     the compound to be converted
	 * @param context the persistent data adapter context
	 * @return the converted persistent data container
	 */
	public static PersistentDataContainer convertNBTToPersistentData(NBTTagCompound nbt, PersistentDataAdapterContext context) {
		PersistentDataContainer container = context.newPersistentDataContainer();
		if(container instanceof CraftPersistentDataContainer craftcontainer)
			craftcontainer.putAll(nbt);
		return container;
	}


	/**
	 * Converts the given {@link ItemStack} into it's NBT representation.
	 * 
	 * @param stack the stack to be converted
	 * @return the nbt data
	 */
	public static NBTTagCompound convertItemStackToNBT(ItemStack stack) {
		return ItemUtils.isEmpty(stack) ? new NBTTagCompound() : (NBTTagCompound) CraftItemStack.asNMSCopy(stack).b(MinecraftPersistentDataHelper.getRegistryAccess(), new NBTTagCompound());
	}


	/**
	 * Converts the given {@link NBTTagCompound} to an {@link ItemStack} if possible.
	 * 
	 * @param nbt the nbt data
	 * @return the converted {@link ItemStack} or an empty one.
	 */
	public static ItemStack convertNBTtoItemStack(NBTTagCompound nbt) {
		if(!nbt.e("id"))
			return null;

		String id = nbt.l("id");
		if(id == "" || id.equals("minecraft:air"))
			return null;

		if(!nbt.e("count") || nbt.h("count") == 0)
			return null;

		return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(MinecraftPersistentDataHelper.getRegistryAccess(), nbt));
	}


	/**
	 * Converts the given {@link BigItemStack} into it's NBT representation.
	 * 
	 * @param stack the stack to be converted
	 * @return the nbt data
	 */
	public static NBTTagCompound convertBigItemStackToNBT(BigItemStack stack) {
		if(stack == null)
			return new NBTTagCompound();

		ItemStack type = stack.getStack();
		NBTTagCompound nbt = convertItemStackToNBT(type);
		nbt.a("size", stack.getAmount());
		return nbt;
	}


	/**
	 * Converts the given {@link NBTTagCompound} to an {@link BigItemStack} if possible.
	 * 
	 * @param nbt the nbt data
	 * @return the converted {@link ItemStack} or an empty one.
	 */
	public static BigItemStack convertNBTtoBigItemStack(NBTTagCompound nbt) {
		ItemStack type = convertNBTtoItemStack(nbt);
		return new BigItemStack(type, nbt.h("size"));
	}


	/**
	 * Converts the given {@link BlockState} into it's NBT representation.
	 * 
	 * @param state the state to be converted
	 * @return the nbt data
	 */
	public static NBTTagCompound saveBlockStateToNBT(BlockState state) {
		CraftBlockState craftstate = (CraftBlockState) state;
		IBlockData craftdata = craftstate.getHandle();
		Map<IBlockState<?>, Comparable<?>> map = craftdata.G();
		NBTTagCompound compound = new NBTTagCompound();
		for(IBlockState<?> data : map.keySet()) {
			if(data instanceof BlockStateBoolean bdata) {
				craftdata.d(bdata).ifPresent(b -> compound.a(data.f(), b));
			} else if(data instanceof BlockStateInteger idata) {
				craftdata.d(idata).ifPresent(i -> compound.a(data.f(), i));
			} else if(data instanceof BlockStateEnum<?> edata) {
				craftdata.d(edata).ifPresent(e -> {
					NBTTagCompound nbtenum = new NBTTagCompound();
					nbtenum.a("class", e.getDeclaringClass().getName());
					nbtenum.a("index", e.ordinal());
					compound.a(data.f(), nbtenum);
				});
			}
		}
		return compound;
	}


	/**
	 * Stores block data from the given {@link NBTTagCompound} into the {@link BlockState}.
	 * 
	 * @param nbt   the nbt data
	 * @param state the block state
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E> & INamable> void storeBlockDataFromNBT(NBTTagCompound nbt, BlockState state) {
		CraftBlockState craftstate = (CraftBlockState) state;
		IBlockData craftdata = craftstate.getHandle();
		for(String key : nbt.e()) {
			try {
				IBlockState<?> data = craftdata.G().keySet().stream().filter(d -> d.f().equals(key)).findFirst().orElse(null);
				if(data instanceof BlockStateBoolean bdata) {
					craftdata = craftdata.b(bdata, nbt.q(key));
				} else if(data instanceof BlockStateInteger idata) {
					craftdata = craftdata.b(idata, nbt.h(key));
				} else if(data instanceof BlockStateEnum<?> edata) {
					NBTTagCompound nbtenum = nbt.p(key);
					Class<?> clazz = Class.forName(nbtenum.l("class"));
					Object element = clazz.getEnumConstants()[nbtenum.h("index")];
					craftdata = craftdata.b((BlockStateEnum<E>) edata, (E) element);
				} else {
					craftdata = null;
				}

				if(craftdata != null)
					craftstate.setData(craftdata);
			} catch(Exception e) {}
		}
	}


	/**
	 * Converts the Tileentity from the given {@link TileState} into it's NBT representation.
	 * 
	 * @param state the state that holds the tile entity to be converted
	 * @return the nbt data
	 */
	public static NBTTagCompound saveTileEntityToNBT(TileState state) {
		return ((CraftWorld) state.getWorld()).getHandle().c_(((CraftBlockEntityState<?>) state).getPosition()).d(MinecraftPersistentDataHelper.getRegistryAccess());
	}


	/**
	 * Stores tile entity data from the given {@link NBTTagCompound} into the {@link TileState}.
	 * 
	 * @param nbt   the nbt data
	 * @param state the block state
	 */
	public static void storeTileEntityFromNBT(NBTTagCompound nbt, TileState state) {
		CraftBlockEntityState<?> craftstate = (CraftBlockEntityState<?>) state;
		WorldServer world = ((CraftWorld) state.getWorld()).getHandle();
		TileEntity tileentity = world.c_(craftstate.getPosition());
		tileentity.c(nbt, MinecraftPersistentDataHelper.getRegistryAccess());
		craftstate.refreshSnapshot();
	}


	/**
	 * Converts the given {@link Location} to it's nbt data representation.
	 * 
	 * @param location the location
	 * @return the converted {@link NBTTagCompound}
	 */
	public static NBTTagCompound convertLocationToNBT(Location location) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.a("world", location.getWorld().getName());
		compound.a("x", location.getX());
		compound.a("y", location.getY());
		compound.a("z", location.getZ());
		compound.a("pitch", location.getPitch());
		compound.a("yaw", location.getYaw());
		return compound;
	}


	/**
	 * Converts the given {@link NBTTagCompound} to a location.
	 * 
	 * @param nbt the nbt data to be converted
	 * @return the converted location
	 */
	public static Location convertNBTToLocation(NBTTagCompound nbt) {
		World world = Bukkit.getWorld(nbt.l("world"));
		double x = nbt.k("x");
		double y = nbt.k("y");
		double z = nbt.k("z");
		float pitch = nbt.j("pitch");
		float yaw = nbt.j("yaw");
		return new Location(world, x, y, z, pitch, yaw);
	}


	/**
	 * Converts the given {@link Location} to it's nbt data representation, but only saving the relevant information a block needs (so only the world name
	 * and the coords as integers).
	 * 
	 * @param block the location to be converted
	 * @return the converted {@link NBTTagCompound}
	 */
	public static NBTTagCompound convertBlockToNBT(Location block) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.a("world", block.getWorld().getName());
		compound.a("x", block.getBlockX());
		compound.a("y", block.getBlockY());
		compound.a("z", block.getBlockZ());
		return compound;
	}


	/**
	 * Converts the given {@link NBTTagCompound} to a location of a block.
	 * 
	 * @param nbt the nbt data to be converted
	 * @return the converted location
	 */
	public static Location convertNBTToBlock(NBTTagCompound nbt) {
		World world = Bukkit.getWorld(nbt.l("world"));
		int x = nbt.h("x");
		int y = nbt.h("y");
		int z = nbt.h("z");
		return new Location(world, x, y, z);
	}


	/**
	 * Converts the given {@link UUID} to it's nbt data representation.
	 * 
	 * @param uuid the uuid to be converted
	 * @return the converted {@link NBTTagCompound}
	 */
	public static NBTTagCompound convertUUIDToNBT(UUID uuid) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.a("uuid_least", uuid.getLeastSignificantBits());
		compound.a("uuid_most", uuid.getMostSignificantBits());
		return compound;
	}


	/**
	 * Converts the given {@link NBTTagCompound} to a UUID.
	 * 
	 * @param nbt the nbt data to be converted
	 * @return the converted uuid
	 */
	public static UUID convertNBTToUUID(NBTTagCompound nbt) {
		return new UUID(nbt.i("uuid_most"), nbt.i("uuid_least"));
	}

}
