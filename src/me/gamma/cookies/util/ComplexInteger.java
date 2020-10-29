package me.gamma.cookies.util;


public class ComplexInteger {
	
	
	private int value;
	
	
	
	public ComplexInteger() {
		this.value = 0;
	}
	
	public ComplexInteger(int value) {
		this.value = value;
	}
	
	
	public void addInteger(int i) {
		value += i;
	}
	
	public void setInteger(int i) {
		value = i;
	}
	
	public int getValue() {
		return value;
	}

}
