
package me.gamma.cookies.object.block.network.energy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.bukkit.block.TileState;

import me.gamma.cookies.object.Cable;
import me.gamma.cookies.object.energy.EnergyConsumer;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergyStorage;
import me.gamma.cookies.object.energy.EnergySupplier;
import me.gamma.cookies.util.collection.Triple;



/**
 * Transfers energy between {@link EnergySupplier}, {@link EnergyConsumer} and {@link EnergyStorage} blocks.
 * 
 * @author gamma
 *
 */
public interface EnergyCable {

	/**
	 * Returns the transfer mode of the given block to transfer energy with this cable.
	 * 
	 * @param block the block
	 * @return the transfer mode
	 */
	Cable.TransferMode getTransferMode(TileState block);

	/**
	 * Returns the amount of energy that can be transferred per tick.
	 * 
	 * @param block the block state of the cable
	 * @return the transferrate
	 */
	int getTransferRate(TileState block);

	/**
	 * Returns the energy buffer for this cable to store energy temporarily.
	 * 
	 * @param block the block state of the cable
	 * @return the energy buffer of the given block
	 */
	EnergyProvider getBuffer(TileState block);

	/**
	 * Returns an stream that contains all neighbors of this block.
	 * 
	 * @param block the block of which the neighbors should be returned
	 * @return the iterator
	 */
	Stream<TileState> getNeighbors(TileState block);


	/**
	 * Returns a triple containing maps with the adjacent {@link EnergySupplier}, {@link EnergyConsumer} and {@link EnergyStorage} of this block with
	 * their corresponding {@link TileState}. The neighbors of this block will be given by {@link EnergyCable#getNeighbors(TileState)}.
	 * 
	 * @param block the energy cable block
	 * @return the triple
	 */
	default Triple<Map<TileState, EnergySupplier>, Map<TileState, EnergyConsumer>, Map<TileState, EnergyStorage>> getNerbyComponents(TileState block) {
		Map<TileState, EnergySupplier> suppliers = new HashMap<>();
		Map<TileState, EnergyConsumer> consumers = new HashMap<>();
		Map<TileState, EnergyStorage> storages = new HashMap<>();

		Consumer<TileState> inspectBlock = state -> {
			EnergySupplier supplier = EnergySupplier.getEnergySupplier(state);
			EnergyConsumer consumer = EnergyConsumer.getEnergyConsumer(state);

			if(supplier != null) {
				if(supplier instanceof EnergyStorage storage) {
					storages.put(state, storage);
				} else {
					suppliers.put(state, supplier);
				}
			}

			if(consumer != null)
				if(!(consumer instanceof EnergyStorage))
					consumers.put(state, consumer);
		};

		inspectBlock.accept(block);
		this.getNeighbors(block).forEach(inspectBlock);

		return new Triple<>(suppliers, consumers, storages);
	}


	/**
	 * Transmits energy between the adjacent energy components.
	 * 
	 * @param block the energy cable block
	 */
	default void transmitEnergy(TileState block) {
		Triple<Map<TileState, EnergySupplier>, Map<TileState, EnergyConsumer>, Map<TileState, EnergyStorage>> triple = this.getNerbyComponents(block);
		this.transmitEnergy(block, triple.left, triple.middle, triple.right);
	}


	/**
	 * Transmits energy between the suppliers to the consumers.
	 * 
	 * @param block    the energy cable block
	 * @param supplier Map of {@link TileState} and {@link EnergySupplier} that supply the energy
	 * @param consumer Map of {@link TileState} and {@link EnergyConsumer} that consume the energy
	 * @param storage  Map of {@link TileState} and {@link EnergyStorage} that supply and consume the rest energy
	 */
	default void transmitEnergy(TileState block, Map<TileState, EnergySupplier> supplier, Map<TileState, EnergyConsumer> consumer, Map<TileState, EnergyStorage> storage) {
		ArrayList<EnergyProvider> supplierlist = new ArrayList<>();
		supplier.forEach((state, sup) -> supplierlist.add(sup.getEnergyOutput(state)));
		ArrayList<EnergyProvider> consumerlist = new ArrayList<>();
		consumer.forEach((state, con) -> consumerlist.add(con.getEnergyInput(state)));
		ArrayList<EnergyProvider> storagelist = new ArrayList<>();
		storage.forEach((state, sto) -> storagelist.add(sto.getEnergyProvider(state)));
		Cable.transfer(this.getTransferMode(block), this.getBuffer(block), this.getTransferRate(block), supplierlist, consumerlist, storagelist);
	}


	/**
	 * Collects energy from the adjacent energy suppliers and storages.
	 * 
	 * @param block the energy cable block
	 */
	default void collectEnergy(TileState block) {
		Triple<Map<TileState, EnergySupplier>, Map<TileState, EnergyConsumer>, Map<TileState, EnergyStorage>> triple = this.getNerbyComponents(block);
		this.collectEnergy(block, triple.left, triple.right);
	}


	/**
	 * Collects energy from energy suppliers and storages.
	 * 
	 * @param block    the energy cable block
	 * @param supplier Map of {@link TileState} and {@link EnergySupplier} that supply the energy
	 * @param storage  Map of {@link TileState} and {@link EnergyStorage} that supply the rest energy
	 */
	default void collectEnergy(TileState block, Map<TileState, EnergySupplier> supplier, Map<TileState, EnergyStorage> storage) {
		ArrayList<EnergyProvider> supplierlist = new ArrayList<>();
		supplier.forEach((state, sup) -> supplierlist.add(sup.getEnergyOutput(state)));
		storage.forEach((state, sto) -> supplierlist.add(sto.getEnergyProvider(state)));
		EnergyProvider buffer = this.getBuffer(block);
		buffer.add(Cable.collect(this.getTransferMode(block), Math.min(buffer.space(), this.getTransferRate(block)), supplierlist));
	}


	/**
	 * Distributes energy to the adjacent energy consumers and storages.
	 * 
	 * @param block the energy cable block
	 */
	default void distributeEnergy(TileState block) {
		Triple<Map<TileState, EnergySupplier>, Map<TileState, EnergyConsumer>, Map<TileState, EnergyStorage>> triple = this.getNerbyComponents(block);
		this.distributeEnergy(block, triple.middle, triple.right);
	}


	/**
	 * Distributes energy to energy consumers and storages.
	 * 
	 * @param block    the energy cable block
	 * @param supplier Map of {@link TileState} and {@link EnergyConsumer} that consume the energy
	 * @param storage  Map of {@link TileState} and {@link EnergyStorage} that consume the rest energy
	 */
	default void distributeEnergy(TileState block, Map<TileState, EnergyConsumer> supplier, Map<TileState, EnergyStorage> storage) {
		ArrayList<EnergyProvider> consumerlist = new ArrayList<>();
		supplier.forEach((state, sup) -> consumerlist.add(sup.getEnergyInput(state)));
		storage.forEach((state, sto) -> consumerlist.add(sto.getEnergyProvider(state)));
		EnergyProvider buffer = this.getBuffer(block);
		buffer.add(Cable.distribute(this.getTransferMode(block), buffer.get(this.getTransferRate(block)), consumerlist));
	}

}
