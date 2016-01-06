package com.libgdx.jam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libgdx.jam.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Life in Space";
	    config.width = 960;
	    config.height = 640;
	    //config.width = 1920;
	    //config.height = 1080;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
