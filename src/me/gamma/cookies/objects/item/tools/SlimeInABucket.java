
package me.gamma.cookies.objects.item.tools;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftSlime;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.list.CustomModelDataValues;
import me.gamma.cookies.objects.property.NBTProperty;
import me.gamma.cookies.objects.recipe.RecipeCategory;
import me.gamma.cookies.objects.recipe.ShowcaseRecipe;
import me.gamma.cookies.util.Utilities;
import net.minecraft.nbt.NBTTagCompound;



public class SlimeInABucket extends AbstractCustomItem {

	public static final NBTProperty NBT_DATA = new NBTProperty("nbt");

	@Override
	public String getRegistryName() {
		return "slime_in_a_bucket";
	}


	@Override
	public String getDisplayName() {
		return "§aSlime in a Bucket";
	}


	@Override
	public Material getMaterial() {
		return Material.BUCKET;
	}


	public ItemStack createDefaultItemStack(Slime slime) {
		ItemStack stack = this.createDefaultItemStack();
		ItemMeta meta = stack.getItemMeta();
		NBT_DATA.store(meta, ((CraftSlime) slime).getHandle().save(new NBTTagCompound()));
		stack.setItemMeta(meta);
		return stack;
	}


	@Override
	public Recipe getRecipe() {
		return new ShowcaseRecipe(this.createDefaultItemStack(), RecipeCategory.FUN);
	}


	@Override
	public int getCustomModelData() {
		return CustomModelDataValues.SLIME_IN_A_BUCKET;
	}


	@Override
	public Listener getCustomListener() {
		return new Listener() {

			@EventHandler
			public void onSlimeInteract(PlayerInteractAtEntityEvent event) {
				if(event.getRightClicked().getType() != EntityType.SLIME)
					return;
				Player player = event.getPlayer();
				ItemStack stack = player.getInventory().getItemInMainHand();
				if(Utilities.isEmpty(stack))
					return;
				if(stack.getType() != Material.BUCKET || AbstractCustomItem.isCustomItem(stack))
					return;
				event.setCancelled(true);
				Slime slime = (Slime) event.getRightClicked();
				player.getInventory().setItemInMainHand(SlimeInABucket.this.createDefaultItemStack(slime));
				slime.remove();
				player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 10.0F, 1.0F);
			}


			@SuppressWarnings("deprecation")
			@EventHandler
			public void onWorldInteract(PlayerInteractEvent event) {
				if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
					return;
				if(event.isCancelled())
					return;
				Player player = event.getPlayer();
				ItemStack stack = player.getInventory().getItemInMainHand();
				if(Utilities.isEmpty(stack))
					return;
				if(!SlimeInABucket.this.isInstanceOf(stack))
					return;
				event.setCancelled(true);
				Block block = event.getClickedBlock();
				Location offset = block.getRelative(event.getBlockFace()).getLocation().add(0.5D, 0.0D, 0.5D);
				Slime slime = (Slime) offset.getWorld().spawnEntity(offset, EntityType.SLIME);
				((CraftSlime) slime).getHandle().load(NBT_DATA.fetch(stack.getItemMeta()));
				slime.teleport(offset);
				slime.setRemoveWhenFarAway(false);
				player.playSound(player.getLocation(), Sound.ENTITY_SLIME_JUMP, 10.0F, 1.0F);
				player.getInventory().setItemInMainHand(new ItemStack(Material.BUCKET));
			}

		};
	}

}
