package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
		
	public Client()
	{
		
	}
	
	public Socket OpenSocket(String adIP, int port)
	{
		Socket sc=null; 
		try 
		{
			
			 sc = new Socket(InetAddress.getByName(adIP), port);
		}
		catch(UnknownHostException ex)
		{
			System.out.println("Erreur au niveau du port ");
			return sc;
		}
		catch(IOException ex)
		{
			System.out.println("Erreur au niveau de l'ouverture du Socket");
			return sc;
		}
		
		return sc;
		
	}	
}
