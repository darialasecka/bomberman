package com.gdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.gdx.bomberman.sprites.Bomber;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Bomberman extends ApplicationAdapter {
	public final int port = 8080;
	private final float UPDATE_TIME = 1/60f;
	float timer;
	SpriteBatch batch;
	Socket socket;
	String id;
	Bomber player;
	Texture playerBomber;
	Texture enemyBomber;
	HashMap<String, Bomber> otherPlayers;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerBomber = new Texture("inky.png");
		enemyBomber = new Texture("blinky.png");
		otherPlayers = new HashMap<>();
		connectSocket();
	}

	public void handleInput(float dt){
		if(player != null){
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				player.setPosition(player.getX() + (-200 * dt), player.getY());
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				player.setPosition(player.getX() + (+200 * dt), player.getY());
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				player.setPosition(player.getX(), player.getY() + (+200 * dt));
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
				player.setPosition(player.getX(), player.getY()+ (-200 * dt));
			}

		}
	}

	/*public void updateServer(float dt){ //póżniej z tutoriala spisac znowu
		timer += dt;
		if(timer >= UPDATE_TIME && pl)
	}*/

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput(Gdx.graphics.getDeltaTime());
		//updateServer(Gdx.graphics.getDeltaTime());

		batch.begin();
		if(player != null){
			player.draw(batch);
		}
		for(HashMap.Entry<String, Bomber> entry: otherPlayers.entrySet()){
			entry.getValue().draw(batch);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerBomber.dispose();
		enemyBomber.dispose();
	}

	public void connectSocket(){
		try{
			InetAddress address = InetAddress.getLocalHost();
			socket = new Socket(address, port);
			System.out.println("You are connected by: " + socket.getLocalPort() + ".");
			player = new Bomber(playerBomber);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF("Test");
		} catch(Exception e) {
			System.out.println(e);
		}
	}

}
