package com.utilities

import java.sql.{Connection, ResultSet, Statement}

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
    //tryFinally(query,statement.executeQuery)
  }

  private def tryFinally[T](query: String,func: (String) => T)(implicit stm: Statement): T = {
    try{
      func(query)
    } finally {
      stm.close()
    }
  }

}
