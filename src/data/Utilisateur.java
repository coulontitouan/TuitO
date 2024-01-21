
package data;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
      private String pseudo;

      private List<Message> messages;

      public Utilisateur(String pseudo) {
            this.pseudo = pseudo;
            this.messages = new ArrayList<>();
      }

      public String getPseudo() {
            return this.pseudo;
      }

      public void suivre(Utilisateur user) {
            BDServeur.getAbonnements(user).add(this.pseudo);
            BDServeur.getAbonnes(this).add(user.getPseudo());
      }

      public void stop_suivre(Utilisateur user) {
            BDServeur.getAbonnements(user).remove(this.pseudo);
            BDServeur.getAbonnes(this).remove(user.getPseudo());
      }

      public List<Message> getMessages() {
            return this.messages;
      }

      public void envoyerMessage(String contenu) {
            this.messages.add(new Message(this, contenu));
      }

      public void ajouterMessage(Message message) {
            this.messages.add(message);
      }

      public void like(Message message) {
            message.getLikes().add(this.pseudo);
      }

      public void unlike(Message message) {
            message.getLikes().remove(this.pseudo);
      }

      @Override
      public String toString() {
            return pseudo + " : " + BDServeur.getAbonnes(this).size() + " followers, "
            + this.getMessages().size() + " messages, " + BDServeur.getAbonnements(this).size() + " abonnements";
      }

      @Override
      public int hashCode() {
            return this.pseudo.hashCode();
      }

}
