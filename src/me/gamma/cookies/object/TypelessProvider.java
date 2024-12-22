
package me.gamma.cookies.object;


/**
 * Provider that provides resources without any type or with an irrelevant type.
 * 
 * @author gamma
 *
 */
public interface TypelessProvider extends Provider<Void> {

	@Override
	default Void getType() {
		return null;
	}


	@Override
	default void setType(Void type) {}


	@Override
	default boolean canChangeType(Void type) {
		return true;
	}


	@Override
	default boolean match(Void type) {
		return true;
	}

}
