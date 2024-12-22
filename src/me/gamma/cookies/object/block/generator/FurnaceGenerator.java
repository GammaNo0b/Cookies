
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.type.Furnace;

import me.gamma.cookies.object.list.HeadTextures;



public class FurnaceGenerator extends AbstractGenerator {

	public FurnaceGenerator() {
		super(null);
	}


	@Override
	public String getGeneratorRegistryName() {
		return "furnace_generator";
	}


	@Override
	public String getTitle() {
		return "§fFurnace Generator";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FURNACE_GENERATOR;
	}


	@Override
	protected boolean fullfillsGeneratingConditions(TileState block) {
		Block down = block.getBlock().getRelative(0, -1, 0);
		return down.getBlockData() instanceof Furnace furnace && furnace.isLit();
	}

}
