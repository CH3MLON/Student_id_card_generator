import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private Properties properties;

    public DatabaseManager() {
        loadProperties();
    }

    private void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("[ERROR] Configuration file not found: db.properties");
            System.out.println("Please create db.properties with database connection details.");
            System.exit(1);
        }
    }

    private Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] MySQL driver not found. Please add mysql-connector-java JAR to lib/ folder");
            throw new SQLException("Driver not found");
        }

        return DriverManager.getConnection(url, username, password);
    }

    public boolean insertStudent(Student student) {
        String sql = "INSERT INTO students (student_id, name, course, year, photo_path) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getCourse());
            pstmt.setString(4, student.getYear());
            pstmt.setString(5, student.getPhotoPath());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("course"),
                    rs.getString("year"),
                    rs.getString("photo_path")
                );
                students.add(student);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
        }

        return students;
    }

    public Student getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("course"),
                    rs.getString("year"),
                    rs.getString("photo_path")
                );
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
        }

        return null;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, course = ?, year = ?, photo_path = ? WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getCourse());
            pstmt.setString(3, student.getYear());
            pstmt.setString(4, student.getPhotoPath());
            pstmt.setString(5, student.getStudentId());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean logIssuance(String studentId) {
        String sql = "INSERT INTO issue_logs (student_id) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to log issuance: " + e.getMessage());
            return false;
        }
    }

    public List<String[]> getIssueLogs() {
        List<String[]> logs = new ArrayList<>();
        String sql = "SELECT il.log_id, il.student_id, s.name, il.issued_at " +
                     "FROM issue_logs il " +
                     "JOIN students s ON il.student_id = s.student_id " +
                     "ORDER BY il.issued_at DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String[] log = new String[4];
                log[0] = String.valueOf(rs.getInt("log_id"));
                log[1] = rs.getString("student_id");
                log[2] = rs.getString("name");
                log[3] = rs.getTimestamp("issued_at").toString();
                logs.add(log);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error: " + e.getMessage());
        }

        return logs;
    }
}
