package server

import slick.jdbc.H2Profile.api._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import java.sql.SQLException

case class User(
                       userid: Int,
                       username: String,
                       password: String,
                       email: String
               )

class UsersTable(tag: Tag) extends Table[User](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (userid, username, password, email).mapTo[User]
}



object PostgreSQLTest extends App {

        val db = Database.forConfig("db")
        def exec [T](action: DBIO[T]): T =
            Await.result(db.run(action), 2.seconds)

        val users = TableQuery[UsersTable]
        val query = users.filter(_.username === "Mike")


        val newUser = Seq(User(5, "MEGACHAD", "dungeonmaster96", "asd@asd.asd"))

        val insert: DBIO[Option[Int]] = users ++= newUser
        val insertAction: Future[Option[Int]] = db.run(insert)
        val rowCount = Await.result(insertAction, 2.seconds)
        println(rowCount)

        val updateQuery = users.filter(_.username === "MEGACHAD")
        exec(updateQuery.map(_.email).update("definetlynotvirgin@gmail.com"))

        val userAction: DBIO[Seq[User]] = users.result
        val userFuture: Future[Seq[User]] = db.run(userAction)
        val userResult = Await.result(userFuture, 2.seconds)
}
