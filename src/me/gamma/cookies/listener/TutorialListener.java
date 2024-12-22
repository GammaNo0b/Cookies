
package me.gamma.cookies.listener;


import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.property.UUIDArrayProperty;
import me.gamma.cookies.util.ItemUtils;



public class TutorialListener implements Listener, WorldPersistentDataStorage {

	private static final UUIDArrayProperty TUTORIALISTS = new UUIDArrayProperty("tutorialists");

	private final HashSet<UUID> players = new HashSet<>();

	public TutorialListener() {
		this.register();
	}


	@Override
	public String getIdentifier() {
		return "tutorial";
	}


	@Override
	public void load(World world, PersistentDataContainer container) {
		UUID[] uuids = TUTORIALISTS.fetchEmpty(container);
		for(UUID uuid : uuids)
			this.players.add(uuid);
	}


	@Override
	public void save(World world, PersistentDataContainer container) {
		TUTORIALISTS.store(container, this.players.toArray(UUID[]::new));
		this.players.clear();
	}


	@EventHandler
	public void onCookieCraft(CraftItemEvent event) {
		if(!(event.getWhoClicked() instanceof Player player))
			return;

		ItemStack result = event.getRecipe().getResult();
		if(result.getType() != Material.COOKIE)
			return;

		if(ItemUtils.isCustomItem(result))
			return;

		if(!this.players.add(player.getUniqueId()))
			return;

		player.sendMessage("§6Craft a cookie cook book using a book and surround it with a bottle of honey, a cocoa bean, a cookie and some sugar!");
		player.sendMessage("§6This paper may help!");
		ItemUtils.giveItemToPlayer(player, Items.CUSTOM_CRAFTING_TABLE_BLUEPRINT.get());
	}

}
