package com.gdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

import com.gdx.bomberman.sprites.Blast;
import com.gdx.bomberman.sprites.Bomb;
import com.gdx.bomberman.sprites.Bomber;
import com.gdx.bomberman.screens.PlayScreen;
import com.gdx.bomberman.sprites.Box;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Bomberman extends Game {
	public final int port = 8080;
	SpriteBatch batch;
	Socket socket;
	public String id;
	public Bomber player;
	TextureAtlas textureAtlas;
	public TextureRegion textureRegionUp;
	public TextureRegion textureRegionDown;
	public TextureRegion textureRegionLeft;
	public TextureRegion textureRegionRight;
	public Sprite playerBomber;
	public Sprite enemyBomber;
	public Sprite bombSprite;
	public Sprite boxSprite;
	public Sprite blastC1;
	public HashMap<String, Bomber> otherPlayers;
	public DataOutputStream out;
	public DataInputStream in;
	public float x;
	public float y;
	PlayScreen screen;
	public int direction; // 0-left, 1-right, 2-up, 3-down
	public HashMap<String, Bomb> bombs;
	public int currBombCounter = 0;
	public int MAX_BOMBS = 2;
	public int BOMB_POWER = 1;


	public HashMap<String, Box> boxes;



	@Override
	public void create () {
		batch = new SpriteBatch();

		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheet/Sprites.atlas"));
		textureRegionUp = textureAtlas.findRegion("sprite002");
		textureRegionDown = textureAtlas.findRegion("sprite008");
		textureRegionLeft = textureAtlas.findRegion("sprite004");
		textureRegionRight = textureAtlas.findRegion("sprite010");

		playerBomber = new Sprite(textureRegionUp);
		enemyBomber = new Sprite(new Texture("Sprites/enemy2.png"));
		float scale = 1;//1.6f;
		playerBomber.setSize(playerBomber.getWidth() * scale, playerBomber.getHeight() * scale);
		enemyBomber.setSize(enemyBomber.getWidth() * scale, enemyBomber.getHeight() * scale);

		otherPlayers = new HashMap<>();
		bombs = new HashMap<>();
		boxes = new HashMap<>();

		bombSprite = new Sprite(new Texture("items/bomb.png"));
		boxSprite = new Sprite(new Texture("items/box.png"));
		boxSprite.setSize(boxSprite.getWidth() * 0.5f, boxSprite.getHeight() * 0.5f);
		blastC1 = new Sprite(new Texture("items/blastCenter1.png")); //pożniej to jakoś na animację zmnienimy

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
			synchronized (out){
				out.writeUTF("connected");
			}
			while (true) {
				String msg = "";
				synchronized (in){
					msg = in.readUTF();
				}
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
						Bomber enemy = new Bomber(bomberman.enemyBomber, 0);
						bomberman.otherPlayers.put(playerId, enemy);
						bomberman.otherPlayers.get(playerId).setPosition(Float.parseFloat(x), Float.parseFloat(y));
					}
				} else if (msg.startsWith("created")) {
					bomberman.id = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					bomberman.player = new Bomber(bomberman.playerBomber, 0);
					bomberman.player.setPosition(Float.parseFloat(x), Float.parseFloat(y));
					bomberman.x = Float.parseFloat(x);
					bomberman.y = Float.parseFloat(y);
				} else if(msg.startsWith("box")){
					String number = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					Box box = new Box(bomberman.boxSprite, Float.parseFloat(x), Float.parseFloat(y));
					bomberman.boxes.put(number, box);
					bomberman.boxes.get(number).setPosition(Float.parseFloat(x), Float.parseFloat(y));

				}

				else if (msg.startsWith("remove")) {
					String id = msg.split(" ")[1];
					bomberman.otherPlayers.remove(id);
				} else if (msg.startsWith("bomb")) {
					String x = msg.split(" ")[1];
					String y = msg.split(" ")[2];
					String power = msg.split(" ")[3];
					String start = msg.split(" ")[4];
					String id = msg.split(" ")[5];
					String number = msg.split(" ")[6];
					Bomb bomb = new Bomb(bomberman.bombSprite, Float.parseFloat(x), Float.parseFloat(y), Integer.parseInt(power), Integer.parseInt(id), Integer.parseInt(number), Long.parseLong(start));
					bomberman.bombs.put(number, bomb); //power na razie z automatu 1
					bomberman.bombs.get(number).setPosition(Float.parseFloat(x), Float.parseFloat(y));
					//String power = msg.split(" ")[3];
				} else if(msg.startsWith("explosion")) {
					System.out.println("Wybuch!!!");


					String x = msg.split(" ")[1];
					String y = msg.split(" ")[2];
					String power = msg.split(" ")[3];
					String number = msg.split(" ")[4];
					if(bomberman.bombs.get(number).bomberId == Integer.parseInt(bomberman.id)) bomberman.currBombCounter--;
					bomberman.bombs.remove(number);


					//Blast blast = new Blast(bomberman.blastC1, Float.parseFloat(x), Float.parseFloat(y), Integer.parseInt(power));
					//w tablicy dopisać wybuchy, sprite powinien być centrun, vertibal lub horizontal, balsty powinny być worzone po kolei


					//bomberman.blasts.put(number, blast); //power na razie z automatu 1
					//bomberman.blasts.get(number).setPosition(Float.parseFloat(x), Float.parseFloat(y));
					//String power = msg.split(" ")[3];
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