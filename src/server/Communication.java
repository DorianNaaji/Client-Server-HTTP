package server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

public class Communication implements Runnable{

    private static final String WWW_PATH = "/home/thiti/websites";

    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;

    private String _command;
    private String _filePath;
    private String _version;

    private String _contentType;
    private String _contentLength;
    private String _contentLocation;

    private String _body;
    private String _codeMessage;
    private int _code;


    public Communication(Socket socket){
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

        if (!protocol.startsWith("HTTP/1.1")){
            buildStatus(400);
        }else{
            try {
                FileInputStream fileInputStream = new FileInputStream("C:\\www\\" + filePath);
                buildStatus(200);
                _out.write("\r\n");
                readFile(filePath);
            } catch (FileNotFoundException e) {
                buildStatus(404);
            }
        }
        _out.write("\r\n\r\n");
        _out.flush();
    }

    /**
     * La méthode ReadFile permet la lecture d'un fichier
     * @param filePath
     */
    private void readFile(String filePath){

        try {
            FileInputStream fileInputStream = new FileInputStream(WWW_PATH + filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder result = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    result.append(line + "\r\n");
                }
                reader.close();
            } catch (IOException e) {
                _body = null;
            }
            _body = result.toString();
        } catch (FileNotFoundException e) {
            _body = null;
        }
    }

    private void buildStatus(int code){
        if (code == 200){
            _out.write("HTTP/1.1 200 OK\r\n");
        }else if(code == 400){
            _out.write("HTTP/1.1 400 bad request\r\n");
        }else if(code == 404){
            _out.write("HTTP/1.1 404 resource not found\r\n");
        }
    }

    public void sendAnwser(){

    }

    private String interprate(String message){
        System.out.println(message);
        /*
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
        */
        return null;
    }

    @Override
    public void run() {
        try {
            _out = new PrintWriter(_socket.getOutputStream());
            _in =  new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            while (true){
                String line;
                StringBuilder message = new StringBuilder();
                while((line = _in.readLine()) != null){
                    message.append(line);
                    message.append("\n");
                    interprate(message.toString());
                }

                //System.out.print(_socket.getPort() + " | "+_socket.getLocalPort()   + " : ");


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
