package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ObstacleLight extends Obstacle{
	
	String stringTexture;

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
		
		//Texture
		if(rectangleObject.getProperties().get("Texture") != null){
			stringTexture = rectangleObject.getProperties().get("Texture").toString();
		}
		else
			stringTexture = "CrateSquare";
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.DynamicBody;
	}
	
	@Override	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){		
		batch.setColor(1, 1, 1, 1);
		batch.draw(textureAtlas.findRegion(stringTexture), 
				this.body.getPosition().x - width, 
				this.body.getPosition().y - height,
				width,
				height,
				2 * width,
				2 * height,
				1,
				1,
				body.getAngle()*MathUtils.radiansToDegrees);
	}

}
