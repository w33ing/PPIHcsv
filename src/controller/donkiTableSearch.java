package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Model.donkiModel;

public class donkiTableSearch {

  public List<donkiModel> getSeachedDonkiTable;

  public donkiTableSearch(String tablename) {
    String csvFile = "source/tabledonki.csv";
    String line;
    String csvSplitBy = ",";

    List<donkiModel> filteredData = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      while ((line = br.readLine()) != null) {
        String[] columns = line.split(csvSplitBy);
        donkiModel don = new donkiModel();
        if (columns.length < 4)
          continue;
        // Check if the first column is "1" and the fifth column is not empty
        if (columns.length >= 1 && tablename.equals(columns[0])) {
          don.setTableName(columns[0]);
          don.setColumnName(columns[1]);
          don.setDatatype(columns[2]);
          don.setColumnLength(columns[3]);

          filteredData.add(don);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.getSeachedDonkiTable = filteredData;
    // Output the filtered data
    // for (String row : filteredData) {
    // System.out.println(String.join(",", row));
    // }
  }

  public List<donkiModel> getDonkiTable() {
    return this.getSeachedDonkiTable;
  }

  public int Count() {
    return this.getSeachedDonkiTable.size();
  }
}