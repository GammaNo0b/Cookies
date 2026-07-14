
package me.gamma.cookies.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.TagParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;

public class NBTUtils {

	/**
	 * Reads a {@link CompoundTag} from the given input stream. Returns the read compound or a new one if an error occurred.
	 * 
	 * @param stream the input stream
	 * @return the read or a new compoun
	 */
	public static CompoundTag readNBT(InputStream stream) {
		try {
			return NbtIo.readCompressed(stream, NbtAccounter.defaultQuota());
		} catch(IOException e) {
			e.printStackTrace();
			return new CompoundTag();
		}
	}


	/**
	 * Writes the {@link CompoundTag} to the given output stream. Returns true if the node was successfully written to the stream, otherwise false.
	 * 
	 * @param stream the output stream
	 * @param nbt    the compound
	 * @return if the compound was written successfully
	 */
	public static boolean writeNBT(OutputStream stream, CompoundTag nbt) {
		try {
			NbtIo.writeCompressed(nbt, stream);
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Converts the given String into a {@link CompoundTag}. If the conversion failed, it will return a new empty compound.
	 * 
	 * @param string the string to be converted
	 * @return the converted or a new compound
	 */
	public static CompoundTag convertStringToNBT(String string) {
		if(string == null)
			return new CompoundTag();
		try {

			return TagParser.parseCompoundFully(string);
		} catch(CommandSyntaxException e) {
			return new CompoundTag();
		}
	}


	/**
	 * Returns the given {@link CompoundTag} into it's String representation.
	 * 
	 * @param nbt the compound to be converted
	 * @return the converted String
	 */
	public static String convertNBTToString(CompoundTag nbt) {
		return nbt.toString();
	}


	/**
	 * Returns the given {@link PersistentDataContainer} into a {@link CompoundTag}.
	 * 
	 * @param container the container to be converted
	 * @return the converted compound
	 */
	public static CompoundTag convertPersistentDataToNBT(PersistentDataContainer container) {
		return container instanceof CraftPersistentDataContainer craftcontainer ? craftcontainer.toTagCompound() : new CompoundTag();
	}


	/**
	 * Returns the given {@link CompoundTag} tranformed into a {@link PersistentDataContainer} using the given persistent adapter context.
	 * 
	 * @param nbt     the compound to be converted
	 * @param context the persistent data adapter context
	 * @return the converted persistent data container
	 */
	public static PersistentDataContainer convertNBTToPersistentData(CompoundTag nbt, PersistentDataAdapterContext context) {
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
	public static CompoundTag convertItemStackToNBT(ItemStack stack) {
		if(ItemUtils.isEmpty(stack))
			return new CompoundTag();

		net.minecraft.world.item.ItemStack nmsstack = CraftItemStack.asNMSCopy(stack);
		try(ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(new ProblemReporter.PathElement() {

			@Override
			public String get() {
				return nmsstack.getItem().toString();
			}

		}, Cookies.LOGGER)) {
			TagValueOutput output = TagValueOutput.createWithContext(reporter, MinecraftPersistentDataHelper.getRegistryAccess());
			output.store("ItemStack", net.minecraft.world.item.ItemStack.CODEC, nmsstack);
			return output.buildResult();
		}
	}


	/**
	 * Converts the given {@link CompoundTag} to an {@link ItemStack} if possible.
	 * 
	 * @param nbt the nbt data
	 * @return the converted {@link ItemStack} or an empty one.
	 */
	public static ItemStack convertNBTtoItemStack(CompoundTag nbt) {
		try(ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(Cookies.LOGGER)) {
			ValueInput input = TagValueInput.create(reporter, MinecraftPersistentDataHelper.getRegistryAccess(), nbt);
			Optional<net.minecraft.world.item.ItemStack> result = input.read("ItemStack", net.minecraft.world.item.ItemStack.CODEC);
			return result.map(CraftItemStack::asBukkitCopy).orElse(null);
		}
	}


	/**
	 * Converts the given {@link BigItemStack} into it's NBT representation.
	 * 
	 * @param stack the stack to be converted
	 * @return the nbt data
	 */
	public static CompoundTag convertBigItemStackToNBT(BigItemStack stack) {
		if(stack == null)
			return new CompoundTag();

		ItemStack type = stack.getType();
		NBTWrapper wrapper = new NBTWrapper(convertItemStackToNBT(type));
		wrapper.putInt("size", stack.getAmount());
		wrapper.putInt("max", stack.getMaxStackSize());
		wrapper.putBoolean("locked", stack.isLocked());
		return wrapper.getTag();
	}


	/**
	 * Converts the given {@link CompoundTag} to an {@link BigItemStack} if possible.
	 * 
	 * @param nbt the nbt data
	 * @return the converted {@link ItemStack} or an empty one.
	 */
	public static BigItemStack convertNBTtoBigItemStack(CompoundTag nbt) {
		ItemStack type = convertNBTtoItemStack(nbt);
		NBTWrapper wrapper = new NBTWrapper(nbt);
		BigItemStack stack = new BigItemStack(type, wrapper.getInt("size", 0), wrapper.getInt("max", 0));
		stack.setLocked(wrapper.getBoolean("locked", false));
		return stack;
	}


	/**
	 * Converts the given {@link BlockState} into it's NBT representation.
	 * 
	 * @param state the state to be converted
	 * @return the nbt data
	 */
	public static CompoundTag saveBlockStateToNBT(BlockState state) {
		CraftBlockState craftstate = (CraftBlockState) state;
		net.minecraft.world.level.block.state.BlockState craftdata = craftstate.getHandle();
		Collection<Property<?>> properties = craftdata.getProperties();
		NBTWrapper wrapper = new NBTWrapper();
		for(Property<?> property : properties) {
			if(property instanceof BooleanProperty bdata) {
				craftdata.getOptionalValue(bdata).ifPresent(b -> wrapper.putBoolean(property.getName(), b));
			} else if(property instanceof IntegerProperty idata) {
				craftdata.getOptionalValue(idata).ifPresent(i -> wrapper.putInt(property.getName(), i));
			} else if(property instanceof EnumProperty<?> edata) {
				craftdata.getOptionalValue(edata).ifPresent(e -> {
					NBTWrapper enumw = new NBTWrapper();
					enumw.putString("class", e.getDeclaringClass().getName());
					enumw.putInt("index", e.ordinal());
					wrapper.putNBT(property.getName(), enumw.getTag());
				});
			}
		}
		return wrapper.getTag();
	}


	/**
	 * Stores block data from the given {@link CompoundTag} into the {@link BlockState}.
	 * 
	 * @param nbt   the nbt data
	 * @param state the block state
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E> & StringRepresentable> void storeBlockDataFromNBT(CompoundTag nbt, BlockState state) {
		CraftBlockState craftstate = (CraftBlockState) state;
		net.minecraft.world.level.block.state.BlockState craftdata = craftstate.getHandle();
		for(String key : nbt.keySet()) {
			try {
				Property<?> data = craftdata.getProperties().stream().filter(d -> d.getName().equals(key)).findFirst().orElse(null);
				if(data instanceof BooleanProperty bdata) {
					craftdata = craftdata.setValue(bdata, nbt.getBoolean(key).get());
				} else if(data instanceof IntegerProperty idata) {
					craftdata = craftdata.setValue(idata, nbt.getInt(key).get());
				} else if(data instanceof EnumProperty<?> edata) {
					CompoundTag nbtenum = nbt.getCompoundOrEmpty(key);
					Class<?> clazz = Class.forName(nbtenum.getStringOr("class", ""));
					Object element = clazz.getEnumConstants()[nbtenum.getIntOr("index", 0)];
					craftdata = craftdata.setValue((EnumProperty<E>) edata, (E) element);
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
	public static CompoundTag saveTileEntityToNBT(TileState state) {
		return ((CraftWorld) state.getWorld()).getHandle().getBlockEntity(((CraftBlockEntityState<?>) state).getPosition()).saveWithFullMetadata(MinecraftPersistentDataHelper.getRegistryAccess());
	}


	/**
	 * Stores tile entity data from the given {@link CompoundTag} into the {@link TileState}.
	 * 
	 * @param nbt   the nbt data
	 * @param state the block state
	 */
	public static void storeTileEntityFromNBT(CompoundTag nbt, TileState state) {
		CraftBlockEntityState<?> craftstate = (CraftBlockEntityState<?>) state;
		ServerLevel world = ((CraftWorld) state.getWorld()).getHandle();
		BlockEntity blockEntity = world.getBlockEntity(craftstate.getPosition());
		try(ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), null)) {
			blockEntity.loadWithComponents(TagValueInput.create(reporter, MinecraftPersistentDataHelper.getRegistryAccess(), nbt));
			craftstate.refreshSnapshot();
		} catch(Throwable e) {}
	}


	/**
	 * Converts the given {@link Location} to it's nbt data representation.
	 * 
	 * @param location the location
	 * @return the converted {@link CompoundTag}
	 */
	public static CompoundTag convertLocationToNBT(Location location) {
		NBTWrapper wrapper = new NBTWrapper();
		wrapper.putString("world", location.getWorld().getName());
		wrapper.putDouble("x", location.getX());
		wrapper.putDouble("y", location.getY());
		wrapper.putDouble("z", location.getZ());
		wrapper.putFloat("pitch", location.getPitch());
		wrapper.putFloat("yaw", location.getYaw());
		return wrapper.getTag();
	}


	/**
	 * Converts the given {@link CompoundTag} to a location.
	 * 
	 * @param nbt the nbt data to be converted
	 * @return the converted location
	 */
	public static Location convertNBTToLocation(CompoundTag nbt) {
		NBTWrapper wrapper = new NBTWrapper(nbt);
		World world = Bukkit.getWorld(wrapper.getString("world", null));
		double x = wrapper.getDouble("x", 0.0D);
		double y = wrapper.getDouble("y", 0.0D);
		double z = wrapper.getDouble("z", 0.0D);
		float pitch = wrapper.getFloat("pitch", 0.0F);
		float yaw = wrapper.getFloat("yaw", 0.0F);
		return new Location(world, x, y, z, pitch, yaw);
	}


	/**
	 * Converts the given {@link Location} to it's nbt data representation, but only saving the relevant information a block needs (so only the world name
	 * and the coords as integers).
	 * 
	 * @param block the location to be converted
	 * @return the converted {@link CompoundTag}
	 */
	public static CompoundTag convertBlockToNBT(Location block) {
		NBTWrapper wrapper = new NBTWrapper();
		wrapper.putString("world", block.getWorld().getName());
		wrapper.putInt("x", block.getBlockX());
		wrapper.putInt("y", block.getBlockY());
		wrapper.putInt("z", block.getBlockZ());
		return wrapper.getTag();
	}


	/**
	 * Converts the given {@link CompoundTag} to a location of a block.
	 * 
	 * @param nbt the nbt data to be converted
	 * @return the converted location
	 */
	public static Location convertNBTToBlock(CompoundTag nbt) {
		NBTWrapper wrapper = new NBTWrapper(nbt);
		World world = Bukkit.getWorld(wrapper.getString("world", null));
		int x = wrapper.getInt("x", 0);
		int y = wrapper.getInt("y", 0);
		int z = wrapper.getInt("z", 0);
		return new Location(world, x, y, z);
	}


	/**
	 * Converts the given {@link UUID} to it's nbt data representation.
	 * 
	 * @param uuid the uuid to be converted
	 * @return the converted {@link CompoundTag}
	 */
	public static CompoundTag convertUUIDToNBT(UUID uuid) {
		NBTWrapper wrapper = new NBTWrapper();
		wrapper.putLong("uuid_least", uuid.getLeastSignificantBits());
		wrapper.putLong("uuid_most", uuid.getMostSignificantBits());
		return wrapper.getTag();
	}


	/**
	 * Converts the given {@link CompoundTag} to a UUID.
	 * 
	 * @param nbt the nbt data to be converted
	 * @return the converted uuid
	 */
	public static UUID convertNBTToUUID(CompoundTag nbt) {
		NBTWrapper wrapper = new NBTWrapper(nbt);
		return new UUID(wrapper.getLong("uuid_most", 0), wrapper.getLong("uuid_least", 0));
	}

}
