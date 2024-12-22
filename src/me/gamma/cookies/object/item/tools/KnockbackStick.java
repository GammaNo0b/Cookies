
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class KnockbackStick extends AbstractCustomItem {

	@Override
	public String getIdentifier() {
		return "knockback_stick";
	}


	@Override
	public String getTitle() {
		return "§dKnockback Stick";
	}


	@Override
	public Material getMaterial() {
		return Material.STICK;
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Knocks your enemies out and away.");
	}


	@Override
	public ItemStack get() {
		ItemStack stack = super.get();
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
		stack.setItemMeta(meta);
		return stack;
	}

}
