
package me.gamma.cookies.object.tile.machine;


import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import me.gamma.cookies.object.block.machine.DisenchanterBlock;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Disenchanter extends EnchantmentMachine<Disenchanter.DisenchanterData, Disenchanter, DisenchanterBlock> {

	public static final int DISENCHANT_DURATION = 100;

	public Disenchanter(DisenchanterBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected DisenchanterData createProcessingData() {
		return new DisenchanterData();
	}


	@Override
	protected int createEnchantmentProcess(ItemStack item, ItemStack book) {
		if(ItemUtils.isEmpty(item) || item.getEnchantments().isEmpty())
			return 0;

		if(!ItemUtils.isType(book, Material.BOOK))
			return 0;

		for(Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
			this.processing.enchantment = entry.getKey();
			this.processing.level = entry.getValue();
			return DISENCHANT_DURATION * this.processing.level;
		}

		return 0;
	}


	@Override
	protected void process(DisenchanterData data) {
		this.processing.item.removeEnchantment(this.processing.enchantment);
		this.processing.book = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.processing.book.getItemMeta();
		meta.addEnchant(this.processing.enchantment, this.processing.level, true);
		this.processing.book.setItemMeta(meta);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return progress < 0.8D ? Material.BOOK : Material.ENCHANTED_BOOK;
	}


	@Override
	public Disenchanter castTileEntity() {
		return this;
	}

	public static class DisenchanterData extends EnchantmentMachine.ProcessingData {

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
