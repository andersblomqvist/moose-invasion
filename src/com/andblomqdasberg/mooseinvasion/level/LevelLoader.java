package com.andblomqdasberg.mooseinvasion.level;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 	Class for loading level.json for the level setup
 * 
 * 	@author Anders Blomqvist
 */
public class LevelLoader {

	private JSONObject json;
	
	public LevelLoader(String level) throws IOException {
		
		System.out.println("-- Loading level --");
		
		InputStream is = LevelLoader.class.getResourceAsStream("/data/" + level + ".json");
		
		if(is == null) {
			System.err.println("Failed to load file from classpath: /data/" + level + ".json");
			return;
		}
		else
			System.out.println("Successfully loaded " + level + ".json");
		
		String file = getStringFromStream(is);
		json = new JSONObject(file);
	}
	
	/**
	 * 	Extracts the data in the .json file and creates a LevelData
	 * 	object from it which we can use.
	 * 	
	 * 	@returns a LevelData object from the .json file
	 */
	public LevelData getLevelData() {
		LevelData data = new LevelData();
		
		// Loop though all the "stageX" keys in json
		for(int i = 1; i <= json.keySet().size(); i++) {
			
			// Get stages and create a stage object
			JSONObject stage = json.getJSONObject("stage" + i);
			LevelStage ls = new LevelStage(stage.getString("background"));
			
			// Go though all the "waveX" keys
			for(int j = 1; j <= stage.keySet().size(); j++) {
				
				// Check that it's actually a wave key
				if(stage.has("wave" + j)) {
					
					// Get the entries from the wave 
					JSONObject wave = stage.getJSONObject("wave" + j);
					JSONArray entries = wave.getJSONArray("entries");
					
					// Add an empty wave to the LevelStage
					ls.addEmptyWave("wave" + j, j);
					
					// Populate the empty wave with the entities
					for(int k = 0; k < entries.length(); k++) {
						JSONObject e = entries.getJSONObject(k);
						
						String entity = e.getString("entity");
						int count = e.getInt("count");
						
						ls.addEntityToWave(entity, count, "wave" + j);
						// System.out.println("Adding " + count + " of " + entity);
					}
					
					// Get duration for wave
					int duration = wave.getInt("duration");
					ls.setWaveDuration("wave" + j, duration);
					
					// Wave completed.
					System.out.println("Wave " + j + " completed.");
				}
			}
			
			// Stage completed.
			data.addStage(ls);
			System.out.println("Stage " + i + " completed.");
		}
		
		System.out.println(" -- Level data setup finished. --");
		
		return data;
	}
	
	
	/**
	 * 	Returns the file which is loaded into the stream in a string
	 * 
	 * 	@param is InputStream with file loaded
	 * 	@return A string of the file
	 * 	@throws IOException
	 */
	private String getStringFromStream(InputStream is) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) != -1)
		    result.write(buffer, 0, length);
		
		return result.toString("UTF-8");
	}
}