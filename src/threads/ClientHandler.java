package threads;

import java.net.Socket;

/* Un handler pour le client */
public class ClientHandler extends Handler {

    public ClientHandler(Socket socket) {
        super(socket);
    }
    
    /* Permet de lire tout le buffer et le print dans le terminal */
    public void run(){
        super.run();
    }
}