package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.libgdx.jam.MyGdxGame;

public class ObstacleDoor extends Obstacle{
	
	private float speed = 5;
	private float doorAngle, doorScale;
	private Vector2 initialPosition, finalPosition;

	public ObstacleDoor(final MyGdxGame game, World world, OrthographicCamera camera,	MapObject rectangleObject) {
		super(game, world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		stringTextureRegion = "Door";
		
		doorScale = 1;
		
		//Motion speed
		if(rectangleObject.getProperties().get("Speed") != null){
			speed = Float.parseFloat((String) rectangleObject.getProperties().get("Speed"));
		}
		
		initialPosition = new Vector2(posX, posY);
		
		if(width > height){
			finalPosition = new Vector2(posX + Math.signum(speed) * 1.9f*width, posY);
			doorAngle = 0;
		}
		else{
			finalPosition = new Vector2(posX, posY + Math.signum(speed) * 1.9f*height);
			doorAngle = 90;
			doorScale = height/width;
		}
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}
	
	@Override
	public void active(Hero hero){
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
	
	@Override
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){		
		batch.setColor(1, 1, 1, 1);
		batch.draw(textureAtlas.findRegion(stringTextureRegion), 
				this.body.getPosition().x - width, 
				this.body.getPosition().y - height,
				width,
				height,
				2 * width,
				2 * height,
				doorScale,
				1/doorScale,
				doorAngle);
	}
}
