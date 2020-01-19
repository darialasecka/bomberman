import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	public static int port = 8080;
	public static ServerSocket serverSocket = null;
	public static List<Player> players = new ArrayList<>();
	public static List<Room> rooms = new ArrayList<>();
	public static int id = 0;
	public static int roomNumber = 0;
	public static int bombNumber = 0;
	public static int boxNumber = 0;
	public static int blastNumber = 0;
	public static int powerUpNumber = 0;

	/** Places new players in correspondings rooms*/
	public static void main(String args[]) {
		try {
			serverSocket = new ServerSocket(port);//nie mogę bo static
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create ServerSocket.");
		}
		System.out.println("Server is running...");
		Room room = null;
		try {
			room = new Room(roomNumber);
			rooms.add(room);
			System.out.println("Created room " + roomNumber);
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					if(room.active_game || room.is_full) {
						roomNumber++;
						room = new Room(roomNumber);
						rooms.add(room);
						System.out.println("Created room " + roomNumber);
					}

					Player player = new Player(40,40, Integer.toString(id), socket, roomNumber);
					room.players_in_room.add(player);
					room.curr_number_of_players ++;
					players.add(player);
					new Multi(player).start(); //coś co ogarnie klientów
					id++;
					if(room.curr_number_of_players == room.MAX_CAPACITY){
						room.is_full = true;
					}
				} catch (Exception e){
					e.printStackTrace();
					System.out.println("Failed to connect to client.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to create room.");
		}

	}
}