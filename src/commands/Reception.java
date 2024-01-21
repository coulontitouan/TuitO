package commands;

import java.io.PrintWriter;
import java.util.Set;

import data.BDServeur;
import data.Message;

public class Reception extends Commande {

    private static final Reception instance = new Reception();

    /* La commande permettant de récuperer tout les messages en attente */
    private Reception() {
    }

    public static Reception getInstance() {
        return instance;
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        String res = "";
        Set<Message> listeMessages = BDServeur.messageAEnvoyer.get(BDServeur.getUser(pseudo));
        if (listeMessages != null) {
            for (Message m : listeMessages) {
                res += m.toJSON() + "\n";
            }
        }else{
            res = "Aucun message à recevoir.\n";
        }
        writer.print(res);
    }
}
