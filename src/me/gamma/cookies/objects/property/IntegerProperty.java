
package me.gamma.cookies.objects.property;


import org.bukkit.persistence.PersistentDataHolder;



public class IntegerProperty extends PrimitiveProperty<Integer> {

	public IntegerProperty(String name) {
		super(name);
	}


	@Override
	public Class<Integer> getPrimitiveClass() {
		return Integer.class;
	}
	
	
	@Override
	public Integer emptyValue() {
		return 0;
	}


	public void increase(PersistentDataHolder holder, int amount) {
		this.store(holder, amount + this.fetch(holder));
	}

}
