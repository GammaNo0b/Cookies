
package me.gamma.cookies.object.block.network.item;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.network.Network;
import me.gamma.cookies.object.network.NetworkMainComponent;
import me.gamma.cookies.object.network.TransferRate;
import me.gamma.cookies.object.property.LongProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;



public class StorageMainComponent extends AbstractCustomBlock implements NetworkMainComponent<ItemStack> {

	private static final LongProperty LAST_UPDATED = Properties.TIMESTAMP;

	private final Set<Location> locations = new HashSet<>();

	@Override
	public String getIdentifier() {
		return "storage_main_component";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_MAIN_COMPONENT;
	}


	@Override
	public Class<ItemStack> getType() {
		return ItemStack.class;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public TransferRate<ItemStack> getTransferRate() {
		return ItemStack::getMaxStackSize;
	}


	@Override
	public void breakComponent(TileState block) {
		// Bukkit.getPluginManager().callEvent(new BlockBreakEvent(block.getBlock(), this.getOwningPlayer(block)));
		block.setType(Material.AIR);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(LAST_UPDATED);
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		if(player == null)
			return false;

		if(this.checkForAdjacentNotOwnedNetworks(player.getUniqueId(), block.getLocation()))
			return false;

		return super.canPlace(player, block);
	}


	@Override
	public boolean onBlockPlace(Player player, PersistentDataHolder holder, TileState block) {
		if(player == null || !this.setup(block, player.getUniqueId()))
			return true;

		return super.onBlockPlace(player, holder, block);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		this.destroy(block);
		return false;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(!this.canAccess(block, player))
			return true;

		long time = block.getWorld().getTime();
		long timestamp = LAST_UPDATED.fetch(block);
		if(time - timestamp < 100)
			return true;

		LAST_UPDATED.store(block, time);
		block.update();

		Network<ItemStack> network = this.getNetwork(block);
		network.highlightNetwork();

		return true;
	}

}
