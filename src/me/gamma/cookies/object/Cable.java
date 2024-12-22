
package me.gamma.cookies.object;


import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import me.gamma.cookies.util.collection.Pair;



public interface Cable {

	static <T, P extends Provider<T>> void transfer(TransferMode transfermode, Provider<T> buffer, Filter<T> filter, int transferrate, List<P> supplier, List<P> consumer, List<P> storage) {
		transfermode.transfer(buffer, filter, transferrate, supplier, consumer, storage);
	}


	static <P extends TypelessProvider> void transfer(TransferMode transfermode, TypelessProvider buffer, int transferrate, List<P> supplier, List<P> consumer, List<P> storage) {
		transfermode.transfer(buffer, transferrate, supplier, consumer, storage);
	}


	static <T, P extends Provider<T>> Pair<T, Integer> collect(TransferMode transfermode, T type, int available, Filter<T> filter, List<P> supplier) {
		return transfermode.collect(type, available, filter, supplier);
	}


	static int collect(TransferMode transfermode, int available, List<? extends TypelessProvider> supplier) {
		return transfermode.collect(available, supplier);
	}


	static <T, P extends Provider<T>> int distribute(TransferMode transfermode, T type, int amount, List<P> consumer) {
		return transfermode.distribute(type, amount, consumer);
	}


	static int distribute(TransferMode transfermode, int amount, List<? extends TypelessProvider> consumer) {
		return transfermode.distribute(amount, consumer);
	}

	public static enum TransferMode {

		ORDERED(new OrderedTransporter()),
		RANDOM(new RandomTransporter(new OrderedTransporter())),
		ROUND_ROBIN(new RoundRobinTransporter());

		private final Transporter transporter;

		private TransferMode(Transporter transporter) {
			this.transporter = transporter;
		}


		/**
		 * Transfers resources from the suppliers to the consumers.
		 * 
		 * @param <T>          the type of the resource
		 * @param <P>          the type of the provider of the resource
		 * @param buffer       the cable buffer to store resources between operations
		 * @param filter       the filter of the cable
		 * @param transferrate the amount of resources this cable can transport in one tick
		 * @param supplier     list of {@link Provider} that supply the resource
		 * @param consumer     list of {@link Provider} that consume the resource
		 * @param storage      list of {@link Provider} that supply and consume the rest resource
		 */
		<T, P extends Provider<T>> void transfer(Provider<T> buffer, Filter<T> filter, int transferrate, List<P> supplier, List<P> consumer, List<P> storage) {
			T type = buffer.isEmpty() ? null : buffer.getType();
			int amount;
			Pair<T, Integer> p;

			// distribute resources from buffer to consumers
			amount = buffer.get(transferrate);
			amount = this.distribute(type, amount, consumer);
			amount = this.distribute(type, amount, storage);
			amount = buffer.set(amount);
			buffer.setType(type);

			assert amount == 0: "All buffered resources should have been distributed!";

			// collect resources from suppliers to buffer
			p = this.collect(type, Math.min(transferrate, buffer.space()) - amount, filter, supplier);
			type = p.left;
			amount += p.right;
			p = this.collect(type, Math.min(transferrate, buffer.space()) - amount, filter, storage);
			type = p.left;
			amount += p.right;
			amount = buffer.set(amount);
			buffer.setType(type);

			assert amount == 0: "The redistributed amount should always be zero!";
		}


		/**
		 * Transfers resources from the suppliers to the consumers.
		 * 
		 * @param buffer       the cable buffer to store resources between operations
		 * @param transferrate the amount of resources this cable can transport in one tick
		 * @param supplier     list of {@link TypelessProvider} that supply the resource
		 * @param consumer     list of {@link TypelessProvider} that consume the resource
		 * @param storage      list of {@link TypelessProvider} that supply and consume the rest resource
		 */
		<P extends TypelessProvider> void transfer(TypelessProvider buffer, int transferrate, List<P> supplier, List<P> consumer, List<P> storage) {
			int amount;

			// distribute resources from buffer to consumers
			amount = buffer.get(transferrate);
			amount = this.distribute(amount, consumer);
			amount = this.distribute(amount, storage);
			amount = buffer.set(amount);

			assert amount == 0: "All buffered resources should have been distributed!";

			// collect resources from suppliers to buffer
			amount += this.collect(Math.min(transferrate, buffer.space()) - amount, supplier);
			amount += this.collect(Math.min(transferrate, buffer.space()) - amount, storage);
			amount = buffer.set(amount);

			assert amount == 0: "All collected resources should have been buffered!";
		}


