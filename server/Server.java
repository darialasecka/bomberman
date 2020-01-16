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

	public Player(float x, float y, String id, Socket socket) throws IOException {
		this.x = x;
		this.y = y;
		this.id = id;
		this.socket = socket;
		this.out = new DataOutputStream(socket.getOutputStream());
		this.in = new DataInputStream(socket.getInputStream());
	}
}

class Room {
	public int number;
	public List<Player> players_in_game = new ArrayList<>();
	public boolean active_game;
	public boolean is_full;
	public int curr_number_of_players = 0;
	public final int MAX_CAPACITY = 4;
	public final int MIN_REQUIRED_CAPACITY = 2;

	public Room(int number) throws IOException {
		this.number = number;
		this.active_game = false;
		this.is_full = false;
	}
}

class Multi extends Thread {
	private Player player;
	float blockSize = 32;
	public ArrayList<ArrayList<String>> map = new ArrayList<ArrayList<String>>();

	Multi(Player player) throws IOException {
		this.player = player;


		ArrayList walls = new ArrayList<String>();
		for(int i=0; i<17; i++)
			walls.add("1");
		ArrayList mixed = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i%2 == 1) mixed.add("0");
			else mixed.add("1");
		}
		ArrayList floor = new ArrayList<String>();
		for(int i=0; i<17; i++){
			if(i == 0 || i ==16) floor.add("1");
			else floor.add("0");
		}
		/*System.out.println(walls);
		System.out.println(mixed);
		System.out.println(floor);*/
		map.add(walls);
		for(int i=0; i<11; i++){
			if(i%2 == 0) map.add(floor);
			else map.add(mixed);
		}
		map.add(walls);
		/*for(int i=0; i<map.size(); i++){
			System.out.println(map.get(i));
		}*/
	}




	public void run()  {
		System.out.println("Connected to " + player.socket.getPort() +".");
		while (true) {
			try {
				String msg = player.in.readUTF();
				if (msg.startsWith("connected")) {
					player.out.writeUTF("created " + player.id + " " + player.x + " " + player.y);
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
					for (Player other : Server.players) {
						player.out.writeUTF("update " + other.id + " " + other.x + " " + other.y);
					}
				} else if (msg.startsWith("bomb")) {
					Server.broadcast(msg);
				}

			} catch (Exception e) {
				Server.players.remove(player);
				System.out.println("Player " + player.socket.getPort() + " disconnected.");
				Server.broadcast("remove " + player.id);
				break;
			}
		}
	}



}

public class Server extends Thread{
	public static int port = 8080;
	public static ServerSocket serverSocket = null;
	public static List<Player> players = new ArrayList<>();
	public static int id = 0;

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
				Socket socket = serverSocket.accept();
				//na razie niech wszyscy się tworzą na 0,0, póżniej poprawimy, żeby byli w rogach w zależności od players-list-length
				Player player = new Player(100,100, Integer.toString(id), socket);
				players.add(player);
				new Multi(player).start(); //coś co ogarnie klientów
				id++;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to connect to client.");
			}
		}
	}

	public static void broadcast(String s) {
		DataOutputStream out = null;
		for(Player player: players) {
			out = (DataOutputStream) player.out;
			try {
				out.writeUTF(s);
			} catch (IOException e) {
				e.printStackTrace();
				//System.out.println("Failed to broadcast to client.");
				players.remove(player);
			}
		}
	}
}