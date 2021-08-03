
package me.gamma.cookies.objects.property;


public class StringListProperty extends ListProperty<String> {

	private String regex;


	public StringListProperty(String name, String regex) {
		super(name);
		this.regex = regex;
	}


	@Override
	public String getDividingRegex() {
		return regex;
	}


	@Override
	public String fromString(String string) {
		return string;
	}


	@Override
	public String toString(String value) {
		return value;
	}

}
