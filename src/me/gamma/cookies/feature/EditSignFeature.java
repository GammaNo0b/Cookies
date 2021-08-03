
package me.gamma.cookies.feature;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.util.ReflectionUtils;
import me.gamma.cookies.util.Utilities;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.item.ItemDye;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntitySign;



public class EditSignFeature implements CookieListener {

	private final Set<UUID> players = new HashSet<>();

	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		Player player = event.getPlayer();
		if(player.isSneaking())
			return;

		ItemStack stack = player.getInventory().getItemInMainHand();
		if(!Utilities.isEmpty(stack)) {
			if(stack.getType() == Material.GLOW_INK_SAC)
				return;

			if(CraftItemStack.asNMSCopy(stack).getItem() instanceof ItemDye)
				return;
		}

		Block block = event.getClickedBlock();
		if(block == null)
			return;

		BlockState state = block.getState();
		if(!(state instanceof Sign))
			return;

		players.add(player.getUniqueId());
		this.editSign(player, (Sign) state, true);
	}


	@EventHandler
	public void onSignEdit(SignChangeEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if(players.remove(uuid))
			this.editSign(event.getPlayer(), (Sign) event.getBlock().getState(), false);
	}


	private void editSign(Player player, Sign sign, boolean edit) {
		TileEntitySign tesign = (TileEntitySign) ReflectionUtils.findAndInvokeMethod(CraftBlockEntityState.class, sign, TileEntity.class);
		EntityPlayer eplayer = ((CraftPlayer) player).getHandle();
		tesign.a(player.getUniqueId());
		tesign.a(edit);
		tesign.update();
		eplayer.b.sendPacket(tesign.getUpdatePacket());
		if(edit)
			eplayer.b.sendPacket(new PacketPlayOutOpenSignEditor(tesign.getPosition()));
	}

}
