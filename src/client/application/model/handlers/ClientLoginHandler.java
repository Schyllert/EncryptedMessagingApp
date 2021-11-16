package client.application.model.handlers;

import client.application.model.Client;
import client.application.model.Reply;

import java.util.ArrayList;
import java.util.Iterator;

public class ClientLoginHandler {

    private Client client;
    private String message;
    private Integer replyReceiveNumber;

    public ClientLoginHandler(Client client) {
        this.client = client;
    }


    //TODO: Add a timeout

    //
    public String login(String username, String password) {


            long now = System.currentTimeMillis();
            replyReceiveNumber = client.getReplyReceiveNumber();

            int replyNumber = client.getAndUpdateReplySendNumber();
            client.sendCall(replyNumber, "login " + username + " " + password);

            while (replyReceiveNumber == client.getReplyReceiveNumber()) {
                if (System.currentTimeMillis() - now > 15000) {
                    message = "We timed out";
                    break;
                }
                System.out.println("Inside loop");
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    message = "We timed out";
                }
            }

            ArrayList<Reply> replyList = client.getReplyArrayList();
            Iterator<Reply> iter = replyList.iterator();

            System.out.println("This is the size of the list: " + replyList.size());
            System.out.println();
            while (iter.hasNext()) {
                Reply reply = iter.next();

                if (reply.getReplyNumber() == replyNumber) {
                    message = reply.getMessage();
                }
            }
            return message;
        }
    }

