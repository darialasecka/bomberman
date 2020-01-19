import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player {
	public float x;
	public float y;
	public String id;
	public Socket socket;
	public DataOutputStream out;
	public DataInputStream in;
	public int room;
	public boolean ready = false;
	/** Creates Player allowinf to store information about him,player position, id socket and room as parameters
	 * @param  x position x of player
	 * @param y position y of player
	 * @param id, id of player
	 * @param socket player socket for communication
	 * @param room room number in which player is*/
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
