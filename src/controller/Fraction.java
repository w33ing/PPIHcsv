package controller;

public class Fraction {
    private int scale;
    private String row;
    private boolean addAbnormal;

    public void setDecimal(String scale, String row, boolean addAbnormal) {
        this.scale = Integer.parseInt(scale);
        this.row = row;
        this.addAbnormal = addAbnormal;
    }

    public String getDecimal() {
        int decimalPoint = this.scale;
        String res = "";

        for(int i = 0; i < decimalPoint - this.row.length(); i++){
            if (addAbnormal) {
                res += "x";
              } else {
                res += "0";
              }
        }
        return res;
    }
}
