package Server;

import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import projA.*;

public class Worker implements Runnable 
{
   private Socket client;
   private TCPServer server;
   private PrintStream log;
   private PrintStream out;
   private Scanner in;
   private boolean running = true;
   private Date previousConnection;
   
   private String gettime = "(?i)gettime";
   private String bye = "(?i)bye";
   private String punch = "(?i)punch\\s(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
   private String plug = "(?i)plug\\s(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
   private String prime = "((?i)prime)\\s+\\d+";
   private String auth = "((?i)auth)\\s+\\w+\\s+\\w+";
   private String roster = "((?i)roster)\\s+\\w+\\s+((?i)(XML|JSON))";
   
   private HashMap<Integer, BigInteger> primeCache = new HashMap<>();
   
   public Worker(Socket client, TCPServer server, PrintStream log) throws Exception {
	   this.client = client;
	   this.server = server;
	   this.log = log;
	   out = new PrintStream(client.getOutputStream());
	   in = new Scanner(client.getInputStream());    
   }
   
   public void handle() throws Exception {
	   while (this.running) {
		   
		   String command = in.nextLine().trim(); 
		   
		   String[]  strs = command.split("\\s+");
		   if (command.matches(gettime)) 
			   this.getTime();
		   
		   else if (command.matches(bye)) 
			   this.bye();
		   
		   else if (command.matches(punch)) 
			   this.plugPunch("punch",strs[1]);   
		   
		   else if (command.matches(plug)) 
			   this.plugPunch("plug",strs[1]);
		   
		   else if (command.matches(prime)) 
			   this.prime(Integer.parseInt(strs[1]));
		   
		   else if (command.matches(auth)) 
			   this.authenticate(strs[1], strs[2]);
		  
		   else if (command.matches(roster)) 
			   this.roster(strs[1], strs[2]); 
		   
		   else 
			   out.println("INVALID COMMAND");		   
		   
		   
		
		   checkConnectionTime();
	   }
   }
   
   private void checkConnectionTime() throws Exception {
	   Date currentTime = new Date();
	   if (previousConnection == null) {
		   previousConnection = new Date();
		   return;
	   }
	   //check for time elapsed
	   int seconds = (int) (previousConnection.getTime() - (currentTime.getTime())/1000);
	   if (seconds < 2) {
		   bye();
	   } else 
		   previousConnection = currentTime;
	   
   }
   
   public void getTime() throws Exception {
	  Date date = new Date();
	  out.println(date.toString());
   }
   
   public void bye() throws Exception {
	   //out.println("Server is closing");
	   this.running = false;
	   client.close();
	   log.append(String.format("A disconnection (subentry: %s)", this.client.getInetAddress()));
   }
 
   public synchronized void plugPunch(String type, String ip) throws Exception {
	   
	   InetAddress address = InetAddress.getByName(ip);
	   if (type.equals("punch"))
		   server.addToWhiteList(address);
	   else if (type.equals("plug"))
		   server.removeFromWhiteList(address); 
	   	
   }
   
   public void prime(int num) throws Exception {
	     
	      if (primeCache.containsKey(num)) 
	      {
	    	  BigInteger previousResult = primeCache.get(num);
	    	  out.println(previousResult.toString() + " (from cache)");
	      } else {  
	    	BigInteger number = BigInteger.probablePrime((int) (num * 3.5), new Random());
	    	primeCache.put(num, number); 
	    	out.println(number.toString());
	      }
		  
   }
   
	public void roster(String courseNum, String type) throws Exception  {
		Course course = Util.getCourse(courseNum);
			
		//----------------------------------------->> Marshall to XML :
		if (type.toLowerCase().equals("xml")) {
			JAXBContext context = JAXBContext.newInstance(Course.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(course, out);
		}
		//----------------------------------------->> Parse into Json:
		else if (type.toLowerCase().equals("json")){
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(course);
			out.println(json);	
		}	
   }

	@Override
	public void run() {
		try {
			this.handle();
		} catch (Exception e) {
			log.append(String.format("An exception (subentry: %s / %s)", e.getCause(), e.getStackTrace().toString()));
		}
	}
	
	public void authenticate(String user, String password) throws Exception {
		boolean auth = server.authenticate(user, password);
		// check if the user is present in the hashmap
		if (auth) 
			out.println("You are in!");
		else 
			out.println("Auth Failure");
		
	}
	
	   
	    
}
