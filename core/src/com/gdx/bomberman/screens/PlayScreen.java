package com.gdx.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gdx.bomberman.Bomberman;
import com.gdx.bomberman.sprites.Bomb;
import com.gdx.bomberman.sprites.Bomber;

import java.util.HashMap;

public class PlayScreen implements Screen {
	private final float UPDATE_TIME = 1/30f;
	float timer = 0;
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;
	Bomberman bomberman;
	SpriteBatch batch;
	Bomb bomb;


	public PlayScreen(Bomberman bomberman){
		batch = new SpriteBatch();
		this.bomberman = bomberman;

	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("maps/map3c.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/2f);//1/1.98f
		camera = new OrthographicCamera();
		camera.position.set(275,210,0);
	}

	public void handleInput(float dt){
		int speed = 130;
		if(bomberman.player != null){
			//batch.begin();
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				bomberman.x += (-speed* dt);
				bomberman.direction = 0;
				bomberman.player.setRegion(bomberman.textureRegionLeft);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				bomberman.direction = 1;
				bomberman.x += (speed * dt);
				bomberman.player.setRegion(bomberman.textureRegionRight);
			} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				bomberman.y += (speed * dt);
				bomberman.direction = 2;
				bomberman.player.setRegion(bomberman.textureRegionUp);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				bomberman.y += (-speed * dt);
				bomberman.direction = 3;
				bomberman.player.setRegion(bomberman.textureRegionDown);
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
				if(bomberman.currBombCounter < bomberman.MAX_BOMBS){
					try{
						synchronized (bomberman.out){
							bomberman.currBombCounter ++;
							bomberman.out.writeUTF("bomb " +  bomberman.x + " " +  bomberman.y + " " + bomberman.BOMB_POWER + " " + System.currentTimeMillis() + " " + bomberman.id);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void updateServer(float dt){
		timer += dt;
		if(timer >= UPDATE_TIME && bomberman.player != null){
			try{
				synchronized (bomberman.out){
					bomberman.out.writeUTF("playerMoved " +  bomberman.x + " " +  bomberman.y + " " + bomberman.direction);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateExplosion(Bomb bomb){
		long currTime = System.currentTimeMillis();
		if((currTime - bomb.start) / 1000 > bomb.EXPLOSION_TIME){
			try{
				synchronized (bomberman.out){
					bomberman.out.writeUTF("explosion " +  bomb.x + " " +  bomb.y + " " + bomb.power + " " + bomb.bombNumber);
				}
				//bomberman.bombs.remove(bomb.bombNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			bomb.draw(batch);
		}
	}

	@Override
	public void render (float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());

		renderer.setView(camera);
		renderer.render();

		batch.begin();
		if(bomberman.player != null){
			bomberman.player.draw(batch);
			//bomberman.player.update(Gdx.graphics.getDeltaTime());
		}
		for(HashMap.Entry<String, Bomber> entry: bomberman.otherPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
		if(bomb != null){
			bomb.draw(batch);
		}
		try{
			for(HashMap.Entry<String, Bomb> entry: bomberman.bombs.entrySet()){
				updateExplosion(entry.getValue());
			}
		} catch(Exception e) {}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}
}
