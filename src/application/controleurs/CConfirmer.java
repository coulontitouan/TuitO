
package application.controleurs;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileWriter;

import application.ClientJFX;
import javafx.event.ActionEvent;

public class CConfirmer implements EventHandler<ActionEvent> {
    private ClientJFX client;
    private TextField pseudoTextField;
    private TextField ipTextField;
    private TextField portTextField;

    public CConfirmer(ClientJFX client, TextField pseudoTextField, TextField ipTextField, TextField portTextField) {
        this.client = client;
        this.pseudoTextField = pseudoTextField;
        this.ipTextField = ipTextField;
        this.portTextField = portTextField;
    }

    /* Permet de gerer le click sur le bouton */
    @Override
    public void handle(ActionEvent event) {
        client.ipServeur = ipTextField.getText();
        client.port = Integer.parseInt(portTextField.getText());
        client.pseudo = pseudoTextField.getText();
        client.connecte();
        
        try {
            FileWriter writer = new FileWriter(".client");
            writer.write(client.ipServeur + ":" + client.port + "\n");
            writer.write(client.pseudo);
            writer.close();
            System.out.println("Fichier de configuration sauvegardé.");
        } catch (Exception e) {
            System.out.println("Erreur lors de la sauvegarde du fichier de configuration.");
        }
    }
}
