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
	public static int NB_HORIZONTAL_TILE = 13;
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
	
	//Graphismes
	public static float ombresX = (float)Gdx.graphics.getWidth()/800;
	public static float ombresY =  - (float)Gdx.graphics.getHeight()/400;
	
	//Gestion des niveaux
	public static int nombreNiveaux = 25;
	public static int niveauSelectione = 1;
	public static float objectif = 70;
	public static int couleurSelectionee = 1;
	
	public static boolean début = true;
	public static boolean pause = true;
	public static boolean perdu = true;
	public static boolean gagné = true;
	public static int INTERSTITIAL_TRIGGER = 2;
	
	//Gestion des barres
	public static boolean spawn = false;
	public static float largeurBordure; 
	public static float posBordure = 0.05f;
	
	//Liens vers les App Store
	public static final String GOOGLE_PLAY_GAME_URL = "https://play.google.com/store/apps/details?id=com.minimal.jezz.android";
	public static final String GOOGLE_PLAY_STORE_URL = "https://play.google.com/store/apps/developer?id=Apprentice+Soft";
	public static final String AMAZON_GAME_URL = "http://www.amazon.com/gp/mas/dl/android?p=com.minimal.jezz.android";
	public static final String AMAZON_STORE_URL = "http://www.amazon.com/gp/mas/dl/android?p=com.premier.jeu.android&showAll=1";
}
