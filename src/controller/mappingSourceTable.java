package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mappingSourceTable {
  private List<String> filteredData;

  public mappingSourceTable(String tablename) {
    String csvFile = "source/masterMappingTables.csv";
    String line;
    String csvSplitBy = ",";

    List<String> filteredData = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      while ((line = br.readLine()) != null) {
        String[] columns = line.split(csvSplitBy);
        if (columns.length < 2)
          continue;
        // Check if the first column is "1" and the fifth column is not empty
        if (columns.length >= 1 && tablename.equals(columns[0]) && !columns[1].isEmpty()) {
          filteredData.add(columns[1]);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.filteredData = filteredData;
    // Output the filtered data
    // for (String row : filteredData) {
    // System.out.println(String.join(",", row));
    // }
  }

  public List<String> getFilteredMapping() {
    return this.filteredData;
  }
}
