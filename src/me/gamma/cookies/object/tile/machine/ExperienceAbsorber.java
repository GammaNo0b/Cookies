
package me.gamma.cookies.object.tile.machine;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.block.machine.ExperienceAbsorberBlock;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.ExperienceUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.math.MathHelper;



public class ExperienceAbsorber extends AbstractGuiMachine<ExperienceAbsorber, ExperienceAbsorberBlock> implements FluidSupplier {

	private static final String KEY_EXPERIENCE = "experience";

	private static final int EXPERIENCE_INFO_SLOT = 21;

	private final int range;

	private byte fluidOutputAccessFlags = 0x3f;
	private int experience = 0;

	public ExperienceAbsorber(ExperienceAbsorberBlock customBlock, Block block) {
		super(customBlock, block);

		this.range = customBlock.getRange();
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		this.experience = data.getInteger(KEY_EXPERIENCE, 0);

		if(!super.load(chunk, data))
			return false;

		FluidSupplier.super.load(chunk, data);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidSupplier.super.save(chunk, data);

		data.setInteger(KEY_EXPERIENCE, this.experience);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		this.updateXPLevel();
	}


	private void transferXPToAbsorber(Player player, int levels) {
		int experience = ExperienceUtils.getPlayerLevelExp(player);
		if(experience > 0)
			levels--;

		int level = player.getLevel();
		experience += ExperienceUtils.getExperienceForLevel(Math.max(level - levels, 0), level);
		int stored = this.experience / 10;
		this.experience = (stored + experience) * 10;
		player.giveExp(-experience);
	}


	private void dropXP(Player player, int levels) {
		int level = player.getLevel();
		int experience = ExperienceUtils.getExperienceForLevel(level, level + levels) - ExperienceUtils.getPlayerLevelExp(player);
		int stored = this.experience / 10;
		int transfer = Math.min(experience, stored);
		if(transfer > 0) {
			this.experience = (stored - transfer) * 10;
			ExperienceOrb orb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
			orb.setExperience(transfer);
		}
	}


	private void transferXPToPlayer(Player player, int levels) {
		int level = player.getLevel();
		int experience = ExperienceUtils.getExperienceForLevel(level, level + levels) - ExperienceUtils.getPlayerLevelExp(player);
		int stored = this.experience / 10;
		int transfer = Math.min(experience, stored);
		this.experience = (stored - transfer) * 10;
		player.giveExp(transfer);
	}


	private void updateXPLevel() {
		int experience = this.experience / 10;
		int buckets = this.experience / 1000;
		int millibuckets = this.experience - buckets * 1000;
		int levels = ExperienceUtils.getLevelFromExperience(experience);
		int partialExperience = experience - ExperienceUtils.getTotalExperience(levels);

		ItemBuilder builder = new ItemBuilder(Material.EXPERIENCE_BOTTLE);
		builder.setName("§eStored Exp: §6" + experience);
		String bucketStr = "§b" + millibuckets + " §3mb";
		if(buckets > 0)
			bucketStr = "§b" + buckets + " §3Buckets " + bucketStr;
		builder.addLore(bucketStr);
		builder.addLore("§a" + levels + " §2Levels §a" + partialExperience + " §e/ §a" + ExperienceUtils.getExperienceForNextLevel(levels) + " §2Points");
		this.getInventory().setItem(EXPERIENCE_INFO_SLOT, builder.build());
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, gui, event);
		int slot = event.getSlot();
		int action = slot / 9 - 1;
		int levels = slot - action * 9 - 13;
		if(0 <= levels && levels <= 3 && 0 <= action && action <= 2) {
			int transferLevels = MathHelper.intpow(10, levels);
			if(action == 0) {
				this.transferXPToAbsorber(player, transferLevels);
			} else if(action == 1) {
				this.dropXP(player, transferLevels);
			} else if(action == 2) {
				this.transferXPToPlayer(player, transferLevels);
			}
			this.updateXPLevel();
		}
		return true;
	}


	@Override
	public void tick() {
		this.tryPushFluid();
		super.tick();
	}


	@Override
	protected boolean run() {
		Location front = this.block.getLocation().add(0.5D, 0.5D, 0.5D).add(((Rotatable) this.block.getBlockData()).getRotation().getDirection().multiply(0.25D));
		for(Entity entity : this.block.getWorld().getNearbyEntities(this.block.getLocation().add(0.5D, 0.5D, 0.5D), this.range, this.range, this.range)) {
			if(entity instanceof ExperienceOrb) {
				ExperienceOrb orb = (ExperienceOrb) entity;
				ParticleManager.drawAnimatedLine(orb.getLocation(), front, 1, 10, pos -> pos.getWorld().spawnParticle(Particle.DUST, pos, 1, 0.1F, 0.1F, 0.1F, new Particle.DustOptions(Color.LIME, 1.0F)));
				this.experience += orb.getExperience() * 10;
				orb.remove();
				this.updateXPLevel();
				return true;
			}
		}
		return false;
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromHolder(FluidType.EXPERIENCE, Holder.create(() -> this.experience, i -> { this.experience = i; }), Integer.MAX_VALUE));
	}


	@Override
	public byte getFluidOutputAccessFlags() {
		return this.fluidOutputAccessFlags;
	}


	@Override
	public void setFluidOutputAccessFlags(byte flags) {
		this.fluidOutputAccessFlags = flags;
	}


	@Override
	public ExperienceAbsorber castTileEntity() {
		return this;
	}

}
