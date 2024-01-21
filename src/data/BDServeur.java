
package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import commands.Commande;

/* La BD du serveur permettant de faire les requetes et de stocker toutes les instances des messages et utilisateurs */
public class BDServeur {
    /* Pour stocker la date en format iso 8601 */
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /* la liste de tout les utilisateurs */
    public static Map<String, Utilisateur> listeUtilisateurs;

    /* la liste de tout les messages */
    public static Map<Integer, Message> listeMessages;

    /* la liste des abonnés de chaque utilisateur */
    private static Map<String, Set<String>> listeAbonnes;

    /* la liste des abonnements de chaque utilisateur */
    private static Map<String, Set<String>> listeAbonnements;

    /* le dictionnaire des commandes */
    public static Map<String, Commande> dico;

    /* la liste des messages à envoyer à chaque utilisateur */
    public static Map<Utilisateur, Set<Message>> messageAEnvoyer;

    /* Instancie toutes les variables */
    private static void creeVariables(){
        BDServeur.listeUtilisateurs = new HashMap<>();
        BDServeur.listeMessages = new HashMap<>();
        BDServeur.listeAbonnes = new HashMap<>();
        BDServeur.listeAbonnements = new HashMap<>();

        dico = new HashMap<>();
        dico.put("search", commands.Search.getInstance());
        dico.put("help", commands.Help.getInstance());
        dico.put("follow", commands.Follow.getInstance());
        dico.put("unfollow", commands.Unfollow.getInstance());
        dico.put("like", commands.Like.getInstance());
        dico.put("unlike", commands.Unlike.getInstance());
        dico.put("reception", commands.Reception.getInstance());
        dico.put("supprimer", commands.Supprimer.getInstance());

        messageAEnvoyer = new HashMap<>();
    }

