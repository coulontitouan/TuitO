package src.commands;

import java.io.PrintWriter;

public class Help extends Commande{

    private static final Help instance = new Help();

    private Help(){
    }

    public static Help getInstance() {
        return instance;
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        writer.write("Liste des commandes :\n"
        + "/help : affiche la liste des commandes\n"
        + "/whoami : affiche des informations sur l'utilisateur\n"
        + "/list : affiche la liste des utilisateurs connectés\n"
        + "/msg <pseudo> <message> : envoie un message privé à l'utilisateur <pseudo>\n"
        + "/like <id> : ajoute un like au message <id>\n"
        + "/unlike <id> : retire un like au message <id>\n"
        + "/follow <pseudo> : suit l'utilisateur <pseudo>\n"
        + "/unfollow <pseudo> : ne suit plus l'utilisateur <pseudo>\n");
    }
}
