package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Box extends Sprite{
	//Sprite sprite;
	public float x;
	public float y;

	/** Destructable box, it takes sprite, x, y as parameters.
	 * @param sprite sprite of a box
	 * @param x position x of the box
	 * @param y position y of the box*/

	public Box (Sprite sprite, float x, float y){
		super(sprite);
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
