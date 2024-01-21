package application.controleurs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CFichier implements EventHandler<ActionEvent> {
    private TextField pseudoTextField;
    private TextField ipTextField;
    private TextField portTextField;

    public CFichier(TextField pseudoTextField, TextField ipTextField, TextField portTextField) {
        this.pseudoTextField = pseudoTextField;
        this.ipTextField = ipTextField;
        this.portTextField = portTextField;
    }

    @Override
    public void handle(ActionEvent event) {
        if (new File(".client").exists()) {
            Button button = (Button) event.getSource();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(".client"));
                String ligne = reader.readLine();
                String[] ipPort = ligne.split(":");
                ipTextField.setText(ipPort[0]);
                portTextField.setText(ipPort[1]);
                ligne = reader.readLine();
                pseudoTextField.setText(ligne);
                button.setText("Informations récupérées.");
                button.setDisable(true);
                reader.close();
            } catch (Exception e) {
                button = (Button) event.getSource();
                button.setText("Erreur lors de la lecture du fichier de configuration.");
            }
        }
    }
}
