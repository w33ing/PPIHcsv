import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import Model.donkiModel;
import controller.*;
import etc.Qoutes;

public class Main {
  private List<String> headers;
  private List<List<String>> rows;
  private static final String[] nulls = { String.format("\"%s\"", ""), "", "null", "\"null\"", "NULL", "\"NULL\"" };
  private static final String[] errorDate = { "2023-02-29", "2024-Jun-24", "06-24-2024", "2024-06-32" };
  private static final String[] errorTimestamp = {
      "2023-02-29 12:59:59",
      "2024-Apr-24 12:59:59",
      "06-24-2024 12:59:59",
      "2024-06-32 12:59:59",
      "2024-06-24 12:60:59",
      "2024-06-24 25:59:59",
      "2024-06-24 12:59:60"
  };

  private static String[] types;
  private static String[] requiredDatatypes;
  private static boolean null_check = false;
  private static boolean length_check = false;
  private static boolean type_check = false;
  private static boolean date_check = false;
  private static boolean time_check = false;

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
    try (FileOutputStream fos = new FileOutputStream(fileName);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw)) {

      writer.write('\uFEFF');
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

    
    //価格判定データ
    //パートナーマスタ
   

    System.out.print("Enter table name: ");
    String table = scanner.nextLine();

    mappingSourceTable map = new mappingSourceTable(table);
    donkiTableSearch don = new donkiTableSearch(table);

    List<String> sourceTableToMap = map.getFilteredMapping();
    List<String> col = new ArrayList<>();
    List<String> datatype = new ArrayList<>();
    List<String> stringLength = new ArrayList<>();
    
    List<donkiModel> dons = don.getDonkiTable();

    for (donkiModel d : don.getDonkiTable()) {
      col.add(d.getColumnName());
      datatype.add(d.getDatatype());
      stringLength.add(d.getColumnLength());
    }

    String cols = String.join(",", col);
    String datatypes = String.join(",", datatype);
    String stringLengths = String.join(",", stringLength);

    String[] column = cols.split(",");
    int numCol = column.length;

    types = datatypes.split(",");
    String[] lens = stringLengths.split(",");


    List<String> headers = new ArrayList<>();
    for (String colss : column) {
      headers.add(colss);
    }

    generator.setHeaders(headers);

    int nullCount = 0;
    int dateCount = 0;
    int timeCount = 0;

    boolean isNulling = false;
    boolean isMaxing = false;
    boolean isType = false;
    boolean isOnDate = false;
    boolean isOnTime = false;
    List<String> numberOfColumns = new ArrayList<>();
      for(int i = 0; i < numCol; i++){
        if(sourceTableToMap.contains(column[i])){
            numberOfColumns.add(types[i]);
          }
          else types[i] = "--";
      }

    requiredDatatypes = Arrays.stream(types).filter(s -> !s.equals("--")).toArray(String[]::new);

    int rowCount = pRow(numberOfColumns);
    Numeric num = new Numeric();
    Syllabary sylla = new Syllabary();

    // rows
    for (int i = 0; i < rowCount; i++) {
      List<String> row = new ArrayList<>();
      String res = "";

      // columns
      for (int j = 0; j < numCol; j++) {
        if (!sourceTableToMap.contains(column[j])) {
          row.add("\"0\"");
          continue;
        }

        switch (types[j].toLowerCase().replace("+n", "").replace("+o", "").replace("+ab", "").replace("+d", "")) {
          case "varchar":
            if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
              row.add(nulls[nullCount]);
              isNulling = true;
            } else if (nullCount > 5 && !types[j].contains("+n")) {
              nullCount = 0;
              types[j] = types[j] + "+n";
              isNulling = false;
              sylla.setSyllabary(Integer.parseInt(lens[j]), true, false);
              row.add(sylla.getSyllabary());
            } else {
              if (i >= numCol * 6 && !types[j].contains("+o") && isMaxing == false) {
                types[j] = types[j] + "+o";
                isMaxing = true;
                sylla.setSyllabary(Integer.parseInt(lens[j]) + 1, true, false);
                row.add(sylla.getSyllabary());
              } else {
                sylla.setSyllabary(Integer.parseInt(lens[j]), true, false);
                row.add(sylla.getSyllabary());
              }
            }
            break;
          case "nvarchar2":
            if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
              row.add(nulls[nullCount]);
              isNulling = true;
            } else if (nullCount > 5 && !types[j].contains("+n")) {
              nullCount = 0;
              types[j] = types[j] + "+n";
              isNulling = false;
              sylla.setSyllabary(Integer.parseInt(lens[j]), true, false);
              row.add(sylla.getSyllabary());
            } else {
              if (i >= (requiredDatatypes.length * 6) && !types[j].contains("+o") && isMaxing == false) {
                types[j] = types[j] + "+o";
                isMaxing = true;
                sylla.setSyllabary(Integer.parseInt(lens[j]) + 1, true, false);
                row.add(sylla.getSyllabary());
              } else {
                sylla.setSyllabary(Integer.parseInt(lens[j]), true, false);
                row.add(sylla.getSyllabary());
              }
            }
            break;
          case "number":
            if (lens[j].contains(".")) {
              Fraction frac = new Fraction();
              String[] size = lens[j].split("\\.");

              if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
                row.add(nulls[nullCount]);
                isNulling = true;
              } else if (nullCount > 5 && !types[j].contains("+n")) {
                nullCount = 0;
                types[j] = types[j] + "+n";
                isNulling = false;
                num.setNumeric(size[0], String.valueOf(i), false);
                frac.setDecimal(size[1], String.valueOf(i), false);
                res = num.getNumeric();
                res += i;
                if (Integer.parseInt(size[1]) == 0) {
                  // なし
                } else {
                  res += "." + frac.getDecimal();
                  res += i;
                }
                Qoutes qouted = new Qoutes(res);
                row.add(qouted.getQouted());
                res = "";
              } else {
                if (i >= (requiredDatatypes.length * 6) && !types[j].contains("+o") && isMaxing == false) {
                  int len = Integer.parseInt(size[0]);
                  int scale = Integer.parseInt(size[1]) + 1;
                  frac.setDecimal(String.valueOf(scale), String.valueOf(i), false);
                  num.setNumeric(String.valueOf(len), String.valueOf(i), false);
                  res = num.getNumeric();
                  res += i;
                  res += "." + frac.getDecimal();
                  res += i;

                  Qoutes q = new Qoutes(res);
                  row.add(q.getQouted());
                  res = "";
                  isMaxing = true;
                  types[j] = types[j] + "+o";
                } else {
                  if (checkAbnormality("+o") && !types[j].contains("+ab") && isType == false) {

                    num.setNumeric(size[0], String.valueOf(i), true);
                    frac.setDecimal(size[1], String.valueOf(i), true);
                    res = num.getNumeric();
                    res += i;
                    if (Integer.parseInt(size[1]) == 0) {
                      // なし
                    } else {
                      res += "." + frac.getDecimal();
                      res += i;
                    }
                    types[j] = types[j] + "+ab";
                    isType = true;
                  } else {
                    num.setNumeric(size[0], String.valueOf(i), false);
                    frac.setDecimal(size[1], String.valueOf(i), false);
                    res = num.getNumeric();
                    res += i;

                    if (Integer.parseInt(size[1]) == 0) {
                      // なし
                    } else {
                      res += "." + frac.getDecimal();
                      res += i;
                    }
                  }
                  // res += i;
                  Qoutes q = new Qoutes(res);
                  row.add(q.getQouted());
                  res = "";
                }
              }
            } else {
              //numeric abnormal did not take effect
              //no decimal
              if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
                row.add(nulls[nullCount]);
                isNulling = true;
              } else if (nullCount > 5 && !types[j].contains("+n")) {
                nullCount = 0;
                types[j] = types[j] + "+n";
                isNulling = false;
                num.setNumeric(lens[j], String.valueOf(i), false);
                res = num.getNumeric();
                // res += i;

                Qoutes qouted = new Qoutes(res);
                row.add(qouted.getQouted());
                res = "";
              } else {
                if (i >= (requiredDatatypes.length * 6) && !types[j].contains("+o") && isMaxing == false) {
                  int len = Integer.parseInt(lens[j]) + 1;
                  num.setNumeric(String.valueOf(len), String.valueOf(i), false);
                  res = num.getNumeric();
                  // res += i;

                  Qoutes q = new Qoutes(res);
                  row.add(q.getQouted());
                  res = "";
                  isMaxing = true;
                  types[j] = types[j] + "+o";
                } else {
                  if (checkAbnormality("+o") && !types[j].contains("+ab") && isType == false) {

                    num.setNumeric(lens[j], String.valueOf(i), true);
                    res = num.getNumeric();

                    types[j] = types[j] + "+ab";
                    isType = true;
                  } else {
                    num.setNumeric(lens[j], String.valueOf(i), false);
                    res = num.getNumeric();
                  }
                  // res += i;
                  Qoutes q = new Qoutes(res);
                  row.add(q.getQouted());
                  res = "";
                }
              }
            }

            break;
          case "date":
            Dates date = new Dates();

            if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
              row.add(nulls[nullCount]);
              isNulling = true;
            } else if (nullCount > 5 && !types[j].contains("+n")) {
              nullCount = 0;
              types[j] = types[j] + "+n";
              isNulling = false;
              row.add(date.getDate());
            } else {
              if (null_check && length_check && type_check && dateCount < errorDate.length && isOnDate == false && isOnTime == false) {
                Qoutes q = new Qoutes(errorDate[dateCount]);
                row.add(q.getQouted());
                isOnDate = true;
                dateCount++;
              } else {
                row.add(date.getDate());
              }

              if (dateCount > errorDate.length - 1 && date_check == false) {
                types[j] = types[j] + "+d";
              }

            }
            break;
          case "timestamp":
            Datetime datetime = new Datetime();

            if (nullCount < 6 && !types[j].contains("+n") && isNulling == false) {
              row.add(nulls[nullCount]);
              isNulling = true;
            } else if (nullCount > 5 && !types[j].contains("+n")) {
              nullCount = 0;
              types[j] = types[j] + "+n";
              isNulling = false;
              row.add(datetime.getDatetime());
            } else {
              if (null_check && length_check && type_check && date_check && (timeCount < errorTimestamp.length) && isOnDate == false && isOnTime == false) {
                Qoutes qouted = new Qoutes(errorTimestamp[timeCount]);
                row.add(qouted.getQouted());
                isOnTime = true;
                timeCount++;
              } else
                row.add(datetime.getDatetime());
            }
            break;
        }
      }

      nullCount++;
      isNulling = false;
      isMaxing = false;
      isType = false;
      isOnDate = false;
      isOnTime = false;
      checking();
      generator.addRow(row);
    }

    String fileName = table + ".csv";

    try {
      generator.generateCSV(fileName);
      System.out.println("CSV file generated successfully: " + fileName);
    } catch (IOException e) {
      System.out.println("Error writing to file: " + e.getMessage());
    }
    scanner.close();

  }

  public static void checking() {
    String[] requiredColumns = Arrays.stream(types).filter(s -> !s.equals("--")).toArray(String[]::new); 

    if (Arrays.stream(requiredColumns).allMatch(s -> s.contains("+n"))) {
      null_check = true;
    }

    boolean containsStringOrInt = Arrays.stream(requiredColumns)
        .anyMatch(s -> s.contains("nvarchar2") || s.contains("number"));

    if (containsStringOrInt) {
      boolean allStringAndIntContainO = Arrays.stream(requiredColumns)
          .filter(s -> s.contains("nvarchar2") || s.contains("number"))
          .allMatch(s -> s.contains("+o"));

      if (allStringAndIntContainO) {
        length_check = true;
      }
    }

    if (Arrays.stream(requiredColumns).anyMatch(s -> s.contains("number")) && Arrays.stream(requiredColumns)
        .filter(s -> s.contains("number"))
        .allMatch(s -> s.contains("+ab"))) {
      type_check = true;
    } else if (Arrays.stream(requiredColumns).noneMatch(s -> s.contains("number"))) {
      type_check = true;
    }

    if (Arrays.stream(requiredColumns).anyMatch(s -> s.contains("date")) && Arrays.stream(requiredColumns)
        .filter(s -> s.contains("date"))
        .allMatch(s -> s.contains("+d"))) {
      date_check = true;
    }

    if (!Arrays.stream(requiredColumns).anyMatch(s -> s.contains("date"))) {
      date_check = true;
    }

  }

  private static int pRow(List<String> header) {
    int countVarchar = 0;
    int countNumber = 0;
    int countDate = 0;
    int countTime = 0;
    int countDecimal = 0;

    for (String h : header) {
      if (h.equalsIgnoreCase("NVARCHAR2")) {
        countVarchar++;
      }

      if(h.equalsIgnoreCase("NUMBER")){
        countNumber++;
      }

      if (h.equalsIgnoreCase("DATE")) {
        countDate = errorDate.length;
      }
      if (h.equalsIgnoreCase("TIMESTAMP")) {
        countTime = errorTimestamp.length;
      }
    }
    return (header.size() * 6) + (countVarchar + (countNumber * 2) + countDate + countTime);
  }

  private static boolean checkAbnormality(String d) {
    String[] arr = Arrays.stream(types).filter(s -> !s.equals("--")).toArray(String[]::new);
    for (String element : arr) {
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
}