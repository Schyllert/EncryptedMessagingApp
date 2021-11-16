package client.application.controller;

import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;



import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private ListView<String> onlineList;

    @FXML
    private TextArea userChatBox;

    private String[] friendList = {"david", "tomas", "Elleh"};
    private String friend;

    private double xOffset;
    private double yOffset;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public MainPageController() {
    }

    @FXML
    void checkSend(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            if (event.isShiftDown()) {
                userChatBox.appendText(System.getProperty("line.separator"));
            } else {
                // This is kinda ugly lol - we should prevent the enter from beeing added at all
                if(!userChatBox.getText().isEmpty()){
                    System.out.println("Send this text");
                }
            }
        }
    }


    @FXML
    void mousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    void mouseDragged(MouseEvent event) {

        try {
            root = FXMLLoader.load(getClass().getResource("../view/mainPage.fxml"));
            scene = onlineList.getScene();
            Window window = scene.getWindow();
            stage = (Stage) window;

            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        onlineList.getItems().addAll(friendList);
        onlineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                friend = onlineList.getSelectionModel().getSelectedItem();

                //showChatHistory(friend);
                System.out.println(friend);
            }
        });

    }
}




