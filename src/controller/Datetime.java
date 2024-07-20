package controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

import etc.Qoutes;

public class Datetime {

  private String datetime;

  public String getDatetime() {
    return this.datetime;
  }

  public Datetime() {
    LocalDateTime startDateTime = LocalDateTime.of(1980, 1, 1, 0, 0);
    LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999_999_999);

    long startEpochSecond = startDateTime.toEpochSecond(ZoneOffset.UTC);
    long endEpochSecond = endDateTime.toEpochSecond(ZoneOffset.UTC);

    LocalDateTime randomDateTime;
    do {
      long randomEpochSecond = ThreadLocalRandom.current().nextLong(startEpochSecond, endEpochSecond);
      int randomNano = ThreadLocalRandom.current().nextInt(0, 1_000_000_000);
      randomDateTime = LocalDateTime.ofEpochSecond(randomEpochSecond, randomNano, ZoneOffset.UTC);
    } while (isLeapYear(randomDateTime));

    String timestamp = randomDateTime.toString();
    Qoutes qoutes = new Qoutes(timestamp.replace("T", " "));
    this.datetime = qoutes.getQouted();
  }

  private boolean isLeapYear(LocalDateTime dateTime) {
    int year = dateTime.getYear();
    return java.time.Year.isLeap(year);
  }
}
