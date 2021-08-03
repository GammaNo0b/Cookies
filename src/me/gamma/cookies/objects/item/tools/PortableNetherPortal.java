
package me.gamma.cookies.objects.item.tools;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import me.gamma.cookies.objects.item.AbstractSkullItem;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;



public class PortableNetherPortal extends AbstractSkullItem {

	@Override
	protected String getBlockTexture() {
		return HeadTextures.NETHER_PORTAL;
	}


	@Override
	public String getRegistryName() {
		return "portable_nether_portal";
	}


	@Override
	public String getDisplayName() {
		return "§5Portable §cNether §6Portal";
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.MISCELLANEOUS, RecipeType.CUSTOM);
		recipe.setShape("OOO", "OSO", "OOO");
		recipe.setIngredient('O', Material.OBSIDIAN);
		recipe.setIngredient('S', Material.FLINT_AND_STEEL);
		return recipe;
	}


	@Override
	public void onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		this.handleRightClick(player);
		super.onAirRightClick(player, stack, event);
	}


	@Override
	public void onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		this.handleRightClick(player);
		super.onBlockRightClick(player, stack, block, event);
	}


	private void handleRightClick(Player player) {
		ResourceKey<World> dimension = ((CraftPlayer) player).getHandle().getWorldServer().getDimensionKey();
		if(dimension.equals(World.f)) {
			this.teleportNether(player, ((CraftServer) Bukkit.getServer()).getHandle().getServer().getWorldServer(World.g).getWorld());
		} else if(dimension.equals(World.g)) {
			this.teleportOverworld(player, ((CraftServer) Bukkit.getServer()).getHandle().getServer().getWorldServer(World.f).getWorld());
		} else {
			player.sendMessage("§cThis Item doesn't work in this dimension!");
		}
	}


	private void teleportOverworld(Player player, CraftWorld world) {
		Location location = player.getBedSpawnLocation();
		if(location == null)
			location = world.getSpawnLocation();
		player.teleport(location);
	}


	private void teleportNether(Player player, CraftWorld world) {
		Location location = world.getSpawnLocation();
		Block block = location.getBlock();
		if(block.getType().isSolid())
			block.setType(Material.AIR);
		Block down = block.getRelative(BlockFace.DOWN);
		if(!down.getType().isSolid())
			down.setType(Material.NETHERRACK);
		Block up = block.getRelative(BlockFace.UP);
		if(up.getType().isSolid())
			up.setType(Material.AIR);
		player.teleport(location);
	}

}
