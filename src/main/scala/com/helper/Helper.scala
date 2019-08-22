package com.helper

import java.sql.Timestamp
import java.time._

object Helper {

  def offsetDateTimeGenerator(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, zoneOffsetHours: Int = 0 , zoneOffsetMinutes: Int = 0): OffsetDateTime = {
    OffsetDateTime.of(LocalDateTime.of(year, month, dayOfMonth, hour, minute),
      ZoneOffset.ofHoursMinutes(zoneOffsetHours, zoneOffsetMinutes))
  }

  def convertTimestampToOffsetDateTime(timestamp: Timestamp): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime), ZoneId.of("UTC"))

  def convertOffsetDateTimeToTimestamp(offsetDateTime: OffsetDateTime): Timestamp =
    Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime)
}