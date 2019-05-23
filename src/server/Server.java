package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

    public static final int defaultPort = 1026;

    private ServerSocket _serverSocket;
    //private Socket _socket; à méditer

    public Server(int port) throws IOException {
        _serverSocket = new ServerSocket(port);
    }



    public void get(){

    }

    public void put(){

    }

    public static void main(String[] args) {

    }

}
