public class Teacher extends Person {
    public Teacher(int id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }
}