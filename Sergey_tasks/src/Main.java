// 

import java.io.*;

public class Main {
    public static void main(String[] args) {

//        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
//             File file = new File(bf.readLine());
//             FileInputStream fi = new FileInputStream(bf.readLine());
//             FileOutputStream fo = new FileOutputStream(bf.readLine())){
//
//            fi.
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            File file = new File(bf.readLine());
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(bf.readLine());

            String fileNameToFO = String.format("Name of input file: %s", file.getName()); 
            fos.write(fileNameToFO.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}