    /* Charge la BD depuis le fichier BD.json en utilisant jackson */
    public static void loadDB() {
        BDServeur.creeVariables();

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Map<String, Object>> map = mapper.readValue(new File("BD.json"),
                    new TypeReference<Map<String, Map<String, Object>>>() {
                    });

            for (String pseudo : map.keySet()) {

                Utilisateur user = new Utilisateur(pseudo);
                listeUtilisateurs.put(pseudo, user);
                if (!listeAbonnes.containsKey(pseudo)){
                    listeAbonnes.put(pseudo, new HashSet<>());
                }
                listeAbonnements.put(pseudo, new HashSet<>());

                Map<String, Map<String, Object>> messages = (Map<String, Map<String, Object>>) map.get(pseudo).get("messages");

                List<String> abonnements = (List<String>) map.get(pseudo).get("abonnements");

                for (String id : messages.keySet()) {

                    Integer idInteger = Integer.parseInt(id);

                    String contenu = (String) messages.get(id).get("content");

                    String dateString = (String) messages.get(id).get("date");
                    Date dateDate = dateFormat.parse(dateString);

                    List<String> likes = new ArrayList<>();
                    for (String u : (List<String>) messages.get(id).get("likes")) {
                        likes.add(u);
                    }

                    Statut statut = Statut.fromValue((int) messages.get(id).get("statut"));

                    Message message = new data.Message(idInteger, user, contenu, dateDate, likes, statut);
                    listeMessages.put(idInteger, message);
                    user.ajouterMessage(message);
                }

                for (String s : abonnements) {
                    listeAbonnements.get(pseudo).add(s);
                    if(!listeAbonnes.containsKey(s)){
                        listeAbonnes.put(s, new HashSet<>());
                    }
                    listeAbonnes.get(s).add(pseudo);
                }
                
            }
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier n'a pas été trouvé...");
        } catch (IOException e) {
            System.err.println("Le fichier n'a pas pu être lu...");
        } catch (java.text.ParseException e) {
            System.err.println("Erreur dans le format de la date.");
        }
    }

    /* Sauvegarde la BD dans le fichier BD.json en utilisant json simple */
    public static void saveDB() {
        JSONObject fichierFinal = new JSONObject();
        for (String pseudo : listeUtilisateurs.keySet()) {
            JSONObject user = new JSONObject();
            JSONObject messages = new JSONObject();
            for (Message m : BDServeur.getUser(pseudo).getMessages()) {
                JSONObject message = new JSONObject();
                message.put("content", m.getContenu());
                message.put("date", m.getDate());
                message.put("likes", m.getLikes());
                message.put("statut", m.getStatut().getValue());
                messages.put(m.getId(), message);
            }
            user.put("messages", messages);
            user.put("abonnements", new ArrayList<>(listeAbonnements.get(pseudo)));
            fichierFinal.put(pseudo, user);
        }
        try {
            FileWriter file = new FileWriter("BD.json");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(fichierFinal);

            file.write(json.toString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Retourne l'utilisateur correspondant au pseudo */
    public static Utilisateur getUser(String pseudo) {
        return BDServeur.listeUtilisateurs.getOrDefault(pseudo, null);
    }

    /* Retourne le message correspondant à l'id */
    public static Message getMessage(int id) {
        return BDServeur.listeMessages.getOrDefault(id, null);
    }

    /* Retourne l'ensemble des abonnes d'un utilisateur */
    public static Set<String> getAbonnes(Utilisateur user) {
        if (BDServeur.listeAbonnes.containsKey(user.getPseudo())) {
            return BDServeur.listeAbonnes.get(user.getPseudo());
        }
        return new HashSet<>();
    }

    /* Retourne l'ensemble des abonnements d'un utilisateur */
    public static Set<String> getAbonnements(Utilisateur user) {
        if (BDServeur.listeAbonnements.containsKey(user.getPseudo())) {
            return BDServeur.listeAbonnements.get(user.getPseudo());
        }
        return new HashSet<>();
    }

    /* Place un message dans la bd avec son id */
    public static int getIdMessage(Message message) {
        int id = Collections.max(listeMessages.keySet()) + 1;
        listeMessages.put(id, message);
        return id;
    }

    /* Poste un message */
    public static void ajouterMessage(String pseudo, String message) {
        Utilisateur user = BDServeur.getUser(pseudo);
        Message m = new Message(user, message);
        user.ajouterMessage(m);
        listeMessages.put(m.getId(), m);
        for (String s : listeAbonnes.get(pseudo)) {
            if (!messageAEnvoyer.containsKey(BDServeur.getUser(s))) {
                messageAEnvoyer.put(BDServeur.getUser(s), new HashSet<>());
            }
            messageAEnvoyer.get(BDServeur.getUser(s)).add(m);
        }
    }

    /* Fait suivre abonne à user */
    public static void follow(Utilisateur user, Utilisateur abonne){
        user.suivre(abonne);
        abonne.suivre(user);
    }

    /* Arrete de faire suivre abonne à user */
    public static void unfollow(Utilisateur user, Utilisateur abonne){
        user.stop_suivre(abonne);
        abonne.stop_suivre(user);
    }

    /* Fait liker un message à un utilisateur */
    public static void like(Utilisateur user, Message message){
        user.like(message);
    }

    /* Fait unliker un message à un utilisateur */
    public static void unlike(Utilisateur user, Message message){
        user.unlike(message);
    }

    /* Retourne la liste des utilisateurs */
    public static List<Utilisateur> getUtilisateurs() {
        return new ArrayList<>(BDServeur.listeUtilisateurs.values());
    }

    /* supprime un message */
    public static void supprimer(Message message) {
        message.setStatut(Statut.SUPPRIME.getValue());
    }

    /* Permet à l'admin de supprimer un message */
    public static void adminDeleteMessage(Integer id) throws NullPointerException {
        listeMessages.get(id).setStatut(Statut.SUPPRIME.getValue());
    }

    /* Permet à l'admin de supprimer un utilisateur et tout ses messages */
    public static void adminRemoveUser(String pseudo) throws NullPointerException{
        listeUtilisateurs.remove(pseudo);
        for(String s : listeAbonnes.get(pseudo)){
            listeAbonnements.get(s).remove(pseudo);
        }
        for(String s : listeAbonnements.get(pseudo)){
            listeAbonnes.get(s).remove(pseudo);
        }
        listeAbonnes.remove(pseudo);
        listeAbonnements.remove(pseudo);
        for(Message m : listeMessages.values()){
            if(m.getAuteur().getPseudo().equals(pseudo)){
                m.setStatut(Statut.SUPPRIME.getValue());
            }
        }
    }

    /* Permet de créer un utilisateur */
    public static void creeUtilisateur(String pseudo) {
        Utilisateur user = new Utilisateur(pseudo);
        listeUtilisateurs.put(pseudo, user);
        listeAbonnes.put(pseudo, new HashSet<>());
        listeAbonnements.put(pseudo, new HashSet<>());
    }

}
