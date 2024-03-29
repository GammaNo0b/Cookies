
package me.gamma.cookies.util;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftMagicNumbers;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.gamma.cookies.objects.Fluid;
import me.gamma.cookies.objects.block.StorageProvider;
import me.gamma.cookies.objects.block.skull.storage.StorageMonitor;
import me.gamma.cookies.objects.block.skull.storage.StorageSkullBlock;
import me.gamma.cookies.objects.list.HeadTextures;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.level.block.entity.TileEntityFurnace;



public class Utilities {

	public static final BlockFace[] faces = new BlockFace[] {
		BlockFace.DOWN, BlockFace.EAST, BlockFace.SOUTH, BlockFace.UP, BlockFace.WEST, BlockFace.NORTH
	};
	public static final char[] RAINBOW_COLOR_SEQUENCE = "4c6eab3915".toCharArray();

	public static boolean isEaster() {
		return currentTimeIsBetween(Calendar.APRIL, 1, Calendar.APRIL, 30);
	}


	public static boolean isHalloween() {
		return currentTimeIsBetween(Calendar.OCTOBER, 20, Calendar.NOVEMBER, 4);
	}


	public static boolean isChristmas() {
		return currentTimeIsBetween(Calendar.DECEMBER, 18, Calendar.DECEMBER, 30);
	}


