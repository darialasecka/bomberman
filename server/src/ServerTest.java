import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;


	public void setBoxes(ArrayList<ArrayList<String>> map){
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
	
	@Test
	public void setMapTest(){
		Room room = null;
		try {
			room = new Room(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList walls;
		ArrayList mixed1;
		ArrayList mixed2;
		ArrayList mixed3;
		ArrayList mixed4;
		ArrayList mixed5;
		ArrayList floor1;
		ArrayList floor2;
		ArrayList floor3;
		ArrayList floor4;
		ArrayList floor5;
		ArrayList floor6;

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

		ArrayList<ArrayList<String>> map = new ArrayList<>();
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
		map.add(walls);

		setBoxes(map);
		
		assertEquals(map, room.map);
	}

	/*@Test
	public void PlayerNullInputStreamTest(){
		try{
			Socket socket = new Socket(InetAddress.getLocalHost(), 2137);
			PlayerTest multi = new PlayerTest(0,0, "", new DataOutputStream(socket.getOutputStream()), null,0);
			fail("Expected exception due to null InputStream");
		} catch (Exception e) {}
	}*/

	@Test
	public void PlayerSetInfoTest(){
		ServerForTest server = null;
		Player player = null;
		Socket socket = null;
		try {
			server = new ServerForTest(420);
			socket = new Socket(InetAddress.getLocalHost(), 420);
			player = new Player(0,0, "0", socket, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(player.x, 0);
		assertEquals(player.y, 0);
		assertEquals(player.id, "0");
		assertEquals(player.socket, socket);
		assertEquals(player.room, 0);
	}

	@Test
	public void RoomSetInfoTest(){
		Room room = null;
		try {
			room = new Room(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(room.number, 0);
		assertEquals(room.is_full, false);
		assertEquals(room.active_game, false);
	}

	@Test
	public void MultiSetInfoTest(){
		ServerForTest server = null;
		Player player = null;
		Socket socket = null;
		Multi multi = null;
		try {
			server = new ServerForTest(404);
			socket = new Socket(InetAddress.getLocalHost(), 404);
			player = new Player(0,0, "0", socket, 0);
			multi = new Multi(player);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(multi.player, player);
	}

	@Test
	public void MultiSetPlayerInfoTest(){
		ServerForTest server = null;
		Player player = null;
		Socket socket = null;
		Multi multi = null;
		try {
			server = new ServerForTest(314);
			socket = new Socket(InetAddress.getLocalHost(), 314);
			player = new Player(0,0, "0", socket, 0);
			multi = new Multi(player);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(multi.player.x, 0);
		assertEquals(multi.player.y, 0);
		assertEquals(multi.player.id, "0");
		assertEquals(multi.player.socket, socket);
		assertEquals(multi.player.room, 0);
	}

	@Test
	public void RoomSetPlayerInfoTest(){
		ServerForTest server = null;
		Player player = null;
		Socket socket = null;
		Room room = null;
		try {
			server = new ServerForTest(666);
			socket = new Socket(InetAddress.getLocalHost(), 666);
			player = new Player(0,0, "0", socket, 0);
			room = new Room(0);
			room.players_in_room.add(player);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(room.players_in_room.get(0).x, 0);
		assertEquals(room.players_in_room.get(0).y, 0);
		assertEquals(room.players_in_room.get(0).id, "0");
		assertEquals(room.players_in_room.get(0).socket, socket);
		assertEquals(room.players_in_room.get(0).room, 0);
		assertEquals(room.players_in_room.get(0).room, room.number);
	}


	@Test
	public void PlayerNullSocketTest(){
		try{
			Player player = new Player(0,0,"",null,1);
			fail("Expected exception due to null socket");
		} catch (Exception e) {}
	}
	
	@Test
	public void startGameAllPlayersPositionTest(){
		try{
			Socket socket = new Socket(InetAddress.getLocalHost(), 2137);
			Player player1 = new Player(0,0,"",socket,0);
			Player player2 = new Player(0,0,"",socket,0);
			Player player3 = new Player(0,0,"",socket,0);
			Player player4 = new Player(0,0,"",socket,0);
			Room room = new Room(0);
			room.players_in_room.add(player1);
			room.players_in_room.add(player2);
			room.players_in_room.add(player3);
			room.players_in_room.add(player4);

			room.startGame();
			assertEquals(player1.x,40);
			assertEquals(player1.y,40);
			assertEquals(player2.x,490);
			assertEquals(player2.y,360);
			assertEquals(player3.x,40);
			assertEquals(player3.y,360);
			assertEquals(player4.x,490);
			assertEquals(player4.y,40);

		} catch (Exception e) {}
	}

}