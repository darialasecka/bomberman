package com.gdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.gdx.bomberman.sprites.Bomber;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Bomberman extends ApplicationAdapter {
	private final float UPDATE_TIME = 1/60f;
	float timer;
	SpriteBatch batch;
	private Socket socket;
	String id;
	Bomber player;
	Texture playerBomber;
	Texture otherBomber;
	HashMap<String, Bomber> otherPlayers;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		playerBomber = new Texture("inky.png");
		otherBomber = new Texture("blinky.png");
		otherPlayers = new HashMap<>();
		connectSocket();
		configSocketEvents();
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

	public void updateServer(float dt){
		timer += dt;
		if(timer >= UPDATE_TIME && player != null && player.hasMoved()){
			JSONObject data = new JSONObject();
			try{
				data.put("x", player.getX());
				data.put("y", player.getY());
				socket.emit("playerMoved", data);

			} catch (JSONException e) {
				Gdx.app.log("SocketIO", "Error sending update data");
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput(Gdx.graphics.getDeltaTime());
		updateServer(Gdx.graphics.getDeltaTime());

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
		otherBomber.dispose();
	}

	public void connectSocket(){
		try{
			socket = IO.socket("http://localhost:8080");
			socket.connect();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				player = new Bomber(playerBomber);
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + id);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connected: " + playerId);
					otherPlayers.put(playerId, new Bomber(otherBomber));
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New PlayerID");
				}
			}
		}).on("playerDisconnected", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					otherPlayers.remove(playerId);
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
				}
			}
		}).on("getPlayers", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONArray objects = (JSONArray) args[0];
				try {
					for(int i = 0; i < objects.length(); i++){
						Bomber enemyPlayer = new Bomber((otherBomber));
						Vector2 position = new Vector2();
						position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
						enemyPlayer.setPosition(position.x, position.y);

						otherPlayers.put(objects.getJSONObject(i).getString("id"), enemyPlayer);
					}
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting other players");
				}
			}
		}).on("playerMoved", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String playerId = data.getString("id");
					Double x = data.getDouble("x");
					Double y = data.getDouble("y");
					if(otherPlayers.get(playerId) != null){
						otherPlayers.get(playerId).setPosition(x.floatValue(), y.floatValue());
					}
				} catch (JSONException e) {
					Gdx.app.log("SocketIO", "Error getting Player coordinates");
				}
			}
		});
	}

}
