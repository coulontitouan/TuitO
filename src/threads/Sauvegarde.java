package src.threads;

import src.data.BDServeur;

public class Sauvegarde implements Runnable{
        public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
                System.out.println("Sauvegarde de la base de données...");
                BDServeur.saveDB();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
