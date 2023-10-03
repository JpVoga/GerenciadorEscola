import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class SchoolClassesScreen extends JPanel {
    private Window window;

    public SchoolClassesScreen(Window window) {
        this.window = window;

        this.setLayout(new BorderLayout());

        JPanel schoolClassesPanel = new JPanel();
        schoolClassesPanel.setLayout(new BoxLayout(schoolClassesPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(schoolClassesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        spawnSchoolClassDisplays(schoolClassesPanel);

        JButton addSchoolClassButton = new JButton("Nova Turma");
        addSchoolClassButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(null, "Insira o novo primeiro nome da turma: ", "");
            int teacherId = -1;

            if (name == null) name = "";

            try {
                teacherId = Integer.parseInt(JOptionPane.showInputDialog(null, "Insira o ID do professor: ", ""));
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erro: ID de professor inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Erro: Nome da turma deve ser preenchido", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Database.createSchoolClass(new SchoolClass(name, teacherId));
                    spawnSchoolClassDisplays(schoolClassesPanel);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                    goBackToHome();
                }
            }
        });
        this.add(addSchoolClassButton, BorderLayout.SOUTH);

        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> goBackToHome());
        this.add(backButton, BorderLayout.NORTH);
    }

    private void spawnSchoolClassDisplays(JPanel panel) {
        panel.removeAll();

        try {
            Connection connection = Database.getConnection();

            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM class");

            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                SchoolClass schoolClass = new SchoolClass(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("teacher_id"));
                JPanel parentPanel = panel;
                JPanel schoolClassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                parentPanel.add(schoolClassPanel);

                schoolClassPanel.add(new JLabel(String.format("(%d) %s", schoolClass.getId(), schoolClass.getName())));

                JButton modifySchoolClass = new JButton("Modificar");
                modifySchoolClass.addActionListener(e -> System.out.println("Modify class..."));;
                schoolClassPanel.add(modifySchoolClass);

                JButton deleteSchoolClass = new JButton("Excluir Turma");
                deleteSchoolClass.addActionListener(e -> {
                    try {
                        Database.deleteSchoolClass(schoolClass.getId());
                        spawnSchoolClassDisplays(parentPanel);
                    }
                    catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                        goBackToHome();
                    }
                });
                schoolClassPanel.add(deleteSchoolClass);
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