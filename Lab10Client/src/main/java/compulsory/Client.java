package compulsory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.security.spec.ECField;
import java.util.Objects;
import java.util.Stack;

public class Client {
    private Boolean logged=false;
    private static Boolean connected=true;
    private static Socket socket = null;
    private static String serverAddress = "127.0.0.1";
    private static int PORT = 8100;
    public Client()
    {
        try{
            socket = new Socket(serverAddress,PORT);
            }
        catch (Exception e)
        {
            System.err.println("Error at making a connection with the server");
            //e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("I failed to close the socket");
               // ex.printStackTrace();
            }
        }
    }
    public void clientInteract() throws IOException {
        System.out.println("I successfully connected to the server:)");
        PrintWriter out = null;
        BufferedReader in = null;
        String request = null;///send
        String response = null;///receive
        try {
             out = new PrintWriter(socket.getOutputStream(),true);
             in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("I couldn't make my out/in in client");
            //e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));;
        while(connected)
        {


           try {
               boolean repeat=true;
               while(repeat) {
                   repeat=false;
                   request = reader.readLine();
                   String chooseRequest = request.split(" ")[0];
                   if(chooseRequest.equals(request)&&!chooseRequest.equals("exit")&&!chooseRequest.equals("stop"))
                   {
                       repeat=true;
                       continue;
                   }
                   System.out.println(chooseRequest);
                   switch (chooseRequest) {
                       case "register":
                           break;
                       case "login":
                           break;
                       case "friend":
                           break;
                       case "send":
                           break;
                       case "read":
                           break;
                       case "exit":
                           break;
                       case "stop":
                           break;
                       default:
                           System.out.println("The command was not recognised");
                           repeat=true;
                   }
               }
               out.println(request);
               response = in.readLine();
               System.out.println(response);
               if(response.endsWith("Connected with success"))
                   logged=true;






               /*---------Case of closing the server-------*/
               if((logged&&(Objects.equals(request, "stop")))||request.startsWith("$$")|| Objects.equals(request, "exit"))
               {
                   connected=false;
                   break;
               }
           }
           catch(Exception e){
               System.err.println("We failed to read/write");
           }
        }

        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("I failed to close the socket");
            // ex.printStackTrace();
        }
    }
}
