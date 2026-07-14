
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

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.machine.EnchanterBlock;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.util.ExperienceUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.core.MinecraftEnchantmentHelper;
import net.minecraft.util.RandomSource;



public class Enchanter extends EnchantmentMachine<Enchanter.EnchanterData, Enchanter, EnchanterBlock> implements ItemConsumer, ItemSupplier, FluidConsumer {

	public static final int ENCHANT_EXPERIENCE = 100;
	public static final int RANDOM_ENCHANT_EXPERIENCE = ExperienceUtils.getExperienceForLevel(27, 30);

	/**
	 * NMS random source for enchanting.
	 */
	private final RandomSource random = RandomSource.create();

	public Enchanter(EnchanterBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected EnchanterData createProcessingData() {
		return new EnchanterData();
	}


	@Override
	protected int createEnchantmentProcess(ItemStack item, ItemStack book) {
		// enchant simple items
		if(!ItemUtils.isCustomItem(item)) {
			int requiredExperience = 0;
			switch (item.getType()) {
				case GOLD_INGOT:
					requiredExperience = 100;
					break;
				case GOLDEN_APPLE:
					requiredExperience = 1000;
					break;
				case GLASS_BOTTLE:
					requiredExperience = 10;
					break;
				default:
					break;
			}

			if(requiredExperience != 0) {
				this.processing.enchantment = null;
				this.processing.level = 0;
				return requiredExperience;
			}
		}

		// enchant randomly
		if(ItemUtils.isType(book, Material.LAPIS_LAZULI)) {
			if(ItemUtils.isEmpty(item))
				return 0;

			if(!item.getEnchantments().isEmpty())
				return 0;

			boolean enchantable = false;
			for(Enchantment enchantment : Registry.ENCHANTMENT) {
				if(enchantment.canEnchantItem(item)) {
					enchantable = true;
					break;
				}
			}

			if(!enchantable)
				return 0;

			return RANDOM_ENCHANT_EXPERIENCE;
		}

		if(!ItemUtils.isType(book, Material.ENCHANTED_BOOK))
			return 0;

		if(!(book.getItemMeta() instanceof EnchantmentStorageMeta meta))
			return 0;

		for(Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
			// transfer one enchantment per process
			Enchantment enchantment = entry.getKey();
			if(!enchantment.canEnchantItem(item))
				continue;

			if(item.getItemMeta().hasConflictingEnchant(enchantment))
				continue;

			int level = entry.getValue();
			int toolLevel = item.getEnchantmentLevel(entry.getKey());
			if(toolLevel > level)
				continue;

			int processingLevel = 0;
			if(level == toolLevel) {
				processingLevel = level + 1;
			} else if(level > toolLevel) {
				processingLevel = level;
			}

			int maxLevel = enchantment.getMaxLevel() == 1 ? 1 : 2 * enchantment.getMaxLevel();
			if(processingLevel > maxLevel)
				continue;

			this.processing.enchantment = enchantment;
			this.processing.level = processingLevel;
			return ENCHANT_EXPERIENCE * processingLevel;
		}

		return 0;
	}


	@Override
	protected void process(EnchanterData data) {
		if(ItemUtils.isEmpty(data.book)) {
			if(!ItemUtils.isCustomItem(data.item)) {
				switch (data.item.getType()) {
					case GOLD_INGOT:
						data.item = Items.MAGIC_METAL.get();
						break;
					case GOLDEN_APPLE:
						data.item = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
						break;
					case GLASS_BOTTLE:
						data.item = new ItemStack(Material.EXPERIENCE_BOTTLE);
						break;
					default:
						break;
				}
			}
		} else if(ItemUtils.isType(data.book, Material.LAPIS_LAZULI)) {
			data.book = null;
			data.item = MinecraftEnchantmentHelper.enchantRandomly(data.item, 3, 15, this.random);
		} else {
			if(data.level > 0) {
				data.item.addUnsafeEnchantment(data.enchantment, data.level);

				if(data.book.getItemMeta() instanceof EnchantmentStorageMeta meta) {
					meta.removeStoredEnchant(data.enchantment);
					if(!meta.hasStoredEnchants()) {
						data.book = new ItemStack(Material.BOOK);
					} else {
						data.book.setItemMeta(meta);
					}
				}
			}
		}
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.ENCHANTED_BOOK;
	}


	@Override
	public Enchanter castTileEntity() {
		return this;
	}

	public static class EnchanterData extends EnchantmentMachine.ProcessingData {

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
