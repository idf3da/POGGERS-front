package server

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class PostManagementService {
    private val db = Database.forConfig("db")
    private val posts = TableQuery[PostsTable]

    def createPost(newPost: CreatePostRequest): String = {
        val insertPostQuery = posts += newPost
        val resultFuture: Future[Int] = db.run(insertPostQuery)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(_) => "Post created."
                case Failure(e) => s"ERROR: ${e.getMessage}"
            }
            case Failure(e) => s"DB timeout. ${e}"
        }
    }
}

