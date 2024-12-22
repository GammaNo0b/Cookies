
package me.gamma.cookies.feature;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockRedstoneEvent;



public class ChangeRepeaterDelayFeature implements CookieListener {

	private boolean enabled = false;

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public boolean isEnabled() {
		return this.enabled;
	}


	@EventHandler
	public void onRedstoneChange(BlockRedstoneEvent event) {
		if(event.getOldCurrent() > 0 || event.getNewCurrent() == 0)
			return;

		Block block = event.getBlock();
		if(block.getType() != Material.COMPARATOR)
			return;

		Comparator comparator = (Comparator) block.getBlockData();
		BlockFace cfacing = comparator.getFacing();
		Block target = block.getRelative(cfacing);
		if(target.getType() != Material.REPEATER)
			return;

		Repeater repeater = (Repeater) target.getBlockData();
		BlockFace rfacing = repeater.getFacing();
		if(rfacing == cfacing || rfacing == cfacing.getOppositeFace())
			return;

		repeater.setDelay((repeater.getDelay() + 1) & 0x11);
		target.setBlockData(repeater);
	}

}
