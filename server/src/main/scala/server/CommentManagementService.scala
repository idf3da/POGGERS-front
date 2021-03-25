package server

import slick.jdbc.H2Profile.api._

import java.sql.Timestamp
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}


class CommentManagementService {
    private val db = Database.forConfig("db")
    private val comments = TableQuery[CommentsTable]

    def createComment(newComment: CreateCommentRequest, creatorid: Int): String = {
        val newCommentDB: Comment = Comment(0, newComment.postid, creatorid, newComment.comment, Timestamp.valueOf("1970-01-01 00:00:01"))
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

    def commentsForPost(postid: Int): (String, Seq[CommentsTable#TableElementType]) = {
        val commentsWithPostID = comments.filter(_.postid === postid).result
        val resultFuture = db.run(commentsWithPostID)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(res) => res.length match {
                    case 0 => ("No comments.", res)
                    case l if l > 0 => ("Found comments.", res)
                    case _ => (s"ERROR 1: ${res}", Seq.empty[CommentsTable#TableElementType])
                }
                case Failure(e) => (s"ERROR 2: ${e.getMessage}", Seq.empty[CommentsTable#TableElementType])
                case _ => (s"ERROR 3: ${f}", Seq.empty[CommentsTable#TableElementType])
            }
            case Failure(e) => (s"DB timeout. $e", Seq.empty[CommentsTable#TableElementType])
        }
    }

}

