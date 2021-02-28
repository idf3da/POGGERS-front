package server

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class PostManagementService {
    private val db = Database.forConfig("db")
    private val postsTable = TableQuery[PostsTable]

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


}

