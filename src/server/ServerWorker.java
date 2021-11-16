package server;

import server.application.ServerGateway;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerWorker implements Runnable {

   private final SSLSocket socket;
   private final PrintWriter out;
   private final BufferedReader in;
   private final ServerGateway serverGateway;
   private final ReentrantLock sendLock;

   // The current number of the identifier to track a reply to the matching outgoing call
   private volatile int replySendNumber = 0;

   // The number of replies we have received from the client
   private volatile int replyReceiveNumber = 0;


    public ServerWorker(SSLSocket socket, PrintWriter out, BufferedReader in, Server server) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.sendLock = new ReentrantLock(false);
        this.serverGateway = new ServerGateway(server);
        newListener();
    }

    private void runREPL() throws IOException {

        String input;
        String request;
        String data = "";

        while ((input = in.readLine()) != null && !input.equals("EOM")) {
            System.out.println("received '" + input + "' from client");

            String regex = "\\{(.*)+\\}";
            Pattern pt = Pattern.compile(regex);
            Matcher mt = pt.matcher(input);
            Boolean result = mt.find();

            if (result == true) {
                data = mt.group(0);
                input = input.replace(" " + mt.group(0), "");
            }

            String[] inputComponents = input.split(" ");


            if (inputComponents[0].equals("call")) {
                sendLock.lock();
                try {
                    int replyNumber = Integer.parseInt(inputComponents[1]);
                    request = input.replace(inputComponents[0] + " " + inputComponents[1] + " ", "");
                    String response = serverGateway.handle(request, data);
                    out.println("reply " + replyNumber + " " + response);
                    out.flush();
                    out.println("EOM");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    sendLock.unlock();
                }

            } else if (inputComponents[0].equals("reply")) {
                incReplyReceiveNumber();

            }

        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public ReentrantLock getSendLock() {
        return sendLock;
    }

    private void newListener() {
        (new Thread(this)).start();
    }

    private void tearDown(SSLSocket socket, PrintWriter out, BufferedReader in) throws IOException {
        in.close();
        out.close();
        socket.close();
        System.out.println("client disconnected");
    }

    public void run() {
            try {
                runREPL();
                System.out.println("Closing the socket");
                tearDown(socket, out, in);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public synchronized void incReplyReceiveNumber() {
        replyReceiveNumber++;
    }


    public ServerGateway getGateway() {
        return serverGateway;
    }
}
