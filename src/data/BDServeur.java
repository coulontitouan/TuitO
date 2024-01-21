
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

public class BDServeur {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static Map<String, Utilisateur> listeUtilisateurs;

    public static Map<Integer, Message> listeMessages;

    private static Map<String, Set<String>> listeAbonnes;

    private static Map<String, Set<String>> listeAbonnements;

    public static Map<String, Commande> dico;

    public static Map<Utilisateur, Set<Message>> messageAEnvoyer;

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

    public static void saveDB() {
        // Creating a JSONObject object
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

    public static Utilisateur getUser(String pseudo) {
        return BDServeur.listeUtilisateurs.getOrDefault(pseudo, null);
    }

    public static Message getMessage(int id) {
        return BDServeur.listeMessages.getOrDefault(id, null);
    }

    public static Set<String> getAbonnes(Utilisateur user) {
        if (BDServeur.listeAbonnes.containsKey(user.getPseudo())) {
            return BDServeur.listeAbonnes.get(user.getPseudo());
        }
        return new HashSet<>();
    }

    public static Set<String> getAbonnements(Utilisateur user) {
        if (BDServeur.listeAbonnements.containsKey(user.getPseudo())) {
            return BDServeur.listeAbonnements.get(user.getPseudo());
        }
        return new HashSet<>();
    }

    public static int getIdMessage(Message message) {
        int id = Collections.max(listeMessages.keySet()) + 1;
        listeMessages.put(id, message);
        return id;
    }

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

    public static void supprimerMessage(String pseudo, int id) {
        Utilisateur user = BDServeur.getUser(pseudo);
        Message m = BDServeur.getMessage(id);
        if (m.getAuteur().equals(user)) {
            m.setStatut(1);
        }
    }

    public static void follow(Utilisateur user, Utilisateur abonne){
        user.suivre(abonne);
        abonne.suivre(user);
    }

    public static void unfollow(Utilisateur user, Utilisateur abonne){
        user.stop_suivre(abonne);
        abonne.stop_suivre(user);
    }

    public static void like(Utilisateur user, Message message){
        user.like(message);
    }

    public static void unlike(Utilisateur user, Message message){
        user.unlike(message);
    }

    public static List<Utilisateur> getUtilisateurs() {
        return new ArrayList<>(BDServeur.listeUtilisateurs.values());
    }

    public static void supprimer(Message message) {
        message.setStatut(Statut.SUPPRIME.getValue());
    }

    public static void adminDeleteMessage(Integer id) throws NullPointerException {
        listeMessages.get(id).setStatut(Statut.SUPPRIME.getValue());
    }

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

    public static void creeUtilisateur(String pseudo) {
        Utilisateur user = new Utilisateur(pseudo);
        listeUtilisateurs.put(pseudo, user);
        listeAbonnes.put(pseudo, new HashSet<>());
        listeAbonnements.put(pseudo, new HashSet<>());
    }

}
