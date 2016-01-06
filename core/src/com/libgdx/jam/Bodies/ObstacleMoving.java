package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
	
	public ObstacleMoving(World world, OrthographicCamera camera, PolylineMapObject polylineObject) {
		super(world, camera, polylineObject);
		
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
			width = Integer.parseInt((String) polylineObject.getProperties().get("Width")) * GameConstants.PPT * GameConstants.MPP/2;
		else
			width = 2 * GameConstants.PPT * GameConstants.MPP/2;
		
		//HEIGHT OF THE MOVING OBJECT
		if(polylineObject.getProperties().get("Height") != null)
			height = Integer.parseInt((String) polylineObject.getProperties().get("Height")) * GameConstants.PPT * GameConstants.MPP/2;
		else
			height = 2 * GameConstants.PPT * GameConstants.MPP/2;
		
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
