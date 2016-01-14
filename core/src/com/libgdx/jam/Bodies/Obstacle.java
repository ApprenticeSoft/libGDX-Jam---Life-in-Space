package com.libgdx.jam.Bodies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.GameConstants;

public class Obstacle {
	
	public Body body;
	protected BodyDef bodyDef;
	protected FixtureDef fixtureDef;
	protected PolygonShape polygonShape;
	protected Rectangle rectangle;
	public float posX, posY, width, height, angle;
	public int associationNumber;
	public boolean active;
	
	//Graphs
	protected NinePatch ninePatch;
	protected String stringTextureRegion;
	
	//Sound
	protected Sound sound;
	
	public Obstacle(final MyGdxGame game, World world, OrthographicCamera camera, MapObject rectangleObject){	
	}
	
	public Obstacle(final MyGdxGame game, World world, OrthographicCamera camera, MapObject rectangleObject, TextureAtlas textureAtlas){		
	}
	
	public Obstacle(final MyGdxGame game, World world, OrthographicCamera camera, PolylineMapObject polylineObject){
		setInitialState(polylineObject);
	}
	
	public void create(World world, OrthographicCamera camera, MapObject rectangleObject){
		setInitialState(rectangleObject);
		
		stringTextureRegion = "WhiteSquare";
		
		rectangle = ((RectangleMapObject) rectangleObject).getRectangle();
			
		this.posX = (rectangle.x + rectangle.width/2) * GameConstants.MPP;
		this.posY = (rectangle.y + rectangle.height/2) * GameConstants.MPP;
		this.width = (rectangle.width/2) * GameConstants.MPP;
		this.height = (rectangle.height/2) * GameConstants.MPP;
		
		if(rectangleObject.getProperties().get("rotation") != null)
			this.angle = -Float.parseFloat(rectangleObject.getProperties().get("rotation").toString())*MathUtils.degreesToRadians;
		
		polygonShape = new PolygonShape();
		polygonShape.setAsBox(width, height);

		bodyDef = new BodyDef();
		bodyDef.position.set(new Vector2(posX, posY));
    	bodyDef.type = getBodyType();
		body = world.createBody(bodyDef);
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
        fixtureDef.density = (float)(GameConstants.DENSITY/(width * height));  
        fixtureDef.friction = 0.5f;  
        fixtureDef.restitution = 0.5f;
   
        body.createFixture(fixtureDef).setUserData("Obstacle");
        body.setUserData("Obstacle");
        body.setFixedRotation(false);
        
        if(rectangleObject.getProperties().get("rotation") != null){
            /*
             * To obtain x' et y' positions from x et y positions after a rotation of an angle A
             * around the origine (0, 0) :
             * x' = x*cos(A) - y*sin(A)
             * y' = x*sin(A) + y*cos(A)
             */
        	float X = (float)(body.getPosition().x - width + width * Math.cos(angle) + height * Math.sin(angle));
        	float Y = (float)(width * Math.sin(angle) + body.getPosition().y + height - height * Math.cos(angle));
        	body.setTransform(X, Y, this.angle);
        }
        
        polygonShape.dispose();  
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public float getX(){
		return posX;
	}
	
	public float getY(){
		return posY;
	}
	
	public BodyType getBodyType(){
		return BodyType.StaticBody;
	}

	public void setX( float X){
		posX = X;
	}

	public void setY( float Y){
		posY = Y;
	}
	
	public void setInitialState(MapObject mapObject){
		//Association Number
		if(mapObject.getProperties().get("Association Number") != null){
			associationNumber = Integer.parseInt((String) mapObject.getProperties().get("Association Number"));
		}
		
		//Is the Obstacle active ?
		if(mapObject.getProperties().get("Active") != null){
			if(Integer.parseInt((String) mapObject.getProperties().get("Active")) == 1)
				active = true;
			else 
				active = false;
		}
		else
			active = true;		
	}

	public void active(Hero hero){
		
	}
	
	public void activate(){
		
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
	
	public void draw(SpriteBatch batch){
		batch.setColor(1, 1, 1, 1);
		ninePatch.draw(batch, 
						this.body.getPosition().x - width,
						this.body.getPosition().y - height, 
						2 * width, 
						2 * height);
	}
	
	public void soundPause(){
		if(sound != null)
			sound.pause();
	}
	
	public void soundResume(){
		if(sound != null)
			sound.resume();
	}
	
	public void dispose(){	
	}
}
