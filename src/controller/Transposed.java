package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Transposed {
  // private int startRow;
  // private int endRow;
  // private int startColumn;
  // private int endColumn;
  private List<List<String>> transposed;

  public Transposed(int startrow, int endrow, int startcolumn, int endcolumn) {
    String inputFile = "source/tabledonki.csv";
    // String outputFile = "source/transposed.csv";

    // this.startRow = startrow; // assuming 0-based index
    // this.endRow = endrow; // inclusive
    // this.startColumn = startcolumn; // assuming 0-based index
    // this.endColumn = endrow; // inclusive

    List<List<String>> data = new ArrayList<>();

    // Read the CSV file
    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
      String line;
      int currentRow = 0;
      while ((line = br.readLine()) != null) {
        if (currentRow >= startrow - 1 && currentRow <= endrow - 1) {
          String[] values = line.split(",");
          List<String> rowData = new ArrayList<>();
          for (int i = startcolumn; i <= endcolumn && i < values.length; i++) {
            rowData.add(values[i]);
          }
          data.add(rowData);
        }
        currentRow++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Transpose the data
    List<List<String>> transposedData = transpose(data);

    this.transposed = transposedData;
    // Write the transformed data to a new CSV file
    // try (FileOutputStream fos = new FileOutputStream(outputFile);
    // OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
    // BufferedWriter writer = new BufferedWriter(osw)) {
    // for (List<String> row : transposedData) {
    // writer.write('\uFEFF');
    // writer.write(String.join(",", row));
    // writer.write("\n");
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
  }

  private static List<List<String>> transpose(List<List<String>> data) {
    List<List<String>> transposed = new ArrayList<>();
    int rowCount = data.size();
    if (rowCount == 0)
      return transposed;

    int colCount = data.get(0).size();
    for (int i = 0; i < colCount; i++) {
      List<String> newRow = new ArrayList<>();
      for (List<String> row : data) {
        newRow.add(row.get(i));
      }
      transposed.add(newRow);
    }
    return transposed;
  }

  public List<List<String>> getTransposed() {
    return this.transposed;
  }
}
