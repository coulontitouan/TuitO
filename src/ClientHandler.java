package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientHandler implements Runnable {
    private Socket socket;
    private InputStreamReader stream;
    private BufferedReader reader;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.stream = new InputStreamReader(this.socket.getInputStream());
            this.reader = new BufferedReader(stream);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            String raw = reader.readLine();

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(raw);
            Map<String, Object> map = mapper.readValue(raw, Map.class);
            
            String pseudo = (String) map.get("user");
            String message = (String) map.get("content");

            BDServeur.ajouterMessage(pseudo, message);
            System.out.println(pseudo + " : " + message);
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
