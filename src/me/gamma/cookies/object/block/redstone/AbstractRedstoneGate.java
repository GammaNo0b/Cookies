
package me.gamma.cookies.object.block.redstone;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockTicker;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;



public abstract class AbstractRedstoneGate extends AbstractCustomBlock implements Switchable, BlockTicker {

	public static final BooleanProperty INVERTED = Properties.INVERTED;

	private Set<Location> locations = new HashSet<>();

	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.REDSTONE_LOGIC_GATE;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(INVERTED);
	}


	@Override
	public void tick(TileState block) {
		BlockFace facing = ((Rotatable) block).getRotation();
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


	public abstract boolean calculateOutput(Skull block, BlockFace facing);


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(event.getHand() == EquipmentSlot.HAND) {
			INVERTED.toggle(block);
			block.update();
		}
		return false;
	}

}
