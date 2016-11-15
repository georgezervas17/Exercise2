
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class ChatUsers extends JFrame implements ActionListener {

    String username;
    PrintWriter pw;
    static BufferedReader br;
    static JTextArea chatmsg;
    JTextField chatip;
    JButton send, exit;

    public ChatUsers(String uname, String servername) throws Exception {

        super(uname);
        this.username = username;
        Socket ch = new Socket(servername,90);

        br = new BufferedReader(new InputStreamReader(ch.getInputStream()));
        pw = new PrintWriter(ch.getOutputStream(), true);
        pw.println(uname);
        builtInterface();
        new MessageThread().start();

    }

    private void builtInterface() {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        chatip = new JTextField(50);
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(chatip);
        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Chat Room");
        add(bp, "North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500, 300);
        setVisible(true);
        pack();

    }

    public void actionPerformed(ActionEvent evt) {

        if (evt.getSource() == exit) {
            pw.println("end");

        } else {
            pw.println(chatip.getText());
            chatip.setText(null);
        }
    }

    public static void main(String args[]) {
        String SetUsersName = JOptionPane.showInputDialog(null, "Please enter your name:", "chat", JOptionPane.PLAIN_MESSAGE);
        String servername = "localhost";
        try {
            new ChatUsers(SetUsersName, servername);
        } catch (Exception ex) {
            System.out.println();
        }
    }

    private static class MessageThread extends Thread {

        public void run() {

            String line;
            try {
                while (true) {
                    line = br.readLine();
                    chatmsg.append(line + "\n");
                }
            } catch (Exception ex) {

            }
        }
    }

}
