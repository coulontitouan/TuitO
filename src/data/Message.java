
package src.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {

      private int id;

      private Utilisateur auteur;

      private String contenu;

      private Date date;

      private List<String> likes;

      private Statut statut;

      public Message(Utilisateur auteur, String contenu) {
            this.id = BDServeur.getIdMessage(this);
            this.auteur = auteur;
            this.contenu = contenu;
            this.date = new Date();
            this.likes = new ArrayList<>();
            this.statut = Statut.CREE;
      }

      public Message(int id, Utilisateur auteur, String contenu, Date date, List<String> likes, Statut statut) {
            this.id = id;
            this.auteur = auteur;
            this.contenu = contenu;
            this.date = date;
            this.likes = likes;
            this.statut = statut;
      }

      public int getId() {
            return this.id;
      }

      public Utilisateur getAuteur() {
            return this.auteur;
      }

      public String getContenu() {
            return this.contenu;
      }

      public String getDateFormat() {
            Calendar cal = Calendar.getInstance(new Locale("fr", "FR"));
            cal.setTime(this.date);
            String res = String.format("%1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS", cal);
            return res;
      }

      public String getDate() {
            return BDServeur.dateFormat.format(this.date);
      }

      public List<String> getLikes() {
            return this.likes;
      }

      public Statut getStatut() {
            return this.statut;
      }

      public String toJSON() {
            Map<String, Object> message = new HashMap<>();
            message.put("id", this.getId());
            message.put("user", this.getAuteur().getPseudo());
            message.put("date", this.getDate());
            message.put("likes", this.getLikes().size());
            message.put("content", this.getContenu());
            message.put("statut", this.getStatut().getValue());
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                  while (json.equals("")) {
                        json = objectMapper.writeValueAsString(message);
                  }
            } catch (JsonProcessingException e) {
                  System.out.println(e);
            }
            return json;
      }

      public void setStatut(int i) {
            this.statut = Statut.fromValue(i);
      }

      @Override
      public String toString() {
            return this.contenu + this.date;
      }

      @Override
      public int hashCode() {
            return Integer.valueOf(this.id).hashCode() + this.auteur.hashCode();
      }
}
