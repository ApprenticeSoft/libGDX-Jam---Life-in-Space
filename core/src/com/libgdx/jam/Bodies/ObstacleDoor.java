package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleDoor extends Obstacle{
	
	private float speed = 5;
	private Vector2 initialPosition, finalPosition;

	public ObstacleDoor(World world, OrthographicCamera camera,	MapObject rectangleObject) {
		super(world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		//Motion speed
		if(rectangleObject.getProperties().get("Speed") != null){
			speed = Float.parseFloat((String) rectangleObject.getProperties().get("Speed"));
		}
		
		initialPosition = new Vector2(posX, posY);
		
		if(width > height)
			finalPosition = new Vector2(posX + Math.signum(speed) * 1.9f*width, posY);
		else
			finalPosition = new Vector2(posX, posY + Math.signum(speed) * 1.9f*height);
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}
	
	@Override
	public void active(){
		if(active)
			body.setLinearVelocity(	Math.signum(speed) * (initialPosition.x - body.getPosition().x) * speed, 
									Math.signum(speed) * (initialPosition.y - body.getPosition().y) * speed
									);
		else
			body.setLinearVelocity(	Math.signum(speed) * (finalPosition.x - body.getPosition().x) * speed,
									Math.signum(speed) * (finalPosition.y - body.getPosition().y) * speed
									);
	}
	
	@Override
	public void activate(){
		active = !active;
	}
}
