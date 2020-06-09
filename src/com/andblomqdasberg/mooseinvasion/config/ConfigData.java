package com.andblomqdasberg.mooseinvasion.config;

import java.util.HashMap;

/**
 * 	Handle the data stored in the config.properties file with a HashMap
 * 
 * 	Each value can be access or overwritten by its key. The {@link ConfigHandler}
 * 	makes sure the data is stored to disk.
 * 
 * 	@author Anders Blomqvist
 */
public class ConfigData
{
	private HashMap<String, String> data = new HashMap<String, String>();
	
	public String getValue(String key) {
		return data.get(key);
	}
	
	public void setValue(String key, String value) {
		data.put(key, value);
	}
	
	/**
	 * 	Set default values in HashMap
	 */
	public void setDefaultValues() {
		data.put("fullscreen", "0");
		data.put("scale", "3");
		data.put("volume", "100");
	}
}
