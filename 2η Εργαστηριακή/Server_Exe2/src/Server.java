
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server implements Serializable {

    //*****************************************************************
    //*************************VARIABLES*******************************
    public static SSLServerSocket serverSocket;
    public static ArrayList<User_Profile> arraylist = new ArrayList<User_Profile>();

    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {

        //*****************************************************************
        //******************LOAD THE CERTIDICATE***************************
        System.setProperty("javax.net.ssl.keyStore", "C:\\killek\\KeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        boolean listening = true;

        //*****************************************************************
        //**********CREATE A FACTORY FOR THE I2P NETWORK*******************
        SSLServerSocketFactory sslserversocketfactory = null;
        SSLServerSocket sslserversocket = null;

        try {

            //*****************************************************************
            //**************SET THE FACTORY AND THE PORT OF SERVER*************
            sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);

            //*****************************************************************
            //**************SERVER IS RUNNING AND CREATING A THREAD************
            while (listening) {

                //********************************************************************************
                //****CREATE A THREAD FOR THE USER WHO IS CONNECTED AND CALL SSL_Server CLASS*****
                Thread t = new Thread(new SSL_Server(sslserversocket.accept(), arraylist));

                if (t != null) {
                    System.out.println("New thread created: " + t.getName());
                }

                //*****************************************************************
                //*****************************THREAD START************************
                t.start();

            }
            
            sslserversocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SSL_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
