
package me.gamma.cookies.objects.item.armor;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.item.ItemTicker;
import me.gamma.cookies.objects.property.IntegerProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;



public class ColoredArmorPiece extends AbstractCustomItem implements ItemTicker {

	private static final IntegerProperty TARGET_RED = new IntegerProperty("targetred");
	private static final IntegerProperty TARGET_GREEN = new IntegerProperty("targetgreen");
	private static final IntegerProperty TARGET_BLUE = new IntegerProperty("targetblue");

	private ArmorType type;
	private String identifier;
	private String name;
	private Material material;
	private Set<UUID> players;


	public ColoredArmorPiece(ArmorType type) {
		this.type = type;
		this.identifier = "colored_" + type.name().toLowerCase();
		this.name = "§6" + Utilities.toCapitalWords("Colored " + type.name());
		this.material = Material.valueOf("LEATHER_" + type.name());
		this.players = new HashSet<>();
	}


	@Override
	public String getRegistryName() {
		return this.identifier;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Gives you more colors.");
	}


	@Override
	public Material getMaterial() {
		return this.material;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("PR", "BG");
		recipe.setIngredient('P', this.material);
		recipe.setIngredient('R', Material.RED_DYE);
		recipe.setIngredient('G', Material.GREEN_DYE);
		recipe.setIngredient('B', Material.BLUE_DYE);
		return recipe;
	}


	@Override
	public long getDelay() {
		return 1;
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
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.OLIVE);
		Random r = new Random();
		TARGET_RED.store(meta, r.nextInt(256));
		TARGET_GREEN.store(meta, r.nextInt(256));
		TARGET_BLUE.store(meta, r.nextInt(256));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void tick(Player player, ItemStack stack) {
		if(stack != null && stack.getItemMeta() instanceof LeatherArmorMeta) {
			LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
			Random ra = new Random();
			Color color = meta.getColor();
			int r = TARGET_RED.fetch(meta);
			while(color.getRed() == r) {
				r = ra.nextInt(256);
				TARGET_RED.store(meta, r);
			}
			int g = TARGET_GREEN.fetch(meta);
			while(color.getGreen() == g) {
				g = ra.nextInt(256);
				TARGET_GREEN.store(meta, g);
			}
			int b = TARGET_BLUE.fetch(meta);
			while(color.getBlue() == b) {
				b = ra.nextInt(256);
				TARGET_BLUE.store(meta, b);
			}
			color = color.setRed(color.getRed() + this.direction(color.getRed(), r));
			color = color.setGreen(color.getGreen() + this.direction(color.getGreen(), g));
			color = color.setBlue(color.getBlue() + this.direction(color.getBlue(), b));
			meta.setColor(color);
			stack.setItemMeta(meta);
		}
	}


	@Override
	public ItemStack getStackFromPlayer(Player player) {
		return this.type.getArmor(player.getInventory());
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


	private int direction(int color, int target) {
		int x = target - color;
		return x / Math.abs(x);
	}

}
