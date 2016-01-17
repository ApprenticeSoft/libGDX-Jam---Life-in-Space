package com.libgdx.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Data {
	
	public static Preferences prefs;
	
	public static void Load(){
		prefs = Gdx.app.getPreferences("Cosmonaut.Data");
		
		if (!prefs.contains("Level")) {
		    prefs.putInteger("Level", 1);
		}
	}
	
	public static void setLevel(int val) {
	    prefs.putInteger("Level", val);
	    prefs.flush();							//Mandatory to save the data
	}

	public static int getLevel() {
	    return prefs.getInteger("Level");
	}
}
