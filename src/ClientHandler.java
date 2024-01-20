package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import src.data.BDServeur;

public class ClientHandler implements Runnable {
    private Socket socket;
    private InputStreamReader stream;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientHandler(Socket socket) {
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

            ObjectMapper mapper = new ObjectMapper();
            System.out.println(raw);
            Map<String, Object> map = mapper.readValue(raw, Map.class);

            if (((String) map.get("content")).startsWith("/")) {
                switch ((String) map.get("content")) {
                    case "/help":
                        System.out.println("Liste des commandes :");
                        System.out.println("/help : Affiche la liste des commandes");
                        System.out.println("/quit : Quitte le programme");
                        System.out.println("/like : Like le dernier message");
                        System.out.println("/unlike : Ne plus like le dernier message");
                        break;
                    case "/quit":
                        System.exit(0);
                        break;
                    case "/like":
                        //like
                        break;
                    case "/unlike":
                        //unlike
                        break;
                    default:
                        System.out.println("Commande inconnue");
                        break;
                }

            }
            
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
