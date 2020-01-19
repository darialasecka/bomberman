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
