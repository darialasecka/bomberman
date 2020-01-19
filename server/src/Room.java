import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Room {
	public int number;
	public List<Player> players_in_room = new ArrayList<>();
	public boolean active_game;
	public boolean is_full;
	public int curr_number_of_players = 0;
	public final int MAX_CAPACITY = 4;
	public final int MIN_CAPACITY_REQUIRED = 2;

	float blockSize = 32;

	public ArrayList<ArrayList<String>> map = new ArrayList<>();

	ArrayList walls;
	ArrayList mixed1;//5
	ArrayList mixed2;
	ArrayList mixed3;
	ArrayList mixed4;
	ArrayList mixed5;

	ArrayList floor1;//6
	ArrayList floor2;
	ArrayList floor3;
	ArrayList floor4;
	ArrayList floor5;
	ArrayList floor6;

	public List<String> chat = new ArrayList<>();

	/** Creates room for players, sets map and boxes for room*/
	public Room(int number) throws IOException {
		this.number = number;
		this.active_game = false;
		this.is_full = false;

		setMap();
		setBoxes();
	}

	/** Prints map*/
	public void printMap(){
		for(int i=0; i<map.size(); i++){
			System.out.println(map.get(i));
		}
	}

	/** Sets basic map*/
	public void setMap(){
		walls = new ArrayList<String>();
		for(int i=0; i<17; i++)
			walls.add("1");

		mixed1 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i%2 == 1) mixed1.add("0");
			else mixed1.add("1");
		}
		mixed2 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i%2 == 1) mixed2.add("0");
			else mixed2.add("1");
		}
		mixed3 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i%2 == 1) mixed3.add("0");
			else mixed3.add("1");
		}
		mixed4 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i%2 == 1) mixed4.add("0");
			else mixed4.add("1");
		}
		mixed5 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i%2 == 1) mixed5.add("0");
			else mixed5.add("1");
		}

		floor1 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor1.add("1");
			else floor1.add("0");
		}
		floor2 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor2.add("1");
			else floor2.add("0");
		}
		floor3 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor3.add("1");
			else floor3.add("0");
		}
		floor4 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor4.add("1");
			else floor4.add("0");
		}
		floor5 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor5.add("1");
			else floor5.add("0");
		}
		floor6 = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor6.add("1");
			else floor6.add("0");
		}


		map.add(walls);

		map.add(floor1);
		map.add(mixed1);
		map.add(floor2);
		map.add(mixed2);
		map.add(floor3);
		map.add(mixed3);
		map.add(floor4);
		map.add(mixed4);
		map.add(floor5);
		map.add(mixed5);
		map.add(floor6);

		/*for(int i=0; i<11; i++){
			if(i%2 == 0) map.add(floor);
			else map.add(mixed);
		}*/
		map.add(walls);
	}
	/** Sets all boxes*/
	public void setBoxes(){
		//boxy ustawiam na 2;
		String box = "2";
		//line 1
		map.get(1).set(2, box);
		map.get(1).set(4, box);
		map.get(1).set(6, box);
		map.get(1).set(7, box);
		map.get(1).set(8, box);
		map.get(1).set(11, box);
		map.get(1).set(12, box);
		//line 2
		map.get(2).set(5, box);
		map.get(2).set(9, box);
		map.get(2).set(11, box);
		map.get(2).set(13, box);
		//line 3
		map.get(3).set(3, box);
		map.get(3).set(4, box);
		map.get(3).set(5, box);
		map.get(3).set(6, box);
		map.get(3).set(9, box);
		map.get(3).set(10, box);
		map.get(3).set(12, box);
		map.get(3).set(13, box);
		map.get(3).set(14, box);
		map.get(3).set(15, box);
		//line 4
		map.get(4).set(1, box);
		map.get(4).set(3, box);
		map.get(4).set(7, box);
		map.get(4).set(9, box);
		map.get(4).set(13, box);
		map.get(4).set(15, box);
		//line 5
		map.get(5).set(1, box);
		map.get(5).set(2, box);
		map.get(5).set(4, box);
		map.get(5).set(5, box);
		map.get(5).set(6, box);
		map.get(5).set(8, box);
		map.get(5).set(10, box);
		map.get(5).set(11, box);
		map.get(5).set(12, box);
		map.get(5).set(13, box);
		map.get(5).set(14, box);
		//line 6
		map.get(6).set(3, box);
		map.get(6).set(5, box);
		map.get(6).set(7, box);
		map.get(6).set(13, box);
		//line 7
		map.get(7).set(1, box);
		map.get(7).set(2, box);
		map.get(7).set(4, box);
		map.get(7).set(6, box);
		map.get(7).set(9, box);
		map.get(7).set(11, box);
		map.get(7).set(14, box);
		map.get(7).set(15, box);
		//line 8
		map.get(8).set(1, box);
		map.get(8).set(5, box);
		map.get(8).set(7, box);
		map.get(8).set(11, box);
		map.get(8).set(15, box);
		//line 9
		map.get(9).set(2, box);
		map.get(9).set(3, box);
		map.get(9).set(6, box);
		map.get(9).set(9, box);
		map.get(9).set(12, box);
		map.get(9).set(13, box);
		map.get(9).set(14, box);
		map.get(9).set(15, box);
		//line 10
		map.get(10).set(3, box);
		map.get(10).set(5, box);
		map.get(10).set(7, box);
		map.get(10).set(9, box);
		map.get(10).set(11, box);
		map.get(10).set(13, box);
		//line11
		map.get(11).set(3, box);
		map.get(11).set(4, box);
		map.get(11).set(6, box);
		map.get(11).set(8, box);
		map.get(11).set(9, box);
		map.get(11).set(10, box);
		map.get(11).set(12, box);
	}
	/** Broadcast message to all players in room*/
	public void broadcast(String msg) {
		for(Player player: players_in_room) {
			try {
				synchronized (player.out){
					player.out.writeUTF(msg);
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Failed to broadcast to client.");
				curr_number_of_players--;
				is_full = false;
				players_in_room.remove(player);
			}
		}
	}
	/** Sends just boxes positions to all players in room*/
	public void sendBoxesPositions() throws IOException {
		for(int i = 1; i < map.size()-1; i++){
			for(int j = 1; j < walls.size()-1; j++){ //którakolwiek
				if (map.get(i).get(j) == "2"){
					float boxX = (j * blockSize) - 0.5f;
					float boxY = ((map.size() - 1.15f) * blockSize) - (i * blockSize);//chyba tak xd
					broadcast("box " + Server.boxNumber++ + " " + boxX + " " + boxY);
				}
			}
		}
	}
	/** Sends just blasts positions to all player in room*/
	public void sendBlastPositions(String start, String bombNumber) throws IOException {
		for(int i = 1; i < map.size()-1; i++){
			for(int j = 1; j < walls.size()-1; j++){ //którakolwiek
				if (map.get(i).get(j).startsWith("3")){
					float blastX = (j * blockSize) - 0.5f;
					float blastY = ((map.size() - 1.15f) * blockSize) - (i * blockSize);//chyba tak xd
					String type = "";
					String position = map.get(i).get(j);
					if (position.endsWith("0") || position.endsWith("1")) type = "h";
					else if (position.endsWith("2") || position.endsWith("3")) type = "v";
					else type = "c";
					broadcast("blast " + Server.blastNumber++ + " " + blastX + " " + blastY + " " + type + " " + start + " " + bombNumber);
				}
			}
		}
	}

	/** Sends information about player and hi status to all players in room*/
	public void updatePlayers(){
		for(Player player: players_in_room){
			broadcast("players " + player.id + " " + player.ready);
		}
	}

	/** If new player connects to room, sends him all previous messages from chat*/
	public void chatNew(Player player){
		for(int i=0; i<chat.size(); i++){
			try {
				synchronized (player.out){
					//System.out.println(chat.get(i));
					player.out.writeUTF("chatNew " + chat.get(i));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/** If all players are ready, sets them to correct corners, and starts game.*/
	public void startGame(){
		//40,40 - left down corner
		//40,360 - left up corner
		//490,360 - right up corner
		//490,40 - right down corner

		//player 0
		players_in_room.get(0).x = 40;
		players_in_room.get(0).y = 40;
		//player 1
		players_in_room.get(1).x = 490;
		players_in_room.get(1).y = 360;
		//player 2
		if(curr_number_of_players >= 3){
			players_in_room.get(2).x = 40;
			players_in_room.get(2).y =360;
		}
		//player 3
		if(curr_number_of_players == 4){
			players_in_room.get(3).x = 490;
			players_in_room.get(3).y = 40;
		}

		for (Player other : players_in_room) {
			broadcast("update " + other.id + " " + other.x + " " + other.y);
		}
		active_game = true;
		is_full = true; //wiem, ze nie jest
		broadcast("start");
	}
}
