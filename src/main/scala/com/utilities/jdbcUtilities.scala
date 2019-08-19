package com.utilities

import java.sql.{Connection, DriverManager}

object jdbcUtilities {
 implicit val conn: Connection = DriverManager.getConnection( "jdbc:h2:~/tmp", "sa", "" )
}
