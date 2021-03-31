import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import play.api.libs.json.{Json, OWrites, Reads, Writes}
import server.CommentsTable
import slick.jdbc.H2Profile.api._
import sun.invoke.empty.Empty

import java.sql.Timestamp
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import spray.json.RootJsonFormat

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

case class Comment (
                           commentid: Int,
                           postid: Int,
                           userid: Int,
                           comment: String,
                           updated_at: Timestamp
                   )

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
                        descriptorid: String,
                        description: String
                )

class PostsTable(tag: Tag) extends Table[Post](tag,"posts") {
    def postid = column[Int]("postid", O.PrimaryKey, O.AutoInc)
    def creatorid = column[Int]("creatorid")
    def title  = column[String]("title")
    def descriptorid = column[String]("descriptorid")
    def created_at = column[Timestamp]("created_at")
    def updated_at = column[Timestamp]("updated_at")
    def description = column[String]("description")
    def * = (creatorid, title, descriptorid, description).mapTo[Post]
}


object PostgreSQLTest extends App {

    val db = Database.forConfig("db")
    val posts = TableQuery[PostsTable]

    val commentsWithPostID = posts.take(10).result
    val resultFuture = db.run(commentsWithPostID)

    Try(Await.ready(resultFuture, 5.second)) match {
        case Success(f) => f.value.get match {
            case Success(res) => res.length match {
                case 0 => ("No comments.", "")
                case l if l > 0 => {
                    val comment = res.head

                    implicit val commentWrites = Json.writes[PostsTable#TableElementType]
                    implicit val commentsWrites = Writes.seq[PostsTable#TableElementType]
                    val commentsJson = Json.toJson(res)
                    println(commentsJson.toString())
                }
                case _ => (s"ERROR 1: ${res}", "{}")
            }
            case Failure(e) => (s"ERROR 2: ${e.getMessage}", "{}")
            case _ => (s"ERROR 3: ${f}", "{}")
        }
        case Failure(e) => (s"DB timeout. $e", "{}")
    }


}