package com.gdx.bomberman;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;

import com.gdx.bomberman.screens.GameOver;
import com.gdx.bomberman.screens.Lobby;
import com.gdx.bomberman.sprites.*;
import com.gdx.bomberman.screens.PlayScreen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bomberman extends Game {
	public Game game;
	public final int port = 8080;

	public Lobby lobby;
	public PlayScreen screen;

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
	public Sprite blastH1;
	public Sprite blastV1;
	public Sprite bombUp;
	public Sprite powerUp;
	public HashMap<String, Bomber> otherPlayers;
	public DataOutputStream out;
	public DataInputStream in;
	public float x;
	public float y;
	public int direction; // 0-left, 1-right, 2-up, 3-down
	public HashMap<String, Bomb> bombs;
	public int currBombCounter = 0;
	public int MAX_BOMBS = 1;
	public int BOMB_POWER = 1;

	public HashMap<String, Box> boxes;
	public HashMap<String, Blast> blasts;
	public HashMap<String, PowerUp> powerUps;

	//to lobby
	public int room;
	public boolean ready = false;
	public List<String> chat = new ArrayList<>();
	public boolean start = false;

	//to dead / win xd
	public boolean is_dead = false;
	public boolean win = false;


	/** Creates Bomberman and initializes all needed arguments, also setSceen to Lobby */
	@Override
	public void create () {
		game = this;
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
		blasts = new HashMap<>();
		powerUps = new HashMap<>();

		bombSprite = new Sprite(new Texture("items/bomb.png"));
		boxSprite = new Sprite(new Texture("items/box.png"));
		boxSprite.setSize(boxSprite.getWidth() * 0.5f, boxSprite.getHeight() * 0.5f);

		blastC1 = new Sprite(new Texture("items/blastCenter1.png")); //pożniej to jakoś na animację zmnienimy
		blastH1 = new Sprite(new Texture("items/blastHorizontal1.png")); //pożniej to jakoś na animację zmnienimy
		blastV1 = new Sprite(new Texture("items/blastVertical1.png")); //pożniej to jakoś na animację zmnienimy
		float blastScale = 2.25f;
		blastC1.setSize(blastC1.getWidth() * blastScale, blastC1.getHeight() * blastScale);
		blastH1.setSize(blastH1.getWidth() * blastScale, blastH1.getHeight() * blastScale);
		blastV1.setSize(blastV1.getWidth() * blastScale, blastV1.getHeight() * blastScale);

		bombUp = new Sprite(new Texture("items/bombPowerUp.png"));
		powerUp = new Sprite(new Texture("items/powerPowerUp.png"));
		float upScale = 1.5f;
		bombUp.setSize(bombUp.getWidth() * upScale, bombUp.getHeight() * upScale);
		powerUp.setSize(powerUp.getWidth() * upScale, powerUp.getHeight() * upScale);

		screen = new PlayScreen(this, game);
		lobby = new Lobby(this, game);
		setScreen(lobby); //lobby

		connectSocket();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}

	/** Initializes connection beetween Bomberman and Server*/
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

	/** Connects Bomberman to Server, takes DataInputStream, DataOutputStream and Bomberman as an arguments.
	 * @param in allows Bomberman to recieve messages from server
	 * @param out allows Bomberman to send messages to server
	 * @param bomberman used to get and set values in Bomberman*/
	public ServerConnection(DataInputStream in, DataOutputStream out, Bomberman bomberman) {
		this.in = in;
		this.out = out;
		this.bomberman = bomberman;
	}

	/** Recieves and send messages beetween Bomberman and Server.*/
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
						/*Bomber enemy = new Bomber(bomberman.enemyBomber, 0, Boolean.parseBoolean(ready));
						bomberman.otherPlayers.put(playerId, enemy);*/
						try{
							bomberman.otherPlayers.get(playerId).setPosition(Float.parseFloat(x), Float.parseFloat(y));
						} catch (Exception e) {}
					}

				} else if(msg.startsWith("players")){
					String playerId = msg.split(" ")[1];
					String ready = msg.split(" ")[2];

					if(!bomberman.id.equals(playerId) && bomberman.otherPlayers.get(playerId) == null){
						Bomber enemy = new Bomber(bomberman.enemyBomber, 0, Boolean.parseBoolean(ready));
						bomberman.otherPlayers.put(playerId, enemy);
					} else if(!bomberman.id.equals(playerId) && bomberman.otherPlayers.get(playerId) != null){
						bomberman.otherPlayers.get(playerId).ready = Boolean.parseBoolean(ready);
					} else {
						bomberman.ready = Boolean.parseBoolean(ready);
					}

				} else if (msg.startsWith("created")) {
					bomberman.id = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					String room = msg.split(" ")[4];
					bomberman.player = new Bomber(bomberman.playerBomber, 0, false);
					bomberman.player.setPosition(Float.parseFloat(x), Float.parseFloat(y));
					bomberman.x = Float.parseFloat(x);
					bomberman.y = Float.parseFloat(y);
					bomberman.room = Integer.parseInt(room);

				} else if(msg.startsWith("box")){
					String number = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					Box box = new Box(bomberman.boxSprite, Float.parseFloat(x), Float.parseFloat(y));
					bomberman.boxes.put(number, box);
					bomberman.boxes.get(number).setPosition(Float.parseFloat(x), Float.parseFloat(y));

				} else if (msg.startsWith("remove")) {
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
					//System.out.println("Wybuch!!!");
					String number = msg.split(" ")[1];
					try{
						if(bomberman.bombs.get(number).bomberId == Integer.parseInt(bomberman.id)) bomberman.currBombCounter--;
						bomberman.bombs.remove(number);
					} catch (Exception e) {}

				} else if(msg.startsWith("blast")) {
					String number = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					String type = msg.split(" ")[4];
					String start = msg.split(" ")[5];
					String bombNumber = msg.split(" ")[6];
					float fx = Float.parseFloat(x);
					float fy = Float.parseFloat(y);
					Blast blast;
					//System.out.println(msg);
					if(type.equals("h")) blast = new Blast(bomberman.blastH1, fx, fy, Long.parseLong(start), Integer.parseInt(bombNumber));
					else if(type.equals("v")) blast = new Blast(bomberman.blastV1, fx, fy, Long.parseLong(start), Integer.parseInt(bombNumber));
					else blast = new Blast(bomberman.blastC1, fx, fy, Long.parseLong(start), Integer.parseInt(bombNumber));
					bomberman.blasts.put(number, blast);
					bomberman.blasts.get(number).setPosition(fx, fy);

				} else if (msg.startsWith("clearBoxes")){//właściwie to equals by starczyło ale olać xd
					bomberman.boxes.clear();

				} else if(msg.startsWith("endBlast")){
					String bombNumber = msg.split(" ")[1];
					try{
						for(HashMap.Entry<String, Blast> entry: bomberman.blasts.entrySet()){
							int currBombNumber = entry.getValue().bombNumber;
							if(currBombNumber == Integer.parseInt(bombNumber)){
								bomberman.blasts.remove(entry.getKey());
							}
						}
					} catch (Exception e) {}

				} else if (msg.startsWith("chatNew")){
					bomberman.chat.add(msg.split(" ",2)[1]);

				} else if(msg.startsWith("chat")){
					String id = msg.split(" ",3)[1];
					String message = msg.split(" ", 3)[2];
					String fullMessage = "Gracz " + id + ": " + message;
					//System.out.println(message);
					bomberman.chat.add(fullMessage);

				} else if(msg.startsWith("start")){
					//System.out.println("START");
					bomberman.start = true;

				} else if(msg.startsWith("DEAD")){
					String id = msg.split(" ")[1];
					//System.out.println("umarlem");
					if(bomberman.id.equals(id)){
						bomberman.is_dead = true;
					} else{
						bomberman.otherPlayers.remove(id);
					}

				} else if(msg.startsWith("Winner")) {
					bomberman.win = true;

				} else if(msg.startsWith("powerUp")){
					String number = msg.split(" ")[1];
					String x = msg.split(" ")[2];
					String y = msg.split(" ")[3];
					String type = msg.split(" ")[4];
					float fx = Float.parseFloat(x);
					float fy = Float.parseFloat(y);

					PowerUp powerUp;
					if(type.equals("b")) powerUp = new PowerUp(bomberman.bombUp, fx, fy);
					else powerUp = new PowerUp(bomberman.powerUp, fx, fy);
					bomberman.powerUps.put(number, powerUp);
					bomberman.powerUps.get(number).setPosition(fx, fy);

				} else if (msg.startsWith("clearPowerUp")){//właściwie to equals by starczyło ale olać xd
					bomberman.powerUps.clear();

				} else if (msg.startsWith("picked")){//właściwie to equals by starczyło ale olać xd
					String id = msg.split(" ")[1];
					String number = msg.split(" ")[2];
					String type = msg.split(" ")[3];

					if(id.equals(bomberman.id)){
						if(type.equals("b")) bomberman.MAX_BOMBS ++;
						else bomberman.BOMB_POWER++;
					}
					bomberman.powerUps.remove(number);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Connection has been lost.");
			System.exit(1);
		}
	}
}
