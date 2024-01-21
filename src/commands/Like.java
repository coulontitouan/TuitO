package commands;

import java.io.PrintWriter;

import data.BDServeur;
import data.Message;

public class Like extends Commande{

    private static final Like instance = new Like();

    private Like(){
    }

    public static Like getInstance() {
        return instance;
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        Integer id = Integer.parseInt(arguments[0]);
        Message message = BDServeur.getMessage(id);
        if (message != null){
            if (message.getLikes().contains(pseudo)){
                writer.write("Vous avez déjà liké le message " + id + ".\n");
            }else{
                BDServeur.like(BDServeur.getUser(pseudo), message);
                writer.write("Vous avez liké le message " + id + ".\n");
            }
        }else{
            writer.write("Le message " + id + " n'existe pas.\n");
        }
    }
}
