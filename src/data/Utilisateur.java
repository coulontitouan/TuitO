
package data;

import java.util.ArrayList;
import java.util.List;

/* Un utilisateur */
public class Utilisateur {
      /* Le pseudo de l'utilisateur */
      private String pseudo;

      /* Les messages de l'utilisateur */
      private List<Message> messages;

      /**
       * Constructeur d'un utilisateur
       * @param pseudo le pseudo de l'utilisateur
       */
      public Utilisateur(String pseudo) {
            this.pseudo = pseudo;
            this.messages = new ArrayList<>();
      }

      /**
       * Getter du pseudo de l'utilisateur
       * @return le pseudo de l'utilisateur
       */
      public String getPseudo() {
            return this.pseudo;
      }

      /**
       * Permet de suivre une utilisateur
       */
      public void suivre(Utilisateur user) {
            BDServeur.getAbonnements(user).add(this.pseudo);
            BDServeur.getAbonnes(this).add(user.getPseudo());
      }

      /**
       * Permet de ne plus suivre un utilisateur
       */
      public void stop_suivre(Utilisateur user) {
            BDServeur.getAbonnements(user).remove(this.pseudo);
            BDServeur.getAbonnes(this).remove(user.getPseudo());
      }

      /**
       * Getter des messages de l'utilisateur
       * @return les messages de l'utilisateur
       */
      public List<Message> getMessages() {
            return this.messages;
      }

      /**
       * Envoye un message
       */
      public void envoyerMessage(String contenu) {
            this.messages.add(new Message(this, contenu));
      }

      /**
       * Ajoute un message à l'utilisateur ( pour la bdserveur )
       */
      public void ajouterMessage(Message message) {
            this.messages.add(message);
      }

      /**
       * Like un message
       */
      public void like(Message message) {
            message.getLikes().add(this.pseudo);
      }

      /**
       * Unlike un message
       */
      public void unlike(Message message) {
            message.getLikes().remove(this.pseudo);
      }

      /**
       * Le tostring pour l'affichage du /search user
       */
      @Override
      public String toString() {
            return pseudo + " : " + BDServeur.getAbonnes(this).size() + " followers, "
            + this.getMessages().size() + " messages, " + BDServeur.getAbonnements(this).size() + " abonnements";
      }

      /**
       * Le equals pour la comparaison des utilisateurs
       */
      @Override
      public int hashCode() {
            return this.pseudo.hashCode();
      }

}
