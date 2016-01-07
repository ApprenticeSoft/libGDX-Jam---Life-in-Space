package com.libgdx.jam.Utils;

import com.badlogic.gdx.Gdx;

public class GameConstants {
	//World constants
	public static float MPP = 0.05f;			//Meter/Pixel
	public static float PPM = 1/MPP;			//Pixel/Meter
	public static float BOX_STEP = 1/60f; 
	public static int BOX_VELOCITY_ITERATIONS = 6;
	public static int BOX_POSITION_ITERATIONS = 2;
	public static float GRAVITY = 0;
	public static float DENSITY = 1.0f;
	
	//Tiled Map constants
	public static int PPT = 100;					//Pixel/Tile
	
	//Screen constants
	public static int NB_HORIZONTAL_TILE = 12;
	public static float SCREEN_WIDTH = MPP * NB_HORIZONTAL_TILE * PPT;
	public static float SCREEN_RATIO = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
	public static float SCREEN_HEIGHT = SCREEN_WIDTH * SCREEN_RATIO;
	
	//Hero constants
	public static float HERO_HEIGHT = 0.55f *  PPT * MPP / 2;
	public static float HERO_WIDTH = 35 * HERO_HEIGHT / 100;
	public static float JETPACK_IMPULSE = 100;
	public static float TOM_ROTATION = 5;
	public static float MAX_OXYGEN = 120;
	public static float MAX_FUEL = 80;
	public static float FUEL_CONSUMPTION = 8;
	public static float CRUSH_IMPULSE = 300;
	
	//Obstacles and Items constants
	public static float DEFAULT_LEAK_FORCE = 20f;
	public static float FUEL_REFILL = 40f;
	public static float OXYGEN_REFILL = 30f;
	
	//Game variables (Yeah, I know, this is the GameConstants.java)
	public static boolean GAME_PAUSED = false;
	public static float ANIM_TIME = 0;
	public static float LEVEL_PIXEL_WIDTH, LEVEL_PIXEL_HEIGHT;
}
