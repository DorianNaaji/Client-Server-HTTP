package server;

import com.sun.istack.internal.NotNull;

import java.net.Socket;

public class Communication implements Runnable{

    private Socket _socket;

    public Communication(@NotNull Socket socket){
        if (socket == null)
            throw new IllegalArgumentException("instance of Socket can not be null, Aliboron");
        _socket = socket;
    }

    @Override
    public void run() {

    }
}