		<T, P extends Provider<T>> Pair<T, Integer> collect(T type, int available, Filter<T> filter, List<P> supplier) {
			return available <= 0 ? new Pair<>(null, 0) : this.transporter.collect(type, available, filter, supplier);
		}


		<P extends TypelessProvider> int collect(int available, List<P> supplier) {
			return available <= 0 ? 0 : this.transporter.collect(available, supplier);
		}


		<T, P extends Provider<T>> int distribute(T type, int amount, List<P> consumer) {
			return amount <= 0 ? 0 : this.transporter.distribute(type, amount, consumer);
		}


		<P extends TypelessProvider> int distribute(int amount, List<P> consumer) {
			return amount <= 0 ? 0 : this.transporter.distribute(amount, consumer);
		}

	}

	/**
	 * Defines the way that a cable collects and distributed resources from multiple providers.
	 */
	static interface Transporter {

		/**
		 * Collects the resource from the given providers that match the given filter and returns the collected amount.
		 * 
		 * @param <T>       type of the resource
		 * @param <P>       provider type
		 * @param type      the type of the resource
		 * @param available maximum amount to be collected
		 * @param filter    the filter
		 * @param supplier  list of supplier
		 * @return pair containing the current type of the resource and the collected amount
		 */
		<T, P extends Provider<T>> Pair<T, Integer> collect(T type, int available, Filter<T> filter, List<P> supplier);

		/**
		 * Collects the resource from the given providers.
		 * 
		 * @param available maximum amount to be collected
		 * @param supplier  list of supplier
		 * @return amount of the collected resource
		 */
		<P extends TypelessProvider> int collect(int available, List<P> supplier);

		/**
		 * Distributes the amount of the resources to the given consumer.
		 * 
		 * @param <T>      type of the resource
		 * @param <P>      provider of the resource
		 * @param type     the type of the resource
		 * @param amount   maximum amount to be distributed
		 * @param consumer list of consumer
		 * @return the rest amount that could not be distributed
		 */
		<T, P extends Provider<T>> int distribute(T type, int amount, List<P> consumer);

		/**
		 * Distributes the amount of the resource to the given consumer.
		 * 
		 * @param amount   maximum amount to be distributed
		 * @param consumer list of consumer
		 * @return the rest amount that could not be distributed
		 */
		<P extends TypelessProvider> int distribute(int amount, List<P> consumer);

	}

	static class OrderedTransporter implements Transporter {

		@Override
		public <T, P extends Provider<T>> Pair<T, Integer> collect(T type, int available, Filter<T> filter, List<P> supplier) {
			supplier.removeIf(Provider::isEmpty);

			int max = available;

			for(int index = 0; available > 0 && index < supplier.size(); index++) {
				P provider = supplier.get(index);
				if(provider.isEmpty())
					continue;

				if(type == null) {
					type = provider.getType();
				} else if(!provider.match(type)) {
					continue;
				}

				available -= provider.get(available, filter);

				if(type == null)
					available -= supplier.get(index).get(available);
			}

			return new Pair<>(type, max - available);
		}


		@Override
		public <P extends TypelessProvider> int collect(int available, List<P> supplier) {
			supplier.removeIf(Provider::isEmpty);

			int max = available;
			int index = 0;

			while(available > 0 && index < supplier.size())
				available -= supplier.get(index++).get(available);

			return max - available;
		}


		@Override
		public <T, P extends Provider<T>> int distribute(T type, int amount, List<P> consumer) {
			consumer.removeIf(Provider::isFull);

			int index = 0;

			while(amount > 0 && index < consumer.size()) {
				P provider = consumer.get(index++);
				if(provider.match(type)) {
					amount = provider.set(amount);
				} else if(provider.isEmpty() && provider.canChangeType(type)) {
					provider.setType(type);
					amount = provider.set(amount);
				}
			}

			return amount;
		}


