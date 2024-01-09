
package src;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class Client {
    private static String pseudo;
    private static String REGEX = "^[a-zA-Z0-9]*$";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez votre pseudo:");
        Client.pseudo = scanner.nextLine(); 
        while (Client.pseudo.equals("") || Client.pseudo.matches(REGEX) == false){
            System.out.println("Seules les lettres et les chiffres sont autorisés.");
            System.out.println("Entrez votre pseudo:");
            Client.pseudo = scanner.nextLine(); 
        }
        System.out.println("Entrez l'adresse IP du serveur:");
        String ip_serveur = scanner.nextLine();
        System.out.println("Entrez le port du serveur:");
        int port = scanner.nextInt();
        scanner.nextLine();

        while(true){
            try{
                Socket socket = new Socket(ip_serveur, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                
                System.out.print("Entrez votre message : ");
                String message = scanner.nextLine();
                writer.println(String.format("%1$s:%2$s", pseudo,message));
                writer.flush();
                socket.close();
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

}
