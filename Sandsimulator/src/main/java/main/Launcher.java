package main;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Launcher {
	//This is called
	public static void main(String[] args) {
		System.out.println("Program started");
		var config = new LwjglApplicationConfiguration();
		
		//Window icon
		config.addIcon("images/BarIcon.png", FileType.Internal);
		
		//LibGDX Application
		new LwjglApplication(new Game("Sandsimulator", 1980/2, 1080/2), config);
	}

}
