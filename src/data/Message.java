
package data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/* Un message */
public class Message {
      /* L'identifiant du message */
      private int id;

      /* L'auteur du message */
      private Utilisateur auteur;

      /* Le contenu du message */
      private String contenu;

      /* La date du message */
      private Date date;

      /* Les pseudos des utilisateurs ayant liké le message */
      private List<String> likes;

      /* Le statut du message */
      private Statut statut;

      /**
       * Constructeur d'un message
       * @param auteur l'auteur du message
       * @param contenu le contenu du message
       */
      public Message(Utilisateur auteur, String contenu) {
            this.id = BDServeur.getIdMessage(this);
            this.auteur = auteur;
            this.contenu = contenu;
            this.date = new Date();
            this.likes = new ArrayList<>();
            this.statut = Statut.CREE;
      }

      /**
       * Constructeur d'un message
       * @param id l'identifiant du message
       * @param auteur l'auteur du message
       * @param contenu le contenu du message
       * @param date la date du message
       * @param likes les pseudos des utilisateurs ayant liké le message
       * @param statut le statut du message
       */
      public Message(int id, Utilisateur auteur, String contenu, Date date, List<String> likes, Statut statut) {
            this.id = id;
            this.auteur = auteur;
            this.contenu = contenu;
            this.date = date;
            this.likes = likes;
            this.statut = statut;
      }

      /**
       * Getter de l'identifiant du message
       * @return l'identifiant du message
       */
      public int getId() {
            return this.id;
      }

      /**
       * Getter de l'auteur du message
       * @return l'auteur du message
       */
      public Utilisateur getAuteur() {
            return this.auteur;
      }

      /**
       * Getter du contenu du message
       * @return le contenu du message
       */
      public String getContenu() {
            return this.contenu;
      }

      /**
       * Getter de la date du message formattée pour l'affichage
       * @return la date du message formattée
       */
      public String getDateFormat() {
            Calendar cal = Calendar.getInstance(new Locale("fr", "FR"));
            cal.setTime(this.date);
            String res = String.format("%1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS", cal);
            return res;
      }

      /**
       * Getter de la date du message
       * @return la date du message
       */
      public String getDate() {
            return BDServeur.dateFormat.format(this.date);
      }

      /**
       * Getter des pseudos des utilisateurs ayant liké le message
       * @return les pseudos des utilisateurs ayant liké le message
       */
      public List<String> getLikes() {
            return this.likes;
      }

      /**
       * Getter du statut du message
       * @return le statut du message
       */
      public Statut getStatut() {
            return this.statut;
      }

      /**
       * Permet d'envoyer le message en JSON pour echange avec clients
       * @return le message en JSON
       */
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

      /**
       * Setteur du statut du message
       * @param i le nouveau statut du message
       */
      public void setStatut(int i) {
            this.statut = Statut.fromValue(i);
      }

      /**
       * Le tostring de la classe
       */
      @Override
      public String toString() {
            return this.contenu + this.date;
      }

      /**
       * Le hash pour la methode equals
       */
      @Override
      public int hashCode() {
            return Integer.valueOf(this.id).hashCode() + this.auteur.hashCode();
      }
}
