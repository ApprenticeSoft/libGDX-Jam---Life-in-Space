package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleRevolving extends Obstacle{
	
	private float speed = 90;

	public ObstacleRevolving(World world, OrthographicCamera camera, MapObject rectangleObject) {
		super(world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		//Rotation speed
		if(rectangleObject.getProperties().get("Speed") != null)
			speed = Float.parseFloat((String) rectangleObject.getProperties().get("Speed"));
		
		body.setFixedRotation(false);
		body.setAngularVelocity(speed*MathUtils.degreesToRadians);
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}
	
	@Override
	public void activate(){
		active = !active;
		
		if(active)
			body.setAngularVelocity(speed*MathUtils.degreesToRadians);
		else
			body.setAngularVelocity(0);
	}
}
