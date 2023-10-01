import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class StudentsScreen extends JPanel {
    Window window;

    public StudentsScreen(Window window) {
        this.window = window;
        this.setLayout(new BorderLayout());

        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(studentsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        spawnStudentDisplays(studentsPanel);
    }

    private void spawnStudentDisplays(JPanel panel) {
        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM student");

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
                JPanel parentPanel = panel;
                JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                parentPanel.add(studentPanel);

                studentPanel.add(new JLabel(String.format("(%d) %s %s", student.getId(), student.getFirstName(), student.getLastName())));

                // ADD BUTTONS TO DO CRUD IN STUDENTS!!!!!!!
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
            goBackToHome();
        }
    }

    private void goBackToHome() {
        window.setContent(new HomeScreen(window));
    }
}
