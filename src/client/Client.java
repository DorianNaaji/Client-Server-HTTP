package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.StrUtils;
import customedexceptions.UnknownFileFormatException;

public class Client
{
    public static void main(String[] args) {
        try {
            Socket sock = new Socket("localhost",1026);
            System.out.println("Socket port: " + sock.getPort());
            System.out.println("Socket local port: " + sock.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
