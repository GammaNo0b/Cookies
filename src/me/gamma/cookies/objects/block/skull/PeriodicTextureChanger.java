package me.gamma.cookies.objects.block.skull;

import org.bukkit.block.Skull;
import org.bukkit.block.TileState;

import me.gamma.cookies.objects.block.BlockTicker;

public interface PeriodicTextureChanger extends TextureChanger, BlockTicker {
	
	String getBlockTexture(TileState block);
	
	@Override
	default void tick(TileState block) {
		if(block instanceof Skull)
			this.changeBlockTexture((Skull) block, this.getBlockTexture(block));
	}

}
