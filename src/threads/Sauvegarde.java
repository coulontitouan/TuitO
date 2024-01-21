package threads;

import data.BDServeur;

public class Sauvegarde implements Runnable {
    /* Permet de sauvegarder la base de données toutes les minutes */
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
