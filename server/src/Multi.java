public class Multi extends Thread {
	public Player player;
	/** It allows to communicate between server and client
	 * @param player player to which sends and from which recieve messages */
	Multi(Player player) {
		this.player = player;
	}

	/** Handles all messages between Server and Client*/
	public void run()  {
		System.out.println("New player " + player.id + " (" + player.socket.getPort() + ") connected.");
		while (true) {
			try {
				String msg = "";
				synchronized (player.in){
					msg = player.in.readUTF();
				}
				if (msg.startsWith("connected")) {
					synchronized (player.out){
						player.out.writeUTF("created " + player.id + " " + player.x + " " + player.y + " " + player.room);
						Room room = Server.rooms.get(player.room);
						room.sendBoxesPositions();
						room.updatePlayers();
						room.chatNew(player);
					}
				}  else if (msg.startsWith("playerMoved")) {
					String x = msg.split(" ")[1];
					String y = msg.split(" ")[2];
					int direction = Integer.parseInt(msg.split(" ")[3]);

					Room room = Server.rooms.get(player.room);
					int posX = (int)(player.x / room.blockSize);// tak chemy zaokrąglić w górę
					int posY = (int)(player.y / room.blockSize);
					posY = room.map.size()-posY - 1;
					//System.out.println(posX + " " + posY);

					if(direction == 0){//lewo
						if(room.map.get(posY).get(posX) == "0") player.x = Float.parseFloat(x);
						else if(room.map.get(posY).get(posX).startsWith("3")){
							player.x = Float.parseFloat(x);
							posX = (int)(((player.x) / room.blockSize) + 0.25);
							if(room.map.get(posY).get(posX).startsWith("3")){
								room.broadcast("DEAD " + player.id);
								room.players_in_room.remove(player);
								room.curr_number_of_players--;
								if(room.curr_number_of_players == 1){
									room.broadcast("Winner");
								}
							}
						}


					} else if(direction == 1) {//prawo
						posX = (int)(((player.x) / room.blockSize) - 0.5);
						if(room.map.get(posY).get(posX+1) == "0") player.x = Float.parseFloat(x);
						else if (room.map.get(posY).get(posX+1).startsWith("3")){
							player.x = Float.parseFloat(x);
							posX = (int)(((player.x) / room.blockSize) + 0.25);
							if(room.map.get(posY).get(posX).startsWith("3")){
								room.broadcast("DEAD " + player.id);
								room.players_in_room.remove(player);
								room.curr_number_of_players--;
								if(room.curr_number_of_players == 1){
									room.broadcast("Winner");
								}
							}
						}
					} else if(direction == 2){//góra
						posY = (int)((player.y / room.blockSize) - 0.75);
						posY = room.map.size()-posY-2;
						if(room.map.get(posY).get(posX) == "0") player.y = Float.parseFloat(y);
						else if (room.map.get(posY).get(posX).startsWith("3")){
							player.y = Float.parseFloat(y);
							posY = (int)((player.y / room.blockSize) - 0.25);
							posY = room.map.size()-posY-2;
							if(room.map.get(posY).get(posX).startsWith("3")){
								room.broadcast("DEAD " + player.id);
								room.players_in_room.remove(player);
								room.curr_number_of_players--;
								if(room.curr_number_of_players == 1){
									room.broadcast("Winner");
								}
							}
						}
					} else if(direction == 3) {//dól
						if(room.map.get(posY).get(posX) == "0") player.y = Float.parseFloat(y);
						else if (room.map.get(posY).get(posX).startsWith("3")){
							player.y = Float.parseFloat(y);
							posY = (int)((player.y / room.blockSize) + 0.25);
							posY = room.map.size()-posY - 1;
							if(room.map.get(posY).get(posX).startsWith("3")){
								room.broadcast("DEAD " + player.id);
								room.players_in_room.remove(player);
								room.curr_number_of_players--;
								if(room.curr_number_of_players == 1){
									room.broadcast("Winner");
								}
							}
						}
					}
					//player.y = Float.parseFloat(y);
					for (Player other : room.players_in_room) {
						synchronized (player.out){
							player.out.writeUTF("update " + other.id + " " + other.x + " " + other.y);
						}
					}
				} else if (msg.startsWith("bomb")) {
					Room room = Server.rooms.get(player.room);
					room.broadcast(msg + " " + Server.bombNumber++);
				} else if (msg.startsWith("explosion")) {
					Room room = Server.rooms.get(player.room);
					String bombNumber = msg.split(" ")[4];
					room.broadcast("explosion " + bombNumber);

					String x = msg.split(" ")[1];
					String y = msg.split(" ")[2];
					String power = msg.split(" ")[3];

					// tu ogarniamy pozycje bomby i liczymy gdzie powinien być wybuch
					int posX = (int)(Float.parseFloat(x) / room.blockSize);
					int posY = (int)(Float.parseFloat(y) / room.blockSize);
					posY = room.map.size()-posY - 1;

					int count = 0;
					//środek
					room.map.get(posY).set(posX,"3"+bombNumber+"-");

					//w lewo
					while(room.map.get(posY).get(posX-count-1) != "1" && count != Integer.parseInt(power)){
						room.map.get(posY).set(posX-count-1, "3"+bombNumber+"0"); //lewo oznacza inaczej
						count++;
					}
					//gora
					count = 0;
					while(room.map.get(posY-count-1).get(posX)!= "1" && count != Integer.parseInt(power)){
						room.map.get(posY-count-1).set(posX, "3"+bombNumber+"2");
						count++;
					}
					//prawo
					count = 0;
					while(room.map.get(posY).get(posX+count+1) != "1" && count != Integer.parseInt(power)){
						room.map.get(posY).set(posX+count+1, "3"+bombNumber+"1");
						count++;
					}
					//dol
					count = 0;
					while(room.map.get(posY+count+1).get(posX) != "1" && count != Integer.parseInt(power)){
						room.map.get(posY+count+1).set(posX, "3"+bombNumber+"3");
						count++;
					}
					//wybuchy dobrze oznacza
					//printMap();
					for (Player other : room.players_in_room) {
						synchronized (player.out){
							player.out.writeUTF("update " + other.id + " " + other.x + " " + other.y);
						}
					}
					room.sendBlastPositions(msg.split(" ")[5], bombNumber);
					room.broadcast("clearBoxes");
					room.sendBoxesPositions();

				} else if(msg.startsWith("endBlast")){
					Room room = Server.rooms.get(player.room);
					room.broadcast(msg);

					String bombNumber = msg.split(" ")[1];

					for(int i = 1; i < room.map.size()-1; i++){
						for(int j = 1; j < room.walls.size()-1; j++) {
							//sprawdzić czy bombNumber jest ten sam i usunąć
							String position = room.map.get(i).get(j);
							if(position.length() >= 3){
								String mapBombNumber = position.substring(1, position.length()-1);
								//System.out.println(mapBombNumber);
								if(mapBombNumber.equals(bombNumber)) room.map.get(i).set(j, "0");
							}
						}
					}

					//printMap();
				} else if(msg.startsWith("chat")){
					Room room = Server.rooms.get(player.room);
					String id = msg.split(" ",3)[1];
					String message = msg.split(" ", 3)[2];
					String fullMessage = "Gracz " + id + ": " + message;
					room.chat.add(fullMessage);
					room.broadcast(msg);
				} else if(msg.startsWith("ready")){
					String id = msg.split(" ")[1];
					Room room = Server.rooms.get(player.room);
					int readyCounter = 0;
					for(Player player: room.players_in_room){
						if(id.equals(player.id)) player.ready = true;
						if(player.ready) readyCounter++;
					}
					room.updatePlayers();
					if(readyCounter == room.curr_number_of_players && room.curr_number_of_players >= room.MIN_CAPACITY_REQUIRED)
						room.startGame();
				}

			} catch (Exception e) {
				Room room = Server.rooms.get(player.room);
				room.players_in_room.remove(player);
				room.broadcast("remove " + player.id);
				room.is_full = false;
				Server.players.remove(player);
				System.out.println("Player " + player.id + " (" + player.socket.getPort() + ") disconnected.");
				break;
			}
		}
	}

}