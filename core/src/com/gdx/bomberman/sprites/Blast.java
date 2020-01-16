package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Blast extends Sprite{
	int power;
	float centerX;
	float centerY;

	public Blast (Sprite sprite, float x, float y, int power){
		super(sprite);
		this.centerX = x;
		this.centerY = y;
		this.power = power;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