	public static boolean currentTimeIsBetween(int startMonth, int startDate, int endMonth, int endDate) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar current = Calendar.getInstance();
		int year = current.get(Calendar.YEAR);
		start.set(year, startMonth, startDate, 0, 0, 0);
		end.set(year, endMonth, endDate, 0, 0, 0);
		return current.after(start) && current.before(end);
	}


	public static String toCapitalWords(String string) {
		if(string.isEmpty()) {
			return string;
		}
		StringBuilder builder = new StringBuilder();
		String[] split = string.split(" ");
		for(int i = 0; i < split.length; i++) {
			String str = split[i].toLowerCase();
			if(!str.isEmpty()) {
				char first = str.charAt(0);
				builder.append(str.replaceFirst(first + "", Character.toUpperCase(first) + "")).append(' ');
			}
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}


	public static boolean isEmpty(ItemStack stack) {
		return stack == null || stack.getType() == Material.AIR || stack.getAmount() == 0;
	}


	public static boolean isEmpty(Fluid fluid) {
		return fluid == null || fluid.isEmpty();
	}


	public static int getFuel(Material material) {
		return TileEntityFurnace.f().getOrDefault(CraftMagicNumbers.getItem(material), 0);
	}


	public static ItemStack subtractItemStack(ItemStack minuend, ItemStack subtrahend) {
		if(subtrahend == null || !minuend.isSimilar(subtrahend)) {
			return minuend;
		}
		minuend.setAmount(minuend.getAmount() - Math.min(minuend.getAmount(), subtrahend.getAmount()));
		return minuend;
	}


	public static String colorize(String string, char[] colorSequence, int colorLength) {
		if(colorSequence.length == 0)
			return string;
		if(colorSequence.length == 1)
			return "�" + colorSequence[0] + string;
		char[] original = string.toCharArray();
		char[] result = new char[original.length * 3];
		for(int i = 0; i < original.length; i++) {
			result[i * 3] = '�';
			result[i * 3 + 1] = colorSequence[i / colorLength % colorSequence.length];
			result[i * 3 + 2] = original[i];
		}
		return new String(result);
	}


	public static String colorizeProgress(double progress, double start, double end, char[] colorSequence) {
		return String.format("�%c%d%%", colorSequence[(int) Math.round(MathHelper.map(progress, start, end, 0, colorSequence.length - 1))], (int) progress);
	}


	public static String romanNumber(int n) {
		char[] main = "IXCM".toCharArray();
		char[] help = "VLDA".toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < main.length; i++) {
			int j = n % 10;
			n /= 10;
			if(j % 5 == 4) {
				builder.insert(0, main[i]);
				builder.insert(0, j == 4 ? help[i] : main[i]);
				continue;
			}
			if(j >= 5)
				builder.insert(0, help[i]);
			for(int k = 0; k < j % 5; k++)
				builder.insert(0, main[i]);
		}
		while(n > 5) {
			n -= 5;
			builder.insert(0, help[help.length - 1]);
		}
		while(n-- > 0)
			builder.insert(0, main[main.length - 1]);
		return builder.toString();
	}


	public static <T> boolean contains(T[] array, T value) {
		for(T element : array)
			if(element.equals(value))
				return true;
		return false;
	}


	public static String toString(Object object) {
		return toString(object, -1);
	}


	public static String toString(Object object, int depth) {
		final Set<Class<?>> canBeString = new HashSet<>(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class));
		StringBuilder builder = new StringBuilder();
		final String fill = fill(' ', depth * 2);
		builder.append(fill + "{\n");
		for(Field field : object.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				builder.append(fill + field.getName() + "=\"");
				Object value = field.get(Modifier.isStatic(field.getModifiers()) ? null : object);
				if(depth == -1 || value == null || canBeString.contains(value.getClass())) {
					builder.append(value);
				} else {
					builder.append(toString(value, depth + 1));
				}
				builder.append("\"\n");
			} catch(IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		builder.append(fill + "}");
		return builder.toString();
	}


	public static String fill(char c, int amount) {
		char[] chars = new char[amount];
		Arrays.fill(chars, c);
		return new String(chars);
	}


	public static BlockFace rotateYCounterClockwise(BlockFace facing) {
		switch (facing) {
			case EAST:
				return BlockFace.NORTH;
			case NORTH:
				return BlockFace.WEST;
			case WEST:
				return BlockFace.SOUTH;
			case SOUTH:
				return BlockFace.EAST;
			case DOWN:
			case UP:
			case SELF:
			default:
				return facing;
		}
	}


	public static BlockFace rotateYClockwise(BlockFace facing) {
		switch (facing) {
			case EAST:
				return BlockFace.SOUTH;
			case NORTH:
				return BlockFace.EAST;
			case WEST:
				return BlockFace.NORTH;
			case SOUTH:
				return BlockFace.WEST;
			case DOWN:
			case UP:
			case SELF:
			default:
				return facing;
		}
	}


	public static Material[] fill(Material material, int amountX) {
		Material[] materials = new Material[amountX];
		for(int x = 0; x < amountX; x++)
			materials[x] = material;
		return materials;
	}


	public static Material[][] fill(Material material, int amountX, int amountZ) {
		Material[][] materials = new Material[amountZ][amountX];
		for(int z = 0; z < amountZ; z++)
			for(int x = 0; x < amountX; x++)
				materials[z][x] = material;
		return materials;
	}


	public static Material[][][] fill(Material material, int amountX, int amountY, int amountZ) {
		Material[][][] materials = new Material[amountY][amountZ][amountX];
		for(int y = 0; y < amountY; y++)
			for(int z = 0; z < amountZ; z++)
				for(int x = 0; x < amountX; x++)
					materials[y][z][x] = material;
		return materials;
	}


	public static Material[][] fill(Material[] material, int amountZ) {
		Material[][] materials = new Material[amountZ][];
		for(int z = 0; z < amountZ; z++)
			materials[z] = material;
		return materials;
	}


	public static Material[][][] fill(Material[] material, int amountY, int amountZ) {
		Material[][][] materials = new Material[amountY][amountZ][];
		for(int y = 0; y < amountY; y++)
			for(int z = 0; z < amountZ; z++)
				materials[y][z] = material;
		return materials;
	}


	public static Material[][][] fill(Material[][] material, int amountY) {
		Material[][][] materials = new Material[amountY][][];
		for(int y = 0; y < amountY; y++)
			materials[y] = material;
		return materials;
	}


	public static GameProfile createGameProfileForTexture(String texture) {
		texture = HeadTextures.getTexture(texture);
		GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);
		profile.getProperties().put("textures", new Property("textures", texture));
		return profile;
	}


	public static boolean setSkullTexture(ItemStack skull, String texture) {
		if(!(skull.getItemMeta() instanceof SkullMeta))
			return false;
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		try {
			Field profilefield = meta.getClass().getDeclaredField("profile");
			Field serializedprofilefield = meta.getClass().getDeclaredField("serializedProfile");
			profilefield.setAccessible(true);
			serializedprofilefield.setAccessible(true);
			GameProfile profile = createGameProfileForTexture(texture);
			profilefield.set(meta, profile);
			serializedprofilefield.set(meta, GameProfileSerializer.serialize(new NBTTagCompound(), profile));
		} catch(IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return false;
		}
		skull.setItemMeta(meta);
		return true;
	}


	public static boolean setSkullTexture(Skull skull, String texture) {
		try {
			Field profileField = skull.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(skull, createGameProfileForTexture(texture));
		} catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		skull.update();
		return true;
	}


	public static ItemStack transferItem(ItemStack stack, Block block) {
		return transferItem(stack, block, faces);
	}


	public static ItemStack transferItem(ItemStack stack, Block block, BlockFace[] directions) {
		for(BlockFace face : directions) {
			if(face != null) {
				Block relative = block.getRelative(face);
				if(relative.getState() instanceof Skull) {
					Skull skull = (Skull) relative.getState();
					if(StorageMonitor.isStorageMonitor(skull)) {
						stack = StorageMonitor.addItemStack(skull, stack);
					} else if(StorageSkullBlock.isStorageBlock(skull)) {
						ItemStack rest = StorageProvider.storeItem(skull, stack);
						if(rest == null) {
							return null;
						} else {
							stack = rest;
						}
					}
				} else if(relative.getState() instanceof BlockInventoryHolder) {
					BlockInventoryHolder holder = (BlockInventoryHolder) relative.getState();
					Map<Integer, ItemStack> rest = holder.getInventory().addItem(stack);
					if(rest.isEmpty())
						return null;
					for(ItemStack restStack : rest.values()) {
						if(restStack != null) {
							stack = restStack;
							continue;
						}
					}
				}
			}
		}
		return stack;
	}


	public static Inventory fillEmptySlotsWith(Inventory inventory, ItemStack filler) {
		for(int i = 0; i < inventory.getSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if(stack == null) {
				inventory.setItem(i, filler);
			}
		}
		return inventory;
	}


	public static void giveItemToPlayer(HumanEntity player, ItemStack item) {
		if(item != null) {
			Map<Integer, ItemStack> rest = player.getInventory().addItem(item);
			if(!rest.isEmpty()) {
				for(ItemStack stack : rest.values()) {
					if(stack != null) {
						dropItem(stack, player.getLocation());
					}
				}
			}
		}
	}


	public static void dropItem(ItemStack stack, Location location) {
		if(!Utilities.isEmpty(stack))
			location.getWorld().dropItemNaturally(location, stack);
	}


	public static NBTTagCompound StringToNBT(String string) {
		if(string == null) {
			return new NBTTagCompound();
		}
		try {
			return MojangsonParser.parse(string);
		} catch(CommandSyntaxException e) {
			return new NBTTagCompound();
		}

	}


	public static NBTTagCompound BigItemStackToNBT(BigItemStack item) {
		if(item == null) {
			return new NBTTagCompound();
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("id", item.getStack().getType().toString().toLowerCase());
		nbt.setInt("Count", item.getAmount());
		net.minecraft.world.item.ItemStack nmsitem = CraftItemStack.asNMSCopy(item.getStack());
		if(nmsitem.hasTag()) {
			nbt.set("tag", nmsitem.getTag());
		}
		return nbt;
	}


	public static BigItemStack NBTtoBigItemStack(NBTTagCompound nbt) {
		try {
			Material material = Material.valueOf(nbt.getString("id").toUpperCase());
			int amount = nbt.getInt("Count");
			ItemStack item = new ItemStack(material, 1);
			net.minecraft.world.item.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
			nmsitem.setTag(nbt.getCompound("tag"));
			item = CraftItemStack.asBukkitCopy(nmsitem);

			return new BigItemStack(item, amount);
		} catch(Exception e) {
			return new BigItemStack(Material.AIR);
		}
	}


	public static NBTTagCompound ItemStackToNBT(ItemStack item) {
		NBTTagCompound nbt = new NBTTagCompound();
		if(item == null) {
			return nbt;
		}
		nbt.setString("id", item.getType().toString().toLowerCase());
		nbt.setInt("Count", item.getAmount());
		net.minecraft.world.item.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
		if(nmsitem.hasTag()) {
			nbt.set("tag", nmsitem.getTag());
		}
		return nbt;
	}


	public static ItemStack NBTtoItemStack(NBTTagCompound nbt) {
		try {
			Material material = Material.valueOf(nbt.getString("id").toUpperCase());
			int amount = nbt.getInt("Count");
			ItemStack item = new ItemStack(material, amount);
			net.minecraft.world.item.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
			nmsitem.setTag(nbt.getCompound("tag"));
			item = CraftItemStack.asBukkitCopy(nmsitem);
			return item;
		} catch(Exception e) {
			return new ItemStack(Material.AIR);
		}
	}

}
