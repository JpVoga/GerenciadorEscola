import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;;

public class SchoolClass {
    private int id = 0;
    private String name;
    private Integer teacherId = null; // Teacher id is null if class has not a teacher assigned to it yet
    private ArrayList<Integer> studentIds = new ArrayList<>(); // Array of the id of the students in this class

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

    public ArrayList<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(ArrayList<Integer> studentIds) {
        this.studentIds.clear();
        this.studentIds.addAll(studentIds.stream().distinct().collect(Collectors.toList()));
    }
}