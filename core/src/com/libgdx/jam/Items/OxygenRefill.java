package com.libgdx.jam.Items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Bodies.Hero;
import com.libgdx.jam.Utils.GameConstants;

public class OxygenRefill extends Item{
	
	private static Hero hero;
	
	public OxygenRefill(final MyGdxGame game, World world,  OrthographicCamera camera, MapObject mapObject, Hero hero){
		this.hero = hero;
		
		stringTextureRegion = "OxygenRefill";
		sound = game.assets.get("Sounds/Oxygen Refill.ogg", Sound.class);
		
		create(world, camera, mapObject);		
	}
	
	@Override
	public void activate(){
		used = true;
		sound.play();
		
		hero.setOxygenLevel(hero.getOxygenLevel() + GameConstants.OXYGEN_REFILL);
		
		if(hero.getOxygenLevel() > GameConstants.MAX_OXYGEN)
			hero.setOxygenLevel(GameConstants.MAX_OXYGEN);
		System.out.println("Oxygen level after refill : " + hero.getOxygenLevel());
	}
}
