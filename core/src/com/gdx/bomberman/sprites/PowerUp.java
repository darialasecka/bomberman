package com.gdx.bomberman.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerUp extends Sprite{
	//Sprite sprite;
	public float x;
	public float y;

	/** Pickable power up, it takes sprite, x, y as parameters.
	 * @param sprite sprite of a certain power up
	 * @param x position x of the power up
	 * @param y position y of the power up*/
	public PowerUp (Sprite sprite, float x, float y){
		super(sprite);
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch spriteBatch){
		super.draw(spriteBatch);
	}
}
