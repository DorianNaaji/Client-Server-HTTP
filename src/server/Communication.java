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

    private String _header;
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

    private void decodeRequest(){
        if (!_version.equalsIgnoreCase("HTTP/1.1")){
            // error
        }else if(_command.equalsIgnoreCase("GET")){
            readGetRequest();
            decodeHeaderRequest();
        }else if(_command.equalsIgnoreCase("PUT")){
            readPutRequest();
            decodeHeaderRequest();
            decodePutRequest();
        }else{
            // error
        }
    }

    private void decodePutRequest(){

    }

    private void decodeHeaderRequest(){
        StringTokenizer stringTokenizer = new StringTokenizer(_header,"\r\n");
        while(stringTokenizer.hasMoreElements()){
            String element = (String) stringTokenizer.nextElement();
            if (element.startsWith("Content-Type:")){
                StringTokenizer st = new StringTokenizer(element," ");
                st.nextElement();
                _contentType = (String) st.nextElement();
            }else if(element.startsWith("Content-Length:")){
                StringTokenizer st = new StringTokenizer(element," ");
                st.nextElement();
                _contentLength = (String) st.nextElement();
            }else if(element.startsWith("Content-location:")){
                StringTokenizer st = new StringTokenizer(element," ");
                st.nextElement();
                _contentLocation = (String) st.nextElement();
            }
        }
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
        if(_command.equalsIgnoreCase("GET")){
            _out.write(_version + " " + _code + " " + _codeMessage + "\r\n");
            _out.write("Content-Length: " + _contentLength + "\r\n");
            _out.write("Content-Type: " + _contentType + "\r\n");
            _out.write("\r\n");
            _out.write(_body + "\r\n");
            _out.write("\r\n");

        }else if(_command.equalsIgnoreCase("PUT")){
            _out.write(_version + " " + _code + " " + _codeMessage + "\r\n");
            _out.write("Content-location: " + _contentLocation + "\r\n");
            _out.write("\r\n");
        }else{

        }
        _out.flush();

    }

    private String readRequest(){

        String message = null;
        try {
            message = _in.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(message," ");
            while(stringTokenizer.hasMoreElements()){
                String element = (String)stringTokenizer.nextElement();
                if (element.equalsIgnoreCase("GET") || element.equalsIgnoreCase("PUT")) {
                    _command = element;
                    _filePath = (String) stringTokenizer.nextElement();
                    _version = (String) stringTokenizer.nextElement();
                }
            }
            decodeRequest();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void readGetRequest(){
        String line;
        try {

            StringBuilder header = new StringBuilder();
            do {
                line = _in.readLine();
                header.append(line + "\r\n");
            } while (!line.equalsIgnoreCase(""));
            _header = header.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readPutRequest(){

    }

    @Override
    public void run() {
        try {
            _out = new PrintWriter(_socket.getOutputStream());
            _in =  new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            do {
                readRequest();
            }while(true);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
