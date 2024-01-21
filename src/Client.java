
package src;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

import src.threads.ClientHandler;

class Client {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private String pseudo;
    private String ipServeur;
    private Integer port;
    public Scanner scanner = new Scanner(System.in);

    public void pseudo() {
        while (this.pseudo == null || this.pseudo.equals("")) {
            if (this.pseudo != null){
                System.out.println("Votre pseudo ne peut pas être vide.");
            }
            System.out.println("Entrez votre pseudo:");
            System.out.print(">>> ");
            this.pseudo = scanner.nextLine();
        }
    }

    public void ipServeur() {
        while (this.ipServeur == null || !this.ipServeur.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")){
            if (this.ipServeur != null){
                System.out.println("L'adresse IP n'est pas valide.");
            }
            System.out.println("Entrez l'adresse IP du serveur: ");
            System.out.print(">>> ");
            this.ipServeur = this.scanner.nextLine();
        }
    }

    public void portServeur() {
        while (this.port == null || this.port < 0 || this.port > 65535){
            if (this.port != null){
                System.out.println("Le port n'est pas valide.");
            }
            System.out.println("Entrez le port du serveur: ");
            System.out.print(">>> ");
            try {
                this.port = Integer.parseInt(this.scanner.nextLine());
            } catch (Exception e) {
                // Pas un integer donc on recommence
                this.port = -1;
            }
        }
    }

    public void sauvegarde() {
        try {
            FileWriter writer = new FileWriter(".client");
            writer.write(this.ipServeur + ":" + this.port + "\n");
            writer.write(this.pseudo);
            writer.close();
            System.out.println("Fichier de configuration sauvegardé.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde du fichier de configuration.");
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        if(new File(".client").exists()){
            System.out.println("Un fichier de configuration existe déjà, voulez-vous l'utiliser ? (y/n)");
            String reponse = null;
            while (reponse == null || !reponse.equals("y") && !reponse.equals("n")){
                if (reponse != null){
                    System.out.println("Veuillez répondre par y ou n.");
                }
                System.out.print(">>> ");
                reponse = client.scanner.nextLine();
            }
            if (reponse.equals("y")){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(".client"));
                    String ligne = reader.readLine();
                    String[] ipPort = ligne.split(":");
                    client.ipServeur = ipPort[0];
                    client.port = Integer.parseInt(ipPort[1]);
                    ligne = reader.readLine();
                    client.pseudo = ligne;
                    reader.close();
                } catch (Exception e) {
                    System.out.println("Erreur lors de la lecture du fichier de configuration.");
                }
            }
        }

        client.pseudo();
        client.ipServeur();
        client.portServeur();

        client.sauvegarde();

        int counter = 0;
        while (true) {
            try {
                Socket socket = new Socket(client.ipServeur, client.port);

                Thread t = new Thread(new ClientHandler(socket));
                t.start();

                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                Map<String, Object> message = new HashMap<>();
                message.put("id", null);
                message.put("user", client.pseudo);
                message.put("date", Client.dateFormat.format(new Date()));
                message.put("likes", 0);

                Thread.sleep(100); // Eviter que le client puisse spam les commandes
                System.out.print(">>> ");
                String contenu = client.scanner.nextLine();
                System.out.print(String.format("%1$s : %2$s -> ", client.pseudo, contenu));

                message.put("content", contenu);

                ObjectMapper objectMapper = new ObjectMapper();

                String json = objectMapper.writeValueAsString(message);

                writer.println(json);
                writer.flush();
            } catch (Exception e) {
                counter += 1;
                if (counter == 5){
                    System.out.println("Impossible de se connecter au serveur. Vérifiez l'adresse IP et le port. (" + client.ipServeur + ":" + client.port + ")");
                    System.err.println(e);
                    System.exit(1);
                }
            }
        }
    }

}