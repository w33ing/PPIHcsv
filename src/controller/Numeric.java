package controller;

public class Numeric {

  private int len;
  private String lengthCounter;
  private boolean addAbnormal;

  public void setNumeric(String len, String lengthCounter, boolean addAbnormal) {
    this.len = Integer.parseInt(len);
    this.lengthCounter = lengthCounter;
    this.addAbnormal = addAbnormal;
  }

  public String getNumeric() {
    int range = this.len;
    StringBuilder res = new StringBuilder();
    if (String.valueOf(range).length() > 4){
      range = Integer.parseInt(String.valueOf(range).substring(2));
    }
    
    for (int j = 0; j < range - this.lengthCounter.length() ; j++) {
      if (j == 0 && !(range < 4)) {
        res.append("9");
      } else if (addAbnormal) {
        res.append("x");
      } else {
        res.append("0");
      }
    }
    return res.toString() + this.lengthCounter;
  }
}