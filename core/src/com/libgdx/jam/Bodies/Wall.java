package com.libgdx.jam.Bodies;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.GameConstants;

public class Wall extends Obstacle{

	public Wall(final MyGdxGame game, World world, OrthographicCamera camera, MapObject rectangleObject, TextureAtlas textureAtlas) {
		super(game, world, camera, rectangleObject, textureAtlas);	
		create(world, camera, rectangleObject);

		ninePatch = new NinePatch(textureAtlas.findRegion("Wall"), 49, 49, 49, 49);
		ninePatch.scale(0.5f*GameConstants.MPP, 0.5f*GameConstants.MPP);
	}
}
