
package me.gamma.cookies.object.item.tools;


import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.LoreBuilder;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.property.ByteProperty;



public class LightningBow extends AbstractCustomItem {

	private static final ByteProperty LIGHTNING_PROJECTILE = new ByteProperty("islightningprojectile");

	@Override
	public String getIdentifier() {
		return "lightning_bow";
	}


	@Override
	public String getTitle() {
		return "§bLightning Bow";
	}


	@Override
	public void getDescription(LoreBuilder builder, PersistentDataHolder holder) {
		builder.createSection(null, true).add("§7Strikes lightnings when hitting an entity or block.");
	}


	@Override
	public Material getMaterial() {
		return Material.BOW;
	}


	@Override
	public Listener getListener() {
		return new Listener() {

			@EventHandler
			public void onBowShoot(EntityShootBowEvent event) {
				if(LightningBow.this.isInstanceOf(event.getBow())) {
					LIGHTNING_PROJECTILE.storeEmpty(event.getProjectile());
				}
			}


			@EventHandler
			public void onProjectileCollide(ProjectileHitEvent event) {
				Projectile projectile = event.getEntity();
				if(LIGHTNING_PROJECTILE.isPropertyOf(projectile)) {
					projectile.getWorld().strikeLightning(projectile.getLocation());
				}
			}

		};
	}

}
