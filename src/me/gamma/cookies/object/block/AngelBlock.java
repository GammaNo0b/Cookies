
package me.gamma.cookies.object.block;


import me.gamma.cookies.object.list.HeadTextures;



public class AngelBlock extends AbstractCustomBlock {

	@Override
	public String getBlockTexture() {
		return HeadTextures.ANGEL_BLOCK;
	}


	@Override
	public String getIdentifier() {
		return "angel_block";
	}

	// @Override
	// public boolean onAirRightClick(Player player, ItemStack stack, PlayerInteractEvent event) {
	// Block block = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(4)).toLocation(player.getWorld()).getBlock();
	// this.setBlock(player, block, stack);
	// return true;
	// }
	//
	//
	// @Override
	// public boolean onBlockRightClick(Player player, ItemStack stack, Block block, PlayerInteractEvent event) {
	// Block target = block.getRelative(event.getBlockFace());
	// this.setBlock(player, target, stack);
	// return true;
	// }
	//
	//
	// private void setBlock(Player player, Block block, ItemStack stack) {
	// if(block.getType() != Material.AIR)
	// return;
	//
	// block.setType(Material.OBSIDIAN);
	// if(player.getGameMode() != GameMode.CREATIVE)
	// stack.setAmount(stack.getAmount() - 1);
	// }

}
