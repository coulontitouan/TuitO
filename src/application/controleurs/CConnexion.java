
package application.controleurs;

import javafx.event.EventHandler;

import application.ClientJFX;
import javafx.event.ActionEvent;

public class CConnexion implements EventHandler<ActionEvent> {
    private ClientJFX client;

    public CConnexion(ClientJFX client) {
        this.client = client;
    }

    /* Permet de gerer le click sur le bouton */
    @Override
    public void handle(ActionEvent event) {
        client.pageConnexion();
    }
}
