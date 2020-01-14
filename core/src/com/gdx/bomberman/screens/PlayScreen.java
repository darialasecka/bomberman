package com.gdx.bomberman.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.gdx.bomberman.Bomberman;
import com.gdx.bomberman.sprites.Bomber;

import java.util.HashMap;

public class PlayScreen implements Screen {
	private final float UPDATE_TIME = 1/60f;
	float timer = 0;
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;
	Bomberman bomberman;
	SpriteBatch batch;

	public PlayScreen(Bomberman bomberman){
		batch = new SpriteBatch();
		this.bomberman = bomberman;

	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/1.98f);
		camera = new OrthographicCamera();
		camera.position.set(275,210,0);
	}

	public void handleInput(float dt){
		if(bomberman.player != null){
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				bomberman.player.setPosition(bomberman.player.getX() + (-200 * dt), bomberman.player.getY());
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				bomberman.player.setPosition(bomberman.player.getX() + (+200 * dt), bomberman.player.getY());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				bomberman.player.setPosition(bomberman.player.getX(), bomberman.player.getY() + (+200 * dt));
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				bomberman.player.setPosition(bomberman.player.getX(), bomberman.player.getY()+ (-200 * dt));
			}

		}
	}

	public void updateServer(float dt){
		timer += dt;
		if(timer >= UPDATE_TIME && bomberman.player != null){
			try{
				bomberman.out.writeUTF("playerMoved " +  bomberman.player.getX() + " " +  bomberman.player.getY());
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		}
		for(HashMap.Entry<String, Bomber> entry: bomberman.otherPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
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
