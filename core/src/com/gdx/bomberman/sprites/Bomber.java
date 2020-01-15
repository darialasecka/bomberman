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

	Vector2 velocity = new Vector2();
	float speed = 60 * 2;
	float gravity = 60 * 1.8f, increment;
	TiledMapTileLayer collisionLayer;
	int direction;

	String wall = "blocked";

	public Bomber(Sprite sprite, TiledMapTileLayer collisionLayer, int dir) {
		super(sprite);
		this.collisionLayer = collisionLayer;
		this.direction = dir;
		//previousPosition = new Vector2(getX(), getY());
	}

	public void draw(SpriteBatch spriteBatch){
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public void update(float delta){
		velocity.y -= gravity * delta;

		if(velocity.y > speed){
			velocity.y = speed;
		} else if (velocity.y < speed){
			velocity.y = -speed;
		}

		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collisionX = false, collisionY = false;

		boolean collisionX1 = false;
		boolean collisionX2 = false;

		setX(getX() + velocity.x * delta);

		if(direction == 0){

			collisionX = collisionLayer.getCell( (int) (getX() / tileWidth),  (int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(wall);

			//if(!collisionX)
				collisionX1 = collisionLayer.getCell((int)(getX() / tileWidth), (int)((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey(wall);

			//if(!collisionX && !collisionX2)
				collisionX2 = collisionLayer.getCell((int)(getX() / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey(wall);

		} else if (direction == 1){
			collisionX = collisionLayer.getCell( (int) ((getX() + getWidth()) / tileWidth),  (int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(wall);

			if(!collisionX)
				collisionX = collisionLayer.getCell((int)((getX() + getWidth()) / tileWidth), (int)((getY() + getHeight() / 2) / tileHeight)).getTile().getProperties().containsKey(wall);

			if(!collisionX)
				collisionX = collisionLayer.getCell((int)((getX() + getWidth()) / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey(wall);

		}

		if(!collisionX){
			setX(oldX);
			velocity.x = 0;
		}

		setY(getY() + velocity.y * delta);

		if(direction == 3){
			collisionY = collisionLayer.getCell( (int) (getX() / tileWidth),  (int)(getY() / tileHeight)).getTile().getProperties().containsKey(wall);

			if(!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + getWidth() / 2) / tileWidth), (int)(getY()/ tileHeight)).getTile().getProperties().containsKey(wall);

			if(!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + getWidth()) / tileWidth), (int)(getY() / tileHeight)).getTile().getProperties().containsKey(wall);

		} else if (direction == 4){
			collisionY = collisionLayer.getCell( (int) (getX() / tileWidth),  (int)((getY() + getHeight())/ tileHeight)).getTile().getProperties().containsKey(wall);

			if(!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + getWidth() / 2) / tileWidth), (int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(wall);

			if(!collisionY)
				collisionY = collisionLayer.getCell((int)((getX() + getWidth()) / tileWidth), (int)((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey(wall);

		}
		if(collisionY){
			setY(oldY);
			velocity.y = 0;
		}
	}
}