package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;

public class Wall extends Obstacle{

	public Wall(World world, OrthographicCamera camera, MapObject rectangleObject, TextureAtlas textureAtlas) {
		super(world, camera, rectangleObject, textureAtlas);	
		create(world, camera, rectangleObject);
	}


}
