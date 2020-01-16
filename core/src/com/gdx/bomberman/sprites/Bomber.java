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



public class Bomber extends Sprite /* implements InputProcessor*/ {
	Vector2 previousPosition;

	int direction;

	String wall = "blocked";

	public Bomber(Sprite sprite, int dir) {
		super(sprite);
		this.direction = dir;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}