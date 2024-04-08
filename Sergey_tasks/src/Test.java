import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Test {

    public static void main(String[] args) {
        try {
            File fileIn = new File("/home/irrisorr/Desktop/Java-features/Sergey_tasks/mysort");
            File fileOut = new File("/home/irrisorr/Desktop/Java-features/Sergey_tasks/FO.txt");
            FileReader fileReader = new FileReader(fileIn);
            FileWriter fileWriter = new FileWriter(fileOut);

            // 1.1 Full fileName
            String fullFileName = fileIn.getName();

            // 1.2 File size (bytes)
            int fileSize = (int) fileIn.length();

            // 1.3 File type (text / unicode / binary)
            byte[] buffer = Files.readAllBytes(fileIn.toPath());
            String fileType;

            if (buffer.length > 0){
                fileType = determineFileType(buffer);
            } else fileType = "File is empty";

            // 1.4 Total number of chars
            int ch;
            int totalChars = 0;
            while ((ch = fileReader.read()) != -1) {
                System.out.println(ch);
                totalChars += 1;
            }


            String fileNameToFO =
                    String.format("Name of input file: %s\nFile size: %d (bytes)\nFile type: %s\nTotal chars: %d",
                            fullFileName, fileSize, fileType, totalChars);

            fileWriter.write(fileNameToFO);

            fileReader.close();
            fileWriter.close();
        } catch (Exception e) {}
    }


    private static String determineFileType(byte[] buffer) {
        String fileType = "Unknown";

        if ((buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xFE && buffer[2] == (byte) 0x00 & buffer[3] == (byte) 0x00) ||
                (buffer[0] == (byte) 0x00 && buffer[1] == (byte) 0x00 && buffer[2] == (byte) 0xFE && buffer[3] == (byte) 0xFF)) {
            fileType = "UTF-32";
        } else if ((buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xFE) || (buffer[0] == (byte) 0xFE && buffer[1] == (byte) 0xFF)) {
            fileType = "UTF-16";
        } else if (isTextFile(buffer, buffer.length)) {
            fileType = "UTF-8";
        } else fileType = "Binary";

        return fileType;
    }


    private static boolean isTextFile(byte[] buffer, int bytesRead) {
        for (int i = 0; i < bytesRead - 1; i++) {
            byte b1 = buffer[i];
            System.out.println(b1);
            byte b2 = buffer[i + 1];

            if (b1 == 0 && b2 == 0) {
                return false;
            }
        }
        return true;
    }

}
