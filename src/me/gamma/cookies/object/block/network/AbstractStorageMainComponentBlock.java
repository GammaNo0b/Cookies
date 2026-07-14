
package me.gamma.cookies.object.block.network;


import java.util.stream.Stream;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.tile.network.AbstractStorageMainComponent;
import me.gamma.cookies.util.BlockUtils;



public abstract class AbstractStorageMainComponentBlock<R, B extends AbstractStorageMainComponentBlock<R, B, T>, T extends AbstractStorageMainComponent<R, T, B>> extends AbstractStorageComponentBlock<R, B, T> implements NetworkMainComponentBlock<R> {

	private final Class<R> type;

	public AbstractStorageMainComponentBlock(Class<R> type) {
		this.type = type;
	}


	@Override
	public Class<R> getType() {
		return this.type;
	}


	@Override
	public void breakComponent(Block block) {
		this.breakBlock(block);
	}


	@Override
	public Stream<Block> getPotentialNeighbors(Block block) {
		return Stream.of(BlockUtils.cartesian).mapMulti((face, consumer) -> {
			for(int i = 1; i <= 5; i++)
				consumer.accept(block.getRelative(face, i));
		});
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		T mainComponent = this.getTileEntity(block);
		if(mainComponent == null)
			return true;

		if(!mainComponent.canAccess(player)) {
			player.sendMessage("§cYou cannot access this storage network!");
			return true;
		}

		mainComponent.highlight();

		return true;
	}

}
