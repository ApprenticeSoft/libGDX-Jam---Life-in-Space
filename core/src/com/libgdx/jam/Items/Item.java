package com.libgdx.jam.Items;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.libgdx.jam.Utils.GameConstants;
import com.libgdx.jam.Utils.TiledMapReader;

public class Item {
	
	protected static World world;
	public Body body;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private PolygonShape polygonShape;
	private float width, height;
	public boolean used;
	protected String stringTextureRegion;
	
	public Item(){	
	}
	
	public void create(World world,  OrthographicCamera camera, MapObject mapObject){
		this.world = world;
		used = false;
		
		height = mapObject.getProperties().get("height", float.class)/2 * GameConstants.MPP/3;
		width = 19 * height / 50;
		
		bodyDef = new BodyDef();
		fixtureDef = new FixtureDef();
		
		bodyDef.type = BodyType.DynamicBody;

		bodyDef.position.set((mapObject.getProperties().get("x", float.class) + mapObject.getProperties().get("width", float.class)/2) * GameConstants.MPP,
							(mapObject.getProperties().get("y", float.class) + 1.5f*mapObject.getProperties().get("height", float.class)) * GameConstants.MPP);
		
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(width, height);
		
		fixtureDef.shape = polygonShape;
		fixtureDef.density = 0.1f;  
        fixtureDef.friction = 0.2f;  
        fixtureDef.restitution = 0f;
		
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData("Item");
        body.setUserData("Item"); 
        
        polygonShape.dispose();
        
        //Impulse
		if(mapObject.getProperties().get("Impulse") != null){
			body.applyLinearImpulse(new Vector2(MathUtils.random(-5, 5) * body.getFixtureList().get(0).getDensity(), 
												MathUtils.random(-5, 5) * body.getFixtureList().get(0).getDensity()), 
									new Vector2(body.getPosition().x + MathUtils.random(-0.9f * width, 0.9f * width), 
												body.getPosition().y + MathUtils.random(-0.9f * height, 0.9f * height)), 
									true);
		}
	}
	
	public void activate(){
		//Called when Major Tom collide with the item
	}
	
	public void active(TiledMapReader tiledMapReader){
		if(used){
    		body.setActive(false);
    		world.destroyBody(body);
    		tiledMapReader.items.removeIndex(tiledMapReader.items.indexOf(this, true));
    	}
	}
	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){		
		batch.setColor(1, 1, 1, 1);
		batch.draw(textureAtlas.findRegion(stringTextureRegion), 
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
