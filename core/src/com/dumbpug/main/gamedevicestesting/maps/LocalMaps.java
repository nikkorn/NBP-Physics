package com.dumbpug.main.gamedevicestesting.maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import com.dumbpug.main.gamedevicestesting.C;

/**
 * 
 * @author nikolas.howard
 *
 */
public class LocalMaps {
	
	/**
	 * Get a list of local maps parsed from JSON map files on disk.
	 * @return list of local maps.
	 */
	public static ArrayList<Map> getLocalMaps() {
		ArrayList<Map> localMaps = new ArrayList<Map>();
		// Get the maps directory.
		File localMapsDirectory = new File(C.RES_MAPS_DIR);
		// Check that our maps directory even exists and that it truly is a directory.
		if(localMapsDirectory.exists() && localMapsDirectory.isDirectory()){
			// Each directory in the maps directory is an individual map.
			for(File mapDir : localMapsDirectory.listFiles()) {
				// Check that this file is actually a directory and that it contains the necessary resources.
				if(mapDir.isDirectory() && mapDirectoryContainsRequiredResources(mapDir)){
					// This is a valid map! Create our map object.
					try {
						JSONObject mapJsonObject = getJSONObjectFromFile(new File(mapDir, "map.json"));
						Map localMap = new Map(mapJsonObject, mapDir.getName());
						// Add the new map to our list of local maps.
						localMaps.add(localMap);
					} catch (FileNotFoundException | JSONException e) {
						// We somehow failed to create our map, try the next one.
						System.out.println("Error: failed to create map: " + mapDir.getName());
						continue;
					}
				}
			}
		} else {
			// We have no maps directory, so we cannot continue.
			System.out.println("Error: maps directory does not exist!");
			System.exit(0);
		}
		return localMaps;
	}
	
	/**
	 * Checks whether a map directory has the required resources (map JSON, tiles PNG and backdrop PNG).
	 * @param mapDirectory.
	 * @return map directory has the required resources.
	 */
	private static boolean mapDirectoryContainsRequiredResources(File mapDirectory) {
		if(!new File(mapDirectory, "map.json").exists())
			return false;
		if(!new File(mapDirectory, "tiles.png").exists())
			return false;
		if(!new File(mapDirectory, "backdrop.png").exists())
			return false;
		return true;
	}
	
	/**
	 * Get the contents of a file as a JSON Object.
	 * @param file
	 * @return JSON object.
	 * @throws FileNotFoundException 
	 * @throws JSONException 
	 */
	private static JSONObject getJSONObjectFromFile(File file) throws FileNotFoundException, JSONException {
		Scanner fileScanner   = new Scanner(file);
		JSONObject jsonObject = null;
		String rawJson        = "";
		// Read all lines of this file into our raw JSON string.
		while(fileScanner.hasNextLine()){
			rawJson += fileScanner.nextLine();
		}
		// Close our scanner.
		fileScanner.close();
		// Create and return our JSON object.
		jsonObject = new JSONObject(rawJson);
		return jsonObject;
	}
}
