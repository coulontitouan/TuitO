import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private String pseudo;
    private List<Message> messages;
    Utilisateur(String pseudo){
        this.pseudo = pseudo;
        this.messages = new ArrayList<>();
    }

    public String getPseudo(){
        return this.pseudo;
    }

    public void follow(String pseudo){
        BDServeur.getFollowers(this).add(pseudo);
    }

    public void unfollow(String pseudo){
        BDServeur.getFollowers(this).remove(pseudo);
    }

    public List<Message> getMessages(){
        return this.messages;
    }

    public void envoyerMessage(String contenu){
        Message message = new Message(this, contenu);
        this.messages.add(message);
    }

    public void ajouterMessage(Message message){
        this.messages.add(message);
    }

    @Override
    public String toString(){
        return this.pseudo;
    }

    @Override
    public int hashCode(){
        return this.pseudo.hashCode();
    }
    
}
