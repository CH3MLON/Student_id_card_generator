import java.io.File;
import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static String generateStudentId() {
        int randomNumber = 100000 + random.nextInt(900000);
        return "STU" + randomNumber;
    }

    public static boolean validateFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean validateNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean checkStudentIdUnique(String studentId, DatabaseManager dbManager) {
        Student existingStudent = dbManager.getStudentById(studentId);
        return existingStudent == null;
    }
}


