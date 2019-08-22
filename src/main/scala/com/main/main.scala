package com.main

import java.sql
import java.sql.{Connection, Date, PreparedStatement, ResultSet, Timestamp}
import java.time.OffsetDateTime
import java.util.Calendar

import com.utilities.jdbcUtilities._
import com.utilities.h2Utilities._
import com.helper.Helper._

object main extends App {

  case class Dates(startDate: OffsetDateTime,endDate: OffsetDateTime)

  val createTable =
    """
      |drop table if exists datesExample; create table if not exists datesExample(
      | startDate timestamp with time zone,
      | endDate timestamp with time zone
      |);
      |""".stripMargin

  val insertOfferQuery =
    """
      | insert into datesExample(startDate,endDate)
      |  values(?,?);
      |""".stripMargin

  val newsShow1StartTime = offsetDateTimeGenerator(2019,2,1,8,0)
  val newsShow1EndTime = offsetDateTimeGenerator(2019,2,1,9,0)

  val newsShow2StartTime = offsetDateTimeGenerator(2019,2,1,9,0)
  val newsShow2EndTime = offsetDateTimeGenerator(2019,2,1,10,0)

  val newsShow3StartTime = offsetDateTimeGenerator(2019,2,1,10,0)
  val newsShow3EndTime = offsetDateTimeGenerator(2019,2,1,11,0)

  val newsShow4StartTime = offsetDateTimeGenerator(2019,2,1,11,0)
  val newsShow4EndTime = offsetDateTimeGenerator(2019,2,1,12,0)

  val newsShow5StartTime = offsetDateTimeGenerator(2019,2,1,12,0)
  val newsShow5EndTime = offsetDateTimeGenerator(2019,2,1,13,0)

  val newsShow6StartTime = offsetDateTimeGenerator(2019,2,1,13,0)
  val newsShow6EndTime = offsetDateTimeGenerator(2019,2,1,14,0)

  execute(createTable)

  insertOffer(insertOfferQuery,newsShow1StartTime,newsShow1EndTime)
  insertOffer(insertOfferQuery,newsShow2StartTime,newsShow2EndTime)
  insertOffer(insertOfferQuery,newsShow3StartTime,newsShow3EndTime)
  insertOffer(insertOfferQuery,newsShow4StartTime,newsShow4EndTime)
  insertOffer(insertOfferQuery,newsShow5StartTime,newsShow5EndTime)
  insertOffer(insertOfferQuery,newsShow6StartTime,newsShow6EndTime)

  def getDates(startsAt: OffsetDateTime,endsAt: OffsetDateTime)(implicit connection: Connection) = {

    def getDateFromResultSet(resultSet: ResultSet, dates: Vector[Dates]): Vector[Dates] =
      if (resultSet.next()) {
        val startTime  = resultSet.getTimestamp("startDate")
        val endTime    = resultSet.getTimestamp("endDate")
        val date= Dates(
          convertTimestampToOffsetDateTime(startTime),
          convertTimestampToOffsetDateTime(endTime)
        )
        getDateFromResultSet(resultSet, dates :+ date)
      } else {
        dates
      }

    val query =
      """
        |select * from datesExample
        |WHERE
        |((startDate > ? AND startDate < ?) OR (endDate < ? AND endDate > ?) OR (startDate <= ? AND endDate >= ?))
        |AND
        |(dateadd(minute,7,startDate) < ? AND dateadd(minute,-7,endDate) > ? AND dateadd(minute,-7,?) != ?)
        |;
        |""".stripMargin

    val queryDates: PreparedStatement =
      connection.prepareStatement(query)
    queryDates.setTimestamp(1, convertOffsetDateTimeToTimestamp(startsAt))
    queryDates.setTimestamp(2, convertOffsetDateTimeToTimestamp(endsAt))
    queryDates.setTimestamp(3, convertOffsetDateTimeToTimestamp(endsAt))
    queryDates.setTimestamp(4, convertOffsetDateTimeToTimestamp(startsAt))
    queryDates.setTimestamp(5, convertOffsetDateTimeToTimestamp(startsAt))
    queryDates.setTimestamp(6, convertOffsetDateTimeToTimestamp(endsAt))
    queryDates.setTimestamp(7, convertOffsetDateTimeToTimestamp(endsAt))
    queryDates.setTimestamp(8, convertOffsetDateTimeToTimestamp(startsAt))
    queryDates.setTimestamp(9, convertOffsetDateTimeToTimestamp(endsAt))
    queryDates.setTimestamp(10, convertOffsetDateTimeToTimestamp(startsAt))
    queryDates.executeQuery()
    val resultSet: ResultSet = queryDates.executeQuery()
    getDateFromResultSet(resultSet, Vector[Dates]())
  }

  val StartTime = offsetDateTimeGenerator(2019,2,1,10,53)
  val EndTime = offsetDateTimeGenerator(2019,2,1,11,8)

  val validDates = if(EndTime.compareTo(StartTime) == 1) getDates(StartTime,EndTime) else Vector()

  println(validDates.size)
  for(date <- validDates) println(date)

}
