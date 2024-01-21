package commands;

import java.io.PrintWriter;

import data.BDServeur;
import data.Utilisateur;
import data.Message;

public class Search extends Commande {

    private static final Search instance = new Search();

    /* La commande permettant de chercher un utilisateur ou un message */
    private Search() {
    }

    public static Search getInstance() {
        return instance;
    }

    private void message(String pseudo, String[] arguments, PrintWriter writer) {
        if (arguments.length == 1) {
            for (Message m : BDServeur.listeMessages.values()) {
                writer.write(m.toJSON() + "\n");
            }
        } else if (arguments.length == 2) {
            Integer id = null;
            Message message = null;
            try {
                id = Integer.parseInt(arguments[1]);
                message = BDServeur.getMessage(id);
            } catch (Exception e) {
                // L'argument n'est pas un entier
            }
            if (message != null) {
                writer.write(message.toJSON() + "\n");
            } else {
                writer.write("Le message " + arguments[1] + " n'existe pas.\n");
            }
        } else if (arguments.length == 3) {
            Integer id = null;
            Message message = null;
            try {
                id = Integer.parseInt(arguments[1]);
                message = BDServeur.getMessage(id);
            } catch (Exception e) {
                // L'argument n'est pas un entier
            }
            if (message != null) {
                if (arguments[2].equals("user")) {
                    writer.write(message.getAuteur().getPseudo() + "\n");
                } else if (arguments[2].equals("date")) {
                    writer.write(message.getDateFormat() + "\n");
                } else if (arguments[2].equals("content")) {
                    writer.write(message.getContenu() + "\n");
                } else if (arguments[2].equals("likes")) {
                    writer.write(message.getLikes().toString() + "\n");
                } else {
                    writer.write(
                            "Usage : search message [id] <likes (likes d'un message) | (juste message [id] pour afficher le contenu d'un message)>\n");
                }
            } else {
                writer.write("Le message " + arguments[1] + " n'existe pas.\n");
            }
        } else {
            writer.write(
                    "Usage : search message [id] <likes (likes d'un message) | (juste message [id] pour afficher le contenu d'un message)>\n");
        }
    }

    private void user(String pseudo, String[] arguments, PrintWriter writer) {
        if (arguments.length == 1) {
            for (Utilisateur u : BDServeur.listeUtilisateurs.values()) {
                writer.write(u.getPseudo() + "\n");
            }
        } else if (arguments.length == 2) {
            Utilisateur user = BDServeur.getUser(arguments[1]);
            if (user != null) {
                writer.write(user.toString() + "\n");
            } else {
                writer.write("L'utilisateur " + arguments[1] + " n'existe pas.\n");
            }
        } else if (arguments.length == 3) {
            Utilisateur user = BDServeur.getUser(arguments[1]);
            if (user != null) {
                if (arguments[2].equals("messages")) {
                    for (Message m : user.getMessages()) {
                        writer.write(m.toJSON() + "\n");
                    }
                } else if (arguments[2].equals("abonnements")) {
                    writer.write(BDServeur.getAbonnements(user) + "\n");
                } else if (arguments[2].equals("abonnes")) {
                    writer.write(BDServeur.getAbonnes(user) + "\n");
                } else {
                    writer.write(
                            "Usage : search user [pseudo] <messages (messages d'un utilisateur) | abonnements (abonnements d'un utilisateur) | abonnes (abonnes d'un utilisateur)>\n");
                }
            } else {
                writer.write("L'utilisateur " + arguments[1] + " n'existe pas.\n");
            }
        } else {
            writer.write(
                    "Usage : search user [pseudo] <messages (messages d'un utilisateur) | abonnements (abonnements d'un utilisateur) | abonnes (abonnes d'un utilisateur)>\n");
        }
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        if (arguments[0].equals("message")) {
            message(pseudo, arguments, writer);
        } else if (arguments[0].equals("user")) {
            user(pseudo, arguments, writer);
        } else {
            writer.write("Argument invalide.\n" +
                    "Usage : search <message (tout les messages) | user (tout les utilisateurs)>\n"
                    + "search user [pseudo] <messages (messages d'un utilisateur) | abonnements (abonnements d'un utilisateur) | abonnes (abonnes d'un utilisateur)>\n"
                    + "search message [id] <likes (likes d'un message) | (juste message [id] pour afficher le contenu d'un message)>\n");
        }
    }
}
