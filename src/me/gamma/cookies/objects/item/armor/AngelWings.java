
package me.gamma.cookies.objects.item.armor;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;



public class AngelWings extends AbstractCustomItem {

	@Override
	public String getRegistryName() {
		return "angel_wings";
	}


	@Override
	public String getDisplayName() {
		return "§bAngel's Wings";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Fly like in Creative!");
	}


	@Override
	public Material getMaterial() {
		return Material.ELYTRA;
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.ANGEL_WINGS;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("F F", "FEF", "DND");
		recipe.setIngredient('F', Material.FEATHER);
		recipe.setIngredient('E', Material.ELYTRA);
		recipe.setIngredient('D', Material.DIAMOND);
		recipe.setIngredient('N', Material.NETHER_STAR);
		return recipe;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onChestEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == ArmorType.CHESTPLATE) {
					if(isInstanceOf(event.getOldArmor()))
						event.getPlayer().setAllowFlight(false);
					if(isInstanceOf(event.getNewArmor()))
						event.getPlayer().setAllowFlight(true);
				}
			}


			@EventHandler
			public void onPlayerGlide(EntityToggleGlideEvent event) {
				Entity entity = event.getEntity();
				if(!(entity instanceof Player))
					return;
				Player player = (Player) entity;
				if(!AngelWings.this.isInstanceOf(player.getInventory().getChestplate()))
					return;
				if(event.isGliding())
					event.setCancelled(true);
			}


			@EventHandler
			public void onServerReload(ServerLoadEvent event) {
				for(Player player : Bukkit.getOnlinePlayers())
					this.checkPlayerFlight(player);
			}


			@EventHandler
			public void onServerEnter(PlayerJoinEvent event) {
				this.checkPlayerFlight(event.getPlayer());
			}


			@EventHandler
			public void onWorldEnter(PlayerChangedWorldEvent event) {
				this.checkPlayerFlight(event.getPlayer());
			}


			@EventHandler
			public void onDamage(EntityDamageEvent event) {
				if(event.getCause() == DamageCause.FLY_INTO_WALL && event.getEntity() instanceof Player && isInstanceOf(ArmorType.CHESTPLATE.getArmor(((Player) event.getEntity()).getInventory()))) {
					event.setCancelled(true);
				}
			}


			private void checkPlayerFlight(Player player) {
				if(AngelWings.this.isInstanceOf(player.getInventory().getChestplate()))
					player.setAllowFlight(true);
			}

		};
	}

}
