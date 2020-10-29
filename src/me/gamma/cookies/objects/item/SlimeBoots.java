
package me.gamma.cookies.objects.item;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.Vec3D;



public class SlimeBoots extends AbstractCustomItem implements ItemTicker {

	private Map<UUID, Double> tick3 = new HashMap<>();
	private Map<UUID, Double> tick2 = new HashMap<>();
	private Map<UUID, Double> tick1 = new HashMap<>();
	private Set<UUID> players = new HashSet<>();

	@Override
	public String getIdentifier() {
		return "slime_boots";
	}


	@Override
	public String getDisplayName() {
		return "§aSlime Boots";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Living Bouncy Ball!");
	}


	@Override
	public Material getMaterial() {
		return Material.LEATHER_BOOTS;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("B B", "S S");
		recipe.setIngredient('B', Material.SLIME_BALL);
		recipe.setIngredient('S', Material.SLIME_BLOCK);
		return recipe;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(0, 255, 31));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Set<UUID> getPlayers() {
		return this.players;
	}


	@Override
	public boolean shouldRegisterPlayer(Player player) {
		return isInstanceOf(ArmorType.BOOTS.getArmor(player.getInventory()));
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public ItemStack getStackFromPlayer(Player player) {
		return ArmorType.BOOTS.getArmor(player.getInventory());
	}


	@Override
	public void tick(Player player, ItemStack stack) {
		tick3.put(player.getUniqueId(), tick2.get(player.getUniqueId()));
		tick2.put(player.getUniqueId(), tick1.get(player.getUniqueId()));
		tick1.put(player.getUniqueId(), ((CraftPlayer) player).getHandle().getMot().getY());
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onPlayerEquip(PlayerArmorEquipEvent event) {
				if(event.getType() == ArmorType.BOOTS) {
					if(isInstanceOf(event.getOldArmor()))
						unregisterPlayer(event.getPlayer());
					if(isInstanceOf(event.getNewArmor()))
						registerPlayer(event.getPlayer());
				}
			}


			@EventHandler
			public void onPlayerMove(PlayerMoveEvent event) {
				Player player = event.getPlayer();
				if(!player.isSneaking() && !this.isOnGround(event.getFrom()) && this.isOnGround(event.getTo()) && isInstanceOf(ArmorType.BOOTS.getArmor(player.getInventory()))) {
					EntityPlayer eplayer = ((CraftPlayer) player).getHandle();
					Vec3D mot = eplayer.getMot();
					if(mot.y < -0.05D) {
						Double y = -tick3.get(player.getUniqueId());
						if(y != null) {
							eplayer.setMot(mot.x, y, mot.z);
							eplayer.velocityChanged = true;
							player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 10.0F, 1.0F);
						}
					}
				}
			}


			@EventHandler
			public void onFallDamage(EntityDamageEvent event) {
				if(event.getEntity() instanceof Player && event.getCause() == DamageCause.FALL && isRegistered((Player) event.getEntity())) {
					event.setDamage(0.0D);
				}
			}


			private boolean isOnGround(Location location) {
				return location.clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() != Material.AIR && location.getY() == Math.floor(location.getY());
			}

		};
	}

}
