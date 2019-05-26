package server;

import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Communication implements Runnable{

    private Socket _socket;
    private OutputStream _out;
    private InputStream _in;

    public Communication(@NotNull Socket socket){
        if (socket == null)
            throw new IllegalArgumentException("instance of Socket can not be null, Aliboron");
        _socket = socket;
    }

    private void put(){

    }

    private void get(){

    }

    @Override
    public void run() {
        try {
            _out = _socket.getOutputStream();
            _in = _socket.getInputStream();

            while (true){
                System.out.println(_socket.getLocalPort() + " " + _socket.getInetAddress().toString() + " " + _socket.getPort());
                byte[] b = new byte[5000];
                _in.read(b);
                System.out.println(new String(b));
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
