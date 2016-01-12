package com.libgdx.jam.Utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Bodies.Exit;
import com.libgdx.jam.Bodies.Hero;
import com.libgdx.jam.Bodies.Leak;
import com.libgdx.jam.Bodies.Obstacle;
import com.libgdx.jam.Bodies.ObstacleDoor;
import com.libgdx.jam.Bodies.ObstacleLight;
import com.libgdx.jam.Bodies.ObstacleMoving;
import com.libgdx.jam.Bodies.ObstaclePiston;
import com.libgdx.jam.Bodies.ItemSwitch;
import com.libgdx.jam.Bodies.ObstacleRevolving;
import com.libgdx.jam.Bodies.Wall;
import com.libgdx.jam.Items.FuelRefill;
import com.libgdx.jam.Items.Item;
import com.libgdx.jam.Items.OxygenRefill;

public class TiledMapReader {
	
    private MapObjects objects;
	public Array<Obstacle> obstacles, obstaclesWithNinePatch;
	public Array<Leak> leaks;
	public Array<ItemSwitch> switchs;
	public Array<Item> items;
	private Array<MapObject> pistons;
	private Array<Exit> exits;
	public Hero hero;
	public Exit exit;
    
	public TiledMapReader(final MyGdxGame game, TiledMap tiledMap, World world, OrthographicCamera camera){
			
		hero = new Hero(game, world, camera, tiledMap);
		
		objects = tiledMap.getLayers().get("Objects").getObjects();

        obstacles = new Array<Obstacle>(); 
        obstaclesWithNinePatch = new Array<Obstacle>();    
        pistons = new Array<MapObject>();
        switchs = new Array<ItemSwitch>();
        items = new Array<Item>();
        leaks = new Array<Leak>();
        exits = new Array<Exit>();
 
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
        	if(rectangleObject.getProperties().get("Type") != null){
        		//End of the level
        		if(rectangleObject.getProperties().get("Type").equals("Exit")){
        			exit = new Exit(game, world, camera, rectangleObject);
                    exits.add(exit);
        		}
        		//Light obstacles
        		else if(rectangleObject.getProperties().get("Type").equals("Light")){
	            	ObstacleLight obstacle = new ObstacleLight(game, world, camera, rectangleObject);
	                obstacles.add(obstacle);
            	}
        		//Doors
        		else if(rectangleObject.getProperties().get("Type").equals("Door")){
	            	ObstacleDoor obstacle = new ObstacleDoor(game, world, camera, rectangleObject);
	                obstacles.add(obstacle);
            	}
            	//Pistons
            	else if(rectangleObject.getProperties().get("Type").equals("Piston")){
	            	pistons.add(rectangleObject);
            	}
            	//Revolving obstacles
            	else if(rectangleObject.getProperties().get("Type").equals("Revolving")){
	            	ObstacleRevolving obstacle = new ObstacleRevolving(game, world, camera, rectangleObject);
	                obstacles.add(obstacle);
            	}
            	//Leaks
            	else if(rectangleObject.getProperties().get("Type").equals("Leak")){
	            	Leak leak = new Leak(game, world, camera, rectangleObject);
	                leaks.add(leak);
            	}
        	}
        	else{
        		Wall obstacle = new Wall(game, world, camera, rectangleObject, game.assets.get("Images/Images.pack", TextureAtlas.class));
                obstacles.add(obstacle);
        	}
        }
        
        //Pistons creation
        for(int i = pistons.size - 1; i > -1; i--){
        	if(pistons.get(i).getProperties().get("Group") != null){
        		for(int j = 0; j < pistons.size; j++){
        			if(Integer.parseInt(pistons.get(i).getProperties().get("Group").toString()) == Integer.parseInt(pistons.get(j).getProperties().get("Group").toString()) &&
        					i != j){  				
        				ObstaclePiston piston = new ObstaclePiston(game, world, camera, pistons.get(i), game.assets.get("Images/Images.pack", TextureAtlas.class), pistons.get(j));
        				obstacles.add(piston);
        				
        				pistons.removeIndex(i);
        				pistons.removeIndex(j);
        				i--;
        			}
        		}
        	}	
        	else
    			System.out.println("Piston creation failed");
        }
        
