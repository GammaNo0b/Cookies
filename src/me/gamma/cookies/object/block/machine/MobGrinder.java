
package me.gamma.cookies.object.block.machine;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.core.MinecraftEntityHelper;



public class MobGrinder extends AbstractGuiMachine {

	private int frequency;
	private double damage;

	public MobGrinder() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.frequency = config.getInt("frequency", 20);
		this.damage = config.getDouble("damage", 0.0D);
	}


	@Override
	public String getTitle() {
		return "§bMob Grinder";
	}


	@Override
	public String getMachineRegistryName() {
		return "mob_grinder";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.MOB_GRINDER;
	}


	@Override
	protected int getWorkPeriod() {
		return this.frequency;
	}


	@Override
	public int rows() {
		return 3;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = Bukkit.createInventory(null, this.rows() * 9);
		InventoryUtils.fillLeftRight(gui, InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE));
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i = 10; i < 17; i++)
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 11;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 12;
	}


	@Override
	protected int getUpgradeSlot() {
		return 14;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 15;
	}


	@Override
	protected boolean run(TileState block) {
		BlockFace facing = ((Rotatable) block.getBlockData()).getRotation();
		Vector direction = facing.getDirection();
		for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D).subtract(direction), 0.5D, 2.5D, 0.5D)) {
			if(entity instanceof LivingEntity) {
				LivingEntity living = (LivingEntity) entity;
				if(living.isDead())
					continue;

				living.damage(this.damage);
				Player owner = this.getOwningPlayer(block);
				if(owner != null)
					MinecraftEntityHelper.setLastHurtByPlayer(living, owner);
				return true;
			}
		}
		return false;
	}


	public String vectorToString(Vector vector) {
		return String.format("%.3f | %.3f | %.3f", vector.getX(), vector.getY(), vector.getZ());
	}

	/*
	 * @Override public Listener getListener() { return new Listener() {
	 * 
	 * @EventHandler public void onEntityKill(EntityDeathEvent event) { LivingEntity entity = event.getEntity(); Byte b = LOOTING.fetch(entity); if(b ==
	 * null) return;
	 * 
	 * List<ItemStack> copy = new ArrayList<>(event.getDrops()); for(int i = 0; i < b; i++) event.getDrops().addAll(copy); }
	 * 
	 * }; }
	 */

}
