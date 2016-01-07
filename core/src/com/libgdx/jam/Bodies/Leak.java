package com.libgdx.jam.Bodies;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.GameConstants;

public class Leak extends Obstacle{

	private Set<Fixture> fixtures;
	private Vector2 leakForce, leakOrigin;
	private float force, leakSize, leakAngle, leakScale, leakSpeed;
	private Animation leakAnimation;
	
	public Leak(World world, OrthographicCamera camera,	MapObject rectangleObject, final MyGdxGame game) {
		super(world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		body.getFixtureList().get(0).setSensor(true);
		body.getFixtureList().get(0).setUserData("Leak");
		body.setUserData("Leak");
		
		leakScale = 1;
		
		fixtures = new HashSet<Fixture>();
		
		//Leak force
		if(rectangleObject.getProperties().get("Force") != null){
			force = Float.parseFloat(rectangleObject.getProperties().get("Force").toString()) * GameConstants.DEFAULT_LEAK_FORCE;
		}
		else
			force = GameConstants.DEFAULT_LEAK_FORCE;
		
		//Leak animation
		leakSpeed = 0.3f * GameConstants.DEFAULT_LEAK_FORCE/Math.abs(force);
		leakSpeed = MathUtils.clamp(leakSpeed, 0.01f, 0.1f);
		leakAnimation = new Animation(leakSpeed, game.assets.get("Images/Leak_Animation.pack", TextureAtlas.class).findRegions("Leak_Animation"), Animation.PlayMode.LOOP);
		
		//Leak direction and leak origine
		if(rectangle.width > rectangle.height){
			leakForce = new Vector2(force, 0);
			leakSize = rectangle.width * GameConstants.MPP;
			
			if(force > 0){
				leakOrigin = new Vector2(posX - width, posY);
				leakAngle = 0;
			}
			else{
				leakOrigin = new Vector2(posX + width, posY);
				leakAngle = 180;
			}
		}
		else{
			leakForce = new Vector2(0, force);
			leakSize = rectangle.height * GameConstants.MPP;
			leakScale = height/width;
			
			if(force > 0){
				leakOrigin = new Vector2(posX, posY - height);
				leakAngle = 90;
			}
			else{
				leakOrigin = new Vector2(posX, posY + height);
				leakAngle = 270;
			}
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
	
	public void draw(SpriteBatch batch, float animTime){		
		batch.setColor(1, 1, 1, 1);
		batch.draw(leakAnimation.getKeyFrame(animTime), 
				this.body.getPosition().x - width, 
				this.body.getPosition().y - height,
				width,
				height,
				2 * width,
				2 * height,
				leakScale,
				1/leakScale,
				leakAngle);
	}
}
