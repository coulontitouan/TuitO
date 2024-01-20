
package src.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Message {
  private int id;

  private Utilisateur auteur;

  private String contenu;

  private Date date;

  private List<String> likes;

  private int statut;

  public Message(Utilisateur auteur, String contenu) {
        this.id = BDServeur.getIdMessage(this);
        this.auteur = auteur;
        this.contenu = contenu;
        this.date = new Date();
        this.likes = new ArrayList<>();
        this.statut = 0;
  }

  public Message(int id, Utilisateur auteur, String contenu, Date date, List<String> likes, int statut) {
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

  public Date getDate() {
        return this.date;
  }

  public List<String> getLikes() {
        return this.likes;
  }

  public int getStatut() {
        return this.statut;
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
