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
