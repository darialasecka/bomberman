package com.gdx.bomberman.screens;

import com.badlogic.gdx.Game;
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
import com.gdx.bomberman.sprites.Blast;
import com.gdx.bomberman.sprites.Bomb;
import com.gdx.bomberman.sprites.Bomber;
import com.gdx.bomberman.sprites.Box;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayScreen implements Screen {
	private final float UPDATE_TIME = 1/30f;
	float timer = 0;
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;
	Bomberman bomberman;
	SpriteBatch batch;
	Game game;

	/** Creates PlayScreen, it takes Bomberman and Game as parameters.
	 * @param  bomberman allows to get ad set bomberman variables
	 * @param game allows to change screen in case of loosing or winning game*/
	public PlayScreen(Bomberman bomberman, Game game){
		batch = new SpriteBatch();
		this.bomberman = bomberman;
		this.game = game;

	}

	/**Shows map on which players will be playing*/
	@Override
	public void show() {
		map = new TmxMapLoader().load("maps/map3c.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/2f);//1/1.98f
		camera = new OrthographicCamera();
		camera.position.set(272,210,0); //275,210
	}

	/** Handles player input allowing him to move and place bombs, it takes dt as parameter. Character speed is set to 130.
	 * @param dt is a deltatime which multiplayed by speed allows player to freely move by certain speed */
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
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){ // w bombermanie można stawiac bomby podczas chodzenia
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

	/**If player position wasn't updated by UPDATE_TIME, send notifibation to server about needing to update. UPDATE_TIME is set to 1/30f
	 * @param dt is delta time that allows to check when was tke last time player position was updated*/
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
	/**Updates player about bomb exploding. If bomb should explode, it sends proper informaion to server abou it.
	 * @param bomb information about bomb that is checked for explosion*/
	public void updateExplosion(Bomb bomb){
		long currTime = System.currentTimeMillis();
		if((currTime - bomb.start) / 1000 > bomb.EXPLOSION_TIME){
			try{
				synchronized (bomberman.out){
					bomberman.out.writeUTF("explosion " +  bomb.x + " " +  bomb.y + " " + bomb.power + " " + bomb.bombNumber + " " + System.currentTimeMillis());
				}
				//bomberman.bombs.remove(bomb.bombNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			bomb.draw(batch);
		}
	}
	/**Updates player about blast disappearing. If blas should disappear, it sends proper information to server about it.
	 * @param blast information about blast that about to diappear*/
	public void updateBlast(Blast blast){
		long currTime = System.currentTimeMillis();
		if((currTime - blast.start) / 1000 > blast.DURATION_TIME){
			try{
				synchronized (bomberman.out){
					bomberman.out.writeUTF("endBlast " + blast.bombNumber);
				}
				//bomberman.bombs.remove(bomb.bombNumber);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			blast.draw(batch);
		}
	}
	/**Checks if player is dead or player won the game and changes Screen accordingly.*/
	public void checkIsDeadOrWin(){
		if(bomberman.is_dead){
			GameOver gameOver = new GameOver(game);
			game.setScreen(gameOver);
		}
		if(bomberman.win){
			Win win = new Win(game);
			game.setScreen(win);
		}
	}
	/**Renders map, all players and objects, handles input and sends information to server if needed*/
	@Override
	public void render (float dt) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		checkIsDeadOrWin();

		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());

		renderer.setView(camera);
		renderer.render();

		batch.begin();
		try{
			for(HashMap.Entry<String, Box> entry: bomberman.boxes.entrySet()){
				entry.getValue().draw(batch);
			}
		} catch(Exception e) {}

		try{
			for(HashMap.Entry<String, Blast> entry: bomberman.blasts.entrySet()){
				updateBlast(entry.getValue());
			}
		} catch(Exception e) {}

		if(bomberman.player != null){
			bomberman.player.draw(batch);
			//bomberman.player.update(Gdx.graphics.getDeltaTime());
		}

		for(HashMap.Entry<String, Bomber> entry: bomberman.otherPlayers.entrySet()){
			entry.getValue().draw(batch);
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
