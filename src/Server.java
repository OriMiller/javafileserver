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
import java.util.Arrays;
import java.io.*;

public class Server {
	static ServerSocket server = null;
	static boolean serUp = true;
	static String file;
	public static void main(final String[] args) throws UnknownHostException
	{
		
		file = args[0];
		System.out.println(file);
		File f = new File(file);
		System.out.println(f.isDirectory());
		System.out.println(Arrays.toString(f.list()));
		if(f.isDirectory()) {
			try {
				server = new ServerSocket(2525);
				System.out.println("Server Up");
				System.out.println(InetAddress.getLocalHost().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
			int x = 0;
			while(serUp){
				try {
					Socket client = server.accept();
					System.out.println(client.toString());
					new Handler(client, file).start();
					System.out.println("Cycle " + x);
					x++;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		} else {
			System.out.println("Directory does not exist");
		}
	}
}
