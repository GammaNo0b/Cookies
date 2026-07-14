
package me.gamma.cookies.object.item.tools;


import java.text.DecimalFormat;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class MeasuringTape extends AbstractCustomItem {

	private static final String KEY_MEASURE_POSITION = "measurepos";

	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	@Override
	protected String getBlockTexture() {
		return HeadTextures.MEASURING_TAPE;
	}


	@Override
	public String getIdentifier() {
		return "measuring_tape";
	}


	@Override
	public String getTitle() {
		return "§eMeasuring Tape";
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		builder.createSection(null, true).add("§7Measures the distance between two clicked positions.").add("§7Sneak and right click to reset the measuring tape.");
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		RayTraceResult result = event.getPlayer().rayTraceBlocks(5.0D);
		this.handleClick(player, stack, result == null ? null : result.getHitPosition());
		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		RayTraceResult result = event.getPlayer().rayTraceBlocks(5.0D);
		this.handleClick(player, stack, result.getHitPosition());
		return true;
	}


	private void handleClick(Player player, ItemStack stack, Vector clickpos) {
		CustomItemData data = getCustomData(stack);
		if(player.isSneaking()) {
			data.getData().remove(KEY_MEASURE_POSITION);
			data.save();
			player.sendMessage("§cRolled in measuring tape!");
		} else if(clickpos != null) {
			Vector position = PersistentDataUtils.getVector(data.getData(), KEY_MEASURE_POSITION);
			if(position == null) {
				PersistentDataUtils.setVector(data.getData(), KEY_MEASURE_POSITION, clickpos);
				data.save();
				player.sendMessage("§cFirst Position: §6X: §e" + FORMAT.format(clickpos.getX()) + " §6Y: §e" + FORMAT.format(clickpos.getY()) + " §6Z: §e" + FORMAT.format(clickpos.getZ()));
			} else {
				player.sendMessage("§cMeasured Distance: §6" + FORMAT.format(clickpos.distance(position)) + "m");
			}
		}
	}

}
