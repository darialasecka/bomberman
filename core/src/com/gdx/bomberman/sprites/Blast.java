package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Blast extends Sprite{
	int power;
	float centerX;
	float centerY;
	public final float DURATION_TIME = 2;
	public long start;

	public Blast (Sprite sprite, float x, float y, long start){
		super(sprite);
		this.centerX = x;
		this.centerY = y;
		this.start = start;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
