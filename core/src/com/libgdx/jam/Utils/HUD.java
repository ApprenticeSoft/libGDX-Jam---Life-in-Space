package com.libgdx.jam.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Bodies.Hero;
import com.libgdx.jam.Screens.GameScreen;
import com.libgdx.jam.Screens.MainMenuScreen;

public class HUD {
	
	final MyGdxGame game;
	public Image OxygenBar, FuelBar;
	private float posXOxygen, posYOxygen, width, height, outOfFuelAlpha;
	private Hero hero;
	private Table tableWin, tableLose, tablePause;
	private TextButtonStyle textButtonStyle;
	private TextButton nextButton, replayButton, replayButton2, replayButton3, menuButton, menuButton2, resumeButton;
	private LabelStyle menulabelStyle, hudLabelStyle;
	private Label outOfFuelLabel, loseLabel, oxygenLabel, fuelLabel;
	private Image imageTableBackground, imageOxygenLevel, imageFuelLevel;
	//private Skin skin;
	public String loseString;
	
	public HUD(final MyGdxGame game, Stage stage, Skin skin, Hero hero){
		this.game = game;
		this.hero = hero;
		//this.skin = skin;

		outOfFuelAlpha = 0;
		posXOxygen = 9 * Gdx.graphics.getWidth()/100;
		posYOxygen = 95 * Gdx.graphics.getHeight()/100;
		width = Gdx.graphics.getWidth()/3;
		height = Gdx.graphics.getHeight()/70;
		loseString = "You lost !";

		menulabelStyle = new LabelStyle(game.assets.get("fontMenu.ttf", BitmapFont.class), Color.WHITE);
		hudLabelStyle = new LabelStyle(game.assets.get("fontHUD.ttf", BitmapFont.class), Color.WHITE);
		
		outOfFuelLabel = new Label("PRESS ESC TO RESTART", hudLabelStyle);
		outOfFuelLabel.setX(Gdx.graphics.getWidth()/2 - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), outOfFuelLabel.getText()).width/2);
		outOfFuelLabel.setY(Gdx.graphics.getHeight()/2 - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), outOfFuelLabel.getText()).height/2);
		outOfFuelLabel.addAction(Actions.alpha(0));
		
		oxygenLabel = new Label("OXYGEN", hudLabelStyle);
		oxygenLabel.setX(posXOxygen - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "OXYGEN").width - Gdx.graphics.getWidth()/100);
		oxygenLabel.setY(posYOxygen - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "OXYGEN").height/2);
		
		fuelLabel = new Label("FUEL", hudLabelStyle);
		fuelLabel.setX(posXOxygen - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "FUEL").width - Gdx.graphics.getWidth()/100);
		fuelLabel.setY(posYOxygen - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "FUEL").height/2 - 2 * height);

		loseLabel = new Label(loseString, menulabelStyle);
		
        textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("Button");
		textButtonStyle.down = skin.getDrawable("ButtonChecked");
		textButtonStyle.font = game.assets.get("fontTable.ttf", BitmapFont.class);
		textButtonStyle.fontColor = Color.WHITE;

		//Win table buttons
		nextButton = new TextButton("NEXT", textButtonStyle);	
		replayButton = new TextButton("PLAY AGAIN", textButtonStyle);
		//Lose table buttons
		replayButton2 = new TextButton("PLAY AGAIN", textButtonStyle);
		menuButton = new TextButton("MENU", textButtonStyle);
		//Pause table buttons
		replayButton3 = new TextButton("PLAY AGAIN", textButtonStyle);
		menuButton2 = new TextButton("MENU", textButtonStyle);
		resumeButton = new TextButton("RESUME", textButtonStyle);
		
		tableWin = new Table();
		tableWin.setFillParent(true);
		tableWin.row().colspan(2);
		tableWin.add(new Label("LEVEL CLEARED", menulabelStyle)).padBottom(Gdx.graphics.getHeight()/22);
		tableWin.row().width(Gdx.graphics.getWidth()/4);
		tableWin.add(nextButton).spaceRight(Gdx.graphics.getWidth()/100);
		tableWin.add(replayButton);
		tableWin.addAction(Actions.alpha(0));
		tableWin.setVisible(false);
		
		tableLose = new Table();
		tableLose.setFillParent(true);
		tableLose.row().colspan(2);
		tableLose.add(loseLabel).padBottom(Gdx.graphics.getHeight()/22);
		tableLose.row().width(Gdx.graphics.getWidth()/4);
		tableLose.add(replayButton2).spaceRight(Gdx.graphics.getWidth()/100);
		tableLose.add(menuButton);
		tableLose.addAction(Actions.alpha(0));
		tableLose.setVisible(false);
		
		tablePause = new Table();
		tablePause.setFillParent(true);
		tablePause.add(resumeButton).width(replayButton3.getPrefWidth()).pad(Gdx.graphics.getHeight()/50).row();
		tablePause.add(replayButton3).width(replayButton3.getPrefWidth()).pad(Gdx.graphics.getHeight()/50).row();
		tablePause.add(menuButton2).width(replayButton3.getPrefWidth()).pad(Gdx.graphics.getHeight()/50);
		tablePause.addAction(Actions.alpha(0));
		tablePause.setVisible(false);

		imageTableBackground = new Image(skin.getDrawable("imageTable"));
		imageTableBackground.setColor(0,0,0.25f,1);
		imageTableBackground.setWidth(1.15f*tableWin.getPrefWidth());
		imageTableBackground.setHeight(1.15f*tableWin.getPrefHeight());
		imageTableBackground.addAction(Actions.alpha(0));
		
		imageOxygenLevel = new Image(skin.getDrawable("WhiteSquare"));
		imageOxygenLevel.setColor(0,0,1,1);
		imageOxygenLevel.setWidth(width);
		imageOxygenLevel.setHeight(height);
		imageOxygenLevel.setX(posXOxygen);
		imageOxygenLevel.setY(posYOxygen);
		
		imageFuelLevel = new Image(skin.getDrawable("WhiteSquare"));
		imageFuelLevel.setColor(1,0,0,1);
		imageFuelLevel.setWidth(width);
		imageFuelLevel.setHeight(height);
		imageFuelLevel.setX(posXOxygen);
		imageFuelLevel.setY(posYOxygen - 2 * height);

		stage.addActor(imageTableBackground);
		stage.addActor(tableWin);
		stage.addActor(tableLose);
		stage.addActor(tablePause);
		stage.addActor(outOfFuelLabel);
		stage.addActor(oxygenLabel);
		stage.addActor(fuelLabel);
		stage.addActor(imageOxygenLevel);
		stage.addActor(imageFuelLevel);
	}
	
	public void draw(){
		/*
		//Oxygen level
		game.batch.setColor(0,0,1,1);
		game.assets.get("fontHUD.ttf", BitmapFont.class).draw(	game.batch, 
																"Oxygen", 
																(posXOxygen - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "Oxygen").width - Gdx.graphics.getWidth()/100) * GameConstants.MPP, 
																(posYOxygen + new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "Oxygen").height) * GameConstants.MPP);
		game.batch.draw(skin.getRegion("WhiteSquare"),
						posXOxygen * GameConstants.MPP, 
						posYOxygen * GameConstants.MPP, 
						width * hero.getOxygenLevel()/GameConstants.MAX_OXYGEN * GameConstants.MPP, 
						height * GameConstants.MPP);
		
		//Fuel level
		game.batch.setColor(1,0,0,1);
		game.assets.get("fontHUD.ttf", BitmapFont.class).draw(	game.batch, 
																"Fuel", 
																posXOxygen - new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "Fuel").width - Gdx.graphics.getWidth()/100, 
																posYOxygen + new GlyphLayout(game.assets.get("fontHUD.ttf", BitmapFont.class), "Fuel").height - 2 * height);
		game.batch.draw(skin.getRegion("WhiteSquare"), 
						posXOxygen, 
						posYOxygen - 2 * height, 
						width * hero.getFuelLevel()/GameConstants.MAX_FUEL, 
						height);	
		*/	
		
		//Test		
		imageOxygenLevel.setWidth(width * hero.getOxygenLevel()/GameConstants.MAX_OXYGEN);	
		imageFuelLevel.setWidth(width * hero.getFuelLevel()/GameConstants.MAX_FUEL);
		
		if(hero.getFuelLevel() <= 0)
			imageFuelLevel.addAction(Actions.alpha(0));	
		if(hero.getOxygenLevel() <= 0)
			imageOxygenLevel.addAction(Actions.alpha(0));		
		
	}
	
	public void win(){
		GameConstants.GAME_PAUSED = true;
		tableWin.setVisible(true);
		
		imageTableBackground.setWidth(tableWin.getPrefWidth() + Gdx.graphics.getWidth()/20);
		imageTableBackground.setHeight(tableWin.getPrefHeight() + Gdx.graphics.getWidth()/20);
		
		tableWin.addAction(Actions.alpha(1, 0.25f));
		imageTableBackground.addAction(Actions.sequence(Actions.moveTo(	Gdx.graphics.getWidth()/2 - imageTableBackground.getWidth()/2, 
																		Gdx.graphics.getHeight()/2 - imageTableBackground.getHeight()/2),
														Actions.alpha(1, 0.25f)));	
	
	}
	
	public void lose(){
		GameConstants.GAME_PAUSED = true;
		tableLose.setVisible(true);
		
		loseLabel.setText(loseString);
		imageTableBackground.setWidth(tableLose.getPrefWidth() + Gdx.graphics.getWidth()/20);
		imageTableBackground.setHeight(tableLose.getPrefHeight() + Gdx.graphics.getWidth()/20);
			
		tableLose.addAction(Actions.alpha(1, 0.25f));
		imageTableBackground.addAction(Actions.sequence(Actions.moveTo(	Gdx.graphics.getWidth()/2 - imageTableBackground.getWidth()/2, 
																		Gdx.graphics.getHeight()/2 - imageTableBackground.getHeight()/2),
														Actions.alpha(1, 0.25f)));		
	}
	
	public void outOfFuel(){
		outOfFuelAlpha += 4 * Gdx.graphics.getDeltaTime();		
		outOfFuelLabel.addAction(Actions.alpha((float)(1 + Math.cos(outOfFuelAlpha))/2));	
	}
	
	public void pause(){
		GameConstants.GAME_PAUSED = true;
		tablePause.setVisible(true);
			
		imageTableBackground.setWidth(tablePause.getPrefWidth() + Gdx.graphics.getWidth()/20);
		imageTableBackground.setHeight(tablePause.getPrefHeight() + Gdx.graphics.getWidth()/20);
		
		tablePause.addAction(Actions.alpha(1, 0.25f));
		imageTableBackground.addAction(Actions.sequence(Actions.moveTo(	Gdx.graphics.getWidth()/2 - imageTableBackground.getWidth()/2, 
																		Gdx.graphics.getHeight()/2 - imageTableBackground.getHeight()/2),
														Actions.alpha(1, 0.25f)));	
			
	}
	
	public void buttonListener(){
		nextButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new GameScreen(game));
				}
		});
		
		replayButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new GameScreen(game));
				}
		});
		
		replayButton2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new GameScreen(game));
				}
		});
		
		replayButton3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new GameScreen(game));
				}
		});
		
		menuButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		menuButton2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		resumeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				GameConstants.GAME_PAUSED = false;
				imageTableBackground.addAction(Actions.alpha(0, 0.15f));
		       	tablePause.addAction(Actions.alpha(0, 0.15f));
				tablePause.setVisible(false);
			}
		});
	}
}
