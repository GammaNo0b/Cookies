
package me.gamma.cookies.objects.item.tools;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.block.skull.TextureChanger;
import me.gamma.cookies.objects.item.AbstractSkullItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.BooleanProperty;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.setup.CustomItemSetup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;



public class PortableEndPortal extends AbstractSkullItem implements TextureChanger {

	public static final BooleanProperty OPEN = new BooleanProperty("open");

	@Override
	protected String getBlockTexture() {
		return HeadTextures.END_PORTAL_CLOSED;
	}


	@Override
	public String getRegistryName() {
		return "portable_end_portal";
	}


	@Override
	public String getDisplayName() {
		return "§aPortable §2End §3Portal";
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Right click the Dragon Egg to activate the portal!");
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		OPEN.storeEmpty(meta);
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("OEO", "SSS");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('S', Material.END_STONE);
		recipe.setIngredient('E', CustomItemSetup.ENDER_CRYSTAL.createDefaultItemStack());
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.handleRightClick(player, stack, null);
		super.onAirRightClick(player, stack, event);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleRightClick(player, stack, block);
		super.onBlockRightClick(player, stack, block, event);
	}


	private void handleRightClick(Player player, ItemStack portal, Block block) {
		if(this.isOpen(portal)) {
			ResourceKey<World> dimension = ((CraftPlayer) player).getHandle().getWorldServer().getDimensionKey();
			if(dimension.equals(World.f)) {
				this.teleportEnd(player, ((CraftServer) Bukkit.getServer()).getHandle().getServer().getWorldServer(World.h).getWorld());
			} else if(dimension.equals(World.h)) {
				this.teleportOverworld(player, ((CraftServer) Bukkit.getServer()).getHandle().getServer().getWorldServer(World.f).getWorld());
			} else {
				player.sendMessage("§cThis Item doesn't work in this dimension!");
			}
		} else if(block != null && block.getType() == Material.DRAGON_EGG) {
			this.setOpen(portal, true);
		} else {
			player.sendMessage("§cThis Portal is not already opened!");
		}
	}


	private void teleportOverworld(Player player, CraftWorld world) {
		Location location = player.getBedSpawnLocation();
		if(location == null)
			location = world.getSpawnLocation();
		player.teleport(location);
	}


	private void teleportEnd(Player player, CraftWorld world) {
		player.teleport(new Location(world, 100, 50, 0));
	}


	public boolean isOpen(ItemStack portal) {
		if(!this.isInstanceOf(portal))
			return false;
		return OPEN.fetch(portal.getItemMeta());
	}


	public void setOpen(ItemStack portal, boolean open) {
		if(!this.isInstanceOf(portal))
			return;
		ItemMeta meta = portal.getItemMeta();
		OPEN.store(meta, open);
		portal.setItemMeta(meta);
		this.changeBlockTexture(portal, open ? HeadTextures.END_PORTAL_OPEN : HeadTextures.END_PORTAL_CLOSED);
	}

}
