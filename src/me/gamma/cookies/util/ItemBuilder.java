package me.gamma.cookies.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemBuilder {
	
	private ItemStack item;
	private ItemMeta meta;
	private List<String> lore;
	
	public ItemBuilder(Material material) {
		this(new ItemStack(material));
	}
	
	public ItemBuilder(ItemStack itemStack) {
		this.item = itemStack.clone();
		this.meta = this.item.getItemMeta();
		this.lore = this.item.getItemMeta().getLore() == null ? new ArrayList<>() : itemStack.getItemMeta().getLore();
	}
	
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setName(String name) {
		meta.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder setLocalizedName(String localizedName) {
		meta.setLocalizedName(localizedName);
		return this;
	}

	public ItemBuilder setCustomModelData(int customModelData) {
		meta.setCustomModelData(customModelData);
		return this;
	}
	
	public ItemBuilder setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}
	
	public ItemBuilder addLore(String text) {
		lore.add(text);
		return this;
	}
	
	public ItemBuilder setColor(int r, int g, int b) {
		if(item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS) {
			((LeatherArmorMeta) meta).setColor(Color.fromRGB(r, g, b));
		}
		return this;
	}
	
	public ItemBuilder setColor(Color color) {
		if(item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS) {
			((LeatherArmorMeta) meta).setColor(color);
		}
		return this;
	}
	
	public ItemBuilder addEnchantment(Enchantment enchantment, int lvl) {
		meta.addEnchant(enchantment, lvl, true);
		return this;
	}
	
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return this;
	}
	
	public ItemBuilder setItemFlag(ItemFlag flag) {
		meta.addItemFlags(flag);
		return this;
	}
	
	public ItemStack build() {
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
