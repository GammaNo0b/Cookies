
package me.gamma.cookies.objects.block.skull.storage;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.Holder;
import me.gamma.cookies.util.Utilities;



public interface StorageComponent {

	static boolean isStorageComponent(TileState block) {
		return CustomBlockSetup.getCustomBlockFromTileState(block) instanceof StorageComponent;
	}


	default TileState getStorageMonitor(TileState block) {
		try {
			Holder<Integer> connectors = new Holder<>(0);
			Set<Location> checked = new HashSet<>();
			for(BlockFace face : Utilities.faces) {
				Block relative = block.getBlock().getRelative(face);
				if(relative.getState() instanceof TileState) {
					TileState monitor = getStorageMonitor((TileState) relative.getState(), checked, connectors);
					if(monitor != null) {
						return monitor;
					}
				}
			}
		} catch(IndexOutOfBoundsException e) {
			System.out.println("To many components!");
		}
		return null;
	}


	default TileState getStorageMonitor(TileState block, Set<Location> checked, Holder<Integer> connectors) {
		if(!isStorageComponent(block)) {
			return null;
		}
		if(StorageMonitor.isStorageMonitor(block)) {
			return block;
		}
		if(StorageConnector.isConnector(block)) {
			int value = connectors.get() + 1;
			if(connectors.get() > ConfigValues.MAX_STORAGE_CONNECTORS) {
				throw new IndexOutOfBoundsException();
			}
			connectors.accept(value);
			for(BlockFace face : Utilities.faces) {
				Block relative = block.getBlock().getRelative(face);
				if(relative.getState() instanceof TileState) {
					if(checked.add(relative.getLocation())) {
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
