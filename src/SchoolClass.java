public class SchoolClass {
    private int id = 0;
    private String name;
    private Integer teacherId = null; // Teacher id is null if class has not a teacher assigned to it yet

    public SchoolClass(int id, String name, Integer teacherId) {
        this.id = id;
        this.setName(name);
        this.setTeacherId(teacherId);
    }

    public SchoolClass(String name, Integer teacherId) {
        this(0, name, teacherId);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}