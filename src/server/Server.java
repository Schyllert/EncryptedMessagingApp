package server;


import javax.net.ServerSocketFactory;
import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Properties;

public class Server implements Runnable{

    private final ServerSocket serverSocket;
    private static final ArrayList<ServerWorker> workerList = new ArrayList<>();
    private String server_port;

    public Server(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        loadConfigs();
        newListener();
    }

    private void newListener() {
        (new Thread(this)).start();
    }

    public static void main(String[] args){
        System.out.println("\nServer started\n");
        int port = getPort(args);
        String type = "TLS";
        try {
            createServer(type, port);
        } catch (IOException e) {
            System.out.println("Unable to start Server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int getPort(String[] args) {
        int port = -1;
        if(args.length >= 1){
            port = Integer.parseInt(args[0]);
        }
        return port;
    }

    private static void createServer(String type, int port) throws IOException{
        ServerSocketFactory serverSocketFactory = getServerSocketFactory(type);
        ServerSocket serverSocket = serverSocketFactory.createServerSocket(port);
        ((SSLServerSocket)serverSocket).setNeedClientAuth(true); // enables client authentication
        new Server(serverSocket);

    }

    private static ServerSocketFactory getServerSocketFactory(String type) {
        if (type.equals("TLS")) {
            SSLServerSocketFactory factory = null;
            try { // set up key manager to perform server authentication
                SSLContext context = SSLContext.getInstance("TLS");
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                KeyStore keyStore = KeyStore.getInstance("JKS");
                KeyStore trustStore = KeyStore.getInstance("JKS");

                char[] password = "password".toCharArray(); //TODO: Load from config file and check pathway to certificates

                keyStore.load(new FileInputStream("src/server/security/certificates/serverKeyStore"), password);  // keystore password (storepass)
                trustStore.load(new FileInputStream("src/server/security/certificates/serverTrustStore"), password); // truststore password (storepass)

                keyManagerFactory.init(keyStore, password); // certificate password (keypass)
                trustManagerFactory.init(trustStore);  // possible to use keystore as truststore here
                context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                factory = context.getServerSocketFactory();
                return factory;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return ServerSocketFactory.getDefault();
        }
        return null;
    }

    public ArrayList<ServerWorker> getWorkerList() {
        return workerList;
    }

    public void run(){
        try{
            SSLSocket socket = setupSocket();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            try {
                workerList.add(new ServerWorker(socket, out, in, this));
                System.out.println("size of worker list is: " + workerList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("Client disconnected");
            e.printStackTrace();
        }
    }

    private void loadConfigs() throws IOException {

        Properties prop = new Properties();
        String propFileName = "server/server-config.properties";

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
        if (inputStream == null) {
            System.out.println("Error: Unable to find configuration files");
            System.exit(-1);
        }
        prop.load(inputStream);
        server_port = prop.getProperty("server_port");
    }

    private SSLSocket setupSocket() throws IOException {
        SSLSocket socket = (SSLSocket)serverSocket.accept(); //Wait for a client to connect and accept if client is trusted
        newListener();
        SSLSession session = socket.getSession();
        Certificate[] clientCertificateChain = session.getPeerCertificates();
        //Get client's own certificate (signed by certificates at following indices
        X509Certificate clientCertificate = (X509Certificate) clientCertificateChain[0];
        String subjectName = clientCertificate.getSubjectX500Principal().getName();
        System.out.println("Client connected");
        System.out.println("Subject name: " + subjectName);
        return socket;
    }


}
