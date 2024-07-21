package controller;

public class Numeric {

  private int len;
  private String row;
  private boolean addAbnormal;

  public void setNumeric(String len, String row, boolean addAbnormal) {
    this.len = Integer.parseInt(len);
    this.row = row;
    this.addAbnormal = addAbnormal;
  }
  public String getNumeric() {
    int range = this.len;
    StringBuilder res = new StringBuilder();

    for (int j = 0; j < range - this.row.length(); j++) {
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
}