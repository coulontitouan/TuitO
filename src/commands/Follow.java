package src.commands;

import java.io.PrintWriter;

import src.data.BDServeur;
import src.data.Utilisateur;

public class Follow extends Commande{

    private static final Follow instance = new Follow();

    private Follow(){
    }

    public static Follow getInstance() {
        return instance;
    }
    
    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        Utilisateur user = BDServeur.getUser(pseudo);
        String res = "";
        for (String argument : arguments){
            Utilisateur userToFollow = BDServeur.getUser(argument);
            if (userToFollow != null){
                if (userToFollow.getPseudo().equals(user.getPseudo())){
                    res += "Vous ne pouvez pas vous suivre vous-même.";
                    continue;
                }
                if (BDServeur.getAbonnements(user).contains(userToFollow.getPseudo())){
                    res += "Vous suivez déjà " + argument + ".";
                }
                else{
                    BDServeur.follow(userToFollow, user);
                    res += "Vous suivez maintenant " + argument + ".";
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
