package com.gdx.bomberman.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Bomber extends Sprite {

	int direction;
	public boolean ready;

	/** Representation fo bomberman, it takes sprite, dir and ready as parameters.
	 * @param sprite actual sprite representing bomberman
	 * @param dir direction in which bomberman is lookin
	 * @param ready if bomberman is ready to start a game, it's only relevant to lobby*/
	public Bomber(Sprite sprite, int dir, boolean ready) {
		super(sprite);
		this.direction = dir;
		this.ready = ready;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}