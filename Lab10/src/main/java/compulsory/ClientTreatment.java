package compulsory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientTreatment extends Thread{
    //not best practice
    private static List<Boolean> conexiuni=new ArrayList<>(100);
    private static Boolean opened=true;
    public static int myClient = 0;
    private Socket socket = null;
    private static List<String> users=new ArrayList<>(100);
    private Boolean logged=false;
    private static List<Integer> whatUser=new ArrayList<>(100);
    private static boolean[][] friendShipMatrix;

    public static Boolean getOpened() {
        return opened;
    }
    public static Boolean areThereClients()
    {
        return conexiuni.stream().filter(x->x==true).count()>0;
    }
    public ClientTreatment(Socket socket,Integer numberClient)
    {
        this.socket=socket;
        myClient=numberClient;
    }
    @Override
    public void run() {
        System.out.println("["+ myClient +"]"+"I am treating a client!");
        /**
         * Here we are putting the client so we keep count of him
         */
        conexiuni.add(myClient,true);
        PrintWriter out = null;
        BufferedReader in = null;
        String request = null;///send
        String response = null ;///receive
        try {
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("I couldn't make my out/in in client");
            //e.printStackTrace();
        }
        while(ClientTreatment.conexiuni.get(myClient))
        {
            try {
                request = in.readLine();
                System.out.println("["+ myClient +"]"+"Received the following command: "+request);
                String whatCase=request.split(" ")[0];
                switch(whatCase)
                {
                    /////------------REGISTER-----////
                    case "register":
                        users.add(myClient,request.split(" ")[1]);
                        out.println("Registered with success");
                        break;
                    /////------------LOGIN-------////
                    case "login" :
                        if(logged)
                            out.println("You are already logged");
                        else
                            if(users.contains(request.split(" ")[1]))
                            {
                                logged=true;
                                whatUser.add(myClient,users.indexOf(request.split(" ")[1]));
                                out.println("["+whatUser.get(myClient)+" "+users.get(whatUser.get(myClient))+"]Connected with success");
                            }
                            else
                                out.println("Nonexistent account");
                        break;
                    ///////--------friend-------//////
                    case "friend" :
                Integer numberUsers=users.size();
                friendShipMatrix=new boolean[numberUsers][numberUsers];
                String[] wantedFriends=request.split(" ");
                for(int elem=1;elem<wantedFriends.length;++elem)
                {
                    if(users.contains(wantedFriends[elem]))
                    {
                            int index=users.indexOf(wantedFriends[elem]);
                            friendShipMatrix[index][whatUser.get(myClient)]=true;
                            friendShipMatrix[whatUser.get(myClient)][index]=true;
                    }
                }
                for(int k=0;k<friendShipMatrix.length;++k)
                    System.out.println(Arrays.toString(friendShipMatrix[k]));
                            break;
                    //////-----------STOP--------////
                    case "stop" :
                        if(logged) {
                            System.out.println("[" + ClientTreatment.myClient + "]" + "Someone wants to stop the server");
                            out.println("$$Server received the request");
                            opened = false;
                            ClientTreatment.conexiuni.set(myClient, false);/////s-a rupt conexiunea cu clientul
                            /**
                             * In case 2 users want to close the app in the same time
                             */
                            socket.close();
                        }
                        else
                        {
                            out.println("You need to be logged so you can stop the server");
                        }
                        break;
                    ///////----------EXIT-------------///////
                    case "exit" :
                        System.out.println("["+ ClientTreatment.myClient +"]"+"Someone wants to exit");
                        out.println("$$Server received the request");
                        ClientTreatment.conexiuni.set(myClient,false);//am rupt conexiunea cu clientul
                        socket.close();
                        break;

                    /////////--------DEFAULT-----------///////
                    default:
                        if(opened) {
                            System.out.println("[" + ClientTreatment.myClient + "]" + "Unknown command");
                            out.println("Invalid Command");
                        }
                        else
                        {
                            System.out.println("Your last command bcs the server is closing");
                            out.println("$$"+"Invalid Command");
                        }
                }
            }
            catch (Exception e)
            {
                System.err.println("Error at reading/writing in clientTreatment");
                /////daca a fost o eroare ii rupem conexiunea
                ClientTreatment.conexiuni.set(myClient,false);
                e.printStackTrace();
                return;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("["+ myClient +"]"+"I can't close the connection...");
            //e.printStackTrace();
        }
    }
}
