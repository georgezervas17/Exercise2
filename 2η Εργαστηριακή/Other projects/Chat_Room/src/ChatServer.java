
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    public static void main(String args[]) throws Exception {
        new ChatServer().createServer();
    }

    ArrayList<String> users = new ArrayList<String>();
    ArrayList<ManageUser> clients = new ArrayList<ManageUser>();

    public void createServer() throws Exception {
        ServerSocket server = new ServerSocket(80, 10);
        System.out.println("Now server is on.");

        while (true) {
            Socket client = server.accept();
            ManageUser c = new ManageUser(client);
            clients.add(c);
        }
    }

    public void sendtoall(String user, String message) {
        for (ManageUser c : clients) {
            if (!c.getchatusers().equals(user)) {
                c.sendMessage(user, message);
            }
        }
    }

    class ManageUser extends Thread {

        String gotuser = "";
        BufferedReader input;
        PrintWriter output;

        public ManageUser(Socket client) throws Exception {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
            users.add(gotuser);
            start();
        }

        public void sendMessage(String chatuser, String chatmsg) {
            output.println(chatuser + "says: " + chatmsg);
        }

        public String getchatusers() {
            return gotuser;
        }

        public void run() {
            String line;
            try {
                while (true) {
                    line = input.readLine();
                    if (line.equals("end")) {
                        clients.remove(this);
                        users.remove(gotuser);
                        break;
                    }
                    sendtoall(gotuser, line);
                }
            } catch (Exception ex) {
                System.out.println();
            }

        }
    }

}
