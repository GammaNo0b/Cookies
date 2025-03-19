
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.EnumProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class Quarry extends AbstractItemProcessingMachine {

	private static final int MAX_SIZE = 64;

	public static final EnumProperty<Quarry.State> STATE = new EnumProperty<>("state", State.class);
	public static final IntegerProperty WIDTH = Properties.WIDTH;
	public static final IntegerProperty LENGTH = Properties.LENGTH;
	public static final VectorProperty BREAK_POS = Properties.POS;
	public static final ListProperty<ItemStack, ItemStackProperty> DROPS = new ListProperty<>("drops", ItemStackProperty::new);

	private static final int STATE_SLOT = 18;

	private List<String> dimensions;
	private boolean blacklisted;

	public Quarry() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.dimensions = config.getStringList("dimensions");
		this.blacklisted = config.getBoolean("blacklisted", true);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		inventory.setItem(STATE_SLOT, STATE.fetch(block).icon);
	}


	@Override
	public String getTitle() {
		return "§dQuarry";
	}


	@Override
	public String getMachineRegistryName() {
		return "quarry";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.QUARRY;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.NETHERITE_PICKAXE;
	}


	@Override
	public int rows() {
		return 5;
	}


	private boolean isValidDimension(World world) {
		return this.dimensions.contains(world.getName()) != this.blacklisted;
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(STATE).add(WIDTH).add(LENGTH).add(BREAK_POS).add(DROPS);
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillTopBottom(gui, filler);
		gui.setItem(9, filler);
		gui.setItem(27, filler);
		for(int i : new int[] { 12, 21, 28, 29, 30 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : new int[] { 14, 23, 32 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	private void updateState(TileState block, Quarry.State state) {
		STATE.store(block, state);
		this.getGui(block).setItem(STATE_SLOT, state.icon);
	}


	private void resetBreakPos(TileState block) {
		BREAK_POS.store(block, new Vector(0, block.getY() - 1, 0));
	}


	@Override
	protected int[] getInputSlots() {
		return new int[] { 10, 11, 19, 20 };
	}


	@Override
	protected int[] getOutputSlots() {
		return new int[] { 15, 16, 17, 24, 25, 26, 33, 34, 35 };
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, block, gui, event))
			return false;

		if(event.getSlot() == STATE_SLOT) {
			Quarry.State state = STATE.fetch(block);
			boolean left = event.getClick().isLeftClick();
			boolean right = event.getClick().isRightClick();
			switch (state) {
				case SETUP:
					if(left) {
						if(this.searchQuarryMarkers(block)) {
							this.resetBreakPos(block);
							this.updateState(block, State.START);
						}
					}
					break;
				case START:
					if(left) {
						this.updateState(block, State.MINING);
					} else if(right) {
						this.updateState(block, State.SETUP);
					}
					break;
				case MINING:
					if(left || right) {
						if(right)
							this.resetBreakPos(block);
						this.updateState(block, State.START);
					}
					break;
				case FINISHED:
					if(left) {
						this.resetBreakPos(block);
						this.updateState(block, State.START);
					}
					break;
				default:
					break;
			}
			block.update();
		}

		return true;
	}


	private boolean searchQuarryMarkers(TileState block) {
		Location pos = block.getLocation();
		int width = searchInDirection(pos, false);
		if(width == 0)
			return false;

		int length = searchInDirection(pos, true);
		if(length == 0)
			return false;

		WIDTH.store(block, width);
		LENGTH.store(block, length);

		pos.add(0.5D, 0.5D, 0.5D);

		ParticleManager.drawLine(pos, pos.clone().add(width, 0, 0), Math.abs(width) + 1, 240, 0, 0);
		ParticleManager.drawLine(pos, pos.clone().add(0, 0, length), Math.abs(length) + 1, 240, 0, 0);

		return true;
	}


	private int searchInDirection(Location pos, boolean z) {
		Location l;
		for(int i = 1; i <= MAX_SIZE; i++) {
			l = pos.clone().add(z ? 0 : i, 0, z ? i : 0);
			if(l.getBlock().getType() == Material.REDSTONE_TORCH)
				return i;

			int j = -i;
			l = pos.clone().add(z ? 0 : j, 0, z ? j : 0);
			if(l.getBlock().getType() == Material.REDSTONE_TORCH)
				return j;
		}

		return 0;
	}


	private Block nextBlock(TileState block) {
		Vector old = BREAK_POS.fetch(block);

		int x = old.getBlockX();
		if(x == -1) {
			return null;
		}

		int y = old.getBlockY();
		int z = old.getBlockZ();

		int width = WIDTH.fetch(block);
		int length = LENGTH.fetch(block);

		boolean done = false;
		if(++x >= Math.abs(width)) {
			x = 0;

			if(++z >= Math.abs(length)) {
				z = 0;

				if(y <= block.getWorld().getMinHeight())
					done = true;

				y--;
			}
		}

		BREAK_POS.store(block, new Vector(done ? -1 : x, y, z));

		int dx = width > 0 ? 1 : -1;
		int dz = length > 0 ? 1 : -1;
		Location location = block.getLocation().add(dx * old.getBlockX(), 0, dz * old.getBlockZ()).add(dx, 0, dz);
		location.setY(old.getBlockY());
		return location.getBlock();
	}


	@Override
	protected int createNextProcess(TileState block) {
		if(STATE.fetch(block) != Quarry.State.MINING)
			return 0;

		if(!this.isValidDimension(block.getWorld()))
			return 0;

		Block b = this.nextBlock(block);
		if(b == null) {
			this.updateState(block, State.FINISHED);
			return 0;
		} else if(b.isEmpty()) {
			return 1;
		}

		if(b.isLiquid()) {
			b.setType(Material.AIR);
			return 40;
		}

		if(BlockUtils.isCustomBlock(b))
			return 10;

		ItemStack bestTool = null;
		int bestSpeed = BlockUtils.getBlockBreakTime(b, null);
		if(bestSpeed <= 0)
			return 10;

		for(Provider<ItemStack> provider : this.getItemInputs(block)) {
			ItemStack tool = ItemProvider.getStack(provider);
			if(ItemUtils.isEmpty(tool))
				continue;

			int speed = Math.max(1, BlockUtils.getBlockBreakTime(b, tool));
			if(speed < bestSpeed) {
				bestSpeed = speed;
				bestTool = tool;
			}
		}

		List<ItemStack> drops = new ArrayList<>();
		drops.addAll(b.getDrops(bestTool));
		if(b.getState() instanceof BlockInventoryHolder holder) {
			Inventory inventory = holder.getInventory();
			drops.addAll(Arrays.asList(inventory.getContents()));
			inventory.clear();
		}
		DROPS.store(block, drops);

		Location bottom = block.getLocation().add(0.5D, 0.0D, 0.5D);
		Location start = b.getLocation().add(0.5D, 0.5D, 0.5D);
		final BlockData data = b.getBlockData().clone();
		ParticleManager.drawAnimatedLine(start, bottom, 1, bestSpeed, pos -> pos.getWorld().spawnParticle(Particle.BLOCK, pos, 1, 0.1F, 0.1F, 0.1F, data));
		b.getWorld().playSound(b.getLocation(), b.getBlockData().getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
		b.setType(Material.AIR);

		return bestSpeed;
	}


	@Override
	protected boolean finishProcess(TileState block) {
		List<ItemStack> results = DROPS.fetch(block);
		while(!this.storeOutputs(block, results)) {
			if(!this.tryPushItems(block)) {
				DROPS.store(block, results);
				return false;
			}
		}

		DROPS.store(block, results);
		while(this.tryPushItems(block));

		return true;
	}


	@Override
	public List<ItemStack> getDrops(TileState block) {
		List<ItemStack> drops = super.getDrops(block);

		drops.addAll(DROPS.fetch(block));

		return drops;
	}


	@Override
	protected int getInputModeSlot() {
		return 38;
	}

	private static enum State {

		SETUP("§2Setup", Material.BLUE_STAINED_GLASS_PANE, "§7Click to search for markers."),
		START("§eStart", Material.YELLOW_STAINED_GLASS_PANE, "§7L-Click to start mining,", "§7R-Click to select new markers."),
		MINING("§aMining", Material.GREEN_STAINED_GLASS_PANE, "§7L-Click to pause mining,", "§7R-Click to reset mining."),
		FINISHED("§cFinished", Material.RED_STAINED_GLASS_PANE, "§7Click to restart mining.");

		private final ItemStack icon;

		private State(String name, Material icon, String... description) {
			this.icon = new ItemBuilder(icon).setName(name).setLore(List.of(description)).build();
		}

	}

}
