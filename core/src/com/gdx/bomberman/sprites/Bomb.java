package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bomb extends Sprite{
	//Sprite sprite;
	public int power;
	public final float EXPLOSION_TIME = 3; //to będzie stała
	public long start;
	public float x;
	public float y;
	int bomberId;
	public int bombNumber;

	public Bomb (Sprite sprite, float x, float y, int power, int id, int bombNumber, long start){
		super(sprite);
		this.x = x;
		this.y = y;
		this.power = power;
		this.bomberId = id;
		this.bombNumber = bombNumber;
		this.start = start;
		//System.out.println(start+EXPLOSION_TIME);
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
