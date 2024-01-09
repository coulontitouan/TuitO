package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private InputStreamReader stream;
    private BufferedReader reader;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.stream = new InputStreamReader(this.socket.getInputStream());
            this.reader = new BufferedReader(stream);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public void run(){
        try{
            String[] raw = reader.readLine().split(":", 2);
            String pseudo = raw[0];
            String message = raw[1];
            System.out.println(pseudo + " : " + message);
            this.socket.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
