
package me.gamma.cookies.object.block.network.pipes;


import java.util.function.DoubleBinaryOperator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.tile.network.pipes.Pipe;
import me.gamma.cookies.object.tile.network.pipes.PipePacket;



public abstract class PipeBlock<P extends PipePacket, B extends PipeBlock<P, B, T>, T extends Pipe<P, T, B>> extends AbstractCustomTileBlock<B, T> implements Cartesian {

	@Override
	public final Material getMaterial() {
		return Material.PLAYER_HEAD;
	}


	public final Vector getCenterOffset(Block block) {
		if(block.getType() == Material.PLAYER_WALL_HEAD) {
			BlockFace facing = this.getFacing(block);
			return new Vector(0.5D, 0.5D, 0.5D).subtract(facing.getDirection().multiply(0.25D));
		}

		return new Vector(0.5D, 0.25D, 0.5D);
	}


	public final Vector getFacePos(Block block, BlockFace face) {
		DoubleBinaryOperator helper = (mod, comp) -> {
			if(mod == 1)
				return 1;

			if(mod == -1)
				return 0;

			return comp;
		};

		Vector center = this.getCenterOffset(block);
		Vector entry = new Vector();

		entry.setX(helper.applyAsDouble(face.getModX(), center.getX()));
		entry.setY(helper.applyAsDouble(face.getModY(), center.getY()));
		entry.setZ(helper.applyAsDouble(face.getModZ(), center.getZ()));

		return entry;
	}

}
