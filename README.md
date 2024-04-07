# CheetSheet

## Files

#### Simple read Path of file from the console and written all text in the console too:
```
        try (Scanner scanner = new Scanner(System.in);
             InputStream input = new FileInputStream(scanner.nextLine());
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))){
            while (reader.ready()){
                System.out.println(reader.readLine());
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
```

#### All text is read from the console and written to the file that was submitted as the first line in the console

```
        try(Scanner scanner = new Scanner(System.in);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new FileWriter(scanner.nextLine()))) {
            String word;
            while ((word = bufferedReader.readLine()) != null){
                writer.write(word + "\n");
                if (word.equals("exit"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

```

#### Numbers are readden from file that submitted in console and sorted even numbers are returned
```
        try (Scanner scanner = new Scanner(System.in);
             Scanner scanFile = new Scanner(new FileInputStream(scanner.nextLine()))) {
            int num;
            List<Integer> list = new ArrayList<>();
            while (scanFile.hasNextInt()){
                num = scanFile.nextInt();
                if (num % 2 == 0) list.add(num);
            }
            list.stream().sorted().forEach(System.out::println);
        } catch (IOException e){
            e.printStackTrace();
        }
```

### Lines are readen from file that full path was submitted in variable and are added in ArrayList
```
public static List<String> lines = new ArrayList<String>();

    static {
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
            String line;
            while (reader.ready()){
                line = reader.readLine();
                lines.add(line);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
```

## Input from Console

#### By scanner

```
Scanner scanner = new Scanner(System.in);
```

#### by BufferedReader

```
BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
```

### test
