package com.gdx.bomberman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.bomberman.Bomberman;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.title = "tytuł" //tak można zmienić tytuł
		config.height = 412;//410
		config.width = 542;//530
		new LwjglApplication(new Bomberman(), config);
	}
}
