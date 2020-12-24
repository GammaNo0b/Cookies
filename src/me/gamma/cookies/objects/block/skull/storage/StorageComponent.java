
package me.gamma.cookies.objects.block.skull.storage;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Utilities;



public interface StorageComponent {

	static boolean isStorageComponent(TileState block) {
		return CustomBlockSetup.getCustomBlockFromTileState(block) instanceof StorageComponent;
	}


	default TileState getStorageMonitor(TileState block) {
		try {
			return getStorageMonitor(block, new HashSet<>(), 0);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}


	default TileState getStorageMonitor(TileState block, Set<Location> checked, int connectors) {
		if(!isStorageComponent(block)) {
			return null;
		}
		if(StorageMonitor.isStorageMonitor(block)) {
			return block;
		}
		if(StorageConnector.isConnector(block)) {
			connectors++;
			if(connectors > ConfigValues.MAX_STORAGE_CONNECTORS) {
				throw new IndexOutOfBoundsException();
			}
			for(BlockFace face : Utilities.faces) {
				Block relative = block.getBlock().getRelative(face);
				if(checked.add(relative.getLocation())) {
					if(relative.getState() instanceof TileState) {
						TileState result = getStorageMonitor((TileState) relative.getState(), checked, connectors);
						if(result != null) {
							return result;
						}
					}
				}
			}
		}
		return null;
	}

}
