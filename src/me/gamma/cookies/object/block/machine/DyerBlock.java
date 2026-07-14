
package me.gamma.cookies.object.block.machine;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataHolder;

import io.netty.util.internal.MathUtil;
import me.gamma.cookies.manager.ParticleManager;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.gui.util.ColorGrid;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.ColorProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.tile.machine.Dyer;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.core.MinecraftItemHelper;
import me.gamma.cookies.util.math.MathHelper;



public class DyerBlock extends AbstractItemProcessingMachineBlock<DyerBlock, Dyer> {

	public DyerBlock() {
		super(null);
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.DYE_FABRICATOR;
	}


	@Override
	public String getMachineRegistryName() {
		return "dyer";
	}


	@Override
	public String getTitle() {
		return "§fDyer";
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));

		ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		gui.setItem(12, filler);
		gui.setItem(21, filler);
		gui.setItem(30, filler);

		for(int i : new int[] { 9, 10, 11, 18, 20, 27, 28, 29 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);

		for(int i : new int[] { 15, 16, 17, 24, 26, 33, 34, 35 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);

		gui.setItem(23, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§7Color").build());

		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Block block, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == Dyer.RANDOMIZE_COLOR_SLOT) {
			Dyer dyer = this.getTileEntity(block);
			if(dyer != null)
				dyer.toggleRandomizeColor();
		} else if(slot == Dyer.COLOR_DISPLAY_SLOT) {
			Dyer dyer = this.getTileEntity(block);
			if(dyer != null)
				ColorGrid.openColorWheel(player, dyer.createColorHolder());
		}

		return super.onMainInventoryInteract(player, block, gui, event);
	}


	@Override
	public DyerBlock castCustomBlock() {
		return this;
	}


	@Override
	public Dyer createNewTileEntity(Block block) {
		return new Dyer(this, block);
	}

}
