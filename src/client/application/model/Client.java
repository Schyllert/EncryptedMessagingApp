package client.application.model;



import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;


public class Client implements Runnable {

    private String ip;
    private int port;
    private final ReentrantLock sendLock;

    private PrintWriter out;
    private BufferedReader in;

    // The number to track the amount of received replies
    private volatile int replyReceiveNumber;

    // Represent the id of the next call to be made, once we get it we will also increment it.
    private volatile int replySendNumber;

    public volatile ArrayList<Reply> replyArrayList = new ArrayList();




    private String viewState; // Tracks current view


    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.sendLock = new ReentrantLock(false);
        this.replyReceiveNumber = 0;
        this.replySendNumber = 0;
        (new Thread(this)).start();
    }

    @Override
    public void run() {

        String username = "clientkeystore";
        char[] password = "password".toCharArray();

        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

        try {

            SSLSocketFactory factory = null;
            try {
                KeyStore ks = KeyStore.getInstance("JKS");
                KeyStore ts = KeyStore.getInstance("JKS");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                SSLContext ctx = SSLContext.getInstance("TLS");
                ks.load(new FileInputStream("src/client/certificates/" + username), password);  // keystore password (storepass)
                ts.load(new FileInputStream("src/client/certificates/clienttruststore"), password); // truststore password (storepass);
                kmf.init(ks, password); // user password (keypass)
                tmf.init(ts); // keystore can be used as truststore here
                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                factory = ctx.getSocketFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }

            SSLSocket socket = (SSLSocket)factory.createSocket(ip, port);
            System.out.println("\nsocket before handshake:\n" + socket + "\n");
            socket.startHandshake();
            SSLSession session = socket.getSession();
            Certificate[] clientCertificateChain = session.getPeerCertificates();
            X509Certificate clientCertificate = (X509Certificate) clientCertificateChain[0];
            String subjectName = clientCertificate.getSubjectX500Principal().getName();

            System.out.println("certificate name (subject DN field) on certificate received from server:\n" + subjectName + "\n");
            System.out.println("socket after handshake:\n" + socket + "\n");
            System.out.println("secure connection established\n\n");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;


            String input;
            String request;


            while((input = in.readLine()) != null && !input.equals("EOM")) {
                System.out.println("received '" + input + "' from server");
                String[] inputComponents = input.split(" ");

                if (inputComponents.length < 3) {
                    System.out.println("Wrong input");
                    continue;
                }

                if (inputComponents[0].equals("call")) {
                    sendLock.lock();
                    try {
                        int replyNumber = Integer.parseInt(inputComponents[1]);
                        request = input.replace(inputComponents[0] + " " + inputComponents[1] + " ", "");


                        // Can we create an event here that is linked to
                        String response = "Not fixed yet";
                        out.println("reply " + replyNumber + " " + response);
                        out.flush();
                        out.println("EOM");

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        sendLock.unlock();
                    }

                } else if (inputComponents[0].equals("reply")) {
                    // Route internally
                    // Take the information from the reply and add it to a list
                    try {
                        int replyNumber = Integer.parseInt(inputComponents[1]);
                        String message = input.replace(inputComponents[0] + " " + inputComponents[1] + " ", "");
                        replyArrayList.add(new Reply(replyNumber, message));
                        incReplyReceiveNumber();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCall(int replyNumber, String message) {
        sendLock.lock();
        try {
            out.println("call " + replyNumber + " " + message);
            out.flush();
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sendLock.unlock();
        }
    }

    public int getReplyReceiveNumber() {
        return replyReceiveNumber;
    }

    public synchronized ArrayList<Reply> getReplyArrayList() {
        return replyArrayList;
    }

    public synchronized int getAndUpdateReplySendNumber() {
        replySendNumber = replySendNumber+1;
        return replySendNumber-1;
    }

    public synchronized void incReplyReceiveNumber() {
        replyReceiveNumber++;
    }
}


