package com.gdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

import com.gdx.bomberman.sprites.Bomber;
import com.gdx.bomberman.screens.PlayScreen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Bomberman extends Game {
	public final int port = 8080;
	private final float UPDATE_TIME = 1/60f;
	float timer;
	SpriteBatch batch;
	Socket socket;
	String id;
	public Bomber player;
	TextureAtlas textureAtlas;
	TextureRegion textureRegion;
	Sprite playerBomber;
	Sprite enemyBomber;
	public HashMap<String, Bomber> otherPlayers;
	public DataOutputStream out;
	public DataInputStream in;
	public float x;
	public float y;
	PlayScreen screen;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheet/Sprites.atlas"));
		textureRegion = textureAtlas.findRegion("sprite002");

		playerBomber = new Sprite(textureRegion);
		enemyBomber = new Sprite(textureRegion);
		float scale = 1.6f;
		playerBomber.setSize(playerBomber.getWidth() * scale, playerBomber.getHeight() * scale);
		enemyBomber.setSize(enemyBomber.getWidth() * scale, enemyBomber.getHeight() * scale);
		otherPlayers = new HashMap<>();

		screen = new PlayScreen(this);
		setScreen(screen);

		connectSocket();
	}

	/*public void handleInput(float dt){
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
		if(timer >= UPDATE_TIME && player != null){
			try{
				out.writeUTF("playerMoved " +  player.getX() + " " +  player.getY());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 0, 1);
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
	}*/
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void connectSocket(){
		try{
			InetAddress address = InetAddress.getLocalHost();
			socket = new Socket(address, port);
			System.out.println("You are connected by: " + socket.getLocalPort() + ".");
			player = new Bomber(playerBomber);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			new ServerConnection(in, out, this).start();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}

class ServerConnection extends Thread {

	private DataInputStream in;
	private DataOutputStream out;
	private Bomberman bomberman;


	public ServerConnection(DataInputStream in, DataOutputStream out, Bomberman bomberman) {
		this.in = in;
		this.out = out;
		this.bomberman = bomberman;
	}

	public void run() {
		try {
			out.writeUTF("connected");
			while (true) {
				String msg = in.readUTF();
				if (msg.startsWith("update")) {
					String id = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					if (bomberman.otherPlayers.get(id) != null) {
						bomberman.otherPlayers.get(id).setPosition(Float.parseFloat(x), Float.parseFloat(y));
					} else if (bomberman.id != null && id.equals(bomberman.id) && bomberman.player != null) {
						bomberman.player.setPosition(Float.parseFloat(x), Float.parseFloat(y));
						bomberman.x = Float.parseFloat(x);
						bomberman.y = Float.parseFloat(y);
					} else if (bomberman.id != null && !id.equals(bomberman.id)){
						String playerId = id;
						Bomber hero = new Bomber(bomberman.enemyBomber);
						bomberman.otherPlayers.put(playerId, hero);
						bomberman.otherPlayers.get(playerId).setPosition(Float.parseFloat(x), Float.parseFloat(y));
					}
				} else if (msg.startsWith("created")) {
					bomberman.id = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					bomberman.player = new Bomber(bomberman.playerBomber);
					bomberman.player.setPosition(Float.parseFloat(x), Float.parseFloat(y));
					bomberman.x = Float.parseFloat(x);
					bomberman.y = Float.parseFloat(y);
				} else if (msg.startsWith("remove")) {
					String id = msg.split(" ")[1];
					bomberman.otherPlayers.remove(id);
				}
			}
		} catch (java.io.EOFException e) {
			System.out.println("Connection has been lost.");
			System.exit(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}