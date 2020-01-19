package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.org.apache.xpath.internal.objects.XNumber;

public class Blast extends Sprite{
	int power;
	public float x;
	public float y;
	public final float DURATION_TIME = 2;
	public long start;
	public int bombNumber;

	/** Individual Blast part created after bomb explosion, it takes sprite, x, y, start and bombNumber as parameters.
	 * @param sprite tells if this is center of the blast, or horizontal or vertical part of it
	 * @param x position x of the blast
	 * @param y position y of the blast
	 * @param start start time of when the blast started
	 * @param bombNumber bomb number from which blast originated*/
	public Blast (Sprite sprite, float x, float y, long start, int bombNumber){
		super(sprite);
		this.x = x;
		this.y = y;
		this.start = start;
		this.bombNumber = bombNumber;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
