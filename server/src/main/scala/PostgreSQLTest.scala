import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import play.api.libs.json.{Json, Reads}
import server.{CommentsTable}
import slick.jdbc.H2Profile.api._
import sun.invoke.empty.Empty

import java.sql.Timestamp
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

case class User(
                       username: String,
                       password: String,
                       email: String
               )



class UsersTable(tag: Tag) extends Table[User](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (username, password, email).mapTo[User]
}

case class LoginRequest(
                               username: String,
                               password: String
                       )

object LoginRequestObj {
    implicit val loginRequestReads: Reads[LoginRequest] = Json.reads[LoginRequest]
}

case class Post (
                        creatorid: Int,
                        title: String,
                        descriptorid: Int,
                        description: String
                )

class PostsTable(tag: Tag) extends Table[Post](tag,"posts") {
    def postid = column[Int]("postid", O.PrimaryKey, O.AutoInc)
    def creatorid = column[Int]("creatorid")
    def title  = column[String]("title")
    def descriptorid = column[Int]("descriptorid")
    def created_at = column[Timestamp]("created_at")
    def updated_at = column[Timestamp]("updated_at")
    def description = column[String]("description")
    def * = (creatorid, title, descriptorid, description).mapTo[Post]
}



object PostgreSQLTest extends App {

    val db = Database.forConfig("db")
    val postsTable = TableQuery[PostsTable]
    val usersTable = TableQuery[UsersTable]

    val userid = 999999999

    val userIDFilter = usersTable.filter(_.userid === userid).result
    val resultFuture = db.run(userIDFilter)
    Try(Await.ready(resultFuture, 5.second)) match {
        case Success(f) => f.value.get match {
            case Success(res) => res.length match {
                case 1 => println("Found user", res)
                case 0 => println(s"User not found. ${res}")
                case _ => println(s"Several users found. ${res}")
            }
            case Failure(e) => println(s"User DB ERROR: ${e.getMessage}", -1)
        }
        case Failure(e) => println(s"DB timeout: ${e.getMessage}", -1)
    }



}