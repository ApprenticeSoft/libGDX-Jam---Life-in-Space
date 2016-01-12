package com.libgdx.jam.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.GameConstants;

public class ObstaclePiston extends Obstacle{

	private PolygonShape shapeAxis;
	private float widthAxis, heightAxis, posXAxis, posYAxis;
	private float speed = 10;
	private float delay = 0;
	private Vector2 initialPosition, finalPosition, direction, posAxisSprite;
	private Vector2[] travel;
	private int step = 1;
	private Rectangle rectangleAxis;
	private Sound sound;
	private long soundId;
	
	private NinePatch ninePatchAxis;
	
	public ObstaclePiston(final MyGdxGame game, World world, OrthographicCamera camera, MapObject rectangleObject1, TextureAtlas textureAtlas, MapObject rectangleObject2) {
		super(game, world, camera, rectangleObject1, textureAtlas);

		sound = game.assets.get("Sounds/Piston.mp3", Sound.class);
		sound.stop();
		
		if(rectangleObject1.getProperties().get("Part").equals("Head")){
			create(world, camera, rectangleObject1);
			rectangleAxis = ((RectangleMapObject) rectangleObject2).getRectangle();
		}
		else{
			create(world, camera, rectangleObject2);
			rectangleAxis = ((RectangleMapObject) rectangleObject1).getRectangle();
		}
			
		//Delay before activation
		if(rectangleObject1.getProperties().get("Delay") != null){
			delay = Float.parseFloat((String) rectangleObject1.getProperties().get("Delay"));
		}
		else if(rectangleObject2.getProperties().get("Delay") != null){
			delay = Float.parseFloat((String) rectangleObject2.getProperties().get("Delay"));
		}
		
		//Motion speed
		if(rectangleObject1.getProperties().get("Speed") != null){
			speed = Float.parseFloat((String) rectangleObject1.getProperties().get("Speed"));
		}
		else if(rectangleObject2.getProperties().get("Speed") != null){
			speed = Float.parseFloat((String) rectangleObject2.getProperties().get("Speed"));
		}
		
		//Creation of the second Fixture		
		widthAxis = (rectangleAxis.width/2) * GameConstants.MPP;
		heightAxis = (rectangleAxis.height/2) * GameConstants.MPP;
		posXAxis = (rectangleAxis.x + rectangleAxis.width/2) * GameConstants.MPP;
		posYAxis = (rectangleAxis.y + rectangleAxis.height/2) * GameConstants.MPP;
		
		shapeAxis = new PolygonShape();
		shapeAxis.setAsBox(widthAxis, heightAxis, new Vector2(posXAxis - posX, posYAxis - posY), 0);
        
		bodyDef.position.set(new Vector2((rectangleAxis.x + rectangleAxis.width/2) * GameConstants.MPP, (rectangleAxis.y + rectangleAxis.height/2) * GameConstants.MPP));
		
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shapeAxis;
        fixtureDef.density = 0;  
        fixtureDef.friction = 0.5f;  
        fixtureDef.restitution = 0.5f;
        
		body.createFixture(fixtureDef);  
		body.setUserData("ObstaclePiston");		
		shapeAxis.dispose();

		body.getFixtureList().get(0).setUserData("ObstaclePiston");
		body.getFixtureList().get(1).setUserData("Obstacle");
		
		initialPosition = new Vector2(posX, posY);
		posAxisSprite = new Vector2(0,0);
		if(posX == posXAxis){
			//Drawing
			ninePatch = new NinePatch(textureAtlas.findRegion("PistonHead"), 24, 24, 24, 24);
			ninePatch.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
			ninePatchAxis = new NinePatch(textureAtlas.findRegion("PistonAxis"), 24, 24, 24, 24);
			ninePatchAxis.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
			
			posAxisSprite.x = - widthAxis;
			finalPosition = new Vector2(initialPosition.x, initialPosition.y + rectangleAxis.height * Math.signum(posYAxis - posY) * GameConstants.MPP);
			
			if(posY < posYAxis)
				posAxisSprite.y = height;
			else
				posAxisSprite.y = - height - 2 * heightAxis;
		}
		else{
			//Drawing
			ninePatch = new NinePatch(textureAtlas.findRegion("PistonHeadHorizontal"), 24, 24, 24, 24);
			ninePatch.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
			ninePatchAxis = new NinePatch(textureAtlas.findRegion("PistonAxisHorizontal"), 24, 24, 24, 24);
			ninePatchAxis.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
			
			posAxisSprite.y = - heightAxis;
			finalPosition = new Vector2(initialPosition.x + rectangleAxis.width * Math.signum(posXAxis - posX) * GameConstants.MPP, initialPosition.y);
			
			if(posX < posXAxis)
				posAxisSprite.x = width;
			else
				posAxisSprite.x = - width - 2 * widthAxis;
		}

		travel = new Vector2[2];
		travel[0] = initialPosition;
		travel[1] = finalPosition;

        direction = new Vector2();
        direction = new Vector2(travel[step].x - body.getPosition().x, travel[step].y - body.getPosition().y);
        soundId = sound.play();
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}

	@Override
	public void active(Hero hero){
		if(active){
			if(delay > 0){
				delay -= Gdx.graphics.getDeltaTime();
			}
			else{			
				if(!new Vector2(travel[step].x - body.getPosition().x, travel[step].y - body.getPosition().y).hasSameDirection(direction)){	
					sound.stop(soundId);
					soundId = sound.play(1, MathUtils.random(0.98f, 1.02f), 0);
					if(step > 0)
						step = 0;
					else step = 1;
					
			        direction = new Vector2(travel[step].x - body.getPosition().x, travel[step].y - body.getPosition().y);
				}
				body.setLinearVelocity(direction.clamp(speed, speed)); 
			}
			
			sound.setVolume(soundId, 10/(new Vector2(hero.heroBody.getPosition().sub(posX, posY)).len()));
			sound.resume();
		}
		else{
			body.setLinearVelocity(0, 0); 	
			sound.pause();
		}
			
	}
	
	@Override
	public void activate(){
		active = !active;
	}
	
	@Override	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas){		
	}
	
	@Override	
	public void draw(SpriteBatch batch){
		batch.setColor(1, 1, 1, 1);
		ninePatch.draw(batch, 
						this.body.getPosition().x - width,
						this.body.getPosition().y - height, 
						2 * width, 
						2 * height);
	
		ninePatchAxis.draw(batch, 
				this.body.getPosition().x + posAxisSprite.x,
				this.body.getPosition().y + posAxisSprite.y, 
				2 * widthAxis, 
				2 * heightAxis);
	}
	
}
