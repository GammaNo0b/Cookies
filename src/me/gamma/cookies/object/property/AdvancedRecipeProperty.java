
package me.gamma.cookies.object.property;


import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.object.recipe.machine.AdvancedMachineRecipe;



public class AdvancedRecipeProperty extends Property<PersistentDataContainer, AdvancedMachineRecipe> {

	public AdvancedRecipeProperty(String name) {
		super(name);
	}


	@Override
	public PersistentDataType<PersistentDataContainer, AdvancedMachineRecipe> getPersistentDataType() {
		return new PersistentDataType<>() {

			@Override
			public Class<PersistentDataContainer> getPrimitiveType() {
				return PersistentDataContainer.class;
			}


			@Override
			public Class<AdvancedMachineRecipe> getComplexType() {
				return AdvancedMachineRecipe.class;
			}


			@Override
			public PersistentDataContainer toPrimitive(AdvancedMachineRecipe complex, PersistentDataAdapterContext context) {
				PersistentDataContainer container = context.newPersistentDataContainer();
				
				return container;
			}


			@Override
			public AdvancedMachineRecipe fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}


	@Override
	public AdvancedMachineRecipe emptyValue(PersistentDataContainer container) {
		return null;
	}

}
