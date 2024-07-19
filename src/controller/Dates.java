package controller;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import etc.Qoutes;

public class Dates {

  private String date;

  public String getDate() {
    return this.date;
  }

  public Dates() {
    LocalDate startDate = LocalDate.of(1980, 1, 1);
    LocalDate endDate = LocalDate.of(2024, 12, 31);

    long startEpochDay = startDate.toEpochDay();
    long endEpochDay = endDate.toEpochDay();

    LocalDate randomDate;
    do {
      long randomEpochDay = ThreadLocalRandom.current().nextLong(startEpochDay, endEpochDay);
      randomDate = LocalDate.ofEpochDay(randomEpochDay);
    } while (isLeapYear(randomDate.getYear()));
    Qoutes qouted = new Qoutes(randomDate.toString());
    this.date = qouted.getQouted();
  }

  private boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }
}
