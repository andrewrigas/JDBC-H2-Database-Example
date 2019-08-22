package com.utilities

import java.sql.{Connection, DriverManager}

object jdbcUtilities {
 Class.forName("org.h2.Driver")
 implicit val conn: Connection = DriverManager.getConnection( "jdbc:h2:~/tmp", "sa", "" )
}
