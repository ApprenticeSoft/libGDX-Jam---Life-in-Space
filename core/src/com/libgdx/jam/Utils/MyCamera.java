package com.libgdx.jam.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.libgdx.jam.Bodies.Hero;

public class MyCamera extends  OrthographicCamera{
	
	float posX, posY;
	
	public MyCamera(){
		super();
	}
	
	public void displacement(Hero hero, TiledMap tiledMap){
		//Positioning relative to the hero
		if(this.position.x < hero.getX() - Gdx.graphics.getWidth() * GameConstants.MPP/10)
			posX = hero.getX() - Gdx.graphics.getWidth() * GameConstants.MPP/10;
		else if(this.position.x > hero.getX() + Gdx.graphics.getWidth() * GameConstants.MPP/10)
			posX = hero.getX() + Gdx.graphics.getWidth() * GameConstants.MPP/10;
		if(this.position.y < hero.getY() - Gdx.graphics.getHeight() * GameConstants.MPP/10)
			posY = hero.getY() - Gdx.graphics.getHeight() * GameConstants.MPP/10;
		else if(this.position.y > hero.getY() + Gdx.graphics.getHeight() * GameConstants.MPP/10)
			posY = hero.getY() + Gdx.graphics.getHeight() * GameConstants.MPP/10;
		
		//Camera smooth motion
		this.position.interpolate(new Vector3(posX,posY,0), 0.45f, Interpolation.fade);
				
		//Camera Rotation	
		/*
		float angleHero = hero.heroBody.getAngle() * MathUtils.radiansToDegrees;
		while(angleHero < 0)
			angleHero += 360;
		while(angleHero > 360)
			angleHero -= 360;
		
		float angleCamera = (float)Math.atan2(this.up.x, this.up.y)*MathUtils.radiansToDegrees;
		
		this.rotate(- angleCamera - angleHero);
		*/
		
		//Positioning relative to the level map limits
		if(position.x + viewportWidth/2 > ((float)(tiledMap.getProperties().get("width", Integer.class)*GameConstants.PPT))*GameConstants.MPP)
			position.set(((float)(tiledMap.getProperties().get("width", Integer.class)*GameConstants.PPT))*GameConstants.MPP - viewportWidth/2, position.y, 0);
		else if(position.x - viewportWidth/2 < 0)
			position.set(viewportWidth/2, position.y, 0);
		if(position.y + viewportHeight/2 > ((float)(tiledMap.getProperties().get("height", Integer.class)*GameConstants.PPT))*GameConstants.MPP)
			position.set(position.x, ((float)(tiledMap.getProperties().get("height", Integer.class)*GameConstants.PPT))*GameConstants.MPP - viewportHeight/2, 0);
		else if(position.y - viewportHeight/2 < 0)
			position.set(position.x, viewportHeight/2, 0);	
		
	}
}
