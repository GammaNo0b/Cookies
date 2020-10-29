
package me.gamma.cookies.util;


import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.gamma.cookies.objects.block.StorageProvider;
import me.gamma.cookies.objects.block.skull.AbstractStorageSkullBlock;
import me.gamma.cookies.objects.block.skull.StorageMonitor;
import me.gamma.cookies.objects.list.HeadTextures;
import net.minecraft.server.v1_16_R2.GameProfileSerializer;
import net.minecraft.server.v1_16_R2.MojangsonParser;
import net.minecraft.server.v1_16_R2.NBTTagCompound;



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
			return "§" + colorSequence[0] + string;
		char[] original = string.toCharArray();
		char[] result = new char[original.length * 3];
		for(int i = 0; i < original.length; i++) {
			result[i * 3] = '§';
			result[i * 3 + 1] = colorSequence[i / colorLength % colorSequence.length];
			result[i * 3 + 2] = original[i];
		}
		return new String(result);
	}


	public static String colorizeProgress(double progress, double start, double end, char[] colorSequence) {
		return String.format("§%c%d%%", colorSequence[(int) Math.round(MathHelper.map(progress, start, end, 0, colorSequence.length - 1))], (int) progress);
	}


	public static String romanNumber(int n) {
		char[] main = "IXCM".toCharArray();
		char[] help = "VLDA".toCharArray();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < main.length; i++) {
			int j = n % 10;
			n /= 10;
			if(j % 5 == 4) {
				builder.append(main[i]);
				builder.append(j == 4 ? help[i] : main[i]);
				continue;
			}
			if(j >= 5)
				builder.append(help[i]);
			for(int k = 0; k < j % 5; k++)
				builder.append(main[i]);
		}
		while(n > 5) {
			n -= 5;
			builder.append(help[help.length - 1]);
		}
		while(n-- > 0)
			builder.append(main[main.length - 1]);
		return builder.toString();
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


	public static ItemStack transferItem(ItemStack stack, Block block, BlockFace[] directions) {
		for(BlockFace face : directions) {
			if(face != null) {
				Block relative = block.getRelative(face);
				if(relative.getState() instanceof Skull) {
					Skull skull = (Skull) relative.getState();
					if(StorageMonitor.isStorageMonitor(skull)) {
						stack = StorageMonitor.addItemStack(skull, stack);
					} else if(AbstractStorageSkullBlock.isStorageBlock(skull)) {
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


	public static void giveItemToPlayer(HumanEntity player, ItemStack item) {
		if(item != null) {
			Map<Integer, ItemStack> rest = player.getInventory().addItem(item);
			if(!rest.isEmpty()) {
				Iterator<Entry<Integer, ItemStack>> iterator = rest.entrySet().iterator();
				if(iterator.hasNext()) {
					ItemStack next = iterator.next().getValue();
					if(next != null) {
						player.getWorld().dropItem(player.getLocation(), next);
					}
				}
			}
		}
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
		net.minecraft.server.v1_16_R2.ItemStack nmsitem = CraftItemStack.asNMSCopy(item.getStack());
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
			net.minecraft.server.v1_16_R2.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
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
		net.minecraft.server.v1_16_R2.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
		if(nmsitem.hasTag()) {
			nbt.set("tag", nmsitem.getTag());
		}
		return nbt;
	}


	public static ItemStack NBTtoItemStack(NBTTagCompound nbt) {
		ItemStack item = null;
		try {
			Material material = Material.valueOf(nbt.getString("id").toUpperCase());
			int amount = nbt.getInt("Count");
			item = new ItemStack(material, amount);
			net.minecraft.server.v1_16_R2.ItemStack nmsitem = CraftItemStack.asNMSCopy(item);
			nmsitem.setTag(nbt.getCompound("tag"));
			item = CraftItemStack.asBukkitCopy(nmsitem);
			return item;
		} catch(Exception e) {
			return new ItemStack(Material.AIR);
		}
	}

}
