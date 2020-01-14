package com.gdx.bomberman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.bomberman.Bomberman;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.title = "tytuł" //tak można zmienić tytuł
		config.height = 420;
		config.width = 550;//nadal za dużo
		new LwjglApplication(new Bomberman(), config);
	}
}
