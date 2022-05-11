package main;

import input.InputManager;
import world.World;

//This class should have been used much more. But in a nutshell it is a class that works like a 
//"bridge" between classes. 
public class Handler {
	
	private Game game;
	private World world;
	
	//Getters and setters
	
	public Handler(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public InputManager getInput() {
		return game.getInput();
	}
}
