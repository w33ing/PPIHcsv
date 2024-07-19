package etc;

public class Qoutes {
  private String val;

  public Qoutes(String val) {
    this.val = "\"" + val + "\"";
  }

  public String getQouted() {
    return this.val;
  }
}
