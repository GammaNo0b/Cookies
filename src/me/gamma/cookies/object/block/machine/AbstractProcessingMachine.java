
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public abstract class AbstractProcessingMachine extends AbstractGuiMachine {

	public static final IntegerProperty PROGRESS = Properties.PROGRESS;
	public static final IntegerProperty GOAL = new IntegerProperty("goal");

	public AbstractProcessingMachine(MachineTier tier) {
		super(tier);
	}


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		this.updateProgress(block);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(PROGRESS).add(GOAL);
	}


	@Override
	protected boolean run(TileState block) {
		int goal = GOAL.fetch(block);

		// check if a goal is set
		if(goal == 0) {
			// try to create the next process
			goal = this.createNextProcess(block);
			if(goal <= 0)
				return false;

			GOAL.store(block, goal);
			PROGRESS.storeEmpty(block);
		} else {
			// continue processing
			int progress = PROGRESS.fetch(block);
			if(progress < goal) {
				// proceed with the current process
				progress++;
				PROGRESS.store(block, progress);
				this.proceed(block, progress, goal);
			} else {
				// finish the current process
				if(!this.finishProcess(block))
					return false;

				GOAL.storeEmpty(block);
			}
		}

		this.updateProgress(block);
		return true;
	}


	/**
	 * Creates a new process for the given block if possible. Get's executed when the current goal is set to 0.
	 * 
	 * @param block the block
	 * @return the goal for the new process or 0
	 */
	protected int createNextProcess(TileState block) {
		return 0;
	}


	/**
	 * Continues working on the current task for the giving block. Continues when the current goal is greater than zero and the current progress is
	 * smaller than the current set coal
	 * 
	 * @param block    the block
	 * @param progress the current progress
	 * @param goal     the goal of the current progress
	 */
	protected void proceed(TileState block, int progress, int goal) {}


	/**
	 * Finishes the current process of the given block. Get's executed when the progress reached the set goal.
	 * 
	 * @param block the block
	 * @return returns if the process could be finished successfully.
	 */
	protected boolean finishProcess(TileState block) {
		return false;
	}


	/**
	 * Returns the slot to store the progress icon.
	 * 
	 * @return the slot
	 */
	protected int getProgressSlot() {
		return MachineConstants.PROGRESS_SLOT;
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
	 * Updates the progress icon in the inventory of the given block.
	 * 
	 * @param block the block
	 */
	protected void updateProgress(TileState block) {
		int max = GOAL.fetch(block);
		double d;
		String name;
		if(max == 0) {
			d = Double.NaN;
			name = "§8Progress: §7Empty";
		} else {
			int progress = PROGRESS.fetch(block);
			d = 1.0D * progress / max;
			name = String.format("§%c%s §8/ §7%s", ColorUtils.getProgressColor(d, ColorUtils.STOPLIGHT_PROGRESS), Utils.formatTicks(progress), Utils.formatTicks(max));
		}

		double p = Double.isNaN(d) ? 0.0D : d;
		Material type = this.getProgressMaterial(p);
		ItemBuilder builder = this.createProgressIcon(p).setName(name);
		if(!Double.isNaN(d)) {
			builder.addLore("  " + ColorUtils.colorProgress(d * 100.0D, 0, 100, ColorUtils.STOPLIGHT_PROGRESS) + "%");
			builder.setDamage((int) Math.round((1.0D - d) * type.getMaxDurability())).setItemFlag(ItemFlag.HIDE_ATTRIBUTES);
		}

		builder.addLore("  §8Speed: §7" + Math.round(this.getSpeed(block) * 100.0D) + '%');

		this.getGui(block).setItem(this.getProgressSlot(), builder.build());
	}

}
