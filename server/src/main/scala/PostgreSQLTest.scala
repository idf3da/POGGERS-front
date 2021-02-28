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

    val userid = 56749


    def postsForUser(userid: Int): (String, Seq[PostsTable#TableElementType]) = {
        val postsWithUserID = postsTable.filter(_.creatorid === userid).result
        val resultFuture = db.run(postsWithUserID)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(res) => res.length match {
                    case 0 => ("No posts.", res)
                    case l if l > 0 => ("Found posts.", res)
                    case _ => (s"ERROR 1: ${res}", Seq.empty[PostsTable#TableElementType])
                }
                case Failure(e) => (s"ERROR 2: ${e.getMessage}", Seq.empty[PostsTable#TableElementType])
                case _ => (s"ERROR 3: ${f}", Seq.empty[PostsTable#TableElementType])
            }
            case Failure(e) => (s"DB timeout. $e", Seq.empty[PostsTable#TableElementType])
        }
    }

    val (userPostsResult, posts) = postsForUser(userid)
    userPostsResult match {
        case "No posts." => println((StatusCodes.NotFound, "No comments found for posts."))
        case "Found posts." => println((StatusCodes.OK, posts))
        case _ => println(StatusCodes.InternalServerError, userPostsResult)
    }





}