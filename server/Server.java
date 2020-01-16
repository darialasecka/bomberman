import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Player {
	public float x;
	public float y;
	public String id;
	public Socket socket;
	public DataOutputStream	out;
	public DataInputStream in;
	public int room;

	public Player(float x, float y, String id, Socket socket, int room) throws IOException {
		this.x = x;
		this.y = y;
		this.id = id;
		this.socket = socket;
		this.out = new DataOutputStream(socket.getOutputStream());
		this.in = new DataInputStream(socket.getInputStream());
		this.room = room;
	}
}

class Room {
	public int number;
	public List<Player> players_in_room = new ArrayList<>();
	public boolean active_game;
	public boolean is_full;
	public int curr_number_of_players = 0;
	public final int MAX_CAPACITY = 4;
	public final int MIN_CAPACITY_REQUIRED = 2;

	public Room(int number) throws IOException {
		this.number = number;
		this.active_game = false;
		this.is_full = false;
	}

	public void broadcast(String msg) {
		for(Player player: players_in_room) {
			try {
				synchronized (player.out){
					player.out.writeUTF(msg);
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Failed to broadcast to client.");
				players_in_room.remove(player);
			}
		}
	}
}

class Multi extends Thread {
	private Player player;
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


	Multi(Player player){
		this.player = player;

		setMap();
		setBoxes();

		/*for(int i=0; i<map.size(); i++){
			System.out.println(map.get(i));
		}*/
	}

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
		map.get(8).set(2, box);
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

	public void run()  {
		System.out.println("New player " + player.socket.getPort() + " connected.");
		while (true) {
			try {
				String msg = "";
				synchronized (player.in){
					 msg = player.in.readUTF();
				}
				if (msg.startsWith("connected")) {
					synchronized (player.out){
						player.out.writeUTF("created " + player.id + " " + player.x + " " + player.y);
						for(int i = 1; i < map.size()-1; i++){
							for(int j = 1; j < walls.size()-1; j++){ //którakolwiek
								if (map.get(i).get(j) == "2"){
									float boxX = (j * blockSize) - 0.5f;
									float boxY = ((map.size() - 1.15f) * blockSize) - (i * blockSize);//chyba tak xd
									player.out.writeUTF("box " + Server.boxNumber++ + " " + boxX + " " + boxY);
								}
							}
						}
					}
				} else if (msg.startsWith("playerMoved")) {
					String x = msg.split(" ")[1];
					String y = msg.split(" ")[2];
					int direction = Integer.parseInt(msg.split(" ")[3]);
					int posX = (int)(player.x / blockSize);// tak chemy zaokrąglić w górę
					int posY = (int)(player.y / blockSize);
					posY = map.size()-posY - 1;
					//System.out.println(posX + " " + posY);

					if(direction == 0){//lewo
						//System.out.println(posX + " " + map.get(posY).get(posX));
						if(map.get(posY).get(posX) == "0") player.x = Float.parseFloat(x);
							/*System.out.println("Tak");
						else System.out.println("Nie");*/
					} else if(direction == 1) {//prawo
						posX = (int)(((player.x) / blockSize) - 0.5);
						if(map.get(posY).get(posX+1) == "0") player.x = Float.parseFloat(x);
					} else if(direction == 2){//góra
						posY = (int)((player.y / blockSize) - 0.75);
						posY = map.size()-posY-2;
						if(map.get(posY).get(posX) == "0") player.y = Float.parseFloat(y);
					} else if(direction == 3) {//dól
						if(map.get(posY).get(posX) == "0") player.y = Float.parseFloat(y);
					}
					//player.y = Float.parseFloat(y);
					Room room = Server.rooms.get(player.room);
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
					room.broadcast(msg);
				}

			} catch (Exception e) {
				Room room = Server.rooms.get(player.room);
				room.players_in_room.remove(player);
				room.broadcast("remove " + player.id);
				Server.players.remove(player);
				System.out.println("Player " + player.socket.getPort() + " disconnected.");
				break;
			}
		}
	}

}

public class Server extends Thread{
	public static int port = 8080;
	public static ServerSocket serverSocket = null;
	public static List<Player> players = new ArrayList<>();
	public static List<Room> rooms = new ArrayList<>();
	public static int id = 0;
	public static int roomNumber = 0;
	public static int bombNumber = 0;
	public static int boxNumber = 0;

	public static void main(String args[]) {
		try {
			serverSocket = new ServerSocket(port);//nie mogę bo static
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create ServerSocket.");
		}
		System.out.println("Server is running...");
		while (true) {
			try {
				Room room = new Room(roomNumber);
				rooms.add(room);
				System.out.println("Created room " + roomNumber);
				while(!room.active_game || !room.is_full){
					try {
						Socket socket = serverSocket.accept();
						//40,40 - left down corner
						//40,360 - left up corner
						//490,40 - right down corner
						//490,360 - right up corner
						//na razie niech wszyscy się tworzą na 40,40, póżniej poprawimy, żeby byli w rogach w zależności od players-list-length w danym pokoju
						Player player = new Player(40,40, Integer.toString(id), socket, roomNumber);
						room.players_in_room.add(player);
						room.curr_number_of_players ++;
						players.add(player);
						new Multi(player).start(); //coś co ogarnie klientów
						id++;
						if(room.curr_number_of_players == room.MAX_CAPACITY){
							room.is_full = true;
							break;
						}
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("Failed to connect to client.");
					}
				}
				roomNumber++;
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("Failed to create room.");
			}
		}

	}
	//sends message to all players in room
	/*public static void broadcast(String msg) {
		DataOutputStream out;
		for(Player player: players) {
			out = player.out;
			try {
				synchronized (player.out){
					out.writeUTF(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				//System.out.println("Failed to broadcast to client.");
				players.remove(player);
			}
		}
	}*/
}