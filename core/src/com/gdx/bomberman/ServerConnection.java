package com.gdx.bomberman;

import com.gdx.bomberman.sprites.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

public class ServerConnection extends Thread {

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