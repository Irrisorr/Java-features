import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String filePath = null;
        String mask = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-mask-")) {
                mask = args[i].substring(6);
            } else {
                filePath = args[i];
            }
        }

        if (filePath == null) {
            System.out.println("Usage: file/directory [-mask-[mask]]");
            return;
        }

        File fileIn = new File(filePath);
        if (fileIn.isDirectory()) {
            processDirectory(fileIn, mask);
        } else {
            processFile(fileIn);
        }

    }

    /**
     * Processes all files in the given directory with a mask if it was supplied.
     *
     * @param directory The directory to process.
     * @param mask      The file mask for filtering files (if specified).
     */
    private static void processDirectory(File directory, String mask) {
        File[] files = directory.listFiles((dir, name) -> mask == null || name.endsWith(mask));
        for (File file : files) {
            processFile(file);
        }
    }

    /**
     * Processes the given file and generates statistics.
     *
     * @param fileIn The file to process.
     */
    private static void processFile(File fileIn) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIn));
             FileWriter fileWriter = new FileWriter(new File(fileIn.getParent(), fileIn.getName() + ".stat"))){

            long startTime = System.currentTimeMillis();

            String fullFileName = fileIn.getName();
            int fileSize = (int) fileIn.length();

            byte[] buffer = Files.readAllBytes(fileIn.toPath());
            String fileType;

            if (buffer.length > 0) {
                fileType = determineFileType(buffer);
            } else fileType = "File is empty";

            // 1.4-5 Total words and chars
            int totalChars = 0;
            int totalWords = 0;
            boolean isNewWord = false;
            Map<Character, Integer> charCount = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder("");
            Map<String, Integer> wordCount = new HashMap<>();

            int charInt;
            while ((charInt = bufferedReader.read()) != -1) {
                //обработка того что может быть два раза подряд новая строка
                if (Character.isWhitespace(charInt) || Character.isISOControl(charInt)) {
                    isNewWord = true;
                    continue;
                }

                if (isNewWord) {
                    wordCount.put(stringBuilder.toString(), wordCount.getOrDefault(stringBuilder.toString(), 0) + 1);
                    stringBuilder = new StringBuilder("");
                    isNewWord = false;
                    totalWords++;
                }

                char charRead = (char) charInt;
                stringBuilder.append(charRead);
                charCount.put(charRead, charCount.getOrDefault(charRead, 0) + 1);
                totalChars++;
            }
            // добавление последнего слова
            if (stringBuilder.length() > 0) {
                String word = stringBuilder.toString();
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                totalWords++;
            }

            //2 Character repetition statistics
            List<Map.Entry<Character, Integer>> sortedChars = new ArrayList<>(charCount.entrySet());
            sortedChars.sort(Map.Entry.<Character, Integer>comparingByValue().reversed());

            String fileNameToFO = String.format("Name of input file: %s\nFile size: %d (bytes)\nFile type: %s\nTotal chars: %d\nTotal words: %d\n\n" +
                            "Character repetition statistics:\n%-10s%-10s%-10s\n",
                    fullFileName, fileSize, fileType, totalChars, totalWords, "Char", "Quantity", "Prob. %");

            // get char probability
            for (Map.Entry<Character, Integer> entry : sortedChars) {
                double percentage = (double) entry.getValue() / totalChars * 100;
                fileNameToFO += String.format("%-10c%-10d%-10.3f\n", entry.getKey(), entry.getValue(), percentage);
            }

            // 3 Word repetition statistics
            fileNameToFO += "\nWord repetition statistics:\n";
            fileNameToFO += String.format("%-20s%-10s%-10s\n", "Word", "Quantity", "Prob. %");
            List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordCount.entrySet());
            sortedWords.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            // get word probability
            for (Map.Entry<String, Integer> entry : sortedWords) {
                double percentage = (double) entry.getValue() / totalWords * 100;
                fileNameToFO += String.format("%-20s%-10d%-10.2f\n", entry.getKey(), entry.getValue(), percentage);
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            fileNameToFO += String.format("\nProcessing time: %d (msec)\n", duration);

            fileWriter.write(fileNameToFO);

        } catch (Exception e) {}
    }

    /**
     * Determines the file type based on its content.
     *
     * @param buffer The file content as a byte array.
     * @return The file type (UTF-32, UTF-16, UTF-8, or Binary).
     */
    private static String determineFileType(byte[] buffer) {
        String fileType = "Unknown";

        // checks file's BOM (byte order mask)
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

    /**
     * Checks if the file is a text file.
     *
     * @param buffer    The file content as a byte array.
     * @param bytesRead The number of bytes read from the file.
     * @return true if the file is a text file, false otherwise.
     */
    private static boolean isTextFile(byte[] buffer, int bytesRead) {
        for (int i = 0; i < bytesRead - 1; i++) {
            byte b1 = buffer[i];
            // System.out.println(b1);
            byte b2 = buffer[i + 1];

            // if two bytes are next to each other and equal to zero then it is not text
            if (b1 == 0 && b2 == 0) {
                return false;
            }
        }
        return true;
    }

}