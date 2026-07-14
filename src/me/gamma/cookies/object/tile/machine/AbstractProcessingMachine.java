
package me.gamma.cookies.object.tile.machine;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import me.gamma.cookies.object.block.machine.AbstractProcessingMachineBlock;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class AbstractProcessingMachine<T extends AbstractProcessingMachine<T, B>, B extends AbstractProcessingMachineBlock<B, T>> extends AbstractGuiMachine<T, B> {

	private static final String TAG_PROGRESS = "progress";
	private static final String TAG_GOAL = "goal";

	private int progress = 0;
	private int goal = 0;

	public AbstractProcessingMachine(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.progress = data.getInteger(TAG_PROGRESS, 0);
		this.goal = data.getInteger(TAG_GOAL, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(TAG_PROGRESS, this.progress);
		data.setInteger(TAG_GOAL, this.goal);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		this.updateProgress();
	}


	@Override
	protected boolean run() {
		// check if a goal is set
		if(this.goal == 0) {
			// try to create the next process
			this.goal = this.createNextProcess();
			if(this.goal <= 0)
				return false;

			this.progress = 0;
		}

		// continue processing
		if(this.progress < this.goal) {
			// proceed with the current process
			this.progress++;
			this.proceed();
		}

		if(this.progress >= this.goal) {
			// finish the current process
			if(!this.finishProcess())
				return false;

			this.goal = 0;
		}

		this.updateProgress();
		return true;
	}


	/**
	 * Creates a new process for this block if possible. Get's executed when the current goal is set to 0.
	 * 
	 * @return the goal for the new process or 0
	 */
	protected int createNextProcess() {
		return 0;
	}


	/**
	 * Continues working on the current task for this block. Continues when the current goal is greater than zero and the current progress is smaller than
	 * the current set goal.
	 */
	protected void proceed() {}


	/**
	 * Finishes the current process of this block. Get's executed when the progress reached the set goal.
	 * 
	 * @return returns if the process could be finished successfully.
	 */
	protected boolean finishProcess() {
		return false;
	}


	/**
	 * Returns the material for the progress icon.
	 * 
	 * @param progress the progress of the current process
	 * @return the material
	 */
	protected abstract Material getProgressMaterial(double progress);


	/**
	 * Returns the basic progress icon builder.
	 * 
	 * @param progress the progress of the current process
	 * @return the progress icon builder
	 */
	protected ItemBuilder createProgressIcon(double progress) {
		return new ItemBuilder(this.getProgressMaterial(progress));
	}


	/**
	 * Updates the progress icon in the inventory.
	 */
	protected void updateProgress() {
		double d;
		String name;
		if(this.goal == 0) {
			d = Double.NaN;
			name = "§8Progress: §7Empty";
		} else {
			d = 1.0D * progress / this.goal;
			name = String.format("§%c%s §8/ §7%s", ColorUtils.getProgressColor(d, ColorUtils.STOPLIGHT_PROGRESS), Utils.formatTicks(this.progress), Utils.formatTicks(this.goal));
		}

		double p = Double.isNaN(d) ? 0.0D : d;
		Material type = this.getProgressMaterial(p);
		ItemBuilder builder = this.createProgressIcon(p).setName(name);
		if(!Double.isNaN(d)) {
			builder.addLore("  " + ColorUtils.colorProgress(d * 100.0D, 0, 100, ColorUtils.STOPLIGHT_PROGRESS) + "%");
			builder.setDamage((int) Math.round((1.0D - d) * type.getMaxDurability())).setItemFlag(ItemFlag.HIDE_ATTRIBUTES);
		}

		builder.addLore("  §8Speed: §7" + Math.round(this.getSpeed() * 100.0D) + '%');

		this.getInventory().setItem(this.customBlock.getProgressSlot(), builder.build());
	}

}
