package com.utilities

import java.sql.{Connection, ResultSet, Statement}
import java.time.OffsetDateTime
import com.helper.Helper._

object h2Utilities {


  def execute(query: String)(implicit conn: Connection): Boolean = {
    implicit val statement: Statement = conn.createStatement()
    tryFinally(query,statement.execute)
  }

  def executeQuery(query: String)(implicit conn: Connection): ResultSet = {
    implicit val statement = conn.createStatement()
    try{
      statement.executeQuery(query)
    }
  }

  def executeUpdate(query: String)(implicit conn: Connection): Int = {
    implicit val statement = conn.createStatement()
    tryFinally(query,statement.executeUpdate)
  }

  def insertOffer(query: String, startsAt: OffsetDateTime,endsAt: OffsetDateTime)(implicit connection: Connection): Boolean = {
    val queryOffers = connection.prepareStatement(query)
    queryOffers.setTimestamp(1, convertOffsetDateTimeToTimestamp(startsAt))
    queryOffers.setTimestamp(2, convertOffsetDateTimeToTimestamp(endsAt))
    queryOffers.execute()
  }
  private def tryFinally[T](query: String,func: (String) => T)(implicit stm: Statement): T = {
    try{
      func(query)
    } finally {
      stm.close()
    }
  }
}
