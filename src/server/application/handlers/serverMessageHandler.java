package server.application.handlers;

import server.Server;
import server.ServerWorker;
import server.models.User;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class serverMessageHandler {


    public static String handle(User user, Server server, String receiver, String message) {
        if (receiver == null || message == null) {
            return "Invalid arguments";
        }

        if (user == null || server == null) {
            return "Something is wrong on the Server-side - sorry for the inconvenience";
        }

        ArrayList<ServerWorker> workerList = server.getWorkerList();

        if (workerList == null) {
            return "Something is wrong on the server side";
        }

        if (user.getUserName().compareTo(receiver) == 0) {
            return "You are trying to pm yourself";
        }

        for (ServerWorker worker : workerList) {

            if (worker.getGateway().getUser().getUserName().compareTo(receiver) == 0) {
                PrintWriter out = worker.getOut();
                ReentrantLock sendLock = worker.getSendLock();
                sendLock.lock();
                try {
                    out.flush();
                    out.println(message);
                    out.flush();
                    out.println("EOM");
                    return "Message sent to: " + receiver;

                } catch (Exception e) {
                e.printStackTrace();
                out.flush();
                out.println("EOM");
                return "We had an error";

                }
                finally {
                    sendLock.unlock();
                }
            }
        }
    return "Why did we get all the way down here?";
    }
}


