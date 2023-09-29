public abstract class Person {
    private int id = 0; // The database id is zero when we don't have included it in the database yet
    private String firstName;
    private String lastName;

    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public Person(String firstName, String lastName) {
        this(0, firstName, lastName);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}