package com.main

import java.sql
import java.sql.{Date, Timestamp}
import java.util.Calendar

import com.utilities.jdbcUtilities._
import com.utilities.h2Utilities._

object main extends App {

  Class.forName("org.h2.Driver")

  val createTable =
    """
      |drop table users; create table if not exists users(
      | id int auto_increment primary key,
      | name varchar(55),
      | date timestamp with time zone
      |);
      |""".stripMargin

  val insertToTable =
    """
      |insert into users(id,name,date)
      |values (1,'andreas','1981-02-03 19:20:21+02:00')
      |""".stripMargin

  import org.h2.api.TimestampWithTimeZone

  lazy val date = new TimestampWithTimeZone(19810203L,0,2.toShort)
  lazy val timestampDate = new Timestamp(Calendar.getInstance().getTime.getTime)
  println(date.toString)
  println(timestampDate.toString)

  val selectTable =
    """
      |select * from users
      |""".stripMargin

  println(execute(createTable))
  println(execute(insertToTable))

  val results = executeQuery(selectTable)

  println(results)
}
