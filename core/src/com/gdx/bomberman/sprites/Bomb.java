package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bomb extends Sprite{
	//Sprite sprite;
	int power;
	float timer; //to będzie stała
	float x;
	float y;
	int bomberId;
	int bombNumber;

	public Bomb (Sprite sprite, float x, float y, int power, int id, int bombNumber){
		super(sprite);
		this.x = x;
		this.y = y;
		this.power = power;
		this.bomberId = id;
		this.bombNumber = bombNumber;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
