public class Student extends Person {
    public Student(int id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }
}