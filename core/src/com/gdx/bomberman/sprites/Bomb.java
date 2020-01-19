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
	public int bomberId;
	public int bombNumber;

	/** Bomb that was but by bomber, it takes sprite, x, y, start, power, id and bombNumber as parameters.
	 * @param sprite sprite of a bomb
	 * @param x position x of the bomb
	 * @param y position y of the bomb
	 * @param power power of expolsion bomb will make
	 * @param id id of bomber who put a bomb
	 * @param start start time of when the blast started
	 * @param bombNumber bomb number from which blast originated*/

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
