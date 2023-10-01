import java.awt.*;
import javax.swing.*;

public class HomeScreen extends JPanel {
    Window window;

    public HomeScreen(Window window) {
        this.window = window;
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        Dimension buttonsDimension = new Dimension(100, 50);

        JButton studentsButton = new JButton("Alunos");
        studentsButton.setSize(buttonsDimension);
        studentsButton.addActionListener(e -> window.setContent(new StudentsScreen(window)));
        this.add(studentsButton);

        JButton teachersButton = new JButton("Professores");
        teachersButton.setSize(buttonsDimension);
        this.add(teachersButton);

        JButton schoolClassesButton = new JButton("Turmas");
        schoolClassesButton.setSize(buttonsDimension);
        this.add(schoolClassesButton);
    }
}