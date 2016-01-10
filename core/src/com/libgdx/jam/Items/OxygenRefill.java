package com.libgdx.jam.Items;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.Bodies.Hero;
import com.libgdx.jam.Utils.GameConstants;

public class OxygenRefill extends Item{
	
	private static Hero hero;
	
	public OxygenRefill(World world,  OrthographicCamera camera, MapObject mapObject, Hero hero){
		this.hero = hero;
		
		stringTextureRegion = "OxygenRefill";
		
		create(world, camera, mapObject);		
	}
	
	@Override
	public void activate(){
		used = true;
		
		System.out.println("Oxygen level before refill : " + hero.getOxygenLevel());
		hero.setOxygenLevel(hero.getFuelLevel() + GameConstants.OXYGEN_REFILL);
		
		if(hero.getOxygenLevel() > GameConstants.MAX_OXYGEN)
			hero.setOxygenLevel(GameConstants.MAX_OXYGEN);
		System.out.println("Oxygen level after refill : " + hero.getOxygenLevel());
	}
}
