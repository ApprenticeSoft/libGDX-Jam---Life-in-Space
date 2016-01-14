package com.libgdx.jam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.libgdx.jam.MyGdxGame;

public class MainMenuScreen implements Screen{

	final MyGdxGame game;
	private OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private Texture backgroundTexture, transitionTexture;
	private Image backgroundImage, transitionImage;
	private TextureAtlas textureAtlas;
	private TextButton playButton, optionButton;
	private TextButtonStyle textButtonStyle;
	
	public MainMenuScreen(final MyGdxGame game){
		this.game = game;
		
		if(!game.music.isPlaying())
			game.music.play();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		//Background
		backgroundTexture = new Texture(Gdx.files.internal("Images/Logo.jpg"), true);
		backgroundTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
		backgroundImage = new Image(backgroundTexture);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(backgroundTexture.getHeight() * backgroundImage.getWidth()/backgroundTexture.getWidth());
		backgroundImage.setX(Gdx.graphics.getWidth()/2 - backgroundImage.getWidth()/2);
		backgroundImage.setY(Gdx.graphics.getHeight()/2 - backgroundImage.getHeight()/2);
		
		//Transition image
		transitionTexture = new Texture(Gdx.files.internal("Images/LevelScreenBackground.jpg"), true);
		transitionTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
		transitionImage = new Image(transitionTexture);
		transitionImage.setWidth(Gdx.graphics.getWidth());
		transitionImage.setHeight(transitionTexture.getHeight() * transitionImage.getWidth()/transitionTexture.getWidth());
		transitionImage.setX(Gdx.graphics.getWidth()/2 - transitionImage.getWidth()/2);
		transitionImage.setY(Gdx.graphics.getHeight()/2 - transitionImage.getHeight()/2);
		transitionImage.addAction(Actions.alpha(0));
		transitionImage.setVisible(false);
		
		stage = new Stage();
		skin = new Skin();
		
		textureAtlas = game.assets.get("Images/Images.pack", TextureAtlas.class);
		skin.addRegions(textureAtlas);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("Button");
		textButtonStyle.down = skin.getDrawable("ButtonChecked");
		textButtonStyle.font = game.assets.get("fontMenu.ttf", BitmapFont.class);
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.downFontColor = new Color(0, 0, 0, 1);
		
		playButton = new TextButton("PLAY", textButtonStyle);
		playButton.setHeight(Gdx.graphics.getHeight()/7);
		playButton.setX(Gdx.graphics.getWidth()/2 - playButton.getWidth()/2);
		playButton.setY(29 * Gdx.graphics.getHeight()/100 - playButton.getHeight()/2);
		
		optionButton = new TextButton("OPTIONS", textButtonStyle);
		optionButton.setWidth(Gdx.graphics.getWidth()/3);
		optionButton.setHeight(Gdx.graphics.getHeight()/10);
		optionButton.setX(playButton.getX());
		optionButton.setY(playButton.getY() - optionButton.getHeight() - Gdx.graphics.getHeight()/100);
		
		stage.addActor(backgroundImage);
		stage.addActor(playButton);
		stage.addActor(transitionImage);
		
		playButton.addAction(Actions.sequence(Actions.alpha(0)
                ,Actions.fadeIn(0.25f)));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.setProjectionMatrix(camera.combined);
		
		stage.act();
		stage.draw();
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		playButton.addListener(new ClickListener(){
			 @Override
		        public void clicked(InputEvent event, float x, float y) {
				transitionImage.setVisible(true);
				transitionImage.addAction(Actions.sequence(Actions.alpha(1, 0.2f),	 
															Actions.run(new Runnable() {
													            @Override
													            public void run() {
																	game.setScreen(new LevelSelectionScreen(game));
													            }})));
			 }
		});
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		this.dispose();
		stage.dispose();
		backgroundTexture.dispose(); 
		transitionTexture.dispose();
		skin.dispose();
		textureAtlas.dispose();
	}
}
