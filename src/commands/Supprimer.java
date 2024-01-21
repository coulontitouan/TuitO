package commands;

import java.io.PrintWriter;

import data.BDServeur;
import data.Message;

public class Supprimer extends Commande{

    private static final Supprimer instance = new Supprimer();

    private Supprimer(){
    }

    public static Supprimer getInstance() {
        return instance;
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        Integer id = Integer.parseInt(arguments[0]);
        Message message = BDServeur.getMessage(id);
        if (message != null){
            if (message.getAuteur().getPseudo().equals(pseudo)){
                BDServeur.supprimer(message);
                writer.write("Le message " + id + " a été supprimé.\n");
            }else{
                writer.write("Vous n'êtes pas l'auteur du message " + id + ".\n");
            }
        }else{
            writer.write("Le message " + id + " n'existe pas.\n");
        }
    }
}
