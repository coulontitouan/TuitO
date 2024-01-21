package threads;

import java.net.ServerSocket;
import java.util.Scanner;

import data.BDServeur;

public class CommandeServeur implements Runnable {

    private ServerSocket serverSock;

    public CommandeServeur(ServerSocket serverSock) {
        this.serverSock = serverSock;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String commande = scanner.nextLine();
                if (commande.startsWith("help")) {
                    System.out.println("Commandes disponibles :");
                    System.out.println("help : affiche ce message");
                    System.out.println("stop : arrête le serveur");
                    System.out.println("delete : supprime un message");
                    System.out.println("remove : supprime un utilisateur");
                    System.out.println("save : sauvegarde la base de données");
                } else if (commande.startsWith("stop")) {
                    System.out.println("Arrêt du serveur...");
                    BDServeur.saveDB();
                    scanner.close();
                    serverSock.close();
                    System.exit(0);
                } else if (commande.startsWith("delete")) {
                    try {
                        Integer id = Integer.parseInt(commande.split(" ")[1]);
                        BDServeur.adminDeleteMessage(id);
                    } catch (Exception e) {
                        System.out.println("Le message " + commande.split(" ")[1] + " n'a pas été trouvé.");
                    }
                } else if (commande.startsWith("remove")) {
                    try {
                        String pseudo = commande.split(" ")[1];
                        BDServeur.adminRemoveUser(pseudo);
                    } catch (Exception e) {
                        System.out.println("L'utilisateur " + commande.split(" ")[1] + " n'a pas été trouvé.");
                    }
                } else if (commande.startsWith("save")) {
                    System.out.println("Sauvegarde de la base de données...");
                    BDServeur.saveDB();
                } else {
                    System.out.println("Commande inconnue.");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}
