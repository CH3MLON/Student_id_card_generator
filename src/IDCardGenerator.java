import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IDCardGenerator {

    public static boolean generateCard(Student student, String outputPath) {
        try {
            BufferedImage cardImage = new BufferedImage(400, 250, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = cardImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 400, 250);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(5, 5, 390, 240);

            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.setColor(Color.BLACK);
            String header = "STUDENT ID CARD";
            FontMetrics fm = g2d.getFontMetrics();
            int headerX = (400 - fm.stringWidth(header)) / 2;
            g2d.drawString(header, headerX, 30);

            try {
                File photoFile = new File(student.getPhotoPath());
                if (photoFile.exists()) {
                    BufferedImage photo = ImageIO.read(photoFile);
                    Image scaledPhoto = photo.getScaledInstance(100, 120, Image.SCALE_SMOOTH);
                    g2d.drawImage(scaledPhoto, 20, 60, null);
                } else {
                    drawPlaceholder(g2d);
                }
            } catch (IOException e) {
                drawPlaceholder(g2d);
                System.out.println("[WARNING] Could not load photo, using placeholder");
            }

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setColor(Color.BLACK);

            int textX = 140;
            int textY = 70;
            int lineSpacing = 30;

            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("ID NUMBER: " + student.getStudentId(), textX, textY);

            textY += lineSpacing;
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("NAME: " + student.getName(), textX, textY);

            textY += lineSpacing;
            g2d.drawString("COURSE: " + student.getCourse(), textX, textY);

            textY += lineSpacing;
            g2d.drawString("YEAR: " + student.getYear(), textX, textY);

            g2d.dispose();

            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            ImageIO.write(cardImage, "PNG", outputFile);

            return true;

        } catch (IOException e) {
            System.out.println("[ERROR] Cannot save ID card image: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("[ERROR] Error generating ID card: " + e.getMessage());
            return false;
        }
    }

    private static void drawPlaceholder(Graphics2D g2d) {
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(20, 60, 100, 120);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(20, 60, 100, 120);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString("NO PHOTO", 35, 125);
    }
}

