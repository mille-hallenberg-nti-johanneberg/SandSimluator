package main;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import cell.Cell;
import cell.CellType;
import cell.recipes.CellRecipe;
import cell.recipes.CellRecipes;
import input.InputManager;
import input.Keys;
import input.Mouse;
import states.GameState;
import states.State;

public class Game extends ApplicationAdapter {
	
	private Handler handler;
	
	ShapeRenderer sr;
	SpriteBatch batch;
	
	//Different states
	public State gameState;
	public State menuState;
	
	//Camera
	private static GameCamera gameCamera;
	private static Camera fontCamera;
	private static Viewport viewport;
	private static Viewport fontViewport;
	public static int width, height;
	
	//Input
	private InputManager input;
	
	//Game Constructor
	public Game (String title, int width, int height) {
		Game.width = width;
		Game.height = height;
	}
	public static BitmapFont font;
	
	@Override	
	//Function is called when Application first starts.
	public void create() {
		
		//Start input
		input = new InputManager();
		Gdx.input.setInputProcessor(input);
		font = new BitmapFont();
		
		handler = new Handler(this);
		gameState = new GameState(handler);
		
		State.setState(gameState);
		
		gameCamera = new GameCamera();
		
		gameCamera.setX(0);
		gameCamera.setY(0);
		
		fontCamera = new OrthographicCamera();
		
	    viewport = new ExtendViewport(width, height, gameCamera);
	    fontViewport = new ScreenViewport(fontCamera);
	    
	    sr = new ShapeRenderer();
	    sr.setAutoShapeType(true);
	    
	    batch = new SpriteBatch();
	    CellRecipes.create();
	}
	
	@Override
	//Function that resizes window
	public void resize(int viewportWidth, int viewportHeight) {
        viewport.update(viewportWidth, viewportHeight, true);
        fontViewport.update(viewportWidth, viewportHeight, true);
        
        gameCamera.position.set(width / 2f, height / 2f, 0);
        fontCamera.position.set(viewportWidth / 2f, viewportHeight / 2f, 0);
        
        gameCamera.updatePosition();
        fontCamera.update(true);
        viewport.apply();
        fontViewport.apply();
	}
	
	//Game Logic
	void tick() {
		if (Keys.A.pressed) {
			gameCamera.setX(gameCamera.getX()-1);
		}
		if (Keys.D.pressed) {
			gameCamera.setX(gameCamera.getX()+1);
		}
		if (Keys.S.pressed) {
			gameCamera.setY(gameCamera.getY()-1);
		}
		if (Keys.W.pressed) {
			gameCamera.setY(gameCamera.getY()+1);
		}
		
		if (Keys.P.pressed) {
			gameCamera.changeZoom(-0.02f, Mouse.getX(), Mouse.getY());
		}
		if (Keys.O.pressed) {
			gameCamera.changeZoom(+0.02f, Mouse.getX(), Mouse.getY());
		}
		if (Keys.R.pressed) {
			gameState = new GameState(handler);
			State.setState(gameState);
		}
		
		if (State.getState() != null) {
			State.getState().update(input);
		}
		
		//Update InputManager
		input.update();
	}
	
	@Override
	//Function is called 60 times a second.
	public void render() {
		//Clear Canvas
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tick();
		
		//Prepare batches
		sr.setProjectionMatrix(gameCamera.combined);
		batch.setProjectionMatrix(fontCamera.combined);
		
		//Update Camera
		gameCamera.update();
		
		//Open batch for ShapeRenderer (Rendering for World etc)
		sr.begin();
		sr.set(ShapeType.Filled);
		if (State.getState() != null) {
			State.getState().render(sr, handler.getGameCamera());
		}
		sr.end();
		//Close ShapeRenderer batch
		
		//Open batch for SpriteBatch (Rendering Text and Interface)
		batch.begin();
		if (State.getState() != null) {
			State.getState().renderUi(batch, handler.getGameCamera());
		}
		
		font.setColor(Color.RED);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 8, Gdx.graphics.getHeight() - 8);
		
		batch.end();
		//Close batch for SpriteBatch
	}

	//Getters and setters
	public static GameCamera getGameCamera() {
		return gameCamera;
	}

	public InputManager getInput() {
		return input;
	}	
}
