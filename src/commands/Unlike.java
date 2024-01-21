package commands;

import java.io.PrintWriter;

import data.BDServeur;
import data.Message;

public class Unlike extends Commande{

    private static final Unlike instance = new Unlike();

    private Unlike(){
    }

    public static Unlike getInstance() {
        return instance;
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        for(String argument : arguments){
            Integer id = null;
            Message message = null;
            try {
                id = Integer.parseInt(argument);
                message = BDServeur.getMessage(id);
            } catch (Exception e) {
                //L'argument n'est pas un entier
            }
            if (message != null){
                if (message.getLikes().remove(pseudo)){
                    BDServeur.unlike(BDServeur.getUser(pseudo), message);
                    writer.write("Vous n'aimez plus le message " + id + ".\n");
                }else{
                    writer.write("Vous n'aimiez pas le message " + id + ".\n");
                }
            }else{
                writer.write("Le message " + id + " n'existe pas.\n");
            }
        }
    }
}
