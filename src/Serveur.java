package src;

import java.net.ServerSocket;
import java.net.Socket;

class Serveur {
    public static void main(String[] args) throws Exception{
        BDServeur.loadDB();
        ServerSocket serverSock = new ServerSocket(4444);
        while(true){
            Socket clientSocket = serverSock.accept();
            Thread t = new Thread(new ClientHandler(clientSocket));
            t.start();
        }
        //BDServeur.saveDB();
    }
}
