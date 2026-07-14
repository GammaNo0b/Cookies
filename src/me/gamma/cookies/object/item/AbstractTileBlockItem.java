
package me.gamma.cookies.object.item;


import me.gamma.cookies.object.block.AbstractCustomTileBlock;



public abstract class AbstractTileBlockItem<B extends AbstractCustomTileBlock<B, ?>> extends AbstractBlockItem<B> {

	public AbstractTileBlockItem(B block) {
		super(block);
	}

}
