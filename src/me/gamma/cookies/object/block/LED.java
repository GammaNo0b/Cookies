
package me.gamma.cookies.object.block;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.GameProfileHelper;



public class LED extends AbstractCustomBlock implements BlockTicker, Switchable {

	public static final BooleanProperty POWERED = Properties.POWERED;

	private Set<Location> locations = new HashSet<>();

	private final String registryName;
	private final String texturePowered;

	public LED(String registryName, String texturePowered) {
		this.registryName = registryName;
		this.texturePowered = texturePowered;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.LED_OFF;
	}


	@Override
	public String getIdentifier() {
		return this.registryName;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(POWERED);
	}


	private void enableLights(Block block) {
		for(BlockFace face : BlockUtils.cartesian) {
			Block relative = block.getRelative(face);
			if(relative.getType().isAir()) {
				relative.setType(Material.LIGHT);
				Light light = (Light) relative.getBlockData();
				light.setLevel(15);
				relative.setBlockData(light);
			}
		}
	}


	private void removeLights(Block block) {
		for(BlockFace face : BlockUtils.cartesian) {
			Block relative = block.getRelative(face);
			if(relative.getType() == Material.LIGHT)
				relative.setType(Material.AIR);
		}
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		this.removeLights(block.getBlock());
		return false;
	}


	@Override
	public void tick(TileState block) {
		boolean power = this.isBlockPowered(block);
		if(POWERED.fetch(block) != power) {
			GameProfileHelper.setSkullTexture((Skull) block, power ? this.texturePowered : HeadTextures.LED_OFF);
			POWERED.store(block, power);
			block.update();

			if(!power)
				this.removeLights(block.getBlock());
		}
		if(power)
			this.enableLights(block.getBlock());
	}

}
