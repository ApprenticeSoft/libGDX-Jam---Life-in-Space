package com.libgdx.jam.Utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Screens.GameScreen;

public class ButtonAction {
	
	public ButtonAction(){
	}
	
	public void niveauListener(final MyGdxGame game, TextButton bouton, final int niveau){
		bouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				GameConstants.SELECTED_LEVEL = niveau;
				try{
					game.setScreen(new GameScreen(game));
				}
					catch(Exception e){
					System.out.println("The level doesn't exist !");
				}
				System.out.println("Num�ro du niveau : " + GameConstants.SELECTED_LEVEL);
			}
		});
	}
}
