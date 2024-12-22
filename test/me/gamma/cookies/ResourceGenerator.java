package me.gamma.cookies;

import com.google.gson.JsonObject;

public interface ResourceGenerator<R> {
	
	JsonObject generate(R resource);

}
