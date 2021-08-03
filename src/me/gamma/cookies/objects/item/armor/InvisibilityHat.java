
package me.gamma.cookies.objects.item.armor;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.item.PlayerRegister;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class InvisibilityHat extends AbstractCustomItem implements PlayerRegister {

	private Set<UUID> players = new HashSet<>();

	@Override
	public String getRegistryName() {
		return "invisibility_hat";
	}


	@Override
	public String getDisplayName() {
		return "§bInvisibility Hat";
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_HELMET;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("BNB", "P P");
		recipe.setIngredient('P', Material.GLASS_PANE);
		recipe.setIngredient('B', Material.GLASS);
		recipe.setIngredient('N', Material.NETHER_STAR);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(120, 255, 226));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public void onPlayerArmorUnequipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		this.unregisterPlayer(player);
	}


	@Override
	public void onPlayerArmorEquipItem(Player player, ItemStack stack, PlayerArmorEquipEvent event) {
		this.registerPlayer(player);
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}


	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return this.isInstanceOf(ArmorType.HELMET.getArmor(player.getInventory()));
	}


	@Override
	public void onUnregisterPlayer(Player player, boolean success) {
		Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(Cookies.INSTANCE, player));
	}


	@Override
	public void onRegisterPlayer(Player player, boolean success) {
		Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(Cookies.INSTANCE, player));
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onPlayerJoin(PlayerJoinEvent event) {
				players.stream().map(Bukkit::getPlayer).filter(player -> player != null).forEach(player -> event.getPlayer().hidePlayer(Cookies.INSTANCE, player));
			}

		};
	}

}
