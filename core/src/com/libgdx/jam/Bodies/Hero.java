package com.libgdx.jam.Bodies;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.GameConstants;

public class Hero {

	public Body heroBody;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private PolygonShape heroShape;
	private float bodyWidth, bodyHeight, spriteWidth, spriteHeight, posXInit, posYInit, oxygenLevel, fuelLevel, angleHero;
	private TextureAtlas tomAtlas;
    private Animation tomIdle, tomFly;
    private boolean fly;
    private int direction;
	
	//Test Box2DLight
	private PointLight light;
	
	public Hero(final MyGdxGame game, World world,  OrthographicCamera camera, TiledMap tiledMap){
		create(game, world, camera, tiledMap);
	}
	
	public Hero(final MyGdxGame game, World world,  OrthographicCamera camera, TiledMap tiledMap, RayHandler rayHandler){
		create(game, world, camera, tiledMap);
        
        light = new PointLight(rayHandler, 100, new Color(.5f,.5f,.9f,.9f), 40, 0, 0);
        light.attachToBody(heroBody, 0, 1.01f*bodyHeight);
	}
	
	public void create(final MyGdxGame game, World world,  OrthographicCamera camera, TiledMap tiledMap){
		
		oxygenLevel = GameConstants.MAX_OXYGEN;
		fuelLevel = GameConstants.MAX_FUEL;
		fly = false;
		
		MapObjects personnages = (MapObjects)tiledMap.getLayers().get("Spawn").getObjects();

		bodyWidth = GameConstants.HERO_WIDTH;
		bodyHeight = GameConstants.HERO_HEIGHT;
		posXInit = (personnages.get("Tom").getProperties().get("x", float.class) + personnages.get("Tom").getProperties().get("width", float.class)/2) * GameConstants.MPP;
		posYInit = (personnages.get("Tom").getProperties().get("y", float.class) + personnages.get("Tom").getProperties().get("height", float.class)/2) * GameConstants.MPP;
		
		heroShape = new PolygonShape();
		heroShape.setAsBox(bodyWidth, bodyHeight);
		
		bodyDef = new BodyDef();
		bodyDef.position.set(posXInit, posYInit);
        bodyDef.type = BodyType.DynamicBody; 
        
        heroBody = world.createBody(bodyDef);
        heroBody.setFixedRotation(false);
        	
        fixtureDef = new FixtureDef();  
        fixtureDef.shape = heroShape; 
        fixtureDef.density = (float)(GameConstants.DENSITY/(bodyWidth * bodyHeight));  
        fixtureDef.friction = 0.01f;  
        fixtureDef.restitution = 0.1f;  
        heroBody.createFixture(fixtureDef).setUserData("Tom"); 
        heroBody.setUserData("Tom");
        
        heroShape.dispose();
        
        //Animation
        tomAtlas = game.assets.get("Images/Tom_Animation.pack", TextureAtlas.class);
        tomIdle = new Animation(0.1f, tomAtlas.findRegions("Tom_Idle"), Animation.PlayMode.LOOP);
        tomFly = new Animation(0.1f, tomAtlas.findRegions("Tom_Fly"), Animation.PlayMode.NORMAL);
        
        spriteHeight = 2 * bodyHeight;
        spriteWidth = spriteHeight * tomIdle.getKeyFrame(0, true).getRegionWidth()  / tomIdle.getKeyFrame(0, true).getRegionHeight();
	}
	
