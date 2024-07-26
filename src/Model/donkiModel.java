package Model;

public class donkiModel {
  public String tablename;
  public String columnname;
  public String columndatatype;
  public String columnlength;

  public void setTableName(String tablename) {
    this.tablename = tablename;
  }

  public String getTableName() {
    return this.tablename;
  }

  public void setColumnName(String columnname) {
    this.columnname = columnname;
  }

  public String getColumnName() {
    return this.columnname;
  }

  public void setDatatype(String columndatatype) {
    this.columndatatype = columndatatype;
  }

  public String getDatatype() {
    return this.columndatatype;
  }

  public void setColumnLength(String columnlength) {
    this.columnlength = columnlength;
  }

  public String getColumnLength() {
    return this.columnlength;
  }
}
