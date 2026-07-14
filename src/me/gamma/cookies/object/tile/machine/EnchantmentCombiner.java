
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import me.gamma.cookies.object.block.machine.EnchantmentCombinerBlock;
import me.gamma.cookies.object.tile.machine.EnchantmentCombiner.CombinerData;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class EnchantmentCombiner extends EnchantmentMachine<CombinerData, EnchantmentCombiner, EnchantmentCombinerBlock> {

	public static final int COMBINING_DURATION = 100;

	public EnchantmentCombiner(EnchantmentCombinerBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected CombinerData createProcessingData() {
		return new CombinerData();
	}


	@Override
	protected int createEnchantmentProcess(ItemStack item, ItemStack book) {
		if(!(item.getItemMeta() instanceof EnchantmentStorageMeta meta1))
			return 0;

		if(!(book.getItemMeta() instanceof EnchantmentStorageMeta meta2))
			return 0;

		for(var entry : meta1.getStoredEnchants().entrySet()) {
			Enchantment enchantment = entry.getKey();
			int level = entry.getValue();

			if(meta2.hasConflictingStoredEnchant(enchantment))
				continue;

			int level2 = meta2.getStoredEnchantLevel(enchantment);
			if(level < level2) {
				continue;
			} else if(level > level2) {
				this.processing.level = level;
			} else {
				this.processing.level = level + 1;
			}

			this.processing.enchantment = enchantment;

			return this.processing.level * COMBINING_DURATION;
		}

		return 0;
	}


	@Override
	protected void process(CombinerData data) {
		if(!(data.item.getItemMeta() instanceof EnchantmentStorageMeta meta1))
			return;

		if(!(data.book.getItemMeta() instanceof EnchantmentStorageMeta meta2))
			return;

		meta1.removeStoredEnchant(data.enchantment);
		meta2.addStoredEnchant(data.enchantment, data.level, true);

		data.book.setItemMeta(meta2);
		if(meta1.hasStoredEnchants()) {
			data.item.setItemMeta(meta1);
		} else {
			data.item = new ItemStack(Material.BOOK);
		}
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.ENCHANTED_BOOK;
	}


	@Override
	public EnchantmentCombiner castTileEntity() {
		return this;
	}

	public static class CombinerData extends EnchantmentMachine.ProcessingData {

		private static final String KEY_PROCESSING_ENCHANTMENT = "processingenchantment";
		private static final String KEY_PROCESSING_LEVEL = "processinglevel";

		private Enchantment enchantment = null;
		private int level = 0;

		@Override
		public boolean load(Chunk chunk, PersistentDataObject data) {
			if(!super.load(chunk, data))
				return false;

			NamespacedKey enchantmentKey = PersistentDataUtils.getKey(data, KEY_PROCESSING_ENCHANTMENT);
			this.enchantment = enchantmentKey == null ? null : Registry.ENCHANTMENT.get(enchantmentKey);
			this.level = data.getInteger(KEY_PROCESSING_LEVEL, 0);

			return true;
		}


		@Override
		public boolean save(Chunk chunk, PersistentDataObject data) {
			if(!super.save(chunk, data))
				return false;

			NamespacedKey enchantmentKey = this.enchantment == null ? null : this.enchantment.getKeyOrNull();
			if(enchantmentKey != null)
				PersistentDataUtils.setKey(data, KEY_PROCESSING_ENCHANTMENT, enchantmentKey);
			data.setInteger(KEY_PROCESSING_LEVEL, this.level);

			return true;
		}


		@Override
		protected void empty() {
			super.empty();

			this.enchantment = null;
			this.level = 0;
		}

	}

}
