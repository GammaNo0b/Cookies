
package me.gamma.cookies.object.block.machine;


import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.ExperienceUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.math.MathHelper;



public class ExperienceAbsorber extends AbstractGuiMachine implements FluidSupplier {

	public static final IntegerProperty STORED_XP = new IntegerProperty("xp");

	private static final int EXPERIENCE_INFO_SLOT = 21;

	private int frequency;
	private int range;

	public ExperienceAbsorber() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.frequency = config.getInt("frequency", 20);
		this.range = config.getInt("range", 4);
	}


	@Override
	public String getTitle() {
		return "§bExperience Absorber";
	}


	@Override
	public String getMachineRegistryName() {
		return "experience_absorber";
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public int getWorkPeriod() {
		return this.frequency;
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.EXPERIENCE_ABSORBER;
	}


	@Override
	public List<FluidProvider> getFluidOutputs(TileState block) {
		return List.of(FluidProvider.fromProperty(FluidType.EXPERIENCE, STORED_XP, block, Integer.MAX_VALUE));
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(9, 11, 18, 20, 27, 29))
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i : ArrayUtils.array(12, 17, 21, 26, 30, 35))
			gui.setItem(i, filler);

		gui.setItem(13, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 1 Level").build());
		gui.setItem(14, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 10 Levels").build());
		gui.setItem(15, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore 100 Levels").build());
		gui.setItem(16, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aStore all Levels").build());
		gui.setItem(22, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 1 Level").build());
		gui.setItem(23, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 10 Levels").build());
		gui.setItem(24, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop 100 Levels").build());
		gui.setItem(25, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setName("§6Drop all Levels").build());
		gui.setItem(31, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 1 Level").build());
		gui.setItem(32, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 10 Levels").build());
		gui.setItem(33, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve 100 Levels").build());
		gui.setItem(34, new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setName("§dRetrieve all Levels").build());

		return gui;
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		this.updateXPLevel(block);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(STORED_XP).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		super.onMainInventoryInteract(player, block, gui, event);
		int slot = event.getSlot();
		int action = slot / 9 - 1;
		int levels = slot - action * 9 - 13;
		if(0 <= levels && levels <= 3 && 0 <= action && action <= 2) {
			int transferLevels = MathHelper.intpow(10, levels);
			if(action == 0) {
				this.transferXPToAbsorber(player, block, transferLevels);
			} else if(action == 1) {
				this.dropXP(player, block, transferLevels);
			} else if(action == 2) {
				this.transferXPToPlayer(player, block, transferLevels);
			}
			this.updateXPLevel(block);
		}
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState block, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	protected boolean run(TileState block) {
		Location front = block.getLocation().add(0.5D, 0.5D, 0.5D).add(((Rotatable) block.getBlockData()).getRotation().getDirection().multiply(0.25D));
		for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation().add(0.5D, 0.5D, 0.5D), this.range, this.range, this.range)) {
			if(entity instanceof ExperienceOrb) {
				ExperienceOrb orb = (ExperienceOrb) entity;
				ParticleManager.drawAnimatedLine(orb.getLocation(), front, 1, 10, pos -> pos.getWorld().spawnParticle(Particle.DUST, pos, 1, 0.1F, 0.1F, 0.1F, new Particle.DustOptions(Color.LIME, 1.0F)));
				STORED_XP.increase(block, orb.getExperience() * 10);
				block.update();
				orb.remove();
				this.updateXPLevel(block);
				return true;
			}
		}
		return false;
	}


	private void transferXPToAbsorber(Player player, TileState block, int levels) {
		int experience = ExperienceUtils.getPlayerLevelExp(player);
		if(experience > 0)
			levels--;

		int level = player.getLevel();
		experience += ExperienceUtils.getExperienceForLevel(Math.max(level - levels, 0), level);
		int stored = STORED_XP.fetch(block) / 10;
		STORED_XP.store(block, (stored + experience) * 10);
		block.update();
		player.giveExp(-experience);
	}


	private void dropXP(Player player, TileState block, int levels) {
		int level = player.getLevel();
		int experience = ExperienceUtils.getExperienceForLevel(level, level + levels) - ExperienceUtils.getPlayerLevelExp(player);
		int stored = STORED_XP.fetch(block) / 10;
		int transfer = Math.min(experience, stored);
		if(transfer > 0) {
			STORED_XP.store(block, (stored - transfer) * 10);
			block.update();
			ExperienceOrb orb = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
			orb.setExperience(transfer);
		}
	}


	private void transferXPToPlayer(Player player, TileState block, int levels) {
		int level = player.getLevel();
		int experience = ExperienceUtils.getExperienceForLevel(level, level + levels) - ExperienceUtils.getPlayerLevelExp(player);
		int stored = STORED_XP.fetch(block) / 10;
		int transfer = Math.min(experience, stored);
		STORED_XP.store(block, (stored - transfer) * 10);
		block.update();
		player.giveExp(transfer);
	}


	private void updateXPLevel(TileState block) {
		int fluid = STORED_XP.fetch(block);
		int experience = fluid / 10;
		int buckets = fluid / 1000;
		int millibuckets = fluid - buckets * 1000;
		int levels = ExperienceUtils.getLevelFromExperience(experience);
		int partialExperience = experience - ExperienceUtils.getTotalExperience(levels);

		ItemBuilder builder = new ItemBuilder(Material.EXPERIENCE_BOTTLE);
		builder.setName("§eStored Exp: §6" + experience);
		String bucketStr = "§b" + millibuckets + " §3mb";
		if(buckets > 0)
			bucketStr = "§b" + buckets + " §3Buckets " + bucketStr;
		builder.addLore(bucketStr);
		builder.addLore("§a" + levels + " §2Levels §a" + partialExperience + " §e/ §a" + ExperienceUtils.getExperienceForNextLevel(levels) + " §2Points");
		this.getGui(block).setItem(EXPERIENCE_INFO_SLOT, builder.build());
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 28;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 19;
	}


	@Override
	protected int getUpgradeSlot() {
		return 10;
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 1;
	}

}
