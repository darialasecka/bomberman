package com.gdx.bomberman.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gdx.bomberman.Bomberman;
import com.gdx.bomberman.sprites.Bomber;

import java.io.IOException;
import java.util.HashMap;

public class Lobby implements Screen {
	Bomberman bomberman;
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private List<String> players_list;
	int room;
	Label roomLabel;
	Game game;

	/** Creates PlayScreen, it takes Bomberman and Game as parameters.
	 * @param  bomberman allows to get ad set bomberman variables
	 * @param game allows to change screen in case of starting game*/
	public Lobby(Bomberman bomberman, Game game){
		this.bomberman = bomberman;
		this.game = game;
	}

	/** Prepares everything to show player when in lobby and handles chat inputs*/
	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
		stage = new Stage();


		Table root = new Table();
		root.setFillParent(true);

		Table playerInfoTable = buildPlayerInfoTable();
		Table chatTable = buildChatTable();


		final TextButton readyButton = new TextButton("Gotowy?", skin, "default");

		readyButton.setWidth(200f);
		readyButton.setHeight(20f);

		readyButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				try {
					synchronized (bomberman.out) {
						bomberman.out.writeUTF("ready " + bomberman.id);
						readyButton.setText("Gotowy!");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		roomLabel = new Label("Pokoj ", skin, "default");
		root.add(roomLabel).colspan(5);
		root.row();
		root.add(new Label("Gracze", skin, "default")).colspan(1);
		root.add(new Label(" ", skin, "default"));
		root.add(new Label("Chat", skin, "default")).colspan(1);
		root.row();
		root.add(playerInfoTable);
		root.add(new Label("    ", skin, "default"));
		root.add(chatTable);
		root.row();
		root.add(readyButton);

		//root.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);
		stage.addActor(root);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	/** Renders everything that player should see in lobby.*/
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		checkStart();

		batch.begin();
		try{
			updatePlayersList();
		} catch (Exception e) {}
		try{
			room = bomberman.room;
			String roomText= "Pokoj " + room;
			roomLabel.setText(roomText);
		} catch (Exception e) {}

		try{
			String fullChat = "";
			for(String message: bomberman.chat){
				fullChat += message + "\n";
			}
			chatLabel.setText(fullChat);
		} catch (Exception e) {}
		stage.draw();
		stage.act();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
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

	/** Prepares table about players that are in the same room and they status about being ready to play a game*/
	private Table buildPlayerInfoTable(){
		Table table = new Table();
		players_list = new List<String>(skin, "default");

		Label titles = new Label("   Gracz                      Status", skin,"default");
		titles.setColor(Color.WHITE);

		table.add(new ScrollPane(titles, skin)).align(Align.left).width(220).height(35);
		table.row();
		table.add(new ScrollPane(players_list, skin)).width(220).height(100);

		return table;
	}

	/** Updates player list if new player joined room, or chenged status*/
	private void updatePlayersList(){
		Array<String> temp = new Array<String>();

		String playerLabel = " Gracz ";

		int length = 24 - (playerLabel.length() + bomberman.id.length());
		String item =  playerLabel + bomberman.id;
		for(int i = 0; i < length; i++){
			item += " ";
		}
		item += (bomberman.ready ? "   Gotowy": "Nie gotowy");

		temp.add(item);

		for(HashMap.Entry<String, Bomber> player: bomberman.otherPlayers.entrySet()) {
			length = 24 - (playerLabel.length() + player.getKey().length());
			item = playerLabel + player.getKey();
			for(int i = 0; i < length; i++){
				item += " ";
			}
			item += (player.getValue().ready ? "   Gotowy": "Nie gotowy");

			temp.add(item);
		}

		players_list.setItems(temp);
	}

	private Label chatLabel;
	private ScrollPane chat_scroll;
	private TextField message_field;

	/** Prepares chat table*/
	private Table buildChatTable(){
		Table table = new Table();

		chatLabel = new Label("", skin);
		chatLabel.setWrap(true);
		chatLabel.setAlignment(Align.topLeft);

		chat_scroll = new ScrollPane(chatLabel, skin);
		chat_scroll.setFadeScrollBars(false);
		//chat_scroll.pack();

		message_field = new TextField("", skin);
		message_field.setMessageText("Wiadomosc");
		message_field.setColor(0.2f, 0.4f, 0.3f, 1f);
		message_field.getStyle().fontColor = Color.WHITE;

		message_field.addListener(new InputListener(){
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if(keycode == Input.Keys.ENTER && !message_field.getText().isEmpty()){
					try {
						synchronized (bomberman.out) {
							bomberman.out.writeUTF("chat " + bomberman.id + " " + message_field.getText());
						}
						message_field.setText("");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				return true;
			}
		});

		table.add(chat_scroll).width(220).height(220);
		table.row();
		table.add(message_field).width(220).height(20);

		return table;
	}

	/** Checks if players are ready to start game, and changes screen if needed.*/
	public void checkStart(){
		if(bomberman.start){
			PlayScreen playScreen = new PlayScreen(bomberman, game);
			game.setScreen(playScreen);
		}
	}






}