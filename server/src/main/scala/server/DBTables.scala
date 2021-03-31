package server

import slick.jdbc.H2Profile.api._

import java.sql.Timestamp

// For POST requests
case class RegisterUserRequest (
                                       username: String,
                                       password: String,
                                       email: String
                               )

// For DB interaction
case class RegisterUserRequestDB (
                                        userid: Int,
                                       username: String,
                                       password: String,
                                       email: String
                               )

case class LoginRequest(
                               username: String,
                               password: String
                       )

case class CreatePostRequest (
                                     title: String,
                                     descriptorid: String,
                                     description: String
                             )

case class CreatePostRequestDB (
                                     creatorid: Int,
                                     title: String,
                                     descriptorid: String,
                                     description: String
                             )

case class CreateCommentRequest (
                                    postid: Int,
                                    comment: String,
                                )

case class CreateCommentRequestDB (
                                        postid: Int,
                                        userid: Int,
                                        comment: String,
                                )

case class Comment (
                       commentid: Int,
                       postid: Int,
                       userid: Int,
                       comment: String,
                       updated_at: Timestamp
                       )

class UsersTable(tag: Tag) extends Table[RegisterUserRequest](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (username, password, email).mapTo[RegisterUserRequest]
}

class UsersTableDB(tag: Tag) extends Table[RegisterUserRequestDB](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (userid, username, password, email).mapTo[RegisterUserRequestDB]
}

class PostsTable(tag: Tag) extends Table[CreatePostRequestDB](tag,"posts") {
    def postid = column[Int]("postid", O.PrimaryKey, O.AutoInc)
    def creatorid = column[Int]("creatorid")
    def title  = column[String]("title")
    def descriptorid = column[String]("descriptorid")
    def created_at = column[Timestamp]("created_at")
    def updated_at = column[Timestamp]("updated_at")
    def description = column[String]("description")
    def * = (creatorid, title, descriptorid, description).mapTo[CreatePostRequestDB]
}

class CommentsTable(tag: Tag) extends Table[Comment](tag,"comments") {
    def commentid = column[Int]("commentid", O.PrimaryKey, O.AutoInc)
    def postid = column[Int]("postid")
    def userid = column[Int]("userid")
    def comment = column[String]("comment")
    def updated_at = column[Timestamp]("date")
    def * = (commentid, postid, userid, comment, updated_at).mapTo[Comment]
}

