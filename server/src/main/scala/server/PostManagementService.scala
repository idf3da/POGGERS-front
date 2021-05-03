package server

import play.api.libs.json.{JsNull, JsValue, Json, Writes}
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class PostManagementService {

    private val db = Database.forConfig("db")
    private val postsTable = TableQuery[PostsTable]

    implicit val postWrites = Json.writes[PostsTable#TableElementType]
    implicit val postsWrites = Writes.seq[PostsTable#TableElementType]

    def createPost(newPost: CreatePostRequest, creatorid: Int): String = {
        val newPostDB: CreatePostRequestDB = CreatePostRequestDB(creatorid, newPost.title, newPost.descriptorid, newPost.description)
        val insertPostQuery = postsTable += newPostDB
        val resultFuture: Future[Int] = db.run(insertPostQuery)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(_) => "Post created."
                case Failure(e) => s"ERROR: ${e.getMessage}"
            }
            case Failure(e) => s"DB timeout. $e"
        }
    }

    def postsForUser(userid: Int): (String, JsValue) = {
        val postsWithUserID = postsTable.filter(_.creatorid === userid).result
        val resultFuture = db.run(postsWithUserID)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(res) => res.length match {
                    case 0 => ("No posts.", JsNull)
                    case l if l > 0 => ("Found posts.", Json.toJson(res).apply(0))
                    case _ => (s"ERROR 1: ${res}", JsNull)
                }
                case Failure(e) => (s"ERROR 2: ${e.getMessage}", JsNull)
                case _ => (s"ERROR 3: ${f}", JsNull)
            }
            case Failure(e) => (s"DB timeout. $e", JsNull)
        }
    }

    def recentPosts(): (String, JsValue) = {
        val postsWithUserID = postsTable.take(10).result
        val resultFuture = db.run(postsWithUserID)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(res) => res.length match {
                    case 0 => ("No posts.", JsNull)
                    case l if l > 0 => ("Found posts.", Json.toJson(res))
                    case _ => (s"ERROR 1: ${res}", JsNull)
                }
                case Failure(e) => (s"ERROR 2: ${e.getMessage}", JsNull)
                case _ => (s"ERROR 3: ${f}", JsNull)
            }
            case Failure(e) => (s"DB timeout. $e", JsNull)
        }
    }


}

