package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{

    public static final int defaultPort = 1026;

    private ServerSocket _serverSocket;
    private Socket _socket; //à méditer

    public Server(int port) throws IOException {
        _serverSocket = new ServerSocket(port);
    }

    public void createConnexion(Socket _socket){
    	new Thread(new Communication(_socket)).start();
    }

  

    @Override
    public void run() {
    	while(true)
    	{
    		try {
    			_socket=_serverSocket.accept();
    			createConnexion(_socket);
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }

    public static void main(String[] args) {

    }
}
