package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleLight extends Obstacle{

	public ObstacleLight(World world, OrthographicCamera camera, MapObject rectangleObject) {
		super(world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		body.setUserData("ObstacleLight");
		body.getFixtureList().get(0).setUserData("ObstacleLight");
		
		//Weight
		if(rectangleObject.getProperties().get("Weight") != null){
			body.getFixtureList().get(0).setDensity(
					body.getFixtureList().get(0).getDensity() * Float.parseFloat(rectangleObject.getProperties().get("Weight").toString())
			);
			body.resetMassData();
		}
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.DynamicBody;
	}

}
