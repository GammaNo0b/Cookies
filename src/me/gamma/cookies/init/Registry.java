
package me.gamma.cookies.init;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;



public class Registry<T> implements Iterable<T> {

	private final ArrayList<T> registry = new ArrayList<>();

	public <R extends T> R register(R item) {
		if(!this.registry.contains(item))
			this.registry.add(item);
		return item;
	}


	public <R extends T> List<R> register(Stream<R> items) {
		return items.map(this::register).toList();
	}


	public int size() {
		return this.registry.size();
	}


	public void clear() {
		this.registry.clear();
	}


	@Override
	public Iterator<T> iterator() {
		return this.registry.iterator();
	}


	public Stream<T> stream() {
		return this.registry.stream();
	}


	public List<T> asList() {
		return new ArrayList<>(this.registry);
	}


	public List<T> filter(Predicate<T> predicate) {
		return this.registry.stream().filter(predicate).toList();
	}


	public T filterFirst(Predicate<T> predicate) {
		for(T item : this.registry)
			if(predicate.test(item))
				return item;
		return null;
	}


	public <R> List<R> filterByClass(Class<R> clazz) {
		return this.registry.stream().filter(e -> clazz.isAssignableFrom(e.getClass())).map(clazz::cast).toList();
	}


	public <R> R filterFirstByClass(Class<R> clazz) {
		for(T item : this.registry)
			if(clazz.isAssignableFrom(item.getClass()))
				return clazz.cast(item);
		return null;
	}

}
