package server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

public class Communication implements Runnable
{

    private Socket _socket;
    private PrintWriter _out;
    private BufferedReader _in;

    public Communication(Socket socket)
    {
        if (socket == null)
        {
            throw new IllegalArgumentException("instance of Socket can not be null, Aliboron");
        }
        _socket = socket;
    }

    private void put(String message)
    {

    }

    private void get(String filePath, String protocol)
    {
        // check protocol
        // ouvre le fichier s'il existe
        // ecrit le contenue du fichier dans le out
        // flush

        if (!protocol.startsWith("HTTP/1.1"))
        {
            buildStatus(400);
        }
        else
        {
            try
            {
                FileInputStream fileInputStream = new FileInputStream("C:\\www\\" + filePath);
                buildStatus(200);
                _out.write("\r\n");
                readFile(fileInputStream);
            }
            catch (FileNotFoundException e)
            {
                buildStatus(404);
            }
        }
        _out.write("\r\n\r\n");
        _out.flush();

    }

    private void readFile(FileInputStream fileInputStream)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String ligne;
        try
        {
            while ((ligne = reader.readLine()) != null)
            {
                _out.write(ligne + "\n\r");
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void buildStatus(int code)
    {
        if (code == 200)
        {
            _out.write("HTTP/1.1 200 OK\r\n");
        }
        else if (code == 400)
        {
            _out.write("HTTP/1.1 400 bad request\r\n");
        }
        else if (code == 404)
        {
            _out.write("HTTP/1.1 404 resource not found\r\n");
        }
    }

    private String interprate(String message)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(message, " ");
        while (stringTokenizer.hasMoreElements())
        {
            String element = (String) stringTokenizer.nextElement();
            if (element.equalsIgnoreCase("GET"))
            {
                String filePath = (String) stringTokenizer.nextElement(); // check si prochiane ele existe
                String protocol = (String) stringTokenizer.nextElement(); // check si prochiane ele existe
                get(filePath, protocol);
            }
            else if (element.equalsIgnoreCase("PUT"))
            {

            }
            else
            {

            }
        }
        return null;
    }

    @Override
    public void run()
    {
        try
        {
            _out = new PrintWriter(_socket.getOutputStream());
            _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));

            while (true)
            {
                interprate(_in.readLine());
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
