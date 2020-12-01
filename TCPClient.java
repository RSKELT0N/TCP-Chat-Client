import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TCPClient extends Thread {

    private String username;
    private Socket socket;
    private String ip;
    private int port;
    private Scanner input;
    private PrintWriter output;
    private Main main;
    private Update update;
    private boolean nickSet;
    private boolean initialConnect;
    private boolean clientNotConnected;
    private final int CLIENT_NOT_CONNECTED_SLEEP_DELAY = 1500;

    TCPClient(String user, String ip, int port, Main main) {
        this.username = user;
        this.ip = ip;
        this.port = port;
        this.main = main;
        this.nickSet = false;
        this.initialConnect = true;
        checkInfo(ip, port);
        update = new Update();

        update.setDaemon(true);
        update.start();
    }

    public void checkInfo(String ip, int port) throws IllegalArgumentException {
        if (!checkValidPort(port) || !checkValidIP(ip))
            throw new IllegalArgumentException();
    }

    public boolean checkValidIP(String ip) {
        String[] parts = ip.split("\\.");

        if (parts.length == 4)
            return true;

        return false;
    }

    public boolean checkValidPort(int port) {
        if (port > 0)
            return true;
        else return false;
    }

    @Override
    public void run() {
        while (true) {
            String in = "";

            try {
                in = input.nextLine();
            } catch (Exception e) {
                update.setConnected(false);
                clientNotConnected = true;

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Client has been disconnected!", ButtonType.OK);
                    alert.showAndWait();
                });
                nickSet = false;

                while (clientNotConnected) {
                    try {
                        sleep(CLIENT_NOT_CONNECTED_SLEEP_DELAY);
                    } catch (InterruptedException ex) {
                    }
                    continue;
                }
            }

            if (in.startsWith("~[") && in.endsWith("']")) {
                String finalIn = in;
                Platform.runLater(() -> {
                    ArrayList<String> clients = new ArrayList<String>(Arrays.asList(finalIn.split("~")));
                    main.getListPlayers().setClients(clients);
                    main.getListPlayers().updateClients();
                });
                continue;
            }

            in += "\n";
            String parts[] = in.split(" ");
            String user = parts[1].substring(1, parts[1].length() - 2);
            String location = parts[0].substring(1, parts[0].length() - 1);
            if(location.equals("Public"))
                main.getMessageScreen().getClientsTabs().get("Public").getArea().appendText(in);
            else {
                try {
                    main.getMessageScreen().getClientsTabs().get(user).getArea().appendText(in);
                } catch (Exception e) {
                    String finalIn = in;
                    Platform.runLater(() -> {
                        main.getMessageScreen().getMessageTabs().getTabs().add(main.getMessageScreen().getCurrIndex(),main.getMessageScreen().createPrivateTab(new ClientTab(user,null)));
                        main.getMessageScreen().setCurrIndex(main.getMessageScreen().getCurrIndex()+1);
                        main.getMessageScreen().getClientsTabs().get(user).getArea().appendText(finalIn);
                    });
                }
        }
    }
}

    public void runThread() {
        Platform.runLater(() -> {
            main.getMessageScreen().setClient(this);
            main.getMessageScreen().getHeadLabel().setText("Client: " + this.ip + "/ Connected: " + update.isConnected);
            main.setClient(this);
        });
        clientNotConnected = false;
        if (initialConnect) {
            this.setDaemon(true);
            this.start();
            initialConnect = false;
        }
        send("/nick " + this.username);
        nickSet = true;


    }

    public void send(String msg) {
        output.println(msg);
    }

    public void sendList(String msg) {
        output.println(msg);
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

private class Update extends Thread {
    private final int SLEEP_DELAY = 3000;
    private final String LIST_COMMAND = "/list";
    private boolean isConnected;

    Update() {
        this.isConnected = false;
    }

    @Override
    public void run() {
        while (true) {
            if (isConnected) {
                if (nickSet)
                    sendList(LIST_COMMAND);
            } else {
                try {
                    socket = new Socket(ip, port);
                    input = new Scanner(socket.getInputStream());
                    output = new PrintWriter(socket.getOutputStream(), true);
                    update.setConnected(true);
                    runThread();
                } catch (Exception e) {
                }
            }
            try {
                sleep(SLEEP_DELAY);
            } catch (InterruptedException e) {
            }
        }
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
}
