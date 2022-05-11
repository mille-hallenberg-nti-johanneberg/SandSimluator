package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

//This class contains positions and its zoom. This class is used for the camera in the World.
public class GameCamera extends OrthographicCamera {
	private float zoom = 1f;
	private float x, y;
	
	private float maxZoom = 0.01f;
	
	//Constructor 
	public GameCamera() {
		super(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    super.position.set(super.viewportWidth / 2, super.viewportHeight / 2, 0);
	}

	
	
	//Zooms in by z amount on point z, y.
	public void changeZoom(float zoom, int pointX, int pointY) {
		var worldPosBefore = super.unproject(new Vector3(pointX, pointY, 0));
		float worldXBefore = worldPosBefore.x, worldYBefore = worldPosBefore.y;
		
		
		this.zoom += zoom * (this.zoom - maxZoom);
		if (this.zoom < maxZoom) this.zoom = maxZoom;
		super.zoom = this.zoom;
		super.update();
		
		var worldPosAfter = super.unproject(new Vector3(pointX, pointY, 0));
		float worldXAfter = worldPosAfter.x, worldYAfter = worldPosAfter.y;
		
		x-=(worldXAfter - worldXBefore);
		y-=(worldYAfter - worldYBefore);
		
		updatePosition();
	}
	
	public void moveX(float amount) {
		x += amount;
		updatePosition();
	}
	
	public void moveY(float amount) {
		y += amount;
		updatePosition();
	}
	
	//Updates 
	void updatePosition() {
		super.position.set(this.x, this.y, 0);
		super.update();
	}

	//Getters and setters
	public float getZoom() {
		return zoom;
	}
	
	//Sets the zoom of GameCamera
	public void setZoom(float zoom) {
		this.zoom = zoom;
		if (this.zoom < maxZoom) this.zoom = maxZoom;
		super.zoom = zoom;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		updatePosition();
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		updatePosition();
	}
	
	
	
}
