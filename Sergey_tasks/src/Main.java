import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            File file = new File(bf.readLine());
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(bf.readLine());

            String fileType;
            String fileEncoding;
            byte[] buffer = new byte[4096];

            int bytesRead = fis.read(buffer);
            if (bytesRead == -1) {
                fileType = "Empty file";
            } else {
                boolean isText = isTextFile(buffer, bytesRead);

                if (isText) {
                    fileType = "Text file";
                } else {
                    fileType = "Binary file";
                }
            }

            String fileNameToFO =
                    String.format("Name of input file: %s\nFile size: %d (bytes)\nFile type: %s",
                            file.getName(), file.length(), fileType);

            fos.write(fileNameToFO.getBytes());

            bf.close();
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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