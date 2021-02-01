package server

import scala.slick.driver.PostgresDriver.simple._

object PostgreSQLTest extends App {
    class Users(tag: Tag) extends Table[(Int, String, String, String)](tag, "users") {
        def userid: Column[Int] = column[Int]("userid", O.PrimaryKey)
        def username: Column[String] = column[String]("username")
        def password: Column[String] = column[String]("password")
        def email: Column[String] = column[String]("email")
        def * = (userid, username, password, email)
    }

    val connectionUrl = "jdbc:postgresql://localhost/db1?user=postgres&password=postgres"

    Database.forURL(connectionUrl, driver = "org.postgresql.Driver") withSession {
        implicit session =>
            val users = TableQuery[Users]
            users.list foreach { row =>
                println(row.productIterator.toList)
            }
    }
}
