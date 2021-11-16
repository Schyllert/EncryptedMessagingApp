package client.application.controller;

import client.application.model.Client;
import client.application.model.Reply;

import client.application.model.handlers.ClientLoginHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Iterator;


public class LoginController {

    @FXML
    private PasswordField serverPasswordField;
    @FXML
    private TextField serverUsernameField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private BorderPane loginBp;


    private Client client;
    private int replyReceiveNumber;

    private String message = "";


    private Stage stage;
    private Scene scene;
    private Parent root;

    private double xOffset;
    private double yOffset;


    @FXML
    void mousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }


    @FXML
    void mouseDragged(MouseEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            scene = loginButton.getScene();
            Window window = scene.getWindow();
            stage = (Stage) window;

            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonOnAction() {

        this.message = "";

        //TODO: Add an other while loop such that if the first replies in the array doesnt match we
        // we wait for an other reply aka  replyReceiveNumber+1 == client.getReplyReceiveNumber
        // This should not happen - i think but who the f knows

        if ((!serverUsernameField.getText().isBlank()) && (!serverPasswordField.getText().isBlank())) {

            loginMessageLabel.setText("You try to login");

            // We should send the username and password to the model and they should handel it


            ClientLoginHandler loginHandler = new ClientLoginHandler(client);

            //TODO: We get stuck if we enter wrong pw
            String message = loginHandler.login(serverUsernameField.getText(), serverPasswordField.getText());

            if (message.equals("Logged in successfully")) {
                try {
                    root = FXMLLoader.load(getClass().getResource("../view/mainPage.fxml"));
                    scene = loginButton.getScene();
                    Window window = scene.getWindow();
                    stage = (Stage) window;
                    scene.setRoot(root);
                    stage.setHeight(400);
                    stage.setWidth(600);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // We should move on into the other scene
                System.out.println(message);

                {

                }
            }
        }
    }



    public void initData(Client client) {
        this.client = client;
    }
}
