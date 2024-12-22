
package me.gamma.cookies.object.item.tools;


import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;



public class NoobSword extends AbstractCustomItem {

	private double accidentChance;

	@Override
	public void configure(ConfigurationSection config) {
		this.accidentChance = config.getDouble("accidentChance", 0.0D);
	}


	@Override
	public String getIdentifier() {
		return "noob_sword";
	}


	@Override
	public String getTitle() {
		return "§dNoob Sword";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("Only the biggest noobs are worthy of holding this sword.");
	}


	@Override
	public Material getMaterial() {
		return Material.STONE_SWORD;
	}


	@Override
	public boolean onEntityDamage(Player player, ItemStack stack, Entity damaged, EntityDamageByEntityEvent event) {
		if(new Random().nextDouble() < this.accidentChance) {
			player.damage(event.getDamage());
			player.sendMessage("§cYou had an accident!");
			return true;
		}
		return false;
	}

}
