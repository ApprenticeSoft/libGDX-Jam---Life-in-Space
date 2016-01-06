package com.libgdx.jam.Bodies;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.Utils.GameConstants;

public class Leak extends Obstacle{

	private Set<Fixture> fixtures;
	private Vector2 leakForce, leakOrigin;
	private float force, leakSize;
	
	public Leak(World world, OrthographicCamera camera,	MapObject rectangleObject) {
		super(world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		body.getFixtureList().get(0).setSensor(true);
		body.getFixtureList().get(0).setUserData("Leak");
		body.setUserData("Leak");
		
		fixtures = new HashSet<Fixture>();
		
		//Leak force
		if(rectangleObject.getProperties().get("Force") != null){
			force = Float.parseFloat(rectangleObject.getProperties().get("Force").toString()) * GameConstants.DEFAULT_LEAK_FORCE;
		}
		else
			force = GameConstants.DEFAULT_LEAK_FORCE;
		
		//Leak direction and leak origine
		if(rectangle.width > rectangle.height){
			leakForce = new Vector2(force, 0);
			leakSize = rectangle.width * GameConstants.MPP;
			
			if(force > 0)
				leakOrigin = new Vector2(posX - width, posY);
			else
				leakOrigin = new Vector2(posX + width, posY);
		}
		else{
			leakForce = new Vector2(0, force);
			leakSize = rectangle.height * GameConstants.MPP;
			
			if(force > 0)
				leakOrigin = new Vector2(posX, posY - height);
			else
				leakOrigin = new Vector2(posX, posY + height);
		}
	}
	
	public void addBody(Fixture fixture) {
		PolygonShape polygon = (PolygonShape) fixture.getShape();
		if (polygon.getVertexCount() > 2) 
			fixtures.add(fixture);
	}

	public void removeBody(Fixture fixture) {
		fixtures.remove(fixture);
	}
	
	public void active(){
		for(Fixture fixture : fixtures){
			float distanceX = Math.abs(fixture.getBody().getPosition().x - leakOrigin.x);
			float distanceY = Math.abs(fixture.getBody().getPosition().y - leakOrigin.y);
			
			fixture.getBody().applyForceToCenter(	
													leakForce.x * Math.abs(leakSize - distanceX)/leakSize, 
													leakForce.y * Math.abs(leakSize - distanceY)/leakSize,
													true
												);
		}
	}
}
