package client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client
{

    public static void main(String[] args) {
        System.out.println("[CLIENT] start");
        try {
            for (int i = 0; i< 1; i++){
                Socket so = new Socket("localhost",1026);
                OutputStream out = so.getOutputStream();
                out.write(new String("coucou").getBytes());
                out.flush();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
