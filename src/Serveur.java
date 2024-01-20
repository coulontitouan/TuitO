package src;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.data.BDServeur;

class Serveur {

    public static void main(String[] args) throws Exception{
        BDServeur.loadDB();
        ServerSocket serverSock = new ServerSocket(4444);
        Thread saveBD = new Thread(new Runnable(){
            public void run(){
                while(true){
                    try{
                        Thread.sleep(60000);
                        System.out.println("Sauvegarde de la base de données...");
                        BDServeur.saveDB();
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
        });
        saveBD.start();
        while(true){
            Socket clientSocket = serverSock.accept();
            Thread t = new Thread(new ClientHandler(clientSocket));
            t.start();
        }
        //BDServeur.saveDB();
    }
}
