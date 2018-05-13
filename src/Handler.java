import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.Socket;

import java.util.ArrayList;
import java.util.Arrays;

class Handler extends Thread
{	
	private String homeFile;
	private File file;
	private Socket socket; //Socket connected to client
	private DataInputStream in;
	private DataOutputStream out;
	
	Handler(Socket s, String homeFile) throws IOException
	{
		System.out.println(this.toString());
		socket = s;
		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		this.homeFile = homeFile;
		this.file = new File(homeFile);
	}
	
	private void getList(File file) throws IOException
	{
			ArrayList<String> strs = new ArrayList<String>();
			for(int i=0;i<file.listFiles().length;i++)
			{
				File tem = file.listFiles()[i];
				if(tem.isDirectory())
				{
					strs.add(file.listFiles()[i].getName() + "(2)");
				} 
				else if(tem.isFile())
				{
					strs.add(file.listFiles()[i].getName() + "(1)");
				}
			}
			String[] outList = Arrays.copyOf(strs.toArray(), strs.size(), String[].class);
			out.writeInt(Serializer.serialize(outList).length);
			out.write(Serializer.serialize(outList));
			out.flush();
	}
	
	private void download(int get){
		try{
			File myFile = new File(file.listFiles()[get].getPath());
			if(myFile.canRead()){
			byte [] mybytearray  = new byte [(int)myFile.length()];
			out.writeInt((int)myFile.length());
		    @SuppressWarnings("resource")
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
	        bis.read(mybytearray,0,mybytearray.length);
	        System.out.println("Sending " + file.list()[get] + "(" + mybytearray.length);
	        BufferedOutputStream bos = new BufferedOutputStream(out);
	        bos.write(mybytearray,0,mybytearray.length);
	        bos.flush();
	        System.out.println("Done.");
	        } else {
	        	System.out.println("ACCESS DENIED");
	        }
			} catch (Exception e){
				e.printStackTrace();
			}	
	}
	
	public void run()
	{
		try 
		{
			System.out.println("CONNECTION: " + socket.toString());
			while(!socket.isClosed())
			{
					int first = in.readInt();
					switch(first){
					case 1:
						System.out.println(this.toString() + " REFRESH");
						getList(file);
						continue;
					case 2:
						int get = in.readInt();
						System.out.println(this.toString() + " DownloadIndex : " + get);
						if(file.listFiles()[get].isFile())
						{
							download(get);
							System.out.println(this.toString() + " "  + file.getAbsolutePath());
						} 
						else if(file.listFiles()[get].isDirectory())
						{
							file = new File(file.listFiles()[get].getPath());
							System.out.println(this.toString() + " " + file.getAbsolutePath());
						}
						break;
					case 3:
						file = new File(homeFile);
						System.out.println(this.toString() + " " + file.getAbsolutePath());
						break;
					case 4:
						System.out.println("Closing: " + socket.toString());
						socket.close();
						break;
					default:
						socket.close();
						break;
					}
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}