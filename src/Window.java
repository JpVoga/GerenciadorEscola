import java.awt.*;
import javax.swing.*;

public class Window extends JFrame {
    private Component content = null;

    public Window() {
        this.setTitle("Gerenciador de Escola");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1024, 512);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setContent(new HomeScreen(this));
    }

    public Component getContent() {
        return this.content;
    }

    public void setContent(Component content) {
        if (this.content != null) this.remove(this.content);
        this.content = content;
        this.add(this.content, BorderLayout.CENTER);
        this.setVisible(true);
    }
}