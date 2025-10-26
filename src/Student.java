public class Student {
    private String studentId;
    private String name;
    private String course;
    private String year;
    private String photoPath;

    public Student(String studentId, String name, String course, String year, String photoPath) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.year = year;
        this.photoPath = photoPath;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", year='" + year + '\'' +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }
}
