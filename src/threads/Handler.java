package src.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Handler implements Runnable {
    protected Socket socket;
    protected InputStreamReader stream;
    protected PrintWriter writer;
    protected BufferedReader reader;

    public Handler(Socket socket) {
        try {
            this.socket = socket;
            this.stream = new InputStreamReader(this.socket.getInputStream());
            this.writer = new PrintWriter(this.socket.getOutputStream());
            this.reader = new BufferedReader(stream);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            String raw = reader.readLine();
            while(raw != null) {
                System.out.println(raw);
                raw = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }
}
