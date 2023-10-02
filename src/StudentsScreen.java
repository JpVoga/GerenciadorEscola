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

        JButton addStudentButton  = new JButton("Novo Aluno");
        addStudentButton.addActionListener(e -> {
            String firstName = JOptionPane.showInputDialog(null, "Insira o novo primeiro nome do aluno: ", "");
            String lastName = JOptionPane.showInputDialog(null, "Insira o novo sobrenome do aluno: ", "");

            if ((firstName.length() == 0) || (lastName.length() == 0)) {
                JOptionPane.showMessageDialog(null, "Erro: Nome e sobrenome do aluno devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Database.createStudent(new Student(firstName, lastName));
                    spawnStudentDisplays(studentsPanel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                    goBackToHome();
                }
            }
        });
        this.add(addStudentButton, BorderLayout.SOUTH);

        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> goBackToHome());
        this.add(backButton, BorderLayout.NORTH);
    }

    private void spawnStudentDisplays(JPanel panel) {
        panel.removeAll();

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

                JButton modifyStudentButton = new JButton("Modificar");
                modifyStudentButton.addActionListener(e -> {
                    String newFirstName = JOptionPane.showInputDialog(null, "Insira o novo primeiro nome do aluno: ", student.getFirstName());
                    String newLastName = JOptionPane.showInputDialog(null, "Insira o novo sobrenome do aluno: ", student.getLastName());

                    if ((newFirstName.length() == 0) || (newLastName.length() == 0)) {
                        JOptionPane.showMessageDialog(null, "Erro: Nome e sobrenome do aluno devem ser preenchidos", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        student.setFirstName(newFirstName);
                        student.setLastName(newLastName);

                        try {
                            Database.updateStudent(student.getId(), student);
                            spawnStudentDisplays(parentPanel);
                        }
                        catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                            goBackToHome();
                        }
                    }
                });
                studentPanel.add(modifyStudentButton);

                JButton deleteStudentButton = new JButton("Excluir Aluno");
                deleteStudentButton.addActionListener(e -> {
                    try {
                        Database.deleteStudent(student.getId());
                        spawnStudentDisplays(parentPanel);
                    }
                    catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                        goBackToHome();
                    }
                });
                studentPanel.add(deleteStudentButton);
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
