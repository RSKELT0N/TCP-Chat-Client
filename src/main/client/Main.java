package main.client;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private StartMenu menu;
    private MessageScreen messageScreen;
    private ListPlayers listPlayers;
    private TCPClient client;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        initialiseStage(stage);
        stage.show();
    }

    public void initialiseStage(Stage stage) {
        scene = new Scene(createRoot());

        stage.setOnCloseRequest(e -> {
            try {
                if (client.getSocket() != null)
                    client.getSocket().close();
                stage.close();
            } catch (Exception ex) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        viewStartMenu();
    }

    private Parent createRoot() {
        root = new BorderPane();
        createChildren();

        return root;
    }

    public void createChildren() {
        menu = new StartMenu(this);
        messageScreen = new MessageScreen(this);
        listPlayers = new ListPlayers(this);
    }

    private void viewStartMenu() {
        stage.setWidth(StartMenu.FORM_WIDTH);
        stage.setHeight(StartMenu.FORM_HEIGHT);
        stage.setTitle("TCP Client");
        root.setCenter(menu);
    }

    public void viewMessageScreen() {
        stage.setWidth(MessageScreen.FORM_WIDTH);
        stage.setHeight(MessageScreen.FORM_HEIGHT);
        stage.setTitle("TCP Client - [" + client.getUsername() + "]");
        root.setLeft(messageScreen);
        root.setCenter(listPlayers);
    }

    public boolean createClient(String username, String IP, int port) throws IOException {

        try {
            client = new TCPClient(username, IP, port,this);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "IP must be valid/Port must be greater than 0", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        launch(args);
    }

    public MessageScreen getMessageScreen() {
        return messageScreen;
    }

    public ListPlayers getListPlayers() {
        return listPlayers;
    }

    public TCPClient getClient() {
        return client;
    }

    public void setClient(TCPClient client) {
        this.client = client;
    }
}
