package compulsory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLOutput;

public class Server {
    static private int numberClient=0;
    private static ServerSocket serverSocket = null;
    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Server(int PORT) {
        try {

            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(10000);

        } catch (Exception e) {
            System.err.println("I have an error when I'm trying to make the server Socket");
            //e.printStackTrace();
            try {
                serverSocket.close();
            } catch (IOException ex) {
                System.err.println("I couldn't close the Server it in the first try");
               // ex.printStackTrace();
            }
        }
    }

    public void serveClients() {
        try {
            while (ClientTreatment.getOpened()) {
                System.out.println("Waiting for a client...");
                Socket socket = serverSocket.accept();
                    new ClientTreatment(socket,numberClient++).start();

            }
        }
        catch (SocketTimeoutException e)
        {
            System.out.println("[server] Am asteptat pentru 10s sa vina un client");
        }
        catch(Exception e)
        {
            System.err.println("I failed to connect with a client ...");
            e.printStackTrace();
        }
    }
    public void close(){
        while(ClientTreatment.areThereClients())
        {
            System.out.println("Inca mai sunt clienti de servit..nu pot inchide");
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Eroare la inchiderea serverului");
                e.printStackTrace();
            }
        }
        System.out.println("I closed the server succesfully");
    }
}
