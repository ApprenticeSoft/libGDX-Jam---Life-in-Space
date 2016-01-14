package com.libgdx.jam.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.GameConstants;

public class Exit extends Obstacle{
	
	private Animation exitAnimation;
	public boolean open = false;
	public boolean heroContact = false;
	private boolean soundPlayed = false;
	private float animTime = 0;
	
	public Exit(final MyGdxGame game, World world, OrthographicCamera camera, MapObject rectangleObject) {
		super(game, world, camera, rectangleObject);
		create(world, camera, rectangleObject);
		
		sound = game.assets.get("Sounds/Exit.ogg", Sound.class);
		
		body.getFixtureList().get(0).setSensor(true);
		body.getFixtureList().get(0).setUserData("Exit");
		body.setUserData("Exit");
		

		exitAnimation = new Animation(0.04f, game.assets.get("Images/Exit_Animation.pack", TextureAtlas.class).findRegions("Exit_Animation"), Animation.PlayMode.NORMAL);
	}
	
	public void active(){
		if(open){
			animTime += Gdx.graphics.getDeltaTime();
		}
		
		if(exitAnimation.isAnimationFinished(animTime) && heroContact)
			GameConstants.LEVEL_FINISHED = true;	
		
		if(exitAnimation.getKeyFrameIndex(animTime) == 27 && !soundPlayed){
			sound.play();
			soundPlayed = true;
		}
	}
	
	@Override
	public void draw(SpriteBatch batch){		
		batch.setColor(1, 1, 1, 1);
		batch.draw(exitAnimation.getKeyFrame(animTime), 
				this.body.getPosition().x - width, 
				this.body.getPosition().y - height,
				2 * width,
				2 * height);
	}
}
