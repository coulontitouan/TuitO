package commands;

import java.io.PrintWriter;

public abstract class Commande{
    private static Commande instance;

    public static Commande getInstance() {
        return instance;
    }

    public void execute(String pseudo, String[] arguments, PrintWriter writer) {
        writer.write("Commande non reconnue\n");
    };
}
