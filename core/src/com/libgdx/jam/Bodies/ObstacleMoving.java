package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.libgdx.jam.Utils.GameConstants;

public class ObstacleMoving extends Obstacle{

	private float speed;
	private boolean backward, loop;
	private Vector2 direction;
	private Vector2[] path;
	private int step;
	private float widthFactor = 1;
	private float heightFactor = 1;
	
	public ObstacleMoving(World world, OrthographicCamera camera, PolylineMapObject polylineObject, TextureAtlas textureAtlas) {
		super(world, camera, polylineObject);
		
		ninePatch = new NinePatch(textureAtlas.findRegion("MovingObstacle"), 49, 49, 49, 49);
		//ninePatch.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
		
		//SPEED
		if(polylineObject.getProperties().get("Speed") != null)
			speed = Float.parseFloat((String) polylineObject.getProperties().get("Speed"));
		else speed = 5;
		
		//DOES THE PATH MAKE A LOOP ?
		if(polylineObject.getProperties().get("Loop") != null)
			loop = true;
		else loop = false;
		
		//WIDTH OF THE MOVING OBJECT		
		if(polylineObject.getProperties().get("Width") != null)
			widthFactor = Float.parseFloat((String) polylineObject.getProperties().get("Width"));
		
		width = GameConstants.PPT * GameConstants.MPP/2;
		width *= widthFactor;
		
		//HEIGHT OF THE MOVING OBJECT
		if(polylineObject.getProperties().get("Height") != null)
			heightFactor = Float.parseFloat((String) polylineObject.getProperties().get("Height"));
		
		height = GameConstants.PPT * GameConstants.MPP/2;
		height *= heightFactor;
		
		//Scaling of the NinePatch
		if(width > height && height >= 1){
			ninePatch.scale(heightFactor * 0.5f*GameConstants.MPP, heightFactor * 0.5f*GameConstants.MPP);
		}
		else if(height >= width && width >= 1)
			ninePatch.scale(widthFactor * 0.5f*GameConstants.MPP, widthFactor * 0.5f*GameConstants.MPP);
		else
			ninePatch.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
		
		path = new Vector2[polylineObject.getPolyline().getTransformedVertices().length/2];
    	for(int i = 0; i < path.length; i++){
    		path[i] = new Vector2(polylineObject.getPolyline().getTransformedVertices()[i*2]*GameConstants.MPP, polylineObject.getPolyline().getTransformedVertices()[i*2 + 1]*GameConstants.MPP);
    	}   
    	
    	polygonShape = new PolygonShape();
    	polygonShape.setAsBox(width, height);

    	bodyDef = new BodyDef();
    	bodyDef.type = getBodyType();
    	bodyDef.position.set(path[0]);
    	
    	fixtureDef = new FixtureDef();
		fixtureDef.shape = polygonShape;
        fixtureDef.density = 0.0f;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 0f;

    	body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData("Objet");
        body.setUserData("Objet");
        
        polygonShape.dispose();

        direction = new Vector2(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y);
        body.setLinearVelocity(direction.clamp(speed, speed));
	}
	
	@Override
	public BodyType getBodyType(){
		return BodyType.KinematicBody;
	}

	@Override
	

	public void active(){
		if(active){
			if(!loop){
				if(!backward){
					if(!new Vector2(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y).hasSameDirection(direction)){
						step++;
						
				        if(step == path.length){
				        	backward = true;
				        	step = path.length - 2;
				        }
				        
						direction.set(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y);
					}
				}
				else{
					if(!new Vector2(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y).hasSameDirection(direction)){
						step--;
						
				        if(step < 0){
				        	backward = false;
				        	step = 1;
				        }
				        
						direction.set(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y);
					}
				}	
			}
			else{
				if(!new Vector2(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y).hasSameDirection(direction)){
					step++;
					
			        if(step == path.length){
			        	step = 0;
			        }
			        
					direction.set(path[step].x - body.getPosition().x, path[step].y - body.getPosition().y);
				}
			}
	        body.setLinearVelocity(direction.clamp(speed, speed)); 
		}
		else 
	        body.setLinearVelocity(0, 0); 		
	}
	
	@Override
	public void activate(){
		active = !active;
	}
}
