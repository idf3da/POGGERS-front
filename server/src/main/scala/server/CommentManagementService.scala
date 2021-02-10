package server

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class CommentManagementService {
    private val db = Database.forConfig("db")
    private val comments = TableQuery[CommentsTable]

    def createComment(newComment: CreateCommentRequest, creatorid: Int): String = {
        val newCommentDB: CreateCommentRequestDB = CreateCommentRequestDB(newComment.postid, creatorid, newComment.comment)
        val insertPostQuery = comments += newCommentDB
        val resultFuture: Future[Int] = db.run(insertPostQuery)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(_) => "Comment created."
                case Failure(e) => s"ERROR: ${e.getMessage}"
            }
            case Failure(e) => s"DB timeout. $e"
        }
    }
}

