
package me.gamma.cookies.objects.item.armor;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.gamma.cookies.event.PlayerArmorEquipEvent;
import me.gamma.cookies.objects.recipe.CustomRecipe;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.RecipeType;
import me.gamma.cookies.util.Utilities;



public class HasteArmorPiece extends AbstractCustomArmorItem {

	private ArmorType type;
	private String identifier;
	private String name;

	public HasteArmorPiece(ArmorType type) {
		this.type = type;
		this.identifier = "haste_" + type.name().toLowerCase();
		this.name = "§c" + Utilities.toCapitalWords("Haste " + type.name());
	}


	@Override
	public String getRegistryName() {
		return this.identifier;
	}


	@Override
	public String getDisplayName() {
		return this.name;
	}


	@Override
	public List<String> getDescription() {
		return Arrays.asList("§7Each piece grants an additional level of Haste!");
	}


	@Override
	public ArmorType getArmorType() {
		return this.type;
	}


	@Override
	public ArmorMaterial getArmorMaterial() {
		return ArmorMaterial.LEATHER;
	}


	@Override
	public ItemStack createDefaultItemStack() {
		ItemStack stack = super.createDefaultItemStack();
		LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
		meta.setColor(Color.fromRGB(255, 181, 0));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		CustomRecipe recipe = new CustomRecipe(this.createDefaultItemStack(), RecipeCategory.ARMOR, RecipeType.CUSTOM);
		recipe.setShape("GSG", "SAS", "GSG");
		recipe.setIngredient('A', this.getMaterial());
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('S', Material.SUGAR);
		return recipe;
	}


	@Override
	public Listener getCustomListener() {
		return this.type != ArmorType.HELMET ? null : new Listener() {

			@EventHandler
			public void onArmorEquip(PlayerArmorEquipEvent event) {
				final Player player = event.getPlayer();
				int count = HasteArmorPiece.this.countArmorPieces(player);
				if(count > 0)
					player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, count));
			}

		};
	}


	private int countArmorPieces(Player player) {
		int count = 0;
		for(ArmorType type : ArmorType.values()) {
			ItemStack armor = type.getArmor(player.getInventory());
			if(armor != null && armor.getType() != Material.AIR && ("haste_" + type.name().toLowerCase()).equals(IDENTIFIER.fetch(armor.getItemMeta())))
				count++;
		}
		return count;
	}

}