		@Override
		public <P extends TypelessProvider> int distribute(int amount, List<P> consumer) {
			consumer.removeIf(Provider::isFull);

			int index = 0;

			while(amount > 0 && index < consumer.size())
				amount = consumer.get(index++).set(amount);

			return amount;
		}

	}

	static class RandomTransporter implements Transporter {

		private final Transporter transporter;

		public RandomTransporter(Transporter transporter) {
			this.transporter = transporter;
		}


		@Override
		public <T, P extends Provider<T>> Pair<T, Integer> collect(T type, int available, Filter<T> filter, List<P> supplier) {
			supplier.removeIf(Provider::isEmpty);
			Collections.shuffle(supplier);
			return this.transporter.collect(type, available, filter, supplier);
		}


		@Override
		public <P extends TypelessProvider> int collect(int available, List<P> supplier) {
			supplier.removeIf(Provider::isEmpty);
			Collections.shuffle(supplier);
			return this.transporter.collect(available, supplier);
		}


		@Override
		public <T, P extends Provider<T>> int distribute(T type, int amount, List<P> consumer) {
			consumer.removeIf(Provider::isFull);
			Collections.shuffle(consumer);
			return this.transporter.distribute(type, amount, consumer);
		}


		@Override
		public <P extends TypelessProvider> int distribute(int amount, List<P> consumer) {
			consumer.removeIf(Provider::isFull);
			Collections.shuffle(consumer);
			return this.transporter.distribute(amount, consumer);
		}

	}

	static class RoundRobinTransporter implements Transporter {

		@Override
		public <T, P extends Provider<T>> Pair<T, Integer> collect(T type, int available, Filter<T> filter, List<P> supplier) {
			supplier.removeIf(Provider::isEmpty);

			int max = available;
			while(!supplier.isEmpty()) {
				int transmit = available / supplier.size();
				if(transmit <= 0)
					break;

				ListIterator<P> iterator = supplier.listIterator();
				while(iterator.hasNext()) {
					P provider = iterator.next();

					if(type == null) {
						type = provider.getType();
					} else if(!provider.match(type)) {
						continue;
					}

					available -= provider.get(transmit, filter);

					if(provider.isEmpty())
						iterator.remove();
				}
			}

			return new Pair<>(type, max - available);
		}


		@Override
		public <P extends TypelessProvider> int collect(int available, List<P> supplier) {
			supplier.removeIf(Provider::isEmpty);

			int max = available;
			while(!supplier.isEmpty()) {
				int transmit = available / supplier.size();
				if(transmit <= 0)
					break;

				ListIterator<P> iterator = supplier.listIterator();
				while(iterator.hasNext()) {
					TypelessProvider provider = iterator.next();

					available -= provider.get(transmit);

					if(provider.isEmpty())
						iterator.remove();
				}
			}

			return max - available;
		}


		@Override
		public <T, P extends Provider<T>> int distribute(T type, int amount, List<P> consumer) {
			consumer.removeIf(Provider::isFull);

			while(!consumer.isEmpty()) {
				int transmit = amount / consumer.size();
				if(transmit <= 0)
					break;

				ListIterator<P> iterator = consumer.listIterator();
				while(iterator.hasNext()) {
					P provider = iterator.next();
					if(!provider.match(type))
						if(!provider.isEmpty() || !provider.canChangeType(type))
							continue;

					provider.setType(type);
					amount += provider.set(transmit) - transmit;

					if(provider.isFull())
						iterator.remove();
				}
			}

			return amount;
		}


		@Override
		public <P extends TypelessProvider> int distribute(int amount, List<P> consumer) {
			consumer.removeIf(Provider::isFull);

			while(!consumer.isEmpty()) {
				int transmit = amount / consumer.size();
				if(transmit <= 0)
					break;

				ListIterator<P> iterator = consumer.listIterator();
				while(iterator.hasNext()) {
					TypelessProvider provider = iterator.next();

					amount += provider.set(transmit) - transmit;

					if(provider.isFull())
						iterator.remove();
				}
			}

			return amount;
		}

	}

}
