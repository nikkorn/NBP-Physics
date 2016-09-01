package com.dumbpug.main.gamedevicestesting.networking;

/**
 * Server event broadcaster.
 * @author nikolas.howard
 */
public class Broadcaster {
	private Broadcaster broadcasterInstance = null;
	
	/**
	 * Constructor for Broadcaster class.
	 * Private to force singleton pattern.
	 */
	private Broadcaster() {}
	
	/**
	 * Get our Broadcaster singleton.
	 * @return broadcaster.
	 */
	public Broadcaster getBroadcaster() {
		if(broadcasterInstance == null) {
			broadcasterInstance = new Broadcaster();
		}
		return broadcasterInstance;
	}

}
