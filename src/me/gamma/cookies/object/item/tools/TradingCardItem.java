
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.tile.machine.TradingMachine.TradingData;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TradingCardItem extends AbstractCustomItem {

	public static final String KEY_TRADING_DATA = "tradingdata";

	@Override
	public String getIdentifier() {
		return "trading_card";
	}


	@Override
	public String getTitle() {
		return "§fTrading Card";
	}


	@Override
	public Material getMaterial() {
		return Material.MAP;
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		TradingData tradingData = PersistentDataUtils.get(data, KEY_TRADING_DATA, new TradingData());
		Section section = builder.createSection("§8" + tradingData.getMerchant(), true);
		section.add("  §7Trades: §3" + tradingData.getTrades());
	}


	@Override
	public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
		if(!player.isSneaking())
			return true;

		this.clearData(stack);

		return true;
	}


	@Override
	public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
		if(!player.isSneaking())
			return true;

		this.clearData(stack);

		return true;
	}


	private void clearData(ItemStack stack) {
		CustomItemData cdata = getCustomData(stack);
		TradingData data = PersistentDataUtils.get(cdata.getData(), KEY_TRADING_DATA, new TradingData());
		data.clear();
		PersistentDataUtils.set(cdata.getData(), KEY_TRADING_DATA, data);
		cdata.save();
		stack.setType(Material.MAP);
		this.updateDescription(stack);
	}


	@Override
	public boolean onEntityRightClick(Player player, ItemStack stack, Entity entity, PlayerInteractEntityEvent event) {
		if(!(entity instanceof Merchant merchant))
			return true;

		CustomItemData cdata = getCustomData(stack);
		TradingData data = PersistentDataUtils.get(cdata.getData(), KEY_TRADING_DATA, new TradingData());
		if(data.loadTrades(merchant)) {
			stack.setType(Material.FILLED_MAP);
		} else {
			stack.setType(Material.MAP);
		}
		PersistentDataUtils.set(cdata.getData(), KEY_TRADING_DATA, data);
		cdata.save();
		this.updateDescription(stack);

		return true;
	}


	public TradingData getTradingData(ItemStack stack) {
		CustomItemData data = getCustomData(stack);
		return PersistentDataUtils.get(data.getData(), KEY_TRADING_DATA, new TradingData());
	}


	public void setTradingData(ItemStack stack, TradingData data) {
		CustomItemData custom = getCustomData(stack);
		PersistentDataUtils.set(custom.getData(), KEY_TRADING_DATA, data);
		custom.save();
		this.updateDescription(stack);
	}

}
