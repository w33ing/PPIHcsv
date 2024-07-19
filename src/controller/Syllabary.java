package controller;

import etc.Qoutes;

public class Syllabary {

  private int len;
  private boolean isLetter;
  private boolean isNumber;

  public void setSyllabary(int len, boolean isLetter, boolean isNumber) {
    this.len = len;
    this.isLetter = isLetter;
    this.isNumber = isNumber;
  }

  public String getSyllabary() {
    StringBuilder sb = new StringBuilder();

    int start = (int) Math.pow(10, this.len - 1) + 1;
    int cnt = 1;

    if (this.isLetter) {
      for (int i = 0; i < this.len; i++) {
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
    } else if (this.isNumber) {
      for (int i = start; i < start + 3; i++) {
        sb.append(i);
      }
    }
    Qoutes result = new Qoutes(sb.toString());
    return result.getQouted();
  }
}
