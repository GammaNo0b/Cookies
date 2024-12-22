
package me.gamma.cookies.util;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import me.gamma.cookies.object.list.HeadTextures;



public class GameProfileHelper {

	private static final char[] base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();

	/**
	 * Converts the given uuid unti a string of length 12 containing only the characters a-z, A-Z, 0-9 and '-' and '_'.
	 * 
	 * @param uuid the uuid
	 * @return the string
	 */
	private static String UUIDToBase64(UUID uuid) {
		char[] str = new char[12];
		Arrays.fill(str, '-');

		long bits;

		bits = uuid.getLeastSignificantBits();
		for(int i = 0; i < 6; i++)
			str[i] = base64[(int) ((bits >> (i * 6)) & 0x3F)];

		bits = uuid.getMostSignificantBits();
		for(int i = 0; i < 6; i++)
			str[6 + i] = base64[(int) ((bits >> (i * 6)) & 0x3F)];

		return new String(str);
	}


	/**
	 * Creates a new unique player profile of the given texture.
	 * 
	 * @param texture the texture
	 * @return a new player provile
	 */
	public static PlayerProfile createPlayerProfile(String texture) {
		UUID uuid = UUID.nameUUIDFromBytes(texture.getBytes());
		return Bukkit.createPlayerProfile(uuid, UUIDToBase64(uuid));
	}


	/**
	 * Updates the texture of the skull meta to the given skin texture.
	 * 
	 * @param skull   the player head as skull meta
	 * @param texture the skin texture
	 * @return if the texture was updates successfully
	 */
	public static boolean setSkullTexture(SkullMeta meta, String texture) {
		try {
			PlayerProfile profile = createPlayerProfile(texture);
			PlayerTextures textures = profile.getTextures();
			textures.setSkin(new URI(HeadTextures.getTexture(texture)).toURL());
			meta.setOwnerProfile(profile);
			return true;
		} catch(MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * Updates the texture of the player head item stack to the given skin texture.
	 * 
	 * @param skull   the player head as item stack
	 * @param texture the skin texture
	 * @return if the texture was updates successfully
	 */
	public static boolean setSkullTexture(ItemStack skull, String texture) {
		if(!(skull.getItemMeta() instanceof SkullMeta meta))
			return false;

		if(!setSkullTexture(meta, texture))
			return false;

		skull.setItemMeta(meta);
		return true;
	}


	/**
	 * Updates the texture of the player head blockstate to the given skin texture.
	 * 
	 * @param skull   the player head as blockstate
	 * @param texture the skin texture
	 * @return if the texture was updates successfully
	 */
	public static boolean setSkullTexture(Skull skull, String texture) {
		try {
			PlayerProfile profile = createPlayerProfile(texture);
			PlayerTextures textures = profile.getTextures();
			textures.setSkin(new URI(HeadTextures.getTexture(texture)).toURL());
			skull.setOwnerProfile(profile);
			skull.update();
			return true;
		} catch(MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
			return false;
		}
	}

}
