
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

class Client {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static String pseudo;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez votre pseudo:");
        Client.pseudo = scanner.nextLine();
        while (Client.pseudo.equals("")) {
            System.out.println("Votre pseudo ne peut pas être vide.");
            System.out.println("Entrez votre pseudo:");
            Client.pseudo = scanner.nextLine();
        }
        System.out.println("Entrez l'adresse IP du serveur: (0.0.0.0)");
        String ip_serveur = scanner.nextLine();
        if (ip_serveur.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")){
            System.out.println("L'adresse IP est valide.");
        } else {
            System.out.println("L'adresse IP n'est pas valide.");
            System.out.println("Entrez l'adresse IP du serveur: (" + ip_serveur + ")");
        }
        System.out.println("Entrez le port du serveur: (4444)");
        int port = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            try {
                Socket socket = new Socket(ip_serveur, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                Map<String, Object> message = new HashMap<>();
                message.put("id", null);
                message.put("user", Client.pseudo);
                message.put("date", dateFormat.format(new Date()));
                message.put("likes", 0);

                System.out.print("Entrez votre message : ");
                String contenu = scanner.nextLine();

                if(contenu.startsWith("/")){
                    switch (contenu) {
                        case "/help":
                            System.out.println("Liste des commandes :");
                            System.out.println("/help : Affiche la liste des commandes");
                            System.out.println("/quit : Quitte le programme");
                            System.out.println("/like : Like le dernier message");
                            System.out.println("/unlike : Ne plus like le dernier message");
                            System.out.println("/follow : Suivre un utilisateur");
                            System.out.println("/unfollow : Ne plus suivre un utilisateur");
                            System.out.println("/list : Affiche la liste des utilisateurs");
                            System.out.println("/listfollow : Affiche la liste des utilisateurs suivis");
                            System.out.println("/listfollower : Affiche la liste des utilisateurs qui vous suivent");
                            System.out.println("/listmessage : Affiche la liste des messages");
                            System.out.println("/listmessagefollow : Affiche la liste des messages des utilisateurs suivis");
                            System.out.println("/listmessagefollower : Affiche la liste des messages des utilisateurs qui vous suivent");
                            System.out.println("/listmessageuser : Affiche la liste des messages d'un utilisateur");
                            break;
                        default:
                            break;
                    }
                }

                message.put("content", scanner.nextLine());

                ObjectMapper objectMapper = new ObjectMapper();

                String json = objectMapper.writeValueAsString(message);
                System.out.println(json);

                writer.println(json);
                writer.flush();
                socket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
