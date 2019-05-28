package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{

    public static final int defaultPort = 1026;

    private ServerSocket _serverSocket;

    public Server(int port) throws IOException {
        _serverSocket = new ServerSocket(port);
    }

    public void createConnexion(Socket socket){
    	new Thread(new Communication(socket)).start();
    }

  

    @Override
    public void run() {
    	while(true)
    	{
    		try {
                System.out.println("[SERVER] Listening...");
    			Socket socket=_serverSocket.accept();
    			createConnexion(socket);
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }

    public static void main(String[] args) {
        try {
            new Thread(new Server(Server.defaultPort)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
