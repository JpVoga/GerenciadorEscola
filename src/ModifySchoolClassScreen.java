import java.sql.*;
import java.awt.*;
import javax.swing.*;

public class ModifySchoolClassScreen extends JPanel {
    private Window window;
    private SchoolClass schoolClass;

    public ModifySchoolClassScreen(Window window, SchoolClass schoolClass) {
        this.window = window;
        this.schoolClass = schoolClass;

        this.setLayout(new BorderLayout());

        JPanel topArea = new JPanel();
        topArea.setLayout(new BoxLayout(topArea, BoxLayout.X_AXIS));
        this.add(topArea, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel(schoolClass.getName());
        topArea.add(titleLabel);

        JButton backButton = new JButton("Voltar");
        backButton.addActionListener(e -> backToSchoolClasses());
        topArea.add(backButton);

        JButton modifySchoolClassButton = new JButton("Modificar");
        modifySchoolClassButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(null, "Insira o novo nome da turma: ", schoolClass.getName());
            if (name == null) name = "";
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Erro: Nome inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Integer teacherId = -1;
            try {
                teacherId = Integer.parseInt(JOptionPane.showInputDialog(null, "Insira o ID do novo professor (0 para ser null): ", ""));
                if ((teacherId == null) || (teacherId.equals(0))) teacherId = null;
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erro: ID de professor inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                SchoolClass newSchoolClass = new SchoolClass(name, teacherId);
                newSchoolClass.setStudentIds(schoolClass.getStudentIds());
                Database.updateSchoolClass(schoolClass.getId(), newSchoolClass);
                titleLabel.setText(name);
            }
            catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                backToSchoolClasses();
            }
        });
        topArea.add(modifySchoolClassButton);

        JPanel studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(studentsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        spawnStudentDisplays(studentsPanel);

        JButton addStudentButton = new JButton("Adcionar Aluno");
        addStudentButton.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(JOptionPane.showInputDialog(null, "Insira o ID do aluno: ", ""));

                try {
                    if (!(schoolClass.getStudentIds().contains(studentId))) schoolClass.getStudentIds().add(studentId);
                    this.schoolClass = Database.updateSchoolClass(schoolClass.getId(), schoolClass);
                    spawnStudentDisplays(studentsPanel);
                }
                catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                    backToSchoolClasses();
                }
            }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Erro: ID de aluno inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        });
        this.add(addStudentButton, BorderLayout.SOUTH);
    }

    private void spawnStudentDisplays(JPanel parentPanel) {
        parentPanel.removeAll();

        try {
            for (int studentId: schoolClass.getStudentIds()) {
                Student student = Database.readStudent(studentId);

                JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                parentPanel.add(studentPanel);

                JLabel studentLabel = new JLabel(String.format("(%d) %s", student.getId(), student.getFirstName() + " " + student.getLastName()));
                studentPanel.add(studentLabel);

                JButton excludeStudentButton = new JButton("Remover Aluno");
                excludeStudentButton.addActionListener(e -> {
                    try {
                        this.schoolClass.getStudentIds().remove(this.schoolClass.getStudentIds().indexOf(student.getId()));
                        this.schoolClass = Database.updateSchoolClass(schoolClass.getId(), schoolClass);
                        spawnStudentDisplays(parentPanel);
                    }
                    catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
                        backToSchoolClasses();
                    }
                });
                studentPanel.add(excludeStudentButton);
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro fatal: Falha ao conectar-se com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
            backToSchoolClasses();
        }
        finally {
            this.window.revalidate();
            this.window.repaint();
        }
    }

    private void backToSchoolClasses() {
        window.setContent(new SchoolClassesScreen(window));
    }
}