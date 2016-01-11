package com.libgdx.jam.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.libgdx.jam.Data;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Utils.ButtonAction;
import com.libgdx.jam.Utils.GameConstants;

public class LevelSelectionScreen implements Screen{

	final MyGdxGame game;
	private Image backgroundImage;
	private Texture backgroundTexture;
	private Stage stage;
	private Skin skin;
	private TextureAtlas textureAtlas;
	
	private TextButtonStyle textButtonStyle;
	private Array<TextButton> levels;
	private TextButton backButton;
	private Image transitionImage;
	private Table tableLevels;
	private ButtonAction buttonAction;
	private Label screenTitle;
	private LabelStyle screenTitleStyle;
	
	public LevelSelectionScreen(final MyGdxGame game){
		this.game = game;
		
		buttonAction = new ButtonAction();
		
		backgroundTexture = new Texture(Gdx.files.internal("Images/LevelScreenBackground.jpg"), true);
		backgroundTexture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
		backgroundImage = new Image(backgroundTexture);
		backgroundImage.setWidth(Gdx.graphics.getWidth());
		backgroundImage.setHeight(backgroundTexture.getHeight() * backgroundImage.getWidth()/backgroundTexture.getWidth());
		backgroundImage.setX(Gdx.graphics.getWidth()/2 - backgroundImage.getWidth()/2);
		backgroundImage.setY(Gdx.graphics.getHeight()/2 - backgroundImage.getHeight()/2);
		
		stage = new Stage();
		
		skin = new Skin();
		textureAtlas = game.assets.get("Images/Images.pack", TextureAtlas.class);
		skin.addRegions(textureAtlas);	
		
		screenTitleStyle = new LabelStyle(game.assets.get("fontMenu.ttf", BitmapFont.class), Color.WHITE);
		screenTitle = new Label("Chose a level", screenTitleStyle);
		screenTitle.setAlignment(Align.center);
		screenTitle.setX(Gdx.graphics.getWidth()/2 - screenTitle.getWidth()/2);
		screenTitle.setY(83*Gdx.graphics.getHeight()/100 - screenTitle.getHeight()/2);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("Button");
		textButtonStyle.down = skin.getDrawable("ButtonChecked");

		textButtonStyle.font = game.assets.get("fontTable.ttf", BitmapFont.class);
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.downFontColor = new Color(35/256f,59/256f,95/256f, 1);
		
		//Displaying levels
		tableLevels = new Table();
		tableLevels.defaults().width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/10).space(Gdx.graphics.getWidth()/60);
		
		levels = new Array<TextButton>();
		
		for(int i = 0; i < GameConstants.NUMBER_OF_LEVEL; i++){
			TextButton textButton = new TextButton("" + (i + 1), textButtonStyle);
			levels.add(textButton);
			if((i + 1)%5 == 0) 
				tableLevels.add(textButton).row();
			else 
				tableLevels.add(textButton);
		}

		stage.addActor(backgroundImage);
		stage.addActor(tableLevels);
	    stage.draw();
		
		tableLevels.setX(Gdx.graphics.getWidth()/2);
		tableLevels.setY(45*Gdx.graphics.getHeight()/100);		

		//Back button
		backButton = new TextButton("<", textButtonStyle);
		backButton.setWidth(Gdx.graphics.getWidth()/10);
		backButton.setHeight(Gdx.graphics.getWidth()/10);
		backButton.setX(levels.get(0).localToStageCoordinates(new Vector2(0,0)).x);
		backButton.setY(levels.get(levels.size - 1).localToStageCoordinates(new Vector2(0,0)).y - backButton.getHeight() - Gdx.graphics.getWidth()/60);
		
		//Black image for transition
		transitionImage = new Image(skin.getDrawable("WhiteSquare"));
		transitionImage.setWidth(Gdx.graphics.getWidth());
		transitionImage.setHeight(Gdx.graphics.getHeight());
		transitionImage.setColor(0, 0, 0, 1);
		transitionImage.setX(-Gdx.graphics.getWidth());
		transitionImage.setY(0);
		transitionImage.addAction(Actions.alpha(0));
		
		stage.addActor(screenTitle);
		stage.addActor(backButton);
		stage.addActor(transitionImage);
		
		for(int i = 0; i < levels.size; i++){
			if((i + 1) > Data.getLevel()){
				levels.get(i).setTouchable(Touchable.disabled);
				levels.get(i).setVisible(false);
			}
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
	    stage.act();
	    stage.draw();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		for(int i = 0; i < levels.size; i++){
			if(levels.get(i).getStyle() == textButtonStyle)
				buttonAction.niveauListener(game, levels.get(i), (i+1));
		}
		
		backButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				transitionImage.addAction(Actions.moveTo(0, 0));
				transitionImage.addAction(Actions.sequence(Actions.alpha(1, 0.2f),	 
															Actions.run(new Runnable() {
													            @Override
													            public void run() {
																	game.setScreen(new MainMenuScreen(game));
													            }})));
				
			}
		});
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		backgroundTexture.dispose();
		stage.dispose();
	}
}
