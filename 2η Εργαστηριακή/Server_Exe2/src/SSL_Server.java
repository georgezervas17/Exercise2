
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public class SSL_Server implements Runnable {

    //*****************************************************************
    //*************************VARIABLES*******************************
    public SSLSocket sslsocket;
    private BufferedReader in = null;
    public BufferedWriter w;
    public ObjectOutputStream obo;
    public ObjectInputStream obi;
    public boolean flag;
    public static ArrayList<User_Profile> arraylist;

    //*****************************************************************
    //***********************CONSTRUCTOR*******************************
    SSL_Server(Socket socket, ArrayList a) {

        sslsocket = (SSLSocket) socket;
        arraylist = a;
    }

    public void run() {

        System.out.println("New client connected with IP: " + sslsocket.getInetAddress() + " in port: " + sslsocket.getLocalPort()+"\n");
        try {

            //*****************************************************************
            //********************CREATE CONNECTION STREAM*********************
            w = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));

            obo = new ObjectOutputStream(sslsocket.getOutputStream());
            obi = new ObjectInputStream(sslsocket.getInputStream());

            Send_Message recieved_message;
            int counter = 0;
            flag = true;

            //*****************************************************************
            //**********SERVER IS RUNNING AND RECIEVE MESSAGES*****************
            while (((recieved_message = (Send_Message) obi.readObject()) != null) && flag == true) {

                //*****************************************************************
                //********************IF THE RECIEVED MESSAGE IS START*************
                if (recieved_message.getmessage().equals("START")) {

                    //*****************************************************************
                    //*********ADD THIS USER TO THE ARRAYLIST WITH ONLINE USERS********
                    arraylist.add(counter++, recieved_message.getuser());

                    //*****************************************************************
                    //********************PRINT ALL ONLINE USERS***********************
                    System.out.println("Online users are: ");
                    for (int i = 0; i < arraylist.size(); i++) {
                        System.out.println(arraylist.get(i).get_nickname());
                    }
                    System.out.println(" ");

                    int number_online_users = arraylist.size();

                    //*****************************************************************
                    //*******PUT ALL USERS INTO A ARRAY TO SEND THEM TO CLIENT*********
                    User_Profile[] online_users_array = new User_Profile[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        online_users_array[i] = arraylist.get(i);
                    }

                    //*****************************************************************
                    //****************SEND IT TO THE CLIENT****************************
                    obo.writeObject(new Send_Message("list", online_users_array, number_online_users));
                    obo.flush();
                    System.out.println("List has been sended." + "\n");

                }

                //*****************************************************************
                //*************IF THE RECIEVED MESSAGE IS HEARTBEAT****************
                if (recieved_message.getmessage().equals("HEARTBEAT")) {
                    w.write("success");
                    w.newLine();
                    w.flush();
                }

                //*****************************************************************
                //*************IF THE RECIEVED MESSAGE IS ΕΧΙΤ******************
                if (recieved_message.getmessage().equals("EXIT")) {
                    flag = false;
                    break;

                }

                //*****************************************************************
                //*************IF THE RECIEVED MESSAGE IS REFRESH******************
                if (recieved_message.getmessage().equals("REFRESH")) {

                    System.out.println("Client has requested for a new users list.");

                    int number_users = arraylist.size();

                    //*****************************************************************
                    //*******PUT ALL USERS INTO A ARRAY TO SEND THEM TO CLIENT*********
                    User_Profile[] k = new User_Profile[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        k[i] = arraylist.get(i);
                    }

                    //*****************************************************************
                    //****************SEND IT TO THE CLIENT****************************
                    obo.writeObject(new Send_Message("refresh_list", k, number_users));
                    obo.flush();
                    System.out.println("The new list has been sended." + "\n");
                }

            }
            //*****************************************************************
            //*********IF SERVER IS CLOSED THEN CLOSE ALL SOCKETS**************
            in.close();
            obo.close();
            sslsocket.close();

        } catch (IOException e) {
            System.out.println("IOException" + "\n");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SSL_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
