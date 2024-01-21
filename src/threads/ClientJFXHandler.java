package threads;

import java.io.IOException;
import java.net.Socket;

import application.Content;

public class ClientJFXHandler extends Handler {

    private Content contenu;

    public ClientJFXHandler(Socket socket, Content contenu) {
        super(socket);
        this.contenu = contenu;
    }
    
    public void run(){
        try {
            String raw = reader.readLine();
            while(raw != null) {
                this.contenu.setContent(raw);
                raw = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return;
    }
}