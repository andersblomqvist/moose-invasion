package com.andblomqdasberg.mooseinvasion.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 	Loads config.properties file for client setup
 * 	
 * 	@author Anders Blomqvist
 */
public class ConfigHandler 
{
	// Holds config values. Read/write to this
	public static ConfigData configData = new ConfigData();
	
	/**
	 * 	Load config.properties file and return its values in a string
	 * 
	 * 	@return a string with its values seperated by a ','. Example output:
	 * 	"1,3,100"
	 * 
	 * 	@throws IOException
	 */
	public static String getPropertiesValues() throws IOException {
		InputStream inputStream;
		
		// Set to default in case no config file or directory was found
		String result = "0,3,100";
		
		try {
			Properties prop = new Properties();
			String filename = "config.properties";
			
			inputStream = new FileInputStream(new File("assets\\config\\" + filename));
			prop.load(inputStream);
			
			// Get values
			String fullscreen = prop.getProperty("fullscreen");
			String scale = prop.getProperty("scale");
			String volume = prop.getProperty("volume");
			
			// Format return string
			result = fullscreen + "," + scale + "," + volume;
			
			// Init config data object
			configData.setValue("fullscreen", fullscreen);
			configData.setValue("scale", scale);
			configData.setValue("volume", volume);
			System.out.println("Loaded properties file with values:");
			
			inputStream.close();
			
		} catch(Exception e) {
			// No directory or file was found, create file with default values.
			configData = null;
			System.out.println("Config file was not found. Creating a default config.properties file");
			setPropertiesValues();
		}
		return result;
	}
	
	/**
	 * 	Writes the data in configData to config.properties file
	 * 
	 * 	@throws IOException 
	 */
	public static void setPropertiesValues() throws IOException {
		if(configData == null) {
			new File("assets\\config").mkdirs();
			
			File config = new File("assets\\config\\config.properties");
			config.createNewFile();
			FileWriter writer = new FileWriter("assets\\config\\config.properties");
			writer.write("# Moose Invasion config\n");
			writer.write("fullscreen=0\n");
			writer.write("scale=3\n");
			writer.write("volume=100\n");
			writer.close();
			System.out.println("Successfully created a default config.properties file");
			configData = new ConfigData();
			configData.setDefaultValues();
			return;
		}
		
		// Write to existing config file with new data
		FileWriter writer = new FileWriter("assets\\config\\config.properties");
		writer.write("# Moose Invasion config\n");
		writer.write("fullscreen=" + configData.getValue("fullscreen") + "\n");
		writer.write("scale=" + configData.getValue("scale") + "\n");
		writer.write("volume=" + configData.getValue("volume") + "\n");
		writer.close();
		System.out.println("Successfully saved config.properties");
	}
}
