import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerForTest extends Thread{
	public int port;
	public ServerSocket ss;

	/** Simple server made just for tests*/
	public ServerForTest(int port) throws IOException {
		this.port = port;
		this.ss = new ServerSocket(port);
	}

	public void run(){
		try {
			Socket socket = ss.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
