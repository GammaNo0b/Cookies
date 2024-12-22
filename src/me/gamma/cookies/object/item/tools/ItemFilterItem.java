
package me.gamma.cookies.object.item.tools;


import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



public class ItemFilterItem extends AbstractCustomItem implements Predicate<ItemStack> {

	private final String identifier;
	private final String name;
	private final String description;
	private final Predicate<ItemStack> filter;

	public ItemFilterItem(String name, String description, Predicate<ItemStack> filter) {
		this.identifier = Utils.getIdentifierFromName(name);
		this.name = name;
		this.description = description;
		this.filter = filter;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getTitle() {
		return this.name;
	}


	@Override
	public Material getMaterial() {
		return Material.MAP;
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		super.getDescription(builder, holder);
		builder.createSection(this.description, false);
	}


	@Override
	public boolean test(ItemStack t) {
		return this.filter.test(t);
	}


	public static ItemFilterItem of(String name, String description, Predicate<Material> filter) {
		return new ItemFilterItem(name, description, stack -> !ItemUtils.isEmpty(stack) && filter.test(stack.getType()));
	}

}
