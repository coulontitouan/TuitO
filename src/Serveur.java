import java.net.ServerSocket;
import java.net.Socket;

import data.BDServeur;
import threads.CommandeServeur;
import threads.Sauvegarde;
import threads.ServeurHandler;
/* Le serveur */
class Serveur {

    public static void main(String[] args) throws Exception{
        /* charge la bd de json a java */
        BDServeur.loadDB();
        /* lance le serveur */
        ServerSocket serverSock = new ServerSocket(4444);
 
        /* lance la sauvegarde et l'ecoute des commandes sur le serv */
        Thread saveBDThread = new Thread(new Sauvegarde());
        Thread commandesThread = new Thread(new CommandeServeur(serverSock));
        saveBDThread.start();
        commandesThread.start();

        /* boucle infinie pour accepter les connexions */
        while(true){
            Socket clientSocket = serverSock.accept();
            Thread t = new Thread(new ServeurHandler(clientSocket));
            t.start();
        }
    }
}
