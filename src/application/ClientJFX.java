
package application;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.controleurs.CConfirmer;
import application.controleurs.CConnexion;
import application.controleurs.CFichier;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import threads.ClientJFXHandler;

public class ClientJFX extends Application {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private Socket socket;
    private Stage primaryStage;
    public String pseudo;
    public String ipServeur;
    public Integer port;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Label titre = new Label();
        titre.setText("Rés'O Social");
        titre.setFont(new Font(24));
        
        Button connexion = new Button();
        connexion.setText("Connexion");
        Button fermer = new Button();
        fermer.setText("Fermer");
        
        connexion.setOnAction(new CConnexion(this));
        fermer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        VBox vBox = new VBox(titre, connexion, fermer);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
        
        this.primaryStage.setScene(new Scene(root, 300, 300));
        this.primaryStage.setTitle("Client");
        this.primaryStage.show();
    }

    public void pageConnexion(){
        Label titre = new Label("Informations sur le serveur :");
        titre.setFont(new Font(20));

        Label ipLabel = new Label("IP:");
        TextField ipTextField = new TextField();

        Label portLabel = new Label("Port:");
        TextField portTextField = createNumericTextField();

        Label pseudoLabel = new Label("Pseudo:");
        TextField pseudoTextField = new TextField();

        Button confirmerButton = new Button("Confirmer");

        BooleanBinding champsRemplis = Bindings.createBooleanBinding(
                () -> ipTextField.getText().matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")
                        && !ipLabel.getText().isEmpty()
                        && !portTextField.getText().isEmpty()
                        && !pseudoTextField.getText().isEmpty(),
                ipTextField.textProperty(), portTextField.textProperty(), pseudoTextField.textProperty());

        confirmerButton.disableProperty().bind(champsRemplis.not());

        confirmerButton.setOnAction(new CConfirmer(this, pseudoTextField, ipTextField, portTextField));

        Button fichierButton = new Button("Fichier");

        fichierButton.setOnAction(new CFichier(pseudoTextField, ipTextField, portTextField));

        ipTextField.textProperty().addListener((observable, oldValue, newValue) -> reactiveFichier(fichierButton));
        portTextField.textProperty().addListener((observable, oldValue, newValue) -> reactiveFichier(fichierButton));
        pseudoTextField.textProperty().addListener((observable, oldValue, newValue) -> reactiveFichier(fichierButton));

        VBox vBox = new VBox(titre, ipLabel, ipTextField, portLabel, portTextField, pseudoLabel, pseudoTextField,confirmerButton, fichierButton);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        Scene nouvelleScene = new Scene(vBox, 300, 300);

        this.primaryStage.setScene(nouvelleScene);
    }

    private void reactiveFichier(Button button) {
        button.setText("Fichier");
        button.setDisable(false);
    }

    private TextField createNumericTextField() {
        TextField textField = new TextField();
        StringConverter<Integer> converter = new IntegerStringConverter();
        TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, 0, change -> (change.getText().matches("\\d*")) ? change : null);
        textField.setTextFormatter(textFormatter);
        return textField;
    }

    public HBox profile(String pseudo){
        try{
            this.socket = new Socket(this.ipServeur, this.port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            Map<String, Object> message = new HashMap<>();
            message.put("id", null);
            message.put("user", this.pseudo);
            message.put("date", ClientJFX.dateFormat.format(new Date()));
            message.put("likes", 0);
            message.put("content", "/search user " + this.pseudo);

            ObjectMapper objectMapper = new ObjectMapper();

            String json = objectMapper.writeValueAsString(message);

            writer.println(json);

            writer.flush();
        }catch (Exception e){
            return new HBox(new Label("Impossible de se connecter au serveur.\nVérifiez l'adresse IP et le port."));
        }

        Content contenu = new Content();
        Thread t = new Thread(new ClientJFXHandler(this.socket, contenu));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] elements = contenu.getContent().split(" : ")[1].split(", ");

        Label titre = new Label("Profil de " + pseudo);
        titre.setFont(new Font(20));

        Button abonnes = new Button(elements[0]);
        abonnes.setFont(new Font(16));

        Button abonnements = new Button(elements[2]);
        abonnements.setFont(new Font(16));

        Button messages = new Button(elements[1]);
        messages.setFont(new Font(16));

        HBox HBox = new HBox(titre, abonnes, abonnements, messages);
        HBox.setSpacing(10);
        HBox.setAlignment(Pos.CENTER);
        HBox.setPadding(new Insets(20));

        return HBox;
    }

    public void connecte() {
        Label titre = new Label(pseudo);
        titre.setFont(new Font(20));

        Label connexion = new Label("Connexion...");
        connexion.setFont(new Font(16));
        
        Button deconnexionButton = new Button("Déconnexion");
        deconnexionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pageConnexion();
            }
        });

        VBox vBox = new VBox(titre, profile(pseudo), deconnexionButton);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));

        Scene nouvelleScene = new Scene(vBox, 1000, 700);

        this.primaryStage.setScene(nouvelleScene);
    }
}