        //Moving obstacles     
        for(PolylineMapObject polylineObject : objects.getByType(PolylineMapObject.class)){
        	ObstacleMoving obstacleMoving = new ObstacleMoving(game, world, camera, polylineObject, game.assets.get("Images/Images.pack", TextureAtlas.class));
        	obstacles.add(obstacleMoving); 	
        }
              
        //Spawned items
        for(int i = 0; i < tiledMap.getLayers().get("Spawn").getObjects().getCount(); i++){
        	if(tiledMap.getLayers().get("Spawn").getObjects().get(i).getProperties().get("Type") != null){	
        		//Switches
        		if(tiledMap.getLayers().get("Spawn").getObjects().get(i).getProperties().get("Type").equals("Switch")){
        			ItemSwitch itemSwitch = new ItemSwitch(world, camera, tiledMap.getLayers().get("Spawn").getObjects().get(i));
        			switchs.add(itemSwitch);
        		}
        		//Oxygen Refill
        		else if(tiledMap.getLayers().get("Spawn").getObjects().get(i).getProperties().get("Type").equals("Oxygen")){
        			OxygenRefill oxygenRefill = new OxygenRefill(world, camera, tiledMap.getLayers().get("Spawn").getObjects().get(i), hero);
        			items.add(oxygenRefill);
        		}
        		//Fuel Refill
        		else if(tiledMap.getLayers().get("Spawn").getObjects().get(i).getProperties().get("Type").equals("Fuel")){
        			FuelRefill fuelRefill = new FuelRefill(world, camera, tiledMap.getLayers().get("Spawn").getObjects().get(i), hero);
        			items.add(fuelRefill);
        		}
        	}
        }
        
        
        //Obstacle organization
	    for(int i = obstacles.size - 1; i > -1; i--){
	    	if(obstacles.get(i).getClass().toString().equals("class com.libgdx.jam.Bodies.ObstaclePiston")){
	    		obstaclesWithNinePatch.add(obstacles.get(i));
	    		obstacles.removeIndex(obstacles.indexOf(obstacles.get(i), true));
	    	}
	    }
	    for(int i = obstacles.size - 1; i > -1; i--){
	    	if(obstacles.get(i).getClass().toString().equals("class com.libgdx.jam.Bodies.ObstacleMoving")){
	    		obstaclesWithNinePatch.add(obstacles.get(i));
	    		obstacles.removeIndex(obstacles.indexOf(obstacles.get(i), true));
	    	}
	    }
	    for(int i = obstacles.size - 1; i > -1; i--){
	    	if(obstacles.get(i).getClass().toString().equals("class com.libgdx.jam.Bodies.Wall")){
	    		obstaclesWithNinePatch.add(obstacles.get(i));
	    		obstacles.removeIndex(obstacles.indexOf(obstacles.get(i), true));
	    	}
	    }
	}
	
	public void active(){
		hero.displacement();
        
        for(Leak leak : leaks)
        	leak.active();		
        for(Obstacle obstacle : obstacles)
        	obstacle.active(hero);
        for(Obstacle obstacle : obstaclesWithNinePatch)
        	obstacle.active(hero);       
        for(Item item : items)
        	item.active(this);        
        for(Exit exit : exits)
        	exit.active();
	}
	
	public void draw(SpriteBatch batch, TextureAtlas textureAtlas, float animTime){    
        for(Exit exit : exits)
        	exit.draw(batch);
		for(ItemSwitch itemSwitch : switchs)
			itemSwitch.draw(batch, textureAtlas);
        for(Item item : items)
        	item.draw(batch, textureAtlas);
		for(Obstacle obstacle : obstacles)
			obstacle.draw(batch, textureAtlas);
		for(Obstacle obstacle : obstaclesWithNinePatch)
			obstacle.draw(batch);
		hero.draw(batch, GameConstants.ANIM_TIME);
		for(Leak leak : leaks)
			leak.draw(batch, animTime);
	}
}
