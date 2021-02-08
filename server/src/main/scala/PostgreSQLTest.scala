import play.api.libs.json.{Json, Reads}
import slick.jdbc.H2Profile.api._

import java.sql.Timestamp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
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

    private val db = Database.forConfig("db")
    private val users = TableQuery[UsersTable]


//    val usernameFilter = users.filter(_.username === "username1338").result
    val insertUserQuery = users returning users.map(_.userid) += User("usernameg3th6446hg64g3t", "password", "7gtvy5t3gbyb5y3tg8by0")
    val resultFuture = db.run(insertUserQuery)

    Try(Await.ready(resultFuture, 5.second)) match {
        case Success(f) => f.value.get match {
            case Success(userid) => println("User registered.", userid)
            case Failure(e) => println(s"ERROR: ${e.getMessage}", 0)
        }
        case Failure(e) => println(s"DB timeout: ${e.getMessage}", 0)
    }




}