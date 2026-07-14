
package me.gamma.cookies.object.block;


import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.LED;
import me.gamma.cookies.util.GameProfileHelper;



public class LEDBlock extends AbstractCustomTileBlock<LEDBlock, LED> {

	private final String registryName;
	private final String texturePowered;

	public LEDBlock(String registryName, String texturePowered) {
		this.registryName = registryName;
		this.texturePowered = texturePowered;
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
	public boolean onBlockBreak(Player player, Block block, BlockBreakEvent event) {
		if(!super.onBlockBreak(player, block, event))
			return false;

		LED led = this.getTileEntity(block);
		if(led != null)
			led.removeLights();

		return false;
	}


	public void updateTexture(Block block, boolean powered) {
		if(block.getState() instanceof Skull skull)
			GameProfileHelper.setSkullTexture(skull, powered ? this.texturePowered : this.getBlockTexture());
	}


	@Override
	public LEDBlock castCustomBlock() {
		return this;
	}


	@Override
	public LED createNewTileEntity(Block block) {
		return new LED(this, block);
	}

}
