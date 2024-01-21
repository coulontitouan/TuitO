import java.net.ServerSocket;
import java.net.Socket;

import data.BDServeur;
import threads.CommandeServeur;
import threads.Sauvegarde;
import threads.ServeurHandler;

class Serveur {

    public static void main(String[] args) throws Exception{
        BDServeur.loadDB();
        ServerSocket serverSock = new ServerSocket(4441);
 
        Thread saveBDThread = new Thread(new Sauvegarde());
        Thread commandesThread = new Thread(new CommandeServeur(serverSock));
        saveBDThread.start();
        commandesThread.start();
        while(true){
            Socket clientSocket = serverSock.accept();
            Thread t = new Thread(new ServeurHandler(clientSocket));
            t.start();
        }
    }
}
