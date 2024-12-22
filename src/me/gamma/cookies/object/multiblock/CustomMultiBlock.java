
package me.gamma.cookies.object.multiblock;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;



public class CustomMultiBlock extends MultiBlock {

	private Material[][][] structure;
	private Vector trigger;
	private ClickHandler handler;


	public CustomMultiBlock(String name, Material[][][] structure, Vector trigger, ClickHandler handler) {
		super(name);
		this.structure = structure;
		this.trigger = trigger;
		this.handler = handler;
	}


	@Override
	public Material[][][] getStructure() {
		return structure;
	}


	@Override
	public Vector getTrigger() {
		return trigger;
	}


	@Override
	public void onClick(Player player, Location location, PlayerInteractEvent event) {
		handler.handle(player, location);
	}

	public static interface ClickHandler {

		void handle(Player player, Location location);

	}

}
