package server;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class Communication implements Runnable{

    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;

    public Communication(@NotNull Socket socket){
        if (socket == null)
            throw new IllegalArgumentException("instance of Socket can not be null, Aliboron");
        _socket = socket;
    }

    private void put(String message){

    }

    private void get(String filePath, String protocol){
        // check protocol
        // ouvre le fichier s'il existe
        // ecrit le contenue du fichier dans le out
        // flush

        _out.write("coucou mon gars");
        _out.flush();


    }

    private String interprate(String message){
        StringTokenizer stringTokenizer = new StringTokenizer(message," ");
        while(stringTokenizer.hasMoreElements()){
            String element = (String)stringTokenizer.nextElement();
            if (element.equalsIgnoreCase("GET")){
                String filePath = (String)stringTokenizer.nextElement(); // check si prochiane ele existe
                String protocol = (String)stringTokenizer.nextElement(); // check si prochiane ele existe
                get(filePath,protocol);
            }else if(element.equalsIgnoreCase("PUT")){

            }else{

            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            _out = new PrintWriter(_socket.getOutputStream());
            _in =  new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            while (true){
                interprate(_in.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
