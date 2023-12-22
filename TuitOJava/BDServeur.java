import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.HashSet;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BDServeur {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static Map<String, Utilisateur> listeUtilisateurs;
    private static Map<Integer, Message> listeMessages;
    private static Map<String, Set<String>> listeFollowers;
    private static Map<String, Set<String>> listeFolloweds;

    public static void loadDB() {
        BDServeur.listeUtilisateurs = new HashMap<>();
        BDServeur.listeMessages = new HashMap<>();
        BDServeur.listeFollowers = new HashMap<>();
        BDServeur.listeFolloweds = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Map<String, Object>> map = mapper.readValue(new File("BD.json"),
                    new TypeReference<Map<String, Map<String, Object>>>() {
                    });

            for (String pseudo : map.keySet()) {

                Utilisateur user = new Utilisateur(pseudo);
                listeUtilisateurs.put(pseudo, user);
                listeFollowers.put(pseudo, new HashSet<>());
                listeFolloweds.put(pseudo, new HashSet<>());

                Map<String, Map<String, Object>> messages = (Map<String, Map<String, Object>>) map.get(pseudo).get("messages");

                List<String> follows = (List<String>) map.get(pseudo).get("follows");

                for (String id : messages.keySet()) {

                    Integer idInteger = Integer.parseInt(id);

                    String contenu = (String) messages.get(id).get("content");

                    String dateString = (String) messages.get(id).get("date");
                    Date dateDate = dateFormat.parse(dateString);

                    List<String> likes = new ArrayList<>();
                    for (String u : (List<String>) messages.get(id).get("likes")) {
                        likes.add(u);
                    }

                    int statut = (int) messages.get(id).get("statut");

                    Message message = new Message(idInteger, user, contenu, dateDate, likes, statut);
                    listeMessages.put(idInteger, message);
                    user.ajouterMessage(message);
                }

                for (String s : follows) {
                    listeFolloweds.get(pseudo).add(s);
                    if (!listeFollowers.containsKey(s)) {
                        listeFollowers.put(s, new HashSet<>());
                    }
                    listeFollowers.get(s).add(pseudo);
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
                message.put("date", dateFormat.format(m.getDate()));
                message.put("likes", m.getLikes());
                message.put("statut", m.getStatut());
                messages.put(m.getId(), message);
            }
            user.put("messages", messages);
            user.put("follows", new ArrayList<>(listeFolloweds.get(pseudo)));
            fichierFinal.put(BDServeur.getUser(pseudo), user);
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
        return BDServeur.listeUtilisateurs.get(pseudo);
    }

    public static Message getMessage(int id) {
        return BDServeur.listeMessages.get(id);
    }

    public static Set<String> getFollowers(Utilisateur user) {
        if (BDServeur.listeFollowers.containsKey(user.getPseudo())) {
            return BDServeur.listeFollowers.get(user.getPseudo());
        }
        return new HashSet<>();
    }

    public static Set<String> getFolloweds(Utilisateur user) {
        if (BDServeur.listeFolloweds.containsKey(user.getPseudo())) {
            return BDServeur.listeFolloweds.get(user.getPseudo());
        }
        return new HashSet<>();
    }
}
