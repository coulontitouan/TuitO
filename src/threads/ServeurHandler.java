package threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import commands.*;
import data.BDServeur;

public class ServeurHandler extends Handler {
    public ServeurHandler(Socket socket) {
        super(socket);
    }

    /* permet de lire la socket avec le client et d'executer les actions en continue avec plusiseurs clients */
    public void run() {
        try {
            String raw = super.reader.readLine();

            ObjectMapper mapper = new ObjectMapper();
            if (raw == null) {
                return;
            }
            Map<String, Object> map = mapper.readValue(raw, Map.class);

            String pseudo = (String) map.get("user");
            String message = (String) map.get("content");

            if (((String) map.get("content")).startsWith("/")) {
                String content = (String) map.get("content");
                if (content.indexOf(" ") == -1) {
                    content += " ";
                }
                String commande = content.substring(1, content.indexOf(" "));
                String[] arguments = content.substring(content.indexOf(" ") + 1).split(" ");

                this.writer = new PrintWriter(socket.getOutputStream());
                if (commande.equals("reception")) {
                    Reception.getInstance().execute(pseudo, arguments, writer);
                }else{
                    BDServeur.dico.getOrDefault(commande, Help.getInstance()).execute(pseudo, arguments, writer);
                }

            } else {
                if (!BDServeur.listeUtilisateurs.containsKey(pseudo)) {
                    BDServeur.creeUtilisateur(pseudo);
                }
                BDServeur.ajouterMessage(pseudo, message);
                System.out.println(pseudo + " : " + message);
                this.writer = new PrintWriter(socket.getOutputStream());
                this.writer.println("Message reçu");
            }

            this.writer.flush();
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
