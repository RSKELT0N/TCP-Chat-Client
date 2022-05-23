package main.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class StartMenu extends VBox {

    public static final int FORM_WIDTH = 400;
    public static final int FORM_HEIGHT = 450;

    private Main main;
    private TextField usernameText;
    private TextField ipText;
    private TextField portText;

    StartMenu(Main main) {
        this.main = main;
        createChildren();

        setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE, null, null)));
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(20, 10, 10, 10));
    }

    private void createChildren() {
        getChildren().add(createTitle());
        getChildren().add(createUsernameBox());
        getChildren().add(createIPBox());
        getChildren().add(createPortBox());
        getChildren().add(createLine1());
        getChildren().add(createLine2());
        getChildren().add(createConnectButton());
    }

    private Label createTitle() {
        Label title = new Label();
        title.setText("Connect To Server");
        title.setFont(Font.font("Courier"));
        title.setTextFill(Color.WHITE);

        title.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(6), null)));
        title.setPadding(new Insets(20, 100, 20, 100));
        layout();
        title.setAlignment(Pos.CENTER);

        return title;
    }

    private HBox createUsernameBox() {
        HBox username = new HBox();
        Label text = new Label("Username: ");
        text.setTextFill(Color.WHITE);
        usernameText = new TextField();

        username.getChildren().add(text);
        username.getChildren().add(usernameText);
        username.setSpacing(10);
        username.setTranslateX(60);
        username.setTranslateY(80);
        return username;
    }

    private HBox createIPBox() {
        HBox ip = new HBox();
        Label text = new Label("IP: ");
        text.setTextFill(Color.WHITE);
        ipText = new TextField();

        ip.getChildren().add(text);
        ip.getChildren().add(ipText);
        ip.setSpacing(52);
        ip.setTranslateX(60);
        ip.setTranslateY(100);
        return ip;
    }

    private HBox createPortBox() {
        HBox port = new HBox();
        Label text = new Label("Port: ");
        text.setTextFill(Color.WHITE);
        portText = new TextField();

        port.getChildren().add(text);
        port.getChildren().add(portText);
        port.setSpacing(38);
        port.setTranslateX(60);
        port.setTranslateY(120);
        return port;
    }

    private Line createLine1() {
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(350);
        line.setTranslateY(-35);
        line.setStrokeWidth(2);
        line.setFill(Color.WHITE);
        return line;
    }

    private Line createLine2() {
        Line line = new Line();
        line.setStartX(0);
        line.setEndX(350);
        line.setTranslateY(160);
        line.setStrokeWidth(2);
        line.setFill(Color.LIGHTGRAY);
        return line;
    }

    private Button createConnectButton() {
        Button connect = new Button("Connect");
        connect.setFont(Font.font(("Courier")));
        connect.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(6), null)));
        connect.setTextFill(Color.WHITE);

        connect.setTranslateY(210);
        connect.setTranslateX(-5);
        connect.setMaxWidth(250);
        connect.setMaxHeight(40);

        connect.setOnAction(e -> {
            try {
                if(usernameText.getText().equals("") || ipText.getText().equals("") || portText.getText().equals("")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Input fields are invalid",ButtonType.OK);
                    alert.showAndWait();
                    return;
                }


                if(!main.createClient(usernameText.getText(),ipText.getText(),Integer.parseInt(portText.getText())))
                    return;
                main.viewMessageScreen();

                main.getMessageScreen().getMessageTabs().getTabs().add(main.getMessageScreen().getCurrIndex(),main.getMessageScreen().createPublicTab());

                main.getMessageScreen().setCurrIndex(main.getMessageScreen().getCurrIndex()+1);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Socket: "+ipText.getText()+"/"+portText.getText()+"\nRefused your connection",ButtonType.OK);
                alert.showAndWait();
            }
        });

        return connect;
    }
}
