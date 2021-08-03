
package me.gamma.cookies.objects.block.skull;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.block.Switchable;
import me.gamma.cookies.objects.list.HeadTextures;
import me.gamma.cookies.objects.property.BooleanProperty;



public abstract class AbstractRedstoneGate extends AbstractSkullBlock implements Switchable, BlockTicker {

	private static final BooleanProperty INVERTED = new BooleanProperty("inverted");

	private Set<Location> locations = new HashSet<>();

	public AbstractRedstoneGate() {
		register();
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.REDSTONE_LOGIC_GATE;
	}


	@Override
	public void tick(TileState block) {
		@SuppressWarnings("deprecation")
		BlockFace facing = ((Skull) block).getRotation();
		BlockState state = block.getBlock().getRelative(facing.getOppositeFace()).getState();
		if(state.getBlockData() instanceof Powerable) {
			Powerable powerable = (Powerable) state.getBlockData();
			boolean power = this.calculateOutput((Skull) block, facing) ^ INVERTED.fetch(block);
			if(power != powerable.isPowered()) {
				powerable.setPowered(power);
				state.setBlockData(powerable);
				state.update();
			}
		}
	}


	@Override
	public long getDelay() {
		return 2;
	}


	@Override
	public boolean shouldTick(TileState block) {
		return this.isInstanceOf(block);
	}


	public abstract boolean calculateOutput(Skull block, BlockFace facing);


	@Override
	public void onBlockPlace(Player player, ItemStack usedItem, TileState block, BlockPlaceEvent event) {
		INVERTED.store(block, false);
		super.onBlockPlace(player, usedItem, block, event);
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.HAND) {
			INVERTED.toggle(block);
			block.update();
		}
		return false;
	}

}
