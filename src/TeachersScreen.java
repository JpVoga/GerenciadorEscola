import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class TeachersScreen extends JPanel {
    Window window;

    public TeachersScreen(Window window) {
        this.window = window;
        this.setLayout(new BorderLayout());

        JPanel teachersPanel = new JPanel();
        teachersPanel.setLayout(new BoxLayout(teachersPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(teachersPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        spawnTeacherDisplays(teachersPanel);

        JButton addTeacherButton  = new JButton("Novo Professor");
        addTeacherButton.addActionListener(e -> {
            String firstName = JOptionPane.showInputDialog(null, "Insira o novo primeiro nome do professor: ", "");
            String lastName = JOptionPane.showInputDialog(null, "Insira o novo sobrenome do professor: ", "");

            if (firstName == null) firstName = "";
            if (lastName == null) lastName = "";

            if ((firstName.length() == 0) || (lastName.length() == 0)) {
                JOptionPane.showMessageDialog(null, "Erro: Nome e sobrenome do professor devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Database.createTeacher(new Teacher(firstName, lastName));
                    spawnTeacherDisplays(teachersPanel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                    goBackToHome();
                }
            }
        });
        this.add(addTeacherButton, BorderLayout.SOUTH);

        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> goBackToHome());
        this.add(backButton, BorderLayout.NORTH);
    }

    private void spawnTeacherDisplays(JPanel panel) {
        panel.removeAll();

        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM teacher");

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
                JPanel parentPanel = panel;
                JPanel TeacherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                parentPanel.add(TeacherPanel);

                TeacherPanel.add(new JLabel(String.format("(%d) %s %s", teacher.getId(), teacher.getFirstName(), teacher.getLastName())));

                JButton modifyTeacherButton = new JButton("Modificar");
                modifyTeacherButton.addActionListener(e -> {
                    String newFirstName = JOptionPane.showInputDialog(null, "Insira o novo primeiro nome do professor: ", teacher.getFirstName());
                    String newLastName = JOptionPane.showInputDialog(null, "Insira o novo sobrenome do professor: ", teacher.getLastName());

                    if ((newFirstName.length() == 0) || (newLastName.length() == 0)) {
                        JOptionPane.showMessageDialog(null, "Erro: Nome e sobrenome do professor devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        teacher.setFirstName(newFirstName);
                        teacher.setLastName(newLastName);

                        try {
                            Database.updateTeacher(teacher.getId(), teacher);
                            spawnTeacherDisplays(parentPanel);
                        }
                        catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                            goBackToHome();
                        }
                    }
                });
                TeacherPanel.add(modifyTeacherButton);

                JButton deleteTeacherButton = new JButton("Excluir Professor");
                deleteTeacherButton.addActionListener(e -> {
                    try {
                        Database.deleteTeacher(teacher.getId());
                        spawnTeacherDisplays(parentPanel);
                    }
                    catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                        goBackToHome();
                    }
                });
                TeacherPanel.add(deleteTeacherButton);
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
            goBackToHome();
        }
        finally {
            window.revalidate();
            window.repaint();
        }
    }

    private void goBackToHome() {
        window.setContent(new HomeScreen(window));
    }
}