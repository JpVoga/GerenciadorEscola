import java.sql.*;
import java.util.Collection;

import javax.swing.JOptionPane;

public class Database {
    private static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/school_admin_db", "schoolAdmin", "school");
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro fatal: Não foi possível se conectar com o banco de dados", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static String escapeQuotes(String string) {
        return string.replace("\'", "\\\'").replace("\"", "\\\"");
    }

    private static void addStudentsToSchoolClass(Collection<Integer> studentIds, int schoolClassId) throws SQLException {
        for (Integer studentId: studentIds) {
            Statement statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO student_class_rel(student_id, class_id) VALUE(%d, %d)", studentId, schoolClassId));
        }
    }

    public static Connection getConnection() {return connection;}

    // Takes as parameter a which has not yet been created on the database and returns it created with a proper id
    public static Student createStudent(Student student) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("INSERT INTO student(first_name, last_name) VALUE(\"%s\", \"%s\")", escapeQuotes(student.getFirstName()), escapeQuotes(student.getLastName())));

        statement = connection.createStatement();
        statement.execute("SELECT * FROM student WHERE id = last_insert_id()");
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        return new Student(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
    }

    public static Student readStudent(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM student WHERE id = %d", id));

        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return new Student(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
        }
        else return null;
    }

    // Updates the fields of a student based on id and returns it
    public static Student updateStudent(int id, Student student) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("UPDATE student SET first_name = \"%s\" WHERE id = %d", escapeQuotes(student.getFirstName()), id));

        statement = connection.createStatement();
        statement.execute(String.format("UPDATE student SET last_name = \"%s\" WHERE id = %d", escapeQuotes(student.getLastName()), id));

        statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM student WHERE id = %d", id));
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        return new Student(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
    }

    public static void deleteStudent(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("DELETE FROM student WHERE id = %d", id));
    }

    public static Teacher createTeacher(Teacher teacher) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("INSERT INTO teacher(first_name, last_name) VALUE(\"%s\", \"%s\")", escapeQuotes(teacher.getFirstName()), escapeQuotes(teacher.getLastName())));

        statement = connection.createStatement();
        statement.execute("SELECT * FROM teacher WHERE id = last_insert_id()");
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        return new Teacher(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
    }

    public static Teacher readTeacher(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM teacher WHERE id = %d", id));

        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            return new Teacher(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
        }
        else return null;
    }

    public static Teacher updateTeacher(int id, Teacher teacher) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("UPDATE teacher SET first_name = \"%s\" WHERE id = %d", escapeQuotes(teacher.getFirstName()), id));

        statement = connection.createStatement();
        statement.execute(String.format("UPDATE teacher SET last_name = \"%s\" WHERE id = %d", escapeQuotes(teacher.getLastName()), id));

        statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM teacher WHERE id = %d", id));
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        return new Teacher(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"));
    }

    public static void deleteTeacher(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("DELETE FROM teacher WHERE id = %d", id));
    }

    public static SchoolClass createSchoolClass(SchoolClass schoolClass) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("INSERT INTO class(name, teacher_id) VALUE(\"%s\", %d)", escapeQuotes(schoolClass.getName()), schoolClass.getTeacherId()));

        statement = connection.createStatement();
        statement.execute("SELECT last_insert_id()");
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        int newSchoolClassId = resultSet.getInt(1);

        addStudentsToSchoolClass(schoolClass.getStudentIds(), newSchoolClassId);

        SchoolClass newSchoolClass = new SchoolClass(newSchoolClassId, schoolClass.getName(), schoolClass.getTeacherId());
        newSchoolClass.setStudentIds(schoolClass.getStudentIds());
        return newSchoolClass;
    }

    public static SchoolClass readSchoolClass(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM class WHERE id = %d", id));

        ResultSet resultSet = statement.getResultSet();
        if (resultSet.next()) {
            SchoolClass schoolClass = new SchoolClass(id, resultSet.getString("name"), resultSet.getInt("teacher_id"));

            statement = connection.createStatement();
            statement.execute(String.format("SELECT * FROM student_class_rel WHERE class_id = %d", schoolClass.getId()));

            resultSet = statement.getResultSet();
            while (resultSet.next()) {
                schoolClass.getStudentIds().add(resultSet.getInt("student_id"));
            }

            return schoolClass;
        }
        else return null;
    }

    public static SchoolClass updateSchoolClass(int id, SchoolClass schoolClass) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("UPDATE class SET name = \"%s\" WHERE id = %d", escapeQuotes(schoolClass.getName()), id));

        statement = connection.createStatement();
        statement.execute(String.format("UPDATE class SET teacher_id = %d WHERE id = %d", schoolClass.getTeacherId(), id));

        statement = connection.createStatement();
        statement.execute(String.format("DELETE FROM student_class_rel WHERE class_id = %d", id));

        addStudentsToSchoolClass(schoolClass.getStudentIds(), id);

        SchoolClass newSchoolClass = new SchoolClass(id, schoolClass.getName(), schoolClass.getTeacherId());
        newSchoolClass.setStudentIds(schoolClass.getStudentIds());
        return newSchoolClass;
    }

    public static void deleteSchoolClass(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("DELETE FROM class WHERE id = %d", id));
    }
}