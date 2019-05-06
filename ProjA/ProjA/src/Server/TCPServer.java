package Server;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TCPServer 
{
	private Set<InetAddress> firewall;
	private HashMap<String, String> auth;
	
	
	public TCPServer(int port, PrintStream log, File file) throws Exception
	{
		ServerSocket server = new ServerSocket(port);
		log.append(String.format("Server start (subentry: %s:%s)", server.getInetAddress(), server.getLocalPort()));
		
		firewall = new HashSet<InetAddress>();
		firewall.add(server.getInetAddress());
		firewall.add(InetAddress.getByName("127.0.0.1"));
		
		auth = new HashMap<>();
		
		while (file.exists())
		{
 			Socket client = server.accept();
 			System.out.println("IP: " + client.getInetAddress());
			// check the client's IP and filter it
 			if (firewall.contains(client.getInetAddress())) 
 			{
				log.append(String.format("A connection (subentry: %s)", client.getInetAddress()));
				
	 			Worker worker = new Worker(client, this, log);
	 			(new Thread(worker)).start();

 			} else {
 				//blacklist
 			   PrintStream out = new PrintStream(client.getOutputStream());
 			   out.println(String.format("IP Address: '%s' IS NOT IN THE FIRE WALL", client.getInetAddress().toString()));
 			   log.append(String.format("Not in whitelist (subentry: %s)", client.getInetAddress()));
 			   client.close();
 			}
		}
		server.close();
		log.append(String.format("Server shutdown (subentry: %s)", server.getInetAddress()));
	}
	
	
	public void addToWhiteList(InetAddress address) {
		this.firewall.add(address);
	}
	
	public void removeFromWhiteList(InetAddress address) {
		this.firewall.remove(address);
	}
	
	public boolean authenticate(String user, String password)  {
		if (!auth.containsKey(user) && password.equals(auth.get(user))) 
			return true;
	 		
		return false;
	}

	
	public static void main(String[] args) throws Exception 
	{
		
		OutputStream outputStream = new FileOutputStream("src/data/data.bin");
		PrintStream log = new PrintStream(outputStream);
		
		File file = new File("src/ServerFile/running.txt");
		
		new TCPServer(6060,log, file);

	}


}


