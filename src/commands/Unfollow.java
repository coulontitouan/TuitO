package src.commands;

import java.io.PrintWriter;

import src.data.BDServeur;
import src.data.Utilisateur;

public class Unfollow extends Commande{

    private static final Unfollow instance = new Unfollow();

    private Unfollow(){
    }

    public static Unfollow getInstance() {
        return instance;
    }
    
    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        Utilisateur user = BDServeur.getUser(pseudo);
        String res = "";
        for (String argument : arguments){
            Utilisateur userToUnFollow = BDServeur.getUser(argument);
            if (userToUnFollow != null){
                if (BDServeur.getAbonnements(user).contains(userToUnFollow.getPseudo())){
                    BDServeur.unfollow(userToUnFollow, user);
                    res += "Vous êtes désabonné de " + argument + ".";
                }
                else{
                    res += "Vous ne suiviez pas " + argument + ".";
                }
            }
            else{
                res += "L'utilisateur " + argument + " n'existe pas.";
            }
            res += "\n";
        }
        writer.write(res);
    }
}
