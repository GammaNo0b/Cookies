
package me.gamma.cookies.object.item.resources;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.LoreBuilder.Section;
import me.gamma.cookies.object.block.network.item.ItemStorageCrateBlock;
import me.gamma.cookies.object.item.AbstractBlockItem;
import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.object.item.CustomItemData;
import me.gamma.cookies.object.tile.network.item.ItemStorageCrate;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemStorageCrateItem extends AbstractBlockItem<ItemStorageCrateBlock> {

	private final String title;

	public ItemStorageCrateItem(ItemStorageCrateBlock block, String title) {
		super(block);

		this.title = title;
	}


	@Override
	public String getTitle() {
		return this.title;
	}


	@Override
	protected void buildDescription(LoreBuilder builder, ItemMeta meta, PersistentDataObject data) {
		super.buildDescription(builder, meta, data);

		Section section = builder.createSection("", false);
		section.add("  §7Storage Capacity: §6" + this.block.getCapacity());
		section.add("  §7Max Stack Size: §6" + this.block.getMaxStackSize());
	}


	/**
	 * Stores the items inside the ingredient storage crate items inside into the result storage crate item. Used for crafting to not loose any materials
	 * stored in item crates.
	 * 
	 * @param result      the result storage crate item
	 * @param ingredients the storage crate item ingredients
	 */
	public static void storeItems(ItemStack result, ItemStack... ingredients) {
		CustomItemData resultData = getCustomData(result);
		if(resultData == null)
			return;

		List<BigItemStack> contents = new ArrayList<>();

		for(ItemStack ingredient : ingredients) {
			CustomItemData ingredientData = getCustomData(ingredient);
			if(ingredientData == null)
				continue;

			List<BigItemStack> items = PersistentDataUtils.getList(ingredientData.getData(), ItemStorageCrate.KEY_CONTENTS, null, data -> PersistentDataUtils.loadBigItemStack(data, null));
			if(items == null)
				continue;

			for(BigItemStack istack : items) {
				if(items.isEmpty())
					continue;

				int amount = istack.getAmount();
				for(BigItemStack cstack : contents) {
					if(!cstack.isSimilar(istack))
						continue;

					int space = cstack.getMaxStackSize() - cstack.getAmount();
					int transfer = Math.min(space, amount);
					cstack.setAmount(cstack.getAmount() + transfer);
					amount -= transfer;
				}

				if(amount > 0) {
					istack.setAmount(amount);
					contents.add(istack);
				}
			}
		}

		PersistentDataUtils.setList(resultData.getData(), ItemStorageCrate.KEY_CONTENTS, contents, PersistentDataUtils::saveBigItemStack);
		resultData.save();
	}

}
