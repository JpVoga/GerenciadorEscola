import java.sql.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {
        Student[] students = {
            Database.createStudent(new Student("João", "Voga")),
            Database.createStudent(new Student("Kate", "Pereira")),
            Database.createStudent(new Student("Jairo", "Silva"))
        };

        Teacher teacher = Database.createTeacher(new Teacher("Nelson", "Maia"));

        SchoolClass schoolClass = new SchoolClass("Matemática", teacher.getId());
        for (Student student: students) schoolClass.getStudentIds().add(student.getId());
        schoolClass = Database.createSchoolClass(schoolClass);

        SchoolClass newSchoolClass = new SchoolClass("Bio", teacher.getId());
        newSchoolClass.getStudentIds().add(students[0].getId());
        Database.updateSchoolClass(schoolClass.getId(), newSchoolClass);

        for (Student student: students) Database.deleteStudent(student.getId());
        Database.deleteTeacher(teacher.getId());
        Database.deleteSchoolClass(schoolClass.getId());
    }
}