import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
        try {
            File fileIn = new File("/home/irrisorr/Desktop/Java-features/Sergey_tasks/FI.txt");
            File fileOut = new File("/home/irrisorr/Desktop/Java-features/Sergey_tasks/FO.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIn));
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

            // 1.4-5 Total words and chars
            int totalChars = 0;
            int totalWords = 0;
            boolean isNewWord = true;
            int c;
            while ((c = bufferedReader.read()) != -1) {
                //обработка того что может быть два раза подряд новая строка
                if (!Character.isLetterOrDigit(c)) {
                    isNewWord = true;
                    continue;
                }

                if (isNewWord) {
                    isNewWord = false;
                    totalWords++;
                }

                totalChars++;
            }

            String fileNameToFO =
                    String.format("Name of input file: %s\nFile size: %d (bytes)\nFile type: %s\nTotal chars: %d\nTotal words: %d",
                            fullFileName, fileSize, fileType, totalChars, totalWords);

            fileWriter.write(fileNameToFO);

            bufferedReader.close();
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
            // System.out.println(b1);
            byte b2 = buffer[i + 1];

            if (b1 == 0 && b2 == 0) {
                return false;
            }
        }
        return true;
    }


    private static int[] getBOM(String fileType) {
        int divider, firstBytes;
        switch (fileType) {
            case "UTF-32" -> {divider = 4; firstBytes = 4;}
            case "UTF-16" -> {divider = 2; firstBytes = 2;}
            case "UTF-8"  -> {divider = 1; firstBytes = 0;}
            default -> throw new IllegalStateException("Unexpected value: " + fileType);
        }

        return new int[]{divider, firstBytes};
    }

}
