
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.Utilities;



public class RainbowArmorPiece extends AbstractCustomItem implements ItemTicker {

	private ArmorType type;
	private String identifier;
	private String name;
	private Material material;
	private Set<UUID> players;

	public RainbowArmorPiece(ArmorType type) {
		this.type = type;
		this.identifier = "rainbow_" + type.name().toLowerCase();
		this.name = Utilities.colorize(Utilities.toCapitalWords("Rainbow " + type.name()), "4c6eab3915".toCharArray(), 1);
		this.material = Material.valueOf("LEATHER_" + type.name());
		this.players = new HashSet<>();
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}
	
	
	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Portable Disco.");
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}
	
	
	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return this.isInstanceOf(this.type.getArmor(player.getInventory()));
	}
	
	
	@Override
	public ItemStack getStackFromPlayer(Player player) {
		return this.type.getArmor(player.getInventory());
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public void tick(Player player, ItemStack stack) {
		if(stack != null) {
			if(stack.getItemMeta() instanceof LeatherArmorMeta) {
				LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
				Color color = meta.getColor();
				float[] hsb = java.awt.Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
				hsb[0] = (hsb[0] + 1.0F / 360.0F) % 1.0F;
				meta.setColor(Color.fromRGB(java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0xFFFFFF));
				stack.setItemMeta(meta);
			}
		}
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape(" R ", "RAR", " R ");
		recipe.setIngredient('A', this.material);
		recipe.setIngredient('R', CustomItemSetup.RAINBOW_DUST.createDefaultItemStack());
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.RED);
		stack.setItemMeta(meta);
		return stack;
	}


	public ArmorType getType() {
		return this.type;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onArmorEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == type) {
					if(isInstanceOf(event.getOldArmor()))
						unregisterPlayer(event.getPlayer());
					if(isInstanceOf(event.getNewArmor()))
						registerPlayer(event.getPlayer());
				}
			}

		};
	}

}
