
package me.gamma.cookies.object.block.redstone;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Powerable;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.TileBlockRegister;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.util.BlockUtils;



public class WirelessRedstoneReceiver extends AbstractCustomBlock implements TileBlockRegister {

	public static final ByteProperty REDSTONE_FREQUENCY = Properties.REDSTONE_FREQUENCY;

	private final Set<Location> locations = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.REDSTONE_RECEIVER;
	}


	@Override
	public String getIdentifier() {
		return "wireless_redstone_receiver";
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	private void update(int frequency, boolean powered) {
		for(Location location : this.locations) {
			Block block = location.getBlock();
			TileState state = (TileState) block.getState();
			if(this.isInstanceOf(state)) {
				if((REDSTONE_FREQUENCY.fetch(state) & 0xFF) == frequency) {
					for(BlockFace face : BlockUtils.cartesian) {
						Block relative = block.getRelative(face);
						if(relative.getBlockData() instanceof Powerable) {
							Powerable powerable = (Powerable) relative.getBlockData();
							powerable.setPowered(powered);
							relative.setBlockData(powerable);
						}
					}
				}
			}
		}
	}


	public static void setBlockPower(int frequency, boolean powered) {
		Blocks.WIRELESS_REDSTONE_RECEIVER.update(frequency, powered);
	}

}
