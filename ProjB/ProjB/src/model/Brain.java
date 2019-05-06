package model;

import java.io.PrintStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;




public class Brain
{
	public static final double BITS_PER_DIGIT = 3.0;
	public static final Random RNG = new Random();	
	public static final String TCP_SERVER = "red.eecs.yorku.ca";
	public static final int TCP_PORT = 12345;
	//public static final String DB_URL = "jdbc:derby://red.eecs.yorku.ca:64413/EECS;user=student;password=secret";
	public static final String DB_URL = "jdbc:derby:localhost;user=student;password=secret";
	public static final String HTTP_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/World.cgi";
	public static final String ROSTER_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/Roster.cgi";
	private static Brain brainInstance = null;
	
	private  Brain()
	{
	}
	
	public static Brain getInstance() {
		if (brainInstance == null) 
			brainInstance = new Brain();
		
		return brainInstance;
	}
	
	public ArrayList<String> doTime(String advanced) throws Exception
	{
		ArrayList<String> response = new ArrayList<>();
		
		Date date = new Date();
		response.add(date.toString()); 
		
		if (!advanced.equals("")) {
			int hours = Integer.parseInt(advanced); 
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR_OF_DAY, hours);
			response.add(calendar.getTime().toString());
		} 
		
		return response;
	}
	

	
	public String doPrime(String prime) throws Exception
	{
		BigInteger number = BigInteger.probablePrime((int) (Integer.parseInt(prime) * 3.5), new Random());
		return number.toString();
	}
	
	public String doTcp(String digit) throws Exception
	{
	       Socket socket = new Socket(Brain.TCP_SERVER, Brain.TCP_PORT);   
		   PrintStream out = new PrintStream(socket.getOutputStream());
		   Scanner in = new Scanner(socket.getInputStream());   
		   out.println("prime " + digit);
		   String resp = in.nextLine();
		   socket.close();
		   return resp;
	}
	
	public String doDb(String itemNo) throws Exception {
	  Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
	  Connection con = DriverManager.getConnection(Brain.DB_URL);
	  Statement s = con.createStatement();
	  s.executeUpdate("set schema roumani");
	//SQL query to obtain the NAME and PRICE of an item whose number is itemNo in a table ITEM
	  String query = "SELECT NAME, PRICE FROM ITEM WHERE number='" + itemNo +"'"; 
	  ResultSet r = s.executeQuery(query);
	  String result = "";
	  if (r.next())
	  {
	  	result = "$" + r.getDouble("PRICE") + " - " + r.getString("NAME");
	  }
	  else
	  {
	  	throw new Exception(itemNo + " not found! ");
	  }
	  r.close(); 
	  s.close(); 
	  con.close();
	  
	  return result;
	
	}

	public String doHttp(String country, String query) throws Exception
	{
		String fullUrl = String.format(HTTP_URL + "?country=%s&query=%s", country, query);
		URL url = new URL(fullUrl);
		HttpURLConnection  connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
	    
	    Scanner in = new Scanner(connection.getInputStream());
	    
	     StringBuffer response = new StringBuffer();
	     while ( in.hasNext()) 
	     	response.append(in.nextLine());
	     in.close();
	
		return response.toString();
	}
	
	public String doRoster(String course) throws Exception {
		String fullUrl = String.format(ROSTER_URL + "?course=%s", course);
		URL url = new URL(fullUrl);
		HttpURLConnection  connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
	    
	    Scanner in = new Scanner(connection.getInputStream());
	    
	     StringBuffer response = new StringBuffer();
	     while ( in.hasNext()) 
	     	response.append(in.nextLine());
	     in.close();
	     
	     
	
		return response.toString();
	}
	
}