package com.libgdx.jam;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libgdx.jam.Screens.LoadingScreen;

public class MyGdxGame extends Game implements ApplicationListener{
	public SpriteBatch batch;
	public AssetManager assets;
	public Music music;
	
	@Override
	public void create () {
		Data.Load();
		
		batch = new SpriteBatch();
		assets = new AssetManager();
		
		music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Music.ogg"));
		music.play();

		this.setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
