
package me.gamma.cookies.object.block.redstone;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;

import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockTicker;
import me.gamma.cookies.object.block.Switchable;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;



public class WirelessRedstoneTransmitter extends AbstractCustomBlock implements Switchable, BlockTicker {

	public static final ByteProperty REDSTONE_FREQUENCY = Properties.REDSTONE_FREQUENCY;
	public static final BooleanProperty LAST_POWERED = new BooleanProperty("lastpowered");

	private static final Set<Location> locations = new HashSet<>();

	@Override
	public String getBlockTexture() {
		return HeadTextures.REDSTONE_TRANSMITTER;
	}


	@Override
	public String getIdentifier() {
		return "wireless_redstone_transmitter";
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(LAST_POWERED);
	}


	@Override
	public Set<Location> getLocations() {
		return locations;
	}


	@Override
	public void tick(TileState block) {
		int frequency = REDSTONE_FREQUENCY.fetch(block) & 0xFF;
		boolean powered = this.isBlockPowered(block);
		boolean lastpowered = LAST_POWERED.fetch(block);
		if(powered != lastpowered) {
			WirelessRedstoneReceiver.setBlockPower(frequency, powered || this.isOtherPowered(frequency));
			LAST_POWERED.store(block, powered);
			block.update();
		}
	}


	private boolean isOtherPowered(int checkFrequency) {
		for(Location location : locations) {
			if(location.getBlock().getState() instanceof Skull) {
				Skull skull = (Skull) location.getBlock().getState();
				if(this.isInstanceOf(skull)) {
					int frequency = REDSTONE_FREQUENCY.fetch(skull) & 0xFF;
					if(frequency == checkFrequency) {
						if(this.isBlockPowered(skull)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
