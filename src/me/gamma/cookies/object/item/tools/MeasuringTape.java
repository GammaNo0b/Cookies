
package me.gamma.cookies.object.item.tools;


import java.text.DecimalFormat;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.VectorProperty;



public class MeasuringTape extends AbstractCustomItem {

	private static final VectorProperty MEASURE_POSITION = Properties.POS;

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
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Measures the distance between two clicked positions.").add("§7Sneak and right click to reset the measuring tape.");
	}


	@Override
	protected PropertyBuilder buildItemProperties(PropertyBuilder builder) {
		return super.buildItemProperties(builder).add(MEASURE_POSITION);
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
		ItemMeta meta = stack.getItemMeta();
		if(player.isSneaking()) {
			MEASURE_POSITION.storeEmpty(meta);
			stack.setItemMeta(meta);
			player.sendMessage("§cRolled in measuring tape!");
		} else if(clickpos != null) {
			Vector position = MEASURE_POSITION.fetch(meta);
			if(position.lengthSquared() == 0) {
				MEASURE_POSITION.store(meta, clickpos);
				stack.setItemMeta(meta);
				player.sendMessage("§cFirst Position: §6X: §e" + FORMAT.format(clickpos.getX()) + " §6Y: §e" + FORMAT.format(clickpos.getY()) + " §6Z: §e" + FORMAT.format(clickpos.getZ()));
			} else {
				player.sendMessage("§cMeasured Distance: §6" + FORMAT.format(clickpos.distance(position)) + "m");
			}
		}
	}

}
