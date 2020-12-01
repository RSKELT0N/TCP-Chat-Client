import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.HashMap;

public class MessageScreen extends VBox {

    public static final int FORM_WIDTH = 650;
    public static final int FORM_HEIGHT = 400;

    private Main main;

    private TCPClient client;
    private Label headLabel;
    private TabPane messageTabs;
    private TextField messageField;
    private int currIndex;
    private String currTabUsername = "Public";
    private HashMap<String, ClientTab> clientsTabs = new HashMap<String,ClientTab>();

    MessageScreen(Main main) {
        this.main = main;
        createChildren();
        this.currIndex = 0;

        setBackground(new Background(new BackgroundFill(Color.MIDNIGHTBLUE, null, null)));
        setPadding(new Insets(20, 20, 20, 20));
        setAlignment(Pos.TOP_CENTER);
        setSpacing(20);
    }

    public void setClient(TCPClient client) {
        this.client = client;
    }

    private void createChildren() {
        getChildren().add(createHeaderLabel());
        getChildren().add(createTab());
        getChildren().add(createMessageField());

    }

    private TabPane createTab() {
        messageTabs = new TabPane();
        messageTabs.setPrefSize(200, 250);
        messageTabs.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));
        messageTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        messageTabs.setOnMouseClicked(e -> {
            this.currTabUsername = messageTabs.getSelectionModel().getSelectedItem().getText();
        });
        return messageTabs;

    }

    private Label createHeaderLabel() {
        headLabel = new Label();
        headLabel.setTextFill(Color.WHITE);
        headLabel.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(6), null)));

        headLabel.setAlignment(Pos.CENTER);
        headLabel.setFont(Font.font("Courier"));
        headLabel.setFont(Font.font(13));
        headLabel.setPrefWidth(400);
        return headLabel;
    }

    private TextField createMessageField() {
        messageField = new TextField();
        messageField.setPrefHeight(20);
        messageField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));

        messageField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (messageField.getText().equals(""))
                    return;

                String msg = messageField.getText();
                String uneditedMsg = msg;
                if (msg.charAt(0) != '/') {
                    if(currTabUsername.equals("Public"))
                        msg = "/send "+msg;
                    else msg = "/pSend " + currTabUsername +" "+ msg;
                }
                client.send(msg);
                messageField.setText("");
                getClientsTabs().get(currTabUsername).getArea().appendText("["+main.getClient().getUsername()+"]: "+uneditedMsg+"\n");
            }
        });


        return messageField;
    }

    public Tab createPublicTab() {
        Tab publicTab = new Tab("Public");
        TextArea box = new TextArea();
        box.setPrefHeight(250);
        box.setPrefWidth(200);
        box.setEditable(false);

        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));

        publicTab.setContent(box);
        getClientsTabs().put("Public", new ClientTab("Public",box));
        return publicTab;
    }

    public Tab createPrivateTab(ClientTab client) {
        Tab privateTab = new Tab(client.getUsername());
        TextArea box = new TextArea();
        box.setPrefHeight(250);
        box.setPrefWidth(200);
        box.setEditable(false);
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), null)));

        client.setArea(box);
        privateTab.setContent(box);
        getClientsTabs().put(client.getUsername(), client);
        return privateTab;
    }

    public TabPane getMessageTabs() {
        return messageTabs;
    }

    public Label getHeadLabel() {
        return headLabel;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }

    public HashMap<String, ClientTab> getClientsTabs() {
        return clientsTabs;
    }

}
