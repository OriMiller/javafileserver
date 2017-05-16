/**
d * @author orimiller
 * This is a Java Client/Server Socket-based file download program 
 * developed by Ori Miller for his 11th grade (Junior) year 
 * independent study of Computer Science
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	static ServerSocket server = null;
	static boolean serUp = true;
	public static void main(final String[] args) throws UnknownHostException
	{
		System.out.println("Server Up");
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		
		try {
			server = new ServerSocket(2525);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int x = 0;
		while(serUp){
			try {
				Socket client = server.accept();
				System.out.println(client.toString());
				new Handler(client).start();
				System.out.println("Cycle " + x);
				x++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}
