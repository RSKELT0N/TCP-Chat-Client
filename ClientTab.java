import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class ClientTab extends Tab {

    private String username;
    private TextArea area;

    ClientTab(String username,TextArea area) {
        super();
        this.username = username;
        this.area = area;
    }

    public String getUsername() {
        return username;
    }

    public TextArea getArea() {
        return this.area;
    }

    public void setArea(TextArea area) {
        this.area = area;
    }
}
