
package me.gamma.cookies.feature;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.util.RayTraceResult;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;



public class LookInBookshelfFeature implements CookieFeature, Runnable {

	private boolean enabled;

	@Override
	public void register() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Cookies.INSTANCE, this, 1, 1);
	}


	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public boolean isEnabled() {
		return this.enabled;
	}


	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!player.isSneaking())
				continue;

			RayTraceResult result = player.rayTraceBlocks(5);
			if(result == null)
				continue;

			Block target = result.getHitBlock();
			if(target == null || target.getType() != Material.CHISELED_BOOKSHELF)
				continue;

			if(!(target.getBlockData() instanceof org.bukkit.block.data.type.ChiseledBookshelf data))
				continue;

			if(result.getHitBlockFace() != data.getFacing())
				continue;

			if(!(target.getState() instanceof ChiseledBookshelf state))
				continue;

			int slot = state.getSlot(result.getHitPosition().subtract(target.getLocation().toVector()));
			ItemStack stack = state.getInventory().getItem(slot);
			if(ItemUtils.isEmpty(stack)) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(""));
				continue;
			}

			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.getBookName(stack)));
		}
	}


	private String getBookName(ItemStack stack) {
		switch (stack.getType()) {
			case WRITABLE_BOOK:
			case WRITTEN_BOOK:
				BookMeta meta = (BookMeta) stack.getItemMeta();
				String title = meta.hasTitle() ? meta.getTitle() : ItemUtils.getItemName(stack);
				return meta.hasAuthor() ? title + " by " + meta.getAuthor() : title;
			case ENCHANTED_BOOK:
				EnchantmentStorageMeta ench = (EnchantmentStorageMeta) stack.getItemMeta();
				if(ench.hasDisplayName())
					return ench.getDisplayName();

				StringBuilder builder = new StringBuilder();
				builder.append("§eEnchanted Book");
				Map<Enchantment, Integer> enchs = ench.getStoredEnchants();
				if(enchs.isEmpty())
					return builder.toString();

				builder.append("§7: ");

				List<Map.Entry<Enchantment, Integer>> list = new ArrayList<>(enchs.entrySet());
				builder.append(this.formatEnchantment(list.get(0)));
				for(int i = 1; i < list.size(); i++) {
					builder.append("§7, ");
					builder.append(this.formatEnchantment(list.get(i)));
				}
				return builder.toString();
			default:
				return ItemUtils.getItemName(stack);
		}
	}


	private String formatEnchantment(Map.Entry<Enchantment, Integer> entry) {
		return String.format("§d%s§7: §b%s", Utils.toCapitalWords(entry.getKey().getKey().getKey().replace('_', ' ')), Utils.romanNumber(entry.getValue()));
	}

}
