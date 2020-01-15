package com.gdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
	public TextureRegion textureRegionUp;
	public TextureRegion textureRegionDown;
	public TextureRegion textureRegionLeft;
	public TextureRegion textureRegionRight;
	public Sprite playerBomber;
	public Sprite enemyBomber;
	public HashMap<String, Bomber> otherPlayers;
	public DataOutputStream out;
	public DataInputStream in;
	public float x;
	public float y;
	PlayScreen screen;
	public TiledMap map;
	public int direction; // 0-left, 1-right, 2-up, 3-down


	@Override
	public void create () {
		batch = new SpriteBatch();

		map = new TmxMapLoader().load("map/map.tmx");

		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheet/Sprites.atlas"));
		textureRegionUp = textureAtlas.findRegion("sprite002");
		textureRegionDown = textureAtlas.findRegion("sprite008");
		textureRegionLeft = textureAtlas.findRegion("sprite004");
		textureRegionRight = textureAtlas.findRegion("sprite010");

		playerBomber = new Sprite(textureRegionUp);
		enemyBomber = new Sprite(textureRegionUp);
		float scale = 1;//1.6f;
		playerBomber.setSize(playerBomber.getWidth() * scale, playerBomber.getHeight() * scale);
		enemyBomber.setSize(enemyBomber.getWidth() * scale, enemyBomber.getHeight() * scale);
		otherPlayers = new HashMap<>();

		screen = new PlayScreen(this);
		setScreen(screen);

		connectSocket();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	public void connectSocket(){
		try{
			InetAddress address = InetAddress.getLocalHost();
			socket = new Socket(address, port);
			System.out.println("You are connected by: " + socket.getLocalPort() + ".");
			//player = new Bomber(playerBomber, (TiledMapTileLayer) map.getLayers().get(0));
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
						Bomber hero = new Bomber(bomberman.enemyBomber, (TiledMapTileLayer) bomberman.map.getLayers().get(0), 0);
						bomberman.otherPlayers.put(playerId, hero);
						bomberman.otherPlayers.get(playerId).setPosition(Float.parseFloat(x), Float.parseFloat(y));
					}
				} else if (msg.startsWith("created")) {
					bomberman.id = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					bomberman.player = new Bomber(bomberman.playerBomber, (TiledMapTileLayer) bomberman.map.getLayers().get(0), 0);
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