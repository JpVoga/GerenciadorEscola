import java.sql.*;
import java.util.*;

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
        statement.execute(
            String.format(
                "INSERT INTO class(name, teacher_id) VALUE(\"%s\", %s)",
                escapeQuotes(schoolClass.getName()),
                ((schoolClass.getTeacherId() == null) || (schoolClass.getTeacherId().equals(0)))? "NULL":Integer.toString(schoolClass.getTeacherId())
            )
        );

        statement = connection.createStatement();
        statement.execute("SELECT last_insert_id() AS \"result\"");
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        int id = resultSet.getInt("result");

        for (int studentId: schoolClass.getStudentIds()) {
            statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO student_class_rel(student_id, class_id) VALUE(%d, %d)", studentId, id));
        }

        return readSchoolClass(id);
    }

    public static SchoolClass readSchoolClass(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM class WHERE id = %d", id));

        ResultSet resultSet = statement.getResultSet();
        resultSet.next();

        int teacherId = resultSet.getInt("teacher_id");
        SchoolClass schoolClass = new SchoolClass(resultSet.getInt("id"), resultSet.getString("name"), (teacherId == 0)? null:teacherId);

        ArrayList<Integer> studentIds = new ArrayList<>();
        statement = connection.createStatement();
        statement.execute(String.format("SELECT * FROM student_class_rel WHERE class_id = %d", id));
        resultSet = statement.getResultSet();
        while (resultSet.next()) {
            studentIds.add(resultSet.getInt("student_id"));
        }

        schoolClass.setStudentIds(studentIds);

        return schoolClass;
    }

    public static SchoolClass updateSchoolClass(int id, SchoolClass schoolClass) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("UPDATE class SET name = \"%s\" WHERE id = %d", escapeQuotes(schoolClass.getName()), id));

        statement = connection.createStatement();
        if (schoolClass.getTeacherId() == null || schoolClass.getTeacherId().equals(0)) {
            statement.execute(String.format("UPDATE class SET teacher_id = NULL WHERE id = %d", id));
        }
        else {
            statement.execute(String.format("UPDATE class SET teacher_id = %d WHERE id = %d", schoolClass.getTeacherId(), id));
        }

        statement = connection.createStatement();
        statement.execute(String.format("DELETE FROM student_class_rel WHERE class_id = %d", id));

        for (int studentId: schoolClass.getStudentIds()) {
            statement = connection.createStatement();
            statement.execute(String.format("INSERT INTO student_class_rel(student_id, class_id) VALUE(%d, %d)", studentId, id));
        }

        return readSchoolClass(id);
    }

    public static void deleteSchoolClass(int id) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(String.format("DELETE FROM class WHERE id = %d", id));
    }
}