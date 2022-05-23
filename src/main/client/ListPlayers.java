package main.client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class ListPlayers extends VBox {

    private Main main;
    private VBox listBox;
    private Label listLabel;
    private ArrayList<String> clients = new ArrayList<String>();
    private ArrayList<HBox> clientBox = new ArrayList<HBox>();

    ListPlayers(Main main) {
        this.main = main;
        createChildren();
        setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE, null, null)));
        setPadding(new Insets(20, 20, 20, 20));
        setAlignment(Pos.TOP_LEFT);
        setSpacing(20);
    }

    private void createChildren() {
        getChildren().add(createListLabel());
        getChildren().add(createListBox());
    }

    private VBox createListBox() {
        listBox = new VBox();
        listBox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));
        listBox.setPrefWidth(80);
        listBox.setPrefHeight(300);
        listBox.setSpacing(1);
        return listBox;
    }

    private Label createListLabel() {
        listLabel = new Label();
        listLabel.setTextFill(Color.WHITE);
        listLabel.setText("User's Connected");
        listLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(6), null)));

        listLabel.setAlignment(Pos.CENTER);
        listLabel.setFont(Font.font("Courier"));
        listLabel.setFont(Font.font(13));
        listLabel.setPrefWidth(150);
        return listLabel;
    }

    public HBox createClient(int i) {
        Label name = new Label(clients.get(i).split("'")[1]);
        name.setPadding(new Insets(5, 10, 5, 5));
        name.setFont(Font.font(12));

        HBox client = new HBox();
        client.setAlignment(Pos.CENTER);
        client.setPrefHeight(30);
        client.setSpacing(10);

        Circle status = new Circle();
        status.setFill(Color.GREEN);
        status.setRadius(7);
        status.setStrokeWidth(2);
        status.setStroke(Color.DARKGREEN);

        Button msg = new Button("msg");

        if (main.getClient().getUsername().equals(clients.get(i).split("'")[1])) {
            msg.setText("You");
            msg.setDisable(true);
        }

        msg.setMaxWidth(40);
        msg.setMaxHeight(20);
        msg.setFont(Font.font(10));
        msg.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1), null)));
        msg.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), new Insets(2, 2, 2, 2))));

        msg.setOnMouseClicked(e -> {

            Platform.runLater(() -> {
                main.getMessageScreen().getMessageTabs().getTabs().add(main.getMessageScreen().getCurrIndex(), main.getMessageScreen().createPrivateTab(new ClientTab(name.getText(), null)));
                main.getMessageScreen().setCurrIndex(main.getMessageScreen().getCurrIndex() + 1);
            });

            try {
                sleep(500);
            } catch (InterruptedException ex) {
            }
            msg.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(5), null)));
        });


        client.getChildren().add(status);
        client.getChildren().add(name);
        client.getChildren().add(msg);
        return client;
    }

    public void updateClients() {
        listBox.getChildren().clear();
        for (int i = 1; i < clients.size(); i++) {
            clientBox.add(createClient(i));
            listBox.getChildren().add(clientBox.get(clientBox.size() - 1));
            listBox.getChildren().add(new Separator());
        }

    }


    public void setClients(ArrayList<String> clients) {
        this.clients = clients;
    }
}
