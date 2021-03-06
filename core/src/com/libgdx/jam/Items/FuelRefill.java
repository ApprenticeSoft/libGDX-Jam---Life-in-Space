package com.libgdx.jam.Items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Bodies.Hero;
import com.libgdx.jam.Utils.GameConstants;

public class FuelRefill extends Item{

	private static Hero hero;
	
	public FuelRefill(final MyGdxGame game, World world,  OrthographicCamera camera, MapObject mapObject, Hero hero){
		this.hero = hero;

		stringTextureRegion = "FuelRefill";
		sound = game.assets.get("Sounds/Fuel Refill.ogg", Sound.class);
		
		create(world, camera, mapObject);	
	}
	
	@Override
	public void activate(){
		used = true;
		sound.play();
		
		hero.setFuelLevel(hero.getFuelLevel() + GameConstants.FUEL_REFILL);
		
		if(hero.getFuelLevel() > GameConstants.MAX_FUEL)
			hero.setFuelLevel(GameConstants.MAX_FUEL);
	}
}