	public void displacement(){	
		oxygenLevel -= Gdx.graphics.getDeltaTime();
		
		if(Gdx.input.isKeyPressed(Keys.W) && fuelLevel > 0){
			heroBody.applyForceToCenter(new Vector2(0, GameConstants.JETPACK_IMPULSE).rotate(heroBody.getAngle() * MathUtils.radiansToDegrees), true);
			fuelLevel -= Gdx.graphics.getDeltaTime() * GameConstants.FUEL_CONSUMPTION;
        }
        
		if(Gdx.input.isKeyPressed(Keys.A))
			heroBody.setAngularVelocity(GameConstants.TOM_ROTATION);
		else if(Gdx.input.isKeyPressed(Keys.D))
			heroBody.setAngularVelocity(- GameConstants.TOM_ROTATION);
		else
			heroBody.setAngularVelocity(0);
		
		if(oxygenLevel < 0)
			oxygenLevel = 0;
		if(fuelLevel < 0)
			fuelLevel = 0;
		
		//TEST
		angleHero = heroBody.getAngle() * MathUtils.radiansToDegrees;
		while(angleHero < 0)
			angleHero += 360;
		while(angleHero > 360)
			angleHero -= 360;
		
		if(angleHero >= 45 && angleHero < 135){
			if(heroBody.getLinearVelocity().y >= 0)
				direction = 1;
			else
				direction = -1;
		}
		else if(angleHero >= 135 && angleHero < 225){
			if(heroBody.getLinearVelocity().x <= 0)
				direction = 1;
			else
				direction = -1;
		}
		else if(angleHero >= 225 && angleHero < 315){
			if(heroBody.getLinearVelocity().y <= 0)
				direction = 1;
			else
				direction = -1;
		}
		else{
			if(heroBody.getLinearVelocity().x >= 0)
				direction = 1;
			else
				direction = -1;
		}
	}
	
	public float getX(){
		return heroBody.getPosition().x;
	}
	
	public float getOxygenLevel(){
		return oxygenLevel;
	}
	
	public void setOxygenLevel(float newOxygenLevel){
		oxygenLevel = newOxygenLevel;
	}
	
	public float getFuelLevel(){
		return fuelLevel;
	}
	
	public void setFuelLevel(float newFuelLevel){
		fuelLevel = newFuelLevel;
	}
	
	public float getY(){
		return heroBody.getPosition().y;
	}
	
	public Vector2 getOrigine(){
		return new Vector2(posXInit, posYInit);
	}
	
	public void draw(SpriteBatch batch, float animTime){
		if(Gdx.input.isKeyPressed(Keys.W) && fuelLevel > 0){
			if(!fly){
				GameConstants.ANIM_TIME = 0;
				fly = true;
			}
			/*
	    	batch.draw(tomFly.getKeyFrame(animTime), 
	    			heroBody.getPosition().x - Math.signum(direction) * bodyWidth, 
	    			heroBody.getPosition().y + bodyHeight - spriteHeight, 
	    			Math.signum(direction) * bodyWidth,
	    			spriteHeight - bodyHeight,
	    			Math.signum(direction) * spriteWidth, 
					spriteHeight,
					1,
					1,
					heroBody.getAngle()*MathUtils.radiansToDegrees);
	    	*/
	    	batch.draw(tomFly.getKeyFrame(animTime), 
	    			heroBody.getPosition().x - bodyWidth, 
	    			heroBody.getPosition().y + bodyHeight - spriteHeight, 
	    			bodyWidth,
	    			spriteHeight - bodyHeight,
	    			spriteWidth, 
					spriteHeight,
					1,
					1,
					heroBody.getAngle()*MathUtils.radiansToDegrees);
		}
		else{
			if(fly){
				GameConstants.ANIM_TIME = 0;
				fly = false;		
			}
			/*
	    	batch.draw(tomIdle.getKeyFrame(animTime, true), 
	    			heroBody.getPosition().x - Math.signum(direction) * bodyWidth, 
	    			heroBody.getPosition().y + bodyHeight - spriteHeight, 
	    			Math.signum(direction) * bodyWidth,
	    			spriteHeight - bodyHeight,
	    			Math.signum(direction) * spriteWidth, 
					spriteHeight,
					1,
					1,
					heroBody.getAngle()*MathUtils.radiansToDegrees);
			*/
	    	batch.draw(tomIdle.getKeyFrame(animTime, true), 
	    			heroBody.getPosition().x - bodyWidth, 
	    			heroBody.getPosition().y + bodyHeight - spriteHeight, 
	    			bodyWidth,
	    			spriteHeight - bodyHeight,
	    			spriteWidth, 
					spriteHeight,
					1,
					1,
					heroBody.getAngle()*MathUtils.radiansToDegrees);
		}
	}
}
