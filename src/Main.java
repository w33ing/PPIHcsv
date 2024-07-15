import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private List<String> headers;
    private List<List<String>> rows;
    private static final String[] nulls = {String.format("\"%s\"", ""), "", "null", "\"null\"", "NULL", "\"NULL\""};
    private static final String[] errorDate = {"2023-02-29", "2024-xx-24", "06-24-2024", "2024-06-32"};
    private static final String[] errorTimestamp = {
            "2023-02-29 12:59:59",
            "2024-xx-24 12:59:59",
            "06-24-2024 12:59:59",
            "2024-06-32 12:59:59",
            "2024-06-32 12:60:59",
            "2024-06-32 25:59:59",
            "2024-06-32 12:59:60"
    };

    private static String[] types;

    public Main() {
        headers = new ArrayList<>();
        rows = new ArrayList<>();
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void addRow(List<String> row) {
        rows.add(row);
    }

    public void generateCSV(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write headers
            writer.append(String.join(",", headers));
            writer.append("\n");

            // Write rows
            for (List<String> row : rows) {
                writer.append(String.join(",", row));
                writer.append("\n");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main generator = new Main();

        System.out.print("Column names separated by comma: ");
        String[] arrCol = scanner.nextLine().replace("\"", "").split(",");
        int numCol = arrCol.length;

        System.out.print("Datatypes separated by comma: ");
        types = scanner.nextLine().split(",");

        System.out.print("Length separated by comma: ");
        String[] lens = scanner.nextLine().split(",");

        List<String> headers = new ArrayList<>();
        for (String cols : arrCol) {
            headers.add(cols);
        }

        generator.setHeaders(headers);

        System.out.print("Enter the number of rows: ");
        int numberOfRows = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        long numbers = 0;
        int nullCount = 0;
        int dateCount = 0;
        int timeCount = 0;

        boolean isNulling = false;
        boolean isMaxing = false;
        boolean isType = false;
        boolean sentryDate = false;

        for (int i = 0; i < numberOfRows; i++) {

            List<String> row = new ArrayList<>();
            String res = "";

            for (int j = 0; j < numCol; j++) {
                switch (types[j].toLowerCase().replace("+n", "").replace("+o", "").replace("+ab", "").replace("+d", "")) {
                    case "string":
                        if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
                            row.add(nulls[nullCount]);
                            isNulling = true;
                        } else if (nullCount > 5 && !types[j].contains("+n")) {
                            nullCount = 0;
                            types[j] = types[j] + "+n";
                            isNulling = false;
                            row.add(randomString(Integer.parseInt(lens[j]), true, false));
                        } else {
                            if (i >= numCol * 6 && !types[j].contains("+o") && isMaxing == false) {
                                types[j] = types[j] + "+o";
                                isMaxing = true;
                                row.add(randomString(Integer.parseInt(lens[j]) + 1, true, false));
                            } else {
                                row.add(randomString(Integer.parseInt(lens[j]), true, false));
                            }
                        }
                        break;
                    case "int":
                        if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
                            row.add(nulls[nullCount]);
                            isNulling = true;
                        } else if (nullCount > 5 && !types[j].contains("+n")) {
                            nullCount = 0;
                            types[j] = types[j] + "+n";
                            isNulling = false;
                            res = testNumbers(lens[j], String.valueOf(i), false);
                            res += i;
                            row.add(addDoubleQoutes(res));
                            res = "";
                        } else {
                            if (i >= numCol * 6 && !types[j].contains("+o") && isMaxing == false) {
                                int len = Integer.parseInt(lens[j]) + 1;
                                res = testNumbers(String.valueOf(len), String.valueOf(i), false);
                                res += i;
                                row.add(addDoubleQoutes(res));
                                res = "";
                                isMaxing = true;
                                types[j] = types[j] + "+o";
                            } else {
                                if (checkAbnormality("+o") && !types[j].contains("+ab") && isType == false) {
                                    res = testNumbers(lens[j], String.valueOf(i), true);
                                    types[j] = types[j] + "+ab";
                                    isType = true;
                                } else {
                                    res = testNumbers(lens[j], String.valueOf(i), false);
                                }
                                res += i;
                                row.add(addDoubleQoutes(res));
                                res = "";
                            }
                        }
                        break;
                    case "date":
                        if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
                            row.add(nulls[nullCount]);
                            isNulling = true;
                        } else if (nullCount > 5 && !types[j].contains("+n")) {
                            nullCount = 0;
                            types[j] = types[j] + "+n";
                            isNulling = false;
                            row.add(randomDate());
                        } else {
                            //not good
                            if (Arrays.stream(types).filter(s -> s.contains("int")).allMatch(s -> s.contains("+ab")) && dateCount < errorDate.length && !isType) {
                                row.add(addDoubleQoutes(errorDate[dateCount]));
                                dateCount++;
                            } else if(Arrays.stream(types).noneMatch(s -> s.contains("int")) && (Arrays.stream(types).filter(s -> s.contains("string")).findAny().isPresent() && Arrays.stream(types).filter(s -> s.contains("string")).allMatch(s -> s.contains("+o"))) && dateCount < errorDate.length && !isType) {
                                row.add(addDoubleQoutes(errorDate[dateCount]));
                                dateCount++;
                            }
                            else if (dateCount > errorDate.length - 1 && !types[j].contains("+d")) {
                                dateCount = 0;
                                types[j] = types[j] + "+d";
                                sentryDate = false;
                                row.add(randomDate());
                            } else {
                                row.add(randomDate());
                            }
                        }
                        break;
                    case "timestamp":
                        if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
                            row.add(nulls[nullCount]);
                            isNulling = true;
                        } else if (nullCount > 5 && !types[j].contains("+n")) {
                            nullCount = 0;
                            types[j] = types[j] + "+n";
                            isNulling = false;
                            row.add(randomDateTime());
                        } else {
                            row.add(randomDateTime());
                        }
                        break;
                }
                System.out.println(types[j]);
            }

            nullCount++;
            isNulling = false;
            isMaxing = false;
            isType = false;
            generator.addRow(row);

        }

        System.out.print("Enter the file name to save the CSV: ");
        String fileName = scanner.nextLine();

        try {
            generator.generateCSV(fileName);
            System.out.println("CSV file generated successfully: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        scanner.close();
    }

    public static boolean checkAbnormality(String d) {
        for (String element : types) {
            if (element.contains("date")) {
                continue;
            }
            if (element.contains("timestamp")) {
                continue;
            }
            if (!element.contains(d)) {
                return false;
            }
        }
        return true;
    }

    public static String testNumbers(String len, String row, boolean addAbnormal) {

        int range = Integer.parseInt(len);
        StringBuilder res = new StringBuilder();

        for (int j = 0; j < range - row.length(); j++) {
            if (j == 0) {
                res.append("9");
            } else if (addAbnormal) {
                res.append("x");
            } else {
                res.append("0");
            }
        }
        return res.toString();
    }

    public static String randomString(int len, boolean isLetter, boolean isNumber) {
        StringBuilder sb = new StringBuilder();
        int start = (int) Math.pow(10, len - 1) + 1;
        int cnt = 1;

        if (isLetter) {
            for (int i = 0; i < len; i++) {
                String cell = "";
                if (cnt < 10) {
                    cell += "x";
                    cnt++;
                } else {
                    cell += "X";
                    cnt = 1;
                }
                sb.append(cell);
            }
        } else if (isNumber) {
            for (int i = start; i < start + 3; i++) {
                sb.append(i);
            }
        }
        return addDoubleQoutes(sb.toString());
    }

    public static String randomDateTime() {
        LocalDateTime startDateTime = LocalDateTime.of(1980, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999_999_999);

        long startEpochSecond = startDateTime.toEpochSecond(ZoneOffset.UTC);
        long endEpochSecond = endDateTime.toEpochSecond(ZoneOffset.UTC);
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);

        int startNano = startDateTime.getNano();
        int endNano = endDateTime.getNano();
        int randomNano = ThreadLocalRandom.current().nextInt(startNano, endNano + 1);

        String timestamp = LocalDateTime.ofEpochSecond(randomEpochSecond, randomNano, ZoneOffset.UTC).toString();
        return addDoubleQoutes(timestamp.replace("T", " "));
    }

    public static String randomDate() {
        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);

        return addDoubleQoutes(LocalDate.ofEpochDay(randomEpochDay).toString());
    }

    public static String addDoubleQoutes(String text) {
        return "\"" + text + "\"";
    }
}