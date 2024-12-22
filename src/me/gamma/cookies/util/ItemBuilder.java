
package me.gamma.cookies.util;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionType;

import me.gamma.cookies.object.IItemSupplier;



/**
 * Class to build {@link ItemStack} in a simple way.
 * 
 * @author gamma
 *
 */
public class ItemBuilder {

	private ItemStack stack;
	private ItemMeta meta;
	private List<String> lore;

	public ItemBuilder(Material material) {
		this(() -> new ItemStack(material));
	}


	public ItemBuilder(ItemStack itemStack) {
		this(itemStack::clone);
	}


	public ItemBuilder(IItemSupplier supplier) {
		this.stack = supplier.get();
		this.meta = this.stack.getItemMeta();
		this.lore = this.stack.getItemMeta() == null || this.stack.getItemMeta().getLore() == null ? new ArrayList<>() : this.stack.getItemMeta().getLore();
	}


	public ItemBuilder setAmount(int amount) {
		this.stack.setAmount(amount);
		return this;
	}


	public ItemBuilder setName(String name) {
		this.meta.setDisplayName(name);
		return this;
	}


	@Deprecated
	public ItemBuilder setLocalizedName(String localizedName) {
		this.meta.setLocalizedName(localizedName);
		return this;
	}


	public ItemBuilder setHeadTexture(String texture) {
		if(this.meta instanceof SkullMeta skull)
			GameProfileHelper.setSkullTexture(skull, texture);
		return this;
	}


	public ItemBuilder setCustomModelData(int customModelData) {
		this.meta.setCustomModelData(customModelData);
		return this;
	}


	public ItemBuilder setLore(List<String> lore) {
		this.lore = lore;
		return this;
	}


	public ItemBuilder addLore(String text) {
		this.lore.add(text);
		return this;
	}


	public ItemBuilder setTexture(String texture) {
		GameProfileHelper.setSkullTexture((SkullMeta) this.meta, texture);
		return this;
	}


	public ItemBuilder setColor(int r, int g, int b) {
		return this.setColor(Color.fromRGB(r, g, b));
	}


	public ItemBuilder setColor(Color color) {
		if(this.meta instanceof LeatherArmorMeta lmeta)
			lmeta.setColor(color);

		if(this.meta instanceof PotionMeta pmeta)
			pmeta.setColor(color);

		if(this.meta instanceof FireworkEffectMeta fmeta) {
			FireworkEffect effect = fmeta.getEffect();
			if(effect == null)
				effect = FireworkEffect.builder().withColor(Color.GRAY).build();

			fmeta.setEffect(FireworkEffect.builder().with(effect.getType()).withColor(color).withFade(effect.getFadeColors()).flicker(effect.hasFlicker()).trail(effect.hasTrail()).build());
		}

		return this;
	}


	public ItemBuilder setDamage(int damage) {
		if(this.meta instanceof Damageable damageable)
			damageable.setDamage(damage);

		return this;
	}


	public ItemBuilder addEnchantment(Enchantment enchantment, int lvl) {
		this.meta.addEnchant(enchantment, lvl, true);
		return this;
	}


	public ItemBuilder setUnbreakable(boolean unbreakable) {
		this.meta.setUnbreakable(unbreakable);
		return this;
	}


	public ItemBuilder setBasePotionType(PotionType type) {
		if(this.meta instanceof PotionMeta pmeta)
			pmeta.setBasePotionType(type);

		return this;
	}


	public ItemBuilder setItemFlag(ItemFlag flag) {
		this.meta.addItemFlags(flag);
		return this;
	}


	public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
		this.meta.addAttributeModifier(attribute, modifier);
		return this;
	}


	public ItemStack build() {
		this.meta.setLore(this.lore);
		this.stack.setItemMeta(this.meta);
		return this.stack;
	}

}
