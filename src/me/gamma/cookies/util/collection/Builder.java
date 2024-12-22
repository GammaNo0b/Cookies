
package me.gamma.cookies.util.collection;


import java.util.Collection;
import java.util.function.Consumer;



public class Builder<T, C extends Collection<T>> {

	private final C collection;
	private Consumer<C> onBuild;

	public Builder(C collection) {
		this.collection = collection;
	}


	public Builder(C collection, Consumer<C> onBuild) {
		this.collection = collection;
		this.onBuild = onBuild;
	}


	public C build() {
		if(this.onBuild != null)
			this.onBuild.accept(this.collection);
		return this.collection;
	}


	public Builder<T, C> add(T element) {
		this.collection.add(element);
		return this;
	}


	@SuppressWarnings("unchecked")
	public Builder<T, C> add(T... elements) {
		for(T element : elements)
			this.collection.add(element);
		return this;
	}


	public Builder<T, C> add(Builder<T, ?> builder) {
		this.collection.addAll(builder.collection);
		return this;
	}

}
