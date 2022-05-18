package compulsory;

import javax.swing.table.JTableHeader;

public class Main {
    public static void main(String[] args) {


        System.out.println("Hello World");
        Server server=new Server(8100);
        while(ClientTreatment.getOpened()) {
            server.serveClients();
        }
       server.close();

    }

}
