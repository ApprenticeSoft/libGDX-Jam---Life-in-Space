package com.libgdx.jam.Screens;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.libgdx.jam.MyGdxGame;
import com.libgdx.jam.Bodies.ItemSwitch;
import com.libgdx.jam.Bodies.Leak;
import com.libgdx.jam.Bodies.Obstacle;
import com.libgdx.jam.Items.Item;
import com.libgdx.jam.Utils.GameConstants;
import com.libgdx.jam.Utils.HUD;
import com.libgdx.jam.Utils.MyCamera;
import com.libgdx.jam.Utils.OrthogonalTiledMapRendererWithSprites;
import com.libgdx.jam.Utils.TiledMapReader;

public class GameScreen implements Screen{

	final MyGdxGame game;
	private MyCamera camera;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	private TiledMapReader mapReader;
	private World world;
    private Box2DDebugRenderer debugRenderer;
    
    //Graphics
	private TextureAtlas textureAtlas;
	private Skin skin;
    private HUD hud;
    private Stage stage;
    
    //Animation
    private float animTime;
    
    //Background
    private Texture backgroundTexture;
    private float levelPixelWidth, levelPixelHeight, backgroundTime;
    
	private RayHandler rayHandler;
	
	public GameScreen(final MyGdxGame game){
		this.game= game;
		
		GameConstants.GAME_PAUSED = false;
		GameConstants.ANIM_TIME = 0;
		
		camera = new MyCamera();
		camera.setToOrtho(false, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        camera.update();  
        
        world = new World(new Vector2(0, GameConstants.GRAVITY), true);
        World.setVelocityThreshold(0.0f);
        debugRenderer = new Box2DDebugRenderer();
        
        tiledMap = new TmxMapLoader().load("Levels/Level 3.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap, GameConstants.MPP, game.batch);

        mapReader = new TiledMapReader(game, tiledMap, world, camera); 
             
        //Graphics
        stage = new Stage();
		textureAtlas = game.assets.get("Images/Images.pack", TextureAtlas.class);
		skin = new Skin();
		skin.addRegions(textureAtlas);
        hud = new HUD(game, stage, skin, mapReader.hero);

        //Background		
		backgroundTexture = new Texture(Gdx.files.internal("Images/Stars.jpg"), true);
		backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        
        levelPixelWidth = Float.parseFloat(tiledMap.getProperties().get("width").toString()) * GameConstants.PPT * GameConstants.MPP;
        levelPixelHeight = Float.parseFloat(tiledMap.getProperties().get("height").toString()) * GameConstants.PPT * GameConstants.MPP;	
        
        //Test Box2DLight
        rayHandler = new RayHandler(world); 
        rayHandler.resizeFBO(Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/5);
        rayHandler.setShadows(false);    
        rayHandler.setBlur(true);
        rayHandler.setAmbientLight(new Color(.1f,.1f,.1f,.1f));
	}

	@Override
	public void render(float delta) {  
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.displacement(mapReader.hero, tiledMap);
        camera.update();      
        tiledMapRenderer.setView(camera);  

        //Animation
        GameConstants.ANIM_TIME += Gdx.graphics.getDeltaTime();
        backgroundTime += Gdx.graphics.getDeltaTime();
        
		if(!GameConstants.GAME_PAUSED){     		
	        if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
	        	hud.pause();
	        }
	        
			world.step(GameConstants.BOX_STEP, GameConstants.BOX_VELOCITY_ITERATIONS, GameConstants.BOX_POSITION_ITERATIONS);
			mapReader.active();
	        
	        if(mapReader.hero.getOxygenLevel() <= 0){
	        	hud.loseString = "OUT OF OXYGEN !";
	        	hud.lose();
	        }
	        else if (mapReader.hero.getFuelLevel() <= 0)
	        	hud.outOfFuel();	
		}

		stage.act();
		
		//Drawing graphics
		//Background	
		game.batch.begin();
		game.batch.draw(backgroundTexture, 
				0, 
				0, 
				levelPixelWidth, 
				levelPixelHeight,  
				(int)(backgroundTime * 8), 
				0, 
				(int)(levelPixelWidth * 20), 
				(int)(levelPixelHeight * 20), 
				false, 
				false);
		game.batch.end();
		
		//Game map
        tiledMapRenderer.render();
		debugRenderer.render(world, camera.combined);
		    
		//HUD and hero
		game.batch.begin();
		for(Obstacle obstacle : mapReader.obstaclesWithNinePatch)
			obstacle.draw(game.batch);
		for(Obstacle obstacle : mapReader.obstacles)
			obstacle.draw(game.batch, textureAtlas);
		hud.draw();
		game.batch.setColor(1, 1, 1, 1);
		mapReader.hero.draw(game.batch, GameConstants.ANIM_TIME);
		game.batch.end();

		stage.draw();
        
		//Test Box2DLight
		rayHandler.setCombinedMatrix(camera);
		rayHandler.updateAndRender();
		
		if (Gdx.input.isKeyPressed(Keys.Z)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Keys.X)) {
            camera.zoom -= 0.02;
        }
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		hud.buttonListener();
		
		world.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
			    Body bodyB = contact.getFixtureB().getBody();
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
			    
			    if(fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
			    	//Finish the level
			    	if(fixtureA.getUserData().equals("Tom") && fixtureB.getUserData().equals("Exit"))
			    		hud.win();
			    		//System.out.println("LEVEL FINISHED !!!!");
			    	else if(fixtureB.getUserData().equals("Tom") && fixtureA.getUserData().equals("Exit"))
			    		hud.win();
				    	//System.out.println("LEVEL FINISHED !!!!");
			    	
			    	//Leak
				    if (fixtureA.getUserData().equals("Leak") && fixtureB.getBody().getType() == BodyType.DynamicBody) {
				    	for(Obstacle obstacle : mapReader.obstacles){
				    		if(obstacle.body.getFixtureList().get(0) == fixtureA){
				    			Leak leak = (Leak) obstacle;
				    			leak.addBody(fixtureB);
				    		}
				    	}
					} 
				    else if (fixtureB.getUserData().equals("Leak") && fixtureA.getBody().getType() == BodyType.DynamicBody) {
				    	for(Obstacle obstacle : mapReader.obstacles){
				    		if(obstacle.body.getFixtureList().get(0) == fixtureB){
				    			Leak leak = (Leak) obstacle;
				    			leak.addBody(fixtureA);
				    		}
				    	}
					}
				    
				    //Switch
			    	if(fixtureA.getUserData().equals("Tom") && fixtureB.getUserData().equals("Switch")){
			    		for(ItemSwitch itemSwitch : mapReader.switchs){
			    			if(itemSwitch.swtichBody == fixtureB.getBody())
			    				itemSwitch.active(mapReader.obstacles);
			    		}
			    	}
			    	else if(fixtureB.getUserData().equals("Tom") && fixtureA.getUserData().equals("Switch")){
			    		for(ItemSwitch itemSwitch : mapReader.switchs){
			    			if(itemSwitch.swtichBody == fixtureA.getBody())
			    				itemSwitch.active(mapReader.obstacles);
			    		}
			    	}
				    
			    	//Items
			    	if(fixtureA.getUserData().equals("Tom") && fixtureB.getUserData().equals("Item")){
			    		for(Item item : mapReader.items){
			    			if(item.body == fixtureB.getBody())
			    				item.activate();
			    		}
			    	}
			    	else if(fixtureB.getUserData().equals("Tom") && fixtureA.getUserData().equals("Item")){
			    		for(Item item : mapReader.items){
			    			if(item.body == fixtureA.getBody())
			    				item.activate();
			    		}
			    	}
				}  
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				
				if(fixtureA.getUserData() != null && fixtureB.getUserData() != null) {
			    	//Leak
				    if (fixtureA.getUserData().equals("Leak") && fixtureB.getBody().getType() == BodyType.DynamicBody) {
				    	for(Obstacle obstacle : mapReader.obstacles){
				    		if(obstacle.body.getFixtureList().get(0) == fixtureA){
				    			Leak leak = (Leak) obstacle;
				    			leak.removeBody(fixtureB);
				    		}
				    	}
					} 
				    else if (fixtureB.getUserData().equals("Leak") && fixtureA.getBody().getType() == BodyType.DynamicBody) {
				    	for(Obstacle obstacle : mapReader.obstacles){
				    		if(obstacle.body.getFixtureList().get(0) == fixtureB){
				    			Leak leak = (Leak) obstacle;
				    			leak.removeBody(fixtureA);
				    		}
				    	}
					}
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				
				if((fixtureA.getUserData() != null && fixtureA.getUserData().equals("Obstacle")) && (fixtureB.getUserData() != null && fixtureB.getUserData().equals("Obstacle"))) {
			    	contact.setEnabled(false);
				}
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				Body bodyA = contact.getFixtureA().getBody();
			    Body bodyB = contact.getFixtureB().getBody();
			    
				//Hero death by crushing
			    if(bodyA.getUserData().equals("Tom") || bodyB.getUserData().equals("Tom")){ 
			    	for(int i = 0; i < impulse.getNormalImpulses().length; i++){
				    	if(impulse.getNormalImpulses()[i] > GameConstants.CRUSH_IMPULSE){
				        	hud.loseString = "CRUSHED !";
				        	hud.lose();
				    	}
			    	}
			    }
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
		stage.dispose();			
	}

}
