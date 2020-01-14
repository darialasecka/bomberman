import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Player {
	public double x;
	public double y;
	public int id;
	public Socket socket;
	public DataOutputStream	out;
	public DataInputStream in;

	public Player(double x, double y, int id, Socket socket) throws IOException {
		this.x = x;
		this.y = y;
		this.id = id;
		this.out = new DataOutputStream(socket.getOutputStream());
		this.in = new DataInputStream(socket.getInputStream());
	}
}

class Room {
	public int number;
	public Player player1;
	public Player player2;
	public Player player3;
	public Player player4;
	public boolean active_game = false;
	public int curr_number_of_players = 0;
	public final int MAX_CAPACITY = 4;
	public final int MIN_REQUIRED_CAPACITY = 2;

	public Room(double number) throws IOException {
		//na razie nic nie robi
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
				Player player = new Player(0,0, id, socket);
				System.out.println("New Player Connected: " + id + " by " + socket.getPort() );
				players.add(player);
				id++;
				//new Multi(sock, out).start(); //coś co ogarnie klientów
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Failed to connect to client.");
			}
		}
	}

	private void broadcast(String s) {
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


class Multi extends Thread {
	private Player player;
	private Server server;
	private DataOutputStream out;
	private DataInputStream in;

	Multi(Player player, Server server) throws IOException {
		this.player = player;
		this.server = server;
	}

	public void run() {
		System.out.println("Connected to " + player.socket.getPort() +".");
		while(true) {
			try {
				//tu obsługa poleceń

			} catch (Exception e) {
				server.players.remove(player);
			}
		}
	}
}