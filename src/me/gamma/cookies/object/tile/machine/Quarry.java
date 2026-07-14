
package me.gamma.cookies.object.tile.machine;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.object.block.machine.QuarryBlock;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Quarry extends AbstractItemProcessingMachine<Quarry, QuarryBlock> {

	private static final String KEY_STATE = "state";
	private static final String KEY_WIDTH = "width";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_BREAK_POS = "breakpos";
	private static final String KEY_DROPS = "drops";

	private static final int MAX_SIZE = 64;

	private static final int STATE_SLOT = 18;

	private State state = State.SETUP;
	private int width = 0;
	private int length = 0;
	private final Vector breakPos = new Vector();
	private final List<ItemStack> drops = new ArrayList<>();

	public Quarry(QuarryBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.state = PersistentDataUtils.getEnum(data, KEY_STATE, State.class);
		if(this.state == null)
			return false;

		this.width = data.getInteger(KEY_WIDTH, 0);
		this.length = data.getInteger(KEY_LENGTH, 0);
		if(PersistentDataUtils.getVector(data, KEY_BREAK_POS, this.breakPos) == null)
			return false;

		if(PersistentDataUtils.getList(data, KEY_DROPS, this.drops, PersistentDataUtils::loadItemStack) == null)
			return false;

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setEnum(data, KEY_STATE, this.state);
		data.setInteger(KEY_WIDTH, this.width);
		data.setInteger(KEY_LENGTH, this.length);
		PersistentDataUtils.setVector(data, KEY_BREAK_POS, this.breakPos);
		PersistentDataUtils.setList(data, KEY_DROPS, this.drops, PersistentDataUtils::saveItemStack);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		inventory.setItem(STATE_SLOT, this.state.icon);
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.NETHERITE_PICKAXE;
	}


	private void updateState(State state) {
		this.state = state;
		this.getInventory().setItem(STATE_SLOT, state.icon);
	}


	private void resetBreakPos() {
		this.breakPos.setX(0);
		this.breakPos.setY(this.block.getY() - 1);
		this.breakPos.setZ(0);
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
	public void destroy() {
		super.destroy();

		for(ItemStack drop : this.drops)
			ItemUtils.dropItem(drop, this.block);
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, gui, event))
			return false;

		if(event.getSlot() == STATE_SLOT) {
			boolean left = event.getClick().isLeftClick();
			boolean right = event.getClick().isRightClick();
			switch (this.state) {
				case SETUP:
					if(left) {
						if(this.searchQuarryMarkers()) {
							this.resetBreakPos();
							this.updateState(State.START);
						}
					}
					break;
				case START:
					if(left) {
						this.updateState(State.MINING);
					} else if(right) {
						this.updateState(State.SETUP);
					}
					break;
				case MINING:
					if(left || right) {
						if(right)
							this.resetBreakPos();
						this.updateState(State.START);
					}
					break;
				case FINISHED:
					if(left) {
						this.resetBreakPos();
						this.updateState(State.START);
					}
					break;
				default:
					break;
			}
		}

		return true;
	}


	private boolean searchQuarryMarkers() {
		Location pos = this.block.getLocation();
		this.width = this.searchInDirection(pos, false);
		if(this.width == 0)
			return false;

		this.length = this.searchInDirection(pos, true);
		if(this.length == 0)
			return false;

		pos.add(0.5D, 0.5D, 0.5D);

		ParticleManager.drawLine(pos, pos.clone().add(this.width, 0, 0), Math.abs(this.width) + 1, 240, 0, 0);
		ParticleManager.drawLine(pos, pos.clone().add(0, 0, this.length), Math.abs(this.length) + 1, 240, 0, 0);

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


	private Block nextBlock() {
		Vector old = this.breakPos;

		int x = old.getBlockX();
		if(x == -1)
			return null;

		int y = old.getBlockY();
		int z = old.getBlockZ();

		boolean done = false;
		if(++x >= Math.abs(this.width)) {
			x = 0;

			if(++z >= Math.abs(this.length)) {
				z = 0;

				if(y <= this.block.getWorld().getMinHeight())
					done = true;

				y--;
			}
		}

		this.breakPos.setX(done ? -1 : x);
		this.breakPos.setY(y);
		this.breakPos.setZ(z);

		int dx = this.width > 0 ? 1 : -1;
		int dz = this.length > 0 ? 1 : -1;
		Location location = this.block.getLocation().add(dx * old.getBlockX(), 0, dz * old.getBlockZ()).add(dx, 0, dz);
		location.setY(old.getBlockY());
		return location.getBlock();
	}


	@Override
	protected int createNextProcess() {
		if(this.state != State.MINING)
			return 0;

		if(!this.customBlock.isValidDimension(this.block.getWorld()))
			return 0;

		Block b = this.nextBlock();
		if(b == null) {
			this.updateState(State.FINISHED);
			return 0;
		} else if(b.isEmpty()) {
			return 1;
		}

		if(b.isLiquid()) {
			b.setType(Material.AIR);
			return 40;
		}

		if(CustomBlockStorage.BLOCK_STORAGE.isCustomBlock(b))
			return 10;

		ItemStack bestTool = null;
		int bestSpeed = BlockUtils.getBlockBreakTime(b, null);
		if(bestSpeed <= 0)
			return 10;

		for(Provider<ItemStack> provider : this.getItemInputs()) {
			ItemStack tool = ItemProvider.getStack(provider);
			if(ItemUtils.isEmpty(tool))
				continue;

			int speed = Math.max(1, BlockUtils.getBlockBreakTime(b, tool));
			if(speed < bestSpeed) {
				bestSpeed = speed;
				bestTool = tool;
			}
		}

		this.drops.clear();
		this.drops.addAll(b.getDrops(bestTool));
		if(b.getState() instanceof BlockInventoryHolder holder) {
			Inventory inventory = holder.getInventory();
			this.drops.addAll(Arrays.asList(inventory.getContents()));
			inventory.clear();
		}

		Location bottom = this.block.getLocation().add(0.5D, 0.0D, 0.5D);
		Location start = b.getLocation().add(0.5D, 0.5D, 0.5D);
		final BlockData data = b.getBlockData().clone();
		ParticleManager.drawAnimatedLine(start, bottom, 1, bestSpeed, pos -> pos.getWorld().spawnParticle(Particle.BLOCK, pos, 1, 0.1F, 0.1F, 0.1F, data));
		b.getWorld().playSound(b.getLocation(), b.getBlockData().getSoundGroup().getBreakSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
		b.setType(Material.AIR);

		return bestSpeed;
	}


	@Override
	protected boolean finishProcess() {
		while(!this.storeOutputs(this.drops)) {
			if(!this.tryPushItems()) {
				return false;
			}
		}

		while(this.tryPushItems());

		return true;
	}


	@Override
	public Quarry castTileEntity() {
		return this;
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
