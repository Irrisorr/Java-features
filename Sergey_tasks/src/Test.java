import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        try {
            File fileIn = new File("/home/irrisorr/Desktop/Java-features/Sergey_tasks/FI.txt");
            String outputFileName = fileIn.getName() + ".stat";
            File fileOut = new File("/home/irrisorr/Desktop/Java-features/Sergey_tasks/" + outputFileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIn));
            FileWriter fileWriter = new FileWriter(fileOut);

            String fileNameToFO = processFile(fileIn, bufferedReader);

            fileWriter.write(fileNameToFO);

            bufferedReader.close();
            fileWriter.close();
        } catch (Exception e) {}

    }


    private static String processFile(File fileIn, BufferedReader bufferedReader) throws IOException {
            // 4 Processing time
            long startTime = System.currentTimeMillis();

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
            boolean isNewWord = false;
            int c;
            Map<Character, Integer> charCount = new HashMap<>();
            StringBuilder sb = new StringBuilder("");
            Map<String, Integer> wordCount = new HashMap<>();
            while ((c = bufferedReader.read()) != -1) {
                //обработка того что может быть два раза подряд новая строка
                if (Character.isWhitespace(c) || Character.isISOControl(c)) {
                    isNewWord = true;
                    continue;
                }

                if (isNewWord) {
                    wordCount.put(sb.toString(), wordCount.getOrDefault(sb.toString(), 0) + 1);
                    sb = new StringBuilder("");
                    isNewWord = false;
                    totalWords++;
                }

                char ch = (char) c;
                sb.append(ch);
                charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);
                totalChars++;
            }
            // добавление последнего слова
            if (sb.length() > 0) {
                String word = sb.toString();
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                totalWords++;
            }

            //2 Character repetition statistics
            List<Map.Entry<Character, Integer>> sortedChars = new ArrayList<>(charCount.entrySet());
            sortedChars.sort(Map.Entry.<Character, Integer>comparingByValue().reversed());

            String fileNameToFO = String.format("Name of input file: %s\nFile size: %d (bytes)\nFile type: %s\nTotal chars: %d\nTotal words: %d\n\n" +
                            "Character repetition statistics:\n%-10s%-10s%-10s\n",
                    fullFileName, fileSize, fileType, totalChars, totalWords, "Char", "Quantity", "Prob. %");


            for (Map.Entry<Character, Integer> entry : sortedChars) {
                double percentage = (double) entry.getValue() / totalChars * 100;
                fileNameToFO += String.format("%-10c%-10d%-10.3f\n", entry.getKey(), entry.getValue(), percentage);
            }

            // 3. Статистика повторения слов
            fileNameToFO += "\nWord repetition statistics:\n";
            fileNameToFO += String.format("%-20s%-10s%-10s\n", "Word", "Quantity", "Prob. %");
            List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCount.entrySet());
            sortedWords.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            for (Map.Entry<String, Integer> entry : sortedWords) {
                double percentage = (double) entry.getValue() / totalWords * 100;
                fileNameToFO += String.format("%-20s%-10d%-10.2f\n", entry.getKey(), entry.getValue(), percentage);
            }
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            fileNameToFO += String.format("\nProcessing time: %d (msec)\n", duration);

            return fileNameToFO;
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
