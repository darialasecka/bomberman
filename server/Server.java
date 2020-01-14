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
	
	Multi(Player player) throws IOException {
		this.player = player;
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
					player.x = Float.parseFloat(x);
					player.y = Float.parseFloat(y);
					for (Player other : Server.players) {
						player.out.writeUTF("update " + other.id + " " + other.x + " " + other.y);
					}
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