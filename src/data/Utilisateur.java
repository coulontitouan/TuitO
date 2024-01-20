
package src.data;

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

  public void suivre(String pseudo) {
        BDServeur.getSuivis(this).add(pseudo);
  }

  public void stop_suivre(String pseudo) {
        BDServeur.getSuiveurs(this).remove(pseudo);
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

  @Override
  public String toString() {
        return this.pseudo;
  }

  @Override
  public int hashCode() {
        return this.pseudo.hashCode();
  }

}
