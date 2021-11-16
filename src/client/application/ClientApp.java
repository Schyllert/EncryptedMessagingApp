package client.application;

import client.application.controller.LoginController;
import client.application.model.Client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientApp extends Application {

    private static Client client;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.exit(-1);
        }

        ClientApp.client = new Client("localhost", 8888);
        Application.launch(ClientApp.class, args);
        System.exit(-1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("view/login.fxml")
        );

        Parent root = loader.load();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Login window");
        primaryStage.setScene(new Scene(root, 520, 400));

        LoginController controller = loader.getController();

        if (controller == null) {
            System.out.println("Controller is null");
            System.exit(-1);
        } else {
            controller.initData(client);
        }
        primaryStage.show();
    }

}
