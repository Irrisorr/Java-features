// 

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            File file = new File(bf.readLine());
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(bf.readLine());

//            String fileType;
//            byte[] bytes = fis.readAllBytes();
//            boolean hasBinary = IntStream.range(0, bytes.length)
//                    .map(i -> Byte.toUnsignedInt(bytes[i]))
//                    .anyMatch(b -> b > 127);
//
//            if (hasBinary) {
//                fileType = "Binary";
//            } else {
//                fileType = "Text";
//            }

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
}