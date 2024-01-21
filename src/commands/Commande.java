package commands;

import java.io.PrintWriter;

public abstract class Commande{
    /* La classe mère des commandes en singleton */
    private static Commande instance;

    public static Commande getInstance() {
        return instance;
    }

    /* Le code de la commande */
    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        writer.write("Commande non reconnue\n");
    };
}
